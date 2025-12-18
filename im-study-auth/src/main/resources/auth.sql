CREATE TABLE `user`
(
    `user_id`     bigint       NOT NULL COMMENT 'id',
    `user_name`   varchar(128) NOT NULL COMMENT '用户昵称',
    `password`    varchar(128)          DEFAULT NULL COMMENT '密码',
    `email`       varchar(128)          DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(32)  NOT NULL COMMENT '手机号',
    `avatar`      varchar(1024)         DEFAULT NULL COMMENT '用户头像',
    `signature`   varchar(1024)         DEFAULT NULL COMMENT '个性签名',
    `gender`      tinyint      NOT NULL DEFAULT '2' COMMENT '性别:0-男,1-女,2-保密',
    `status`      tinyint      NOT NULL DEFAULT '1' COMMENT '状态:1-正常,2-封号,3-注销',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY idx_user_name (`user_name`)
) COMMENT ='用户表'