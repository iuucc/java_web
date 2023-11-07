<script>
export default {
  name: "index_car",
  data() {
    return{
      file: localStorage.getItem("file") ? JSON.parse(localStorage.getItem("file")) : [],
      tableData:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      id:0,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      userAll:localStorage.getItem("userAll") ? JSON.parse(localStorage.getItem("userAll")) : {},
      carnumber:"",
      fileName:"",
      dialogFormVisible: false,
      form:{},
      order:{},
      multipleSelection: [],
    }
  },
  created() {
    this.request
        .get("/car/page/", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            id:this.userAll.id
          },
        })
        .then((res) => {
          this.tableData = res.data.records
          this.total = res.data.total;
        });
  },
  methods:{
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
      load() {
        this.request
            .get("/car/page/", {
              params: {
                pageNum: this.pageNum,
                pageSize: this.pageSize,
                id:this.userAll.id,
                carnumber: this.carnumber,
                fileName: this.fileName,
              },
            })
            .then((res) => {
              this.tableData = res.data.records
              this.total = res.data.total;
            });
      },
    reset() {
      this.username = "";
      this.carnumber = "";
      this.fileName = "";
      this.load();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    handleEdit(row) {
      this.form = row;
      this.dialogFormVisible = true;
    },
    save(){
      this.request.post("/car", this.form).then((res) => {
        if (res.code==='200') {
          this.$message.success("保存成功");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("保存失败");
        }
      });
    },
    del(id) {
      console.log(id.orderNumber)
      this.request.delete("/car/" + id.carnumber).then((res) => {
        if (res.code==='200') {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error("删除失败");
        }
      });
    },
    buy(row){
      console.log(row)
      this.order.orderNumber =row.carnumber;
      this.order.fileId=row.fileid
      this.order.amount=row.amount
      this.order.userId=row.userId
      this.order.userAddress =row.userAddress
      this.order.userPhone=row.userPhone
      this.order.total=row.total
      this.order.fileName=this.file.tradename
      this.order.sellername=this.file.sellername
      this.order.filepiture=this.file.image
      this.order.paid="未支付"
      console.log(this.order)
      this.request.post("/orders",this.order).then((res) => {
        console.log(this.order)
        if (res.code==='200') {
          this.$message.success("购买成功");
          this.dialogFormVisible = false;
          this.request.delete("/car/" +row.carnumber).then((res) => {
          });
          this.load()
        } else {
          this.$message.error("购买失败");
        }
      });

    },
    byall(){
      for (let i=0;i<this.multipleSelection.length;i++){
        console.log(this.multipleSelection[i])
        this.order.orderNumber =this.multipleSelection[0].carnumber;
        this.order.fileId=this.multipleSelection[i].fileid
        this.order.amount=this.multipleSelection[i].amount
        this.order.userId=this.multipleSelection[i].userId
        this.order.userAddress =this.multipleSelection[i].userAddress
        this.order.userPhone=this.multipleSelection[i].userPhone
        this.order.total=this.multipleSelection[i].total
        this.order.sellername=this.file.sellername
        this.order.fileName=this.multipleSelection[i].fileName
        this.order.filepiture=this.multipleSelection[i].filepiture
        this.order.paid="未支付"
        this.order.delivery="未发货"
        console.log(this.order)
        this.request.post("/orders",this.order).then((res) => {
          console.log(this.order)
          if (res.code==='200') {
            this.$message.success("购买成功");
            this.dialogFormVisible = false;
            this.request.delete("/car/" +this.multipleSelection[i].carnumber).then((res) => {
            });
            this.load()
          } else {
            this.$message.error("购买失败");
          }
        });
      }

    },
    delBatch(){
      console.log(this.multipleSelection.map)
      let ids = this.multipleSelection.map((v) => v.carnumber);
      this.request.post("/car/del/batch", ids).then((res) => {
        if (res.code==='200') {
          this.$message.success("批量删除成功");
          this.load();
        } else {
          this.$message.error("批量删除失败");
        }
      });
    },

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
        v-model="carnumber"
    >
    </el-input>
    <el-input
        style="width: 200px; margin-left: 5px"
        placeholder="请输入物品名称"
        suffix-icon="el-icon-message"
        v-model="fileName"
    >
    </el-input>
    <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
    <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    <el-popconfirm
        style="margin-left: 5px"
        confirm-button-text="确定"
        cancel-button-text="我再想想"
        icon="el-icon-info"
        icon-color="red"
        title="您确定批量删除吗？"
        @confirm="delBatch"
    >
      <el-button type="danger" slot="reference"
      >批量删除 <i class="el-icon-remove-outline"></i
      ></el-button>
    </el-popconfirm>
    <el-popconfirm
        style="margin-left: 5px"
        confirm-button-text="确定"
        cancel-button-text="我再想想"
        icon="el-icon-info"
        icon-color="red"
        title="您确定批量购买吗？"
        @confirm="byall"
    >
      <el-button type="danger" slot="reference"
      >批量购买 <i class="el-icon-remove-outline"></i
      ></el-button>
    </el-popconfirm>
  </div>
  <el-table
      :data="tableData"
      :border="true"
      :stripe="true"
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column
        prop="carnumber"
        label="订单编号"
        width="100"
    ></el-table-column>
    <el-table-column
        prop="fileName"
        label="物品名称"
        width="100"
    ></el-table-column>
    <el-table-column prop="amount" label="数量" width="50"></el-table-column>
    <el-table-column prop="userAddress" label="地址" width="100"></el-table-column>
    <el-table-column prop="time" label="订单时间" width="120"></el-table-column>
    <el-table-column prop="sellername" label="所属商家" width="120"></el-table-column>
    <el-table-column label="操作" >
      <template slot-scope="scope">
        <el-button
            type="info"
            @click="handleEdit(scope.row)"
            style="margin-left: 30px"
        >编辑 <i class="el-icon-edit"></i
        ></el-button>
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
          <el-button type="success" slot="reference" @click="buy(scope.row)">购买<i class="el-icon-remove-outline"></i></el-button>
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
  <el-dialog title="订单信息" :visible.sync="dialogFormVisible">
    <el-form label-width="60px">
      <el-form-item label="数量">
        <el-input v-model="form.amount" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.userAddress" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<style scoped>

</style>