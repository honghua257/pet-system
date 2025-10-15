<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <!-- 搜索区 -->
      <el-card>
        <el-form ref="searchFromComponents" :model="searchForm" inline>
          <el-form-item label="标题" prop="title">
            <el-input v-model="searchForm.title" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button type="info" :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>

        <el-space>
          <el-button type="danger" :icon="Delete" @click="BatchDelete(null)" :disabled="selectionRows.length <= 0">
            批量删除
          </el-button>
        </el-space>
      </el-card>

      <!-- 表格区 -->
      <el-card>
        <el-table
            :data="listData" style="width: 100%" border stripe tooltip-effect="dark"
            @selection-change="selectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column property="id" label="ID" width="50" />
          <el-table-column property="title" label="标题" />
          <el-table-column property="username" label="用户名称" />
          <el-table-column property="content" label="内容" />
          <el-table-column property="createTime" label="创建时间" />

          <!-- 操作列 -->
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="success" :icon="Edit" @click="edit(scope.row)">编辑</el-button>
              <el-button type="danger" :icon="Delete" @click="deleteOne(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
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

    </div>

    <!-- 编辑弹窗 -->
    <el-dialog
        v-model="dialogOpen"
        v-if="dialogOpen"
        title="编辑宠物日记"
        width="600">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="标题" prop="title"
                      :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.title"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content"
                      :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.content" type="textarea" :rows="6"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit" :icon="Check">提交</el-button>
          <el-button @click="closeDialog" :icon="Close">取消</el-button>
        </div>
      </template>
    </el-dialog>
</template>

<script setup>
import request from "@/utils/http.js"
import { ref, toRaw } from "vue"
import { Delete, Edit, Refresh, Search, Check, Close } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"

const searchFromComponents = ref()
const searchForm = ref({
  title:undefined,
  username: undefined
})

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })

getPageList()


function getPageList() {
  const data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petDiary/page", { params: data }).then(res => {
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


const selectionRows = ref([])

function selectionChange(rows) {
  selectionRows.value = rows
}

function BatchDelete(rows) {
  if (!rows) {
    rows = selectionRows.value
  }
  let ids = rows.map(item => item.id)
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据,是否确认删除`, '警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/petDiary/delete", { data: ids }).then(res => {
      if (!res) {
        return
      }
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

// 编辑相关变量和函数
const dialogOpen = ref(false)
const formData = ref({})
const formRef = ref()

// 编辑
function edit(row) {
  formData.value = Object.assign({}, row)
  dialogOpen.value = true
}

// 关闭弹窗
function closeDialog() {
  dialogOpen.value = false
}

// 提交编辑
function submit() {
  formRef.value.validate((valid) => {
    if (!valid) {
      ElMessage({
        message: "验证失败，请检查表单",
        type: "warning"
      })
      return
    }

    request.put("/petDiary/update", formData.value).then(res => {
      if (!res) {
        return
      }
      dialogOpen.value = false
      ElMessage({
        message: "操作成功",
        type: "success"
      })
      getPageList()
    })
  })
}

</script>
