<script>
export default {
  data() {
    return {
      activeIndex: '1',
      activeIndex2: '1',
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    logout1(){
      this.$store.commit("logout")
      this.$message.success("退出成功")
    }
  }
}
</script>
<template>
  <div>
    <div class="header" style="margin: 0 auto;">
      <div class="log" >
        <img style="width: 110px; height:40px;" src="https://static.360buyimg.com/mtd/pc/fresh_v3/1.0.0/gb/images/mod_header_logo@2x.png" alt="">
      </div>
      <div class="topLink">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" router>
          <el-menu-item index="/index/home"> <i class="el-icon-s-home"></i>首页</el-menu-item>
          <el-menu-item index="/index/car"><i class="el-icon-shopping-cart-full"></i>购物车</el-menu-item>
          <el-menu-item index="/index/order"><i class="el-icon-money"></i>我的订单</el-menu-item>
        </el-menu>
      </div>
      <div class="person" style="width: 200px;float: right">
        <div v-if="!user.username" style="text-align: right;line-height: 60px;padding-right: 30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')" >注册</el-button>
        </div>
        <div v-else style="line-height: 60px">
          <el-dropdown style="width: 100px; cursor: pointer">
            <div style="display: inline-block">
              <img :src="user.avatarUrl" alt="" style="width: 23px;border-radius: 50%;position: relative;top: 7px;right: 10px" referrerpolicy="no-referrer">
              <span >{{ user.nickname }}</span>
              <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center">
              <el-dropdown-item style="font-size: 14px;padding:5px 0">
                <router-link to="/index/password">修改密码</router-link>
              </el-dropdown-item>
              <el-dropdown-item  style="font-size: 14px;padding:5px 0">
                <router-link to="/index/person">个人信息</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px;padding:5px 0">
                <span style="text-decoration: none" @click="logout1">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div>
        <router-view/>
    </div>

  </div>

</template>

<style >
.header{
  height: 60px;

}
.log{
  float: left;
  width: 60px;
  height: 60px;
  padding: 10px 20px 0;
  margin-right: 50px;margin-left: 80px;

}
.topLink{
  float: left;
  width: 500px;
  margin-left: 100px;
}
*{
  margin: 0;
  padding: 0;
}
.el-menu.el-menu--horizontal{
  border: none;
}
</style>