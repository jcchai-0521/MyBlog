<template>
  <div>
    <Header></Header>
    <div class="block">
      <el-timeline>
        <el-timeline-item :timestamp="blog.created" placement="top" v-for="blog in blogs">
          <el-card>
            <h4>
              <router-link :to="{name: 'BlogDetail', params: {blogId: blog.id}}">
                {{blog.title}}
              </router-link>
            </h4>
            <p>{{blog.description}}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-pagination class="pageInfo"
          background
          layout="prev, pager, next"
          :total="total"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change=page>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import Header from "@/views/Header";
export default {
  name: "Blogs.vue",
  components: {Header},
  data(){
    return {
      blogs: {},
      currentPage: 1,
      total: 0,
      pageSize: 5
    }
  },
  methods:{
    page(currentPage){
      const _this = this;
      _this.$axios.get("/blogs?currentPage=" + this.currentPage).then(response =>{
        const dataInfo = response.data.data;
        _this.blogs = dataInfo.records;
        _this.currentPage = dataInfo.current;
        _this.total = dataInfo.total;
        _this.pageSize = dataInfo.size;
      });
    }
  },
  created() {
    // 请求服务端加载博客列表信息，默认加载第一页
    this.page(1);
  }
}
</script>

<style scoped>
.pageInfo{
  float: right;
}
</style>