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
          <el-table-column prop="reservedStartTime" label="预约开始时间" width="110"></el-table-column>
          <el-table-column prop="reservedEndTime" label="预约结束时间" width="110"></el-table-column>
          <el-table-column prop="remark" label="备注"></el-table-column>
          <el-table-column prop="status" label="状态" class-name="status-col" width="81">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='已下单'" type="warning">已下单</el-tag>
              <el-tag v-if="scope.row.status==='服务中'" type="primary">服务中</el-tag>
              <el-tag v-if="scope.row.status==='已完成'" type="success">已完成</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
          <el-table-column fixed="right" label="高级操作" width="110">
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
  //当前页
  pageNum: 1,
  //分页大小
  pageSize: 10,
  //总条数
  total: 0
});
const searchForm = ref({});
const status = ref([
  {label: "已下单", value: "已下单"},
  {label: "服务中", value: "服务中"},
  {label: "已完成", value: "已完成"}
]);


getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petFosterCare/page", {
    params: data
  }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

/**
 * 选择分页
 * @param e
 */
function currentChange(e) {
  pageInfo.value.pageNum = e
  getPageList()
}

/**
 * 改变分页数量
 * @param e
 */
function sizeChange(e) {
  pageInfo.value.pageSize = e
  getPageList()
  console.log(e)
}

/**
 * 搜索
 */
function search() {
  pageInfo.value.pageNum = 1
  getPageList()
}

/**
 * 重置搜索框
 */
function resetSearch() {
  searchFormComponents.value.resetFields();
  getPageList()
}




const selectionRows = ref([]);

/**
 * 多选
 * @param rows
 */
function selectionChange(rows) {
  selectionRows.value = rows
}

/**
 * 单个删除
 * @param index
 * @param row
 */
function deleteOne(index, row) {
  batchDelete([row])
}

/**
 * 批量删除
 * @param rows
 */
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

// 接单
function inService(row){
  request.put(`/petFosterCare/inService/${row.id}`).then(res=>{
    if(!res){return}
    ElMessage({message:"操作成功",type:'success'});
    getPageList();
  })
}

// 完成
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
</style>
