use db_wiki;

CREATE TABLE `t_chart` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `value` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图表数据的demo';



INSERT INTO `t_chart` VALUES (1,'Direct',335),(2,'Email',310),(3,'Ad Networks',234),(4,'Video Ads',135),(5,'Search Engines',1548),(6,'Web',1000);