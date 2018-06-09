/*
 Navicat Premium Data Transfer

 Source Server         : gandoph
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : mall

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 09/06/2018 10:07:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mmall_cart
-- ----------------------------
DROP TABLE IF EXISTS `mmall_cart`;
CREATE TABLE `mmall_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `user_id` int(11) NOT NULL COMMENT '用户id，对应mmall_user表的主键',
  `product_id` int(11) NOT NULL COMMENT '商品id，对应mmall_product表的主键',
  `quantity` int(11) DEFAULT NULL COMMENT '商品数量',
  `checked` tinyint(1) DEFAULT '1' COMMENT '是否选择1-已选择，0-未勾选',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mmall_category
-- ----------------------------
DROP TABLE IF EXISTS `mmall_category`;
CREATE TABLE `mmall_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `parent_id` int(11) DEFAULT NULL COMMENT '父类别id，当id为0时说明是根节点，一级类别',
  `name` varchar(50) NOT NULL COMMENT '类别名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '类别状态1-正常，2-已废弃',
  `sort_order` int(4) DEFAULT NULL COMMENT '排序编号，同类展示顺序，数值相等则自然排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mmall_order
-- ----------------------------
DROP TABLE IF EXISTS `mmall_order`;
CREATE TABLE `mmall_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `user_id` int(11) NOT NULL COMMENT '用户id，对应mmall_user表的主键',
  `shipping_id` int(11) DEFAULT NULL COMMENT '运送编号',
  `payment` decimal(20,2) NOT NULL COMMENT '实际支付金额，单位元，保留两位有效数字',
  `payment_type` int(4) DEFAULT NULL COMMENT '支付类型，1-在线支付，2-货到付款',
  `postage` int(10) DEFAULT NULL COMMENT '运费取整，单位是元',
  `status` int(10) DEFAULT NULL COMMENT '订单状态0-已取消，10-未付款，20-已付款，30-已发货，40-交易成功，50-交易关闭',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `send_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mmall_order_item
-- ----------------------------
DROP TABLE IF EXISTS `mmall_order_item`;
CREATE TABLE `mmall_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单子表id',
  `user_id` int(11) NOT NULL COMMENT '用户id，对应mmall_user表的主键',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片地址',
  `current_unit_price` decimal(20,2) DEFAULT NULL COMMENT '生成订单时的商品单价，单位是元，保留两位有效数字',
  `quantity` int(11) DEFAULT NULL COMMENT '商品数量',
  `total_price` decimal(20,2) DEFAULT NULL COMMENT '商品总价，单位是元，保留两位有效数字',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE,
  UNIQUE KEY `order_no_user_id_index` (`user_id`,`order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mmall_pay_info
-- ----------------------------
DROP TABLE IF EXISTS `mmall_pay_info`;
CREATE TABLE `mmall_pay_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付流水id',
  `user_id` int(11) NOT NULL COMMENT '用户id，对应mmall_user表的主键',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `pay_platform` tinyint(1) DEFAULT NULL COMMENT '支付平台:1-支付宝，2-微信',
  `platform_no` varchar(200) DEFAULT '1' COMMENT '支付流水号',
  `platform_status` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mmall_product
-- ----------------------------
DROP TABLE IF EXISTS `mmall_product`;
CREATE TABLE `mmall_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `category_id` int(11) NOT NULL COMMENT '分类id，对应mmall_category表的主键',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '商品副标题',
  `main_image` varchar(500) DEFAULT NULL COMMENT '商品主图，url相对地址',
  `sub_images` text COMMENT '图片地址，json格式，扩展用',
  `detail` text COMMENT '商品详情',
  `price` decimal(20,2) NOT NULL COMMENT '价格，单位是元，保留两位有效数字',
  `stock` int(11) NOT NULL COMMENT '库存数量',
  `status` tinyint(1) DEFAULT '1' COMMENT '商品状态1-在售，2-下架，3删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mmall_shipping
-- ----------------------------
DROP TABLE IF EXISTS `mmall_shipping`;
CREATE TABLE `mmall_shipping` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id，对应mmall_user表的主键',
  `receive_name` varchar(20) DEFAULT NULL COMMENT '收货人姓名',
  `receive_mobile` varchar(20) DEFAULT NULL COMMENT '收货人手机号码',
  `receive_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receive_province` varchar(20) DEFAULT NULL COMMENT '收货人所在省',
  `receive_city` varchar(20) DEFAULT NULL COMMENT '收货人所在市',
  `receive_district` varchar(20) DEFAULT NULL COMMENT '收货人所在区',
  `receive_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `receive_zip` varchar(6) DEFAULT NULL COMMENT '邮政编码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mmall_user
-- ----------------------------
DROP TABLE IF EXISTS `mmall_user`;
CREATE TABLE `mmall_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT 'MD5加密用户密码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱地址',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `question` varchar(100) DEFAULT NULL COMMENT '找回密码问题',
  `answer` varchar(100) DEFAULT NULL COMMENT '找回密码答案',
  `role` int(4) NOT NULL COMMENT '角色0-管理员，1-普通用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
