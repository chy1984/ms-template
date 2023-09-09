import request from '@/utils/request'

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
    params
  })
}

export function saveRoleResource(data) {
  return request({
    url: '/v1/system/roles/resources',
    method: 'post',
    data
  })
}
