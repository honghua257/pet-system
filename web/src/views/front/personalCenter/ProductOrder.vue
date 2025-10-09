<template>
  <div class="product-order-container">
    <!-- 状态筛选标签 -->
    <div class="tabs-section">
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
          <el-table-column property="id" label="ID" width="60"/>
          <el-table-column property="productName" label="商品名称" width="300" show-overflow-tooltip/>
          <el-table-column property="price" label="价格" width="100">
            <template #default="scope">
              {{ scope.row.price }}
            </template>
          </el-table-column>
          <el-table-column property="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column property="receiverName" label="收货人姓名" width="120"/>
          <el-table-column property="receiverPhone" label="收货人电话" width="120"/>
          <el-table-column property="receiverAddress" label="收货人地址" width="200" show-overflow-tooltip/>
          <el-table-column property="createTime" label="创建时间" width="180"/>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button
                v-if="scope.row.status === '待支付'"
                type="primary"
                link
                @click="pay(scope.row)"
              >
                付款
              </el-button>
              <el-button
                v-if="scope.row.status === '待收货'"
                type="success"
                link
                @click="confirm(scope.row)"
              >
                确认收货
              </el-button>
              <el-button
                v-if="scope.row.status === '待支付'"
                type="danger"
                link
                @click="cancel(scope.row)"
              >
                取消
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrapper">
          <el-pagination
              background
              layout="prev, pager, next, sizes, total"
              :page-sizes="[10,20,50,100]"
              :total="pageInfo.total"
              :page-size="pageInfo.pageSize"
              :current-page="pageInfo.pageNum"
              @size-change="sizeChange"
              @current-change="currentChange"
              class="custom-pagination"
          />
        </div>
      </el-card>
    </div>
    <!-- 订单详情对话框 -->
    <el-dialog v-model="viewDialogOpen" title="订单详情" width="600px" :close-on-click-modal="false">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单ID">{{ currentOrder.id }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentOrder.productName }}</el-descriptions-item>
        <el-descriptions-item label="用户名称">{{ currentOrder.username }}</el-descriptions-item>
        <el-descriptions-item label="价格">¥{{ currentOrder.price }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentOrder.status)">{{ currentOrder.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentOrder.createTime }}</el-descriptions-item>
        <el-descriptions-item label="收货人姓名" :span="2">{{ currentOrder.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="收货人电话" :span="2">{{ currentOrder.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.receiverAddress }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button :icon="Close" @click="closeViewDialog">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw} from "vue";
import {Close} from "@element-plus/icons-vue";
import tools from "@/utils/tools.js";
import {ElMessage, ElMessageBox} from "element-plus";

// 状态筛选标签
const statusTabs = ref([
  { label: '全部', value: '' },
  { label: '待支付', value: '待支付' },
  { label: '待发货', value: '待发货' },
  { label: '待收货', value: '待收货' },
  { label: '已完成', value: '已完成' },
  { label: '已取消', value: '已取消' }
])

const selectedStatus = ref('')

const listData = ref([])
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 })

getPageList()

function getPageList() {
  const data = {
    status: selectedStatus.value,
    ...toRaw(pageInfo.value)
  }
  request.get("/productOrder/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
function sizeChange(e) { pageInfo.value.pageSize = e; pageInfo.value.pageNum = 1; getPageList() }

// 状态筛选
function selectStatus(status) {
  selectedStatus.value = status
  pageInfo.value.pageNum = 1
  getPageList()
}

const viewDialogOpen = ref(false)
const currentOrder = ref({})

function view(row) {
  currentOrder.value = Object.assign({}, row)
  viewDialogOpen.value = true
}

function closeViewDialog() {
  viewDialogOpen.value = false
}

function pay(row) {
  const user = tools.getCurrentUser()
  if (!user) {
    return ElMessage.error('请先登录')
  }
  // 查询最新余额
  request.get('/common/currentUser').then(res => {
    const balance = Number(res?.data?.balance || 0)
    const price = Number(row.price || 0)
    if (balance < price) {
      ElMessageBox.confirm(`余额不足（￥${balance}），是否前往充值？`, '余额不足', {
        confirmButtonText: '去充值',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        // 跳转充值页面
        window.location.href = '/balanceInfo'
      }).catch(() => {})
      return
    }
    // 余额充足，二次确认
    ElMessageBox.confirm(`是否确认支付 ￥${price} 购买【${row.productName}】？`, '支付确认', {
      confirmButtonText: '确认支付',
      cancelButtonText: '取消',
      type: 'warning',
      center: true
    }).then(() => {
      // 扣减余额
      request.post(`/user/consumption/${price}`).then(() => {
        ElMessage({ type: 'success', message: '付款成功' })
        // 更新订单状态为已付款 -> 待发货
        const payload = { ...row, status: '待发货' }
        request.put('/productOrder/update', payload).then(() => {
          // 同步更新商品库存与销量（基于商品名称匹配），按quantity扣减
          request.get('/product/list').then(res => {
            const list = res?.data || []
            const prod = list.find(p => p.name === row.productName)
            if (prod && prod.id) {
              // 直接读取订单的quantity
              const qty = Math.max(1, Number(row.quantity || 1))
              const newStock = Math.max(0, Number(prod.stock || 0) - qty)
              const newSales = Number(prod.salesVolume || 0) + qty
              request.put('/product/update', { id: prod.id, stock: newStock, salesVolume: newSales }).then(() => {
                getPageList()
              })
            } else {
              // 若未匹配到商品，仍刷新列表
              getPageList()
            }
          })
        })
      })
    }).catch(() => { ElMessage({ type: 'info', message: '已取消支付' }) })
  })
}

function confirm(row) {
  ElMessageBox.confirm('确认收货？', '确认收货', {
    confirmButtonText: '确认收货',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    // 更新订单状态为 已完成
    const payload = { ...row, status: '已完成' }
    request.put('/productOrder/update', payload).then(() => {
      ElMessage({ type: 'success', message: '确认收货成功' })
      getPageList()
    })
  }).catch(() => { ElMessage({ type: 'info', message: '已取消确认收货' }) })
}

function cancel(row) {
  ElMessageBox.confirm('确认取消此订单？', '取消订单', {
    confirmButtonText: '确认取消',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(() => {
    // 这里可以调用取消订单接口
    ElMessage({ type: 'success', message: '订单已取消' })
    getPageList()
  }).catch(() => { ElMessage({ type: 'info', message: '已取消操作' }) })
}

function getStatusType(status) {
  const statusMap = {
    '待支付': 'warning',
    '待发货': 'warning',
    '待收货': 'primary',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return statusMap[status] || 'info'
}
</script>

<style scoped>
/* 导入前端美化样式 */
@import "@/styles/front.css";

/* 商城订单页面样式 */
.product-order-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 标签区域 */
.tabs-section {
  margin-bottom: 20px;
}

.tabs-card {
  background: var(--bg-primary);
  border-radius: 16px;
  box-shadow: 0 2px 8px var(--shadow-neutral);
  border: 1px solid var(--gray-200);
}

.status-tabs {
  display: flex;
  gap: 8px;
  padding: 8px;
  flex-wrap: wrap;
}

.status-tab {
  padding: 8px 16px;
  margin: 0;
  border-radius: 12px;
  color: var(--text-secondary);
  font-weight: 500;
  transition: all 0.3s ease;
  cursor: pointer;
  white-space: nowrap;
  border: 2px solid transparent;
}

.status-tab:hover {
  background: var(--bg-accent);
  color: var(--primary-orange);
  border-color: var(--accent-cyan);
}

.status-tab.active {
  background: var(--gradient-primary);
  color: white;
  box-shadow: 0 2px 8px var(--shadow-primary);
  border-color: var(--primary-orange);
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
.product-order-container :deep(.el-empty) {
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
  .tabs-section {
    margin-bottom: 16px;
  }

  .status-tabs {
    padding: 6px;
    gap: 4px;
  }

  .status-tab {
    padding: 6px 12px;
    font-size: 14px;
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
  .tabs-section {
    margin-bottom: 12px;
  }

  .status-tabs {
    padding: 4px;
    gap: 2px;
  }

  .status-tab {
    padding: 4px 8px;
    font-size: 13px;
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
