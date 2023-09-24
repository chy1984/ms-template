import { login, logout, getUserDetail, updatePassword, refreshToken } from '@/api/system/user'
import {
  getToken, setToken, getUsername, setUsername, getRealName, setRealName,
  getMenus, setMenus, getOperationResUrls, setOperationResUrls, clear
} from '@/store/local-storage'
import { resetRouter } from '@/router'

const getState = () => {
  return {
    token: getToken(),
    username: getUsername(),
    realName: getRealName(),
    menus: getMenus(),
    operationResUrls: getOperationResUrls() || [],
    refreshingTokenFlag: false
  }
}

const state = getState()

const mutations = {
  RESET_STATE: (state) => {
    clear()
    Object.assign(state, getState())
  },
  SET_TOKEN: (state, token) => {
    setToken(token)
    state.token = token
  },
  SET_USERNAME: (state, username) => {
    setUsername(username)
    state.username = username
  },
  SET_REAL_NAME: (state, realName) => {
    setRealName(realName)
    state.realName = realName
  },
  SET_MENUS: (state, menus) => {
    setMenus(menus)
    state.menus = menus
  },
  SET_OPERATION_RES_URLS: (state, operationResUrls) => {
    setOperationResUrls(operationResUrls)
    state.operationResUrls = operationResUrls
  },
  SET_REFRESHING_TOKEN_FLAG: (state, refreshingTokenFlag) => {
    state.refreshingTokenFlag = refreshingTokenFlag
  }
}

const actions = {
  // 登录
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { tokenPrefix, token } = response.data
        commit('SET_TOKEN', tokenPrefix + token)
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
        const operationResUrls = operationList.map(operation => operation.resUrl)
        commit('SET_USERNAME', username)
        commit('SET_REAL_NAME', realName)
        commit('SET_MENUS', menuList)
        commit('SET_OPERATION_RES_URLS', operationResUrls)
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
        const { tokenPrefix, token } = response.data
        commit('SET_TOKEN', tokenPrefix + token)
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
        commit('RESET_STATE')
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 重置用户信息
  resetUserInfo({ commit }) {
    return new Promise(resolve => {
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

