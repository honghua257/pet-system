<template>
  <div class="admin-home">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="welcome-section">
          <h2>欢迎回来，{{ currentUser?.username }}</h2>
          <p>这里是数据统计分析页面，您可以查看系统的关键业务指标</p>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <SalesChart height="450px" />
      </el-col>
      <el-col :span="12">
        <PetTypePieChart height="450px" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import tools from '@/utils/tools.js'
import SalesChart from '@/components/SalesChart.vue'
import PetTypePieChart from '@/components/PetTypePieChart.vue'

const currentUser = ref(null)

onMounted(() => {
  currentUser.value = tools.getCurrentUser()
})
</script>

<style scoped>
.admin-home {
  padding: 20px;
}

.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.welcome-section h2 {
  margin: 0 0 10px 0;
  font-size: 24px;
  font-weight: 600;
}

.welcome-section p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.chart-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-card :deep(.el-card__body) {
  padding: 0;
}
</style>
