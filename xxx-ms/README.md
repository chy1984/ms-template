# xxx-ms

## 使用步骤
1. 新建数据库 xxx-ms，字符集 utf8mb4，排序方式 utf8mb4_general_ci
2. 依次执行 resources/sql 下的DDL、DML
3. 修改yml中的db、redis连接配置
4. 启动服务

## 接口文档
knife4j官方文档：[https://doc.xiaominfo.com](https://doc.xiaominfo.com)

后端接口文档：http://host:port/xxx-ms/doc.html
本地示例：[http://localhost:9500/xxx-ms/doc.html](http://localhost:9500/xxx-ms/doc.html)

调用登录接口后 全局设置token：

![api文档token配置](https://github.com/chy1984/ms-template/blob/master/document/image/api-document-token.png)
