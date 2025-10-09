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
          <el-button type="primary" @click="add" :icon="Plus">新增</el-button>
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
          <el-table-column prop="reservedTime" label="预约时间" width="160"></el-table-column>
          <el-table-column prop="remark" label="备注" ></el-table-column>
          <el-table-column prop="status" label="状态" class-name="status-col" width="81">
            <template #default="scope">
              <el-tag v-if="scope.row.status==='PENDING' || scope.row.status==='已下单'" type="warning">{{ scope.row.status==='PENDING' ? '待接单' : '已下单' }}</el-tag>
              <el-tag v-else-if="scope.row.status==='IN_SERVICE' || scope.row.status==='服务中'" type="primary">服务中</el-tag>
              <el-tag v-else-if="scope.row.status==='FINISHED' || scope.row.status==='已完成'" type="success">已完成</el-tag>
              <span v-else>{{ scope.row.status }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column fixed="right" label="高级操作" width="110">
            <template #default="scope">
              <el-space>
                <el-button size="small" type="primary" @click="inService(scope.row)" v-if="scope.row.status==='PENDING' || scope.row.status==='已下单'">接单</el-button>
                <el-button size="small" type="success" @click="finish(scope.row)" v-if="scope.row.status==='IN_SERVICE' || scope.row.status==='服务中'">完成</el-button>
              </el-space>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="180">
            <template #default="scope">
              <el-space>
                <el-button size="small" :icon="Edit" @click="edit(scope.$index, scope.row)">编辑</el-button>
                <el-button size="small" :icon="Delete" type="danger" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
              </el-space>
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
    <el-dialog
        v-model="dialogOpen"
        v-if="dialogOpen"
        :title="formData.id?'编辑':'新增'"
        width="800px"
    >
      <el-form ref="formRef" :model="formData" label-width="100px" inline>
        <el-form-item label="宠物ID" prop="petId" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]"><el-input v-model="formData.petId" placeholder="宠物ID"></el-input></el-form-item>
        <el-form-item label="宠物类型ID" prop="petTypeId" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]"><el-input v-model="formData.petTypeId" placeholder="宠物类型ID"></el-input></el-form-item>
        <el-form-item label="店长ID" prop="petStoreManagerId" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]"><el-input v-model="formData.petStoreManagerId" placeholder="店长ID"></el-input></el-form-item>
        <el-form-item label="预约时间" prop="reservedTime" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-date-picker
              v-model="formData.reservedTime"
              type="datetime"
              placeholder="选择预约时间"
              style="width: 200px;"
              value-format="YYYY-MM-DD HH:mm:ss"
              format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" placeholder="备注"></el-input></el-form-item>
        <el-form-item label="状态" prop="status" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-select v-model="formData.status" placeholder="请选择" style="width: 200px">
            <el-option :label="item.label" :value="item.value" :key="item.value" v-for="item in status"></el-option>
          </el-select>
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
import request from "@/utils/http.js";
import {Check, Close, Delete, Edit, Refresh, Plus, Search} from '@element-plus/icons-vue'
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
  {label: "待接单", value: "PENDING"},
  {label: "服务中", value: "IN_SERVICE"},
  {label: "已完成", value: "FINISHED"}
]);


getPageList()

/**
 * 获取分页数据
 */
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/petFeed/page", {
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


const dialogOpen = ref(false);
const formData = ref({});
const formRef = ref();

/**
 * 新增
 */
function add() {
  dialogOpen.value = true
  formData.value = {}
}

/**
 * 编辑
 * @param index
 * @param row
 */
function edit(index, row) {
  dialogOpen.value = true
  formData.value = Object.assign({}, row)
}

/**
 * 关闭弹框
 */
function closeDialog() {
  dialogOpen.value = false
}

/**
 * 提交数据
 */
function submit() {
  //新增
  formRef.value.validate((valid) => {
    if (!valid) {
      ElMessage({
        message: "验证失败，请检查必填项!",
        type: 'warning'
      });
      return
    }
    if (!formData.value.id) {
      request.post("/petFeed/add", formData.value).then(res => {
        if (!res) {
          return
        }
        dialogOpen.value = false
        ElMessage({
          message: res.msg + " 新增用户密码默认为：123456",
          type: 'success'
        });
        getPageList()
      })
    } else {
      //更新
      request.put("/petFeed/update", formData.value).then(res => {
        if (!res) {
          return
        }
        dialogOpen.value = false
        ElMessage({
          message: "操作成功",
          type: 'success'
        });
        getPageList()
      })
    }
  })
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
    request.delete("/petFeed/delete", {data: ids}).then(res => {
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
  request.put(`/petFeed/inService/${row.id}`).then(res=>{
    if(!res){return}
    ElMessage({message:"操作成功",type:'success'});
    getPageList();
  })
}

// 完成
function finish(row){
  request.put(`/petFeed/finish/${row.id}`).then(res=>{
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
