/*
Navicat MySQL Data Transfer

Source Server         : localhost-gadmin
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : gpress

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2016-08-19 00:22:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gp_proc_flows
-- ----------------------------
DROP TABLE IF EXISTS `gp_proc_flows`;
CREATE TABLE `gp_proc_flows` (
  `proc_id` int(11) NOT NULL,
  `flow_id` int(11) DEFAULT NULL,
  `proc_name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `launch_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  `state` varchar(32) DEFAULT NULL,
  `json_data` varchar(2048) DEFAULT NULL,
  `modifier` varchar(32) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`proc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gp_proc_flows
-- ----------------------------

-- ----------------------------
-- Table structure for gp_proc_step
-- ----------------------------
DROP TABLE IF EXISTS `gp_proc_step`;
CREATE TABLE `gp_proc_step` (
  `step_id` int(11) NOT NULL,
  `proc_id` int(11) NOT NULL,
  `node_id` int(11) DEFAULT NULL,
  `step_name` varchar(255) DEFAULT NULL,
  `prev_step` varchar(255) DEFAULT NULL,
  `next_step` varchar(255) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `exec_mode` varchar(32) DEFAULT NULL,
  `executor` varchar(32) DEFAULT NULL,
  `exec_time` datetime DEFAULT NULL,
  `state` varchar(32) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`step_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gp_proc_step
-- ----------------------------

-- ----------------------------
-- Table structure for gp_quick_flows
-- ----------------------------
DROP TABLE IF EXISTS `gp_quick_flows`;
CREATE TABLE `gp_quick_flows` (
  `flow_id` int(11) NOT NULL,
  `flow_name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `modifier` varchar(32) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gp_quick_flows
-- ----------------------------

-- ----------------------------
-- Table structure for gp_quick_node
-- ----------------------------
DROP TABLE IF EXISTS `gp_quick_node`;
CREATE TABLE `gp_quick_node` (
  `node_id` int(11) NOT NULL,
  `flow_id` int(11) NOT NULL,
  `node_name` varchar(255) DEFAULT NULL,
  `prev_nodes` varchar(255) DEFAULT NULL,
  `next_nodes` varchar(255) DEFAULT NULL,
  `exec_mode` varchar(32) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gp_quick_node
-- ----------------------------
