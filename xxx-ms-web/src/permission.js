import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/store/local-storage' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // 根据有无token判断是否已登录
  if (getToken()) {
    // 来自登录页面，则跳转到首页
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
      return
    }

    // 已挂载权限路由，则放行
    if (store.getters.permissionRoutes !== null) {
      next()
      return
    }

    // 未挂载权限路由：如果本地存储了权限菜单（刷新页面 vuex中的权限路由会丢失，但localStorage中的menus信息还在），则从菜单数据中重新挂载，否则重新获取用户信息
    let menus = store.getters.menus
    try {
      if (menus === null) {
        const { menuList } = await store.dispatch('user/getDetail')
        menus = menuList
      }

      const accessRoutes = await store.dispatch('permission/generateRoutes', menus)

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      // hack method to ensure that addRoutes is complete
      // set the replace: true, so the navigation will not leave a history record
      next({ ...to, replace: true })
    } catch (error) {
      // remove user info and go to login page to re-login
      await store.dispatch('user/resetUserInfo')
      Message.error(error || 'Has Error')
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }

    return
  }

  // 没token：如果访问的是白名单url则放行，否则跳转到登录页面
  if (whiteList.indexOf(to.path) !== -1) {
    next()
  } else {
    // 记录目标url，登录成功后直接跳转到此url
    next(`/login?redirect=${to.path}`)
    NProgress.done()
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
