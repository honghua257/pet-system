<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFromComponents" :model="searchForm" inline>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable placeholder="请输入用户名称"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" clearable placeholder="请选择" style="width: 150px">
              <el-option label="待支付" value="待支付"/>
              <el-option label="待发货" value="待发货"/>
              <el-option label="待收货" value="待收货"/>
              <el-option label="已完成" value="已完成"/>
              <el-option label="已取消" value="已取消"/>
            </el-select>
          </el-form-item>
          <el-form-item label="收货人姓名" prop="receiverName">
            <el-input v-model="searchForm.receiverName" clearable placeholder="请输入收货人姓名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <el-card>
        <el-table :data="listData" style="width: 100%" border stripe tooltip-effect="dark">
          <el-table-column property="id" label="ID" width="60"/>
          <el-table-column property="productName" label="商品名称" width="150"/>
          <el-table-column property="username" label="用户名称" width="120"/>
          <el-table-column property="price" label="价格" width="100">
            <template #default="scope">
              ¥{{ scope.row.price }}
            </template>
          </el-table-column>
          <el-table-column property="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column property="receiverName" label="收货人" width="100"/>
          <el-table-column property="receiverPhone" label="收货电话" width="120"/>
          <el-table-column property="receiverAddress" label="收货地址" width="200" show-overflow-tooltip/>
          <el-table-column property="createTime" label="创建时间" width="180"/>
          <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
              <el-button type="success" :icon="Edit" @click="edit(scope.row)">编辑</el-button>
              <el-button
                v-if="scope.row.status === '待发货'"
                type="warning"
                @click="ship(scope.row)">
                发货
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 12px; text-align: right">
          <el-pagination
              background
              layout="prev, pager, next, sizes, total"
              :page-sizes="[10,20,50,100]"
              :total="pageInfo.total"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.pageNum"
              @size-change="sizeChange"
              @current-change="currentChange"
          />
        </div>
      </el-card>
    </el-space>

    <el-dialog v-model="dialogOpen" title="编辑订单" width="800px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品名称" prop="productName" :rules="[{ required: true, message: '请输入商品名称', trigger: 'blur' }]">
              <el-input v-model="formData.productName"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="username" :rules="[{ required: true, message: '请输入用户名称', trigger: 'blur' }]">
              <el-input v-model="formData.username"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price" :rules="[{ required: true, message: '请输入价格', trigger: 'blur' }]">
              <el-input v-model.number="formData.price" type="number" placeholder="请输入价格"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status" :rules="[{ required: true, message: '请选择状态', trigger: 'change' }]">
              <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="待支付" value="待支付"/>
                <el-option label="待发货" value="待发货"/>
                <el-option label="待收货" value="待收货"/>
                <el-option label="已完成" value="已完成"/>
                <el-option label="已取消" value="已取消"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="收货人姓名" prop="receiverName" :rules="[{ required: true, message: '请输入收货人姓名', trigger: 'blur' }]">
              <el-input v-model="formData.receiverName"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收货人电话" prop="receiverPhone" :rules="[{ required: true, message: '请输入收货人电话', trigger: 'blur' }]">
              <el-input v-model="formData.receiverPhone"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收货地址" prop="receiverAddress" :rules="[{ required: true, message: '请输入收货地址', trigger: 'blur' }]">
          <el-input v-model="formData.receiverAddress" type="textarea" :rows="3"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :icon="Check" @click="submit">提交</el-button>
          <el-button :icon="Close" @click="closeDialog">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw} from "vue";
import {Check, Close, Edit, Refresh, Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";

const searchFromComponents = ref()
const searchForm = ref({ 
  username: undefined, 
  status: undefined, 
  receiverName: undefined 
})

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })

getPageList()

function getPageList() {
  const data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/productOrder/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
function sizeChange(e) { pageInfo.value.pageSize = e; pageInfo.value.pageNum = 1; getPageList() }
function search() { pageInfo.value.pageNum = 1; getPageList() }
function resetSearch() { searchFromComponents.value.resetFields(); getPageList() }


const dialogOpen = ref(false)
const formData = ref({})
const formRef = ref()

function edit(row) { formData.value = Object.assign({}, row); dialogOpen.value = true }
function closeDialog() { dialogOpen.value = false }

function submit() {
  formRef.value.validate(valid => {
    if (!valid) return
    request.put("/productOrder/update", formData.value).then(res => {
      if (!res) return
      ElMessage({ type: 'success', message: '修改成功' })
      dialogOpen.value = false
      getPageList()
    })
  })
}

function getStatusType(status) {
  const statusMap = {
    '待支付': 'warning',
    '待发货': 'warning',
    '待收货': 'primary',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return statusMap[status] || 'info'
}

// 发货操作：将状态从“待发货”改为“待收货”
function ship(row) {
  ElMessageBox.confirm(`确认对订单[${row.id}]进行发货操作？`, '发货确认', {
    confirmButtonText: '确认发货',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    const payload = { ...row, status: '待收货' }
    request.put('/productOrder/update', payload).then(res => {
      if (!res) return
      ElMessage({ type: 'success', message: '发货成功' })
      getPageList()
    })
  }).catch(() => {})
}
</script>
