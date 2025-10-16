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

 Date: 16/10/2025 14:47:07
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
  `reply_count` int NULL DEFAULT 0 COMMENT '回复数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '信息求助表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_message
-- ----------------------------
INSERT INTO `help_message` VALUES (1, '【紧急寄养】流浪橘猫母子四人，求一个月的临时庇护所', '大家好！我在小区停车场发现了一只非常亲人的流浪橘猫妈妈，它带着三只刚满月的小猫。小猫们非常可爱，但停车场环境复杂，非常危险。我已经带它们去做了基础检查和驱虫，确保健康。\n由于我个人原因无法长期收养，希望能为它们找到一个或几个安全的临时寄养家庭，期限大约一个月，直到小猫断奶并找到领养家庭。猫粮、猫砂等所有费用由我承担！只求能给这四个小生命一个遮风挡雨的地方。如果您能帮助，请与我联系，万分感谢！\n', 1, '2025-09-23 18:37:46', 2);
INSERT INTO `help_message` VALUES (2, '【重金悬赏寻狗】棕色边境牧羊犬“木木”于人民公园附近走失', '爱犬“木木”于昨天下午在人民公园南门附近因受惊吓挣脱绳索跑失。它是一只三岁大的棕色边境牧羊犬，额头有一道明显的白色条纹，叫它“木木”会有反应。\n“木木”是我们家不可或缺的一员，感情深厚。如有好心人发现或收留，请立刻与我们联系，我们愿意重金酬谢，绝不食言！也恳请附近的朋友帮忙转发扩散，多一次转发，就多一份希望！谢谢大家！', 1, '2025-10-01 18:11:27', 1);
INSERT INTO `help_message` VALUES (3, '【医疗众筹】陪伴八年的金毛“多多”确诊胰腺炎，求助医疗费', '“多多”是我八年前从救助站领养回来的金毛，它温柔、懂事，陪伴我度过了人生中最艰难的时光。如今它被确诊为急性胰腺炎，需要立即住院治疗。高昂的医疗费用让我这个刚工作的年轻人实在难以独自承担。\n万般无奈之下，在此发起求助。恳请各位爱护动物的朋友们能帮帮“多多”，无论是一次转发还是一份支持，都能给我们带来巨大的希望。所有款项将全部用于治疗，并会全程公示。谢谢每一位善良的你！', 1, '2025-10-01 18:11:46', 1);
INSERT INTO `help_message` VALUES (4, '【寻求上门喂养】寻找有经验的朋友，帮忙照顾英短蓝猫一周', '因公司临时安排重要出差，时间为10月1日至10月7日，家中一岁大的英短蓝猫“煤球”无人照料。它已绝育，疫苗齐全，性格胆小但非常温顺。\n希望能找到一位有养猫经验、有爱心、住在同城的朋友，在这七天里每天上门一次，帮忙添加粮水、清理猫砂盆，并陪它玩一会儿。我会准备好所有用品，并支付相应的酬劳。希望“煤球”能在熟悉的家里安心等待我回来。感谢您的帮助！', 1, '2025-10-10 00:00:08', 0);
INSERT INTO `help_message` VALUES (5, '【求领养】给它一个家！两个月大的中华田园犬妹妹找领养', '这只可爱的小狗是我在路边纸箱里发现的，当时它和兄弟姐妹们被遗弃在那里。其他小狗都已找到领养，只剩下它一个，是个乖巧的小妹妹。它大约两个月大，健康活泼，会用尿垫。\n希望为它寻找一位有责任心、不离不弃的主人。要求：科学喂养，办理狗证，适龄绝育，接受回访。它会用一生的忠诚和爱来回报您。如果您准备好了，请给它一个温暖的家吧！', 2, '2025-10-10 14:47:26', 0);
INSERT INTO `help_message` VALUES (6, '【在线求助】猫咪最近食欲不振，精神萎靡，求问可能原因？', '各位宠友和专业人士好！我家猫咪三岁，已绝育公猫。最近三天发现它食欲明显下降，猫粮吃得很少，精神也不如以前，总喜欢躲在角落睡觉。没有呕吐拉稀现象，小便正常。\n因为最近工作太忙，暂时无法立刻带它去医院，心里非常焦急。想先在线咨询一下，有没有朋友或医生遇到过类似情况？这可能是什么问题的前兆吗？任何建议都将对我有很大帮助！谢谢大家！', 2, '2025-10-10 15:06:46', 0);
INSERT INTO `help_message` VALUES (7, '【寻找宠友】有没有城东区的小伙伴？周末一起遛哈士奇呀！', '大家好！我家有一只一岁半的哈士奇“二哈”，精力无比旺盛，每天都需要大量运动。我一个人遛它常常感觉力不从心，而且它看到别的狗有时会特别兴奋。\n想寻找同样住在城东区、养中大型犬的朋友，周末或晚上可以约在附近的公园或宠物友好区一起遛狗，让狗狗们有个玩伴，社会化更好，我们也可以交流养宠心得。期待认识你们和你们的毛孩子！\n', 2, '2025-10-10 15:11:56', 0);
INSERT INTO `help_message` VALUES (8, '【物资求助】个人流浪狗救助小院断粮，求援狗粮或物资', '大家好，我是一个个人流浪狗救助者，小院里目前收留了十几只从街上救回来的狗狗。由于最近救助了两只重病的狗狗，医疗开销巨大，导致小院的狗粮储备已经见底。\n在此恳求大家的帮助，如果您家有狗狗不爱吃的富余狗粮，或者愿意捐赠一些狗粮、旧衣物（用作垫子）、消毒液等物资，我们将感激不尽！也接受二手闲置的宠物用品。您的点滴爱心，都能让这些毛孩子吃上一顿饱饭。\n', 9, '2025-10-10 15:15:13', 1);

-- ----------------------------
-- Table structure for help_reply
-- ----------------------------
DROP TABLE IF EXISTS `help_reply`;
CREATE TABLE `help_reply`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `help_id` int NOT NULL COMMENT '求助信息ID',
  `user_id` int NOT NULL COMMENT '回复用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回复内容',
  `parent_id` int NULL DEFAULT NULL COMMENT '父回复ID(用于嵌套回复)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_help_id`(`help_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '求助回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_reply
-- ----------------------------
INSERT INTO `help_reply` VALUES (1, 1, 2, '这个问题我也遇到过，建议你先检查一下宠物的饮食情况', NULL, '2025-10-13 23:43:47');
INSERT INTO `help_reply` VALUES (2, 2, 1, '可以试试这个方法，我的宠物之前也有类似问题', NULL, '2025-10-13 23:43:47');
INSERT INTO `help_reply` VALUES (3, 1, 9, '饮食确实很重要，另外还要注意休息', 1, '2025-10-13 23:43:47');
INSERT INTO `help_reply` VALUES (4, 3, 2, '建议尽快带宠物去医院检查一下', NULL, '2025-10-13 23:43:47');
INSERT INTO `help_reply` VALUES (6, 8, 1, '12313edesd', NULL, '2025-10-14 00:00:24');

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
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet
-- ----------------------------
INSERT INTO `pet` VALUES (2, '小九', 'http://localhost:1000/file/cad0d45851ba72c7ff89cdffa79967f5.jpg', 1, '', 1, '2025-09-27 19:28:49');
INSERT INTO `pet` VALUES (4, '九哥', 'http://localhost:1000/file/ac39fe1dd819107b52fc0d75a146a327.jpg', 1, '一只可爱的小猫咪', 1, '2025-09-27 19:35:29');
INSERT INTO `pet` VALUES (5, '小鸟', 'http://localhost:1000/file/46ec0ce549867af20b038fbe2e82dff7.png', 5, '123', 2, '2025-10-10 15:00:47');
INSERT INTO `pet` VALUES (6, '小龟', 'http://localhost:1000/file/c2bbc1b04de790dc79a3f919863a7219.png', 8, '44', 2, '2025-10-10 15:01:33');
INSERT INTO `pet` VALUES (7, '鸟', 'http://localhost:1000/file/46ec0ce549867af20b038fbe2e82dff7.png', 5, '566', 2, '2025-10-10 15:02:14');
INSERT INTO `pet` VALUES (8, '小鱼', 'http://localhost:1000/file/57b63c54aa2a25f646ded0c966c37ac1.png', 6, '3423d', 2, '2025-10-10 15:04:09');
INSERT INTO `pet` VALUES (9, '鱼2', 'http://localhost:1000/file/57b63c54aa2a25f646ded0c966c37ac1.png', 6, '44444', 2, '2025-10-10 15:04:40');
INSERT INTO `pet` VALUES (10, '小狗', 'http://localhost:1000/file/33640c1826189a8b310a92f9fea1aced.png', 4, NULL, 9, '2025-10-10 15:13:33');
INSERT INTO `pet` VALUES (11, '狗2', 'http://localhost:1000/file/33640c1826189a8b310a92f9fea1aced.png', 4, NULL, 9, '2025-10-10 15:13:56');
INSERT INTO `pet` VALUES (12, '狗3', 'http://localhost:1000/file/33640c1826189a8b310a92f9fea1aced.png', 4, NULL, 9, '2025-10-10 15:14:10');
INSERT INTO `pet` VALUES (13, '兔子', 'http://localhost:1000/file/54b97f27be2966ee6d58225cf42705c9.png', 7, NULL, 9, '2025-10-10 15:14:32');

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物日记' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_diary
-- ----------------------------
INSERT INTO `pet_diary` VALUES (3, '深夜的陪伴，是最安心的力量', '昨晚熬夜赶一个方案，到了深夜两三点，家里静得只剩键盘声。我家的大橘“福仔”本来早已在窝里睡熟，不知何时却跳上了书桌，在我手边找了个空位趴下，既不吵也不闹，只是静静地陪着我。偶尔伸出爪子，轻轻搭在我的手腕上。那一刻，疲惫感仿佛瞬间减轻了大半。原来，我不是一个人在战斗。有你真好。\n\n', 1, '2025-09-28 12:57:43');
INSERT INTO `pet_diary` VALUES (4, '和一只猫争夺纸箱的“战争”', '网上买的猫爬架到了，我兴高采烈地组装好，呼唤我家“主子”来享用。结果它高贵冷艳地瞥了一眼，然后毫不犹豫地钻进了旁边那个装猫爬架的破纸箱里，开始又抓又咬，玩得不亦乐乎。看着它在那个“免费豪宅”里自得其乐的样子，我忍不住笑出声。好吧，你快乐就好，毕竟在“如何获得快乐”这门学问上，你才是我的导师。', 1, '2025-09-28 13:01:18');
INSERT INTO `pet_diary` VALUES (5, '它把最爱的玩具，送给了我', '下班回到家，一开门就看见我家柴犬“阿柴”兴奋地摇着尾巴冲过来，嘴里还叼着它那个已经玩得脏兮兮的橙色橡胶球。它把球放到我脚边，然后用湿漉漉的、充满期待的大眼睛望着我。我知道，这是它表达“我想你”和“我爱你”的最高级方式。虽然只是一个旧玩具，但在我心里，却是今天收到的最珍贵的礼物。', 1, '2025-09-28 13:05:01');
INSERT INTO `pet_diary` VALUES (6, '各自忙碌，又互相陪伴', '周末的下午，我在书桌前看书或工作，我家英短“奶糖”就蜷在桌角的猫窝里，睡得四仰八叉。阳光透过窗户洒在它身上，把它的毛镀成了一层金色。偶尔它会醒来，伸个懒腰，看我一眼，然后换个姿势继续睡。房间里很安静，只有它均匀的呼吸声和书页翻动的声音。这种彼此独立又相互陪伴的静谧时光，让人觉得无比安心和富足。\n\n', 1, '2025-10-10 14:48:17');
INSERT INTO `pet_diary` VALUES (7, '它终于学会了“击掌”！', '经过快一个星期的努力，我家的小柯基“饼干”今天终于清晰地完成了“击掌”的动作！当它的小爪子准确地拍在我掌心时，我和它都愣住了，随后我们俩都兴奋地又叫又跳！它开心地转着圈，而我比自己升职加薪还高兴。这种共同成长、分享喜悦的感觉，真的太美妙了。', 1, '2025-10-10 15:06:07');
INSERT INTO `pet_diary` VALUES (8, '坏心情的终结者，是一只猫', '今天被一些琐事弄得心情很低落，回到家瘫在沙发上一言不发。我家高冷的“大王”仿佛察觉到了什么，它默默地走过来，跳上沙发，把它毛茸茸的身子紧紧地贴在我的腿边，用它的大尾巴轻轻地圈住我的手腕。那一刻，所有的不快好像都被它温暖的体温融化了。世界或许有时很复杂，但它的陪伴却如此简单而直接。', 1, '2025-10-10 15:10:37');
INSERT INTO `pet_diary` VALUES (9, '谢谢你，教我欣赏慢下来的风景', '傍晚遛狗时，原本因为工作有些烦躁。但我家金毛“元宝”却兴致勃勃，它认真地嗅着每一棵草、每一块石头，仿佛在阅读一本有趣的报纸。看着它那专注而快乐的样子，我也不自觉地慢下了脚步，开始注意到天边粉色的晚霞和拂面的微风。谢谢你，我的小老师，是你一次次教会我，生活不在别处，就在此刻的脚下和眼前。', 1, '2025-10-10 15:11:06');
INSERT INTO `pet_diary` VALUES (10, '毛茸茸的闹钟，比阳光更准时', '今天又是被我家猫咪“拿铁”用带刺的小舌头舔醒的，时间刚好是六点半，比闹钟还准。它用脑袋蹭着我的脸，喉咙里发出满足的呼噜声，像一辆小型摩托车。睡意瞬间被这温暖的幸福感驱散。新的一天，从这份沉甸甸、毛茸茸的爱开始，感觉什么都难不倒我！', 1, '2025-10-10 15:14:50');

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
  `reserved_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物喂养' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_feed
-- ----------------------------
INSERT INTO `pet_feed` VALUES (1, 2, 1, 1, 1, '2025-10-01 17:13:30', NULL, '已完成', '2025-09-28 17:18:56');
INSERT INTO `pet_feed` VALUES (2, 4, 1, 1, 1, '2025-10-01 17:13:29', 'wwww', '已完成', '2025-09-28 18:58:18');
INSERT INTO `pet_feed` VALUES (4, 4, 1, 1, 1, '2025-10-01 17:13:29', '222', '已完成', '2025-10-01 17:12:38');
INSERT INTO `pet_feed` VALUES (9, 4, 1, 1, 1, '2025-10-16 00:00:00', NULL, '已下单', '2025-10-12 13:39:33');
INSERT INTO `pet_feed` VALUES (10, 4, 1, 1, 1, '2025-10-25 00:00:00', NULL, '服务中', '2025-10-12 13:40:11');

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
INSERT INTO `pet_store_manager` VALUES (2, 'store2', 'store2', 'store2', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '123456789', '123456789@qq.com', '启用', 'aa宠物店', 'http://localhost:1000/file/26f2d8462385b8967c0a77ea9b04c216.webp', '广东省佛山市', '2025-10-01 17:27:11');
INSERT INTO `pet_store_manager` VALUES (3, 'store3', 'store3', 'store3', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '123456789', '123456789@qq.com', '启用', 'bb宠物店', 'http://localhost:1000/file/51841ea33a2b9683edca1a69e1d63aba.webp', '广东省广州市', '2025-10-01 17:28:11');
INSERT INTO `pet_store_manager` VALUES (4, 'store4', 'store4', 'store4', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '1234566789', '1234566789@qq.com', '启用', 'cc宠物店', 'http://localhost:1000/file/063a81156c65f5c3b6425f36fb51f8f2.webp', '广东省深圳市', '2025-10-01 17:28:55');
INSERT INTO `pet_store_manager` VALUES (5, 'store5', 'store5', 'store5', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '123456789', '123456789@qq.com', '启用', 'dd宠物店', 'http://localhost:1000/file/1840a7996db5b0e154079382129bdb49.webp', '广东省东莞市', '2025-10-01 17:29:38');
INSERT INTO `pet_store_manager` VALUES (6, 'store6', 'store6', 'store6', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', '123456789', '123456789@qq.com', '启用', 'ee宠物店', 'http://localhost:1000/file/477717e9959080ea360bd1b14343b7cf.jpg', '广东省汕尾市', '2025-10-01 17:31:01');

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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pet_type
-- ----------------------------
INSERT INTO `pet_type` VALUES (1, '猫', '食肉目猫科猫属的脊索动物', '2025-09-26 15:57:53');
INSERT INTO `pet_type` VALUES (4, '狗', '食肉目犬科犬属哺乳动物', '2025-10-10 14:36:03');
INSERT INTO `pet_type` VALUES (5, '鸟', '脊椎动物亚门的一纲', '2025-10-10 14:36:59');
INSERT INTO `pet_type` VALUES (6, '鱼', '最古老的脊椎动物', '2025-10-10 14:38:43');
INSERT INTO `pet_type` VALUES (7, '兔', '兔形目兔科动物下的一属', '2025-10-10 14:39:43');
INSERT INTO `pet_type` VALUES (8, '龟', '现存的爬行类中最为特化的一类', '2025-10-10 14:42:08');

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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商城商品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '化毛膏', 'http://localhost:1000/file/912b84bf7fd273d5bcc87dde2cfa5685.webp', '处理毛球', '上架', 9.9, 190, 11, '2025-09-30 18:04:18');
INSERT INTO `product` VALUES (2, '狗窝', 'http://localhost:1000/file/3ed5c8272f8a8d8f7a911c6490e06f89.webp', '是否曾见过它在你离家时，蜷在冰冷地板上等待的模样？是时候，为它打造一个真正温暖、安全的专属小家了。\n我们的狗窝，不止是简单的睡垫，更是它安心休憩的港湾。采用高密度回弹填充内芯，为关节提供稳固支撑，有效缓解日常奔跑跳跃的压力。表层面料亲肤柔软，并经过耐磨防抓处理，内里则选用透气保暖的短绒，真正做到冬暖夏凉。可全拆洗的设计，让清洁变得轻而易举，时刻守护爱犬的睡眠健康。\n我们相信，爱是给予最贴心的守护。这个小小的窝，盛放的是它毫无保留的依赖与忠诚。从此，每一个夜晚，都有美梦相伴。给它一个最好的窝，就是给它一份触手可及的幸福。\n', '上架', 40, 50, 29, '2025-10-15 20:02:26');
INSERT INTO `product` VALUES (3, '猫抓板', 'http://localhost:1000/file/b1e6a3be8a53111b3c96d3dc75ceaecd.jpg', '是否还在为沙发、窗帘的抓痕而心痛？别急着责怪猫咪，这只是它们与生俱来的天性。与其制止，不如为它提供一个专属的释放空间！\n我们的猫抓板，就是为满足猫咪抓挠天性而设计的理想乐园。采用致密瓦楞纸，耐磨耐抓，能有效吸引猫咪的注意，让它从此爱上这里，自然保护您的贵重家具。独特的立体设计，不仅是一个抓板，更是一个可以趴、可以睡、可以探险的多功能堡垒。内置的猫薄荷空间，能瞬间激发猫咪的玩耍兴趣。\n一份贴心的礼物，既能守护家的完整，也能尊重伙伴的天性。选择它，告别拆家烦恼，享受和谐、亲密的陪伴时光！', '上架', 77, 50, 6, '2025-10-15 20:04:00');
INSERT INTO `product` VALUES (4, '猫爬架', 'http://localhost:1000/file/1128027eeb098f988a767914d46dae88.webp', '您家是否也有一位“空中交通管制员”？猫咪天生就爱登高望远，那是它们刻在基因里的安全感与乐趣。一个普通的家，对它们来说，或许只是二维的地面。是时候，为它打造一座专属的三维空中城堡了！\n我们的猫爬架，远不止是玩具，更是猫咪的健身中心、瞭望台与安乐窝。采用天然剑麻柱缠绕，坚固耐磨，完美满足它们抓挠的天性，贴心守护您的家具。稳固的基座与多层次平台，让它们可以自信地攀爬、跳跃，尽情释放活力。顶部的舒适软垫观景台，则是它巡视“领地”的最佳位置，享受独处的安宁。\n这不仅是一件宠物用品，更是您对爱猫天性的尊重与宠爱。为它投资一座猫爬架，就是为它创造一个更快乐、更健康、更符合本性的生活环境。看它在上面慵懒酣睡或兴奋探索，您会明白，这一切都值得。\n', '上架', 66, 50, 10, '2025-10-15 20:05:02');
INSERT INTO `product` VALUES (5, '牵引绳', 'http://localhost:1000/file/010fec2358f1e7badb5968e3f5f4dfff.webp', '每一次出门探险，都是一场信任的同行。一根好的牵引绳，不仅是安全的保障，更是您与爱犬之间无形的亲密纽带。我们深知，它向往自由，而您守护它的安全。\n我们的牵引绳，为此而生。采用航空级铝合金扣具与高强度尼龙织带，确保坚固耐用，能从容应对突发状况。符合人体工学的缓冲手柄，有效减轻拉扯时的冲击力，保护您的手腕，也让爱犬更舒适。可自由调节的长度，既满足它安心探索的好奇心，也给您随时唤回的控制力。\n我们相信，最美好的时光，是在路上。系上这条牵引绳，意味着责任，更意味着无限的爱与陪伴。放开探索，握紧安全，让每一次出行，都成为彼此记忆中轻松愉悦的风景。', '上架', 88, 50, 24, '2025-10-15 20:06:05');
INSERT INTO `product` VALUES (6, '驱虫药', 'http://localhost:1000/file/3c1d47fb1db311581f8592c37f94cd3e.jpg', '看不见的威胁，正悄悄接近您心爱的伙伴。草丛里的跳蚤、看不见的虫卵、通过蚊子传播的心丝虫……这些不仅仅是困扰，更会严重危及它们的健康。守护爱宠，驱虫防护是第一道，也是不可或缺的防线。\n我们的宠物驱虫药，为您提供专业、全面的守护。采用科学配方，能高效驱杀和预防跳蚤、虱子、蜱虫及多种体内外寄生虫，阻断寄生虫生命周期。产品剂量经过精准计算，温和高效，确保爱宠安全。更重要的是，我们化繁为简：只需每月一次，简单滴于颈后，即可带来长效保护，让防护变得轻松无负担。\n爱它，就给它最周全的呵护。驱虫不仅是治“已病”，更是防“未病”。选择我们，就是为它撑起一把坚实的保护伞，让您可以毫无后顾之忧地享受亲密无间的陪伴时光。', '上架', 36, 50, 13, '2025-10-15 20:08:44');
INSERT INTO `product` VALUES (7, '喂食玩具', 'http://localhost:1000/file/ad41e7d584dda145574c358dbdf67f21.jpg', '吃饭，也可以是一场有趣的冒险！您是否担心爱宠狼吞虎咽影响消化，或是在您离家时感到无聊焦虑？是时候升级它们的用餐体验了。\n我们的益智喂食玩具，正是为这些烦恼量身打造的完美解决方案。它巧妙地将取食与游戏合二为一，宠物需要通过拨动、滚动、破解等动作，才能让美食缓缓漏出。这个过程，不仅能显著减慢进食速度，促进消化，预防呕吐和肥胖，更能充分激发它们的好奇心与智力，消耗多余精力，有效缓解分离焦虑。\n这不仅仅是一个食盆，更是宠物的“全职娱乐官”和“健康管理师”。把简单的喂食变成充满挑战的游戏，让它们在获取食物的过程中获得巨大的成就感与满足感。从此，独自在家的时光不再漫长，而是充满探索的乐趣。', '上架', 55, 40, 8, '2025-10-15 20:10:19');
INSERT INTO `product` VALUES (8, '宠物飞盘', 'http://localhost:1000/file/a8b2a0af2c0858fd3ad540ab09750c20.webp', '每一次抛出的弧线，都是对它快乐的精准召唤！您是否也想寻找一种方式，既能释放爱犬无穷的精力，又能深度增进彼此的感情？宠物飞盘，就是为您和它量身打造的最佳互动游戏。\n我们的专业宠物飞盘，远非普通玩具可比。采用柔软耐撕咬的柔性材质制成，保护爱犬牙齿和口腔免受伤害；符合空气动力学的设计，让飞行轨迹更稳定，易于狗狗追逐和衔取；超轻量化的机身，减轻狗狗负重，让它能更轻松地跃起、接住，享受奔跑的纯粹乐趣。\n这不仅仅是一次投掷，更是一场属于你们俩的默契训练与健康运动。看它在阳光下自信跃起的身影，您会感受到无与伦比的满足与快乐。放下手机，拾起飞盘，一起奔向户外，创造只属于你们的美好回忆吧！', '上架', 19, 78, 5, '2025-10-15 20:11:47');

-- ----------------------------
-- Table structure for product_order
-- ----------------------------
DROP TABLE IF EXISTS `product_order`;
CREATE TABLE `product_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名称',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',
  `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_order
-- ----------------------------
INSERT INTO `product_order` VALUES (1, '化毛膏', '张三', 1, 9.90, 1, '已完成', 'dd', '12345678', '广东省佛山市南海区广东东软学院', '2025-10-01 15:34:05');
INSERT INTO `product_order` VALUES (2, '化毛膏', '张三', 1, 9.90, 1, '已完成', 'dd', '12345678', '广东省佛山市南海区广东东软学院', '2025-10-01 17:09:17');
INSERT INTO `product_order` VALUES (3, '化毛膏', '张三', 1, 19.80, 2, '已完成', 'dd', '12345678', '广东省佛山市南海区广东东软学院', '2025-10-10 13:53:15');
INSERT INTO `product_order` VALUES (4, '化毛膏', '张三', 1, 69.30, 7, '已完成', 'dd', '12345678', '广东省佛山市南海区广东东软学院', '2025-10-10 14:10:45');

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
INSERT INTO `user` VALUES (1, '张三', '123456', '1', 'http://localhost:1000/file/8826e8c280cb3bec6a4fbeb61514ee74.png', '213131', '123456@honghua.com', '启用', 91.1, '2025-09-18 13:57:54');
INSERT INTO `user` VALUES (2, '李四', '123456', '23', 'http://localhost:1000/file/8826e8c280cb3bec6a4fbeb61514ee74.png', '213131', '123456@honghua.com', '启用', 0, '2025-09-18 15:45:47');
INSERT INTO `user` VALUES (9, 'hong', 'hong', 'hong', 'http://localhost:1000/file/e7e519ce78d211099fcd2243d7397e94.png', NULL, NULL, '启用', 100, '2025-09-23 14:47:39');

-- ----------------------------
-- Triggers structure for table help_reply
-- ----------------------------
DROP TRIGGER IF EXISTS `update_help_reply_count`;
delimiter ;;
CREATE TRIGGER `update_help_reply_count` AFTER INSERT ON `help_reply` FOR EACH ROW BEGIN
    UPDATE `help_message` SET `reply_count` = (
        SELECT COUNT(*) FROM `help_reply` WHERE `help_id` = NEW.`help_id`
    ) WHERE `id` = NEW.`help_id`;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
