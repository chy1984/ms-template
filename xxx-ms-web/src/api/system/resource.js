import request from '@/utils/request'
import qs from 'qs'

export function listResource(params) {
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
