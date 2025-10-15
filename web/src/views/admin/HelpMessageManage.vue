<template>
  <div>
    <!--    排列的方式：垂直，对齐方式：左对齐-->
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <!--      共两个区域，搜索区和内容表格展示区-->
      <!--      搜索区-->
      <el-card>
        <el-form ref="searchFromComponents" :model="searchForm" inline>
          <el-form-item label="标题" prop="title">
            <el-input v-model="searchForm.title" clearable></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="searchForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button type="info" :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="success" :icon="Plus" @click="add">新增</el-button>
          <!--              批量删除功能，当没有选择数据时（<=0），此功能禁用-->
          <el-button type="danger" :icon="Delete" @click="BatchDelete(null)" :disabled="selectionRows.length<=0">
            批量删除
          </el-button>
        </el-space>
      </el-card>
      <!--      内容表格展示区-->
      <el-card>
        <el-table :data="listData" style="width: 100%" border stripe tooltip-effect="dark"
                  @selection-change="selectionChange">
          <el-table-column type="selection" width="55"/>

          <el-table-column property="id" label="id" width="50"/>

          <el-table-column property="title" label="标题"/>
          <el-table-column property="username" label="用户名"/>
          <el-table-column property="content" label="内容"/>
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
              layout="total, sizes, prev, pager, next" :total="pageInfo.total"/>
        </div>

      </el-card>
    </el-space>

<!--    弹窗-->
    <el-dialog
        v-model="dialogOpen"
        v-if="dialogOpen"
        :title="formData.id?'编辑':'新增'"
        width="700">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="标题" prop="title"
                      :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <el-input v-model="formData.title"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content"
                      :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <MyEditor :content="formData.content" @content-change="formData.content=$event" v-if="dialogOpen"></MyEditor>
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
import {ref, toRaw} from "vue";
import {Check, Close, Delete, Edit, Plus, Refresh, RefreshLeft, Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import MyEditor from "@/components/MyEditor.vue";


const searchFromComponents = ref()

const searchForm = ref({
  username: undefined,
  title: undefined,
})

const listData = ref([])

//分页模糊查询向后端发起请求的三个参数
const pageInfo = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

getPageList()

//获取分页数据
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  //向后端发起请求
  request.get("/helpMessage/page", {
    params: data
  }).then(res => {
    //获取分页列表的数据
    listData.value = res.data.list
    //获取分页总数
    pageInfo.value.total = res.data.total
  })
}

//选择分页
function currentChange(e) {
  pageInfo.value.pageNum = e
  getPageList()
}

//改变分页数量
function sizeChange(e) {
  pageInfo.value.pageSize = e
  //改变之后重置分页为第一页
  pageInfo.value.pageNum = 1
  getPageList()
}

function search() {
  //将分页重置到第一页，不然查询出来的数据会出现在别的页数上
  pageInfo.value.pageNum = 1
  //调用分页接口
  getPageList()
}

//重置搜索框（清空搜索框）
function resetSearch() {
  searchFromComponents.value.resetFields()
  getPageList()
}

// 新增
function add() {
  //清空表单中的数据
  formData.value = {}
  //将弹窗打开
  dialogOpen.value = true
}

// 编辑
function edit(row) {
  //不能直接使用等号赋值，会出现两个变量使用一个内存地址的情况，需要重新生成一个对象
  formData.value = Object.assign({}, row)
  dialogOpen.value = true
}

//选中的数据
const selectionRows = ref([])

//多选，选择了之后批量删除的功能就可以使用
function selectionChange(rows) {
  selectionRows.value = rows
}

//批量删除
function BatchDelete(rows) {
  if (!rows) {
    rows = selectionRows.value
  }
  //取出每个对象中的id
  let ids = rows.map(item => item.id)
  //输出提示信息，询问是否确认删除
  ElMessageBox.confirm(
      `此操作将永久删除ID为[${ids}]的数据,是否确认删除`,
      '警告',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        center: true,
      }
  )
      .then(() => {
        request.delete("/helpMessage/delete", {data: ids}).then((res => {
          if (!res) {
            return
          }
          ElMessage({
            type: 'success',
            message: '删除成功',
          })
          //删除完成后，重新获取数据展示
          getPageList()
        }))
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除',
        })
      })
}

//单个删除
function deleteOne(row) {
  //调用批量删除的方法
  BatchDelete([row])
}

const dialogOpen = ref(false)

const formData = ref({})

const formRef = ref()

// 关闭弹窗
function closeDialog() {
  dialogOpen.value = false
}

//提交数据
function submit() {
  formRef.value.validate((valid) => {
    //校验
    if (!valid) {
      ElMessage({
        message: "验证失败，请检查表单",
        type: "warning"
      })
      return
    }
    //如果id不存在，调用新增接口
    if (!formData.value.id) {
      request.post("/helpMessage/add", formData.value).then(res => {
        if (!res) {
          return
        }
        dialogOpen.value = false
        ElMessage({
          message: "操作成功",
          type: "success"
        })
        //重新获取列表
        getPageList()
      })
    } else {
      //修改调用更新接口
      request.put("/helpMessage/update", formData.value).then(res => {
        if (!res) {
          return
        }
        dialogOpen.value = false
        ElMessage({
          message: "操作成功",
          type: "success"
        })
        //重新获取列表
        getPageList()
      })
    }

  })
}
</script>