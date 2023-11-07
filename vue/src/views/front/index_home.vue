<script>
export default {
  name: "Home",
  data(){
    return{
      imgs:[
          'https://m.360buyimg.com/babel/jfs/t20260925/10205/2/22555/99153/651294bbF848535ec/93fa7906b4d3d144.jpg',
          'https://m.360buyimg.com/babel/jfs/t20261006/6310/18/21849/145317/65211e0dF4416202b/8c29adf86f7e7db0.jpg',
          'https://m.360buyimg.com/babel/jfs/t20260927/105403/19/34764/72376/6514dff8F1c7b393f/af3e065165e8651d.jpg',
          'https://m.360buyimg.com/babel/jfs/t20261007/59890/24/25865/57101/6522818eF318cf214/6a9720ceadb84ce0.jpg',
          'https://m.360buyimg.com/babel/jfs/t20261007/222971/9/30786/92720/65220934F3bdb3c93/7f5bc96261512124.jpg'
      ],
      files:[],
      file:[],
      type:null
    }
  },
  created() {
    this.request.get("/commodity/all").then(res=>{
      localStorage.setItem("files",JSON.stringify(res.data))
      this.files=res.data.filter(v=>v.image)
    })
  },
  methods:{
    getDetail(id){
        this.$router.push("/index/detail")
        console.log(this.files.find(file => file.id === id))
        const file = this.files.find(file => file.id === id)
      localStorage.setItem("file",JSON.stringify(file))
    },
    handleClick1(){
      this.type="新鲜水果"
      this.request.post("/commodity/whattype/type",this.type).then(res=>{
        localStorage.setItem("files",JSON.stringify(res.data))
        this.files=res.data.filter(v=>v.image)
      })
    },
    handleClick2(){
      this.type="海鲜水产"
      this.request.post("/commodity/whattype/type",this.type).then(res=>{
        localStorage.setItem("files",JSON.stringify(res.data))
        this.files=res.data.filter(v=>v.image)
      })
    },
  }
}
</script>

<template>
<div>
<!--  头部标签栏-->
  <div style="height: 30px;background-color: rgb(255,255,255);">
  </div>
  <!--主体-->
  <div style="background-image: url('https://img11.360buyimg.com/cms/jfs/t9016/320/246907648/570978/346ecda1/59a3d65eNd604759b.png'); background-position: -230px 0px;height: 400px;">
    <div>
      <!--左侧边栏-->
      <div>
        <div style="background-color: #3F5EFB;width: 200px;height: 20px;position: absolute;left: 160px;top: 70px;line-height: 20px;font-size: 14px; color: #fff;background: #222;text-align: center">
            京东生鲜商品分类
        </div>
        <div style="width: 200px;height: 400px;float: left;margin-left: 160px">
          <div class="card">
            <img src="../../../src/assets/img1.png" class="card-iocn" >
            <a href="#" style="" @click="handleClick1" >新鲜水果</a>
            <span> > </span>
          </div>
          <div class="card">
            <img src="../../../src/assets/img2.png" class="card-iocn" >
            <a href="#" style="" @click="handleClick2">海鲜水产</a>
            <span> > </span>
          </div>
          <div class="card">
            <img src="../../../src/assets/img3.png" class="card-iocn" >
            <a href="#" style="">新鲜水果</a>
            <span> > </span>
          </div>
          <div class="card">
            <img src="../../../src/assets/img4.png" class="card-iocn" >
            <a href="#" style="">新鲜水果</a>
            <span> > </span>
          </div>
          <div class="card">
            <img src="../../../src/assets/img5.png" class="card-iocn" >
            <a href="#" style="">新鲜水果</a>
            <span> > </span>
          </div>
        </div>
      </div>
      <!--  轮播图-->
      <div class="block" style="width: 800px;margin: 0 auto;">
        <el-carousel height="400px">
          <el-carousel-item v-for="item in imgs" :key="item">
            <img :src="item" alt="" style="vertical-align: center">
          </el-carousel-item>
        </el-carousel>
      </div>
<!--      右侧边栏-->
      <div style="width: 200px;height: 400px;background-color: rgb(255,255,255) ;float: right;margin-right: 160px;position: absolute; left: 1160px;top: 90px;">
        <div style="width: 200px;height: 60px;background-color:rgb(255,255,255);border-top: #42b983 solid 5px;text-align: center;line-height: 60px">
            <span>--</span>
          <span>生鲜头条</span>
          <span>--</span>
        </div>
        <div class="toutiao" style="width: 180px;margin-left: 20px;margin-top: 10px">
          <ul style="font: 12px/1.5 Microsoft YaHei,tahoma,arial,Hiragino Sans GB,\\5b8b\4f53,sans-serif;color: #222222;">
            <a href="#" style="display: block;margin-bottom: 5px; text-decoration: none;"><li>精挑细选100分营养美味</li></a>
            <a href="#" style="display: block;margin-bottom: 5px; text-decoration: none;"><li>0元试吃 全新上线 抓紧尝鲜</li></a>
            <a href="#" style="display: block;margin-bottom: 5px; text-decoration: none;"><li>一键立享 用心做顿好饭</li></a>
          </ul>
        </div>
      </div>
    </div>
  </div>
<!--  商品页-->
    <div style="margin: 10px auto;width: 1200px" @click="getDetail(item.id)">
      <el-row :gutter="10" >
        <el-col :span="6" v-for="item in files"  style="margin-bottom: 10px;" :key="item.id" :offset="index > 0 ? 2 : 0">
          <el-card :body-style="{ padding: '0px' }" shadow="hover">
            <img :src="item.image" alt="" style="width: 100%; ">
            <div style="padding: 14px;height: 125px;">
              <span>{{item.tradename}}</span>
              <div class="bottom clearfix">
                <time class="time">{{item.description}}</time>
                <el-button type="text" @click="getDetail(item.id)" class="button" style="float: right;">查看详细</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
  </div>
</div>
</template>

<style >
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
.demonstration{
  margin: 0 25px;
  font-size: 14px;
  color: #222;
}
.demonstration > a{
  text-decoration: none;
}
.demonstration>a:hover{
  color: rgb(45, 208, 122);
}
.card{
  opacity: 0.95;
  width: 200px;
  height: 80px;
  border-bottom: 1px solid;
  background-color: rgb(255,255,255);
}
.card-iocn{
  width: 26px;height: 26px;margin: 26px 20px
}
.card >a{
  position: relative;
  height: 19px;
  font-size: 14px;
  color: #222;font-weight: 700;
  bottom: 35px;left: 10px;
  text-decoration: none;
}
.card > span{
  position: relative;
  height: 19px;
  font-size: 14px;
  bottom: 35px;left: 60px;
}
.card >a:hover{
  color: rgb(45, 208, 122);
}
.toutiao > li:hover{
  color: rgb(45, 208, 122);
}
</style>