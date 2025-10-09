<template>
  <el-container class="my-container">
    <!-- 顶部导航（flex 布局，避免文字被压缩） -->
    <el-header class="my-header gradient-header">
      <div class="nav-wrapper">
        <!-- 左侧 Logo -->
        <div class="logo" @click="router.push('/')">
          <div class="logo-content">
            <span class="logo-paw">🐾</span>
            <h1 class="site-title">宠物综合服务</h1>
          </div>
        </div>

        <!-- 中间菜单（自适应） -->
        <div class="nav-center">
          <el-menu
              :default-active="route.path"
              mode="horizontal"
              router
              @select="handleSelect"
              class="nav-menu"
              :collapse="false"
          >
            <el-menu-item index="/index">首页</el-menu-item>
            <el-menu-item index="/myPet">我的宠物</el-menu-item>
            <el-menu-item index="/helpMessage">求助信息</el-menu-item>
            <el-menu-item index="/petStore">查找店铺</el-menu-item>
            <el-menu-item index="/petDiary">宠物日记</el-menu-item>
            <el-menu-item index="/product">宠物商城</el-menu-item>
            <el-menu-item index="/personalCenter">个人中心</el-menu-item>
          </el-menu>
        </div>

        <!-- 右侧用户区域 -->
        <div class="user-area">
          <!-- AI助手按钮 -->
          <AIChatButton @open="openChatDialog" class="ai-chat-btn" />

          <el-dropdown v-if="isUserLogin">
            <div class="user-info" @click.stop>
              <el-avatar shape="circle" :size="32" :src="currentUser.avatarUrl" />
              <span class="username">{{ currentUser.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <span @click="editCurrentUser">个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item>
                  <span @click="editPassword">修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item>
                  <span @click="balanceInfo">余额/充值</span>
                </el-dropdown-item>
                <el-dropdown-item divided>
                  <span @click="logout">退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>

    <!-- 主体 -->
    <el-main class="my-main">
      <div class="front-content">
        <router-view />
      </div>

      <!-- AI聊天弹窗 -->
      <AIChatDialog v-model="chatDialogVisible" @close="chatDialogVisible = false" />
    </el-main>

    <!-- 页脚 -->
    <el-footer class="my-footer">
      <p>版权归 红花 所有</p>
      <p>© 2025 宠物综合服务平台</p>
    </el-footer>
  </el-container>
</template>

<script setup>
import tools from "@/utils/tools.js";
import { ref, onMounted } from "vue";
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import {useRoute} from "vue-router";
import AIChatButton from "@/components/AIChatButton.vue";
import AIChatDialog from "@/components/AIChatDialog.vue";

const isUserLogin = ref(tools.isLogin());
const currentUser = ref(tools.getCurrentUser());
const route = useRoute();
const chatDialogVisible = ref(false);

// 打开聊天对话框
const openChatDialog = () => {
  chatDialogVisible.value = true;
};



onMounted(() => {
  // 延迟检查，避免与登录跳转冲突
  setTimeout(() => {
    if (currentUser.value === null) {
      window.location.href = "/login";
    }
    if (currentUser.value && currentUser.value.type !== "USER") {
      router.push({path: "/admin"});
    }
  }, 200);
});

function handleSelect(key /*, keyPath */) {
  // el-menu 设置了 router，通常不需要手动跳转，但我们也可以：
  if (key) router.push(key);
}

function logout() {
  ElMessage({
    message: "退出登录成功，正在跳转",
    type: "success",
  });
  localStorage.clear();
  router.push({path: "/login"});
}

function editCurrentUser() {
  router.push({path: "/editCurrentUser"});
}

function balanceInfo(){
  router.push({path:"/balanceInfo"});
}

function editPassword() {
  router.push({path: "/editPassword"});
}
</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";
/* 容器 */
.my-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

/* 头部 */
.my-header {
  color: var(--text-white);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

/* nav-wrapper 使用 flex，左右两端最小宽度保护，避免被压缩 */
.nav-wrapper {
  display: flex;
  align-items: center;
  width: 100%;
  gap: 12px;
  justify-content: space-between;
}

/* logo 保证不被压缩 */
.logo {
  flex: 0 0 auto; /* 不缩放 */
  min-width: 220px; /* 根据需要调整，确保文字不被截断 */
  cursor: pointer;
  display: flex;
  align-items: center;
}

.logo-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-paw {
  font-size: 24px;
  animation: float 3s ease-in-out infinite;
}

/* 标题 */
.site-title {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: var(--text-white);
  white-space: nowrap; /* 不换行 */
  overflow: hidden;
  text-overflow: ellipsis; /* 超出省略 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  transition: var(--transition-normal);
}

.site-title:hover {
  transform: scale(1.05);
}

/* 中间菜单：靠左显示 */
.nav-center {
  flex: 1 1 auto;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding-left: 20px;
}

/* 菜单样式 */
.nav-menu {
  background: transparent;
  border-bottom: none !important;
  width: 100%;
  max-width: 800px; /* 增加最大宽度以容纳所有菜单项 */
  overflow: visible; /* 确保菜单项不被隐藏 */
}

.nav-menu .el-menu-item {
  color: var(--text-white) !important;
  font-size: 14px;
  padding: 8px 16px !important;
  border-radius: 20px;
  margin: 0 6px;
  white-space: nowrap; /* 防止文字换行 */
  flex-shrink: 0; /* 防止菜单项被压缩 */
  transition: var(--transition-normal);
  background: transparent !important;
  border: 1px solid transparent;
  border-bottom: none !important;
}

.nav-menu .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.15) !important;
  border-color: var(--accent-cyan) !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px var(--shadow-accent);
  color: var(--accent-cyan) !important;
}

.nav-menu .el-menu-item.is-active {
  color: var(--text-white) !important;
  background: var(--primary-orange) !important;
  border-color: var(--primary-orange) !important;
  font-weight: 600;
  box-shadow: 0 2px 8px var(--shadow-primary);
  border-bottom: none !important;
}

/* 用户区域：不被压缩 */
.user-area {
  flex: 0 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

/* 用户信息 */
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: var(--transition-normal);
  color: var(--text-white);
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

/* 名称超长处理 */
.username {
  color: #fff;
  font-size: 14px;
  max-width: 110px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 主体 */
.my-main {
  flex: 1;
  background: var(--bg-secondary);
  padding: 0;
  overflow: auto;
  position: relative;
}

/* 添加背景装饰 */
.my-main::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 25% 25%, rgba(255, 168, 110, 0.04) 0%, transparent 50%),
              radial-gradient(circle at 75% 75%, rgba(110, 212, 200, 0.04) 0%, transparent 50%),
              radial-gradient(circle at 85% 15%, rgba(255, 168, 110, 0.03) 0%, transparent 50%);
  animation: bgFloat 20s ease-in-out infinite;
  pointer-events: none;
  z-index: 0;
}

/* 页脚 */
.my-footer {
  font-size: 14px;
  padding: 12px;
  color: #666;
  background: #fff;
  border-top: 1px solid #eaeaea;
  text-align: center;
}

/* 响应式：屏幕窄时适当缩小 logo 最小宽度 / 菜单改为左对齐 */
@media (max-width: 1200px) {
  .logo {
    min-width: 180px;
  }

  .nav-menu {
    max-width: 600px;
  }
  
  .nav-menu .el-menu-item {
    font-size: 13px;
    padding: 6px 8px;
    margin: 0 2px;
  }
}

@media (max-width: 900px) {
  .logo {
    min-width: 160px;
  }

  .nav-menu {
    max-width: 500px;
  }
}

@media (max-width: 640px) {
  .site-title {
    font-size: 16px;
  }

  .nav-center {
    display: none;
  }

  /* 更复杂的场景可考虑汉堡菜单 */
}
</style>
