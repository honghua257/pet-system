<template>
  <div class="front-store front-content">
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card shadow="hover">
        <el-space style="flex-wrap: wrap;gap: 15px">
          <el-input v-model="searchForm.storeName"
                    placeholder="请输入店铺名称"
                    style="width: 300px"
                    clearable/>
          <el-input v-model="searchForm.storeAddress"
                    placeholder="请输入店铺地址"
                    style="width: 300px"
                    clearable/>
          <el-button type="primary" :icon="Search" @click="search"></el-button>
        </el-space>
      </el-card>
      <el-card shadow="hover" v-for="(item,index) in listData">
        <el-descriptions
            class="margin-top"
            :title="item.storeName"
            :column="3"
            border
        >
          <template #extra>
            <el-button type="primary" @click="copy(item)">联系商家</el-button>
          </template>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <user/>
                </el-icon>
                {{ item.username }}
              </div>
            </template>
            店长
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <iphone/>
                </el-icon>
                电话
              </div>
            </template>
            {{ item.tel }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <location/>
                </el-icon>
                开业时间
              </div>
            </template>
            {{ item.createTime }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <office-building/>
                </el-icon>
                地址
              </div>
            </template>
            {{ item.storeAddress }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="pagination-card">
        <!--        分页-->
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
    </el-space>
  </div>
</template>


<script setup>
import request from "@/utils/http.js";
import {Iphone, Location, OfficeBuilding, Search, User} from "@element-plus/icons-vue";
import {ref, toRaw} from "vue";
import tools from "@/utils/tools.js";
import {ElMessage} from "element-plus";

const searchFormComponents = ref();
const tableComponents = ref();
const listData = ref([]);
const pageInfo = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
});
const searchForm = ref({
  storeName: undefined,
  storeAddress: undefined,
})

const userInfo = tools.getCurrentUser()

getPageList()

function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petStoreManager/page", {
    params: data
  }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) {
  pageInfo.value.pageNum = e
  getPageList()
}

function sizeChange(e) {
  pageInfo.value.pageSize = e
  getPageList()
  console.log(e)
}

function search() {
  pageInfo.value.pageNum = 1
  getPageList()
}

function resetSearch() {
  searchFormComponents.value.resetFields();
  getPageList()
}

function copy(row){
  navigator.clipboard.writeText(row.tel)
      .then(()=>{
        ElMessage.success("联系方式已复制")
      })
      .catch(err=>{
        console.log("复制失败",err)
      })
}
</script>

<!--<script setup>-->
<!--import {Location, OfficeBuilding, Search} from "@element-plus/icons-vue";-->
<!--import {ref} from "vue";-->


<!--function search(){-->

<!--}-->

<!--</script>-->

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

/* 店铺页面特定样式 */
.front-store {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* 卡片增强效果 */
.el-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.el-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

/* 描述列表美化 */
.el-descriptions {
  background: transparent;
}

.el-descriptions :deep(.el-descriptions__header) {
  background: var(--gradient-primary);
  color: white;
  border-radius: 8px 8px 0 0;
  padding: 12px 16px;
  margin: -16px -16px 16px -16px;
}

.el-descriptions :deep(.el-descriptions__title) {
  color: white;
  font-weight: 600;
}

/* 按钮美化 */
.el-button--primary {
  background: var(--gradient-primary);
  border: none;
  border-radius: 20px;
  font-weight: 600;
  transition: all 0.3s ease;
  color: var(--text-white);
}

.el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-primary);
  background: var(--primary-orange-dark);
}

.el-button--default {
  background: var(--bg-primary);
  border: 1px solid var(--gray-200);
  color: var(--text-primary);
  border-radius: 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.el-button--default:hover {
  border-color: var(--primary-orange);
  color: var(--primary-orange);
  box-shadow: 0 2px 8px var(--shadow-primary);
}

/* 链接样式美化 */
.el-link {
  color: var(--accent-cyan);
  font-weight: 500;
  transition: all 0.3s ease;
}

.el-link:hover {
  color: var(--accent-cyan-dark);
}

/* 搜索框美化 */
.el-input {
  border-radius: 20px;
}

.el-input :deep(.el-input__wrapper) {
  border-radius: 20px;
  transition: all 0.3s ease;
}

.el-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.el-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 2px 12px rgba(255, 154, 158, 0.3);
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
</style>
