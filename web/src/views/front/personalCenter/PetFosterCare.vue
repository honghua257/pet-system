<template>
  <div class="foster-care-container">
    <!-- 状态筛选标签 -->
    <div class="header-section">
      <el-card class="tabs-card">
        <div class="status-tabs">
          <div
            v-for="tab in statusTabs"
            :key="tab.value"
            @click="selectStatus(tab.value)"
            :class="['status-tab', { 'active': selectedStatus === tab.value }]"
          >
            {{ tab.label }}
          </div>
        </div>
      </el-card>
    </div>

    <!-- 订单列表 -->
    <div class="table-section">
      <el-card class="data-table-card">
        <el-table :data="listData" class="data-table" border tooltip-effect="dark">
          <el-table-column prop="id" label="ID" width="60"/>
          <el-table-column prop="petName" label="宠物名称" width="120"/>
          <el-table-column prop="petTypeName" label="宠物类型" width="140"/>
          <el-table-column prop="petStoreName" label="店铺名称" show-overflow-tooltip/>
          <el-table-column prop="reservedStartTime" label="开始时间" width="180"/>
          <el-table-column prop="reservedEndTime" label="结束时间" width="180"/>
          <el-table-column prop="remark" label="备注" show-overflow-tooltip/>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"/>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button
                v-if="scope.row.status === '已下单'"
                type="danger"
                link
                @click="deleteOne(scope.row)"
              >
                删除
              </el-button>
              <span v-else style="color: #999; font-size: 12px;">
                {{ scope.row.status === '服务中' ? '服务中' : '已完成' }}
              </span>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-wrapper">
          <el-pagination
              @current-change="currentChange"
              @size-change="sizeChange"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.pageNum"
              background
              layout="total,sizes, prev, pager, next"
              :total="pageInfo.total"
              class="custom-pagination"
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { ref, toRaw } from "vue";

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })
const selectedStatus = ref('')

// 状态标签选项
const statusTabs = [
  { label: '全部', value: '' },
  { label: '已下单', value: '已下单' },
  { label: '服务中', value: '服务中' },
  { label: '已完成', value: '已完成' }
]

getPageList()

function getPageList(){
  const params = Object.assign({}, toRaw(pageInfo.value))
  if (selectedStatus.value) {
    params.status = selectedStatus.value
  }
  request.get('/petFosterCare/page', { params }).then(res => {
    if(!res){ return }
    listData.value = res.data?.list || []
    pageInfo.value.total = res.data?.total || 0
  })
}

function currentChange(p){
  pageInfo.value.pageNum = p
  getPageList()
}

function sizeChange(s){
  pageInfo.value.pageSize = s
  getPageList()
}

function selectStatus(status) {
  selectedStatus.value = status
  pageInfo.value.pageNum = 1
  getPageList()
}

function getStatusType(status) {
  switch(status) {
    case '已下单': return 'success'
    case '服务中': return 'warning'
    case '已完成': return 'info'
    default: return ''
  }
}

function deleteOne(row){
  ElMessageBox.confirm(`此操作将永久删除该订单, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    request.delete('/petFosterCare/delete', { data: [row.id] }).then(res => {
      if(!res){ return }
      ElMessage({ message: '操作成功', type: 'success' })
      getPageList()
    })
  }).catch(() => {})
}
</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

/* 临时寄养订单页面样式 */
.foster-care-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 头部区域 */
.header-section {
  margin-bottom: 20px;
  background: var(--bg-primary);
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 2px 8px var(--shadow-neutral);
}

.tabs-card {
  border: none;
  box-shadow: none;
}

.status-tabs {
  display: flex;
  gap: 8px;
  align-items: center;
}

.status-tab {
  padding: 8px 16px;
  border-radius: 12px;
  background: var(--bg-secondary);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 14px;
  border: 1px solid transparent;
}

.status-tab:hover {
  background: var(--bg-accent);
  color: var(--primary-orange);
  transform: translateY(-1px);
}

.status-tab.active {
  background: var(--gradient-primary);
  color: white;
  box-shadow: 0 2px 8px var(--shadow-primary);
}

/* 表格区域 */
.table-section {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.data-table-card {
  flex: 1;
  background: var(--bg-primary);
  border-radius: 16px;
  box-shadow: 0 4px 16px var(--shadow-neutral);
  border: 1px solid var(--gray-200);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 表格样式 */
.data-table {
  flex: 1;
  --el-table-border-color: var(--gray-200);
  --el-table-header-bg-color: var(--bg-secondary);
}

.data-table :deep(.el-table__header) {
  background: var(--bg-secondary);
  font-weight: 600;
  color: var(--text-primary);
}

.data-table :deep(.el-table__body tr) {
  transition: background-color 0.3s ease;
}

.data-table :deep(.el-table__body tr:hover) {
  background-color: var(--bg-accent);
}

.data-table :deep(.el-table__cell) {
  padding: 12px 8px;
  border-bottom: 1px solid var(--gray-200);
}

/* 分页样式 */
.pagination-wrapper {
  padding: 20px;
  display: flex;
  justify-content: center;
  background: var(--bg-primary);
  border-top: 1px solid var(--gray-200);
}

.custom-pagination {
  --el-pagination-button-bg-color: var(--bg-primary);
  --el-pagination-hover-color: var(--primary-orange);
}

.custom-pagination :deep(.el-pager li.is-active) {
  background: var(--gradient-primary);
  color: white;
  border-radius: 50%;
}

.custom-pagination :deep(.el-pager li:hover) {
  color: var(--primary-orange);
}

/* 空状态 */
.foster-care-container :deep(.el-empty) {
  background: var(--bg-secondary);
  border-radius: 16px;
  padding: 40px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-section {
    padding: 12px;
    margin-bottom: 16px;
  }

  .status-tabs {
    flex-wrap: wrap;
    gap: 6px;
  }

  .status-tab {
    padding: 6px 12px;
    font-size: 13px;
  }

  .data-table :deep(.el-table__cell) {
    padding: 8px 6px;
    font-size: 14px;
  }

  .pagination-wrapper {
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .header-section {
    padding: 8px;
    margin-bottom: 12px;
  }

  .status-tab {
    padding: 4px 10px;
    font-size: 12px;
  }

  .data-table :deep(.el-table__cell) {
    padding: 6px 4px;
    font-size: 13px;
  }

  .pagination-wrapper {
    padding: 12px;
  }
}
</style>