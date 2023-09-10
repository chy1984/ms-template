import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param menuList
 * @param route
 */
function hasPermission(menuList, route) {
  if (route.meta && route.meta.resUrl) {
    return menuList.some(menu => menu.resUrl === route.meta.resUrl)
  }
  return true
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param menuList
 */
export function filterAsyncRoutes(routes, menuList) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(menuList, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, menuList)
      }
      // 即使只有一个子菜单也总是显示父菜单
      if (tmp.children) {
        route.alwaysShow = true
      }
      // 在路由配置中指定各个菜单的默认icon，如果接口有下发则使用接口下发的icon
      if (tmp.resIcon) {
        route.meta.icon = tmp.resIcon
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, menuList) {
    return new Promise(resolve => {
      let accessedRoutes = filterAsyncRoutes(asyncRoutes, menuList)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
