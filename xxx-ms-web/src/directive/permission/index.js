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
  if (value) {
    const operationResUrls = store.getters && store.getters.operationResUrls
    if (!operationResUrls.includes(value)) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error('v-permission属性值缺失')
  }
}

