<template>
  <div class="front-index">
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card shadow="hover" class="pet-card front-card front-fade-in front-stagger-1">
            <template #header>
              <div class="card-header-flex">
                <div class="card-title">
                  <span class="card-icon">🏪</span>
                  <span class="card-title-text">宠物店铺</span>
                </div>
                <el-button type="primary" link @click="router.push('/petStore')">更多</el-button>
              </div>
            </template>

            <el-skeleton :loading="loading" animated>
              <template #default>
                <el-row :gutter="10">
                  <el-col :span="12">
                    <div v-if="imageStores.length" class="img-card large clickable-card" @click="router.push('/petStore')">
                      <el-image :src="getStoreCover(imageStores[0],0)" fit="cover" style="width:100%;height:260px"/>
                      <div class="img-title">{{ imageStores[0]?.storeName || '宠物店' }}</div>
                    </div>
                    <div v-else class="empty-state">
                      <el-empty description="暂无店铺数据" :image-size="80"/>
                    </div>
                    <div class="extra-stores-list">
                      <div v-for="(s, i) in extraStores" :key="'extra-'+i" class="extra-store-item">
                        <span class="clickable-text" @click="router.push('/petStore')">{{ s.storeName }}</span>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <el-row :gutter="10">
                      <el-col :span="12" v-for="(item, index) in imageStores.slice(1)" :key="'thumb-'+index" class="small-img-col">
                        <div class="img-card clickable-card" @click="router.push('/petStore')">
                          <el-image :src="getStoreCover(item,index+1)" fit="cover" style="width:100%;height:125px"/>
                          <div class="img-title">{{ item.storeName }}</div>
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </template>
              <template #skeleton>
              </template>
            </el-skeleton>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card shadow="hover" class="pet-card front-card front-fade-in front-stagger-2">
            <template #header>
              <div class="card-header-flex">
                <div class="card-title">
                  <span class="card-icon">🆘</span>
                  <span class="card-title-text">求助信息</span>
                </div>
                <el-button type="primary" link @click="router.push('/helpMessage')">更多</el-button>
              </div>
            </template>

            <el-skeleton :loading="loading" animated>
              <template #default>
                <div class="help-message-container hide-scrollbar">
                  <el-timeline v-if="helpList.length">
                    <el-timeline-item
                        v-for="item in helpList"
                        :key="item.id"
                        :timestamp="item.createTime"
                        placement="top">
                      <el-link type="primary" :underline="false" @click="goToDetails('helpMessageDetails', item.id)">{{ item.title }}</el-link>
                      <div class="timeline-username">{{ item.username }}</div>
                    </el-timeline-item>
                  </el-timeline>
                  <el-empty v-else description="暂无求助信息" :image-size="80"/>
                </div>
              </template>
              <template #skeleton>
              </template>
            </el-skeleton>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="16">
          <el-card shadow="hover" class="pet-card front-card front-fade-in front-stagger-3">
            <template #header>
              <div class="card-header-flex">
                <div class="card-title">
                  <span class="card-icon">🛍️</span>
                  <span class="card-title-text">宠物商城</span>
                </div>
                <el-button type="primary" link @click="router.push('/product')">更多</el-button>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :span="8" v-for="item in products" :key="item.id" class="product-col">
                <el-card shadow="hover" class="clickable-card" @click="goToDetails('productDetails', item.id)">
                  <div class="product-img-wrapper">
                    <el-image :src="item.mainImg" style="width: 100%; height: 160px;" fit="contain" />
                  </div>
                  <div class="product-name">{{ item.name }}</div>
                  <div class="product-info">
                    <div class="product-price">¥ {{ item.price }}</div>
                    <div>已售 {{ item.salesVolume }}</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="pet-card front-card front-fade-in front-stagger-4">
            <template #header>
              <div class="card-header-flex">
                <div class="card-title">
                  <span class="card-icon">📔</span>
                  <span class="card-title-text">宠物日记</span>
                </div>
                <el-button type="primary" link @click="router.push('/petDiary')">更多</el-button>
              </div>
            </template>
            <div class="diary-container hide-scrollbar">
              <div v-for="item in diaries" :key="item.id" class="diary-item">
                <el-icon class="diary-icon"><Document/></el-icon>
                <el-link type="primary" :underline="false" @click="goToDetails('petDiaryDetails', item.id)">{{ item.title }}</el-link>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-space>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import router from "@/router/index.js";
import {ref, computed} from "vue";
import {Document} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";

const stores = ref([])
const helpList = ref([])
const products = ref([])
const diaries = ref([])
const loading = ref(false)

getHomeData()

async function getHomeData(){
  loading.value = true
  try {
    const promises = [
      request.get('/petStoreManager/page', { params: { pageNum: 1, pageSize: 7 }}),
      request.get('/helpMessage/page', { params: { pageNum: 1, pageSize: 7 }}),
      request.get('/product/page', { params: { pageNum: 1, pageSize: 8 }}),
      request.get('/petDiary/page', { params: { pageNum: 1, pageSize: 8 }})
    ]
    const [storesRes, helpRes, productsRes, diariesRes] = await Promise.all(promises)
    stores.value = storesRes.data?.list || []
    helpList.value = helpRes.data?.list || []
    products.value = productsRes.data?.list || []
    diaries.value = diariesRes.data?.list || []
  } catch (error) {
    console.error('获取首页数据失败:', error)
    ElMessage.error('获取数据失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

function getStoreCover(row, idx){
  const candidates = [row?.storeImg, row?.avatarUrl, row?.cover, row?.coverImg, row?.img, row?.imageUrl, row?.avatar, row?.logo]
  const found = candidates?.find(Boolean)
  if(found){ return found }
  const localFallbacks = [
    '/src/assets/logo.png',
    '/src/assets/login.png',
    '/src/assets/index2.png'
  ]
  return localFallbacks[idx % localFallbacks.length]
}

const imageStores = computed(()=> (stores.value || []).slice(0,5))
const extraStores = computed(()=> (stores.value || []).slice(5, 7))

// 通用详情页跳转函数
function goToDetails(pathPrefix, id) {
  if (!id) return;
  router.push(`/${pathPrefix}/${id}`);
}
</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

/* --- 通用类 --- */
.clickable-card, .clickable-text {
  cursor: pointer;
}
.hide-scrollbar {
  scroll-behavior: smooth;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none;  /* IE and Edge */
}
.hide-scrollbar::-webkit-scrollbar {
  display: none; /* Chrome, Safari, and Opera */
}


/* --- 首页布局 --- */
.front-index {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* --- 卡片头部 --- */
.card-header-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-title-text {
  font-weight: bold;
}


/* --- 宠物店铺 --- */
.extra-stores-list {
  margin-top: 10px;
}
.extra-store-item {
  display: flex;
  align-items: center;
  color: #666;
  margin-top: 8px;
}
.small-img-col:nth-child(n + 3) {
  margin-top: 10px;
}
.img-card {
  position: relative;
  overflow: hidden;
  border-radius: 16px;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  background: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}
.img-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 16px 32px rgba(0, 0, 0, 0.15);
}
.img-card.large{
  height: 260px;
}
.img-title{
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: #fff;
  padding: 12px 16px;
  font-weight: 600;
  transform: translateY(100%);
  transition: transform 0.3s ease;
}
.img-card:hover .img-title {
  transform: translateY(0);
}


/* --- 求助信息 --- */
.help-message-container {
  max-height: 293px;
  overflow-y: auto;
  padding: 0 8px;
  margin: 0 -8px;
}
.timeline-username {
  color: #999;
  font-size: 12px;
  margin-top: 4px;
}


/* --- 宠物商城 --- */
.product-col:nth-child(n + 4) {
  margin-top: 20px;
}
.product-img-wrapper {
  text-align: center;
  margin-bottom: 8px;
}
.product-img-wrapper .el-image {
  border-radius: 6px;
  background: #fafafa;
}
.product-name {
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-info {
  display: flex;
  justify-content: space-between;
  color: #999;
  margin-top: 6px;
}
.product-price {
  color: #e53935;
  font-weight: bold;
}


/* --- 宠物日记 --- */
.diary-container {
  max-height: 267px;
  overflow-y: auto;
  padding: 0 8px;
  margin: 0 -8px;
}
.diary-icon {
  margin-right: 6px;
}


/* --- 原始样式(部分保留和整合) --- */
.pet-card {
  border-radius: 20px;
  border: 1px solid var(--gray-200);
  transition: var(--transition-normal);
  overflow: hidden;
  background: var(--bg-primary);
  box-shadow: 0 4px 16px var(--shadow-neutral);
}
.pet-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 32px var(--shadow-primary);
  border-color: var(--primary-orange-light);
}
.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: var(--text-primary);
  font-size: 1.1rem;
}
.card-icon {
  font-size: 20px;
  animation: pulse-gentle 2s ease-in-out infinite;
  color: var(--primary-orange);
}
.diary-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--gray-200);
  transition: var(--transition-normal);
  border-radius: 8px;
  margin: 0 -8px;
  padding-left: 8px;
  padding-right: 8px;
}
.diary-item:hover {
  transform: translateX(4px);
}
.el-card[shadow="hover"]:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px var(--shadow-primary) !important;
  border-color: var(--primary-orange-light);
}

/* 通用链接样式 */
.help-message-container .el-link,
.diary-container .el-link {
  text-decoration: none !important;
  color: #333;
  font-weight: 500;
  transition: color 0.3s ease;
  border-bottom: none !important;
  --el-link-text-decoration: none !important;
}
.help-message-container .el-link:hover,
.diary-container .el-link:hover {
  color: #409eff;
  text-decoration: none !important;
  --el-link-hover-text-decoration: none !important;
}
.el-timeline {
  padding-left: 0;
}
.el-timeline-item {
  padding-bottom: 16px;
}
.el-timeline-item:last-child {
  padding-bottom: 0;
}
.el-timeline-item__timestamp {
  color: var(--text-secondary);
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .help-message-container,
  .diary-container {
    max-height: 225px;
  }
}

@media (max-width: 768px) {
  .help-message-container,
  .diary-container {
    max-height: 190px;
    padding-right: 4px;
  }
}
</style>