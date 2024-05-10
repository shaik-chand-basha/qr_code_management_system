CREATE DATABASE  IF NOT EXISTS `user_registration` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `user_registration`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: user_registration
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `authentication_details`
--

DROP TABLE IF EXISTS `authentication_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authentication_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(500) NOT NULL,
  `refresh_token` varchar(500) NOT NULL,
  `active` bit(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  `fk_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `authentication_details_fk_user_id` (`fk_user_id`),
  CONSTRAINT `authentication_details_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authentication_details`
--



--
-- Table structure for table `email_verification`
--

DROP TABLE IF EXISTS `email_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_verification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fk_user_id` bigint DEFAULT NULL,
  `token` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL,
  `otp_expires` timestamp NOT NULL,
  `email_verified` bit(1) DEFAULT b'0',
  `email` varchar(100) DEFAULT NULL,
  `active` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `email_verification_fk_user_id` (`fk_user_id`),
  CONSTRAINT `email_verification_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `event_attendence`
--

DROP TABLE IF EXISTS `event_attendence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_attendence` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fk_event_id` bigint DEFAULT NULL,
  `fk_user_id` bigint DEFAULT NULL,
  `attendence_datetime` timestamp NULL DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `approved` bit(1) DEFAULT b'0',
  `created_by` bigint NOT NULL,
  `created_at` timestamp NOT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  `fk_screenshot` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_attendence_fk_profile` (`fk_screenshot`),
  KEY `event_attendence_fk_event_id` (`fk_event_id`),
  KEY `event_attendence_fk_user_id` (`fk_user_id`),
  KEY `event_attendence_created_by` (`created_by`),
  KEY `event_attendence_last_modified_by` (`last_modified_by`),
  CONSTRAINT `event_attendence_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `event_attendence_fk_event_id` FOREIGN KEY (`fk_event_id`) REFERENCES `event_info` (`event_id`),
  CONSTRAINT `event_attendence_fk_profile` FOREIGN KEY (`fk_screenshot`) REFERENCES `image_metadata` (`id`),
  CONSTRAINT `event_attendence_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `event_attendence_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `event_info`
--

DROP TABLE IF EXISTS `event_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_info` (
  `event_id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `description` longtext,
  `starting_date` date DEFAULT NULL,
  `ending_date` date DEFAULT NULL,
  `whatsapp_group_link` varchar(200) DEFAULT NULL,
  `venue` varchar(200) DEFAULT NULL,
  `active` bit(1) DEFAULT b'0',
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  `fk_profile` bigint DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `event_info_fk_profile` (`fk_profile`),
  KEY `event_info_created_by` (`created_by`),
  KEY `event_info_last_modified_by` (`last_modified_by`),
  CONSTRAINT `event_info_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `event_info_fk_profile` FOREIGN KEY (`fk_profile`) REFERENCES `image_metadata` (`id`),
  CONSTRAINT `event_info_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_info`
--


DROP TABLE IF EXISTS `event_photos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_photos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  `fk_photo` bigint DEFAULT NULL,
  `fk_event_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_photos_fk_profile` (`fk_photo`),
  KEY `event_photos_created_by` (`created_by`),
  KEY `event_photos_last_modified_by` (`last_modified_by`),
  KEY `event_photos_fk_event_id` (`fk_event_id`),
  CONSTRAINT `event_photos_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `event_photos_fk_event_id` FOREIGN KEY (`fk_event_id`) REFERENCES `event_info` (`event_id`),
  CONSTRAINT `event_photos_fk_profile` FOREIGN KEY (`fk_photo`) REFERENCES `image_metadata` (`id`),
  CONSTRAINT `event_photos_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_registration`
--

DROP TABLE IF EXISTS `event_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_registration` (
  `fk_event_id` bigint NOT NULL,
  `fk_user_id` bigint NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`fk_event_id`,`fk_user_id`),
  KEY `event_registration_fk_user_id` (`fk_user_id`),
  KEY `event_registration_created_by` (`created_by`),
  KEY `event_registration_last_modified_by` (`last_modified_by`),
  CONSTRAINT `event_registration_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `event_registration_fk_event_id` FOREIGN KEY (`fk_event_id`) REFERENCES `event_info` (`event_id`),
  CONSTRAINT `event_registration_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `event_registration_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



DROP TABLE IF EXISTS `image_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_metadata` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `path_to_image` varchar(500) NOT NULL,
  `image_type` varchar(100) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `image_metadata_created_by` (`created_by`),
  KEY `image_metadata_last_modified_by` (`last_modified_by`),
  CONSTRAINT `image_metadata_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `image_metadata_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `student_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_details` (
  `fk_user_id` bigint NOT NULL,
  `address` varchar(500) DEFAULT NULL,
  `hallticket_num` varchar(100) DEFAULT NULL,
  `csi_id` varchar(30) DEFAULT NULL,
  `class` varchar(50) DEFAULT NULL,
  `college` varchar(200) DEFAULT NULL,
  `year_of_join` int DEFAULT NULL,
  `approved` bit(1) DEFAULT NULL,
  `fk_approved_by` bigint DEFAULT NULL,
  `active` bit(1) DEFAULT b'0',
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`fk_user_id`),
  UNIQUE KEY `hallticket_num` (`hallticket_num`),
  UNIQUE KEY `csi_id` (`csi_id`),
  KEY `student_details_created_by` (`created_by`),
  KEY `student_details_fk_approved_by` (`fk_approved_by`),
  KEY `student_details_last_modified_by` (`last_modified_by`),
  CONSTRAINT `student_details_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `student_details_fk_approved_by` FOREIGN KEY (`fk_approved_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `student_details_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `student_details_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



DROP TABLE IF EXISTS `token_validations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token_validations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fk_user_id` bigint DEFAULT NULL,
  `token` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL,
  `token_expires` timestamp NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  `active` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `token_validations_fk_user_id` (`fk_user_id`),
  KEY `token_validations_created_by` (`created_by`),
  KEY `token_validations_last_modified_by` (`last_modified_by`),
  CONSTRAINT `token_validations_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `token_validations_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `token_validations_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `gender` enum('M','F','O') NOT NULL,
  `password` varchar(400) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `mobile_number` varchar(20) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `active` bit(1) DEFAULT b'0',
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_at` timestamp NULL DEFAULT NULL,
  `fk_profile` bigint DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mobile_number` (`mobile_number`),
  KEY `user_info_created_by` (`created_by`),
  KEY `user_info_last_modified_by` (`last_modified_by`),
  KEY `user_info_fk_profile` (`fk_profile`),
  CONSTRAINT `user_info_created_by` FOREIGN KEY (`created_by`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `user_info_fk_profile` FOREIGN KEY (`fk_profile`) REFERENCES `image_metadata` (`id`),
  CONSTRAINT `user_info_last_modified_by` FOREIGN KEY (`last_modified_by`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `user_info_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info_role` (
  `fk_role_id` int NOT NULL,
  `fk_user_id` bigint NOT NULL,
  PRIMARY KEY (`fk_role_id`,`fk_user_id`),
  KEY `user_info_role_fk_user_id` (`fk_user_id`),
  CONSTRAINT `user_info_role_fk_role_id` FOREIGN KEY (`fk_role_id`) REFERENCES `user_role` (`role_id`),
  CONSTRAINT `user_info_role_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `user_role` varchar(20) NOT NULL,
  `active` bit(1) DEFAULT b'0',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'ROLE_ADMIN',_binary ''),(2,'ROLE_STUDENT',_binary ''),(3,'ROLE_INCHARGE',_binary '');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-30 12:10:29
