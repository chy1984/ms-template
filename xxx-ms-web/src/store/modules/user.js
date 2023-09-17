import { login, logout, getUserDetail, updatePassword, refreshToken } from '@/api/system/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import store from '@/store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'

const getDefaultState = () => {
  return {
    token: getToken(),
    tokenPrefix: '',
    refreshingTokenFlag: false,
    username: '',
    realName: '',
    menuList: [],
    operationList: []
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_TOKEN_PREFIX: (state, tokenPrefix) => {
    state.tokenPrefix = tokenPrefix
  },
  SET_REFRESHING_TOKEN_FLAG: (state, refreshingTokenFlag) => {
    state.refreshingTokenFlag = refreshingTokenFlag
  },
  SET_USERNAME: (state, username) => {
    state.username = username
  },
  SET_REAL_NAME: (state, realName) => {
    state.realName = realName
  },
  SET_MENU_LIST: (state, menuList) => {
    state.menuList = menuList
  },
  SET_OPERATION_LIST: (state, operationList) => {
    state.operationList = operationList
  }
}

const actions = {
  // 登录
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        commit('SET_TOKEN_PREFIX', data.tokenPrefix)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 登录后获取当前用户信息
  getDetail({ commit, state }) {
    return new Promise((resolve, reject) => {
      getUserDetail().then(response => {
        const { data } = response
        if (!data) {
          reject('验证失败，请重新登录')
        }

        const { username, realName, menuList, operationList } = data
        commit('SET_USERNAME', username)
        commit('SET_REAL_NAME', realName)
        commit('SET_MENU_LIST', menuList)
        commit('SET_OPERATION_LIST', operationList)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  async refreshToken({ commit }) {
    commit('SET_REFRESHING_TOKEN_FLAG', true)
    return new Promise((resolve, reject) => {
      refreshToken().then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        commit('SET_TOKEN_PREFIX', data.tokenPrefix)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      }).finally(() => {
        commit('SET_REFRESHING_TOKEN_FLAG', false)
      })
    })
  },

  // 更新密码
  updatePassword({ commit }, data) {
    return new Promise((resolve, reject) => {
      updatePassword(data).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 登出
  logout({ commit }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

