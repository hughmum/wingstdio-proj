<template>
  <div style="height: 100%">
    <el-container style="height: 100%;">
      <el-aside :width="sideWidth+'px'" style="background-color: rgb(238, 241, 246);height: 100%;overflow: hidden ;box-shadow: 2px 0 6px rgb(0 21 41 /35%)">
        <Aside :is-collapse="isCollapse" :logo-test-show="logoTestShow" />
      </el-aside>

      <el-container>
        <el-header style=" border-bottom:1px solid #ccc; ">
          <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user"></Header>
        </el-header>

        <el-main>
           <router-view @refreshUser="getUser"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>


// import request from "@/utils/request";
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'HomeView',
  data(){
    const item = {
      date: '2016-05-02',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1518 弄'
    };
    return {

      msg:"沐",
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      sideWidth:200,
      logoTestShow:true,
      user: {}

    }
  },

  components:{
    Aside,
    Header
  },
  created() {
    this.getUser()
  },
  methods:{
    collapse() {//点击收缩按钮触发
        this.isCollapse=!this.isCollapse;
        if(this.isCollapse){
            this.sideWidth=64;
            this.collapseBtnClass='el-icon-s-unfold'
            this.logoTestShow=false
        }else{
          this.sideWidth=200;
          this.collapseBtnClass='el-icon-s-fold'
          this.logoTestShow=true
        }
    },
    getUser(){
      let account  = localStorage.getItem("userInfo")?JSON.parse(localStorage.getItem("userInfo")).account:""
      //从后台获取数据
     this.request.get("/user/account/"+account).then(res=>{
        this.user = res.data
     })
    },
  }
}
</script>
