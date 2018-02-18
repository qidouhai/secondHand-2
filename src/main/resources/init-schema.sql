Drop table  IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `head_url` varchar(60) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `salt` varchar(32) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `qq` varchar(10) DEFAULT NULL,
  `wechat` varchar(20) DEFAULT NULL,
  `alipay` varchar(20) DEFAULT NULL,
  `email` varchar(30) NOT NULL,
  `authenticate_id` int(11) DEFAULT NULL COMMENT '认证id',
  `address_id` int(11) DEFAULT NULL,
  `role` int(1) NOT NULL,
  `status` int(1) DEFAULT '1',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`) USING BTREE,
  UNIQUE KEY `unique_name` (`name`) USING BTREE,
  UNIQUE KEY `unique_phone` (`phone`) USING BTREE,
  UNIQUE KEY `unique_wechat` (`wechat`) USING BTREE,
  UNIQUE KEY `unique_qq` (`qq`) USING BTREE,
  UNIQUE KEY `unique_alipay` (`alipay`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;





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

Drop table if EXISTS `login_record`;
CREATE TABLE `login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `login_time` datetime NOT NULL,
  `ip` varchar(15) NOT NULL,
  `address` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

Drop table if EXISTS `user_authenticate`;
CREATE TABLE `user_authenticate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stu_id` varchar(11) NOT NULL COMMENT '学号',
  `name` varchar(10) NOT NULL COMMENT '真实姓名',
  `sex` varchar(1) NOT NULL COMMENT '性别',
  `school_name` varchar(15) NOT NULL COMMENT '高校名',
  `dept_name` varchar(20) NOT NULL COMMENT '院部名',
  `subject_name` varchar(20) NOT NULL COMMENT '专业名',
  `class_name` varchar(20) NOT NULL COMMENT '班级名',
  `register_year` int(4) NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_stuId` (`stu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;



Drop table if EXISTS `address`;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `area` varchar(20) NOT NULL COMMENT '区域',
  `hostel_id` varchar(20) NOT NULL COMMENT '宿舍栋号',
  `house_id` int(11) NOT NULL COMMENT '门牌号',
  `status` int(11) DEFAULT '1',
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

Drop table if EXISTS `suggest`;
create table `suggest`(
  id int auto_increment PRIMARY key,
  title VARCHAR(100) not null COMMENT '建议标题',
  content text not NULL COMMENT '建议内容',
  user_id int not null COMMENT '发起人',
  `status` int(1) DEFAULT '1',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '修改时间'
);

Drop table if EXISTS `Cart`;
CREATE TABLE `cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `goods_name` varchar(100) NOT NULL,
  `goods_image` varchar(40) NOT NULL,
  `goods_price` int(11) NOT NULL,
  `num` int(11) NOT NULL,
  `status` int(1) DEFAULT '1',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



