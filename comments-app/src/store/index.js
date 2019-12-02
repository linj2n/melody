import Vue from 'vue'
import Vuex from 'vuex'
import { isAuthenticated } from '@/utils/auth'
import { getAccountInfo, logout } from '@/api/user'
Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    isAuthenticated: isAuthenticated(),
    name: '',
    id: null,
    avatarUrl: '',
    siteUrl: '#'
  },

  mutations: {
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR_URL: (state, avatarUrl) => {
      state.avatarUrl = avatarUrl
    },
    SET_SITE_URL: (state, siteUrl) => {
      state.siteUrl = siteUrl
    },
    SET_ID: (state, id) => {
      state.id = id
    }
  },

  actions: {

    // 获取用户信息
    GetInfo ({ commit, state }) {
      return new Promise((resolve, reject) => {
        if (state.isAuthenticated) {
          getAccountInfo().then(response => {
            if (response.code === 20000) {
              const data = response.data
              commit('SET_ID', data.id)
              commit('SET_NAME', data.username)
              commit('SET_AVATAR_URL', data.avatarUrl)
              commit('SET_SITE_URL', data.link)
              resolve(response)
            } else {
              this.$message.error(response.message)
            }
          })
        }
      })
    },

    // 登出
    LogOut ({ commit }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          commit('SET_ID', null)
          commit('SET_NAME', '')
          commit('SET_AVATAR_URL', '')
          commit('SET_SITE_URL', '')
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
})
