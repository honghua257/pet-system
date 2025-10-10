<template>
  <div class="front-my-pet front-content">
    <div class="front-section">
      <div class="front-section-header">
        <div class="front-section-title">
          <span class="front-section-icon">🐾</span>
          <span>我的宠物</span>
        </div>
        <el-button type="success" @click="add" :icon="Plus" class="btn-pet-primary">新增宠物</el-button>
      </div>
    </div>

    <div class="pet-list">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(item,index) in listData" :key="item.id">
          <el-card shadow="hover" class="pet-card front-card front-fade-in" :class="'front-stagger-' + ((index % 4) + 1)" :style="index>3 ?{marginTop:'10px'}:null">
            <template #header>
              <div class="pet-card-header">
                <div class="pet-info">
                  <span class="pet-name">{{ item.name }}</span>
                  <el-tag type="primary" size="small" class="pet-type-tag">{{ item.petTypeName }}</el-tag>
                </div>
                <div class="pet-time">
                  {{ item.createTime }}
                </div>
              </div>
            </template>

            <div class="pet-image-container">
              <el-image
                :src="item.mainImg"
                class="pet-image"
                fit="cover"
                :preview-src-list="[]"
                @click="openFullscreen(item.mainImg)"
              />
            </div>

            <div class="pet-actions">
              <el-button type="primary" size="small" @click="feed(item)" class="action-btn feed-btn">
                <span>🍖</span> 上门喂养
              </el-button>
              <el-button type="primary" size="small" @click="fosterCare(item)" class="action-btn foster-btn">
                <span>🏠</span> 临时寄养
              </el-button>
              <el-button type="warning" size="small" @click="edit(index, item)" class="action-btn edit-btn">
                <span>✏️</span> 编辑
              </el-button>
              <el-button type="danger" size="small" @click="deleteOne(index, item)" class="action-btn delete-btn">
                <span>🗑️</span> 删除
              </el-button>
            </div>
          </el-card>
        </el-col>
        </el-row>

      <!-- 分页组件 -->
      <el-card class="pagination-card">
        <el-pagination
            @current-change="currentChange"
            @size-change="sizeChange"
            :page-size="pageInfo.pageSize"
            :current-page="pageInfo.pageNum"
            background
            layout="total, sizes, prev, pager, next"
            :total="pageInfo.total"
            class="unified-pagination">
        </el-pagination>
      </el-card>
    </div>

    <!-- 新增/编辑宠物对话框 -->
    <el-dialog
        v-model="dialogOpen"
        :title="formData.id ? '编辑宠物' : '新增宠物'"
        width="600px"
        @close="closeDialog"
    >
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="宠物名称" prop="name" :rules="[{required: true, message: '请输入宠物名称'}]">
          <el-input v-model="formData.name" placeholder="请输入宠物名称"></el-input>
        </el-form-item>

        <el-form-item label="宠物类型" prop="petTypeId" :rules="[{required: true, message: '请选择宠物类型'}]">
          <el-select v-model="formData.petTypeId" placeholder="请选择宠物类型" style="width: 100%;">
            <el-option
                v-for="item in petTypeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="宠物图片" prop="mainImg" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <MyUpload type="imageCard" :limit="1" :files="formData.mainImg" @setFiles="formData.mainImg = $event" />
        </el-form-item>

        <el-form-item label="宠物描述" prop="basicInfo">
          <MyEditor :content="formData.basicInfo" @content-change="formData.basicInfo=$event" v-if="dialogOpen"></MyEditor>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 上门喂养对话框 -->
    <el-dialog v-model="feedDialogOpen" title="上门喂养服务" width="500px">
      <el-form ref="feedFormRef" :model="feedFormData" label-width="100px">
        <el-form-item label="选择店铺" prop="petStoreManagerId" :rules="[{required: true, message: '请选择店铺'}]">
          <el-select v-model="feedFormData.petStoreManagerId" placeholder="请选择店铺" style="width: 100%;">
            <el-option
                v-for="item in petStoreManagerList"
                :key="item.id"
                :label="item.storeName"
                :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="服务时间" prop="serviceTime" :rules="[{required: true, message: '请选择服务时间'}]">
          <el-date-picker
              v-model="feedFormData.serviceTime"
              type="datetime"
              placeholder="选择服务时间"
              style="width: 100%;"
              value-format="YYYY-MM-DD HH:mm:ss"
              format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
              v-model="feedFormData.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="feedDialogOpen = false">取消</el-button>
        <el-button type="primary" @click="feedSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 临时寄养对话框 -->
    <el-dialog v-model="fosterCareDialogOpen" title="临时寄养服务" width="500px">
      <el-form ref="fosterCareFormRef" :model="fosterCareFormData" label-width="100px">
        <el-form-item label="选择店铺" prop="petStoreManagerId" :rules="[{required: true, message: '请选择店铺'}]">
          <el-select v-model="fosterCareFormData.petStoreManagerId" placeholder="请选择店铺" style="width: 100%;">
            <el-option
                v-for="item in petStoreManagerList"
                :key="item.id"
                :label="item.storeName"
                :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="寄养开始时间" prop="reservedStartTime" :rules="[{required: true, message: '请选择寄养开始时间'}]">
          <el-date-picker
              v-model="fosterCareFormData.reservedStartTime"
              type="datetime"
              placeholder="选择寄养开始时间"
              style="width: 100%;"
              value-format="YYYY-MM-DD HH:mm:ss"
              format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="寄养结束时间" prop="reservedEndTime" :rules="[{required: true, message: '请选择寄养结束时间'}]">
          <el-date-picker
              v-model="fosterCareFormData.reservedEndTime"
              type="datetime"
              placeholder="选择寄养结束时间"
              style="width: 100%;"
              value-format="YYYY-MM-DD HH:mm:ss"
              format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
              v-model="fosterCareFormData.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="fosterCareDialogOpen = false">取消</el-button>
        <el-button type="primary" @click="fosterCareSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 全屏图片预览 -->
    <div
      v-if="showFullscreen"
      class="fullscreen-overlay"
      @click="closeFullscreen"
      @wheel.prevent="handleZoom"
      @touchstart="handleTouchStart"
      @touchmove="handleTouchMove"
      @touchend="handleTouchEnd"
    >
      <div class="fullscreen-container">
        <img
          :src="fullscreenImage"
          class="fullscreen-image"
          :style="imageStyle"
          @click.stop
          @mousedown="handleMouseDown"
          @mousemove="handleMouseMove"
          @mouseup="handleMouseUp"
          @mouseleave="handleMouseUp"
        />
        <button class="close-btn" @click="closeFullscreen">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M18 6L6 18M6 6l12 12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>
        <button class="zoom-in-btn" @click.stop="zoomIn">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
            <path d="M21 21l-4.35-4.35M11 8v6M8 11h6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>
        <button class="zoom-out-btn" @click.stop="zoomOut">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
            <path d="M21 21l-4.35-4.35M8 11h6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>
        <button class="reset-btn" @click.stop="resetZoom">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M1 4v6h6M23 20v-6h-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import request from "@/utils/http.js";
import {Plus} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "@/router/index.js";
import {ref, toRaw, computed, onMounted, onUnmounted} from "vue";
import tools from "@/utils/tools.js";
import MyEditor from "@/components/MyEditor.vue";
import MyUpload from "@/components/MyUpload.vue";

const searchFormComponents = ref();
const tableComponents =ref();
const listData = ref([]);
const pageInfo = ref({
  pageNum:1,
  pageSize:10,
  total:0
});
const searchForm = ref({
  title:undefined,
  onlyYou:false,
});

const userInfo = tools.getCurrentUser()

getPageList()

function getPageList(){
  let data = Object.assign(toRaw(searchForm.value),toRaw(pageInfo.value))
  request.get("/pet/page",{
    params:data
  }).then(res =>{
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e){
  pageInfo.value.pageNum = e
  getPageList()
}

function sizeChange(e){
  pageInfo.value.pageSize = e
  getPageList()
  console.log(e)
}

function search(){
  pageInfo.value.pageNum = 1
  getPageList()
}

function resetSearch(){
  searchFormComponents.value.resetFields();
  getPageList()
}

const dialogOpen = ref(false);
const formData = ref({});
const formRef = ref();

function add(){
  formData.value = {}
  dialogOpen.value = true
}

function edit(index,row){
  formData.value = Object.assign({},row)
  dialogOpen.value = true
}

function closeDialog(){
  dialogOpen.value = false
  // 清空表单数据
  formData.value = {
    name: '',
    petTypeId: null,
    mainImg: '',
    basicInfo: ''
  }
}


function submit(){
  formRef.value.validate((valid)=>{
    if (!valid){
      ElMessage({
        message:"验证失败，请检查表单",
        type:'warning'
      });
      return
    }
    //新增
    if (!formData.value.id){
      request.post("/pet/add",formData.value).then(res => {
        if(!res){
          return
        }
        dialogOpen.value=false
        ElMessage({
          message:"操作成功",
          type:'success'
        });
        getPageList()
        // 清空表单数据
        formData.value = {
          name: '',
          petTypeId: null,
          mainImg: '',
          basicInfo: ''
        }
      })
    }else{
      //更新
      request.put("/pet/update",formData.value).then(res=>{
        if(!res){
          return
        }
        dialogOpen.value=false
        ElMessage({
          message:"操作成功",
          type:'success'
        });
        getPageList()
        // 清空表单数据
        formData.value = {
          name: '',
          petTypeId: null,
          mainImg: '',
          basicInfo: ''
        }
      })
    }
  })
}

function deleteOne(index,row){
  batchDelete([row])
}
function batchDelete(rows){
  let ids = rows.map(item => item.id);
  let names = rows.map(item => item.name);
  ElMessageBox.confirm(`此操作将永久删除宠物[${names.join('、')}],是否继续？`,'提示',{
    confirmButtonText:'确定',
    cancelButtonText:'取消',
    type:'warning',
    center:true
  }).then(()=>{
    request.delete("/pet/delete",{data:ids}).then(res=>{
      if(!res){
        return
      }
      ElMessage({
        message:"操作成功",
        type:'success'
      });
      getPageList()
    })
  }).catch(()=>{
    ElMessage({
      type:'info',
      message:'已取消删除'
    });
    tableComponents.value.clearSelection();
  });
}

const petStoreManagerList = ref([])
const petTypeList = ref([])

getPetStoreManagerList()
getPetTypeList()

function getPetStoreManagerList(){
  request.get("/petStoreManager/list").then(res=>{
    petStoreManagerList.value = res.data;
  })
}

function getPetTypeList(){
  request.get("/petType/list").then(res=>{
    petTypeList.value = res.data;
  })
}

const feedFormRef = ref()
const feedDialogOpen = ref(false)
const feedFormData=ref({})

function feed(row){
  feedDialogOpen.value=true
  feedFormData.value = {}
  feedFormData.value.petId = row.id
  feedFormData.value.petTypeId = row.petTypeId
}

function feedSubmit(){
  feedFormRef.value.validate((valid)=>{
    if(!valid){
      ElMessage({
        message:"验证失败，请检查表单！",
        type:"warning"
      });
      return
    }
    request.post("/petFeed/add",feedFormData.value).then(res=>{
      if(!res){
        return
      }
      feedDialogOpen.value = false
      ElMessage({
        message:"操作成功",
        type:"success"
      });
      router.push("/personalCenter?type=petFeed")
    })
  })
}

const fosterCareFormRef = ref()
const fosterCareDialogOpen = ref(false)
const fosterCareFormData = ref({})

function fosterCare(row){
  fosterCareDialogOpen.value = true
  fosterCareFormData.value = {}
  fosterCareFormData.value.petId = row.id
  fosterCareFormData.value.petTypeId = row.petTypeId
}

function fosterCareSubmit(){
  fosterCareFormRef.value.validate((valid)=>{
    if(!valid){
      ElMessage({
        message:"验证失败，请检查表单！",
        type:"warning"
      });
      return
    }
    request.post("/petFosterCare/add",fosterCareFormData.value).then(res=>{
      if(!res){
        return
      }
      fosterCareDialogOpen.value = false
      ElMessage({
        message:"操作成功",
        type:"success"
      });
      router.push("/personalCenter?type=petFosterCare")
    })
  })
}

// 全屏图片预览功能
const showFullscreen = ref(false)
const fullscreenImage = ref('')
const scale = ref(1)
const translateX = ref(0)
const translateY = ref(0)
const isDragging = ref(false)
const startX = ref(0)
const startY = ref(0)
const lastX = ref(0)
const lastY = ref(0)

const imageStyle = computed(() => ({
  transform: `translate(${translateX.value}px, ${translateY.value}px) scale(${scale.value})`,
  transition: isDragging.value ? 'none' : 'transform 0.3s ease',
  cursor: isDragging.value ? 'grabbing' : 'grab'
}))

function openFullscreen(imageSrc) {
  fullscreenImage.value = imageSrc
  showFullscreen.value = true
  resetZoom()
  document.body.style.overflow = 'hidden'
}

function closeFullscreen() {
  showFullscreen.value = false
  document.body.style.overflow = ''
  resetZoom()
}

function zoomIn() {
  scale.value = Math.min(scale.value * 1.2, 5)
}

function zoomOut() {
  scale.value = Math.max(scale.value / 1.2, 0.5)
}

function resetZoom() {
  scale.value = 1
  translateX.value = 0
  translateY.value = 0
}

function handleZoom(e) {
  e.preventDefault()
  const delta = e.deltaY > 0 ? 0.9 : 1.1
  scale.value = Math.max(0.5, Math.min(5, scale.value * delta))
}

// 鼠标拖拽功能
function handleMouseDown(e) {
  if (scale.value > 1) {
    isDragging.value = true
    startX.value = e.clientX - lastX.value
    startY.value = e.clientY - lastY.value
  }
}

function handleMouseMove(e) {
  if (isDragging.value && scale.value > 1) {
    e.preventDefault()
    lastX.value = e.clientX - startX.value
    lastY.value = e.clientY - startY.value
    translateX.value = lastX.value
    translateY.value = lastY.value
  }
}

function handleMouseUp() {
  isDragging.value = false
}

// 触摸功能（移动端支持）
let touchStartDistance = 0
let touchStartScale = 1

function handleTouchStart(e) {
  if (e.touches.length === 2) {
    // 双指缩放
    const dx = e.touches[0].clientX - e.touches[1].clientX
    const dy = e.touches[0].clientY - e.touches[1].clientY
    touchStartDistance = Math.sqrt(dx * dx + dy * dy)
    touchStartScale = scale.value
  } else if (e.touches.length === 1 && scale.value > 1) {
    // 单指拖拽
    isDragging.value = true
    startX.value = e.touches[0].clientX - lastX.value
    startY.value = e.touches[0].clientY - lastY.value
  }
}

function handleTouchMove(e) {
  e.preventDefault()
  if (e.touches.length === 2) {
    // 双指缩放
    const dx = e.touches[0].clientX - e.touches[1].clientX
    const dy = e.touches[0].clientY - e.touches[1].clientY
    const distance = Math.sqrt(dx * dx + dy * dy)
    const scaleChange = distance / touchStartDistance
    scale.value = Math.max(0.5, Math.min(5, touchStartScale * scaleChange))
  } else if (e.touches.length === 1 && isDragging.value) {
    // 单指拖拽
    lastX.value = e.touches[0].clientX - startX.value
    lastY.value = e.touches[0].clientY - startY.value
    translateX.value = lastX.value
    translateY.value = lastY.value
  }
}

function handleTouchEnd() {
  isDragging.value = false
}

// 键盘事件监听
onMounted(() => {
  document.addEventListener('keydown', (e) => {
    if (showFullscreen.value) {
      if (e.key === 'Escape') {
        closeFullscreen()
      } else if (e.key === '+' || e.key === '=') {
        zoomIn()
      } else if (e.key === '-' || e.key === '_') {
        zoomOut()
      } else if (e.key === '0') {
        resetZoom()
      }
    }
  })
})

onUnmounted(() => {
  document.removeEventListener('keydown', closeFullscreen)
  document.body.style.overflow = ''
})

</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

/* 我的宠物页面特定样式 */
.front-my-pet {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 宠物列表容器 */
.pet-list {
  margin-top: 32px;
}

/* 宠物卡片头部样式 */
.pet-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 0;
}

.pet-info {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 0;
}

.pet-name {
  font-weight: 700;
  font-size: 1.1rem;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pet-type-tag {
  background: var(--gradient-accent);
  color: white;
  border: none;
  font-weight: 500;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
}

.pet-time {
  color: var(--text-muted);
  font-size: 12px;
  white-space: nowrap;
  margin-left: 8px;
}

/* 宠物图片容器 */
.pet-image-container {
  text-align: center;
  margin-bottom: 16px;
  position: relative;
  overflow: hidden;
  border-radius: 12px;
}

.pet-image {
  width: 100%;
  height: 200px;
  border-radius: 12px;
  transition: transform 0.3s ease;
}

.pet-image:hover {
  transform: scale(1.05);
}

/* 宠物操作按钮 */
.pet-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
  margin-top: 16px;
}

.action-btn {
  border-radius: 12px;
  font-size: 12px;
  padding: 6px 8px;
  transition: all 0.3s ease;
  border: none;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  min-height: 32px;
}

.action-btn span {
  font-size: 14px;
}

.feed-btn {
  background: var(--gradient-accent);
  color: white;
  box-shadow: 0 2px 8px var(--shadow-accent);
}

.feed-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-accent);
}

.foster-btn {
  background: var(--gradient-primary);
  color: white;
  box-shadow: 0 2px 8px var(--shadow-primary);
}

.foster-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-primary);
}

.edit-btn {
  background: linear-gradient(135deg, #FFA726, #FFB74D);
  color: white;
  box-shadow: 0 2px 8px rgba(255, 167, 38, 0.3);
}

.edit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 167, 38, 0.4);
}

.delete-btn {
  background: linear-gradient(135deg, #EF5350, #E57373);
  color: white;
  box-shadow: 0 2px 8px rgba(239, 83, 80, 0.3);
}

.delete-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 83, 80, 0.4);
}

/* 分页容器 */
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

/* 空状态优化 */
.el-empty {
  background: var(--bg-primary);
  border-radius: 16px;
  padding: 40px;
  margin: 20px 0;
}

/* 对话框样式优化 */
.el-dialog {
  border-radius: 20px;
  overflow: hidden;
}

.el-dialog :deep(.el-dialog__header) {
  background: var(--gradient-header);
  color: white;
  padding: 20px 24px;
  margin: 0;
}

.el-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
  font-size: 1.1rem;
}

.el-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-size: 18px;
}

.el-dialog :deep(.el-dialog__body) {
  padding: 24px;
}

/* 表单样式优化 */
.el-form :deep(.el-form-item__label) {
  color: var(--text-primary);
  font-weight: 500;
}

.el-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid var(--gray-200);
}

.el-input :deep(.el-input__wrapper:hover) {
  border-color: var(--accent-cyan);
  box-shadow: 0 2px 8px var(--shadow-accent);
}

.el-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-orange);
  box-shadow: 0 2px 12px var(--shadow-primary);
}

.el-select :deep(.el-select .el-input__wrapper) {
  border-radius: 12px;
}

.el-button {
  border-radius: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .pet-actions {
    grid-template-columns: 1fr;
  }

  .pet-name {
    font-size: 1rem;
  }

  .front-my-pet {
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .pet-card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .pet-time {
    margin-left: 0;
    align-self: flex-end;
  }
}

/* 全屏图片预览样式 */
.fullscreen-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.9);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(5px);
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.fullscreen-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.fullscreen-image {
  max-width: 90vw;
  max-height: 90vh;
  object-fit: contain;
  user-select: none;
  pointer-events: auto;
  border-radius: 8px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.5);
}

/* 控制按钮样式 */
.close-btn,
.zoom-in-btn,
.zoom-out-btn,
.reset-btn {
  position: absolute;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #333;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  z-index: 10;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.close-btn {
  top: 20px;
  right: 20px;
}

.zoom-in-btn {
  top: 20px;
  right: 80px;
}

.zoom-out-btn {
  top: 80px;
  right: 20px;
}

.reset-btn {
  top: 80px;
  right: 80px;
}

.close-btn:hover,
.zoom-in-btn:hover,
.zoom-out-btn:hover,
.reset-btn:hover {
  background: rgba(255, 255, 255, 1);
  transform: scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.close-btn:active,
.zoom-in-btn:active,
.zoom-out-btn:active,
.reset-btn:active {
  transform: scale(0.95);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .close-btn {
    top: 10px;
    right: 10px;
    width: 40px;
    height: 40px;
  }

  .zoom-in-btn {
    top: 10px;
    right: 60px;
    width: 40px;
    height: 40px;
  }

  .zoom-out-btn {
    top: 60px;
    right: 10px;
    width: 40px;
    height: 40px;
  }

  .reset-btn {
    top: 60px;
    right: 60px;
    width: 40px;
    height: 40px;
  }

  .fullscreen-image {
    max-width: 95vw;
    max-height: 95vh;
  }
}

/* 添加点击提示 */
.pet-image {
  cursor: pointer;
  position: relative;
}

.pet-image::after {
  content: '🔍 点击查看大图';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
  white-space: nowrap;
}

.pet-image:hover::after {
  opacity: 1;
}

/* 加载状态 */
.fullscreen-image {
  transition: opacity 0.3s ease;
}

.fullscreen-image.loading {
  opacity: 0.5;
}

/* 防止文字选择 */
.fullscreen-overlay {
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>
