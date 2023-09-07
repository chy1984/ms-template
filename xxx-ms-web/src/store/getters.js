const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  username: state => state.user.username,
  realName: state => state.user.realName,
  menuList: state => state.user.menuList,
  operationList: state => state.user.operationList,
  permission_routes: state => state.permission.routes
}
export default getters