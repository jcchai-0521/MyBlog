<template>
  <div>
    <Header></Header>
    <div class="main-content">
      <h2>{{blog.title}}</h2>
      <el-link icon="el-icon-edit" v-if="ownBlog">
        <router-link :to="{name: 'BlogEdit', param: {blogId: blog.id}}">
          编辑
        </router-link>
      </el-link>
      <div class="markdown-body" v-html="blog.content"></div>
    </div>
  </div>
</template>

<script>
import Header from "@/views/Header";
import 'github-markdown-css'

export default {
  name: "BlogDetail.vue",
  components:{Header},
  data(){
    return {
      blog:{
        id: "",
        title: "",
        description: "",
        content: ""
      },
      ownBlog:false
    }
  },created() {
    const blogId = this.$route.params.blogId;
    if (blogId)
    {
      const _this = this;
      _this.$axios.get("/blog/" + blogId).then(response =>{
        const blogInfo = response.data.data;
        _this.blog.id = blogInfo.userId;
        _this.blog.title = blogInfo.title;

        var MarkdownIt = require("markdown-it");
        var mk = new MarkdownIt();
        _this.blog.content = mk.render(blogInfo.content);

        // 判断是否是登录用户的博客
        _this.ownBlog = (blogInfo.userId === _this.$store.getters.getUserInfo.id);
      });
    }
  }
}
</script>

<style scoped>
  .main-content{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    width: 100%;
    min-height: 700px;
    padding: 20px 15px;
  }
</style>