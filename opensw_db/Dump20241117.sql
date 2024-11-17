-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: religious_facility_review_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookmarks`
--

DROP TABLE IF EXISTS `bookmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmarks` (
  `bookmark_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `facility_id` int NOT NULL,
  PRIMARY KEY (`bookmark_id`),
  KEY `fk_bookmark_user_idx` (`user_id`),
  KEY `facility_id_idx` (`facility_id`),
  CONSTRAINT `facility_id` FOREIGN KEY (`facility_id`) REFERENCES `religious_facility_info` (`facility_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_bookmark_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmarks`
--

LOCK TABLES `bookmarks` WRITE;
/*!40000 ALTER TABLE `bookmarks` DISABLE KEYS */;
INSERT INTO `bookmarks` VALUES (1,1,1),(2,2,2),(3,3,3);
/*!40000 ALTER TABLE `bookmarks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_information`
--

DROP TABLE IF EXISTS `post_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_information` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(50) NOT NULL,
  `content` text,
  `category` varchar(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `likes` int DEFAULT '0',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_information`
--

LOCK TABLES `post_information` WRITE;
/*!40000 ALTER TABLE `post_information` DISABLE KEYS */;
INSERT INTO `post_information` VALUES (1,1,'\"봉사 활동 모집\"','\"다음 주 토요일, 공원 총소 봉사를 진행합니다.\"','봉사','2024-11-04 02:00:00',15),(2,2,'\"기부 행사 공지\"','\"이번 주말, 기부 행사가 진행됩니다. 많은 참여 바랍니다.\"','이벤트','2024-11-05 05:30:00',10),(3,3,'\"종교 세미나 안내\"','\"오는 금요일, 종교와 과학에 대한 세미나가 열립니다.\"','세미나','2024-11-06 00:15:00',25);
/*!40000 ALTER TABLE `post_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `religious_facility_info`
--

DROP TABLE IF EXISTS `religious_facility_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `religious_facility_info` (
  `facility_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  `address` varchar(50) NOT NULL,
  `phone` varchar(12) NOT NULL,
  `category` varchar(10) NOT NULL,
  `rating` decimal(3,2) DEFAULT '0.00',
  `description` text,
  `created_at` date DEFAULT NULL,
  PRIMARY KEY (`facility_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `religious_facility_info`
--

LOCK TABLES `religious_facility_info` WRITE;
/*!40000 ALTER TABLE `religious_facility_info` DISABLE KEYS */;
INSERT INTO `religious_facility_info` VALUES (1,'서울 성당','서울특별시 중구 성당로','021234567','천주교',4.80,'서울 중심에 위치한 역사적인 성당','1960-06-25'),(2,'대구 사찰','대구광역시 북구 사찰길','0539876543','불교',4.20,'대구에서 가장 오래된 사찰','1940-06-20'),(3,'부산 교회','부산광역시 해운대구','0517654321','개신교',4.50,'현대적인 시설을 갖춘 활기찬 교회','2001-06-16');
/*!40000 ALTER TABLE `religious_facility_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `facility_id` int NOT NULL,
  `user_id` int NOT NULL,
  `rating` decimal(3,2) NOT NULL DEFAULT '1.00',
  `comment` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `likes` int DEFAULT '0',
  PRIMARY KEY (`review_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,1,1,4.50,'\"시설이 정말 깨긋하고 편안했어요.\"','2024-11-04 15:00:00',12),(2,2,2,3.80,'\"목사님이 불친절합니다.\"','2024-11-06 15:00:00',8),(3,3,3,4.30,'\"절밥이 맛있습니다\"','2022-12-14 15:00:00',20);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `user_name` varchar(15) NOT NULL,
  `user_pwd` varchar(15) NOT NULL,
  `user_phone` char(11) NOT NULL,
  `user_email` varchar(40) NOT NULL,
  `user_adr` varchar(30) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `member_id_UNIQUE` (`user_id`),
  UNIQUE KEY `member_email_UNIQUE` (`user_email`),
  UNIQUE KEY `member_pwd_UNIQUE` (`user_pwd`),
  UNIQUE KEY `member_phone_UNIQUE` (`user_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'junsik0616','0000','01075857178','wn7178@naver.com','청주시 흥덕구 복대동','2024-11-16 15:00:00'),(2,'yeonhwi123','1111','01012345678','yeonhwi12@naver.com','청주시 흥덕구 사창동','2024-11-17 15:00:00'),(3,'hyunjun00','2222','01056781234','hj12@gmail.com','청주시 가덕면','2024-11-19 15:00:00'),(4,'seonghun','3333','01011112222','sh0214@naver.com','청주시 서원구','2023-01-10 15:00:00'),(5,'hyunayang','4444','01098765432','hyeonaa@daum.net','청주시 율량동','2022-03-14 15:00:00');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-17 22:45:20
