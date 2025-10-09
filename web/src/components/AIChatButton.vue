<template>
  <div class="ai-chat-button" @click="openChat">
    <el-tooltip content="🐾 宠物AI助手" placement="bottom">
      <div class="ai-button">
        <span class="ai-text">🐾 宠物助手</span>
      </div>
    </el-tooltip>
  </div>
</template>

<script setup>
const emit = defineEmits(['open'])

const openChat = () => {
  emit('open')
}
</script>

<style scoped>
.ai-chat-button {
  cursor: pointer;
  user-select: none;
}

.ai-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 20px;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
}

.ai-button:hover {
  transform: translateY(-1px);
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.2);
}

.ai-button:active {
  transform: translateY(0);
  background: rgba(255, 255, 255, 0.25);
}

.ai-text {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
}
</style>
