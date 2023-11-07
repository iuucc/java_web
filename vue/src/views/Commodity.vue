<script>
export default {
  name: "Commodity",
  data() {
    return {
      tableData: Object,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      tradename: "",
      type: "",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      roles:[],
      id:JSON.parse(localStorage.getItem("userAll")).id,
      sellername:JSON.parse(localStorage.getItem("userAll")).nickname,
    };
  },
  created() {
    //请求分页查询数据
    this.load();
  },
  methods:{
    handleAdd(){
      this.dialogFormVisible=true
      this.form={}
    },
    reset() {
      this.tradename = "";
      this.type = "";
      this.load();
    },
    load() {
      console.log(JSON.parse(localStorage.getItem("userAll")).id)
      this.request
          .get("/commodity/page", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              tradename: this.tradename,
              type: this.type,
              id:this.id
            },
          })
          .then((res) => {
            this.tableData = res.data.records
            this.total = res.data.total;

          });
    },
    save() {
      this.form.userid=this.id
      this.form.sellername=this.sellername
      this.request.post("/commodity", this.form).then((res) => {
        if (res.code==='200') {
          this.$message.success("保存成功");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("保存失败");
        }
      });
    },
    changeEnable(row){
      this.request.post("/commodity",row).then(res=>{
        if(res.code==='200'){
          this.$message.success("操作成功")
        }
      })
    },
    handleAvatarSuccess(res){
      this.form.image=res
    },
    handleEdit(row){
      this.form = row;
      this.dialogFormVisible = true;
    },
    del(id) {
      this.request.delete("/commodity/" + id).then((res) => {
        if (res.code==='200') {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error("删除失败");
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    delBatch(){
      let ids = this.multipleSelection.map((v) => v.id);
      this.request.post("/commodity/del/batch", ids).then((res) => {
        if (res.code==='200') {
          this.$message.success("批量删除成功");
          this.load();
        } else {
          this.$message.error("批量删除失败");
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
  }
}
</script>

<template>
  <div>
    <div style="padding: 10px 0">
      <el-input
          style="width: 200px"
          placeholder="请输入名称"
          suffix-icon="el-icon-search"
          v-model="tradename"
      >
      </el-input>
      <el-input
          style="width: 200px; margin-left: 5px"
          placeholder="请输入种类"
          suffix-icon="el-icon-message"
          v-model="type"
      >
      </el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"
      >搜索</el-button
      >
      <el-button style="margin-left: 5px" type="warning" @click="reset"
      >重置</el-button
      >
    </div>
    <div style="margin: 5px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
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

    </div>
    <el-table
        :data="tableData"
        :border="true"
        :stripe="true"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column
          prop="tradename"
          label="商品名"
          width="140"
      ></el-table-column>
      <el-table-column
          prop="description"
          label="商品描述"
          width="120"
      ></el-table-column>
      <el-table-column prop="price" label="商品价格" width="100"></el-table-column>
      <el-table-column prop="type" label="商品种类" width="150"></el-table-column>
      <el-table-column prop="inventory" label="库存" width="50"></el-table-column>
      <el-table-column  prop="image" label="图片" >
        <template slot-scope="scope">
        <img  :src="tableData[scope.$index].image"  style="width: 150px;height:150px;margin-left: 70px">
        </template>
      </el-table-column>

      <el-table-column label="操作" width="220">
        <template slot-scope="scope">
          <el-button
              type="success"
              @click="handleEdit(scope.row)"
              style="margin-left: 20px"
          >编辑 <i class="el-icon-edit"></i
          ></el-button>
          <el-popconfirm
              style="margin-left: 5px"
              confirm-button-text="确定"
              cancel-button-text="我再想想"
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference"
            >删除<i class="el-icon-remove-outline"></i
            ></el-button>
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
    <el-dialog title="商品信息" :visible.sync="dialogFormVisible">
      <el-form label-width="60px">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess">
          <img v-if="form.image" :src="form.image"  class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-form-item label="名称">
          <el-input v-model="form.tradename" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="种类">
          <el-input v-model="form.type" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model="form.inventory" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style >
.avatar-uploader{
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width:138px;
  height: 138px;
  display: block;
}

</style>