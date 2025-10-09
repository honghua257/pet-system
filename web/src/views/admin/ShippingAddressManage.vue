<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFromComponents" :model="searchForm" inline>
          <el-form-item label="姓名" prop="name"><el-input v-model="searchForm.name" clearable/></el-form-item>
          <el-form-item label="用户名" prop="username"><el-input v-model="searchForm.username" clearable/></el-form-item>
          
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button type="info" :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="success" :icon="Plus" @click="add">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="BatchDelete(null)" :disabled="selectionRows.length<=0">批量删除</el-button>
        </el-space>
      </el-card>

      <el-card>
        <el-table :data="listData" style="width: 100%" border stripe tooltip-effect="dark" @selection-change="selectionChange">
          <el-table-column type="selection" width="55"/>
          <el-table-column property="id" label="ID" width="60"/>
          <el-table-column property="name" label="姓名"/>
          <el-table-column property="phone" label="电话"/>
          <el-table-column property="username" label="用户名"/>
          <el-table-column property="address" label="地址"/>
          <el-table-column property="createTime" label="创建时间"/>
          <el-table-column fixed="right" label="操作" width="150">
            <template #default="scope">
              <el-button type="primary" link :icon="Edit" @click="edit(scope.row)">编辑</el-button>
              <el-button type="danger" link :icon="Delete" @click="deleteOne(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 12px; text-align: right">
          <el-pagination background layout="prev, pager, next, sizes, total" :page-sizes="[10,20,50,100]" :total="pageInfo.total" :page-size="pageInfo.pageSize" :current-page="pageInfo.pageNum" @size-change="sizeChange" @current-change="currentChange"/>
        </div>
      </el-card>
    </el-space>

    <el-dialog v-model="dialogOpen" :title="formData.id?'编辑':'新增'" width="600px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="姓名" prop="name" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]"><el-input v-model="formData.name"/></el-form-item>
        <el-form-item label="电话" prop="phone" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]"><el-input v-model="formData.phone"/></el-form-item>
        <el-form-item label="地址" prop="address" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]"><el-input v-model="formData.address"/></el-form-item>
        <el-form-item label="用户名" prop="username"><el-input v-model="formData.username" disabled/></el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit" :icon="Check">提交</el-button>
          <el-button @click="closeDialog" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js"
import {ref, toRaw} from "vue";
import {Check, Close, Delete, Edit, Plus, Refresh, Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";

const searchFromComponents = ref()
const searchForm = ref({ name: undefined, username: undefined })

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })

getPageList()

function getPageList() {
  const data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/shippingAddress/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
function sizeChange(e) { pageInfo.value.pageSize = e; pageInfo.value.pageNum = 1; getPageList() }
function search() { pageInfo.value.pageNum = 1; getPageList() }
function resetSearch() { searchFromComponents.value.resetFields(); getPageList() }

const selectionRows = ref([])
function selectionChange(rows) { selectionRows.value = rows }
function BatchDelete(rows) {
  if (!rows) rows = selectionRows.value
  const ids = rows.map(item => item.id)
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据,是否确认删除`, '警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/shippingAddress/delete", { data: ids }).then(res => {
      if (!res) return
      ElMessage({ type: 'success', message: '删除成功' })
      getPageList()
    })
  }).catch(() => { ElMessage({ type: 'info', message: '已取消删除' }) })
}
function deleteOne(row) { BatchDelete([row]) }

const dialogOpen = ref(false)
const formData = ref({})
const formRef = ref()

function add() { formData.value = {}; dialogOpen.value = true }
function edit(row) { formData.value = Object.assign({}, row); dialogOpen.value = true }
function closeDialog() { dialogOpen.value = false }

function submit() {
  formRef.value.validate(valid => {
    if (!valid) return
    const isAdd = !formData.value.id
    const api = isAdd ? request.post : request.put
    const url = isAdd ? "/shippingAddress/add" : "/shippingAddress/update"
    api(url, formData.value).then(res => {
      if (!res) return
      ElMessage({ type: 'success', message: '提交成功' })
      dialogOpen.value = false
      getPageList()
    })
  })
}
</script>


