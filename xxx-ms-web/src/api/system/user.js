import request from '@/utils/request'

// post请求的请求体参数固定为data，不要随意改参数名
export function login(data) {
  return request({
    url: '/v1/system/users/login',
    method: 'post',
    data
  })
}

export function getUserDetail() {
  return request({
    url: '/v1/system/users/detail',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/v1/system/users/logout',
    method: 'put'
  })
}

export function pageUser(params) {
  return request({
    url: '/v1/system/users/page',
    method: 'get',
    params
  })
}

export function addUser(data) {
  return request({
    url: '/v1/system/users',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/v1/system/users',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/v1/system/users/${id}`,
    method: 'delete'
  })
}

export function updatePassword(data) {
  return request({
    url: '/v1/system/users/password',
    method: 'put',
    data
  })
}

export function resetPassword(id) {
  return request({
    url: `/v1/system/users/${id}/password/reset`,
    method: 'put'
  })
}

// 用户状态枚举，状态对应关系统一维护在此枚举中
export const userStatusEnum = {
  normal: { status: 0, desc: '正常', tagType: 'success' },
  disabled: { status: 1, desc: '禁用', tagType: 'danger' }
}
