

### 在线面试使用
* SessionQueue 目前仅支持单机，如果要扩展为集群，需要改为依赖mq。
* 使用方式：直接访问首页即可，见 IndexController

### sql 如下
```
CREATE TABLE `code_content` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `session_id` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `type` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `content` text CHARACTER SET utf8,
  `gmt_create` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sid` (`session_id`),
  KEY `idx_userid` (`user_id`),
  KEY `idx_create` (`gmt_create`)
) ENGINE=InnoDB AUTO_INCREMENT=643 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```