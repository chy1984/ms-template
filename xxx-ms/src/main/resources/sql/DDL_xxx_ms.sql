-- 以下为基本表
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `username`    varchar(50)  NOT NULL COMMENT '用户名',
    `real_name`   varchar(50)  NOT NULL COMMENT '真实姓名',
    `password`    varchar(255) NOT NULL COMMENT '密码',
    `tel`         varchar(20)  NOT NULL DEFAULT '' COMMENT '手机号',
    `email`       varchar(50)  NOT NULL DEFAULT '' COMMENT '邮箱',
    `status`      tinyint(4)   NOT NULL DEFAULT 0 COMMENT '用户状态：0正常，1禁用',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统用户表';

DROP TABLE IF EXISTS `t_sys_user_log`;
CREATE TABLE `t_sys_user_log`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `username`    varchar(50)  NOT NULL DEFAULT '' COMMENT '用户名',
    `real_name`   varchar(50)  NOT NULL DEFAULT '' COMMENT '真实姓名',
    `log_title`   varchar(100) NOT NULL DEFAULT '' COMMENT '日志标题',
    `req_ip`      varchar(20)  NOT NULL DEFAULT '' COMMENT '请求者ip',
    `user_agent`  varchar(255) NOT NULL DEFAULT '' COMMENT '用户代理',
    `req_param`   text COMMENT '请求参数',
    `resp_data`   text COMMENT '响应数据',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_username` (`username`),
    KEY `idx_real_name` (`real_name`),
    KEY `idx_log_title` (`log_title`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统用户日志表';

DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `role_name`   varchar(50) NOT NULL COMMENT '角色名称',
    `status`      tinyint(4)  NOT NULL DEFAULT 0 COMMENT '角色状态：0正常，1禁用',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_name` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统角色表';

DROP TABLE IF EXISTS `t_sys_resource`;
CREATE TABLE `t_sys_resource`
(
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `res_name`       varchar(100) NOT NULL COMMENT '资源名称',
    `res_type`       tinyint(4)   NOT NULL COMMENT '资源类型：1菜单，2操作/按钮，3接口',
    `res_icon`       varchar(255) NOT NULL DEFAULT '' COMMENT '资源图标',
    `res_url`        varchar(150) NOT NULL COMMENT '资源url',
    `res_req_method` varchar(20) NOT NULL DEFAULT '' COMMENT '资源请求方式：GET、POST、PUT、PATCH、DELETE、HEAD',
    `seq`            int(11)      NOT NULL DEFAULT 0 COMMENT '同级别排序号，值越小展示越靠前',
    `status`         tinyint(4)   NOT NULL DEFAULT 0 COMMENT '资源状态：0正常，1禁用',
    `parent_id`      bigint(20)   NOT NULL DEFAULT 0 COMMENT '上级资源id',
    `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统资源表';


-- 以下为关联关系表
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`     bigint(20) NOT NULL COMMENT '用户id',
    `role_id`     bigint(20) NOT NULL COMMENT '角色id',
    `create_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id_role_id` (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统用户角色关联表';

DROP TABLE IF EXISTS `t_sys_role_resource`;
CREATE TABLE `t_sys_role_resource`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `role_id`     bigint(20) NOT NULL COMMENT '角色id',
    `res_id`      bigint(20) NOT NULL COMMENT '资源id',
    `create_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_id_res_id` (`role_id`, `res_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统角色资源关联表';
