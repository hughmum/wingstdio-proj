<template>
<div style="font-size: 12px; line-height: 60px;display: flex">
  <div style="flex:1;font-size:20px">
    <span :class="collapseBtnClass" style="cursor:pointer" @click="collapse"></span>
    <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
      <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <el-dropdown style="width: 110px;cursor:pointer">
    <div style="display: inline-block">
      <img :src="user.avatarUrl" alt="" style="width: 30px;height:30px;border-radius: 50% ;
      position: relative; top: 10px;right: 5px">
      <span>{{user.username}}</span><i class="el-icon-arrow-down" style="margin-left:5px"></i>
    </div>
    <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center" >
      <el-dropdown-item style="font-size: 14px;padding: 5px 0">
        <router-link to="/person">个人信息</router-link>
      </el-dropdown-item>
      <el-dropdown-item style="font-size: 14px;padding: 5px 0">
        <span  style="text-decoration: none" @click="logout">退出</span>
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>

</div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    user: Object
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　　//需要监听的数据
    }
  },
  watch: {
    currentPathName (newVal, oldVal) {
      console.log(newVal)
    }
  },
  data(){
    return {
      // paths: []
    }

  },
  methods:{
    logout() {
      //清空缓存
      this.$store.commit("REMOVE_INFO")
      this.$router.push("/login")
      this.$message.success("退出成功")
    },
    collapse(){
      // this.$parent.$parent.$parent.$parent.collapse();//使用父组件进行通信调用
    this.$emit("asideCollapse")
    },
    created(){
    }
  }
}
</script>

<style scoped>

</style>