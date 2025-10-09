<template>
  <div class="front-pet-diary front-content">
    <div class="front-section">
        <div class="front-section-header">
          <div class="front-section-title">
            <span class="front-section-icon">📔</span>
            <span>宠物日记</span>
          </div>
          <el-button type="success" @click="add" :icon="Plus" class="btn-pet-primary">写日记</el-button>
        </div>
        <div class="search-container">
          <el-space style="flex-wrap: wrap;gap: 15px">
            <el-button type="primary" :icon="Search" @click="search" class="search-btn">搜索</el-button>
            <el-input v-model="searchForm.title"
                      placeholder="请输入关键词搜索"
                      style="width: 300px"
                      clearable
                      @keyup.enter="search"/>
            <el-radio-group v-model="searchForm.onlyYou" @change="search" class="filter-radio">
              <el-radio-button :label="false">查看全部</el-radio-button>
              <el-radio-button :label="true">只看自己</el-radio-button>
            </el-radio-group>
          </el-space>
        </div>
      </div>
      <div class="diary-list">
        <el-card v-for="(item,index) in listData" :key="item.id" shadow="hover" class="diary-card front-card front-fade-in" :class="'front-stagger-' + ((index % 4) + 1)">
          <template #header>
            <H3>{{item.title}}</H3>
            <br>
            <div class="user-details">
              <div class="user-left">
                <el-avatar :src="item.userAvatarUrl" class="user-avatar"></el-avatar>
                <h3 class="username">{{item.username}}</h3>
              </div>
              <p class="created-at">{{item.createTime}}</p>
            </div>
          </template>
          <div class="content" :style="{height:item.showFullContent ? 'auto' : '150px'}">
            <span v-html="item.content"></span>
          </div>
          <template #footer>
            <el-button v-if="!item.showFullContent"
                       @click="item.showFullContent = true"
                       type="info"
                       link>
                        展开
            </el-button>
            <el-button link type="plan" @click="goToDetails(item.id)">查看详情</el-button>
            <el-button link type="primary" v-if="userInfo.type === 'USER' && userInfo.id === item.userId"
            @click= "edit(index,item)">编辑
            </el-button>
            <el-button link type="primary" v-if="userInfo.type === 'USER' && userInfo.id === item.userId"
            @click= "deleteOne(index,item)">删除
            </el-button>
          </template>
        </el-card>
      </div>
      <el-card class="pagination-card">
        <el-pagination
              @current-change="currentChange"
              @size-change="sizeChange"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.currentPage"
              background
              layout="total, sizes, prev, pager, next"
              :total="pageInfo.total"
              class="unified-pagination">
        </el-pagination>
      </el-card>
    </div>

    <!-- 对话框 -->
    <el-dialog v-model="dialogOpen"
               v-if="dialogOpen"
               :title="formData.id?'编辑':'新增'"
               width="800">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="标题" prop="title"
                      :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.title"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <MyEditor :content="formData.content" @content-change="formData.content=$event" v-if="dialogOpen"></MyEditor>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit" :icon="Check">提交</el-button>
          <el-button @click="closeDialog" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>
</template>
<script setup>
import request from "@/utils/http.js";
import {Check, Plus, Search,Close} from "@element-plus/icons-vue";
import { ref, toRaw} from "vue";
import {ElMessage,ElMessageBox} from "element-plus";
import MyEditor from "@/components/MyEditor.vue";
import tools from "@/utils/tools.js";
import router from "@/router/index.js";
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
  request.get("/petDiary/page",{
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
  formData.value={}
  dialogOpen.value =true
}

function edit(index,row){
  formData.value = Object.assign({},row)
  dialogOpen.value = true
}

function closeDialog(){
  dialogOpen.value = false
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
      request.post("/petDiary/add",formData.value).then(res => {
        if(!res){
          return
        }
        dialogOpen.value=false
        ElMessage({
          message:"操作成功",
          type:'success'
        });
        getPageList()
      })
    }else{
      //更新
      request.put("/petDiary/update",formData.value).then(res=>{
        if(!res){
          return
        }
        dialogOpen.value=false
        ElMessage({
          message:"操作成功",
          type:'success'
        });
        getPageList()
      })
    }
  })
}
// 跳转到详情页
function goToDetails(id) {
  if (!id) {
    ElMessage.error('日记ID不存在，无法跳转');
    return;
  }
  console.log('跳转到详情页，ID:', id);
  router.push('/petDiaryDetails/' + id);
}

function deleteOne(index,row){
  batchDelete([row])
}

function batchDelete(rows){
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据,是否继续？`,'提示',{
    confirmButtonText:'确定',
    cancelButtonText:'取消',
    type:'warning',
    center:true
  }).then(()=>{
      request.delete("/petDiary/delete",{data:ids}).then(res=>{
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
  });
}


</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";
@import "@/styles/module-common.css";

/* 宠物日记页面特定样式 */
.front-pet-diary {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 搜索区域布局 */
.search-container {
  margin-top: 20px;
}

.filter-radio {
  display: flex;
  gap: 12px;
}

.filter-radio :deep(.el-radio-button__inner) {
  border-radius: 20px;
  border: 2px solid var(--gray-200);
  background: var(--bg-primary);
  color: var(--text-secondary);
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.filter-radio :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: var(--gradient-primary);
  color: white;
  border-color: var(--primary-orange);
  box-shadow: 0 2px 8px var(--shadow-primary);
}

/* 搜索按钮样式 */
.search-btn {
  background: var(--gradient-primary);
  border: none;
  border-radius: 20px;
  color: white;
  font-weight: 600;
  padding: 10px 24px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px var(--shadow-primary);
  display: flex;
  align-items: center;
  gap: 6px;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-primary);
  background: var(--primary-orange-dark);
}

.search-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 6px var(--shadow-primary);
}

/* 用户信息样式 */
.user-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
}

/* 日记列表样式 */
.diary-list {
  margin-top: 32px;
}

.diary-card {
  margin-bottom: 20px;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.diary-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px var(--shadow-primary);
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

.user-left{
  display: flex;
  align-items: center;
}

.user-avatar{
  margin-right: 10px;
}

.username{
  font-size: 16px;
  font-weight: bold;
  margin: 0;
}

.created-at{
  margin: 0;
  font-size: 14px;
  color: #888;
  white-space: nowrap;
}

.content{
  overflow: hidden;
  transition: height 0.3s;
  padding: 0 16px 16px;
}
</style>
