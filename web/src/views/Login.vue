<template>
  <div class="main-context">
    <el-card class="box-card">
      <el-space direction="vertical" style="width: 100%" size="large">
        <el-space>
<!--          <img src="../assets/logo.png" width="100%" style="width: 55px">-->
          <el-space direction="vertical" style="width: 100%" size="small">
            <h2 style="font-style: oblique; color: #333; margin: 0;">宠物综合管理系统</h2>
          </el-space>
        </el-space>
        <el-form :model="formData" label-width="0px" ref="formRef">
          <el-form-item label="" prop="username"
                        :rules="[{required:true,message:'请输入用户名',trigger:[ 'blur','change']}]">
            <el-input
                :prefix-icon="User"
                placeholder="请输入账号"
                v-model.trim="formData.username"
                clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="password"
                        :rules="[{required:true,message:'请输入密码',trigger:[ 'blur','change']}]">
            <el-input
                :prefix-icon="Lock"
                placeholder="请输入密码"
                show-password
                style="width: 240px"
                v-model.trim="formData.password"
                clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="type">
            <el-select v-model="formData.type" placeholder="请选择用户类型" style="width: 240px">
              <el-option label="管理员" value="ADMIN"></el-option>
              <el-option label="用户" value="USER"></el-option>
              <el-option label="宠物店" value="PET_STORE_MANAGER"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="">
            <el-space direction="vertical" alignment="left" style="width: 100%">
              <el-button @click="submitForm()" type="primary" style="width: 100%; background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%); border: none; border-radius: 20px; padding: 12px 0; font-weight: 500;">登 录</el-button>
              <router-link tag="span" :to="{path:'register'}">
                <span style="float: right; color: #666;">没有账号？去注册</span>
              </router-link>
              <router-link tag="span" :to="{path:'retrievePassword'}">
                <span class="button" style="float: right; color: #666;">忘记密码？</span>
              </router-link>
            </el-space>
          </el-form-item>
        </el-form>
      </el-space>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import http from "@/utils/http.js";
import {User, Lock} from "@element-plus/icons-vue";
import router from "@/router/index.js";

const formData = ref({
  username: '',
  password: '',
  type: 'ADMIN'
});

const formRef = ref(null);
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (!valid) {

    }
    http.post("/common/login", formData.value).then(res => {
      if (!res) {
        return
      }
      ElMessage({
        message: "登录成功，正在跳转",
        type: "success"
      });
      console.log(res.data)
      localStorage.setItem("token", res.data);
      http.get("/common/currentUser").then(res1 => {
        let currentUser = res1.data;
        localStorage.setItem("currentUser", JSON.stringify(currentUser));
        // 使用 nextTick 确保 DOM 更新完成后再跳转
        setTimeout(() => {
          if (currentUser.type !== "USER") {
            router.push({path: "/admin"});
          } else {
            router.push({path: "/"});
          }
        }, 100);
      })
    })
  });
};
</script>
<style scoped>
.main-context {
  height: 100vh; /* 使 .app 高度为视口高度 */
  background: url("../assets/loginBackground.png") no-repeat center center fixed;
  background-size: cover; /* 使用 cover 保持图片比例 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: #333; /* 深色文字适配白色背景 */
}

.box-card {
  width: 320px;
  margin: 0 auto;
  text-align: center;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}
</style>
