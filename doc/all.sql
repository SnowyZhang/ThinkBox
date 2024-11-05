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