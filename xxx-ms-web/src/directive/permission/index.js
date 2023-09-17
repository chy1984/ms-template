import store from '@/store'

// 自定义的 v-permission 权限校验组件
export const permission = function(Vue) {
  Vue.directive('permission', {
    inserted(el, binding) {
      checkPermission(el, binding)
    },
    update(el, binding) {
      checkPermission(el, binding)
    }
  })
}

function checkPermission(el, binding) {
  const { value } = binding
  const operationList = store.getters && store.getters.operationList
  if (value) {
    const resUrl = value
    const hasPermission = operationList.some(x => x.resUrl === resUrl)
    if (!hasPermission) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error(`没有相关操作权限：${value}`)
  }
}

