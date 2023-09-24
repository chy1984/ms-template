import { constantRoutes, asyncRoutes } from '@/router'

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
      if (tmp.children) {
        tmp.children.sort((res1, res2) => res1.meta.seq - res2.meta.seq)
        // 只有一个子菜单也显示父菜单
        route.alwaysShow = true
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  // null表示未挂载权限路由，[]表示已挂载但用户没有任何动态路由的权限
  permissionRoutes: null,
  routes: []
}

const mutations = {
  SET_ROUTES: (state, permissionRoutes) => {
    state.permissionRoutes = permissionRoutes
    state.routes = constantRoutes.concat(permissionRoutes)
  }
}

const actions = {
  generateRoutes({ commit }, menus) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(asyncRoutes, menus)
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
