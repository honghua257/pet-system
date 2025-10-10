<template>
  <div class="front-product-list front-content">
    <div class="front-section">
        <div class="front-section-header">
          <div class="front-section-title">
            <span class="front-section-icon">🛍️</span>
            <span>宠物商城</span>
          </div>
        </div>
        <div class="search-container">
          <el-space style="align-items: center">
            <el-input v-model="searchForm.name" placeholder="请输入关键字搜索" clearable @keyup.enter="search" style="width: 300px"/>
            <el-button type="primary" :icon="Search" @click="search" class="search-btn-capsule">搜索</el-button>
          </el-space>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="6" v-for="(item, index) in listData" :key="item.id">
          <el-card shadow="hover" :style="index>3?{marginTop:'10px'}:null" @click="goDetails(item)" style="cursor: pointer">
            <template #header>
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <div style="font-weight: bold; font-size: 16px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 80%">{{ item.name }}</div>
              </div>
            </template>

            <div style="text-align: center; margin-bottom: 10px">
              <el-image :src="item.mainImg" style="width: 100%; height: 200px; border-radius: 8px" fit="cover"/>
            </div>

            <div style="display:flex; justify-content: space-between; align-items: center">
              <div class="price-tag">¥ {{ item.price }}</div>
              <div class="sales-info">已售 {{ item.salesVolume }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="pagination-card">
        <el-pagination
            @current-change="currentChange"
            @size-change="sizeChange"
            :page-size="pageInfo.pageSize"
            :current-page="pageInfo.pageNum"
            background
            layout="total, sizes, prev, pager, next"
            :total="pageInfo.total"
            class="unified-pagination"/>
      </el-card>
  </div>
</template>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";
@import "@/styles/module-common.css";

/* 宠物商城页面特定样式 */
.front-product-list {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 搜索容器样式 */
.search-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 胶囊形状搜索按钮样式 */
.search-btn-capsule {
  background: var(--gradient-primary);
  border: none;
  border-radius: 25px;
  color: white;
  font-weight: 600;
  padding: 12px 28px;
  transition: all 0.3s ease;
  box-shadow: 0 3px 10px var(--shadow-primary);
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  min-width: 100px;
  justify-content: center;
  white-space: nowrap;
}

.search-btn-capsule:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px var(--shadow-primary);
  background: linear-gradient(135deg, #ff8c42, #ff6b35);
}

.search-btn-capsule:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px var(--shadow-primary);
}

.search-btn-capsule .el-icon {
  font-size: 16px;
}

/* 价格标签 */
.price-tag {
  color: var(--primary-orange);
  font-weight: 700;
  font-size: 1.1rem;
}

/* 销售信息 */
.sales-info {
  color: var(--text-muted);
  font-size: 12px;
}

/* 商品卡片增强 */
.front-product-list .el-card {
  background: var(--bg-primary);
  border: 1px solid var(--gray-200);
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

.front-product-list .el-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 32px var(--shadow-primary);
  border-color: var(--primary-orange-light);
}

.front-product-list .el-image {
  border-radius: 12px;
  transition: transform 0.3s ease;
}

.front-product-list .el-image:hover {
  transform: scale(1.05);
}

/* 统一分页组件样式 */
.pagination-card {
  margin-top: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 16px var(--shadow-neutral);
  border: 1px solid var(--gray-200);
  background: var(--bg-primary);
}

.unified-pagination {
  --el-pagination-button-bg-color: var(--bg-primary);
  --el-pagination-hover-color: var(--primary-orange);
  padding: 20px;
  display: flex;
  justify-content: center;
}

.unified-pagination :deep(.el-pager li.is-active) {
  background: var(--gradient-primary);
  color: white;
  border-radius: 50%;
  font-weight: 600;
}

.unified-pagination :deep(.el-pager li:hover) {
  color: var(--primary-orange);
}

.unified-pagination :deep(.el-pagination__total) {
  color: var(--text-primary);
  font-weight: 500;
}

.unified-pagination :deep(.el-pagination__sizes .el-select .el-input) {
  border-radius: 8px;
}

.unified-pagination :deep(.el-pagination__jump) {
  margin-left: 16px;
}
</style>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw} from "vue";
import {Search} from "@element-plus/icons-vue";
import router from "@/router/index.js";

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 12, total: 0 })
const searchForm = ref({ name: undefined })

getPageList()

function getPageList() {
  const data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/product/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
function sizeChange(e) { pageInfo.value.pageSize = e; pageInfo.value.pageNum = 1; getPageList() }
function search() { pageInfo.value.pageNum = 1; getPageList() }

function goDetails(row) {
  router.push(`/productDetails/${row.id}`)
}
</script>


