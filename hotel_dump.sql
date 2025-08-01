-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: hotel_db
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill_items`
--

DROP TABLE IF EXISTS `bill_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `discount` double DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `total` double DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `bill_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj9o7g8krc56gf6t6f0sy4ic5p` (`bill_id`),
  CONSTRAINT `FKj9o7g8krc56gf6t6f0sy4ic5p` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_items`
--

LOCK TABLES `bill_items` WRITE;
/*!40000 ALTER TABLE `bill_items` DISABLE KEYS */;
INSERT INTO `bill_items` VALUES (1,0,'Tea',2,20,10,1),(2,0,'Coffee',1,20,20,1),(3,0,'Tea',2,20,10,2),(4,0,'Coffee',1,20,20,2),(5,0,'Tea',2,20,10,3),(6,0,'Coffee',1,20,20,3),(7,0,'Tea',2,20,10,4),(8,0,'Coffee',1,20,20,4),(9,0,'Tea',2,20,10,5),(10,0,'Coffee',1,20,20,5),(11,0,'Tea',2,20,10,6),(12,0,'Coffee',1,20,20,6),(13,0,'Tea',2,20,10,7),(14,0,'Coffee',1,20,20,7),(15,0,'Tea',2,20,10,8),(16,0,'Coffee',1,20,20,8),(17,0,'Tea',2,20,10,9),(18,0,'Coffee',1,20,20,9),(19,0,'Tea',2,20,10,10),(20,0,'Coffee',1,20,20,10),(21,0,'Tea',2,20,10,11),(22,0,'Coffee',1,20,20,11),(23,0,'Tea',2,20,10,12),(24,0,'Coffee',1,20,20,12),(25,0,'Tea',2,20,10,13),(26,0,'Coffee',1,20,20,13),(27,0,'Tea',2,20,10,14),(28,0,'Coffee',1,20,20,14),(29,0,'Tea',2,20,10,15),(30,0,'Coffee',1,20,20,15),(31,0,'Tea',2,20,10,16),(32,0,'Coffee',1,20,20,16),(33,0,'Tea',2,20,10,17),(34,0,'Coffee',1,20,20,17),(35,0,'Tea',2,20,10,18),(36,0,'Coffee',1,20,20,18),(37,0,'Tea',2,20,10,19),(38,0,'Coffee',1,20,20,19),(39,0,'Tea',2,20,10,20),(40,0,'Coffee',1,20,20,20),(41,0,'Tea',2,20,10,21),(42,0,'Coffee',1,20,20,21),(43,0,'Tea',2,20,10,22),(44,0,'Coffee',1,20,20,22),(45,0,'Tea',2,20,10,23),(46,0,'Coffee',1,20,20,23),(47,0,'Tea',2,20,10,24),(48,0,'Coffee',1,20,20,24),(49,0,'Tea',2,20,10,25),(50,0,'Coffee',1,20,20,25),(51,0,'Tea',2,20,10,26),(52,0,'Coffee',1,20,20,26),(53,0,'Tea',2,20,10,27),(54,0,'Coffee',1,20,20,27),(55,0,'Tea',2,20,10,28),(56,0,'Coffee',1,20,20,28),(57,0,'Tea',2,20,10,29),(58,0,'Coffee',1,20,20,29),(59,0,'Tea',2,20,10,30),(60,0,'Coffee',1,20,20,30),(61,0,'Tea',2,20,10,31),(62,0,'Coffee',1,20,20,31),(63,0,'Tea',2,20,10,32),(64,0,'Coffee',1,20,20,32),(65,0,'Tea',2,20,10,33),(66,0,'Coffee',1,20,20,33),(67,10,'Tea',2,490,250,34),(68,0,'Coffee',4,160,40,34),(69,0,'Tea',2,599.98,299.99,35),(70,10,'Tea',2,490,250,36),(71,0,'Coffee',4,160,40,36),(72,0,'Tea',2,599.98,299.99,37),(73,10,'Tea',2,490,250,38),(74,0,'Coffee',4,160,40,38),(75,0,'Tea',2,599.98,299.99,39),(76,10,'Tea',2,490,250,40),(77,0,'Coffee',4,160,40,40),(78,0,'Tea',2,599.98,299.99,41),(79,10,'Tea',2,490,250,42),(80,0,'Coffee',4,160,40,42),(81,0,'Tea',2,599.98,299.99,43),(82,10,'Tea',2,490,250,44),(83,0,'Coffee',4,160,40,44),(84,0,'Tea',2,599.98,299.99,45),(85,0,'Tea',2,20,10,46),(86,0,'Coffee',1,20,20,46),(87,0,'Tea',2,20,10,47),(88,0,'Coffee',1,20,20,47),(89,0,'Tea',2,20,10,48),(90,0,'Coffee',1,20,20,48),(91,0,'Tea',2,599.98,299.99,49),(92,0,'Tea',2,20,10,50),(93,0,'Coffee',1,20,20,50),(94,0,'Tea',2,599.98,299.99,51),(95,10,'Tea',2,490,250,52),(96,0,'Coffee',4,160,40,52),(97,0,'Tea',2,599.98,299.99,53),(98,0,'Tea',2,20,10,54),(99,0,'Coffee',1,20,20,54),(100,0,'Tea',2,20,10,55),(101,0,'Coffee',1,20,20,55),(102,0,'Tea',2,20,10,56),(103,0,'Coffee',1,20,20,56),(104,0,'Tea',2,30,15,57),(105,2,'Coffee',1,23,25,57),(106,0,'Tea',2,599.98,299.99,58),(107,0,'Tea',2,599.98,299.99,59),(108,0,'Tea',2,20,10,60),(109,0,'Coffee',1,20,20,60),(110,0,'Tea',2,20,10,61),(111,0,'Coffee',1,20,20,61),(112,0,'Tea',2,20,10,62),(113,0,'Coffee',1,20,20,62),(114,0,'Tea',2,20,10,63),(115,0,'Coffee',1,20,20,63),(116,0,'Tea',2,20,10,64),(117,0,'Coffee',1,20,20,64),(118,0,'Tea',2,20,10,65),(119,0,'Coffee',1,20,20,65),(120,0,'Tea',2,20,10,66),(121,0,'Coffee',1,20,20,66),(122,0,'Tea',2,20,10,67),(123,0,'Coffee',1,20,20,67),(124,0,'Tea',2,20,10,68),(125,0,'Coffee',1,20,20,68),(126,0,'Tea',2,20,10,69),(127,0,'Coffee',1,20,20,69),(128,0,'Tea',2,20,10,70),(129,0,'Coffee',1,20,20,70),(130,0,'Tea',2,20,10,71),(131,0,'Coffee',1,20,20,71),(132,0,'Tea',2,20,10,72),(133,0,'Coffee',1,20,20,72),(134,0,'Tea',2,20,10,73),(135,0,'Coffee',1,20,20,73),(136,0,'Tea',2,20,10,74),(137,0,'Coffee',1,20,20,74),(138,0,'Tea',2,20,10,75),(139,0,'Coffee',1,20,20,75);
/*!40000 ALTER TABLE `bill_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_number` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `discount` double DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  `tax` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoy9sc2dmxj2qwjeiiilf3yuxp` (`customer_id`),
  KEY `FKk8vs7ac9xknv5xp18pdiehpp1` (`user_id`),
  CONSTRAINT `FKk8vs7ac9xknv5xp18pdiehpp1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKoy9sc2dmxj2qwjeiiilf3yuxp` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (1,'INV-1001','2025-06-30 16:17:36.717121',5,40,7.2,42.2,NULL,NULL),(2,'INV-1001','2025-07-01 13:45:42.117860',5,40,7.2,42.2,NULL,NULL),(3,'INV-1001','2025-07-01 14:55:49.010287',5,40,7.2,42.2,NULL,NULL),(4,'INV-1001','2025-07-01 15:00:54.205965',5,40,7.2,42.2,NULL,NULL),(5,'INV-1001','2025-07-01 15:01:21.046975',5,40,7.2,42.2,NULL,NULL),(6,'INV-1001','2025-07-01 17:07:05.576610',5,40,7.2,42.2,NULL,NULL),(7,'INV-1001','2025-07-01 17:07:33.855006',5,40,7.2,42.2,NULL,NULL),(8,'INV-1001','2025-07-01 17:13:39.101874',5,40,7.2,42.2,NULL,NULL),(9,'INV-1001','2025-07-01 17:38:09.081189',5,40,7.2,42.2,NULL,NULL),(10,'INV-1001','2025-07-02 14:25:13.355648',5,40,7.2,42.2,NULL,NULL),(11,'INV-1001','2025-07-03 13:14:04.162338',5,40,7.2,42.2,NULL,NULL),(12,'INV-1001','2025-07-03 15:11:59.126225',5,40,7.2,42.2,NULL,NULL),(13,'INV-1001','2025-07-03 15:14:57.437170',5,40,7.2,42.2,NULL,NULL),(14,'INV-1001','2025-07-03 15:16:36.044572',5,40,7.2,42.2,NULL,NULL),(15,'INV-1001','2025-07-03 15:19:23.170363',5,40,7.2,42.2,NULL,NULL),(16,'INV-1001','2025-07-03 15:26:09.545189',5,40,7.2,42.2,NULL,NULL),(17,'INV-1001','2025-07-03 15:35:18.502134',5,40,7.2,42.2,NULL,NULL),(18,'INV-1001','2025-07-03 15:48:51.805362',5,40,7.2,42.2,NULL,NULL),(19,'INV-1001','2025-07-03 15:52:50.281094',5,40,7.2,42.2,NULL,NULL),(20,'INV-1001','2025-07-03 15:55:24.248543',5,40,7.2,42.2,NULL,NULL),(21,'INV-1001','2025-07-03 16:06:37.367072',5,40,7.2,42.2,NULL,NULL),(22,'INV-1001','2025-07-03 16:07:38.675975',5,40,7.2,42.2,NULL,NULL),(23,'INV-1001','2025-07-03 16:21:43.763405',5,40,7.2,42.2,NULL,NULL),(24,'INV-1001','2025-07-03 16:22:06.182957',5,40,7.2,42.2,NULL,NULL),(25,'INV-1001','2025-07-03 16:49:17.502400',5,40,7.2,42.2,NULL,NULL),(26,'INV-1001','2025-07-03 17:58:45.339009',5,40,7.2,42.2,NULL,NULL),(27,'INV-1001','2025-07-03 18:05:21.296591',5,40,7.2,42.2,NULL,NULL),(28,'INV-1001','2025-07-03 18:09:00.815116',5,40,7.2,42.2,NULL,NULL),(29,'INV-1001','2025-07-03 18:17:30.531318',5,40,7.2,42.2,NULL,NULL),(30,'INV-1001','2025-07-04 11:15:13.039822',5,40,7.2,42.2,NULL,NULL),(31,'INV-1001','2025-07-04 11:45:20.136589',5,40,7.2,42.2,NULL,NULL),(32,'INV-1001','2025-07-04 12:02:45.493610',5,40,7.2,42.2,NULL,NULL),(33,'INV-1001','2025-07-04 12:57:31.533982',5,40,7.2,42.2,NULL,NULL),(34,'MBL-1751614140851','2025-07-04 12:59:00.851515',0,650,78,728,1,NULL),(35,'INV-1751614375641','2025-07-04 13:02:55.641317',50,599.98,71.9976,621.9776,1,NULL),(36,'MBL-1751616259568','2025-07-04 13:34:19.568881',0,650,78,728,2,NULL),(37,'INV-1751616291218','2025-07-04 13:34:51.218490',50,599.98,71.9976,621.9776,2,NULL),(38,'MBL-1751616588169','2025-07-04 13:39:48.169662',0,650,78,728,2,NULL),(39,'INV-1751616621284','2025-07-04 13:40:21.284820',50,599.98,71.9976,621.9776,2,NULL),(40,'MBL-1751617792774','2025-07-04 13:59:52.775911',0,650,78,728,2,NULL),(41,'INV-1751617824420','2025-07-04 14:00:24.420088',50,599.98,71.9976,621.9776,2,NULL),(42,'MBL-1751617882656','2025-07-04 14:01:22.656750',0,650,78,728,2,NULL),(43,'INV-1751617900711','2025-07-04 14:01:40.711665',50,599.98,71.9976,621.9776,2,NULL),(44,'MBL-1751618035345','2025-07-04 14:03:55.345204',0,650,78,728,3,NULL),(45,'INV-1751618054413','2025-07-04 14:04:14.413369',50,599.98,71.9976,621.9776,3,NULL),(46,'INV-1001','2025-07-04 14:42:42.123105',5,40,7.2,42.2,NULL,NULL),(47,'INV-1001','2025-07-04 14:44:23.153255',5,40,7.2,42.2,NULL,NULL),(48,'INV-1001','2025-07-04 14:48:05.219995',5,40,7.2,42.2,NULL,NULL),(49,'INV-1751622614023','2025-07-04 15:20:14.023020',50,599.98,71.9976,621.9776,2,NULL),(50,'INV-1001','2025-07-04 17:39:00.879872',5,40,7.2,42.2,NULL,NULL),(51,'INV-1751631397678','2025-07-04 17:46:37.678982',50,599.98,71.9976,621.9776,1,NULL),(52,'MBL-1751631491584','2025-07-04 17:48:11.584345',0,650,78,728,2,NULL),(53,'INV-1751631541383','2025-07-04 17:49:01.383059',50,599.98,71.9976,621.9776,2,NULL),(54,'INV-1001','2025-07-07 10:58:45.984530',50,3000,2000,4950,NULL,NULL),(55,'INV-1001','2025-07-07 11:03:07.849388',5,40,7.2,42.2,NULL,NULL),(56,'INV-1001','2025-07-07 11:09:41.936799',5,40,7.2,42.2,NULL,NULL),(57,'MBL-1751866941845','2025-07-07 11:12:21.845478',0,53,6.359999999999999,59.36,2,NULL),(58,'INV-1751867057854','2025-07-07 11:14:17.855103',50,599.98,71.9976,621.9776,2,NULL),(59,'INV-1751867216481','2025-07-07 11:16:56.481740',50,599.98,71.9976,621.9776,2,NULL),(60,'INV-1001','2025-07-07 11:33:46.103786',5,40,7.2,42.2,NULL,NULL),(61,'INV-1001','2025-07-07 11:53:07.994250',5,40,7.2,42.2,NULL,NULL),(62,'INV-1001','2025-07-08 14:31:46.763233',5,40,7.2,42.2,NULL,NULL),(63,'INV-1001','2025-07-10 14:44:28.607645',5,40,7.2,42.2,NULL,NULL),(64,'INV-1001','2025-07-10 14:46:28.140906',5,40,7.2,42.2,NULL,NULL),(65,'INV-1001','2025-07-11 11:55:41.947261',5,40,7.2,42.2,NULL,NULL),(66,'INV-1001','2025-07-14 09:48:46.352485',5,40,7.2,42.2,NULL,NULL),(67,'INV-1001','2025-07-14 12:24:09.177487',5,40,7.2,42.2,NULL,NULL),(68,'INV-1001','2025-07-14 12:28:37.834880',5,40,7.2,42.2,NULL,NULL),(69,'INV-1001','2025-07-16 10:36:53.724062',5,40,7.2,42.2,NULL,NULL),(70,'INV-1001','2025-07-16 10:37:37.379054',5,40,7.2,42.2,NULL,NULL),(71,'INV-1001','2025-07-16 17:51:50.303447',5,40,7.2,42.2,NULL,NULL),(72,'INV-1001','2025-07-17 10:06:18.496155',5,40,7.2,42.2,NULL,NULL),(73,'INV-1001','2025-07-17 10:57:54.274615',5,40,7.2,42.2,NULL,NULL),(74,'INV-1001','2025-07-17 17:55:49.878819',5,40,7.2,42.2,NULL,NULL),(75,'INV-1001','2025-08-01 11:56:35.607375',5,40,7.2,42.2,NULL,NULL);
/*!40000 ALTER TABLE `bills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,NULL,NULL,'9876543210','Abhishek Mahajan'),(2,NULL,NULL,'9860060771','Abhishek Mahajan'),(3,NULL,NULL,'+919860060771','Abhishek Mahajan');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotels`
--

DROP TABLE IF EXISTS `hotels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotels` (
  `hotel_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gst_number` varchar(255) DEFAULT NULL,
  `hotel_name` varchar(255) DEFAULT NULL,
  `hotel_type` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`hotel_id`),
  UNIQUE KEY `UK497hd03hhoy0rnc18gqaibe7e` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotels`
--

LOCK TABLES `hotels` WRITE;
/*!40000 ALTER TABLE `hotels` DISABLE KEYS */;
INSERT INTO `hotels` VALUES (1,'gb road,pune','2025-06-30 16:17:36.528833','taj@gmail.com','GSTT1234','Hotelabhi','Luxury',_binary '','9985947896','Rajeev','2025-07-14 10:29:50.686533'),(2,'123 MG Road, Pune, Maharashtra','2025-07-03 16:08:30.444414','rahul@sunriseinn.com','27AAACI1234A1ZB','Sunrise Inn','Lodging',_binary '','9876543210','Rahul Sharma','2025-07-03 16:08:30.444414');
/*!40000 ALTER TABLE `hotels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `quantity` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `total_price` double NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkp5k52qtiygd8jkag4hayd0qg` (`product_id`),
  CONSTRAINT `FKkp5k52qtiygd8jkag4hayd0qg` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otpverification`
--

DROP TABLE IF EXISTS `otpverification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otpverification` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `expiry_time` datetime(6) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `verified` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otpverification`
--

LOCK TABLES `otpverification` WRITE;
/*!40000 ALTER TABLE `otpverification` DISABLE KEYS */;
/*!40000 ALTER TABLE `otpverification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `product_code` varchar(100) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `quantity` int DEFAULT NULL,
  `hotel_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK922x4t23nx64422orei4meb2y` (`product_code`),
  KEY `FK4kcfnm4xqk3rkrjpxaqx2rl7p` (`hotel_id`),
  KEY `FKdb050tk37qryv15hd932626th` (`user_id`),
  CONSTRAINT `FK4kcfnm4xqk3rkrjpxaqx2rl7p` FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`hotel_id`),
  CONSTRAINT `FKdb050tk37qryv15hd932626th` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Beverage',_binary '',10,'TEA001','Tea',100,1,NULL),(2,'Beverage',_binary '',20,'COF001','Coffee',100,1,NULL),(3,'breakfast',NULL,25,'12','Bun Muska',19,1,NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `revoked` bit(1) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2dylsfo39lgjyqml2tbe0b0ss` (`user_id`),
  CONSTRAINT `FK2dylsfo39lgjyqml2tbe0b0ss` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin12@taj.com','admin123'),(2,'abhishekmahajanabhi240@gmail.com','$2a$10$fnGGyvlEUnWZIRIq3GgsZuGk6sK3m84jNnfkxLJ.ZLh4d3iJK9ZdO'),(3,'gergwegwr@example.com','$2a$10$8Or9YI1S9zjFRsr6MzLRy.2vPMKl6Ul9Wwcxn1G150kV4R/2XG6OC');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whatsapp_logs`
--

DROP TABLE IF EXISTS `whatsapp_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `whatsapp_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone` varchar(255) DEFAULT NULL,
  `error` varchar(255) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whatsapp_logs`
--

LOCK TABLES `whatsapp_logs` WRITE;
/*!40000 ALTER TABLE `whatsapp_logs` DISABLE KEYS */;
INSERT INTO `whatsapp_logs` VALUES (1,101,'Ravi Sharma','+919860060771','Authenticate','Hello Ravi Sharma,\n\n Your bill is ready (ID: 101)\nThank you for dining with us. Your bill total is ₹850.\n\n View your bill PDF: https:http://localhost:8080//bill/101.pdf\n\nThank you for choosing our service!\n Hotel Management','FAILED','2025-07-04 11:25:41.873527'),(2,101,'Ravi Sharma','+919860060771',NULL,'Hello Ravi Sharma,\n\n Your bill is ready (ID: 101)\nThank you for dining with us. Your bill total is ₹850.\n\n View your bill PDF: https:http://localhost:8080//bill/101.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 11:46:52.404038'),(3,102,'Ravi Sharma','+919860060771',NULL,'Hello Ravi Sharma,\n\n Your bill is ready (ID: 102)\nThank you for dining with us. Your bill total is ₹850.\n\n View your bill PDF: https://yourdomain.com/bill/101.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 12:03:53.082132'),(4,35,'Abhishek Mahajan','9876543210',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 35)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751614375641.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 13:02:57.453134'),(5,37,'Abhishek Mahajan','9860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 37)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751616291218.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 13:34:51.732191'),(6,39,'Abhishek Mahajan','9860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 39)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751616621284.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 13:40:21.769869'),(7,41,'Abhishek Mahajan','9860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 41)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751617824420.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 14:00:24.879858'),(8,43,'Abhishek Mahajan','9860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 43)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751617900711.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 14:01:41.074052'),(9,45,'Abhishek Mahajan','+919860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 45)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751618054413.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 14:04:14.773615'),(10,49,'Abhishek Mahajan','+919860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 49)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751622614023.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 15:20:16.225778'),(11,51,'Abhishek Mahajan','+919876543210',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 51)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751631397678.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 17:46:40.104967'),(12,53,'Abhishek Mahajan','+919860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 53)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751631541383.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-04 17:49:01.823712'),(13,58,'Abhishek Mahajan','+919860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 58)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751867057854.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-07 11:14:18.918602'),(14,59,'Abhishek Mahajan','+919860060771',NULL,'Hello Abhishek Mahajan,\n\n Your bill is ready (ID: 59)\nYour bill total is ₹621.9776.\n\n View your bill PDF: https://yourdomain.cominvoices/Invoice_INV-1751867216481.pdf\n\nThank you for choosing our service!\n Hotel Management','SENT','2025-07-07 11:16:56.828803');
/*!40000 ALTER TABLE `whatsapp_logs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-01 11:56:36
