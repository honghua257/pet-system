<template>
  <div class="front-help-message front-content">
    <div class="front-section">
        <div class="front-section-header">
          <div class="front-section-title">
            <span class="front-section-icon">🆘</span>
            <span>求助信息</span>
          </div>
          <el-button type="primary" @click="add" :icon="Plus" class="publish-help-btn">
              发布求助
            </el-button>
        </div>
        <div class="search-container">
          <el-space style="flex-wrap: wrap;gap: 15px;align-items: center">
            <el-input v-model="searchForm.title"
                      placeholder="请输入关键词搜索"
                      style="width: 300px"
                      clearable
                      @keyup.enter="search"
                      class="search-input"/>
            <el-button type="primary" :icon="Search" @click="search" class="search-btn-capsule">搜索</el-button>
            <el-radio-group v-model="searchForm.onlyYou" @change="search" class="filter-radio">
              <el-radio-button :label="false">查看全部</el-radio-button>
              <el-radio-button :label="true">只看自己</el-radio-button>
            </el-radio-group>
          </el-space>
        </div>
      </div>
    <div class="help-list">
        <el-card v-for="(item,index) in listData" :key="item.id" shadow="hover" class="help-card front-card front-fade-in" :class="'front-stagger-' + ((index % 4) + 1)">
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
            <el-button link type="plan" @click="router.push('/helpMessageDetails/'+item.id)">查看详情</el-button>
            <el-button link type="info" @click="router.push('/helpMessageDetails/'+item.id)">
              💬 {{ item.replyCount || 0 }} 条回复
            </el-button>
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
  </div>
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
  request.get("/helpMessage/page",{
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
      request.post("/helpMessage/add",formData.value).then(res => {
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
      request.put("/helpMessage/update",formData.value).then(res=>{
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
      request.delete("/helpMessage/delete",{data:ids}).then(res=>{
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

/* 求助信息页面特定样式 */
.front-help-message {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 发布求助按钮样式 */
.publish-help-btn {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;
  border: none;
  background: var(--gradient-primary);
  color: white;
  box-shadow: 0 2px 8px var(--shadow-primary);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.publish-help-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-primary);
  background: linear-gradient(135deg, #ff8c42, #ff6b35);
}

.publish-help-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 6px var(--shadow-primary);
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

/* 搜索输入框样式 */
.search-input {
  border-radius: 25px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 25px;
  border: 2px solid var(--gray-200);
  transition: all 0.3s ease;
  padding: 0 16px;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: var(--accent-cyan);
  box-shadow: 0 2px 8px var(--shadow-accent);
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-orange);
  box-shadow: 0 2px 12px var(--shadow-primary);
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

/* 求助列表 */
.help-list {
  margin-top: 32px;
}

.help-card {
  margin-bottom: 20px;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.help-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px var(--shadow-primary);
}

/* 用户信息样式 */
.user-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
}

.user-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border: 2px solid var(--primary-orange-light);
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
  border-color: var(--primary-orange);
}

.username {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
  transition: color 0.3s ease;
}

.username:hover {
  color: var(--accent-cyan);
}

.created-at {
  margin: 0;
  font-size: 12px;
  color: var(--text-muted);
  white-space: nowrap;
}

/* 内容样式 */
.content {
  overflow: hidden;
  transition: height 0.3s ease;
  padding: 0 16px 16px;
  line-height: 1.6;
  color: var(--text-secondary);
}

.content :deep(p) {
  margin: 8px 0;
}

.content :deep(img) {
  max-width: 100%;
  border-radius: 8px;
  margin: 8px 0;
}

/* 求助卡片标题 */
.help-card :deep(.el-card__header) {
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  border-bottom: 2px solid var(--primary-orange-light);
  padding: 20px 24px;
}

.help-card :deep(h3) {
  color: var(--text-primary);
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0 0 12px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.help-card :deep(h3::before) {
  content: '🆘';
  font-size: 1.4rem;
}

/* 操作按钮 */
.help-card :deep(.el-card__footer) {
  background: var(--bg-secondary);
  padding: 12px 24px;
  border-top: 1px solid var(--gray-200);
}

.help-card :deep(.el-button--link) {
  border: none;
  font-weight: 500;
  margin: 0 8px;
  padding: 6px 12px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.help-card :deep(.el-button--link:hover) {
  background: var(--bg-accent);
  transform: translateY(-1px);
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

/* 对话框样式 */
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

/* 表单样式 */
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

/* 空状态优化 */
.el-empty {
  background: var(--bg-primary);
  border-radius: 16px;
  padding: 40px;
  margin: 20px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .front-help-message {
    padding: 16px;
  }

  .search-container {
    gap: 12px;
  }

  .filter-radio {
    flex-direction: column;
    gap: 8px;
  }

  .user-details {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .help-card :deep(.el-card__header) {
    padding: 16px 20px;
  }

  .help-card :deep(h3) {
    font-size: 1.1rem;
  }
}

@media (max-width: 480px) {
  .search-input {
    max-width: 100%;
  }

  .username {
    font-size: 14px;
  }

  .help-card :deep(.el-card__footer) {
    padding: 8px 16px;
  }

  .help-card :deep(.el-button--link) {
    margin: 4px;
    padding: 4px 8px;
    font-size: 12px;
  }
}
</style>
