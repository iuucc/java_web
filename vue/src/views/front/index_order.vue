<script>
export default {
  name: "index_order",
  data(){
    return{
      tableData:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      id:0,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      userAll:localStorage.getItem("userAll") ? JSON.parse(localStorage.getItem("userAll")) : {},
      orderNumber:"",
      fileName:""
    }
  },
  created() {
    this.request
        .get("/orders/page/", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            id:this.userAll.id
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
          .get("/orders/page/", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              id:this.userAll.id,
              orderNumber: this.orderNumber,
              fileName: this.fileName,
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
      this.username = "";
      this.ornumber = "";
      this.fileName = "";
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
            console.log(res)
            this.$router.push("/index/orderdetail")
            localStorage.setItem("files",JSON.stringify(res.data.records))
          });

    },
    shouHuo(id){
      this.request.get("/orders/updata2/"+id.orderNumber)
          .then((res) => {
            if (res.code==='200'&& id.delivery==="已发货") {
              this.$message.success("收货成功");
              // this.load();
            } else if(id.delivery==="已收货"){
              this.$message.error("订单已经收货过了!!!");
            } else if(id.delivery==="未发货"){
              this.$message.error("订单尚未发货!!!");}
            else {
              this.$message.error("收货失败");
            }
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
    <el-input
        style="width: 200px; margin-left: 5px"
        placeholder="请输入物品名称"
        suffix-icon="el-icon-message"
        v-model="fileName"
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
            width="120"
        ></el-table-column>
        <el-table-column prop="userAddress" label="地址" width="130"></el-table-column>
        <el-table-column prop="time" label="购买时间" width="150"></el-table-column>
        <el-table-column prop="sellername" label="所属商家" width="70"></el-table-column>
        <el-table-column prop="paytime" label="支付时间" width="150"></el-table-column>
        <el-table-column prop="deliverytime" label="发货时间" width="150"></el-table-column>
        <el-table-column prop="receipttime" label="收货时间" width="150"></el-table-column>
        <el-table-column prop="paid" label="订单状态" width="150"></el-table-column>
        <el-table-column prop="delivery" label="发货状态" width="70"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-popconfirm
                style="margin-left: 5px"
                confirm-button-text="确定"
                cancel-button-text="我再想想"
                icon="el-icon-info"
                icon-color="red"
                title="您确定支付吗？"
                @confirm="pay(scope.row)"
            >
            <el-button type="success" slot="reference" >支付<i class="el-icon-circle-check"></i></el-button>
            </el-popconfirm>
            <el-popconfirm
                style="margin-left: 5px"
                confirm-button-text="确定"
                cancel-button-text="我再想想"
                icon="el-icon-info"
                icon-color="red"
                title="您确定删除吗？"
                @confirm="del(scope.row)"

            >
              <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
              <el-button type="primary" icon="el-icon-tickets" slot="reference" @click="detail(scope.row.orderNumber) " >详细</el-button>
            </el-popconfirm>
            <el-popconfirm
                style="margin-left: 5px"
                confirm-button-text="确定"
                cancel-button-text="我再想想"
                icon="el-icon-info"
                icon-color="red"
                title="您确定收货吗？"
                @confirm="shouHuo(scope.row)"
            >
              <el-button type="warning" slot="reference" >收货<i class="el-icon-circle-check"></i></el-button>
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

<style >
.table{
  margin: 0 auto;
  border-color: #666666;
}
</style>