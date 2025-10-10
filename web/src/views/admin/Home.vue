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
  background: linear-gradient(135deg, #1a1a1a 0%, #2c3e50 50%, #34495e 100%);
  color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
  border: 1px solid #333;
}

.welcome-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: repeating-linear-gradient(
    45deg,
    transparent,
    transparent 10px,
    rgba(255, 255, 255, 0.05) 10px,
    rgba(255, 255, 255, 0.05) 20px
  );
  pointer-events: none;
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
