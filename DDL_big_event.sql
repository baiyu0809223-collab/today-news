/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = article   */
/******************************************/
CREATE TABLE `article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `category_id` int DEFAULT NULL,
  `create_user` int NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `cover_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '已发布/草稿/待审核',
  `views` int DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_article_category` (`category_id`) USING BTREE,
  KEY `fk_article_user` (`create_user`) USING BTREE,
  CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_article_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = category   */
/******************************************/
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_user` int NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_category` (`create_user`) USING BTREE,
  CONSTRAINT `fk_category` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = comment   */
/******************************************/
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL COMMENT '关联的文章ID',
  `user_id` int NOT NULL COMMENT '评论用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论用户名',
  `user_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户头像URL',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `parent_id` int DEFAULT NULL COMMENT '父评论ID，NULL表示一级评论',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'APPROVED' COMMENT '状态: PENDING, APPROVED, REJECTED, DELETED',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `is_top` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否置顶',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_article_id` (`article_id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='评论表'
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = comment_like   */
/******************************************/
CREATE TABLE `comment_like` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comment_id` int NOT NULL COMMENT '评论ID',
  `user_id` int NOT NULL COMMENT '点赞用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_comment_user` (`comment_id`,`user_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  CONSTRAINT `fk_comment_like_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='评论点赞表'
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = examine   */
/******************************************/
CREATE TABLE `examine` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `category_id` int DEFAULT NULL,
  `create_user` int NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `cover_img` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `state` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `target` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `aid` int DEFAULT NULL COMMENT '用户删除书籍时，需要进行审核，这是要删除的书的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = user   */
/******************************************/
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户邮箱',
  `user_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户头像',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新信息时间',
  `role` int unsigned NOT NULL DEFAULT '0' COMMENT '0是用户，1为管理员',
  `statue` int NOT NULL DEFAULT '0' COMMENT '0:正常用户，1：修改中用户，2：禁用用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = user_detail   */
/******************************************/
CREATE TABLE `user_detail` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int NOT NULL COMMENT '关联 user.id',
  `avatar` varchar(255) DEFAULT NULL COMMENT '名片头像',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `username` varchar(50) NOT NULL COMMENT '展示用户名',
  `bio` varchar(200) DEFAULT NULL COMMENT '个人简介',
  `gender` tinyint DEFAULT '0' COMMENT '性别：0-未知 1-男 2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `location` varchar(100) DEFAULT NULL COMMENT '所在地',
  `website` varchar(255) DEFAULT NULL COMMENT '个人主页',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `follow_count` int DEFAULT '0' COMMENT '关注数',
  `fan_count` int DEFAULT '0' COMMENT '粉丝数',
  `like_count` int DEFAULT '0' COMMENT '获赞数',
  `last_active_time` datetime DEFAULT NULL COMMENT '最近活跃时间',
  `is_verified` tinyint DEFAULT '0' COMMENT '是否认证：0-否 1-是',
  `status` tinyint DEFAULT '1' COMMENT '用户状态：1-正常 0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  CONSTRAINT `fk_user_detail_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户个人名片扩展表'
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = user_update   */
/******************************************/
CREATE TABLE `user_update` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '修改表id',
  `userid` int DEFAULT NULL COMMENT '用户id',
  `user_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改的图片',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改的名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = video   */
/******************************************/
CREATE TABLE `video` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '视频id',
  `title` varchar(255) NOT NULL COMMENT '视频标题',
  `description` varchar(500) DEFAULT NULL COMMENT '视频描述',
  `video_url` varchar(255) NOT NULL COMMENT '视频播放地址',
  `cover_img` varchar(255) NOT NULL COMMENT '视频封面',
  `create_user` int NOT NULL COMMENT '上传用户',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `state` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/APPROVED/REJECTED',
  `like_count` int NOT NULL DEFAULT '0',
  `comment_count` int NOT NULL DEFAULT '0',
  `view_count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_video_user` (`create_user`),
  CONSTRAINT `fk_video_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='短视频表'
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = video_comment   */
/******************************************/
CREATE TABLE `video_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int NOT NULL,
  `user_id` int NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `user_pic` varchar(255) DEFAULT NULL,
  `content` text NOT NULL,
  `parent_id` int DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `state` varchar(20) NOT NULL DEFAULT 'APPROVED',
  `like_count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_video_id` (`video_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频评论表'
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = video_examine   */
/******************************************/
CREATE TABLE `video_examine` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int NOT NULL,
  `create_user` int NOT NULL,
  `state` varchar(20) NOT NULL COMMENT 'PENDING/APPROVED/REJECTED',
  `reason` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频审核表'
;

/******************************************/
/*   DatabaseName = big_event   */
/*   TableName = video_like   */
/******************************************/
CREATE TABLE `video_like` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int NOT NULL,
  `user_id` int NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_video_user` (`video_id`,`user_id`),
  CONSTRAINT `fk_video_like_video` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频点赞表'
;
