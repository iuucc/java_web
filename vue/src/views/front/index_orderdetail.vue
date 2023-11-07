<script>
export default {
  name: "index_orderdetail",
  data(){
    return{
      files:localStorage.getItem("files") ? JSON.parse(localStorage.getItem("files")) : {},
    }
  },

  methods:{
    back(){
      this.$router.push("order")
    },
  },
}
</script>

<template>
<div>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>订单详细</span>
      <el-button style="float: right; padding: 3px 0" type="text" @click="back">返回</el-button>
    </div>
    <div>
      <h3>订单编号：{{files[0].orderNumber}}</h3>
    </div>
    <div v-for="file in files" :key="file.id">
      <br>
    <h4 style="color: #E6A23C">商品名称：{{ file.fileName }}</h4>
      <img :src="file.filepiture" alt="">
     <h6> 数量：{{file.amount}}</h6>
     <h6 style="color: red"> 价格：{{file.total}}</h6>
    </div>
  </el-card>
</div>
</template>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 480px;
  margin: 0 auto;
}
</style>