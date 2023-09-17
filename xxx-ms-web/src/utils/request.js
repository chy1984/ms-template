import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 10000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    // 如果有token，则在请求头中携带token
    if (store.getters.token) {
      // config.headers['Authorization'] = store.getters.tokenPrefix + getToken()
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    console.log(response.headers['refresh-token'])

    // 服务端提示token即将过期，则异步刷新token
    if (response.headers['refresh-token'] && !store.getters.refreshingTokenFlag) {
      store.dispatch('user/refreshToken').then(() => {
        console.log('已刷新token')
      })
    }

    // 成功则返回响应数据
    const res = response.data
    if (res.code === '000000') {
      return res
    }

    // 失败则展示错误提示
    Message({
      message: res.message || '系统繁忙，请稍后再试',
      type: 'error',
      duration: 5 * 1000
    })

    // 未登录、token不合法、token已过期，则弹框引导重新登录
    if (res.code === '900001') {
      MessageBox.confirm('你已被退出, 是否重新登录', '操作提示', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('user/resetToken').then(() => {
          location.reload()
        })
      })
    }

    return Promise.reject(new Error(res.message || '系统繁忙，请稍后再试'))
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
