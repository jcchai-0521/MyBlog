<template>
  <div>
    <Header></Header>
    <div class="m-content">
      <el-form ref="editForm" status-icon :model="editForm" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="editForm.title"></el-input>
        </el-form-item>
        <el-form-item label="摘要" prop="description">
          <el-input type="textarea" v-model="editForm.description"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <mavon-editor v-model="editForm.content"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('editForm')">新增</el-button>
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Header from "@/views/Header";
export default {
  name: "BlogEdit.vue",
  components: {Header},
  data(){
    return {
      editForm: {
        blogId: "",
        title: "",
        description: "",
        content: ""
      },
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入摘要', trigger: 'blur' }
        ],
        content: [
          { trequired: true, message: '请输入内容', trigger: 'blur' }
        ]
      }
    }
  },
  methods:{
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 调用服务端进行添加处理
          const _this = this;
          _this.$axios.post("/blog/edit", this.editForm, {
            headers:{
              "Authorization": _this.$store.getters.getToken
            }
          }).then(response =>{
            // 添加完成后跳转至列表页面
            _this.$alert('操作成功', '提示', {
              confirmButtonText: '确定',
              callback: action => {
                _this.$router.push("/blogs")
              }
            });
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName){
      this.$refs[formName].resetFields();
    }
  },
  created() {
    // 获取URL上的参数
    const blogId = this.$route.params.blogId;
    if (blogId)
    {
      const _this = this;
      _this.$axios.get("/blog/" + blogId).then(response =>{
        const blogInfo = response.data.data;
        _this.editForm.blogId = blogInfo.blogId;
        _this.editForm.title = blogInfo.title;
        _this.editForm.description = blogInfo.description;
        _this.editForm.content = blogInfo.content;
      });
    }
    console.log(blogId);
  }
}
</script>

<style scoped>

</style>