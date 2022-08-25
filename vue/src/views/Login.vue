<template>
  <div class="wrapper">

    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.account"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
      <el-button type="text" size="mid" @click="handlePass" style="float: right; position: relative;bottom: 15px;">找回密码</el-button>

    </div>
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input v-model="form.code" autocomplete="off" style="width: 220px"></el-input>
          <el-button type="primary" size="small" class="ml-5" @click="getEmailCode">获取验证码</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="passwordBack">重置密码</el-button>
      </div>
    </el-dialog>
  </div>
</template>



<script>
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data(){
    return{
      user:{},
      dialogFormVisible: false,
      form: {}
    }
  },
  methods:{
    getEmailCode() {

      if(!this.form.email){
          this.$message.success("请输入邮箱账号")
      }
      if(!/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(this.form.email)) {
        this.$message.warning("请输入正确的邮箱账号")
      }
      console.log(this.form.email)
      //发送邮箱验证码
      this.request.get("/user/email/" + this.form.email).then(res=>{
        if(res.code==='200'){
          this.$message.success("发送成功");
        }
        else{
          this.$message.error(res.msg)
        }
      })
    },


    login(){

      this.request.post("/user/login",this.user).then(res=>{
        if(res.code==='200'){
          this.$store.commit("SET_TOKEN",res.data.token)
          this.$store.commit("SET_USERINFO",res.data.user)
          // localStorage.setItem("user",JSON.stringify(res.data))
          // localStorage.setItem("menus",JSON.stringify(res.data.menus))
          // //动态设置当前用户的路由
          // setRoutes()
          this.$router.replace("/")//跳转到主页面
          this.$message.success("登录成功")
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handlePass(){
        this.dialogFormVisible = true
    },
    passwordBack(){
      this.request.put("/user/reset",this.form).then(res=>{
        if(res.code === '200'){
          this.$message.success("重置密码成功，新密码为"+res.data)
          this.dialogFormVisible = false
        }else{
          this.$message.error(res.msg)
        }
      })

    }
  }
}
</script>

<style>
  .wrapper{
    /*窗口高度*/
    height: 100vh;
    /*渐变的背景色*/
    background-image: linear-gradient(to bottom right,#FC466B, #3F5EFB);
    overflow: hidden;
  }
</style>