-- MySQL dump 10.13  Distrib 9.3.0, for Win64 (x86_64)
--
-- Host: localhost    Database: hotel_db
-- ------------------------------------------------------
-- Server version	9.3.0

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
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_items`
--

LOCK TABLES `bill_items` WRITE;
/*!40000 ALTER TABLE `bill_items` DISABLE KEYS */;
INSERT INTO `bill_items` VALUES (15,0,'Tea',2,20,10,8),(16,0,'Coffee',1,20,20,8),(17,0,'Tea',2,20,10,9),(18,0,'Coffee',1,20,20,9),(19,0,'Tea',2,20,10,10),(20,0,'Coffee',1,20,20,10),(21,0,'Tea',2,20,10,11),(22,0,'Coffee',1,20,20,11),(23,0,'Tea',2,20,10,12),(24,0,'Coffee',1,20,20,12),(25,0,'Tea',2,20,10,13),(26,0,'Coffee',1,20,20,13),(27,0,'Tea',2,20,10,14),(28,0,'Coffee',1,20,20,14),(29,0,'Tea',2,20,10,15),(30,0,'Coffee',1,20,20,15),(31,0,'Tea',2,20,10,16),(32,0,'Coffee',1,20,20,16),(33,0,'Tea',2,20,10,17),(34,0,'Coffee',1,20,20,17),(35,0,'Tea',2,20,10,18),(36,0,'Coffee',1,20,20,18),(37,0,'Tea',2,20,10,19),(38,0,'Coffee',1,20,20,19),(39,0,'Tea',2,20,10,20),(40,0,'Coffee',1,20,20,20),(41,0,'Tea',2,20,10,21),(42,0,'Coffee',1,20,20,21),(43,0,'Tea',2,20,10,22),(44,0,'Coffee',1,20,20,22),(45,0,'Tea',2,20,10,23),(46,0,'Coffee',1,20,20,23),(47,0,'Tea',2,20,10,24),(48,0,'Coffee',1,20,20,24),(49,0,'Tea',2,20,10,25),(50,0,'Coffee',1,20,20,25),(51,0,'Tea',2,20,10,26),(52,0,'Coffee',1,20,20,26),(53,0,'Tea',2,20,10,27),(54,0,'Coffee',1,20,20,27),(55,0,'Tea',2,20,10,28),(56,0,'Coffee',1,20,20,28),(57,0,'Tea',2,20,10,29),(58,0,'Coffee',1,20,20,29),(59,0,'Tea',2,20,10,30),(60,0,'Coffee',1,20,20,30),(61,0,'Tea',2,20,10,31),(62,0,'Coffee',1,20,20,31),(63,0,'Tea',2,20,10,32),(64,0,'Coffee',1,20,20,32),(65,0,'Tea',2,20,10,33),(66,0,'Coffee',1,20,20,33),(67,0,'Tea',2,20,10,34),(68,0,'Coffee',1,20,20,34),(69,0,'Tea',2,20,10,35),(70,0,'Coffee',1,20,20,35),(73,0,'Tea',2,20,10,37),(74,0,'Coffee',1,20,20,37),(79,0,'Tea',2,20,10,40),(80,0,'Coffee',1,20,20,40),(85,0,'Tea',2,20,10,43),(86,0,'Coffee',1,20,20,43),(89,0,'Tea',2,20,10,45),(90,0,'Coffee',1,20,20,45),(93,0,'Tea',2,20,10,47),(94,0,'Coffee',1,20,20,47),(97,0,'Tea',2,20,10,49),(98,0,'Coffee',1,20,20,49),(121,0,'Tea',2,20,10,61),(122,0,'Coffee',1,20,20,61),(123,0,'Tea',2,30,15,62),(124,2,'Samosa',1,23,25,62),(125,0,'Tea',2,20,10,63),(126,0,'Coffee',1,20,20,63),(127,0,'Tea',2,20,10,64),(128,0,'Coffee',1,20,20,64),(129,0,'Samosa',2,30,15,65),(130,2,'Samosa',1,23,25,65),(131,0,'Tea',2,20,10,66),(132,0,'Coffee',1,20,20,66),(133,0,'Tea',2,20,10,67),(134,0,'Coffee',1,20,20,67),(135,0,'Tea',2,20,10,68),(136,0,'Coffee',1,20,20,68),(137,0,'Tea',2,20,10,69),(138,0,'Coffee',1,20,20,69);
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
  PRIMARY KEY (`id`),
  KEY `FKoy9sc2dmxj2qwjeiiilf3yuxp` (`customer_id`),
  CONSTRAINT `FKoy9sc2dmxj2qwjeiiilf3yuxp` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (8,'INV-1001','2025-06-30 14:19:51.793165',5,40,7.2,42.2,NULL),(9,'INV-1001','2025-06-30 15:24:29.074457',5,40,7.2,42.2,NULL),(10,'INV-1001','2025-07-01 11:23:33.655535',5,40,7.2,42.2,NULL),(11,'INV-1001','2025-07-01 11:24:13.993257',5,40,7.2,42.2,NULL),(12,'INV-1001','2025-07-01 11:25:05.327199',5,40,7.2,42.2,NULL),(13,'INV-1001','2025-07-01 11:52:12.170876',5,40,7.2,42.2,NULL),(14,'INV-1001','2025-07-01 11:57:24.052794',5,40,7.2,42.2,NULL),(15,'INV-1001','2025-07-01 12:27:45.211060',5,40,7.2,42.2,NULL),(16,'INV-1001','2025-07-01 12:35:57.477905',5,40,7.2,42.2,NULL),(17,'INV-1001','2025-07-01 12:36:47.175719',5,40,7.2,42.2,NULL),(18,'INV-1001','2025-07-01 12:47:41.683963',5,40,7.2,42.2,NULL),(19,'INV-1001','2025-07-01 12:48:53.549338',5,40,7.2,42.2,NULL),(20,'INV-1001','2025-07-01 12:59:02.344380',5,40,7.2,42.2,NULL),(21,'INV-1001','2025-07-01 13:01:48.707046',5,40,7.2,42.2,NULL),(22,'INV-1001','2025-07-01 13:04:56.483233',5,40,7.2,42.2,NULL),(23,'INV-1001','2025-07-01 13:06:09.707165',5,40,7.2,42.2,NULL),(24,'INV-1001','2025-07-01 13:09:42.616575',5,40,7.2,42.2,NULL),(25,'INV-1001','2025-07-01 14:27:14.886301',5,40,7.2,42.2,NULL),(26,'INV-1001','2025-07-01 14:34:13.040218',5,40,7.2,42.2,NULL),(27,'INV-1001','2025-07-01 14:35:28.324299',5,40,7.2,42.2,NULL),(28,'INV-1001','2025-07-01 14:36:23.484539',5,40,7.2,42.2,NULL),(29,'INV-1001','2025-07-01 14:42:34.100455',5,40,7.2,42.2,NULL),(30,'INV-1001','2025-07-01 15:10:16.282063',5,40,7.2,42.2,NULL),(31,'INV-1001','2025-07-01 16:18:14.434234',5,40,7.2,42.2,NULL),(32,'INV-1001','2025-07-01 16:19:47.206908',5,40,7.2,42.2,NULL),(33,'INV-1001','2025-07-01 16:50:38.648394',5,40,7.2,42.2,NULL),(34,'INV-1001','2025-07-01 16:54:28.108224',5,40,7.2,42.2,NULL),(35,'INV-1001','2025-07-03 10:59:52.170964',5,40,7.2,42.2,NULL),(37,'INV-1001','2025-07-03 11:18:21.671145',5,40,7.2,42.2,NULL),(40,'INV-1001','2025-07-03 11:29:33.144608',5,40,7.2,42.2,NULL),(43,'INV-1001','2025-07-03 13:13:55.117484',5,40,7.2,42.2,NULL),(45,'INV-1001','2025-07-04 14:52:58.173309',5,40,7.2,42.2,NULL),(47,'INV-1001','2025-07-04 15:12:01.680985',5,40,7.2,42.2,NULL),(49,'INV-1001','2025-07-04 17:38:17.311354',5,40,7.2,42.2,NULL),(61,'INV-1001','2025-07-07 11:25:28.702463',5,40,7.2,42.2,NULL),(62,'MBL-1751867944447','2025-07-07 11:29:04.452745',0,53,6.359999999999999,59.36,1),(63,'INV-1001','2025-07-07 11:35:50.196479',5,40,7.2,42.2,NULL),(64,'INV-1001','2025-07-07 11:41:28.435560',5,40,7.2,42.2,NULL),(65,'MBL-1751868810086','2025-07-07 11:43:30.087898',0,53,6.359999999999999,59.36,1),(66,'INV-1001','2025-07-07 11:48:36.087583',5,40,7.2,42.2,NULL),(67,'INV-1001','2025-07-07 12:22:58.158322',5,40,7.2,42.2,NULL),(68,'INV-1001','2025-07-07 17:25:39.157198',5,40,7.2,42.2,NULL),(69,'INV-1001','2025-07-08 11:32:09.690483',5,40,7.2,42.2,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,NULL,NULL,'9860060771','Rahul Sharma');
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotels`
--

LOCK TABLES `hotels` WRITE;
/*!40000 ALTER TABLE `hotels` DISABLE KEYS */;
INSERT INTO `hotels` VALUES (1,'Sector 12, Connaught Place, New Delhi, 110001','2025-06-30 10:22:12.747921','sneha.elitehotel@example.com',NULL,NULL,NULL,NULL,NULL,NULL,'2025-06-30 14:01:36.215551'),(2,NULL,'2025-06-30 14:05:47.129647','taj@gmail.com','GSTT1234','Hotel Taj','Luxury',_binary '','9985947896','Rajeev','2025-06-30 14:05:47.129647'),(3,'pune','2025-07-06 17:47:10.295458','swaraj@gmail.com','p001','Swaraj','Luxury',_binary '','09146747054','raj','2025-07-06 17:47:10.303263'),(4,'','2025-07-07 12:24:05.674244','','','Rajv','',_binary '','','','2025-07-07 12:24:05.674244'),(5,'at post Tai/Dist ahamadnagar','2025-07-07 12:27:04.295803','vaibhavkadus@gmail.com','123','a','Luxury',_binary '','1234567890','b','2025-07-07 12:27:04.295803');
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
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `hotel_id` bigint DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `product_code` varchar(100) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4kcfnm4xqk3rkrjpxaqx2rl7p` (`hotel_id`),
  CONSTRAINT `FK4kcfnm4xqk3rkrjpxaqx2rl7p` FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`hotel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (2,'Food',200,1,_binary '','SOF100','Samosa',1),(3,'Beverage',10,1,_binary '','TEA001','Tea',100),(4,'Beverage',20,1,_binary '','COF001','Coffee',100),(6,'Beverage',10,2,_binary '','TEA001','Tea',100),(7,'Beverage',20,2,_binary '','COF001','Coffee',100),(12,'Food',2000,1,_binary '','1023','ab',1),(13,'Food',2500,1,_binary '','KCT001','kachori',10);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin12@taj.com','admin123'),(2,'ram@gmail.com','$2a$10$x93vwgiDva/l7FJVA7ipLOBJUbgt9NqdKFPAxD84h0nNAsgkIkPFy'),(3,'admin@gmail.com','$2a$10$ML1g9eY/Lpud/uR.uhwzF.u7wiOokrnPnFzOz5UN8IFReNIai6WJa'),(4,'admin1@gmail.com','$2a$10$Rb5.a6OcftFiQq4XpX4jAOxibbEbTRVRyoZjZowaqc72gmQ8GFMRi'),(5,'supriya@gmail.com','$2a$10$tsISImcE/NXiybEXuEQ7t.bynJn1ysu8iC/WI.5z2lZozV4cnwpoO'),(6,'ganesh@gmail.com','$2a$10$Pki.fBSyY/VuGal3fi9c/O1FD5KM9xhuoRsmhwoMR9DsAmtaPt/oy');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whatsapp_logs`
--

LOCK TABLES `whatsapp_logs` WRITE;
/*!40000 ALTER TABLE `whatsapp_logs` DISABLE KEYS */;
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

-- Dump completed on 2025-07-08 11:32:11
