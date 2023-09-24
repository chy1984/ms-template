import store from '@/store'

// 校验用户是否有指定resUrl的权限
export function checkPermission(value) {
  if (value) {
    const operationResUrls = store.getters && store.getters.operationResUrls
    return operationResUrls.includes(value)
  }
  return false
}
