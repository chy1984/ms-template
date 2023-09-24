const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  username: state => state.user.username,
  realName: state => state.user.realName,
  menus: state => state.user.menus,
  operationResUrls: state => state.user.operationResUrls,
  refreshingTokenFlag: state => state.user.refreshingTokenFlag,
  permissionRoutes: state => state.permission.permissionRoutes,
  routes: state => state.permission.routes
}
export default getters
