drop table if exists `ebook`;
CREATE TABLE `ebook` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `name` VARCHAR(50) COMMENT '名称',
  `category1_id` BIGINT COMMENT '分类1',
  `category2_id` BIGINT COMMENT '分类2',
  `description` VARCHAR(200) COMMENT '描述',
  `cover` VARCHAR(200) COMMENT '封面',
  `doc_count` int COMMENT '文档数',
  `view_count` int COMMENT '阅读数',
  `vote_count` int COMMENT '点赞数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电子书';
INSERT INTO `ebook` (`id`, `name`, `description`)
VALUES
    (1, 'Java编程思想', '经典的 Java 编程书籍，涵盖了面向对象、并发编程、设计模式等重要概念。'),
    (2, '深入理解Java虚拟机', '从 JVM 的角度深入分析 Java 程序的运行机制，优化性能的必读书籍。'),
    (3, 'Effective Java', '一本帮助 Java 开发者写出更高质量代码的书籍，涵盖最佳实践和技巧。'),
    (4, 'Spring实战', '讲解 Spring 框架的使用，从基础到高级，帮助开发者提升 Java 企业级应用开发能力。'),
    (5, 'Java并发编程实践', '深入讲解 Java 并发编程的技巧与实践，适合有一定基础的开发者提升并发编程能力。');

CREATE TABLE `demo` (
                        `id` BIGINT NOT NULL COMMENT 'id',
                        `name` VARCHAR(50) COMMENT '名称',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试';
insert into `demo` (id,name) values (1,'测试');

CREATE TABLE `category` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `name` VARCHAR(50) COMMENT '名称',
  `parent_id` BIGINT COMMENT '父分类id',
  `priority` int COMMENT '优先级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类';
-- 新增技术相关分类
INSERT INTO `category`(id, name, parent_id, priority) VALUES (500, '后端', 000, 401);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (501, 'Java', 500, 402);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (502, 'Python', 500, 403);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (503, 'Node.js', 500, 404);

-- 新增框架相关分类
INSERT INTO `category`(id, name, parent_id, priority) VALUES (600, '框架', 000, 501);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (601, 'Django', 502, 502);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (602, 'SpringCloud', 101, 601);

-- 新增工具类分类
INSERT INTO `category`(id, name, parent_id, priority) VALUES (700, '工具', 000, 601);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (701, 'Git', 700, 701);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (702, 'Docker', 700, 702);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (703, 'Kubernetes', 700, 703);

-- 新增大数据分类
INSERT INTO `category`(id, name, parent_id, priority) VALUES (800, '大数据', 000, 801);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (801, 'Hadoop', 800, 802);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (802, 'Spark', 800, 803);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (803, 'Flink', 800, 804);

-- 新增人工智能分类
INSERT INTO `category`(id, name, parent_id, priority) VALUES (900, '人工智能', 000, 901);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (901, '机器学习', 900, 902);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (902, '深度学习', 900, 903);
INSERT INTO `category`(id, name, parent_id, priority) VALUES (903, '自然语言处理', 900, 904);

drop table if exists `doc`;
CREATE TABLE `doc` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `name` VARCHAR(50) COMMENT '名称',
  `ebook_id` BIGINT COMMENT '电子书id',
    `parent_id` BIGINT COMMENT '父分类id',
    `priority` int COMMENT '优先级',
  `view_count` int COMMENT '阅读数',
  `vote_count` int COMMENT '点赞数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档';

-- 新增技术相关文档
# INSERT INTO `doc`(id, name, ebook_id, parent_id, priority,view_count, vote_count) VALUES (1, 'Java8新特性', 1, 0, 1,0,0);
INSERT INTO `doc`(id, name, ebook_id, parent_id, priority,view_count, vote_count) VALUES (1, '文档1', 1, 0, 1,0,0);
INSERT INTO `doc`(id, name, ebook_id, parent_id, priority,view_count, vote_count) VALUES (2, '文档2', 2, 0, 2,0,0);
INSERT INTO `doc`(id, name, ebook_id, parent_id, priority,view_count, vote_count) VALUES (3, '文档1.1', 1, 1, 3,0,0);

drop table if exists `content`;
CREATE TABLE `content` (
  `id` BIGINT NOT NULL COMMENT '文档id',
  `content` mediumtext not null comment '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档内容';

# drop table if exists `users`;
# drop table if exists `roles`;
# drop table if exists `user_roles`;
# drop table if exists `comments`;
#
# CREATE TABLE roles (
#                        id BIGINT PRIMARY KEY PRIMARY KEY,  -- 角色 ID
#                        role_name VARCHAR(255) NOT NULL,  -- 角色名称（如 "admin", "user","guest"）
#                        description VARCHAR(255)  -- 角色描述
# );
# CREATE TABLE users (
#                        id BIGINT PRIMARY KEY,  -- 用户 ID
#                        username VARCHAR(255) NOT NULL,  -- 用户名
#                        email VARCHAR(255) UNIQUE,  -- 用户邮箱，确保唯一
#                        password VARCHAR(255),  -- 用户密码（仅适用于普通注册）
#                        profile_picture VARCHAR(255),  -- 用户头像 URL
#                        third_party_provider VARCHAR(255),  -- 第三方平台（如 GitHub、Google）
#                        third_party_id VARCHAR(255),  -- 第三方平台的用户唯一标识符
#                        third_party_access_token VARCHAR(255),  -- OAuth2 access token
#                        third_party_refresh_token VARCHAR(255),  -- OAuth2 refresh token
#                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
#                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 更新时间
#                        is_active BOOLEAN DEFAULT TRUE,  -- 是否激活
#                        is_deleted BOOLEAN DEFAULT FALSE,  -- 是否删除
#                        role_id BIGINT,  -- 用户角色（外键，关联角色表）
#                        FOREIGN KEY (role_id) REFERENCES roles(id)  -- 外键，关联角色表
# );
# CREATE TABLE user_roles (
#                             user_id BIGINT,  -- 用户 ID（外键，关联用户表）
#                             role_id BIGINT,  -- 角色 ID（外键，关联角色表）
#                             PRIMARY KEY (user_id, role_id),  -- 联合主键
#                             FOREIGN KEY (user_id) REFERENCES users(id),  -- 外键，关联用户表
#                             FOREIGN KEY (role_id) REFERENCES roles(id)  -- 外键，关联角色表
# );
# CREATE TABLE comments (
#                           id BIGINT PRIMARY KEY,  -- 评论 ID
#                           user_id BIGINT,  -- 评论作者（外键，关联 users 表）
#                           doc_id BIGINT,  -- 评论的文章 ID（外键，关联 posts 表）
#                           content TEXT NOT NULL,  -- 评论内容
#                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
#                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 更新时间
#                           FOREIGN KEY (user_id) REFERENCES users(id),  -- 关联用户表
#                           FOREIGN KEY (doc_id) REFERENCES doc(id)  -- 关联文档表
# );

drop table if exists `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `login_name` VARCHAR(50) COMMENT '登录名',
    `name` VARCHAR(50) COMMENT '名称',
    `password` VARCHAR(50) COMMENT '密码',
    primary key (`id`),
    unique key `login_name_unique` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

insert into `user` (id,login_name,name,password) values (1,'admin','管理员','123456');