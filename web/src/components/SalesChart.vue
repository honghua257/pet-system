<template>
  <el-card class="sales-chart-card" shadow="hover">
    <template #header>
      <div class="chart-header">
        <h3 class="chart-title">{{ chartTitle }}</h3>
        <div class="chart-controls">
          <el-radio-group v-model="selectedDays" @change="onDaysChange" size="small">
            <el-radio-button :label="3">3天</el-radio-button>
            <el-radio-button :label="7">7天</el-radio-button>
            <el-radio-button :label="15">15天</el-radio-button>
            <el-radio-button :label="30">30天</el-radio-button>
          </el-radio-group>
          <el-button @click="refreshChart" :icon="Refresh" size="small" :loading="loading">
            刷新
          </el-button>
        </div>
      </div>
    </template>

    <div class="chart-container" v-loading="loading">
      <div ref="chartContainer" class="chart-wrapper"></div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/http.js'
import { ElMessage } from 'element-plus'

// Props
const props = defineProps({
  height: {
    type: String,
    default: '400px'
  }
})

// 响应式数据
const chartContainer = ref(null)
const selectedDays = ref(7)
const chartTitle = ref('最近7天销售额趋势')
const loading = ref(false)
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
    const response = await request.get('/admin/sales/chart', {
      params: { days: selectedDays.value }
    })

    if (response.data) {
      updateChart(response.data)
    }
  } catch (error) {
    console.error('加载销售数据失败:', error)
    ElMessage.error('加载销售数据失败')
  } finally {
    loading.value = false
  }
}

// 更新图表
const updateChart = (data) => {
  if (!chartInstance) return

  chartTitle.value = data.title || '销售额趋势'

  const option = {
    title: {
      text: chartTitle.value,
      left: 'center',
      top: 10,
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#303133'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        crossStyle: {
          color: '#999'
        }
      },
      formatter: function (params) {
        let tooltipText = `<div style="font-weight: bold;">${params[0].axisValueLabel}</div>`
        params.forEach(param => {
          tooltipText += `
            <div style="display: flex; align-items: center; margin-top: 4px;">
              <span style="display: inline-block; width: 10px; height: 10px; background-color: ${param.color}; border-radius: 50%; margin-right: 8px;"></span>
              <span>${param.seriesName}: ¥${param.value}</span>
            </div>
          `
        })
        return tooltipText
      }
    },
    legend: {
      data: ['销售额', '趋势'],
      top: 40,
      textStyle: {
        color: '#606266'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.dates || [],
      axisPointer: {
        type: 'shadow'
      },
      axisLabel: {
        color: '#606266',
        rotate: 45,
        interval: data.dates && data.dates.length > 15 ? 'auto' : 0,
        fontSize: data.dates && data.dates.length > 15 ? 12 : 14
      },
      axisLine: {
        lineStyle: {
          color: '#DCDFE6'
        }
      }
    },
    yAxis: {
      type: 'value',
      min: 0,
      interval: 100,
      axisLabel: {
        formatter: '¥{value}',
        color: '#606266'
      },
      axisLine: {
        lineStyle: {
          color: '#DCDFE6'
        }
      },
      splitLine: {
        lineStyle: {
          color: '#EBEEF5'
        }
      }
    },
    series: [
      {
        name: '销售额',
        type: 'bar',
        data: data.barData || [],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#ff8c42' },
            { offset: 1, color: '#ff6b35' }
          ]),
          borderRadius: [4, 4, 0, 0]
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(255, 140, 66, 0.5)'
          }
        }
      },
      {
        name: '趋势',
        type: 'line',
        data: data.trendData || [],
        smooth: true,
        lineStyle: {
          color: '#409EFF',
          width: 3
        },
        itemStyle: {
          color: '#409EFF',
          borderColor: '#fff',
          borderWidth: 2
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(64, 158, 255, 0.5)'
          }
        }
      }
    ],
    animation: true,
    animationDuration: 1000,
    animationEasing: 'cubicOut'
  }

  chartInstance.setOption(option, true)
}

// 天数选择改变
const onDaysChange = (days) => {
  selectedDays.value = days
  loadChartData()
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
.sales-chart-card {
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
}

.chart-wrapper {
  width: 100%;
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
}
</style>