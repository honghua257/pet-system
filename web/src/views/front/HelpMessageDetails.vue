<template>
  <div class="help-details-container">
    <!-- 求助信息内容 -->
    <el-card class="help-content-card">
      <el-space direction="vertical" alignment="left" style="width: 100%">
        <!-- 返回链接 -->
        <div class="back-section">
          <span class="back-link" @click="goBack">《 返回求助列表</span>
        </div>

        <div class="help-header">
          <h1 class="help-title">{{ info.title }}</h1>
          <div class="help-meta">
            <el-space>
              <span>发布时间: {{ info.createTime }}</span>
            </el-space>
          </div>
        </div>

        <div class="help-content">
          <div v-html="info.content"></div>
        </div>
      </el-space>
    </el-card>

    <!-- 回复区域 -->
    <el-card class="reply-section-card">
      <template #header>
        <div class="reply-header">
          <span class="reply-title">全部回复 ({{ totalReplies }})</span>
        </div>
      </template>

      <!-- 发布回复 -->
      <div class="reply-input-area">
        <el-input
          v-model="newReply"
          type="textarea"
          :rows="3"
          placeholder="写下你的回复..."
          maxlength="500"
          show-word-limit
          class="reply-textarea"
        />
        <div class="reply-submit-area">
          <el-button
            type="primary"
            @click="submitReply"
            :loading="submitting"
            class="submit-btn"
          >
            发布回复
          </el-button>
        </div>
      </div>

      <!-- 回复列表 -->
      <div class="reply-list" v-if="replyList.length > 0">
        <div
          v-for="reply in replyList"
          :key="reply.id"
          class="reply-item"
        >
          <!-- 主回复 -->
          <div class="main-reply">
            <div class="reply-user-info">
              <el-avatar :src="reply.userAvatarUrl" :size="36" />
              <div class="user-details">
                <span class="username">{{ reply.username }}</span>
                <span class="reply-time">{{ reply.createTime }}</span>
              </div>
            </div>
            <div class="reply-content">{{ reply.content }}</div>
            <div class="reply-actions">
              <el-button
                v-if="userInfo && userInfo.id === reply.userId"
                type="text"
                size="small"
                class="delete-btn"
                @click="deleteReply(reply.id)"
              >
                删除
              </el-button>
              <el-button
                type="text"
                size="small"
                class="reply-btn"
                @click="showNestedReplyInput(reply.id)"
              >
                回复
              </el-button>
            </div>
          </div>

          <!-- 嵌套回复输入框 -->
          <div v-if="replyingTo === reply.id" class="nested-reply-input">
            <el-input
              v-model="nestedReplyContent"
              type="textarea"
              :rows="2"
              :placeholder="`回复 ${reply.username}`"
              class="nested-textarea"
            />
            <div class="nested-submit-area">
              <el-button size="small" @click="submitNestedReply(reply.id)" :loading="nestedSubmitting">
                回复
              </el-button>
              <el-button size="small" @click="cancelNestedReply">取消</el-button>
            </div>
          </div>

          <!-- 子回复列表 -->
          <div v-if="reply.replies && reply.replies.length > 0" class="nested-replies">
            <div v-for="nestedReply in reply.replies" :key="nestedReply.id" class="nested-reply-item">
              <div class="nested-reply-user-info">
                <el-avatar :src="nestedReply.userAvatarUrl" :size="28" />
                <div class="nested-user-details">
                  <span class="nested-username">{{ nestedReply.username }}</span>
                  <span class="nested-time">{{ nestedReply.createTime }}</span>
                </div>
              </div>
              <div class="nested-reply-content">{{ nestedReply.content }}</div>
              <div class="nested-reply-actions">
                <el-button
                  v-if="userInfo && userInfo.id === nestedReply.userId"
                  type="text"
                  size="small"
                  @click="deleteReply(nestedReply.id)"
                >
                  删除
                </el-button>
                <el-button
                  type="text"
                  size="small"
                  @click="showNestedReplyInput(nestedReply.id)"
                >
                  回复
                </el-button>
              </div>

              <!-- 三级回复输入框 -->
              <div v-if="replyingTo === nestedReply.id" class="nested-reply-input">
                <el-input
                  v-model="nestedReplyContent"
                  type="textarea"
                  :rows="2"
                  :placeholder="`回复 ${nestedReply.username}`"
                  class="nested-textarea"
                />
                <div class="nested-submit-area">
                  <el-button size="small" @click="submitNestedReply(nestedReply.id)" :loading="nestedSubmitting">
                    回复
                  </el-button>
                  <el-button size="small" @click="cancelNestedReply">取消</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无回复，来做第一个回复的人吧！" class="empty-replies" />
    </el-card>
  </div>
</template>
<script setup>
import {useRoute, useRouter} from "vue-router";
import {ref, onMounted, computed} from "vue";
import http from "@/utils/http.js";
import {ElMessage, ElMessageBox} from "element-plus";
import tools from "@/utils/tools.js";

const route = useRoute()
const router = useRouter()
const id = ref(route.params.id)
const info = ref({})
const userInfo = ref(tools.getCurrentUser())

// 回复相关数据
const replyList = ref([])
const newReply = ref('')
const submitting = ref(false)
const replyingTo = ref(null)
const nestedReplyContent = ref('')
const nestedSubmitting = ref(false)

// 计算回复总数
const totalReplies = computed(() => {
  const countMainReplies = replyList.value.length
  const countNestedReplies = replyList.value.reduce((total, reply) => {
    return total + (reply.replies ? reply.replies.length : 0)
  }, 0)
  return countMainReplies + countNestedReplies
})

// 获取求助信息
function getInfo(){
  http.get("/helpMessage/selectById/"+id.value).then(res=>{
    info.value = res.data
  })
}

// 获取回复列表
function getReplies(){
  http.get(`/helpReply/list/${id.value}`).then(res=>{
    replyList.value = res.data || []
  })
}

// 提交回复
function submitReply(){
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    return
  }

  if (!newReply.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  submitting.value = true
  http.post('/helpReply/add', {
    helpId: parseInt(id.value),
    content: newReply.value.trim()
  }).then(res=>{
    if(res.code === 200){
      ElMessage.success('回复成功')
      newReply.value = ''
      getReplies()
    } else {
      ElMessage.error(res.msg || '回复失败')
    }
  }).catch(error => {
    ElMessage.error('回复失败，请重试')
  }).finally(() => {
    submitting.value = false
  })
}

// 删除回复
function deleteReply(replyId){
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    return
  }

  ElMessageBox.confirm('确定要删除这条回复吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    http.delete(`/helpReply/delete/${replyId}`).then(res=>{
      if(res.code === 200){
        ElMessage.success('删除成功')
        getReplies()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    }).catch(error => {
      ElMessage.error('删除失败，请重试')
    })
  })
}

// 显示嵌套回复输入框
function showNestedReplyInput(replyId){
  replyingTo.value = replyId
  nestedReplyContent.value = ''
}

// 提交嵌套回复
function submitNestedReply(parentId){
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    return
  }

  if (!nestedReplyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  nestedSubmitting.value = true
  http.post('/helpReply/add', {
    helpId: parseInt(id.value),
    parentId: parentId,
    content: nestedReplyContent.value.trim()
  }).then(res=>{
    if(res.code === 200){
      ElMessage.success('回复成功')
      nestedReplyContent.value = ''
      replyingTo.value = null
      getReplies()
    } else {
      ElMessage.error(res.msg || '回复失败')
    }
  }).catch(error => {
    ElMessage.error('回复失败，请重试')
  }).finally(() => {
    nestedSubmitting.value = false
  })
}

// 取消嵌套回复
function cancelNestedReply(){
  replyingTo.value = null
  nestedReplyContent.value = ''
}

// 返回列表
function goBack(){
  router.push('/helpMessage')
}

onMounted(() => {
  getInfo()
  getReplies()
})
</script>

<style scoped>
/* 容器样式 */
.help-details-container {
  width: 70%;
  max-width: 900px;
  margin: 20px auto;
}

.help-content-card {
  margin-bottom: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.reply-section-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 返回链接样式 */
.back-section {
  margin-bottom: 20px;
}

.back-link {
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 6px;
  transition: all 0.3s;
  display: inline-block;
}

.back-link:hover {
  background-color: #f0f8ff;
  color: #337ecc;
}

/* 求助信息样式 */
.help-header {
  text-align: center;
  margin-bottom: 20px;
}

.help-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 10px 0;
}

.help-meta {
  color: #7f8c8d;
  font-size: 14px;
}

.help-content {
  font-size: 16px;
  line-height: 1.8;
  color: #34495e;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-top: 15px;
}

.help-content :deep(p) {
  margin: 10px 0;
}

.help-content :deep(img) {
  max-width: 100%;
  border-radius: 6px;
  margin: 10px 0;
}

/* 回复区域样式 */
.reply-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.reply-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}

/* 回复输入区域 */
.reply-input-area {
  margin-bottom: 25px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.reply-textarea :deep(.el-textarea__inner) {
  border-radius: 8px;
  border: 1px solid #ddd;
  font-size: 14px;
  line-height: 1.5;
}

.reply-textarea :deep(.el-textarea__inner:focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.reply-submit-area {
  margin-top: 12px;
  text-align: right;
}

.submit-btn {
  padding: 8px 20px;
  font-weight: 500;
}

/* 回复列表样式 */
.reply-list {
  max-height: 600px;
  overflow-y: auto;
}

.reply-item {
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.reply-item:last-child {
  border-bottom: none;
}

/* 主回复样式 */
.main-reply {
  position: relative;
}

.reply-user-info {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.user-details {
  margin-left: 12px;
}

.username {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
  display: block;
  margin-bottom: 2px;
}

.reply-time {
  color: #95a5a6;
  font-size: 12px;
}

.reply-content {
  margin-left: 48px;
  margin-bottom: 12px;
  padding: 12px 15px;
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
  line-height: 1.6;
  color: #34495e;
  font-size: 14px;
}

.reply-actions {
  margin-left: 48px;
  display: flex;
  gap: 15px;
}

.reply-btn, .delete-btn {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s;
}

.reply-btn:hover {
  background-color: #f0f8ff;
  color: #409eff;
}

.delete-btn:hover {
  background-color: #fef0f0;
  color: #f56c6c;
}

/* 嵌套回复输入框 */
.nested-reply-input {
  margin-left: 48px;
  margin-top: 12px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.nested-textarea :deep(.el-textarea__inner) {
  border-radius: 6px;
  font-size: 13px;
}

.nested-submit-area {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}

/* 嵌套回复样式 */
.nested-replies {
  margin-left: 48px;
  margin-top: 15px;
  padding-left: 15px;
  border-left: 2px solid #e9ecef;
}

.nested-reply-item {
  padding: 12px 0;
  border-bottom: 1px solid #f8f9fa;
}

.nested-reply-item:last-child {
  border-bottom: none;
}

.nested-reply-user-info {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.nested-user-details {
  margin-left: 10px;
}

.nested-username {
  font-weight: 500;
  color: #2c3e50;
  font-size: 13px;
  display: block;
  margin-bottom: 2px;
}

.nested-time {
  color: #95a5a6;
  font-size: 11px;
}

.nested-reply-content {
  margin-left: 38px;
  margin-bottom: 8px;
  padding: 8px 12px;
  background-color: #ffffff;
  border-radius: 6px;
  border: 1px solid #f8f9fa;
  line-height: 1.5;
  color: #34495e;
  font-size: 13px;
}

.nested-reply-actions {
  margin-left: 38px;
  display: flex;
  gap: 12px;
}

/* 空状态样式 */
.empty-replies {
  margin: 40px 0;
}

/* 滚动条样式 */
.reply-list::-webkit-scrollbar {
  width: 6px;
}

.reply-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.reply-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 10px;
}

.reply-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .help-details-container {
    width: 80%;
  }
}

@media (max-width: 768px) {
  .help-details-container {
    width: 95%;
    margin: 10px auto;
  }

  .help-title {
    font-size: 20px;
  }

  .reply-content,
  .reply-actions,
  .nested-reply-input,
  .nested-replies {
    margin-left: 0;
  }

  .reply-user-info,
  .nested-reply-user-info {
    margin-bottom: 8px;
  }

  .username,
  .nested-username {
    font-size: 13px;
  }

  .reply-content,
  .nested-reply-content {
    font-size: 14px;
  }
}
</style>
