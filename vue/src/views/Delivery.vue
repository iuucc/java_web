<script>
export default {
  name: "Delivery",
  data(){
    return{
      tableData:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      id:0,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      orderNumber:"",
      fileName:""

    }
  },
  created() {
    this.request
        .get("/orders/sell", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name:this.user.nickname,
            orderNumber: this.orderNumber,
          },
        })
        .then((res) => {
          console.log(res);
          const uniqueRecords = res.data.records.filter((record, index, self) => {
            // 使用findIndex方法找到当前订单在数组中的第一个索引
            const firstIndex = self.findIndex((r) => r.orderNumber === record.orderNumber);
            // 只保留第一个索引对应的订单，去除后续重复的订单
            return index === firstIndex;
          });
          this.tableData = uniqueRecords;
          this.total = res.data.total;
        });
  },
  methods:{
    load() {
      this.request
          .get("/orders/sell/", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              name:this.user.nickname,
              orderNumber: this.orderNumber,
            },
          })
          .then((res) => {
            this.tableData = res.data.records
            this.total = res.data.total;
          });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    del(id) {
      console.log(id)
      console.log(id.orderNumber)
      this.request.delete("/orders/" + id.orderNumber+"/"+id.fileId).then((res) => {
        if (res.code==='200') {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error("删除失败");
        }
      });
    },
    reset() {
      this.load();
    },
    pay(id){
      this.request.get("/orders/updata/"+id.orderNumber+"/"+'1')
          .then((res) => {
            if (res.code==='200'&& id.paid==="未支付") {
              this.$message.success("支付成功");
              this.load();
            } else if(id.paid==="已支付"){
              this.$message.error("订单已经支付过了!!!");
            } else {
              this.$message.error("支付失败");
            }
          });
    },
    faHuo(id){
      this.request.get("/orders/updata1/"+id.orderNumber)
          .then((res) => {
            if (res.code==='200'&& id.delivery==="未发货") {
              this.$message.success("发货成功");
              // this.load();
            } else if(id.delivery==="已发货"){
              this.$message.error("订单已经发货过了!!!");
            } else {
              this.$message.error("发货失败");
            }
          });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    detail(id){
      console.log(id)
      this.request
          .get("/orders/page/", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              id:this.userAll.id,
              orderNumber:id,
              fileName:"",
            },
          })
          .then((res) => {
            this.$router.push("/index/orderdetail")
            localStorage.setItem("files",JSON.stringify(res.data.records))
          });

    }
  }
}
</script>

<template>
<div>
  <div style="padding: 10px 0">
    <el-input
        style="width: 200px"
        placeholder="请输入订单编号"
        suffix-icon="el-icon-search"
        v-model="orderNumber"
    >
    </el-input>

    <el-button style="margin-left: 5px" type="primary" @click="load"
    >搜索</el-button
    >
    <el-button style="margin-left: 5px" type="warning" @click="reset"
    >重置</el-button
    >
  </div>
  <div class="table" >
    <el-table
        :data="tableData"
        :border="true"
        :stripe="true"
        @selection-change="handleSelectionChange"
    >
      <el-table-column
          prop="orderNumber"
          label="订单编号"
          width="140"
      ></el-table-column>
      <el-table-column prop="userAddress" label="地址" width="150"></el-table-column>
      <el-table-column prop="paytime" label="支付时间" width="150"></el-table-column>
      <el-table-column prop="deliverytime" label="发货时间" width="150"></el-table-column>
      <el-table-column prop="delivery" label="发货状态" width="150"></el-table-column>
      <el-table-column label="操作" width="120px">
        <template slot-scope="scope">
          <el-popconfirm
              style="margin-left: 5px"
              confirm-button-text="确定"
              cancel-button-text="我再想想"
              icon="el-icon-info"
              icon-color="red"
              title="您确定发货吗？"
              @confirm="faHuo(scope.row)"
          >
            <el-button type="success" slot="reference" >发货<i class="el-icon-circle-check"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>

    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
      >
      </el-pagination>
    </div>
  </div>
</div>
</template>

<style scoped>

</style>