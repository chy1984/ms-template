# xxx-ms-web
高版本 node.js 容易出现兼容性问题，建议使用 v16 及其以下版本的 node.js，推荐：[https://nodejs.org/download/release/v16.20.2](https://nodejs.org/download/release/v16.20.2)

## 构建
```bash
# 克隆项目
git clone https://github.com/chy1984/ms-template.git

# 进入项目目录
cd xxx-ms-web

# 安装依赖，npm install 太慢，直接用 cnpm 容易发生各种诡异的bug，推荐以下方式
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```
浏览器访问 [http://localhost:9000](http://localhost:9000)

内置的2个账号密码
- admin 1234
- visitor 1234

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
