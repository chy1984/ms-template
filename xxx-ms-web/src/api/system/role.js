import request from '@/utils/request'

export function pageRole(params) {
  return request({
    url: '/v1/system/roles/page',
    method: 'get',
    params
  })
}
