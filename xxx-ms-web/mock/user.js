const loginResponse = {
  code: '000000',
  message: '操作成功',
  data: {
    token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJlYWxOYW1lIjoi566h55CG5ZGYIiwiY3JlYXRlZCI6MTY5NTk3MTA3NjM2NCwiZXhwIjoxNjk1OTc0Njc2fQ.20tXEbbScKuiJtYyqRg4eWZ5Zhp4A2BWEY1JdTtbm_e0vZKk87jspwNDFZGYirhFTGJQYuLlcdPdluvogflTAw',
    tokenPrefix: 'Bearer '
  }
}

const logoutResponse = {
  code: '000000',
  message: '操作成功',
  data: null
}

const userDetailResponse = {
  code: '000000',
  message: '操作成功',
  data: {
    username: 'admin',
    realName: '管理员',
    menuList: [
      {
        id: 1,
        resName: '系统管理',
        resType: 1,
        resIcon: 'el-icon-setting',
        resUrl: '/system',
        resReqMethod: '',
        seq: 0,
        status: 0,
        parentId: 0
      },
      {
        id: 2,
        resName: '资源管理',
        resType: 1,
        resIcon: '',
        resUrl: '/system/resource',
        resReqMethod: '',
        seq: 0,
        status: 0,
        parentId: 1
      },
      {
        id: 11,
        resName: '角色管理',
        resType: 1,
        resIcon: '',
        resUrl: '/system/role',
        resReqMethod: '',
        seq: 1,
        status: 0,
        parentId: 1
      },
      {
        id: 23,
        resName: '用户管理',
        resType: 1,
        resIcon: '',
        resUrl: '/system/user',
        resReqMethod: '',
        seq: 2,
        status: 0,
        parentId: 1
      }
    ],
    operationList: [
      {
        id: 3,
        resName: '查看系统资源列表',
        resType: 2,
        resIcon: '',
        resUrl: '/system/resource/list',
        resReqMethod: '',
        seq: 0,
        status: 0,
        parentId: 2
      },
      {
        id: 5,
        resName: '添加系统资源',
        resType: 2,
        resIcon: '',
        resUrl: '/system/resource/add',
        resReqMethod: '',
        seq: 1,
        status: 0,
        parentId: 2
      },
      {
        id: 7,
        resName: '编辑系统资源',
        resType: 2,
        resIcon: '',
        resUrl: '/system/resource/edit',
        resReqMethod: '',
        seq: 2,
        status: 0,
        parentId: 2
      },
      {
        id: 9,
        resName: '删除系统资源',
        resType: 2,
        resIcon: '',
        resUrl: '/system/resources/delete',
        resReqMethod: '',
        seq: 3,
        status: 0,
        parentId: 2
      },
      {
        id: 12,
        resName: '查看系统角色列表',
        resType: 2,
        resIcon: '',
        resUrl: '/system/role/list',
        resReqMethod: '',
        seq: 0,
        status: 0,
        parentId: 11
      },
      {
        id: 14,
        resName: '添加系统角色',
        resType: 2,
        resIcon: '',
        resUrl: '/system/role/add',
        resReqMethod: '',
        seq: 1,
        status: 0,
        parentId: 11
      },
      {
        id: 16,
        resName: '编辑系统角色',
        resType: 2,
        resIcon: '',
        resUrl: '/system/role/edit',
        resReqMethod: '',
        seq: 2,
        status: 0,
        parentId: 11
      },
      {
        id: 18,
        resName: '删除系统角色',
        resType: 2,
        resIcon: '',
        resUrl: '/system/role/delete',
        resReqMethod: '',
        seq: 3,
        status: 0,
        parentId: 11
      },
      {
        id: 20,
        resName: '角色授权',
        resType: 2,
        resIcon: '',
        resUrl: '/system/role/grant-permission',
        resReqMethod: '',
        seq: 5,
        status: 0,
        parentId: 11
      },
      {
        id: 24,
        resName: '查看系统用户列表',
        resType: 2,
        resIcon: '',
        resUrl: '/system/user/list',
        resReqMethod: '',
        seq: 0,
        status: 0,
        parentId: 23
      },
      {
        id: 27,
        resName: '添加系统用户',
        resType: 2,
        resIcon: '',
        resUrl: '/system/user/add',
        resReqMethod: '',
        seq: 1,
        status: 0,
        parentId: 23
      },
      {
        id: 29,
        resName: '编辑系统用户',
        resType: 2,
        resIcon: '',
        resUrl: '/system/user/edit',
        resReqMethod: '',
        seq: 2,
        status: 0,
        parentId: 23
      },
      {
        id: 31,
        resName: '删除系统用户',
        resType: 2,
        resIcon: '',
        resUrl: '/system/user/delete',
        resReqMethod: '',
        seq: 3,
        status: 0,
        parentId: 23
      },
      {
        id: 33,
        resName: '重置系统用户密码',
        resType: 2,
        resIcon: '',
        resUrl: '/system/user/reset-password',
        resReqMethod: '',
        seq: 4,
        status: 0,
        parentId: 23
      }
    ]
  }
}

module.exports = [
  // user login
  {
    url: '/xxx-ms/v1/system/users/login',
    type: 'post',
    response: _ => loginResponse
  },

  // get user info
  {
    url: '/xxx-ms/v1/system/users/detail\.*',
    type: 'get',
    response: _ => userDetailResponse
  },

  // user logout
  {
    url: '/xxx-ms/v1/system/users/logout',
    type: 'put',
    response: _ => logoutResponse
  }
]
