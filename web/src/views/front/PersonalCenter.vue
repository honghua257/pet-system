<template>
  <div class="front-personal-center front-content" :key="activeName">
    <el-tabs v-model="activeName" tab-position="left">
        <el-tab-pane label="临时寄养订单" name="petFosterCare">
          <PetFosterCare v-show="activeName === 'petFosterCare'" :key="'petFosterCare-' + activeName"></PetFosterCare>
        </el-tab-pane>
        <el-tab-pane label="上门喂养订单" name="petFeed">
          <PetFeed v-show="activeName === 'petFeed'" :key="'petFeed-' + activeName"></PetFeed>
        </el-tab-pane>
        <el-tab-pane label="收货地址" name="shippingAddress">
          <ShippingAddress v-if="activeName === 'shippingAddress'"></ShippingAddress>
        </el-tab-pane>
        <el-tab-pane label="商城订单" name="productOrder">
          <ProductOrder v-if="activeName === 'productOrder'"></ProductOrder>
        </el-tab-pane>
      </el-tabs>
  </div>
</template>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";
@import "@/styles/module-common.css";

/* 个人中心页面特定样式 */
.front-personal-center {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
  padding: 0;
  background: var(--bg-primary);
  border-radius: 20px;
  box-shadow: 0 8px 24px var(--shadow-neutral);
  overflow: hidden;
  border: 1px solid var(--gray-200);
  min-height: 600px;
}

/* 标签页样式增强 */
.front-personal-center :deep(.el-card__body) {
  padding: 0;
}

/* 标签页容器 */
.front-personal-center :deep(.el-tabs) {
  display: flex;
  min-height: 600px;
  height: auto;
  width: 100%;
  min-width: 800px;
}

.front-personal-center :deep(.el-tabs__header) {
  background: var(--bg-primary);
  border-radius: 20px 0 0 20px;
  padding: 24px 16px;
  margin: 0;
  box-shadow: none;
  border-right: 1px solid var(--gray-200);
  width: 220px;
  min-width: 180px;
  flex-shrink: 0;
}

.front-personal-center :deep(.el-tabs__nav-wrap) {
  padding: 0 20px;
}

.front-personal-center :deep(.el-tabs__nav) {
  border: none;
}

.front-personal-center :deep(.el-tabs__item) {
  border: none;
  padding: 12px 16px;
  margin: 4px 0;
  border-radius: 12px;
  color: var(--text-secondary);
  font-weight: 500;
  transition: all 0.3s ease;
  display: block;
  width: 100%;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.front-personal-center :deep(.el-tabs__item:hover) {
  background: var(--bg-accent);
  color: var(--primary-orange);
  transform: translateX(4px);
}

.front-personal-center :deep(.el-tabs__item.is-active) {
  background: var(--gradient-primary);
  color: white;
  box-shadow: 0 4px 12px var(--shadow-primary);
  font-weight: 600;
}

.front-personal-center :deep(.el-tabs__content) {
  padding: 32px;
  background: var(--bg-primary);
  border-radius: 0 20px 20px 0;
  margin-left: 0;
  box-shadow: none;
  overflow: visible;
  min-height: 500px;
  flex: 1;
  width: calc(100% - 220px);
  height: auto;
  min-width: 580px;
}

/* 隐藏左侧标签的蓝色激活指示条 */
.front-personal-center :deep(.el-tabs__active-bar) {
  display: none !important;
}

/* 标签面板内容样式 */
.front-personal-center :deep(.el-tab-pane) {
  width: 100%;
  min-height: 100%;
  height: auto;
  overflow: visible;
  min-width: 100%;
}

/* 确保子组件有正确的间距 */
.front-personal-center :deep(.el-tab-pane > *) {
  margin-bottom: 24px;
}

.front-personal-center :deep(.el-tab-pane > *:last-child) {
  margin-bottom: 0;
}

/* 空状态优化 */
.front-personal-center :deep(.el-empty) {
  background: var(--bg-secondary);
  border-radius: 16px;
  padding: 40px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .front-personal-center :deep(.el-tabs) {
    flex-direction: column;
    height: auto;
    min-height: auto;
  }

  .front-personal-center :deep(.el-tabs__header) {
    width: 100%;
    border-radius: 20px 20px 0 0;
    border-right: none;
    border-bottom: 1px solid var(--gray-200);
    order: 2;
  }

  .front-personal-center :deep(.el-tabs__content) {
    width: 100%;
    min-width: auto;
    order: 1;
    border-radius: 0 0 20px 20px;
  }
}

/* 缩放适配 */
@media (min-resolution: 120dpi) {
  .front-personal-center {
    max-width: 1600px;
  }
}

@media (min-resolution: 144dpi) {
  .front-personal-center {
    max-width: 1800px;
  }
}

/* 全局缩放适配 */
.front-personal-center {
  container-type: inline-size;
}

@container (min-width: 1200px) {
  .front-personal-center {
    max-width: 1400px;
  }
}

@container (min-width: 1600px) {
  .front-personal-center {
    max-width: 1800px;
  }
}

@container (min-width: 2000px) {
  .front-personal-center {
    max-width: 2200px;
  }
}

@media (max-width: 768px) {
  .front-personal-center {
    padding: 16px;
    border-radius: 16px;
    min-height: auto;
  }

  .front-personal-center :deep(.el-tabs__header) {
    padding: 16px;
  }

  .front-personal-center :deep(.el-tabs__content) {
    padding: 20px;
  }

  .front-personal-center :deep(.el-tabs__item) {
    padding: 10px 16px;
    margin: 2px 0;
    font-size: 14px;
  }

  .front-personal-center :deep(.el-tabs__nav-wrap) {
    padding: 0 16px;
  }
}

@media (max-width: 480px) {
  .front-personal-center {
    padding: 8px;
    border-radius: 12px;
  }

  .front-personal-center :deep(.el-tabs__header) {
    padding: 12px;
  }

  .front-personal-center :deep(.el-tabs__content) {
    padding: 16px;
  }

  .front-personal-center :deep(.el-tabs__item) {
    padding: 8px 12px;
    margin: 1px 0;
    font-size: 13px;
  }

  .front-personal-center :deep(.el-tabs__nav-wrap) {
    padding: 0 12px;
  }
}

@media (max-width: 768px) {
  .front-personal-center {
    padding: 16px;
  }

  .personal-card {
    border-radius: 16px;
    min-height: 500px;
  }

  .front-personal-center :deep(.el-tabs__header) {
    padding: 12px 0;
  }

  .front-personal-center :deep(.el-tabs__content) {
    padding: 16px;
    max-height: 500px;
    min-height: 400px;
  }

  .front-personal-center :deep(.el-tabs__item) {
    padding: 8px 16px;
    margin: 2px 8px;
    font-size: 14px;
  }

  .front-personal-center :deep(.el-tabs__nav-wrap) {
    padding: 0 12px;
  }
}

@media (max-width: 480px) {
  .front-personal-center {
    padding: 8px;
  }

  .personal-card {
    border-radius: 12px;
    min-height: 400px;
  }

  .front-personal-center :deep(.el-tabs__content) {
    padding: 12px;
    max-height: 400px;
    min-height: 350px;
  }

  .front-personal-center :deep(.el-tabs__item) {
    padding: 6px 12px;
    margin: 1px 4px;
    font-size: 13px;
  }

  .front-personal-center :deep(.el-tabs__nav-wrap) {
    padding: 0 8px;
  }
}
</style>

<script setup>

import {ref} from "vue";
import {useRoute} from "vue-router";
import PetFosterCare from "@/views/front/personalCenter/PetFosterCare.vue";
import PetFeed from "@/views/front/personalCenter/PetFeed.vue";
import ShippingAddress from "@/views/front/personalCenter/ShippingAddress.vue";
import ProductOrder from "@/views/front/personalCenter/ProductOrder.vue";

const activeName = ref('petFosterCare')
const route = useRoute()
if(route.query.type){
  activeName.value = route.query.type
}
</script>
