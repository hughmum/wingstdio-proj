<template>
  <el-menu :default-openeds="opens" style="height: 100% "
           background-color="rgb(48,65,86)"
           :default-active="active"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px">
      <b style="color: white" v-show="logoTestShow">设备管理系统</b>
    </div>
    <div v-for="item in menuList " :key="item.menu.id">

        <div v-if="item.menu.path">
          <el-menu-item :index="item.menu.path">
            <i :class="item.menu.icon"></i>
            <span slot="title">{{ item.menu.name }}</span>
          </el-menu-item>
        </div>
        <div v-else>
          <el-submenu :index="item.menu.id" :key="item.menu.id">
            <template slot="title">
              <i :class="item.menu.icon"></i>
              <span>{{item.menu.name}}</span>
            </template>
              <el-menu-item :index="it.path" v-for="it in item.subMenu"
                            :key="it.id"
                            @click="saveActive(it.path)">
                <i :class="it.icon"></i>
                <span >{{it.name}}</span>
              </el-menu-item>
          </el-submenu>
        </div>


<!--            <div v-if="item.path">-->
<!--          <el-menu-item :index="item.path">-->
<!--            <template slot="title">-->
<!--              <i :class="item.icon"></i>-->
<!--              <span slot="title">{{item.menu.name}}</span>-->
<!--            </template>-->
<!--          </el-menu-item>-->
<!--        </div>-->
<!--        <div v-else>-->
<!--          <el-submenu :index="item.id+''">-->
<!--            <template slot="title">-->
<!--              <i :class="item.icon"></i>-->
<!--              <span slot="title">{{item.name}}</span>-->
<!--            </template>-->
<!--            <div  v-for="subItem in item.children" :key="subItem.id">-->
<!--              <el-menu-item :index="subItem.path">-->
<!--                <i :class="subItem.icon"></i>-->
<!--                <span slot="title">{{subItem.name}}</span>-->
<!--              </el-menu-item>-->
<!--            </div>-->
<!--          </el-submenu>-->
<!--        </div>-->
<!--    </div>-->
  </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTestShow: Boolean,
  },
  data() {
    return {
      menuList: [],
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")):[],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id +''):[],//默认全部展开
      active: ''
    }
  },
  methods: {
      getMenuList(){
        var account
        if(this.$store.getters.getUser == null || this.$store.getters == ''){
          account = ''
        }else{
          account = this.$store.getters.getUser.account
        }
          this.request.get("/user/getMenuList?account="+account,
              {
                  headers: {
                    "Authorization": this.$store.getters.getToken
                  }
              }).then(res=>{
                this.menuList = res.data
          })
      },
    saveActive(val){
        this.active = val;
        localStorage.setItem("active",val)
    }
  },
  created() {
    this.getMenuList();
    this.active = localStorage.getItem("active")
  }
}
</script>

<style scoped>
/*解决收缩菜单文字不消失问题*/
.el-menu--collapse span{
  visibility: hidden;
}
</style>