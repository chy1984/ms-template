import store from '@/store'

// 校验用户是否有指定resUrl的权限
export function checkPermission(value) {
  if (value) {
    const operationList = store.getters && store.getters.operationList
    return operationList.some(x => x.resUrl === value)
  }
  return false
}
