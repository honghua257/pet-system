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
            </div>
          </div>
        </template>

        <div class="diary-content">
          <div v-html="info.content" class="content-html"></div>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script setup>
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import http from "@/utils/http.js";

const route = useRoute()
const router = useRouter()
const id = ref(route.params.id)
const info = ref({})

getInfo()
function getInfo(){
  http.get("/petDiary/selectById/"+id.value).then(res=>{
    info.value = res.data
  })
}

function goBack(){
  router.push('/petDiary')
}
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
  gap: 8px;
}

.diary-date {
  color: var(--text-secondary);
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 16px;
  background: var(--bg-accent);
  border-radius: 20px;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .front-diary-details {
    padding: 16px;
  }

  .diary-title {
    font-size: 1.5rem;
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
}
</style>
