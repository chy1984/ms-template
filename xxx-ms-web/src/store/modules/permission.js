import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param menuList
 * @param route
 */
function hasPermission(menuList, route) {
  if (route.meta && route.meta.resUrl) {
    for (const menu in menuList) {
      if (menu.resUrl === route.meta.resUrl) {
        // 如果是父级菜单，总是显示
        alert(menu.parentId === 0)
        if (menu.parentId === 0) {
          route.alwaysShow = true
          if (menu.resIcon) {
            route.meta.icon = menu.resIcon
          }
        }
        return true
      }
    }
    return false
  } else {
    return true
  }
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
