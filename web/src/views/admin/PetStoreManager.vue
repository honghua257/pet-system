<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <!-- 搜索区 -->
      <el-card>
        <el-form ref="searchFromComponents" :model="searchForm" inline>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="tel">
            <el-input v-model="searchForm.tel" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 150px">
              <el-option :label="item" :value="item" :key="item" v-for="item in statusList"></el-option>
            </el-select>
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
          <el-table-column type="selection" width="55" />
          <el-table-column property="id" label="ID" width="50" />
          <el-table-column property="username" label="用户名" />
          <el-table-column property="nickname" label="昵称" />
          <el-table-column property="avatarUrl" label="头像">
            <template #default="scope">
              <el-image v-if="scope.row.avatarUrl" style="width: 75px; height: 75px" :src="scope.row.avatarUrl" :preview-src-list="[scope.row.avatarUrl]" preview-teleported="true"></el-image>
            </template>
          </el-table-column>
          <el-table-column property="tel" label="电话" />
          <el-table-column property="email" label="邮箱" />
          <el-table-column property="status" label="状态" />
          <el-table-column property="storeName" label="店铺名" />
          <el-table-column property="storeAddress" label="店铺地址" />
          <el-table-column property="storeImg" label="店铺图片">
            <template #default="scope">
              <el-image v-if="scope.row.storeImg" style="width: 75px; height: 75px" :src="scope.row.storeImg" :preview-src-list="[scope.row.storeImg]" preview-teleported="true"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="高级操作" width="135">
            <template #default="scope">
              <el-button type="info" :icon="RefreshLeft" @click="resetPassword(scope.row)">重置密码</el-button>
            </template>
          </el-table-column>
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
        <el-form-item label="用户名" prop="username" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-input v-model="formData.username" :disabled="formData.id != null"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-input v-model="formData.nickname"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatarUrl" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <MyUpload type="imageCard" :limit="1" :files="formData.avatarUrl" @setFiles="formData.avatarUrl = $event" />
        </el-form-item>
        <el-form-item label="电话" prop="tel" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-input v-model="formData.tel"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-input v-model="formData.email"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-radio-group v-model="formData.status">
            <el-radio :label="item" :key="item" v-for="item in statusList">{{ item }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="店铺名" prop="storeName" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-input v-model="formData.storeName"></el-input>
        </el-form-item>
        <el-form-item label="店铺地址" prop="storeAddress" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-input v-model="formData.storeAddress"></el-input>
        </el-form-item>
        <el-form-item label="店铺图片" prop="storeImg" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <MyUpload type="imageCard" :limit="1" :files="formData.storeImg" @setFiles="formData.storeImg = $event" />
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
import { Check, Close, Delete, Edit, Plus, Refresh, RefreshLeft, Search } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"
import MyUpload from "@/components/MyUpload.vue"

const searchFromComponents = ref()

const searchForm = ref({
  username: undefined,
  tel: undefined,
  status: undefined,
})

const statusList = ref(["启用", "禁用"])

const listData = ref([])

const pageInfo = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

getPageList()

function getPageList() {
  const data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petStoreManager/page", { params: data }).then(res => {
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
    request.delete("/petStoreManager/delete", { data: ids }).then(res => {
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

function resetPassword(row) {
  request.post("/common/resetPassword?type=PET_STORE_MANAGER&id=" + row.id).then(res => {
    if (!res) return
    ElMessage({ message: "操作成功", type: 'success' })
  })
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
      request.post("/petStoreManager/add", formData.value).then(res => {
        if (!res) return
        dialogOpen.value = false
        ElMessage({ message: "操作成功", type: "success" })
        getPageList()
      })
    } else {
      request.put("/petStoreManager/update", formData.value).then(res => {
        if (!res) return
        dialogOpen.value = false
        ElMessage({ message: "操作成功", type: "success" })
        getPageList()
      })
    }
  })
}
</script>
