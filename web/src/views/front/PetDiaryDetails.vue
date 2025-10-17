<template>
  <div class="front-diary-details front-content">
    <div class="front-section">
      <div class="front-section-header">
        <el-button @click="goBack" class="back-btn" type="default">
          <span class="back-icon">←</span>
          返回日记列表
        </el-button>
      </div>
    </div>

    <div class="diary-content-container">
      <el-card class="diary-content-card">
        <template #header>
          <div class="diary-header">
            <h1 class="diary-title">{{ info.title }}</h1>
            <div class="diary-meta">
              <span class="diary-date">
                <span class="meta-icon">📅</span>
                {{ info.createTime }}
              </span>
              <span class="diary-replies">
                <span class="meta-icon">💬</span>
                {{ info.replyCount || 0 }} 条评论
              </span>
            </div>
          </div>
        </template>

        <div class="diary-content">
          <div v-html="info.content" class="content-html"></div>
        </div>
      </el-card>
    </div>

    <!-- 评论区域 -->
    <div class="comments-section">
      <el-card class="comments-card">
        <template #header>
          <div class="comments-header">
            <h3 class="comments-title">
              <span class="title-icon">💬</span>
              全部评论 ({{ totalComments }})
            </h3>
          </div>
        </template>

        <!-- 发表评论 -->
        <div class="comment-input-area">
          <div class="input-header">
            <el-avatar :src="userInfo?.avatarUrl" :size="40" class="user-avatar" />
            <span class="input-label">发表评论</span>
          </div>
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="4"
            placeholder="分享你的想法..."
            maxlength="500"
            show-word-limit
            class="comment-textarea"
            resize="none"
          />
          <div class="comment-submit-area">
            <el-button
              type="primary"
              @click="submitComment"
              :loading="submitting"
              class="submit-btn"
              size="default"
            >
              <span class="btn-icon">📤</span>
              发表评论
            </el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list" v-if="commentList.length > 0">
          <div
            v-for="comment in commentList"
            :key="comment.id"
            class="comment-item"
          >
            <!-- 主评论 -->
            <div class="main-comment">
              <div class="comment-user-info">
                <el-avatar :src="comment.userAvatarUrl" :size="42" class="comment-avatar" />
                <div class="user-details">
                  <div class="user-header">
                    <span class="username">{{ comment.username }}</span>
                    <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                  </div>
                </div>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-actions">
                <el-button
                  v-if="userInfo && userInfo.id === comment.userId"
                  type="text"
                  size="small"
                  class="delete-btn"
                  @click="deleteComment(comment.id)"
                >
                  <span class="action-icon">🗑️</span>
                  删除
                </el-button>
                <el-button
                  type="text"
                  size="small"
                  class="reply-btn"
                  @click="showReplyInput(comment.id)"
                >
                  <span class="action-icon">💬</span>
                  回复
                </el-button>
              </div>
            </div>

            <!-- 回复输入框 -->
            <div v-if="replyingTo === comment.id" class="reply-input-area">
              <div class="reply-input-wrapper">
                <el-avatar :src="userInfo?.avatarUrl" :size="32" class="reply-avatar" />
                <div class="reply-input-content">
                  <el-input
                    v-model="replyContent"
                    type="textarea"
                    :rows="3"
                    :placeholder="`回复 ${comment.username}`"
                    class="reply-textarea"
                    resize="none"
                    maxlength="300"
                    show-word-limit
                  />
                  <div class="reply-actions">
                    <el-button
                      size="small"
                      type="primary"
                      @click="submitReply(comment.id)"
                      :loading="replySubmitting"
                      class="reply-submit-btn"
                    >
                      回复
                    </el-button>
                    <el-button size="small" @click="cancelReply" class="reply-cancel-btn">
                      取消
                    </el-button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 子评论列表 -->
            <div v-if="comment.replies && comment.replies.length > 0" class="replies-section">
              <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                <div class="reply-user-info">
                  <el-avatar :src="reply.userAvatarUrl" :size="32" class="reply-avatar" />
                  <div class="reply-user-details">
                    <div class="reply-user-header">
                      <span class="reply-username">{{ reply.username }}</span>
                      <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
                    </div>
                  </div>
                </div>
                <div class="reply-content">{{ reply.content }}</div>
                <div class="reply-actions">
                  <el-button
                    v-if="userInfo && userInfo.id === reply.userId"
                    type="text"
                    size="small"
                    class="delete-btn"
                    @click="deleteComment(reply.id)"
                  >
                    <span class="action-icon">🗑️</span>
                    删除
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    class="reply-btn"
                    @click="showReplyInput(reply.id)"
                  >
                    <span class="action-icon">💬</span>
                    回复
                  </el-button>
                </div>

                <!-- 三级回复输入框 -->
                <div v-if="replyingTo === reply.id" class="reply-input-area">
                  <div class="reply-input-wrapper">
                    <el-avatar :src="userInfo?.avatarUrl" :size="28" class="reply-avatar" />
                    <div class="reply-input-content">
                      <el-input
                        v-model="replyContent"
                        type="textarea"
                        :rows="2"
                        :placeholder="`回复 ${reply.username}`"
                        class="reply-textarea"
                        resize="none"
                        maxlength="300"
                        show-word-limit
                      />
                      <div class="reply-actions">
                        <el-button
                          size="small"
                          type="primary"
                          @click="submitReply(reply.id)"
                          :loading="replySubmitting"
                          class="reply-submit-btn"
                        >
                          回复
                        </el-button>
                        <el-button size="small" @click="cancelReply" class="reply-cancel-btn">
                          取消
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <el-empty
          v-else
          description="暂无评论，来做第一个评论的人吧！"
          class="empty-comments"
        >
          <template #image>
            <span class="empty-icon">💭</span>
          </template>
        </el-empty>
      </el-card>
    </div>
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

// 评论相关数据
const commentList = ref([])
const newComment = ref('')
const submitting = ref(false)
const replyingTo = ref(null)
const replyContent = ref('')
const replySubmitting = ref(false)

// 计算评论总数
const totalComments = computed(() => {
  const countMainComments = commentList.value.length
  const countReplies = commentList.value.reduce((total, comment) => {
    return total + (comment.replies ? comment.replies.length : 0)
  }, 0)
  return countMainComments + countReplies
})

// 格式化时间
function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  if (diff < 604800000) return Math.floor(diff / 86400000) + '天前'

  return date.toLocaleDateString('zh-CN')
}

// 获取日记信息
function getInfo(){
  http.get("/petDiary/selectById/"+id.value).then(res=>{
    info.value = res.data
  })
}

// 获取评论列表
function getComments(){
  http.get(`/petDiaryReply/list/${id.value}`).then(res=>{
    commentList.value = res.data || []
  })
}

// 提交评论
function submitComment(){
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    return
  }

  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  submitting.value = true
  http.post('/petDiaryReply/add', {
    diaryId: parseInt(id.value),
    content: newComment.value.trim()
  }).then(res=>{
    if(res.code === 200){
      ElMessage.success('评论成功')
      newComment.value = ''
      getComments()
      getInfo() // 刷新评论数量
    } else {
      ElMessage.error(res.msg || '评论失败')
    }
  }).catch(error => {
    ElMessage.error('评论失败，请重试')
  }).finally(() => {
    submitting.value = false
  })
}

// 删除评论
function deleteComment(commentId){
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    return
  }

  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    http.delete(`/petDiaryReply/delete/${commentId}`).then(res=>{
      if(res.code === 200){
        ElMessage.success('删除成功')
        getComments()
        getInfo() // 刷新评论数量
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    }).catch(error => {
      ElMessage.error('删除失败，请重试')
    })
  })
}

// 显示回复输入框
function showReplyInput(commentId){
  replyingTo.value = commentId
  replyContent.value = ''
}

// 提交回复
function submitReply(parentId){
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    return
  }

  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  replySubmitting.value = true
  http.post('/petDiaryReply/add', {
    diaryId: parseInt(id.value),
    parentId: parentId,
    content: replyContent.value.trim()
  }).then(res=>{
    if(res.code === 200){
      ElMessage.success('回复成功')
      replyContent.value = ''
      replyingTo.value = null
      getComments()
      getInfo() // 刷新评论数量
    } else {
      ElMessage.error(res.msg || '回复失败')
    }
  }).catch(error => {
    ElMessage.error('回复失败，请重试')
  }).finally(() => {
    replySubmitting.value = false
  })
}

// 取消回复
function cancelReply(){
  replyingTo.value = null
  replyContent.value = ''
}

// 返回列表
function goBack(){
  router.push('/petDiary')
}

onMounted(() => {
  getInfo()
  getComments()
})
</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

/* 日记详情页面特定样式 */
.front-diary-details {
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 返回按钮样式 */
.back-btn {
  background: var(--bg-primary);
  color: var(--text-primary);
  border: 2px solid var(--gray-200);
  border-radius: 20px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-btn:hover {
  border-color: var(--accent-cyan);
  color: var(--accent-cyan);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-accent);
}

.back-icon {
  font-size: 16px;
  font-weight: bold;
}

/* 日记内容容器 */
.diary-content-container {
  margin-top: 24px;
}

.diary-content-card {
  background: var(--bg-primary);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 24px var(--shadow-neutral);
  transition: all 0.3s ease;
}

.diary-content-card:hover {
  box-shadow: 0 12px 32px var(--shadow-primary);
}

/* 日记头部 */
.diary-header {
  text-align: center;
  padding: 20px 0;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  border-radius: 16px 16px 0 0;
}

.diary-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 16px 0;
  line-height: 1.2;
}

.diary-meta {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.diary-date,
.diary-replies {
  color: var(--text-secondary);
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 16px;
  background: var(--bg-accent);
  border-radius: 20px;
  transition: all 0.3s ease;
}

.diary-replies:hover {
  background: var(--accent-cyan);
  color: white;
  transform: translateY(-1px);
}

.meta-icon {
  font-size: 16px;
}

/* 日记内容 */
.diary-content {
  padding: 32px;
  line-height: 1.8;
  color: var(--text-primary);
  font-size: 16px;
}

.content-html {
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.content-html :deep(p) {
  margin: 16px 0;
  line-height: 1.8;
}

.content-html :deep(h1),
.content-html :deep(h2),
.content-html :deep(h3),
.content-html :deep(h4),
.content-html :deep(h5),
.content-html :deep(h6) {
  color: var(--text-primary);
  margin: 24px 0 12px 0;
  font-weight: 600;
}

.content-html :deep(h1) { font-size: 1.8rem; }
.content-html :deep(h2) { font-size: 1.6rem; }
.content-html :deep(h3) { font-size: 1.4rem; }
.content-html :deep(h4) { font-size: 1.2rem; }
.content-html :deep(h5) { font-size: 1.1rem; }
.content-html :deep(h6) { font-size: 1rem; }

.content-html :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 12px;
  margin: 16px 0;
  box-shadow: 0 4px 12px var(--shadow-neutral);
}

.content-html :deep(blockquote) {
  border-left: 4px solid var(--primary-orange);
  padding-left: 16px;
  margin: 16px 0;
  color: var(--text-secondary);
  font-style: italic;
}

.content-html :deep(code) {
  background: var(--bg-secondary);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  color: var(--primary-orange);
}

.content-html :deep(pre) {
  background: var(--bg-secondary);
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 16px 0;
}

.content-html :deep(pre code) {
  background: none;
  padding: 0;
}

.content-html :deep(ul),
.content-html :deep(ol) {
  margin: 16px 0;
  padding-left: 24px;
}

.content-html :deep(li) {
  margin: 8px 0;
}

.content-html :deep(a) {
  color: var(--accent-cyan);
  text-decoration: none;
  transition: all 0.3s ease;
}

.content-html :deep(a:hover) {
  color: var(--accent-cyan-dark);
  text-decoration: underline;
}

/* 评论区域样式 */
.comments-section {
  margin-top: 24px;
}

.comments-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.comments-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.comments-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 8px;
}

.title-icon {
  font-size: 18px;
}

/* 评论输入区域 */
.comment-input-area {
  margin-bottom: 25px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.input-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.input-label {
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
}

.comment-textarea :deep(.el-textarea__inner) {
  border-radius: 8px;
  border: 1px solid #ddd;
  font-size: 14px;
  line-height: 1.5;
}

.comment-textarea :deep(.el-textarea__inner:focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.comment-submit-area {
  margin-top: 12px;
  text-align: right;
}

.submit-btn {
  padding: 8px 20px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
}

.btn-icon {
  font-size: 14px;
}

/* 评论列表样式 */
.comments-list {
  max-height: 600px;
  overflow-y: auto;
}

.comment-item {
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item:last-child {
  border-bottom: none;
}

/* 主评论样式 */
.main-comment {
  position: relative;
}

.comment-user-info {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.comment-avatar {
  /* 移除特殊边框和阴影，使用默认样式 */
}

.user-details {
  margin-left: 12px;
  flex: 1;
}

.username {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
  display: block;
  margin-bottom: 2px;
}

.comment-time {
  color: #95a5a6;
  font-size: 12px;
}

.comment-content {
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

.comment-actions {
  margin-left: 48px;
  display: flex;
  gap: 15px;
}

.reply-btn,
.delete-btn {
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

.action-icon {
  font-size: 12px;
}

/* 回复输入框样式 */
.reply-input-area {
  margin-left: 48px;
  margin-top: 12px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.reply-input-wrapper {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.reply-input-content {
  flex: 1;
}

.reply-textarea :deep(.el-textarea__inner) {
  border-radius: 6px;
  font-size: 13px;
}

.reply-actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}

.reply-submit-btn {
  background: #409eff;
  border: none;
  color: white;
}

.reply-cancel-btn:hover {
  background: #f0f0f0;
  color: #606266;
}

/* 子评论样式 */
.replies-section {
  margin-left: 48px;
  margin-top: 15px;
  padding-left: 15px;
  border-left: 2px solid #e9ecef;
}

.reply-item {
  padding: 12px 0;
  border-bottom: 1px solid #f8f9fa;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-user-info {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.reply-avatar {
  /* 移除特殊边框和阴影 */
}

.reply-user-details {
  margin-left: 10px;
  flex: 1;
}

.reply-username {
  font-weight: 500;
  color: #2c3e50;
  font-size: 13px;
  display: block;
  margin-bottom: 2px;
}

.reply-time {
  color: #95a5a6;
  font-size: 11px;
}

.reply-content {
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

.reply-actions {
  margin-left: 38px;
  display: flex;
  gap: 12px;
}

/* 空状态样式 */
.empty-comments {
  margin: 40px 0;
}

.empty-icon {
  font-size: 48px;
  opacity: 0.6;
}

/* 滚动条样式 */
.comments-list::-webkit-scrollbar {
  width: 6px;
}

.comments-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.comments-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 10px;
}

.comments-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .front-diary-details {
    width: 95%;
    margin: 10px auto;
    padding: 16px;
  }

  .diary-title {
    font-size: 20px;
  }

  .diary-content {
    padding: 20px;
    font-size: 15px;
  }

  .back-btn {
    padding: 6px 16px;
    font-size: 14px;
  }

  .diary-meta {
    flex-direction: column;
    gap: 12px;
  }

  .comment-content,
  .comment-actions,
  .reply-input-area,
  .replies-section {
    margin-left: 0;
  }

  .comment-user-info,
  .reply-user-info {
    margin-bottom: 8px;
  }

  .username,
  .reply-username {
    font-size: 13px;
  }

  .comment-content,
  .reply-content {
    font-size: 14px;
  }

  .comment-input-area {
    padding: 16px;
  }

  .comments-title {
    font-size: 1.3rem;
  }
}

@media (max-width: 480px) {
  .diary-title {
    font-size: 1.3rem;
  }

  .diary-content {
    padding: 16px;
    font-size: 14px;
  }

  .content-html :deep(img) {
    margin: 12px 0;
  }

  .comment-input-area {
    padding: 12px;
  }

  .comments-section {
    margin-top: 20px;
  }

  .comment-item {
    padding: 16px 0;
  }

  .comment-textarea :deep(.el-textarea__inner) {
    font-size: 16px;
    padding: 12px;
  }

  .submit-btn {
    width: 100%;
    justify-content: center;
    padding: 12px;
  }

  .reply-input-wrapper {
    flex-direction: column;
    gap: 8px;
  }

  .reply-actions {
    width: 100%;
    justify-content: center;
  }

  .reply-submit-btn,
  .reply-cancel-btn {
    flex: 1;
  }
}
</style>
