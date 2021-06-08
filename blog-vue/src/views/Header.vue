<template>
  <div class="m-header">
    <h2>欢迎来到我的博客</h2>
    <div class="block"><el-avatar :size="50" :src="circleUrl"></el-avatar></div>
    <div class="m-content">
      <span>
        <el-link type="primary" href="/blogs">主页</el-link>
      </span>
      <el-divider direction="vertical"></el-divider>
      <span>
        <el-link type="success" href="/blog/add">发表博客</el-link>
      </span>
      <el-divider direction="vertical"></el-divider>
      <span v-if="!hasLogin">
        <el-link type="info" href="/login">登录</el-link>
      </span>
      <span v-if="hasLogin">
        <el-link type="info" @click="logout">退出</el-link>
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return {
      circleUrl: "",
      userInfo:{
        username: ''
      },
      hasLogin: false
    }
  },
  methods:{
    /**
     * 退出或者登陆处理
     */
    logout() {
      // 赋值this用户
      const _this = this;

      // 调用后台进行退出处理
      _this.$axios.get("/logout", {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(response =>{
        // 清理保存的用户信息
        _this.$store.commit("REMOVE_USERINFO");
        // 跳转到登陆页面
        _this.$router.push("/login");
      });


    }
  },
  created() {
    if (this.$store.getters.getUserInfo.username)
    {
      // 已经有登录用户
      this.username = this.$store.getters.getUserInfo.username;
      this.hasLogin = true;
    }
  }
}
</script>

<style scoped>
.m-header{
  max-width: 960px;
  margin: 0 auto;
  text-align: center;
}
.m-content{
  margin: 10px 0;
}
</style>