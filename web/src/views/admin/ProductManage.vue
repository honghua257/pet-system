<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFromComponents" :model="searchForm" inline>
          <el-form-item label="名称" prop="name">
            <el-input v-model="searchForm.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" clearable placeholder="请选择" style="width: 150px">
              <el-option label="上架" value="上架"/>
              <el-option label="下架" value="下架"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="primary" :icon="Plus" @click="add">新增</el-button>
          <el-button type="danger" :icon="Delete" @click="BatchDelete(null)" :disabled="selectionRows.length<=0">批量删除</el-button>
        </el-space>
      </el-card>

      <el-card>
        <el-table :data="listData" style="width: 100%" border stripe tooltip-effect="dark" @selection-change="selectionChange">
          <el-table-column type="selection" width="55"/>
          <el-table-column property="id" label="ID" width="60"/>
          <el-table-column property="name" label="名称"/>
          <el-table-column label="封面图" width="120">
            <template #default="scope">
              <el-image v-if="scope.row.mainImg" :src="scope.row.mainImg" style="width: 80px; height: 80px" fit="cover"/>
            </template>
          </el-table-column>
          <el-table-column property="status" label="状态" width="100"/>
          <el-table-column property="price" label="价格" width="100"/>
          <el-table-column property="stock" label="库存" width="90"/>
          <el-table-column property="salesVolume" label="销量" width="90"/>
          <el-table-column property="createTime" label="创建时间" width="180"/>
          <el-table-column fixed="right" label="操作" width="150">
            <template #default="scope">
              <el-button type="primary" link :icon="Edit" @click="edit(scope.row)">编辑</el-button>
              <el-button type="danger" link :icon="Delete" @click="deleteOne(scope.row)">删除</el-button>
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

    <el-dialog v-model="dialogOpen" title="商品" width="800px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formData" label-width="90px">
        <el-form-item label="名称" prop="name" :rules="[{ required: true, message: '请输入名称', trigger: 'blur' }]"><el-input v-model="formData.name"/></el-form-item>
        <el-form-item label="封面图" prop="mainImg">
          <MyUpload type="imageCard" :limit="1" :files="formData.mainImg" @setFiles="formData.mainImg = $event"/>
        </el-form-item>
        <el-form-item label="介绍" prop="introduce">
          <MyEditor :content="formData.introduce" @content-change="formData.introduce = $event" v-if="dialogOpen"/>
        </el-form-item>
        <el-form-item label="状态" prop="status" :rules="[{ required: true, message: '不能为空', trigger: ['blur','change'] }]">
          <el-radio-group v-model="formData.status">
            <el-radio label="上架"/>
            <el-radio label="下架"/>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="价格" prop="price"><el-input v-model.number="formData.price" type="number"/></el-form-item>
        <el-form-item label="库存" prop="stock" :rules="[{ validator: validateStock, trigger: ['blur','change'] }]"><el-input v-model.number="formData.stock" type="number"/></el-form-item>
        <el-form-item label="销量" prop="salesVolume"><el-input v-model.number="formData.salesVolume" type="number"/></el-form-item>
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
import {Check, Close, Delete, Edit, Plus, Refresh, Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import MyUpload from "@/components/MyUpload.vue";
import MyEditor from "@/components/MyEditor.vue";

const searchFromComponents = ref()
const searchForm = ref({ name: undefined, status: undefined })

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })

getPageList()

function getPageList() {
  const data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/product/page", { params: data }).then(res => {
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
    request.delete("/product/delete", { data: ids }).then(res => {
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
    const url = isAdd ? "/product/add" : "/product/update"
    api(url, formData.value).then(res => {
      if (!res) return
      ElMessage({ type: 'success', message: '提交成功' })
      dialogOpen.value = false
      getPageList()
    })
  })
}

function validateStock(rule, value, callback) {
  const s = formData.value?.status
  if (s === '上架') {
    if (value == null || value <= 0) {
      return callback(new Error('上架商品库存必须大于0'))
    }
  }
  callback()
}
</script>


