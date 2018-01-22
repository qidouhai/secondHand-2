Drop table  IF EXISTS `user`;
create table `user`(
  id int auto_increment PRIMARY key,
  name varchar(20) not null ,
  head_url varchar(60),
  password varchar(32) not null,
  salt VARCHAR(32) not null,
  stu_id varchar(11) UNIQUE ,
  dept_name varchar(30) ,
  subject_name varchar(20) ,
  phone varchar(11) UNIQUE ,
  qq varchar(10) UNIQUE,
  wechat varchar(20) UNIQUE,
  alipay varchar(20) UNIQUE,
  email VARCHAR(30) UNIQUE not NULL ,
  status int(1)  DEFAULT 1,
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '修改时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

Drop table if EXISTS `goods`;
create TABLE `goods`(
  id int AUTO_INCREMENT PRIMARY KEY ,
  goods_name varchar(20) not NULL ,
  category_id int not null,
  sub_category_id int not null,
  price int not null ,
  images varchar(250) not null,
  detail TEXT not NULL ,
  inventory int not null COMMENT '库存',
  bargain int DEFAULT 0 COMMENT '议价，1为支持，0为不支持',
  view_num int DEFAULT 0 COMMENT '浏览数',
  collect_num int DEFAULT 0 COMMENT '收藏数',
  hot_num int DEFAULT 0 COMMENT '点赞数',
  owner_id int not null,
  status int(1)  DEFAULT 1 COMMENT '0 代表删除，1代表发布，2代表仅仅保存',
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '修改时间'
)ENGINE=InnoDB DEFAULT CHARSET = utf8;

Drop table if EXISTS `category`;
create TABLE `category`(
  id int AUTO_INCREMENT PRIMARY KEY PRIMARY KEY ,
  category_name VARCHAR(20) not NULL COMMENT '类目名称',
  parent_id bigint(20) DEFAULT NULL COMMENT '父类目ID=0时，代表的是一级的类目',
  status int(1) DEFAULT '1' COMMENT '状态。可选值:1(正常),2(删除)',
  sort_order int(4) DEFAULT NULL COMMENT '排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数',
  is_parent tinyint(1) DEFAULT '1' COMMENT '该类目是否为父类目，1为true，0为false',
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '修改时间',
  KEY `parent_id` (`parent_id`,`status`) USING BTREE,
  KEY `sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品类目';

Drop table if EXISTS `content`;
create TABLE `content`(
  id int AUTO_INCREMENT PRIMARY KEY  ,
  title varchar(100),
  image varchar(50),
  content text,
  entity_type int(1) not null,
  author_id int,
  status int(1)  DEFAULT 1,
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

Drop table if EXISTS `comment`;
CREATE TABLE `comment`(
  id int auto_increment primary key ,
  content TEXT not NULL COMMENT '回复内容',
  from_id  int NOT NULL COMMENT '发送人id',
  to_id  int NOT NULL COMMENT '接收人id',
  parent_id int not null COMMENT '被回复的留言id, 0代表是一级留言，1代表是二级留言，回复别人的留言',
  is_parent int not null COMMENT '此留言是否有其他人回复，1代表有，2代表没有',
  sub_num int not null COMMENT '子回复的个数',
  entity_id int not null ,
  entity_type int not null,
  status int(1) DEFAULT 1,
  created datetime DEFAULT NULL COMMENT '创建时间',
  updated datetime DEFAULT NULL COMMENT '修改时间'
);

CREATE TABLE `login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `login_time` datetime NOT NULL,
  `ip` varchar(15) NOT NULL,
  `address` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

