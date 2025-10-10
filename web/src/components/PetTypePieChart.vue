<template>
  <el-card class="pet-type-chart-card" shadow="hover">
    <template #header>
      <div class="chart-header">
        <h3 class="chart-title">宠物类型分布</h3>
        <div class="chart-controls">
          <el-button @click="refreshChart" :icon="Refresh" size="small" :loading="loading">
            刷新
          </el-button>
        </div>
      </div>
    </template>

    <div class="chart-container" v-loading="loading">
      <div ref="chartContainer" class="chart-wrapper"></div>
      <div v-if="noData" class="no-data">
        <el-empty description="暂无宠物数据" />
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/http.js'
import { ElMessage } from 'element-plus'
import router from '@/router/index.js'

// Props
const props = defineProps({
  height: {
    type: String,
    default: '400px'
  }
})

// 响应式数据
const chartContainer = ref(null)
const loading = ref(false)
const noData = ref(false)
let chartInstance = null

// 组件挂载
onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

// 组件卸载
onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
  }
  window.removeEventListener('resize', handleResize)
})

// 初始化图表
const initChart = () => {
  nextTick(() => {
    if (chartContainer.value) {
      chartInstance = echarts.init(chartContainer.value)
      loadChartData()
    }
  })
}

// 加载图表数据
const loadChartData = async () => {
  loading.value = true
  try {
    const response = await request.get('/admin/pet-type/distribution')

    if (response.data && response.data.length > 0) {
      noData.value = false
      updateChart(response.data)
    } else {
      noData.value = true
    }
  } catch (error) {
    console.error('加载宠物类型分布数据失败:', error)
    ElMessage.error('加载宠物类型分布数据失败')
    noData.value = true
  } finally {
    loading.value = false
  }
}

// 更新图表
const updateChart = (data) => {
  if (!chartInstance) return

  // 准备图表数据
  const chartData = data.map(item => ({
    name: item.pet_type_name,
    value: item.pet_count,
    typeId: item.pet_type_id,
    otherTypes: item.other_types // "其他"类别包含的类型
  }))

  // 计算总数和百分比
  const total = chartData.reduce((sum, item) => sum + item.value, 0)
  const chartDataWithPercent = chartData.map(item => ({
    ...item,
    percent: total > 0 ? ((item.value / total) * 100).toFixed(1) : 0
  }))

  const option = {
    title: {
      text: '宠物类型分布',
      left: 'center',
      top: 10,
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#303133'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        const data = params.data
        let tooltipText = `
          <div style="font-weight: bold; margin-bottom: 4px;">${data.name}</div>
          <div style="display: flex; align-items: center;">
            <span style="display: inline-block; width: 10px; height: 10px; background-color: ${params.color}; border-radius: 50%; margin-right: 8px;"></span>
            <span>数量: ${data.value}只 (${data.percent}%)</span>
          </div>
        `

        // 如果是"其他"类别，显示包含的类型
        if (data.name === '其他' && data.otherTypes && data.otherTypes.length > 0) {
          tooltipText += `
            <div style="margin-top: 4px; font-size: 12px; color: #909399;">
              包含: ${data.otherTypes.join(', ')}
            </div>
          `
        }

        return tooltipText
      }
    },
    legend: {
      orient: 'vertical',
      right: '10%',
      top: 'center',
      textStyle: {
        color: '#606266'
      },
      formatter: function(name) {
        const item = chartDataWithPercent.find(d => d.name === name)
        return item ? `${name} (${item.percent}%)` : name
      }
    },
    series: [
      {
        name: '宠物类型',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['40%', '55%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold',
            formatter: '{b}\n{c}只\n({d}%)'
          },
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        labelLine: {
          show: false
        },
        data: chartDataWithPercent
      }
    ],
    color: [
      '#FF6B6B', // 红色
      '#4ECDC4', // 青色
      '#45B7D1', // 蓝色
      '#96CEB4', // 绿色
      '#FFEAA7', // 黄色
      '#DDA0DD', // 梅花色
      '#FFA07A', // 浅橙红
      '#87CEEB', // 天蓝色
      '#98D8C8', // 薄荷绿
      '#F7DC6F'  // 金黄色
    ],
    animation: true,
    animationDuration: 1000,
    animationEasing: 'cubicOut'
  }

  // 点击事件
  chartInstance.off('click')
  chartInstance.on('click', function(params) {
    const typeId = params.data.typeId
    if (typeId && typeId !== -1) {
      // 跳转到对应宠物类型的管理页面
      router.push('/admin/pet?typeId=' + typeId)
    } else if (typeId === -1) {
      // 点击"其他"类别，显示包含的类型详情
      ElMessage.info(`其他类别包含: ${params.data.otherTypes ? params.data.otherTypes.join(', ') : '无'}`)
    }
  })

  chartInstance.setOption(option, true)
}

// 刷新图表
const refreshChart = () => {
  loadChartData()
}

// 处理窗口大小变化
const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

// 暴露方法给父组件
defineExpose({
  refreshChart,
  loadChartData
})
</script>

<style scoped>
.pet-type-chart-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-title {
  margin: 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.chart-controls {
  display: flex;
  align-items: center;
  gap: 12px;
}

.chart-container {
  padding: 10px;
  position: relative;
}

.chart-wrapper {
  width: 100%;
  height: v-bind(height);
  min-height: 300px;
}

.no-data {
  display: flex;
  justify-content: center;
  align-items: center;
  height: v-bind(height);
  min-height: 300px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .chart-controls {
    width: 100%;
    justify-content: space-between;
  }

  .chart-wrapper {
    height: 300px;
  }

  .no-data {
    height: 300px;
  }
}
</style>