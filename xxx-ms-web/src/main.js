import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control
import { permission } from '@/directive/permission'
import { checkPermission } from '@/directive/permission/permission'

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

Vue.use(ElementUI)
// 如果想特定语言版的 element-ui，locale引入，并按如下方式声明
// Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

Vue.prototype.checkPermission = checkPermission
Vue.use(permission)

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
