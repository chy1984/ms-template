// 按钮/操作的resUrl列表
export const operationResUrls = {
  system: {
    resource: {
      list: '/system/resource/list',
      add: '/system/resource/add',
      edit: '/system/resource/edit',
      delete: '/system/resources/delete'
    },
    role: {
      list: '/system/role/list',
      add: '/system/role/add',
      edit: '/system/role/edit',
      delete: '/system/role/delete',
      grantPermission: '/system/role/grant-permission'
    },
    user: {
      list: '/system/user/list',
      add: '/system/user/add',
      edit: '/system/user/edit',
      delete: '/system/user/delete',
      resetPassword: '/system/user/reset-password'
    }
  }
}
