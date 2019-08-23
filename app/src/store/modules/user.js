import { login, logout, getInfo } from '@/api/user'
import { getAuthState, removeAuthCookie } from '@/utils/auth'

const user = {
  state: {
    authState: getAuthState(),
    name: '',
    avatar: '',
    roles: []
  },

  mutations: {
    SET_AUTH_STATE: (state, authState) => {
      state.authState = authState
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          // const data = response.data
          // console.log(data)
          // commit('SET_NAME', data.username)
          // commit('SET_ROLES', data.authorities)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const data = response.data
          if (data.authorities && data.authorities.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', data.authorities)
          } else {
            reject('getInfo: roles must be a non-null array !')
          }
          commit('SET_NAME', data.username)
          commit('SET_AVATAR', data.avatarUrl)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          // commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_AUTH_STATE', '')
          removeAuthCookie()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        // commit('SET_TOKEN', '')
        commit('SET_AUTH_STATE', '')
        removeAuthCookie()
        resolve()
      })
    }
  }
}

export default user
