<template>
  <div style="width: 60%;margin: 0 auto">
      <el-card>
        <el-space direction="vertical" alignment="left" style="width: 100%">
          <!-- 返回链接 -->
          <div style="margin-bottom: 20px;">
            <span class="back-link" @click="goBack">《返回</span>
          </div>
          
          <div style="text-align: center">
            <H2 style="font-size: 25px">{{ info.title }}</H2>
          </div>
          <div style="text-align: center">
            <el-space style="color: black;font-size: 14px">
            <span>发布时间:{{info.createTime}}</span>
          </el-space>
          </div>
          <div style="font-size: 30px;font-family: 'Arial',serif">
             <div v-html="info.content"></div>
          </div>

        </el-space>
      </el-card>
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
  http.get("/helpMessage/selectById/"+id.value).then(res=>{
    info.value = res.data
  })
}

function goBack(){
  router.push('/helpMessage')
}
</script>

<style scoped>
.back-link {
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
  text-decoration: none;
  transition: color 0.3s;
}

.back-link:hover {
  color: #66b1ff;
}
</style>
