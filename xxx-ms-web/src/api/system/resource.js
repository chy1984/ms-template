import request from '@/utils/request'
import qs from 'qs'

export function listResource(params) {
  return request({
    url: '/v1/system/resources/list',
    method: 'get',
    params,
    paramsSerializer: x => qs.stringify(x, { indices: false })
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

export function buildResourceTree(resourceList) {
  // 按parentId分组
  const parentResourceMap = resourceList.reduce((acc, cur) => {
    acc[cur.parentId] = acc[cur.parentId] || []
    acc[cur.parentId].push(cur)
    return acc
  }, {})
  // 从根资源（根菜单）开始，递归构建子资源树
  const resourceTree = resourceList.filter(resource => resource.parentId === 0)
  buildSubResourceTree(resourceTree, parentResourceMap)
  return resourceTree
}

function buildSubResourceTree(parentResourceList, parentResourceMap) {
  parentResourceList.forEach(parentResource => {
    const subResourceList = parentResourceMap[parentResource.id]
    if (subResourceList) {
      subResourceList.sort((res1, res2) => res1.seq - res2.seq)
      parentResource.children = subResourceList
      buildSubResourceTree(subResourceList, parentResourceMap)
    }
  })
}
