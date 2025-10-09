<template>
  <el-dialog
    v-model="visible"
    title="🐾宠物助手"
    width="700px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    class="ai-chat-dialog"
    @close="handleClose"
  >
    <div class="chat-container">
      <!-- 聊天历史区域 -->
      <div class="chat-history" ref="chatHistoryRef">
        <div v-if="chatHistory.length === 0" class="empty-state">
          <div class="pet-icons">
            <div class="pet-icon cat-icon">
              <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
                <!-- 猫咪头部 -->
                <circle cx="50" cy="45" r="25" fill="#ff9a9e"/>
                <!-- 猫耳朵 -->
                <path d="M30 35 L25 20 L35 30 Z" fill="#ff9a9e"/>
                <path d="M70 35 L75 20 L65 30 Z" fill="#ff9a9e"/>
                <!-- 眼睛 -->
                <circle cx="40" cy="42" r="3" fill="#2d3748"/>
                <circle cx="60" cy="42" r="3" fill="#2d3748"/>
                <!-- 鼻子 -->
                <path d="M48 48 L52 48 L50 52 Z" fill="#2d3748"/>
                <!-- 嘴巴 -->
                <path d="M50 52 Q45 55 42 52" stroke="#2d3748" stroke-width="2" fill="none"/>
                <path d="M50 52 Q55 55 58 52" stroke="#2d3748" stroke-width="2" fill="none"/>
                <!-- 身体 -->
                <ellipse cx="50" cy="75" rx="20" ry="15" fill="#ff9a9e"/>
                <!-- 尾巴 -->
                <path d="M70 75 Q85 70 80 85" stroke="#ff9a9e" stroke-width="8" fill="none" stroke-linecap="round"/>
                <!-- 爪子 -->
                <circle cx="40" cy="85" r="4" fill="#ff9a9e"/>
                <circle cx="60" cy="85" r="4" fill="#ff9a9e"/>
              </svg>
            </div>
            <div class="pet-icon dog-icon">
              <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
                <!-- 小狗头部 -->
                <circle cx="50" cy="45" r="25" fill="#a8edea"/>
                <!-- 狗耳朵（垂下来） -->
                <ellipse cx="32" cy="35" rx="8" ry="15" fill="#a8edea" transform="rotate(-20 32 35)"/>
                <ellipse cx="68" cy="35" rx="8" ry="15" fill="#a8edea" transform="rotate(20 68 35)"/>
                <!-- 眼睛 -->
                <circle cx="40" cy="42" r="3" fill="#2d3748"/>
                <circle cx="60" cy="42" r="3" fill="#2d3748"/>
                <!-- 鼻子 -->
                <circle cx="50" cy="50" r="3" fill="#2d3748"/>
                <!-- 嘴巴 -->
                <path d="M50 50 Q45 53 42 50" stroke="#2d3748" stroke-width="2" fill="none"/>
                <path d="M50 50 Q55 53 58 50" stroke="#2d3748" stroke-width="2" fill="none"/>
                <!-- 身体 -->
                <ellipse cx="50" cy="75" rx="20" ry="15" fill="#a8edea"/>
                <!-- 尾巴（摇摇的） -->
                <path d="M70 75 Q82 72 85 82" stroke="#a8edea" stroke-width="8" fill="none" stroke-linecap="round"/>
                <!-- 爪子 -->
                <circle cx="40" cy="85" r="4" fill="#a8edea"/>
                <circle cx="60" cy="85" r="4" fill="#a8edea"/>
              </svg>
            </div>
          </div>
          <p>你好！有什么可以帮助你的吗？</p>
        </div>

        <div
          v-for="(item, index) in chatHistory"
          :key="index"
          class="message-item"
        >
          <div class="message user-message">
            <div class="message-avatar">👤</div>
            <div class="message-content">
              <div class="message-text">{{ item.user }}</div>
              <div class="message-time">{{ item.userTime }}</div>
            </div>
          </div>

          <div class="message assistant-message">
            <div class="message-avatar">🤖</div>
            <div class="message-content">
              <div class="message-text" v-html="formatMessage(item.assistant)"></div>
              <div class="message-time">{{ item.assistantTime }}</div>
            </div>
          </div>
        </div>

        <!-- 正在输入状态 -->
        <div v-if="isTyping" class="message assistant-message">
          <div class="message-avatar">🤖</div>
          <div class="message-content">
            <div class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <el-input
          v-model="currentMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          :disabled="isTyping"
          @keyup.enter.ctrl="sendMessage"
          resize="none"
          class="chat-input"
        />
        <div class="input-actions">
          <el-button
            type="primary"
            :loading="isTyping"
            @click="sendMessage"
            :disabled="!currentMessage.trim()"
            size="small"
          >
            发送
          </el-button>
          <el-button
            @click="clearHistory"
            size="small"
            :disabled="chatHistory.length === 0"
            text
          >
            清空记录
          </el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import { marked } from 'marked'
import { ChatService } from '@/utils/chatService.js'

// 配置 marked
marked.setOptions({
  breaks: true,
  gfm: true
})

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'close'])

const visible = ref(false)
const currentMessage = ref('')
const chatHistory = ref([])
const isTyping = ref(false)
const chatHistoryRef = ref(null)

// 监听visible变化
watch(() => props.modelValue, (newVal) => {
  visible.value = newVal
})

watch(visible, (newVal) => {
  emit('update:modelValue', newVal)
  if (!newVal) {
    emit('close')
  }
})

// 格式化消息
const formatMessage = (message) => {
  return marked.parse(message)
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatHistoryRef.value) {
      chatHistoryRef.value.scrollTop = chatHistoryRef.value.scrollHeight
    }
  })
}

// 获取当前时间
const getCurrentTime = () => {
  const now = new Date()
  return now.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 发送消息
const sendMessage = async () => {
  if (!currentMessage.value.trim() || isTyping.value) return

  const userMessage = currentMessage.value.trim()
  currentMessage.value = ''

  // 添加用户消息到历史记录
  const userMsg = {
    user: userMessage,
    userTime: getCurrentTime(),
    assistant: '',
    assistantTime: ''
  }

  chatHistory.value.push(userMsg)
  scrollToBottom()

  try {
    isTyping.value = true

    let assistantResponse = ''

    // 使用流式聊天
    await ChatService.chatStream(userMessage, (chunk) => {
      assistantResponse += chunk

      // 更新最后一条消息的AI回复
      const lastIndex = chatHistory.value.length - 1
      if (lastIndex >= 0) {
        chatHistory.value[lastIndex].assistant = assistantResponse
        chatHistory.value[lastIndex].assistantTime = getCurrentTime()
        scrollToBottom()
      }
    })

  } catch (error) {
    console.error('Chat error:', error)
    const lastIndex = chatHistory.value.length - 1
    if (lastIndex >= 0) {
      chatHistory.value[lastIndex].assistant = '抱歉，发生了错误，请稍后再试。'
      chatHistory.value[lastIndex].assistantTime = getCurrentTime()
    }
  } finally {
    isTyping.value = false
    scrollToBottom()
  }
}

// 清空历史记录
const clearHistory = () => {
  chatHistory.value = []
}

// 处理关闭
const handleClose = () => {
  visible.value = false
}
</script>

<style scoped>
.ai-chat-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.ai-chat-dialog :deep(.el-dialog__header) {
  padding: 20px 24px 12px;
  border-bottom: 1px solid #f0f0f0;
  margin: 0;
}

.ai-chat-dialog :deep(.el-dialog__title) {
  font-size: 16px;
  font-weight: 600;
  color: #2d3748;
  display: flex;
  align-items: center;
  gap: 8px;
}

.ai-chat-dialog :deep(.el-dialog__title)::before {
  content: "🐾";
  font-size: 18px;
}

.ai-chat-dialog :deep(.el-dialog__headerbtn) {
  top: 20px;
  right: 20px;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 600px;
}

.chat-history {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: #fefefe;
  background-image:
    radial-gradient(circle at 20% 80%, rgba(255, 182, 193, 0.08) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(173, 216, 230, 0.08) 0%, transparent 50%);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #6b7280;
  text-align: center;
}

.empty-icon {
  width: 48px;
  height: 48px;
  margin-bottom: 12px;
  opacity: 0.8;
}

.empty-icon svg {
  filter: drop-shadow(0 2px 4px rgba(255, 182, 193, 0.3));
}

.pet-icons {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  justify-content: center;
  align-items: center;
}

.pet-icon {
  width: 64px;
  height: 64px;
  opacity: 0.9;
}

.pet-icon svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 4px 8px rgba(255, 154, 158, 0.3));
}

.cat-icon svg {
  filter: drop-shadow(0 4px 8px rgba(255, 154, 158, 0.3));
}

.dog-icon svg {
  filter: drop-shadow(0 4px 8px rgba(168, 237, 234, 0.3));
}

.empty-state p {
  margin: 0;
  font-size: 14px;
  color: #718096;
  font-weight: 500;
}

.message-item {
  margin-bottom: 20px;
}

.message {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  margin-right: 12px;
  flex-shrink: 0;
  font-weight: 600;
}

.user-message .message-avatar {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 50%, #fecfef 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(255, 154, 158, 0.3);
}

.assistant-message .message-avatar {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(168, 237, 234, 0.3);
}

.message-content {
  flex: 1;
  max-width: 80%;
}

.message-text {
  background: white;
  padding: 12px 16px;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  line-height: 1.5;
  word-wrap: break-word;
  font-size: 14px;
  border: 1px solid #f0f4f8;
}

.user-message .message-text {
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
  color: white;
  border: none;
  box-shadow: 0 4px 12px rgba(255, 154, 158, 0.25);
}

.assistant-message .message-text {
  background: white;
  color: #2d3748;
}

.message-time {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 6px;
  padding: 0 4px;
}

/* Markdown样式 */
.message-text :deep(h1),
.message-text :deep(h2),
.message-text :deep(h3),
.message-text :deep(h4),
.message-text :deep(h5),
.message-text :deep(h6) {
  margin: 8px 0;
  font-weight: 600;
  color: inherit;
}

.message-text :deep(p) {
  margin: 8px 0;
}

.message-text :deep(code) {
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 0.85em;
}

.message-text :deep(pre) {
  background: #f3f4f6;
  padding: 12px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 8px 0;
  border: 1px solid #e5e7eb;
}

.message-text :deep(pre code) {
  background: none;
  padding: 0;
  border: none;
}

.message-text :deep(a) {
  color: #667eea;
  text-decoration: none;
}

.message-text :deep(a:hover) {
  text-decoration: underline;
}

.message-text :deep(ul),
.message-text :deep(ol) {
  margin: 8px 0;
  padding-left: 20px;
}

.message-text :deep(li) {
  margin: 4px 0;
}

.typing-indicator {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 12px 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(1) {
  animation-delay: 0s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-6px);
  }
}

.input-area {
  padding: 20px 24px;
  background: white;
  border-top: 1px solid #f0f0f0;
}

.chat-input {
  margin-bottom: 12px;
}

.chat-input :deep(.el-textarea__inner) {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s;
}

.chat-input :deep(.el-textarea__inner):focus {
  border-color: #ff9a9e;
  box-shadow: 0 0 0 3px rgba(255, 154, 158, 0.1);
}

.input-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

/* 滚动条样式 */
.chat-history::-webkit-scrollbar {
  width: 4px;
}

.chat-history::-webkit-scrollbar-track {
  background: transparent;
}

.chat-history::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 2px;
}

.chat-history::-webkit-scrollbar-thumb:hover {
  background: #9ca3af;
}
</style>