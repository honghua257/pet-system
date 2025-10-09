<template>
  <div class="front-index">
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <!-- 顶部：宠物店铺 + 求助信息 -->
      <el-row :gutter="20">
        <!-- 宠物店铺 -->
        <el-col :span="16">
          <el-card shadow="hover" class="pet-card front-card front-fade-in front-stagger-1">
            <template #header>
              <div style="display:flex;justify-content: space-between;align-items: center">
                <div class="card-title">
                  <span class="card-icon">🏪</span>
                  <span style="font-weight: bold">宠物店铺</span>
                </div>
                <el-button type="primary" link @click="router.push('/petStore')">更多</el-button>
              </div>
            </template>

            <!-- 左大右小图片网格（最多5张图）-->
            <el-skeleton :loading="loading" animated>
              <template #default>
                <el-row :gutter="10">
                  <el-col :span="12">
                    <div v-if="imageStores.length" class="img-card large" @click="router.push('/petStore')" style="cursor:pointer">
                      <el-image :src="getStoreCover(imageStores[0],0)" fit="cover" style="width:100%;height:260px"/>
                      <div class="img-title">{{ imageStores[0]?.storeName || '宠物店' }}</div>
                    </div>
                    <div v-else class="empty-state">
                      <el-empty description="暂无店铺数据" :image-size="80"/>
                    </div>
                    <div style="margin-top:10px">
                      <div v-for="(s, i) in extraStores" :key="'extra-'+i" style="display:flex;align-items:center;color:#666;margin-top:8px">
                        <span style="cursor: pointer" @click="router.push('/petStore')">{{ s.storeName }}</span>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <el-row :gutter="10">
                      <el-col :span="12" v-for="(item, index) in imageStores.slice(1)" :key="'thumb-'+index">
                        <div class="img-card" @click="router.push('/petStore')" style="cursor:pointer">
                          <el-image :src="getStoreCover(item,index+1)" fit="cover" style="width:100%;height:125px"/>
                          <div class="img-title">{{ item.storeName }}</div>
                        </div>
                        <div v-if="index>1" style="height:0;margin-top:10px"></div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </template>
              <template #skeleton>
                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-skeleton-item variant="image" style="width:100%;height:260px;border-radius:12px"/>
                    <div style="margin-top:10px">
                      <el-skeleton-item variant="text" style="width:80%;margin-top:8px"/>
                      <el-skeleton-item variant="text" style="width:60%;margin-top:8px"/>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <el-row :gutter="10">
                      <el-col :span="12" v-for="index in 4" :key="'skeleton-'+index">
                        <el-skeleton-item variant="image" style="width:100%;height:125px;border-radius:12px"/>
                        <div v-if="index>1" style="height:0;margin-top:10px"></div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </template>
            </el-skeleton>
          </el-card>
        </el-col>

        <!-- 求助信息 -->
        <el-col :span="8">
          <el-card shadow="hover" class="pet-card front-card front-fade-in front-stagger-2">
            <template #header>
              <div style="display:flex;justify-content: space-between;align-items: center">
                <div class="card-title">
                  <span class="card-icon">🆘</span>
                  <span style="font-weight: bold">求助信息</span>
                </div>
                <el-button type="primary" link @click="router.push('/helpMessage')">更多</el-button>
              </div>
            </template>

            <el-skeleton :loading="loading" animated>
              <template #default>
                <el-timeline v-if="helpList.length">
                  <el-timeline-item
                      v-for="item in helpList"
                      :key="item.id"
                      :timestamp="item.createTime"
                      placement="top">
                    <el-link type="primary" @click="goHelpDetails(item)">{{ item.title }}</el-link>
                    <div style="color:#999;font-size:12px;margin-top:4px">{{ item.username }}</div>
                  </el-timeline-item>
                </el-timeline>
                <el-empty v-else description="暂无求助信息" :image-size="80"/>
              </template>
              <template #skeleton>
                <el-timeline>
                  <el-timeline-item v-for="index in 5" :key="'help-skeleton-'+index">
                    <el-skeleton-item variant="text" style="width:90%;margin-bottom:8px"/>
                    <el-skeleton-item variant="text" style="width:40%"/>
                  </el-timeline-item>
                </el-timeline>
              </template>
            </el-skeleton>
          </el-card>
        </el-col>
      </el-row>

      <!-- 中下部：左商城 + 右日记列表 -->
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card shadow="hover" class="pet-card front-card front-fade-in front-stagger-3">
            <template #header>
              <div style="display:flex;justify-content: space-between;align-items: center">
                <div class="card-title">
                  <span class="card-icon">🛍️</span>
                  <span style="font-weight: bold">宠物商城</span>
                </div>
                <el-button type="primary" link @click="router.push('/product')">更多</el-button>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :span="8" v-for="(item, index) in products" :key="item.id">
                <el-card shadow="hover" :style="index>3?{marginTop:'10px'}:null" @click="goProductDetails(item)" style="cursor: pointer">
                  <div style="text-align:center;margin-bottom:8px">
                    <el-image :src="item.mainImg" style="width: 100%; height: 160px; border-radius: 6px; background:#fafafa" fit="contain" />
                  </div>
                  <div style="font-weight:600;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ item.name }}</div>
                  <div style="display:flex;justify-content: space-between;color:#999;margin-top:6px">
                    <div style="color:#e53935;font-weight:bold">¥ {{ item.price }}</div>
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
              <div style="display:flex;justify-content: space-between;align-items: center">
                <div class="card-title">
                  <span class="card-icon">📔</span>
                  <span style="font-weight: bold">宠物日记</span>
                </div>
                <el-button type="primary" link @click="router.push('/petDiary')">更多</el-button>
              </div>
            </template>
            <div>
              <div v-for="item in diaries" :key="item.id" class="diary-item">
                <el-icon style="margin-right:6px"><Document/></el-icon>
                <el-link type="primary" @click="goDiaryDetails(item)">{{ item.title }}</el-link>
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
      // 店铺 - 需要至少 5 张图片 + 最多 2 条文字，取 7 条以满足首页展示
      request.get('/petStoreManager/page', { params: { pageNum: 1, pageSize: 7 }}),
      // 求助
      request.get('/helpMessage/page', { params: { pageNum: 1, pageSize: 5 }}),
      // 商品
      request.get('/product/page', { params: { pageNum: 1, pageSize: 8 }}),
      // 日记
      request.get('/petDiary/page', { params: { pageNum: 1, pageSize: 4 }})
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
  // 后端可能没有图片字段，尝试常见字段并提供本地兜底
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

// 计算属性：最多显示5张图片，多余显示至多2条文字
const imageStores = computed(()=>{
  // 取前5条作为图片区
  return (stores.value || []).slice(0,5)
})
const extraStores = computed(()=>{
  const rest = (stores.value || []).slice(5)
  return rest.slice(0,2)
})

function goHelpDetails(row){
  router.push(`/helpMessageDetails/${row.id}`)
}
function goProductDetails(row){
  router.push(`/productDetails/${row.id}`)
}
function goDiaryDetails(row){
  router.push(`/petDiaryDetails/${row.id}`)
}
</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

.diary-content{
  max-height: 120px;
  overflow: hidden;
}

/* 首页容器样式 */
.front-index {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 图片卡片样式增强 */
.img-card {
  position: relative;
  overflow: hidden;
  border-radius: 16px;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  cursor: pointer;
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

/* 宠物主题卡片样式 */
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
  background: var(--bg-secondary);
  transform: translateX(4px);
}

/* 日记链接样式 */
.diary-item .el-link {
  color: var(--text-primary);
  font-weight: 500;
  transition: all 0.3s ease;
}

.diary-item .el-link:hover {
  color: var(--accent-cyan);
}

/* 商品卡片优化 */
.el-card[shadow="hover"] {
  transition: var(--transition-normal);
  background: var(--bg-primary);
  border: 1px solid var(--gray-200);
}

.el-card[shadow="hover"]:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px var(--shadow-primary) !important;
  border-color: var(--primary-orange-light);
}

/* 商品价格样式 */
.text-primary {
  color: var(--primary-orange);
  font-weight: 700;
}

/* 求助信息时间线样式 */
.el-timeline {
  padding-left: 0;
}

.el-timeline-item__timestamp {
  color: var(--text-secondary);
  font-size: 12px;
}
</style>
