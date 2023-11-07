<script>
export default {
  name: "index_detail",
  data(){
    return{
      file: localStorage.getItem("file") ? JSON.parse(localStorage.getItem("file")) : [],
      num: 1,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      order: {},
      userAll: {},
      input:'',
      form:{},
      value2: null,
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      items: [], // 后端返回的评论数据组
      mycomments:{},
      text:"",
    }
  },
  computed: {
    fullName() {
      return this.num * this.file.price;
    }
  },
  created() {
    this.request.get("/orders/finduser/"+this.user.username).then(res=>{
      localStorage.setItem("userAll",JSON.stringify(res.data))
      this.userAll=localStorage.getItem("userAll") ? JSON.parse(localStorage.getItem("userAll")) : {}
    })
    this.request.get("/comments/one/"+this.file.id).then(res=>{
      console.log(res)
      this.items=res.data
    })

  },
  methods:{
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleChange(value) {
      console.log(value);
    },
    bug(){
      this.order.orderNumber = Math.floor(10000000 + Math.random() * 90000000).toString().substring(0, 8);
      this.order.fileId=this.file.id
      this.order.amount=this.num
      this.order.userId=this.userAll.id
      this.order.userAddress =this.userAll.address
      this.order.userPhone=this.userAll.phone
      this.order.total=this.fullName
      this.order.fileName=this.file.tradename
      this.order.paid="未支付"
      this.order.delivery="未发货"
      this.order.sellername=this.file.sellername
      this.order.filepiture=this.file.image
      console.log(this.order)
      this.save()
      this.freshFiles()
      this.load()
    },
    save() {
      this.request.post("/orders",this.order).then((res) => {
        console.log(this.order)
        if (res.code==='200') {
          this.$message.success("购买成功");
        } else {
          this.$message.error("购买失败");
        }
      });
    },
    freshFiles(){
      this.request.get("/commodity/all").then(res=>{
        localStorage.setItem("files",JSON.stringify(res.data))
        console.log(res.data.filter(v=>v.image))
        this.files=res.data.filter(v=>v.image)
      })
    },
    load(){
      this.request.get("/orders/finduser/"+this.user.username).then(res=>{
        localStorage.setItem("userAll",JSON.stringify(res.data))
        this.userAll=localStorage.getItem("userAll") ? JSON.parse(localStorage.getItem("userAll")) : {}
      })
    },
    addtoCar(){
      this.order.carnumber = Math.floor(10000000 + Math.random() * 90000000).toString().substring(0, 8);
      this.order.fileid=this.file.id
      this.order.amount=this.num
      this.order.userId=this.userAll.id
      this.order.userAddress =this.userAll.address
      this.order.userPhone=this.userAll.phone
      this.order.total=this.fullName
      this.order.fileName=this.file.tradename
      this.order.sellername=this.file.sellername
      this.order.filepiture=this.file.image
      console.log(this.order)
      this.request.post("/car",this.order).then((res) => {
        console.log(this.order)
        if (res.code==='200') {
          this.$message.success("成功加入购物车");
        } else {
          this.$message.error("加入购物车失败");
        }
      });
    },
    onSubmit(){
      console.log(this.value2)
        this.mycomments.fileId=this.file.id
        this.mycomments.userId=this.userAll.id
        this.mycomments.userName=this.userAll.nickname
        this.mycomments.text=this.text
        this.mycomments.image=this.user.avatarUrl
        this.mycomments.scores=this.value2
        this.request.post("/comments",this.mycomments).then((res) => {
        console.log(this.mycomments)
        if (res.code==='200') {
          this.$message.success("发表成功");
          this.value2=null;
          this.text=""
        } else {
          this.$message.error("发表失败");
        }
      });
      this.request.get("/comments").then(res=>{
        console.log(res)
        this.items=res.data
      })
    }

  }

}
</script>

<template>
<div>
  <div style="width: 100%;height: 400px;background-color: #448CBB;margin-top: 20px">
    <div style="float: left;height: 400px;width: 400px;margin-left: 350px" >
      <img :src="file.image" alt="" style="width: 400px; height: 400px;">
    </div>
    <div style="float: left;background-color: #FDFEFE  ;width: 400px;height: 400px;">
      <div class="sku-name" style="margin-left: 5px">
        <img src="https://img10.360buyimg.com/ling/jfs/t1/171125/33/30080/10168/6311f027Ef4ece6e4/b4dae3917f8e68f4.png" alt="" style="height: 16px;">
        {{file.tradename }}
      </div>
      <div class="new" style="margin-left: 5px">
        {{file.description }}
      </div>
      <div style="background-color: #f3f3f3;height: 315px;">
        <div class="price" >
          <div class="dt">
            京 东 价
          </div>
          <div class="dd">
            <span>￥</span>
            <span>{{file.price}}</span>
          </div>
        </div>
        <div class="kucun">
          <span>库 存 :  </span>
          <span >{{file.inventory}}</span>
        </div>
        <div style="margin-top: 20px;margin-left: 5px">
          <span>数量</span>
          <el-input-number v-model="num" controls-position="right" @change="handleChange" :min="1" :max="10"></el-input-number>
          <span>总计</span>
          <el-input v-model="fullName"  placeholder="" style="width: 60px;"></el-input>
        </div>
        <div style="margin-top: 20px;margin-left: 5px">
          <el-button type="danger" size="medium" @click="bug">购买</el-button>
          <div style="float: right;margin-right: 5px">
            <el-button type="danger" size="medium" @click="addtoCar" >加入购物车</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
<!--  评论区-->
  <div style="width: 800px;height: 200px;background-color: #FDFEFE;margin: 0 auto">
    <div class="block">
      <span class="demonstration">评分</span>
      <el-rate
          v-model="value2"
          :colors="colors">
      </el-rate>
    </div>
  <el-form ref="form" :model="form" >
  <el-form-item label="评论">
    <el-input type="textarea" v-model="text"></el-input>
  </el-form-item>
  <el-form-item style="">
    <el-button type="primary" @click="onSubmit">发送评论</el-button>
  </el-form-item>
  </el-form>
    <el-divider content-position="center">评论区</el-divider>
  </div>
  <div class="comment" v-for="(items, index) in items" :key="index">
    <el-card style="width: 800px;margin: 5px auto">
      <div style="width: 800px;height: 200px;background-color: rgb(255,255,255);margin: 10px auto ;border-top: rgb(221, 221, 221) solid 1px;">
        <div style="width: 100px;height: 200px;float: left">
         <div style="width: 100px;height: 50px;">
           <img :src="items.image" alt="" style="width: 30px;height: 30px;margin: 2px 0">
           <h6>{{items.userName}}</h6>
         </div>
        </div>
        <div style="height: 200px;width: 700px;float: left">
         <div style="height: 30px;width: 700px;background-color: #ffffff">
           <el-rate
               v-model="items.scores"
               disabled
               show-score
               text-color="#ff9900"
               score-template="{value}">
           </el-rate>
         </div>
          <div style="height: 170px;width: 700px;background-color: #ffffff;margin-top: 10px">
            {{items.text}}
           <div style="width: 700px;height: 10px;margin-top: 120px;line-height: 10px;font-size: 10px">
             {{items.time[0]+"-"+items.time[1]+"-"+items.time[2]+"  "+items.time[3]+":"+items.time[4]+":"+items.time[5]}}
           </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>

</div>
</template>

<style >
*{
  margin: 0;
  padding: 0;
}
.sku-name{
  font: 700 16px Arial,"microsoft yahei";
  color: #666;
  padding-top: 10px;
  line-height: 28px;
  margin-bottom: 5px;
}
.new{
  color: #e4393c;
  font: 700 10px Arial,"microsoft yahei";
}
.price{
  margin-top: 10px;
  height: 50px;
}
 .dt{
  float: left;
  padding-left: 10px;
  color: #999;
}
.dd{
  float: left;
  margin-left: 10px;
  color: #e4393c;
  font-family: "microsoft yahei";
  margin-right: 10px;
}
.kucun{
  padding-left: 10px;
  color: #999;
}
</style>