import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
  {
    path: '/',
    name: 'Manage',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('../views/User.vue')
      },
      {
        path: '/person',
        name: 'Person',
        component: () => import('../views/Person.vue')
      },
      {
        path: '/borrow',
        name: 'Borrow',
        component: () => import('../views/Borrow.vue')
      },
      {
        path: '/examine',
        name: 'Examine',
        component: () => import('../views/Examine.vue')
      },
      {
        path: '/class',
        name: 'Class',
        component: () => import('../views/Class.vue')
      },
      {
        path: '/role',
        name: 'Role',
        component: () => import('../views/Role.vue')
      },
      {
        path: '/menu',
        name: 'Menu',
        component: () => import('../views/Menu.vue')
      },
      {
        path: '/device',
        name: 'Device',
        component: () => import('../views/Device.vue')
      },
      {
        path: '/borrow/myborrow',
        name: 'MyBorrow',
        component: () => import('../views/MyBorrow.vue')
      },
    ]
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router




/*

//重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}


//刷新页面会导致路由重置
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if(storeMenus){
    //获取当前路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v=>v.name)
    if(!currentRouteNames.includes('Manage')){
      //当没有mange的时候再去创建这一大坨路由
      const manageRoute = {path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children:[
          { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
          { path: 'borrow/myborrow', name: '个人借用记录', component: () => import('../views/MyBorrow.vue')},
        ]}
      const menus = JSON.parse(storeMenus)
      menus.forEach(item=>{
        if(item.path){
          let itemMenu = {path: item.path.replace("/",""), name: item.name, component: () => import('../views/'+item.pagePath+'.vue')}
          manageRoute.children.push(itemMenu)
        }
        else if(item.children.length){
          item.children.forEach(item=>{
            if(item.path){
              let itemMenu = {path: item.path.replace("/",""), name: item.name, component: () => import('../views/'+item.pagePath+'.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      router.addRoute(manageRoute)
    }
  }
}

 setRoutes()

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  //未找到路由的情况
  if(!to.matched.length){
    //拿到当前用户的menus
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus) {
      // next("/404") // 放行路由
      next()
    }else{
      //跳回登录
      // next("/login")
      next()
    }

  }
  //其他情况方形
  next()

})

export default router
*/
