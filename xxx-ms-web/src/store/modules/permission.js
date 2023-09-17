import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param menuList
 * @param route
 */
function hasPermission(menuList, route) {
  if (route.meta && route.meta.resUrl) {
    const menu = menuList.find(x => x.resUrl === route.meta.resUrl)
    if (!menu) {
      return false
    }
    // 在路由配置中指定各个菜单的默认标题、icon、seq，如果接口有下发则使用接口下发的
    route.meta.title = menu.resName
    route.meta.seq = menu.seq
    if (menu.resIcon) {
      route.meta.icon = menu.resIcon
    }
  }

  // 如果没使用meta.resUrl配置所需权限，则认为不需要权限控制，直接展示
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
      res.push(tmp)
    }
  })

  return res.sort((res1, res2) => res2.seq - res1.seq)
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
