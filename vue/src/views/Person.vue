<template>
  <el-card style="width: 500px;">
    <el-form label-width="60px" size="small">
      <el-upload
          class="avatar-uploader"
          :action="'http://'+serverIp+':9090/file/upload'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          >
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="学工号">
        <el-input v-model="form.account" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="教师">
        <el-input v-model="form.teacherName" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Person",
  data(){
    return {
      form:{},
      user: localStorage.getItem("userInfo") ? JSON.parse(localStorage.getItem("userInfo")):{},
      serverIp: serverIp
    }
  },
  created() {
      this.getUser().then(res=>{
        console.log(res)
        this.form = res
      })
  },
  methods:{
    async getUser(){//
      return (await this.request.get("/user/account/"+this.user.account)).data
    },
    save(){
      this.request.post("/user",this.form).then(res=>{
        if(res.code==='200'){
          this.$message.success("保存成功")
          //触发父级，通过父级来获取后台数据。然后再传给header
          //触发父级更新User的方法
          this.$emit("refreshUser")
          this.getUser().then(res=>{
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user",JSON.stringify(res))
          })
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res){
      console.log(res)
      this.form.avatarUrl = res
    }
  }
}
</script>

<style scoped>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px ;
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
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>