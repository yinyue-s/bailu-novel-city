/*
 Navicat Premium Dump SQL

 Source Server         : mybatis
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : novelcity

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 11/09/2026 13:47:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for author
-- ----------------------------
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author`  (
  `authorID` int NOT NULL AUTO_INCREMENT,
  `userID` bigint NOT NULL,
  `pen_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`authorID`) USING BTREE,
  UNIQUE INDEX `uk_userID`(`userID` ASC) USING BTREE,
  UNIQUE INDEX `uk_pen_name`(`pen_name` ASC) USING BTREE,
  CONSTRAINT `fk_author_user` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作者扩展信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bookshelf
-- ----------------------------
DROP TABLE IF EXISTS `bookshelf`;
CREATE TABLE `bookshelf`  (
  `shelfID` int NOT NULL AUTO_INCREMENT,
  `userID` bigint NOT NULL,
  `novelID` int NOT NULL,
  `groupID` int NULL DEFAULT NULL,
  `addTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastReadChapterID` int NULL DEFAULT NULL,
  `lastReadTime` datetime NULL DEFAULT NULL,
  `isPinned` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`shelfID`) USING BTREE,
  INDEX `fk_shelf_user`(`userID` ASC) USING BTREE,
  INDEX `fk_shelf_novel`(`novelID` ASC) USING BTREE,
  INDEX `fk_shelf_group`(`groupID` ASC) USING BTREE,
  CONSTRAINT `fk_shelf_group` FOREIGN KEY (`groupID`) REFERENCES `bookshelfgroup` (`groupID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_shelf_novel` FOREIGN KEY (`novelID`) REFERENCES `novel` (`novelID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_shelf_user` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bookshelfgroup
-- ----------------------------
DROP TABLE IF EXISTS `bookshelfgroup`;
CREATE TABLE `bookshelfgroup`  (
  `groupID` int NOT NULL AUTO_INCREMENT,
  `userID` bigint NOT NULL,
  `groupName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sortOrder` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`groupID`) USING BTREE,
  INDEX `fk_group_user`(`userID` ASC) USING BTREE,
  CONSTRAINT `fk_group_user` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for manuscript
-- ----------------------------
DROP TABLE IF EXISTS `manuscript`;
CREATE TABLE `manuscript`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `novelID` int NOT NULL COMMENT '小说ID',
  `authorID` bigint NOT NULL COMMENT '作者ID',
  `manuscript_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'MAIN' COMMENT '稿件类型',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '稿件标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '稿件内容',
  `word_count` int NULL DEFAULT 0 COMMENT '字数',
  `chapter_number` int NULL DEFAULT 0 COMMENT '章节号',
  `review_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'DRAFT' COMMENT '审核状态',
  `tags` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reviewer_id` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  `review_opinion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_novelID`(`novelID` ASC) USING BTREE,
  INDEX `idx_authorID`(`authorID` ASC) USING BTREE,
  INDEX `fk_manuscript_reviewer`(`reviewer_id` ASC) USING BTREE,
  CONSTRAINT `fk_manuscript_author` FOREIGN KEY (`authorID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_manuscript_novel` FOREIGN KEY (`novelID`) REFERENCES `novel` (`novelID`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_manuscript_reviewer` FOREIGN KEY (`reviewer_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '稿件信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for novel
-- ----------------------------
DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel`  (
  `novelID` int NOT NULL AUTO_INCREMENT,
  `novelName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authorID` bigint NULL DEFAULT NULL,
  `categoryID` int NOT NULL,
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `coverUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '连载',
  `wordCount` int NOT NULL DEFAULT 0,
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`novelID`) USING BTREE,
  INDEX `authorID`(`authorID` ASC) USING BTREE,
  INDEX `categoryID`(`categoryID` ASC) USING BTREE,
  CONSTRAINT `novel_ibfk_1` FOREIGN KEY (`authorID`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `novel_ibfk_2` FOREIGN KEY (`categoryID`) REFERENCES `novelcategory` (`categoryID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for novelcategory
-- ----------------------------
DROP TABLE IF EXISTS `novelcategory`;
CREATE TABLE `novelcategory`  (
  `categoryID` int NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`categoryID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` enum('READER','AUTHOR','EDITOR','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'READER',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
