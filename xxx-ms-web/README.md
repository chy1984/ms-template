# xxx-ms-web

> 基于vue、element ui的管理后台模板，已集成权限控制

## Extra

基于 PanJiaChen [vue-admin-template](https://github.com/PanJiaChen/vue-admin-template) 的 permission-control 分支改造而成，

使用高版本node.js容易发生兼容性问题，推荐使用v16及其以下版本的node.js，推荐：[node.js v16.20.2](https://nodejs.org/download/release/v16.20.2/)

## Build Setup

```bash
# 克隆项目
git clone https://github.com/chy1984/ms-template.git

# 进入项目目录
cd xxx-ms-web

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装以来，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 [http://localhost:9000](http://localhost:9000)

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 其它

```bash
# 预览发布环境效果
npm run preview

# 预览发布环境效果 + 静态资源分析
npm run preview -- --report

# 代码格式检查
npm run lint

# 代码格式检查并自动修复
npm run lint -- --fix
```

