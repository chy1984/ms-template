import request from '@/utils/request'
import qs from 'qs'

export function listResource(params) {
  // todo: 数组url拼接友好显示
  params = qs.stringify(params, { arrayFormat: 'repeat', encode: false })
  return request({
    url: '/v1/system/resources/list',
    method: 'get',
    params
  })
}

export function addResource(data) {
  return request({
    url: '/v1/system/resources',
    method: 'post',
    data
  })
}

export function updateResource(data) {
  return request({
    url: '/v1/system/resources',
    method: 'put',
    data
  })
}

export function deleteResource(id) {
  return request({
    url: `/v1/system/resources/${id}`,
    method: 'delete'
  })
}

export const resReqMethodList = ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'HEAD']

export const resStatusEnum = {
  normal: { status: 0, desc: '正常', tagType: 'success' },
  disabled: { status: 1, desc: '禁用', tagType: 'danger' }
}

export const resTypeEnum = {
  menu: { resType: 1, desc: '菜单' },
  operation: { resType: 2, desc: '操作/按钮' },
  interface: { resType: 3, desc: '接口' }
}
