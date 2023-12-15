/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : mall

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 15/12/2023 16:52:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ums_admin
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin`;
CREATE TABLE `ums_admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icon` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机验证码',
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` int NULL DEFAULT 1 COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_admin
-- ----------------------------
INSERT INTO `ums_admin` VALUES (11, 'admin', '$2a$10$D4gx.JcABvs.sTooRqo/SO6hDvaCN2rA3YZSmFEPXe59Wtz0XvYLG', '', '', '耿大大', '15036925269', NULL, '系统管理员', '2023-12-15 15:26:02', NULL, 1);

-- ----------------------------
-- Table structure for ums_admin_login_log
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin_login_log`;
CREATE TABLE `ums_admin_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_agent` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器登录类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 412 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户登录日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_admin_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for ums_admin_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin_role_relation`;
CREATE TABLE `ums_admin_role_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint NULL DEFAULT NULL,
  `role_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户和角色关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_admin_role_relation
-- ----------------------------
INSERT INTO `ums_admin_role_relation` VALUES (26, 3, 5);
INSERT INTO `ums_admin_role_relation` VALUES (27, 6, 1);
INSERT INTO `ums_admin_role_relation` VALUES (28, 7, 2);
INSERT INTO `ums_admin_role_relation` VALUES (29, 1, 5);
INSERT INTO `ums_admin_role_relation` VALUES (30, 4, 5);
INSERT INTO `ums_admin_role_relation` VALUES (31, 8, 1);

-- ----------------------------
-- Table structure for ums_member
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` int NULL DEFAULT NULL COMMENT '性别：0->未知；1->男；2->女',
  `phone` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机验证码',
  `status` int NULL DEFAULT NULL COMMENT '帐号启用状态:0->禁用；1->启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `icon` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE,
  UNIQUE INDEX `idx_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_member
-- ----------------------------
INSERT INTO `ums_member` VALUES (11, 'gengwei', '$2a$10$WpDsaW0JOngAUtETfE1umOHldQL06s1QBbWAJmRZxu65lmnd99V/2', NULL, NULL, '15036925269', NULL, 1, '2023-12-15 15:48:15', NULL, NULL);

-- ----------------------------
-- Table structure for ums_member_login_log
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_login_log`;
CREATE TABLE `ums_member_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_type` int NULL DEFAULT NULL COMMENT '登录类型：0->PC；1->android;2->ios;3->小程序',
  `province` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员登录记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_member_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for ums_menu
-- ----------------------------
DROP TABLE IF EXISTS `ums_menu`;
CREATE TABLE `ums_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `level` int NULL DEFAULT NULL COMMENT '菜单级数',
  `sort` int NULL DEFAULT NULL COMMENT '菜单排序',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端名称',
  `icon` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端图标',
  `hidden` int NULL DEFAULT NULL COMMENT '前端隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_menu
-- ----------------------------
INSERT INTO `ums_menu` VALUES (1, 0, '2020-02-02 14:50:36', '商品', 0, 0, 'pms', 'product', 0);
INSERT INTO `ums_menu` VALUES (2, 1, '2020-02-02 14:51:50', '商品列表', 1, 0, 'product', 'product-list', 0);
INSERT INTO `ums_menu` VALUES (3, 1, '2020-02-02 14:52:44', '添加商品', 1, 0, 'addProduct', 'product-add', 0);
INSERT INTO `ums_menu` VALUES (4, 1, '2020-02-02 14:53:51', '商品分类', 1, 0, 'productCate', 'product-cate', 0);
INSERT INTO `ums_menu` VALUES (5, 1, '2020-02-02 14:54:51', '商品类型', 1, 0, 'productAttr', 'product-attr', 0);
INSERT INTO `ums_menu` VALUES (6, 1, '2020-02-02 14:56:29', '品牌管理', 1, 0, 'brand', 'product-brand', 0);
INSERT INTO `ums_menu` VALUES (7, 0, '2020-02-02 16:54:07', '订单', 0, 0, 'oms', 'order', 0);
INSERT INTO `ums_menu` VALUES (8, 7, '2020-02-02 16:55:18', '订单列表', 1, 0, 'order', 'product-list', 0);
INSERT INTO `ums_menu` VALUES (9, 7, '2020-02-02 16:56:46', '订单设置', 1, 0, 'orderSetting', 'order-setting', 0);
INSERT INTO `ums_menu` VALUES (10, 7, '2020-02-02 16:57:39', '退货申请处理', 1, 0, 'returnApply', 'order-return', 0);
INSERT INTO `ums_menu` VALUES (11, 7, '2020-02-02 16:59:40', '退货原因设置', 1, 0, 'returnReason', 'order-return-reason', 0);
INSERT INTO `ums_menu` VALUES (12, 0, '2020-02-04 16:18:00', '营销', 0, 0, 'sms', 'sms', 0);
INSERT INTO `ums_menu` VALUES (13, 12, '2020-02-04 16:19:22', '秒杀活动列表', 1, 0, 'flash', 'sms-flash', 0);
INSERT INTO `ums_menu` VALUES (14, 12, '2020-02-04 16:20:16', '优惠券列表', 1, 0, 'coupon', 'sms-coupon', 0);
INSERT INTO `ums_menu` VALUES (16, 12, '2020-02-07 16:22:38', '品牌推荐', 1, 0, 'homeBrand', 'product-brand', 0);
INSERT INTO `ums_menu` VALUES (17, 12, '2020-02-07 16:23:14', '新品推荐', 1, 0, 'homeNew', 'sms-new', 0);
INSERT INTO `ums_menu` VALUES (18, 12, '2020-02-07 16:26:38', '人气推荐', 1, 0, 'homeHot', 'sms-hot', 0);
INSERT INTO `ums_menu` VALUES (19, 12, '2020-02-07 16:28:16', '专题推荐', 1, 0, 'homeSubject', 'sms-subject', 0);
INSERT INTO `ums_menu` VALUES (20, 12, '2020-02-07 16:28:42', '广告列表', 1, 0, 'homeAdvertise', 'sms-ad', 0);
INSERT INTO `ums_menu` VALUES (21, 0, '2020-02-07 16:29:13', '权限', 0, 0, 'ums', 'ums', 0);
INSERT INTO `ums_menu` VALUES (22, 21, '2020-02-07 16:29:51', '用户列表', 1, 0, 'admin', 'ums-admin', 0);
INSERT INTO `ums_menu` VALUES (23, 21, '2020-02-07 16:30:13', '角色列表', 1, 0, 'role', 'ums-role', 0);
INSERT INTO `ums_menu` VALUES (24, 21, '2020-02-07 16:30:53', '菜单列表', 1, 0, 'menu', 'ums-menu', 0);
INSERT INTO `ums_menu` VALUES (25, 21, '2020-02-07 16:31:13', '资源列表', 1, 0, 'resource', 'ums-resource', 0);

-- ----------------------------
-- Table structure for ums_resource
-- ----------------------------
DROP TABLE IF EXISTS `ums_resource`;
CREATE TABLE `ums_resource`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源URL',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `category_id` bigint NULL DEFAULT NULL COMMENT '资源分类ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_resource
-- ----------------------------
INSERT INTO `ums_resource` VALUES (1, '2020-02-04 17:04:55', '商品品牌管理', '/brand/**', NULL, 1);
INSERT INTO `ums_resource` VALUES (2, '2020-02-04 17:05:35', '商品属性分类管理', '/productAttribute/category/**', NULL, 1);
INSERT INTO `ums_resource` VALUES (3, '2020-02-04 17:06:13', '商品属性管理', '/productAttribute/**', NULL, 1);
INSERT INTO `ums_resource` VALUES (4, '2020-02-04 17:07:15', '商品分类管理', '/productCategory/**', NULL, 1);
INSERT INTO `ums_resource` VALUES (5, '2020-02-04 17:09:16', '商品管理', '/product/**', NULL, 1);
INSERT INTO `ums_resource` VALUES (6, '2020-02-04 17:09:53', '商品库存管理', '/sku/**', NULL, 1);
INSERT INTO `ums_resource` VALUES (8, '2020-02-05 14:43:37', '订单管理', '/order/**', '', 2);
INSERT INTO `ums_resource` VALUES (9, '2020-02-05 14:44:22', ' 订单退货申请管理', '/returnApply/**', '', 2);
INSERT INTO `ums_resource` VALUES (10, '2020-02-05 14:45:08', '退货原因管理', '/returnReason/**', '', 2);
INSERT INTO `ums_resource` VALUES (11, '2020-02-05 14:45:43', '订单设置管理', '/orderSetting/**', '', 2);
INSERT INTO `ums_resource` VALUES (12, '2020-02-05 14:46:23', '收货地址管理', '/companyAddress/**', '', 2);
INSERT INTO `ums_resource` VALUES (13, '2020-02-07 16:37:22', '优惠券管理', '/coupon/**', '', 3);
INSERT INTO `ums_resource` VALUES (14, '2020-02-07 16:37:59', '优惠券领取记录管理', '/couponHistory/**', '', 3);
INSERT INTO `ums_resource` VALUES (15, '2020-02-07 16:38:28', '限时购活动管理', '/flash/**', '', 3);
INSERT INTO `ums_resource` VALUES (16, '2020-02-07 16:38:59', '限时购商品关系管理', '/flashProductRelation/**', '', 3);
INSERT INTO `ums_resource` VALUES (17, '2020-02-07 16:39:22', '限时购场次管理', '/flashSession/**', '', 3);
INSERT INTO `ums_resource` VALUES (18, '2020-02-07 16:40:07', '首页轮播广告管理', '/home/advertise/**', '', 3);
INSERT INTO `ums_resource` VALUES (19, '2020-02-07 16:40:34', '首页品牌管理', '/home/brand/**', '', 3);
INSERT INTO `ums_resource` VALUES (20, '2020-02-07 16:41:06', '首页新品管理', '/home/newProduct/**', '', 3);
INSERT INTO `ums_resource` VALUES (21, '2020-02-07 16:42:16', '首页人气推荐管理', '/home/recommendProduct/**', '', 3);
INSERT INTO `ums_resource` VALUES (22, '2020-02-07 16:42:48', '首页专题推荐管理', '/home/recommendSubject/**', '', 3);
INSERT INTO `ums_resource` VALUES (23, '2020-02-07 16:44:56', ' 商品优选管理', '/prefrenceArea/**', '', 5);
INSERT INTO `ums_resource` VALUES (24, '2020-02-07 16:45:39', '商品专题管理', '/subject/**', '', 5);
INSERT INTO `ums_resource` VALUES (25, '2020-02-07 16:47:34', '后台用户管理', '/admin/**', '', 4);
INSERT INTO `ums_resource` VALUES (26, '2020-02-07 16:48:24', '后台用户角色管理', '/role/**', '', 4);
INSERT INTO `ums_resource` VALUES (27, '2020-02-07 16:48:48', '后台菜单管理', '/menu/**', '', 4);
INSERT INTO `ums_resource` VALUES (28, '2020-02-07 16:49:18', '后台资源分类管理', '/resourceCategory/**', '', 4);
INSERT INTO `ums_resource` VALUES (29, '2020-02-07 16:49:45', '后台资源管理', '/resource/**', '', 4);
INSERT INTO `ums_resource` VALUES (30, '2020-09-19 15:47:57', '会员等级管理', '/memberLevel/**', '', 7);
INSERT INTO `ums_resource` VALUES (31, '2020-09-19 15:51:29', '获取登录用户信息', '/admin/info', '用户登录必配', 4);
INSERT INTO `ums_resource` VALUES (32, '2020-09-19 15:53:34', '用户登出', '/admin/logout', '用户登出必配', 4);

-- ----------------------------
-- Table structure for ums_role
-- ----------------------------
DROP TABLE IF EXISTS `ums_role`;
CREATE TABLE `ums_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `admin_count` int NULL DEFAULT NULL COMMENT '后台用户数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` int NULL DEFAULT 1 COMMENT '启用状态：0->禁用；1->启用',
  `sort` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_role
-- ----------------------------
INSERT INTO `ums_role` VALUES (1, '商品管理员', '只能查看及操作商品', 0, '2020-02-03 16:50:37', 1, 0);
INSERT INTO `ums_role` VALUES (2, '订单管理员', '只能查看及操作订单', 0, '2018-09-30 15:53:45', 1, 0);
INSERT INTO `ums_role` VALUES (5, '超级管理员', '拥有所有查看和操作功能', 0, '2020-02-02 15:11:05', 1, 0);

-- ----------------------------
-- Table structure for ums_role_menu_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_role_menu_relation`;
CREATE TABLE `ums_role_menu_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台角色菜单关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_role_menu_relation
-- ----------------------------
INSERT INTO `ums_role_menu_relation` VALUES (53, 2, 7);
INSERT INTO `ums_role_menu_relation` VALUES (54, 2, 8);
INSERT INTO `ums_role_menu_relation` VALUES (55, 2, 9);
INSERT INTO `ums_role_menu_relation` VALUES (56, 2, 10);
INSERT INTO `ums_role_menu_relation` VALUES (57, 2, 11);
INSERT INTO `ums_role_menu_relation` VALUES (72, 5, 1);
INSERT INTO `ums_role_menu_relation` VALUES (73, 5, 2);
INSERT INTO `ums_role_menu_relation` VALUES (74, 5, 3);
INSERT INTO `ums_role_menu_relation` VALUES (75, 5, 4);
INSERT INTO `ums_role_menu_relation` VALUES (76, 5, 5);
INSERT INTO `ums_role_menu_relation` VALUES (77, 5, 6);
INSERT INTO `ums_role_menu_relation` VALUES (78, 5, 7);
INSERT INTO `ums_role_menu_relation` VALUES (79, 5, 8);
INSERT INTO `ums_role_menu_relation` VALUES (80, 5, 9);
INSERT INTO `ums_role_menu_relation` VALUES (81, 5, 10);
INSERT INTO `ums_role_menu_relation` VALUES (82, 5, 11);
INSERT INTO `ums_role_menu_relation` VALUES (83, 5, 12);
INSERT INTO `ums_role_menu_relation` VALUES (84, 5, 13);
INSERT INTO `ums_role_menu_relation` VALUES (85, 5, 14);
INSERT INTO `ums_role_menu_relation` VALUES (86, 5, 16);
INSERT INTO `ums_role_menu_relation` VALUES (87, 5, 17);
INSERT INTO `ums_role_menu_relation` VALUES (88, 5, 18);
INSERT INTO `ums_role_menu_relation` VALUES (89, 5, 19);
INSERT INTO `ums_role_menu_relation` VALUES (90, 5, 20);
INSERT INTO `ums_role_menu_relation` VALUES (91, 5, 21);
INSERT INTO `ums_role_menu_relation` VALUES (92, 5, 22);
INSERT INTO `ums_role_menu_relation` VALUES (93, 5, 23);
INSERT INTO `ums_role_menu_relation` VALUES (94, 5, 24);
INSERT INTO `ums_role_menu_relation` VALUES (95, 5, 25);
INSERT INTO `ums_role_menu_relation` VALUES (121, 1, 1);
INSERT INTO `ums_role_menu_relation` VALUES (122, 1, 2);
INSERT INTO `ums_role_menu_relation` VALUES (123, 1, 3);
INSERT INTO `ums_role_menu_relation` VALUES (124, 1, 4);
INSERT INTO `ums_role_menu_relation` VALUES (125, 1, 5);
INSERT INTO `ums_role_menu_relation` VALUES (126, 1, 6);

-- ----------------------------
-- Table structure for ums_role_resource_relation
-- ----------------------------
DROP TABLE IF EXISTS `ums_role_resource_relation`;
CREATE TABLE `ums_role_resource_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  `resource_id` bigint NULL DEFAULT NULL COMMENT '资源ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 249 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台角色资源关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_role_resource_relation
-- ----------------------------
INSERT INTO `ums_role_resource_relation` VALUES (194, 5, 1);
INSERT INTO `ums_role_resource_relation` VALUES (195, 5, 2);
INSERT INTO `ums_role_resource_relation` VALUES (196, 5, 3);
INSERT INTO `ums_role_resource_relation` VALUES (197, 5, 4);
INSERT INTO `ums_role_resource_relation` VALUES (198, 5, 5);
INSERT INTO `ums_role_resource_relation` VALUES (199, 5, 6);
INSERT INTO `ums_role_resource_relation` VALUES (200, 5, 8);
INSERT INTO `ums_role_resource_relation` VALUES (201, 5, 9);
INSERT INTO `ums_role_resource_relation` VALUES (202, 5, 10);
INSERT INTO `ums_role_resource_relation` VALUES (203, 5, 11);
INSERT INTO `ums_role_resource_relation` VALUES (204, 5, 12);
INSERT INTO `ums_role_resource_relation` VALUES (205, 5, 13);
INSERT INTO `ums_role_resource_relation` VALUES (206, 5, 14);
INSERT INTO `ums_role_resource_relation` VALUES (207, 5, 15);
INSERT INTO `ums_role_resource_relation` VALUES (208, 5, 16);
INSERT INTO `ums_role_resource_relation` VALUES (209, 5, 17);
INSERT INTO `ums_role_resource_relation` VALUES (210, 5, 18);
INSERT INTO `ums_role_resource_relation` VALUES (211, 5, 19);
INSERT INTO `ums_role_resource_relation` VALUES (212, 5, 20);
INSERT INTO `ums_role_resource_relation` VALUES (213, 5, 21);
INSERT INTO `ums_role_resource_relation` VALUES (214, 5, 22);
INSERT INTO `ums_role_resource_relation` VALUES (215, 5, 23);
INSERT INTO `ums_role_resource_relation` VALUES (216, 5, 24);
INSERT INTO `ums_role_resource_relation` VALUES (217, 5, 25);
INSERT INTO `ums_role_resource_relation` VALUES (218, 5, 26);
INSERT INTO `ums_role_resource_relation` VALUES (219, 5, 27);
INSERT INTO `ums_role_resource_relation` VALUES (220, 5, 28);
INSERT INTO `ums_role_resource_relation` VALUES (221, 5, 29);
INSERT INTO `ums_role_resource_relation` VALUES (222, 5, 30);
INSERT INTO `ums_role_resource_relation` VALUES (232, 2, 8);
INSERT INTO `ums_role_resource_relation` VALUES (233, 2, 9);
INSERT INTO `ums_role_resource_relation` VALUES (234, 2, 10);
INSERT INTO `ums_role_resource_relation` VALUES (235, 2, 11);
INSERT INTO `ums_role_resource_relation` VALUES (236, 2, 12);
INSERT INTO `ums_role_resource_relation` VALUES (237, 2, 31);
INSERT INTO `ums_role_resource_relation` VALUES (238, 2, 32);
INSERT INTO `ums_role_resource_relation` VALUES (239, 1, 1);
INSERT INTO `ums_role_resource_relation` VALUES (240, 1, 2);
INSERT INTO `ums_role_resource_relation` VALUES (241, 1, 3);
INSERT INTO `ums_role_resource_relation` VALUES (242, 1, 4);
INSERT INTO `ums_role_resource_relation` VALUES (243, 1, 5);
INSERT INTO `ums_role_resource_relation` VALUES (244, 1, 6);
INSERT INTO `ums_role_resource_relation` VALUES (245, 1, 23);
INSERT INTO `ums_role_resource_relation` VALUES (246, 1, 24);
INSERT INTO `ums_role_resource_relation` VALUES (247, 1, 31);
INSERT INTO `ums_role_resource_relation` VALUES (248, 1, 32);

SET FOREIGN_KEY_CHECKS = 1;
