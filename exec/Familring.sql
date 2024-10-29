# create database d108;
use d108;


-- DROP
DROP TABLE IF EXISTS `family_user`;
DROP TABLE IF EXISTS `photo`;
DROP TABLE IF EXISTS `schedule_user`;
DROP TABLE IF EXISTS `question_family`;
DROP TABLE IF EXISTS `question_answer`;
DROP TABLE IF EXISTS `timecapsule_anwser`;
DROP TABLE IF EXISTS `interest_mission`;
DROP TABLE IF EXISTS `interest_answer`;

DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `family`;
DROP TABLE IF EXISTS `notification`;
DROP TABLE IF EXISTS `album`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `daily`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `timecapsule`;
DROP TABLE IF EXISTS `interest`;



-- USER
CREATE TABLE `user` (
	`user_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`user_kakao_id`	VARCHAR(100)	NOT NULL,
	`user_password`	VARCHAR(255)	NOT NULL,
	`user_nickname`	VARCHAR(100)	NOT NULL,
	`user_birth_date`	DATE	NOT NULL	COMMENT '띠 계산을 위한 것',
	`user_zodiac_sign`	VARCHAR(50)	NOT NULL	COMMENT '띠 계산 후 저장',
	`user_role`	ENUM('F', 'M', 'S', 'D')	NOT NULL	COMMENT 'F: 아빠, M: 엄마, S: 아들, D: 딸',
	`user_face`	VARCHAR(255)	NOT NULL	COMMENT '얼사분을 위한 회원 사진',
	`user_color`	VARCHAR(100)	NOT NULL	COMMENT 'HEX CODE',
	`user_emotion`	VARCHAR(100)	NOT NULL	DEFAULT "",
	`user_created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
	`user_modified_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
	`user_is_deleted`	TINYINT	NOT NULL	DEFAULT 0,
	`user_is_admin`	TINYINT	NOT NULL	DEFAULT 0,
    PRIMARY KEY(`user_id`)
);



-- FAMILY
CREATE TABLE `family` (
	`family_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`family_code`	CHAR(6)	NOT NULL,
	`family_count`	TINYINT	NOT NULL,
	`family_created_at`	TIMESTAMP	NOT NULL,
	`family_communication_status`	INT	NOT NULL	DEFAULT 75	COMMENT 'Lv.0: 0~24. Lv.1: 25~49. Lv.2:  50~74, Lv.3: 75~100',
    PRIMARY KEY(`family_id`)
);

CREATE TABLE `family_user` (
	`family_user_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`family_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
    PRIMARY KEY(`family_user_id`),
    CONSTRAINT `fk_family_id_family-user` FOREIGN KEY(`family_id`) REFERENCES `family` (`family_id`) ON DELETE CASCADE ON UPDATE CASCADE
);



-- NOTIFICATION
CREATE TABLE `notification` (
    `notification_id` BIGINT NOT NULL AUTO_INCREMENT,
    `receiver_user_id` BIGINT NOT NULL COMMENT '알림을 받을 사용자 ID',
    `sender_user_id` BIGINT NULL COMMENT '알림을 발생시킨 사용자 ID',
    `notification_type` ENUM('KNOCK', 'MENTION_CHAT', 'MENTION_SCHEDULE', 'RANDOM_QUESTION', 'TIMECAPSULE', 'INTEREST_PICK', 'INTEREST_COMPLETE') NOT NULL COMMENT '노션에 설명 써둠 !',
    `notification_title` VARCHAR(50) NOT NULL,
    `notification_mesage` VARCHAR(100) NOT NULL,
    `notification_is_read` TINYINT NOT NULL DEFAULT FALSE,
    `notification_read_at` TIMESTAMP NULL,
    `notification_created_at` TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY(`notification_id`)
);




-- ALBUM
CREATE TABLE `album` (
	`album_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`family_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NULL,
	`schedule_id`	BIGINT	NULL,
	`album_name`	VARCHAR(100)	NOT NULL,
	`album_type`	ENUM('SHARE', 'PERSON')	NOT NULL,
    PRIMARY KEY(`album_id`)
);

CREATE TABLE `photo` (
	`photo_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`album_id`	BIGINT	NOT NULL,
	`photo_url`	VARCHAR(255)	NOT NULL,
    PRIMARY KEY(`photo_id`),
    CONSTRAINT `fk_album_id_photo` FOREIGN KEY(`album_id`) REFERENCES `album` (`album_id`) ON DELETE CASCADE ON UPDATE CASCADE
);



-- CALENDAR
CREATE TABLE `schedule` (
	`schedule_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`schedule_start_time`	TIMESTAMP	NOT NULL,
	`schedule_end_time`	TIMESTAMP	NOT NULL,
	`schedule_title`	VARCHAR(100)	NOT NULL,
	`schedule_notification`	TINYINT	NOT NULL,
	`schedule_color`	VARCHAR(100)	NOT NULL,
    PRIMARY KEY(`schedule_id`)
);

CREATE TABLE `schedule_user` (
	`schedule_user_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`schedule_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL,
    PRIMARY KEY(`schedule_user_id`),
    CONSTRAINT `fk_schedule_id_schedule-user` FOREIGN KEY(`schedule_id`) REFERENCES `schedule` (`schedule_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `daily` (
	`daily_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`user_id`	BIGINT	NOT NULL,
	`daily_content`	VARCHAR(255)	NOT NULL,
	`daily_url`	VARCHAR(255)	NOT NULL,
	`daily_created_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
	`daily_modified_at`	TIMESTAMP	NOT NULL	DEFAULT NOW(),
    PRIMARY KEY(`daily_id`)
);



-- QUESTION
CREATE TABLE `question` (
	`question_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`question_content`	VARCHAR(255)	NOT NULL,
    PRIMARY KEY(`question_id`)
);

CREATE TABLE `question_family` (
	`question_family_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`question_id`	BIGINT	NOT NULL,
	`family_id`	BIGINT	NOT NULL,
    PRIMARY KEY(`question_family_id`),
    CONSTRAINT `fk_question_id_question-family` FOREIGN KEY(`question_id`) REFERENCES `question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `question_answer` (
	`question_answer_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`question_family_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL	COMMENT '질문 답변 작성자 고유번호',
	`question_answer`	VARCHAR(255)	NOT NULL,
	`question_created_at`	TIMESTAMP	NOT NULL,
	`question_modified_at`	TIMESTAMP	NOT NULL,
    PRIMARY KEY(`question_answer_id`),
    CONSTRAINT `fk_question_family_id_question-answer` FOREIGN KEY(`question_family_id`) REFERENCES `question_family` (`question_family_id`) ON DELETE CASCADE ON UPDATE CASCADE
);



-- TIMECAPSULE
CREATE TABLE `timecapsule` (
	`timecapsule_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`family_id`	BIGINT	NOT NULL,
	`timecapsule_start_date`	TIMESTAMP	NOT NULL,
	`timecapsule_end_date`	TIMESTAMP	NOT NULL,
    PRIMARY KEY(`timecapsule_id`)
);

CREATE TABLE `timecapsule_anwser` (
	`timecapsule_answer_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`timecapsule_id`	BIGINT	NOT NULL,
	`timecapsule_answer_content`	VARCHAR(255)	NOT NULL,
	`timecapsule_answer_created_at`	TIMESTAMP	NOT NULL,
    PRIMARY KEY(`timecapsule_answer_id`),
    CONSTRAINT `fk_timecapsule_id_timecapsule-anwser` FOREIGN KEY(`timecapsule_id`) REFERENCES `timecapsule` (`timecapsule_id`) ON DELETE CASCADE ON UPDATE CASCADE
);



-- INTEREST
CREATE TABLE `interest` (
	`interest_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`family_id`	BIGINT	NOT NULL,
	`interest_start_date`	TIMESTAMP	NOT NULL,
	`interest_end_date`	TIMESTAMP	NOT NULL,
    PRIMARY KEY(`interest_id`)
);

CREATE TABLE `interest_mission` (
	`interest_mission_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`interest_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL	COMMENT '관심사 인증 작성자 고유번호',
	`interest_mission_photo_url`	VARCHAR(255)	NOT NULL,
	`interest_mission_content`	VARCHAR(255)	NOT NULL,
    PRIMARY KEY(`interest_mission_id`),
    CONSTRAINT `fk_interest_id_interest-mission` FOREIGN KEY(`interest_id`) REFERENCES `interest` (`interest_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `interest_answer` (
	`interest_answer_id`	BIGINT	NOT NULL AUTO_INCREMENT,
	`interest_mission_id`	BIGINT	NOT NULL,
	`user_id`	BIGINT	NOT NULL	COMMENT '관심사 답변 작성자 고유번호',
	`interest_answer_content`	VARCHAR(100)	NOT NULL,
    PRIMARY KEY(`interest_answer_id`),
    CONSTRAINT `fk_interest_mission_id_interest-answer` FOREIGN KEY(`interest_mission_id`) REFERENCES `interest_mission` (`interest_mission_id`) ON DELETE CASCADE ON UPDATE CASCADE
);