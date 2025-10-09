/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80405 (8.4.5)
 Source Host           : localhost:3306
 Source Schema         : pet-system

 Target Server Type    : MySQL
 Target Server Version : 80405 (8.4.5)
 File Encoding         : 65001

 Date: 09/10/2025 23:51:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名称',
  `password` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '123456', '管理员', 'http://localhost:1000/file/c0d89a12e1ae8f57b9ba4bc15e281e42.jpg', '123456', '123456@javadh.com', '启用', '2024-12-07 17:29:35');
INSERT INTO `admin` VALUES (12, 'honghua', '123456', 'honghua', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '123321', '22222', '启用', '2025-09-22 14:53:49');

-- ----------------------------
-- Table structure for help_message
-- ----------------------------
DROP TABLE IF EXISTS `help_message`;
CREATE TABLE `help_message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `user_id` int NOT NULL COMMENT '用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '信息求助表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_message
-- ----------------------------
INSERT INTO `help_message` VALUES (1, '123', '123456', 1, '2025-09-23 18:37:46');
INSERT INTO `help_message` VALUES (2, '34565432', '15更多v在测序放下你从', 1, '2025-10-01 18:11:27');
INSERT INTO `help_message` VALUES (3, 'cmnzhcssca', '权威的和帮助下啊擦9啊擦时间为啊u成本', 1, '2025-10-01 18:11:46');

-- ----------------------------
-- Table structure for pet
-- ----------------------------
DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `main_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '封面图',
  `pet_type_id` int NOT NULL COMMENT '类型',
  `basic_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '基本信息',
  `user_id` int NOT NULL COMMENT '所属用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet
-- ----------------------------
INSERT INTO `pet` VALUES (2, '小九', 'http://localhost:1000/file/cad0d45851ba72c7ff89cdffa79967f5.jpg', 1, '', 1, '2025-09-27 19:28:49');
INSERT INTO `pet` VALUES (4, '九哥', 'http://localhost:1000/file/ac39fe1dd819107b52fc0d75a146a327.jpg', 1, '一只可爱的小猫咪', 1, '2025-09-27 19:35:29');

-- ----------------------------
-- Table structure for pet_diary
-- ----------------------------
DROP TABLE IF EXISTS `pet_diary`;
CREATE TABLE `pet_diary`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `user_id` int NOT NULL COMMENT '用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物日记' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_diary
-- ----------------------------
INSERT INTO `pet_diary` VALUES (3, '1', '1', 1, '2025-09-28 12:57:43');
INSERT INTO `pet_diary` VALUES (4, '2', '2', 1, '2025-09-28 13:01:18');
INSERT INTO `pet_diary` VALUES (5, '3', '444332', 1, '2025-09-28 13:05:01');

-- ----------------------------
-- Table structure for pet_feed
-- ----------------------------
DROP TABLE IF EXISTS `pet_feed`;
CREATE TABLE `pet_feed`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pet_id` int NOT NULL COMMENT '宠物',
  `pet_type_id` int NOT NULL COMMENT '宠物类型',
  `user_id` int NOT NULL COMMENT '用户',
  `pet_store_manager_id` int NOT NULL COMMENT '店长',
  `reserved_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '预约时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物喂养' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_feed
-- ----------------------------
INSERT INTO `pet_feed` VALUES (1, 2, 1, 1, 1, '2025-10-01 17:13:30', NULL, '已完成', '2025-09-28 17:18:56');
INSERT INTO `pet_feed` VALUES (2, 4, 1, 1, 1, '2025-10-01 17:13:29', 'wwww', '已完成', '2025-09-28 18:58:18');
INSERT INTO `pet_feed` VALUES (4, 4, 1, 1, 1, '2025-10-01 17:13:29', '222', '已完成', '2025-10-01 17:12:38');

-- ----------------------------
-- Table structure for pet_foster_care
-- ----------------------------
DROP TABLE IF EXISTS `pet_foster_care`;
CREATE TABLE `pet_foster_care`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pet_id` int NOT NULL COMMENT '宠物',
  `pet_type_id` int NOT NULL COMMENT '宠物类型',
  `user_id` int NOT NULL COMMENT '用户',
  `pet_store_manager_id` int NOT NULL COMMENT '店长',
  `reserved_start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '预约开始时间',
  `reserved_end_time` timestamp NULL DEFAULT NULL COMMENT '预约结束时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物寄养' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_foster_care
-- ----------------------------
INSERT INTO `pet_foster_care` VALUES (6, 4, 1, 1, 1, '2025-10-01 17:13:25', '2025-09-30 00:00:00', '1', '已完成', '2025-09-28 20:10:44');

-- ----------------------------
-- Table structure for pet_store_manager
-- ----------------------------
DROP TABLE IF EXISTS `pet_store_manager`;
CREATE TABLE `pet_store_manager`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺名称',
  `store_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店铺图片',
  `store_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物店长' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_store_manager
-- ----------------------------
INSERT INTO `pet_store_manager` VALUES (1, 'store', 'store', 'store', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '1234567876543', NULL, '启用', 'kk宠物店', 'http://localhost:1000/file/f3392168eb306c7cc1f7cab1ac6dde8e.jpg', '广东省河源市', '2025-09-23 15:03:26');
INSERT INTO `pet_store_manager` VALUES (2, 'store2', 'store2', 'store2', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', NULL, NULL, '启用', 'aa宠物店', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '广东省佛山市', '2025-10-01 17:27:11');
INSERT INTO `pet_store_manager` VALUES (3, 'store3', 'store3', 'store3', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', NULL, NULL, '启用', 'bb宠物店', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '广东省广州市', '2025-10-01 17:28:11');
INSERT INTO `pet_store_manager` VALUES (4, 'store4', 'store4', 'store4', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', NULL, NULL, '启用', 'cc宠物店', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '广东省深圳市', '2025-10-01 17:28:55');
INSERT INTO `pet_store_manager` VALUES (5, 'store5', 'store5', 'store5', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', NULL, NULL, '启用', 'dd宠物店', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '广东省东莞市', '2025-10-01 17:29:38');
INSERT INTO `pet_store_manager` VALUES (6, 'store6', 'store6', 'store6', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', NULL, NULL, '启用', 'ee宠物店', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '广东省汕尾市', '2025-10-01 17:31:01');

-- ----------------------------
-- Table structure for pet_type
-- ----------------------------
DROP TABLE IF EXISTS `pet_type`;
CREATE TABLE `pet_type`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_type
-- ----------------------------
INSERT INTO `pet_type` VALUES (1, '缅因', '大型猫', '2025-09-26 15:57:53');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `main_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '封面图',
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '介绍',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `price` float NOT NULL COMMENT '价格',
  `stock` int NOT NULL COMMENT '库存',
  `sales_volume` int NOT NULL COMMENT '销量',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商城商品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '化毛膏', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '处理毛球', '上架', 9.9, 199, 2, '2025-09-30 18:04:18');

-- ----------------------------
-- Table structure for product_order
-- ----------------------------
DROP TABLE IF EXISTS `product_order`;
CREATE TABLE `product_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名称',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_order
-- ----------------------------
INSERT INTO `product_order` VALUES (1, '化毛膏', '张三', 9.90, 1, '已完成', 'dd', '12345678', '广东省佛山市南海区广东东软学院', '2025-10-01 15:34:05');
INSERT INTO `product_order` VALUES (2, '化毛膏', '张三', 9.90, 1, '已完成', 'dd', '12345678', '广东省佛山市南海区广东东软学院', '2025-10-01 17:09:17');

-- ----------------------------
-- Table structure for shipping_address
-- ----------------------------
DROP TABLE IF EXISTS `shipping_address`;
CREATE TABLE `shipping_address`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电话',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户和名称',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收货地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of shipping_address
-- ----------------------------
INSERT INTO `shipping_address` VALUES (1, '红花', '123456', 'admin', '广东东软学院', '2025-09-30 19:15:49');
INSERT INTO `shipping_address` VALUES (4, 'dd', '12345678', '张三', '广东省佛山市南海区广东东软学院', '2025-10-01 15:31:02');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `balance` float NULL DEFAULT NULL COMMENT '余额',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '普通用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '张三', '123456', '1', 'http://localhost:1000/file/8826e8c280cb3bec6a4fbeb61514ee74.png', '213131', '123456@honghua.com', '启用', 80.2, '2025-09-18 13:57:54');
INSERT INTO `user` VALUES (2, '李四', '123456', '23', 'http://localhost:1000/file/8826e8c280cb3bec6a4fbeb61514ee74.png', '213131', '123456@honghua.com', '启用', 0, '2025-09-18 15:45:47');
INSERT INTO `user` VALUES (9, 'hong', 'hong', 'hong', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', NULL, NULL, '启用', 100, '2025-09-23 14:47:39');

SET FOREIGN_KEY_CHECKS = 1;
