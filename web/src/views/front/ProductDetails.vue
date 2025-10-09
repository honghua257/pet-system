<template>
  <div style="width: 75%; margin: 0 auto">
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <div style="border-left: 2px solid #e5e5e5; padding-left: 10px; font-weight: bold; margin-bottom: 10px">基本信息</div>
        <el-row :gutter="20">
          <el-col :span="10">
            <el-image :src="data.mainImg" style="width: 100%; height: 280px" fit="cover"/>
          </el-col>
          <el-col :span="14">
            <div style="font-size: 22px; font-weight: bold; margin-bottom: 6px">{{ data.name }}</div>
            <div style="color:#e53935; font-size: 22px; font-weight: bold; margin: 6px 0">价格: ¥ {{ data.price }}</div>
            <div style="margin: 6px 0">库存 <b>{{ data.stock }}</b>　销量 <b>{{ data.salesVolume }}</b></div>
            <div style="margin-top: 15px">
              <el-button type="danger" @click="buyNow" style="width: 120px">立即购买</el-button>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-card>
        <div style="border-left: 2px solid #e5e5e5; padding-left: 10px; font-weight: bold; margin-bottom: 10px">图文详情</div>
        <div v-html="data.introduce"></div>
      </el-card>
    </el-space>
    <!-- 下单弹窗 -->
    <el-dialog v-model="dialogOpen" title="选择收货地址" width="560px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="orderForm" label-width="100px">
        <el-form-item label="数量" prop="quantity">
          <el-input v-model.number="orderForm.quantity" type="number" placeholder="请输入数量" />
          <div style="color:#999; font-size: 12px; margin-top: 4px">范围：1 ~ {{ data.stock }}</div>
        </el-form-item>
        <el-form-item label="收货地址" prop="addressId">
          <el-select v-model="orderForm.addressId" placeholder="请选择" style="width: 100%">
            <el-option v-for="addr in addressList" :key="addr.id" :label="addr.address + '（' + addr.name + ' ' + addr.phone + '）'" :value="addr.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitOrder">提交</el-button>
        <el-button @click="dialogOpen=false">取消</el-button>
      </template>
    </el-dialog>
  </div>
  
</template>

<script setup>
import request from "@/utils/http.js";
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {ElMessage, ElMessageBox} from "element-plus";
import tools from "@/utils/tools.js";

const route = useRoute()
const router = useRouter()
const data = ref({})
const dialogOpen = ref(false)
const formRef = ref()
const orderForm = ref({ quantity: 1, addressId: undefined })
const addressList = ref([])

onMounted(() => {
  const id = route.params.id
  request.get(`/product/selectById/${id}`).then(res => {
    data.value = res.data
  })
})

function buyNow() {
  if (!data.value || !data.value.id) return
  if (data.value.stock <= 0) {
    ElMessage.warning('库存不足，暂不可购买')
    return
  }
  const user = tools.getCurrentUser()
  if (!user || !user.username) {
    ElMessage.warning('请先登录')
    return router.push('/login')
  }
  request.get('/shippingAddress/page', { params: { username: user.username, pageNum: 1, pageSize: 100 } }).then(res => {
    const list = res?.data?.list || []
    if (!list.length) {
      ElMessageBox.alert('您还没有收货地址，请先前往个人中心添加收货地址', '提示', {
        confirmButtonText: '去添加'
      }).then(() => {
        router.push({ path: '/personalCenter', query: { type: 'shippingAddress' } })
      })
      return
    }
    addressList.value = list
    orderForm.value = { quantity: 1, addressId: list[0].id }
    dialogOpen.value = true
  })
}

function validateQuantity(qty) {
  if (qty == null) return false
  if (!Number.isInteger(Number(qty))) return false
  const n = Number(qty)
  return n > 0 && n <= Number(data.value?.stock || 0)
}

function submitOrder() {
  if (!validateQuantity(orderForm.value.quantity)) {
    return ElMessage.error(`数量必须为 1 ~ ${data.value.stock} 之间的正整数`)
  }
  if (!orderForm.value.addressId) {
    return ElMessage.error('请选择收货地址')
  }
  // 生成订单（简化：当前系统订单表未包含数量与总价字段，仅记录单价和收货信息）
  const addr = addressList.value.find(a => a.id === orderForm.value.addressId)
  const user = tools.getCurrentUser()
  const payload = {
    productName: data.value.name,
    username: user.username,
    price: data.value.price,
    status: '待支付',
    receiverName: addr?.name,
    receiverPhone: addr?.phone,
    receiverAddress: addr?.address
  }
  // 使用数量计算总价
  const qty = Number(orderForm.value.quantity || 1)
  const totalPrice = Number(data.value.price || 0) * qty
  payload.price = totalPrice
  payload.quantity = qty
  request.post('/productOrder/add', payload).then(res => {
    if (!res) return
    ElMessage.success('操作成功')
    dialogOpen.value = false
    router.push({ path: '/personalCenter', query: { type: 'productOrder' } })
  })
}
</script>


