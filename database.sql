-- MySQL Script - Optimized and Fixed Version
-- Model: Optimized Model    Version: 2.0

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema religious_facility_review_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `religious_facility_review_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `religious_facility_review_db`;

-- -----------------------------------------------------
-- Table: users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(15) NOT NULL,
  `user_pwd` VARCHAR(255) NOT NULL,
  `user_phone` CHAR(11) DEFAULT NULL,
  `user_email` VARCHAR(40) NOT NULL,
  `user_adr` VARCHAR(30) DEFAULT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `create_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_email_UNIQUE` (`user_email` ASC),
  UNIQUE INDEX `user_username_UNIQUE` (`username` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: facility_category
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facility_category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: religious_facility_info
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `religious_facility_info` (
  `facility_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `phone` CHAR(12) NOT NULL,
  `category_id` INT NOT NULL,
  `rating` DECIMAL(3,2) DEFAULT '0.00',
  `description` TEXT DEFAULT NULL,
  `created_at` DATE NOT NULL DEFAULT CURRENT_DATE,
  PRIMARY KEY (`facility_id`),
  CONSTRAINT `FK_facility_category`
    FOREIGN KEY (`category_id`) REFERENCES `facility_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: question
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `author_id` BIGINT NOT NULL,
  `create_date` DATETIME(6) DEFAULT NULL,
  `modify_date` DATETIME(6) DEFAULT NULL,
  `subject` VARCHAR(200) NOT NULL,
  `content` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_question_author` (`author_id` ASC),
  CONSTRAINT `FK_question_author`
    FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: answer
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `answer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `author_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,
  `create_date` DATETIME(6) DEFAULT NULL,
  `modify_date` DATETIME(6) DEFAULT NULL,
  `content` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_answer_question` (`question_id` ASC),
  INDEX `FK_answer_author` (`author_id` ASC),
  CONSTRAINT `FK_answer_question`
    FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
  CONSTRAINT `FK_answer_author`
    FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: reviews
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reviews` (
  `review_id` BIGINT NOT NULL AUTO_INCREMENT,
  `facility_id` INT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `rating` DECIMAL(3,2) NOT NULL DEFAULT '1.00',
  `comment` TEXT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`),
  INDEX `FK_review_facility` (`facility_id` ASC),
  INDEX `FK_review_user` (`user_id` ASC),
  CONSTRAINT `FK_review_facility`
    FOREIGN KEY (`facility_id`) REFERENCES `religious_facility_info` (`facility_id`),
  CONSTRAINT `FK_review_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: bookmarks
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookmarks` (
  `bookmark_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `facility_id` INT NOT NULL,
  PRIMARY KEY (`bookmark_id`),
  INDEX `FK_bookmark_facility` (`facility_id` ASC),
  INDEX `FK_bookmark_user` (`user_id` ASC),
  CONSTRAINT `FK_bookmark_facility`
    FOREIGN KEY (`facility_id`) REFERENCES `religious_facility_info` (`facility_id`),
  CONSTRAINT `FK_bookmark_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: post_information
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post_information` (
  `post_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(50) NOT NULL,
  `content` TEXT DEFAULT NULL,
  `category` VARCHAR(10) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `likes` INT DEFAULT '0',
  PRIMARY KEY (`post_id`),
  CONSTRAINT `FK_post_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: question_voter
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `question_voter` (
  `question_id` BIGINT NOT NULL,
  `voter_user_id` BIGINT NOT NULL,
  PRIMARY KEY (`question_id`, `voter_user_id`),
  INDEX `FK_question_voter_user` (`voter_user_id` ASC),
  CONSTRAINT `FK_question_voter_user`
    FOREIGN KEY (`voter_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_question_voter_question`
    FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table: answer_voter
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `answer_voter` (
  `answer_id` BIGINT NOT NULL,
  `voter_user_id` BIGINT NOT NULL,
  PRIMARY KEY (`answer_id`, `voter_user_id`),
  INDEX `FK_answer_voter_user` (`voter_user_id` ASC),
  CONSTRAINT `FK_answer_voter_user`
    FOREIGN KEY (`voter_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_answer_voter_answer`
    FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
