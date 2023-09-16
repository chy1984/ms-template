const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  tokenPrefix: state => state.user.tokenPrefix,
  refreshingToken: state => state.user.refreshingToken,
  username: state => state.user.username,
  realName: state => state.user.realName,
  menuList: state => state.user.menuList,
  operationList: state => state.user.operationList,
  permission_routes: state => state.permission.routes
}
export default getters
