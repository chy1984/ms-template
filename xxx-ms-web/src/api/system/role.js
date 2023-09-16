import request from '@/utils/request'
import qs from 'qs'

export function pageRole(params) {
  return request({
    url: '/v1/system/roles/page',
    method: 'get',
    params
  })
}

export function addRole(data) {
  return request({
    url: '/v1/system/roles',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/v1/system/roles',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/v1/system/roles/${id}`,
    method: 'delete'
  })
}

export function listRoleResource(params) {
  return request({
    url: '/v1/system/roles/resources/list',
    method: 'get',
    params,
    paramsSerializer: x => qs.stringify(x, { indices: false })
  })
}

export function saveRoleResource(data) {
  return request({
    url: '/v1/system/roles/resources',
    method: 'post',
    data
  })
}

// 角色状态枚举，状态对应关系统一维护在此枚举中
export const roleStatusEnum = {
  normal: { status: 0, desc: '正常', tagType: 'success' },
  disabled: { status: 1, desc: '禁用', tagType: 'danger' }
}
