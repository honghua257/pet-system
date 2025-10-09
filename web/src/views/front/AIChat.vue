<template>
  <div class="ai-chat-container">
    <el-card class="chat-card">
      <template #header>
        <div class="chat-header">
          <h2>🤖 AI智能助手</h2>
          <p>我是你的AI助手km，很高兴为你服务！</p>
        </div>
      </template>

      <el-input
        v-model="message"
        placeholder="请输入您的问题..."
        :disabled="loading"
        @keyup.enter="sendMessage"
        size="large"
      />

      <el-button
        type="primary"
        :loading="loading"
        @click="sendMessage"
        :disabled="!message.trim()"
        size="large"
        style="margin-top: 15px; width: 100%"
      >
        发送消息
      </el-button>

      <div
        v-if="response"
        class="ai-response"
      >
        <div class="response-header">
          <strong>🤖 AI回复：</strong>
        </div>
        <div class="response-content" v-html="marked.parse(response)"></div>
      </div>

      <!-- 聊天历史记录 -->
      <div v-if="chatHistory.length > 0" class="chat-history">
        <div class="history-header">
          <strong>💬 聊天记录</strong>
        </div>
        <div class="history-items">
          <div
            v-for="(item, index) in chatHistory"
            :key="index"
            class="history-item"
          >
            <div class="user-message">
              <strong>👤 你：</strong> {{ item.user }}
            </div>
            <div class="assistant-message">
              <strong>🤖 AI：</strong>
              <span v-html="marked.parse(item.assistant)"></span>
            </div>
            <div class="message-divider"></div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ChatService } from '@/utils/chatService.js'
import { marked } from 'marked'

// 配置 marked 选项
marked.setOptions({
  breaks: true, // 转换换行符为 <br>
  gfm: true // 启用 GitHub 风格的 Markdown
})

const message = ref('')
const response = ref('')
const loading = ref(false)
const chatHistory = ref([])

const sendMessage = async () => {
  if (!message.value.trim()) return

  const userMessage = message.value.trim()
  message.value = ''

  try {
    loading.value = true
    response.value = ''

    // 使用ChatService的流式方法
    await ChatService.chatStream(userMessage, (chunk) => {
      response.value += chunk
    })

    // 保存到聊天历史
    chatHistory.value.push({
      user: userMessage,
      assistant: response.value
    })

    // 限制历史记录数量
    if (chatHistory.value.length > 10) {
      chatHistory.value.shift()
    }

  } catch (error) {
    console.error('Chat error:', error)
    response.value = '抱歉，发生了错误，请稍后再试。'
  } finally {
    loading.value = false
  }
}

// 清空聊天记录
const clearHistory = () => {
  chatHistory.value = []
  response.value = ''
}
</script>

<style scoped>
.ai-chat-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.chat-card {
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.chat-header {
  text-align: center;
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  padding: 20px;
  margin: -20px -20px 20px -20px;
  border-radius: 20px 20px 0 0;
}

.chat-header h2 {
  margin: 0 0 10px 0;
  font-size: 24px;
}

.chat-header p {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

.ai-response {
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #667eea;
}

.response-header {
  color: #667eea;
  font-size: 16px;
  margin-bottom: 10px;
}

.response-content {
  line-height: 1.6;
  word-wrap: break-word;
}

/* Markdown样式 */
.response-content :deep(h1),
.response-content :deep(h2),
.response-content :deep(h3),
.response-content :deep(h4),
.response-content :deep(h5),
.response-content :deep(h6) {
  color: #2c3e50;
  margin: 16px 0 12px 0;
  font-weight: 600;
}

.response-content :deep(h1) { font-size: 1.8em; border-bottom: 2px solid #667eea; padding-bottom: 8px; }
.response-content :deep(h2) { font-size: 1.5em; border-bottom: 1px solid #667eea; padding-bottom: 6px; }
.response-content :deep(h3) { font-size: 1.3em; color: #34495e; }
.response-content :deep(h4) { font-size: 1.2em; color: #5d6d7e; }
.response-content :deep(h5) { font-size: 1.1em; color: #707b7c; }
.response-content :deep(h6) { font-size: 1em; color: #85929e; }

.response-content :deep(p) {
  margin: 12px 0;
  line-height: 1.7;
  color: #2c3e50;
}

.response-content :deep(a) {
  color: #667eea;
  text-decoration: none;
  border-bottom: 1px dotted transparent;
  transition: all 0.3s ease;
}

.response-content :deep(a:hover) {
  color: #764ba2;
  border-bottom-color: #764ba2;
}

.response-content :deep(code) {
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  padding: 4px 8px;
  border-radius: 6px;
  font-family: 'Fira Code', 'Courier New', monospace;
  font-size: 0.9em;
  color: #e91e63;
  border: 1px solid #dee2e6;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.response-content :deep(pre) {
  background: linear-gradient(135deg, #2c3e50, #34495e);
  padding: 20px;
  border-radius: 12px;
  overflow-x: auto;
  margin: 16px 0;
  border: 1px solid #1a252f;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
  position: relative;
}

.response-content :deep(pre code) {
  background: transparent;
  padding: 0;
  color: #ecf0f1;
  border: none;
  box-shadow: none;
  font-family: 'Fira Code', 'Courier New', monospace;
}

.response-content :deep(blockquote) {
  border-left: 4px solid #667eea;
  padding-left: 20px;
  margin: 16px 0;
  color: #5d6d7e;
  font-style: italic;
  background: linear-gradient(90deg, rgba(102, 126, 234, 0.1), transparent);
  border-radius: 0 8px 8px 0;
}

.response-content :deep(ul),
.response-content :deep(ol) {
  padding-left: 25px;
  margin: 12px 0;
}

.response-content :deep(li) {
  margin: 8px 0;
  color: #2c3e50;
  line-height: 1.6;
}

.response-content :deep(ul li::marker) {
  color: #667eea;
}

.response-content :deep(ol li::marker) {
  color: #764ba2;
}

.response-content :deep(strong) {
  color: #2c3e50;
  font-weight: 600;
}

.response-content :deep(em) {
  color: #7f8c8d;
  font-style: italic;
}

.response-content :deep(hr) {
  border: none;
  height: 2px;
  background: linear-gradient(90deg, #667eea, #764ba2, transparent);
  margin: 24px 0;
  border-radius: 2px;
}

.response-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.response-content :deep(th),
.response-content :deep(td) {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #dee2e6;
}

.response-content :deep(th) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  font-weight: 600;
}

.response-content :deep(tr:nth-child(even)) {
  background: #f8f9fa;
}

.response-content :deep(tr:hover) {
  background: rgba(102, 126, 234, 0.1);
}

.response-content :deep(.task-list-item) {
  list-style: none;
  margin-left: -20px;
}

.response-content :deep(.task-list-item input) {
  margin-right: 8px;
}

/* 高亮块样式 */
.response-content :deep(.highlight) {
  background: linear-gradient(120deg, rgba(255, 235, 59, 0.3), rgba(255, 193, 7, 0.2));
  padding: 2px 4px;
  border-radius: 4px;
}

/* 代码块语言标签 */
.response-content :deep(pre::before) {
  content: attr(data-lang);
  position: absolute;
  top: 8px;
  right: 12px;
  background: rgba(255, 255, 255, 0.1);
  color: #bdc3c7;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8em;
  font-family: 'Fira Code', monospace;
}

.chat-history {
  margin-top: 30px;
  border-top: 2px solid #e9ecef;
  padding-top: 20px;
}

.history-header {
  color: #495057;
  font-size: 16px;
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.history-items {
  max-height: 400px;
  overflow-y: auto;
}

.history-item {
  margin-bottom: 15px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-message {
  color: #667eea;
  font-weight: 500;
  margin-bottom: 8px;
}

.assistant-message {
  color: #333;
  line-height: 1.5;
}

.assistant-message :deep(strong) {
  color: #764ba2;
}

.message-divider {
  height: 1px;
  background: #e9ecef;
  margin-top: 12px;
}

/* 清空历史按钮 */
.clear-history-btn {
  background: #dc3545;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.clear-history-btn:hover {
  background: #c82333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-chat-container {
    padding: 10px;
  }

  .chat-header h2 {
    font-size: 20px;
  }

  .ai-response {
    padding: 15px;
  }
}

/* 滚动条样式 */
.history-items::-webkit-scrollbar {
  width: 6px;
}

.history-items::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.history-items::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 10px;
}

.history-items::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>