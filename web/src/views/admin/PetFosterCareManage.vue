<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="宠物名称" prop="petName">
            <el-input v-model="searchForm.petName" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="店铺名称" prop="petStoreName">
            <el-input v-model="searchForm.petStoreName" clearable></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="searchForm.remark" clearable></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 150px">
              <el-option :label="item.label" :value="item.value" :key="item.value" v-for="item in status"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="">
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">
            批量删除
          </el-button>
        </el-space>
      </el-card>
      <el-card>
        <el-table ref="tableComponents"
                  :data="listData"
                  tooltip-effect="dark"
                  style="width: 100%"
                  border
                  @selection-change="selectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="ID" width="60"></el-table-column>
          <el-table-column prop="petName" label="宠物名称"></el-table-column>
          <el-table-column prop="petTypeName" label="宠物类型名称"></el-table-column>
          <el-table-column prop="username" label="用户名"></el-table-column>
          <el-table-column prop="petStoreName" label="店铺名称"></el-table-column>
          <el-table-column prop="reservedStartTime" label="预约开始时间" width="160"></el-table-column>
          <el-table-column prop="reservedEndTime" label="预约结束时间" width="160"></el-table-column>
          <el-table-column prop="remark" label="备注"></el-table-column>
          <el-table-column prop="status" label="状态" class-name="status-col" width="81">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='已下单'" type="warning">已下单</el-tag>
              <el-tag v-if="scope.row.status==='服务中'" type="primary">服务中</el-tag>
              <el-tag v-if="scope.row.status==='已完成'" type="success">已完成</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="120">
            <template #default="scope">
              <div class="time-column">{{ scope.row.createTime }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="高级操作" width="110" class-name="advanced-operations-col">
            <template #default="scope">
              <el-space >
                <el-button size="small" type="primary" @click="inService(scope.row)" v-if="scope.row.status==='已下单'">接单</el-button>
                <el-button size="small" type="success" @click="finish(scope.row)" v-if="scope.row.status==='服务中'">完成</el-button>
              </el-space>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button size="small" :icon="Delete" type="danger" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
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
              layout="total,sizes, prev, pager, next"
              :total="pageInfo.total">
          </el-pagination>
        </div>
      </el-card>
    </el-space>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Delete, Refresh, Search} from '@element-plus/icons-vue'
import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const searchFormComponents = ref();
const tableComponents = ref();

const listData = ref([]);
const pageInfo = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
});
const searchForm = ref({});
const status = ref([
  {label: "已下单", value: "已下单"},
  {label: "服务中", value: "服务中"},
  {label: "已完成", value: "已完成"}
]);


getPageList()

function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petFosterCare/page", {
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

const selectionRows = ref([]);

function selectionChange(rows) {
  selectionRows.value = rows
}

function deleteOne(index, row) {
  batchDelete([row])
}

function batchDelete(rows) {
  if (!rows) {
    rows = selectionRows.value;
  }
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`此操作将永久删除ID为[${ids}]的数据, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete("/petFosterCare/delete", {data: ids}).then(res => {
      if (!res) {
        return
      }
      ElMessage({
        message: "操作成功",
        type: 'success'
      });
      getPageList()

    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
    tableComponents.value.clearSelection();
  });
}

function inService(row){
  request.put(`/petFosterCare/inService/${row.id}`).then(res=>{
    if(!res){return}
    ElMessage({message:"操作成功",type:'success'});
    getPageList();
  })
}

function finish(row){
  request.put(`/petFosterCare/finish/${row.id}`).then(res=>{
    if(!res){return}
    ElMessage({message:"操作成功",type:'success'});
    getPageList();
  })
}
</script>

<style scoped>
.status-col .cell {
  overflow: visible !important;
  text-overflow: clip !important;
}

.time-column {
  position: relative;
  padding-right: 12px;
}

/* --- 【主要修改区域】 --- */
/* 高级操作列样式 - 通过为td元素添加左边框来实现分隔线 */
:deep(.advanced-operations-col) {
  /* 使用 :deep() 穿透scoped样式 */
  /* #DCDFE6 是Element Plus标准的边框颜色，更加清晰 */
  border-left: 1px solid #DCDFE6 !important;
}

/* 保持单元格内部的样式，但移除这里多余的边框 */
.advanced-operations-col .cell {
  background-color: #FAFAFA;
  padding-left: 12px;
}
/* --- 【修改结束】 --- */

/* 表格行悬停时的高级操作列背景色 */
.el-table__body tr:hover .advanced-operations-col .cell {
  background-color: #F5F7FA;
}
</style>