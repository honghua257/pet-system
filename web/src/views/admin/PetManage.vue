<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <!-- 搜索区 -->
      <el-card>
        <el-form ref="searchFromComponents" :model="searchForm" inline>
          <el-form-item label="名称" prop="name">
            <el-input v-model="searchForm.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="类型" prop="petTypeId">
            <el-select v-model="searchForm.petTypeId" placeholder="请选择" clearable filterable>
              <el-option
                  v-for="item in petTypeList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属用户" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button type="info" :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>

        <el-space>
          <el-button type="success" :icon="Plus" @click="add">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="BatchDelete(null)" :disabled="selectionRows.length <= 0">
            批量删除
          </el-button>
        </el-space>
      </el-card>

      <!-- 表格区 -->
      <el-card>
        <el-table :data="listData" style="width: 100%" border stripe tooltip-effect="dark" @selection-change="selectionChange">
          <el-table-column type="selection" width="55"/>
          <el-table-column property="id" label="ID" width="50"/>
          <el-table-column property="name" label="名称"/>
          <el-table-column property="petTypeName" label="类型"/>
          <el-table-column property="basicInfo" label="简介"/>
          <el-table-column property="mainImg" label="图片">
            <template #default="scope">
              <el-image
                  v-if="scope.row.mainImg"
                  style="width: 75px; height: 75px"
                  :src="scope.row.mainImg"
                  :preview-src-list="[scope.row.mainImg]"
                  preview-teleported
              />
            </template>
          </el-table-column>
          <el-table-column property="createTime" label="创建时间"/>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="success" :icon="Edit" @click="edit(scope.row)">编辑</el-button>
              <el-button type="danger" :icon="Delete" @click="deleteOne(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div style="margin-top: 20px">
          <el-pagination
              @current-change="currentChange"
              @size-change="sizeChange"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.pageNum"
              background
              layout="total, sizes, prev, pager, next"
              :total="pageInfo.total"
          />
        </div>
      </el-card>
    </el-space>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogOpen" v-if="dialogOpen" :title="formData.id ? '编辑' : '新增'" width="500">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="名称" prop="name" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-input v-model="formData.name"/>
        </el-form-item>

        <el-form-item label="类型" prop="petTypeId" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-select v-model="formData.petTypeId" placeholder="请选择" clearable filterable>
            <el-option
                v-for="item in petTypeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="简介" prop="basicInfo">
          <el-input type="textarea" v-model="formData.basicInfo"/>
        </el-form-item>

        <el-form-item label="图片" prop="mainImg">
          <MyUpload type="imageCard" :limit="1" :files="formData.mainImg" @setFiles="formData.mainImg = $event"/>
        </el-form-item>
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
import { ref, toRaw } from "vue"
import { Check, Close, Delete, Edit, Plus, Refresh, Search } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"
import MyUpload from "@/components/MyUpload.vue"

const searchFromComponents = ref()
const searchForm = ref({
  name: undefined,
  petTypeId: undefined,
  username: undefined
})

const petTypeList = ref([])
const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })

getPageList()
getPetTypeList()

function getPageList() {
  const data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/pet/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function getPetTypeList() {
  request.get("/petType/list").then(res => {
    petTypeList.value = res.data
  })
}

function currentChange(e) {
  pageInfo.value.pageNum = e
  getPageList()
}

function sizeChange(e) {
  pageInfo.value.pageSize = e
  pageInfo.value.pageNum = 1
  getPageList()
}

function search() {
  pageInfo.value.pageNum = 1
  getPageList()
}

function resetSearch() {
  searchFromComponents.value.resetFields()
  getPageList()
}

function add() {
  formData.value = {}
  dialogOpen.value = true
}

function edit(row) {
  formData.value = Object.assign({}, row)
  dialogOpen.value = true
}

const selectionRows = ref([])

function selectionChange(rows) {
  selectionRows.value = rows
}

function BatchDelete(rows) {
  if (!rows) rows = selectionRows.value
  const ids = rows.map(item => item.id)
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据,是否确认删除`, '警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/pet/delete", { data: ids }).then(res => {
      if (!res) return
      ElMessage({ type: 'success', message: '删除成功' })
      getPageList()
    })
  }).catch(() => {
    ElMessage({ type: 'info', message: '已取消删除' })
  })
}

function deleteOne(row) {
  BatchDelete([row])
}

const dialogOpen = ref(false)
const formData = ref({})
const formRef = ref()

function closeDialog() {
  dialogOpen.value = false
}

function submit() {
  formRef.value.validate(valid => {
    if (!valid) {
      ElMessage({ message: "验证失败，请检查表单", type: "warning" })
      return
    }
    if (!formData.value.id) {
      request.post("/pet/add", formData.value).then(res => {
        if (!res) return
        dialogOpen.value = false
        ElMessage({ message: "操作成功", type: "success" })
        getPageList()
      })
    } else {
      request.put("/pet/update", formData.value).then(res => {
        if (!res) return
        dialogOpen.value = false
        ElMessage({ message: "操作成功", type: "success" })
        getPageList()
      })
    }
  })
}
</script>
