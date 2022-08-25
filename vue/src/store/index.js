import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";
Vue.use(Vuex)

// const store = new Vuex.Store({
//     state: {
//         currentPathName: ''
//     },
//     mutations: {
//         setPath (state) {
//             state.currentPathName = localStorage.getItem("currentPathName")
//         },
//         logout() {
//             //清空缓存
//             localStorage.removeItem("user")
//             localStorage.removeItem("menus")
//             router.push("/login")
//             //重置路由
//             resetRouter()
//         }
//     }
// })
//
// export default store

export default new Vuex.Store({
    state: {
        token: localStorage.getItem("token"),
        userInfo: JSON.parse(localStorage.getItem("userInfo")),
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
            localStorage.setItem("token", token)
        },
        SET_USERINFO: (state, userInfo) => {
            state.userInfo = userInfo
            localStorage.setItem("userInfo", JSON.stringify(userInfo))
        },
        REMOVE_INFO: (state) => {
            state.token = ''
            state.userInfo = {}
            localStorage.setItem("token", '')
            localStorage.setItem("userInfo", JSON.stringify(''))
        },

    },
    getters: {
        // get
        getUser: state => {
            return state.userInfo
        },
        getToken: state => {
            if (state.token == null) {
                return ''
            } else {
                return state.token
            }
        },

    },
    actions: {},
    modules: {}
})
