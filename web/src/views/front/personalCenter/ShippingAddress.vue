<template>
  <div class="shipping-address-container">
    <div class="header-section">
      <el-button type="primary" @click="add" class="add-address-btn">
        <i class="el-icon-plus"></i> 新增地址
      </el-button>
    </div>
    <div class="table-section">
      <el-card class="data-table-card">
        <el-table :data="listData" class="data-table" border>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="phone" label="电话" />
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="address" label="地址" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column fixed="right" label="操作" width="140">
            <template #default="scope">
              <el-button link type="primary" @click="edit(scope.row)">编辑</el-button>
              <el-button link type="danger" @click="deleteOne(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-wrapper">
          <el-pagination
              @current-change="currentChange"
              @size-change="sizeChange"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.pageNum"
              background
              layout="total,sizes, prev, pager, next"
              :total="pageInfo.total"
              class="custom-pagination"
          />
        </div>
      </el-card>
    </div>

    <el-dialog v-model="dialogOpen" :title="formData.id ? '编辑地址' : '新增地址'" width="500px">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="姓名" prop="name" :rules="[{ required: true, message: '请输入姓名' }]"><el-input v-model="formData.name"/></el-form-item>
        <el-form-item label="电话" prop="phone" :rules="[{ required: true, message: '请输入电话' }]"><el-input v-model="formData.phone"/></el-form-item>
        <el-form-item label="地址" prop="address" :rules="[{ required: true, message: '请输入地址' }]"><el-input v-model="formData.address"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogOpen=false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { ref, toRaw } from "vue";

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })

getPageList()

function getPageList(){
  const params = Object.assign({}, toRaw(pageInfo.value))
  request.get('/shippingAddress/page', { params }).then(res => {
    if(!res){ return }
    listData.value = res.data?.list || []
    pageInfo.value.total = res.data?.total || 0
  })
}

function currentChange(p){ pageInfo.value.pageNum = p; getPageList() }
function sizeChange(s){ pageInfo.value.pageSize = s; getPageList() }

const dialogOpen = ref(false)
const formRef = ref()
const formData = ref({})

function add(){ formData.value = {}; dialogOpen.value = true }
function edit(row){ formData.value = Object.assign({}, row); dialogOpen.value = true }

function submit(){
  formRef.value.validate(valid => {
    if(!valid) return
    const api = formData.value.id ? request.put : request.post
    const url = formData.value.id ? '/shippingAddress/update' : '/shippingAddress/add'
    api(url, formData.value).then(res => {
      if(!res){ return }
      ElMessage({ message: '操作成功', type: 'success' })
      dialogOpen.value = false
      getPageList()
    })
  })
}

function deleteOne(row){
  ElMessageBox.confirm(`此操作将永久删除该地址, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete('/shippingAddress/delete', { data: [row.id] }).then(res => {
      if(!res){ return }
      ElMessage({ message: '操作成功', type: 'success' })
      getPageList()
    })
  }).catch(() => {})
}
</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

/* 收货地址页面样式 */
.shipping-address-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 头部区域 */
.header-section {
  margin-bottom: 20px;
  background: var(--bg-primary);
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 2px 8px var(--shadow-neutral);
}

/* 新增地址按钮样式 */
.add-address-btn {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;
  border: none;
  background: var(--gradient-primary);
  color: white;
  box-shadow: 0 2px 8px var(--shadow-primary);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.add-address-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-primary);
  background: linear-gradient(135deg, #ff8c42, #ff6b35);
}

.add-address-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 6px var(--shadow-primary);
}

/* 表格区域 */
.table-section {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.data-table-card {
  flex: 1;
  background: var(--bg-primary);
  border-radius: 16px;
  box-shadow: 0 4px 16px var(--shadow-neutral);
  border: 1px solid var(--gray-200);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 表格样式 */
.data-table {
  flex: 1;
  --el-table-border-color: var(--gray-200);
  --el-table-header-bg-color: var(--bg-secondary);
}

.data-table :deep(.el-table__header) {
  background: var(--bg-secondary);
  font-weight: 600;
  color: var(--text-primary);
}

.data-table :deep(.el-table__body tr) {
  transition: background-color 0.3s ease;
}

.data-table :deep(.el-table__body tr:hover) {
  background-color: var(--bg-accent);
}

.data-table :deep(.el-table__cell) {
  padding: 12px 8px;
  border-bottom: 1px solid var(--gray-200);
}

/* 分页样式 */
.pagination-wrapper {
  padding: 20px;
  display: flex;
  justify-content: center;
  background: var(--bg-primary);
  border-top: 1px solid var(--gray-200);
}

.custom-pagination {
  --el-pagination-button-bg-color: var(--bg-primary);
  --el-pagination-hover-color: var(--primary-orange);
}

.custom-pagination :deep(.el-pager li.is-active) {
  background: var(--gradient-primary);
  color: white;
  border-radius: 50%;
}

.custom-pagination :deep(.el-pager li:hover) {
  color: var(--primary-orange);
}

/* 空状态 */
.shipping-address-container :deep(.el-empty) {
  background: var(--bg-secondary);
  border-radius: 16px;
  padding: 40px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-section {
    padding: 12px;
    margin-bottom: 16px;
  }

  .data-table :deep(.el-table__cell) {
    padding: 8px 6px;
    font-size: 14px;
  }

  .pagination-wrapper {
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .header-section {
    padding: 8px;
    margin-bottom: 12px;
  }

  .data-table :deep(.el-table__cell) {
    padding: 6px 4px;
    font-size: 13px;
  }

  .pagination-wrapper {
    padding: 12px;
  }
}
</style>

