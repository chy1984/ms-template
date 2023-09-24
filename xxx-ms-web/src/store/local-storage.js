const tokenKey = 'xxx_ms_token'
const usernameKey = 'xxx_ms_username'
const realNameKey = 'xxx_ms_real_name'
const menusKey = 'xxx_ms_menus'
const operationResUrlsKey = 'xxx_ms_operation_res_urls'
const sidebarStatusKey = 'xxx_ms_sidebar_status'

export function getToken() {
  return localStorage.getItem(tokenKey)
}

export function setToken(token) {
  localStorage.setItem(tokenKey, token)
}

export function getUsername() {
  return localStorage.getItem(usernameKey)
}

export function setUsername(username) {
  localStorage.setItem(usernameKey, username)
}

export function getRealName() {
  return localStorage.getItem(realNameKey)
}

export function setRealName(realName) {
  localStorage.setItem(realNameKey, realName)
}

export function getMenus() {
  return JSON.parse(localStorage.getItem(menusKey))
}

export function setMenus(menus) {
  localStorage.setItem(menusKey, JSON.stringify(menus))
}

export function getOperationResUrls() {
  return localStorage.getItem(operationResUrlsKey)
}

export function setOperationResUrls(operationResUrls) {
  localStorage.setItem(operationResUrlsKey, operationResUrls)
}

export function getSidebarStatus() {
  return localStorage.getItem(sidebarStatusKey)
}

export function setSidebarStatus(sidebar) {
  localStorage.setItem(sidebarStatusKey, sidebar)
}

export function clear() {
  localStorage.clear()
}
