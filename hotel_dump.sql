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
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_items`
--

LOCK TABLES `bill_items` WRITE;
/*!40000 ALTER TABLE `bill_items` DISABLE KEYS */;
INSERT INTO `bill_items` VALUES (196,0,'Tea',2,20,10,100),(197,0,'Coffee',1,20,20,100),(200,0,'Tea',2,20,10,102),(201,0,'Coffee',1,20,20,102),(202,0,'Tea',2,20,10,103),(203,0,'Coffee',1,20,20,103),(204,0,'Tea',2,20,10,104),(205,0,'Coffee',1,20,20,104),(206,0,'Tea',2,20,10,105),(207,0,'Coffee',1,20,20,105),(220,0,'Tea',2,20,10,112),(221,0,'Coffee',1,20,20,112),(222,0,'Tea',2,20,10,113),(223,0,'Coffee',1,20,20,113);
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
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (100,'INV-1001','2025-07-14 15:47:36.972798',5,40,7.2,42.2,NULL,NULL),(102,'INV-1001','2025-07-14 17:10:32.187110',5,40,7.2,42.2,NULL,NULL),(103,'INV-1001','2025-07-15 11:57:37.643719',5,40,7.2,42.2,NULL,NULL),(104,'INV-1001','2025-07-15 12:20:12.491786',5,40,7.2,42.2,NULL,NULL),(105,'INV-1001','2025-07-15 12:41:31.743755',5,40,7.2,42.2,NULL,NULL),(112,'INV-1001','2025-07-17 14:53:13.567057',5,40,7.2,42.2,NULL,NULL),(113,'INV-1001','2025-07-17 16:20:53.569655',5,40,7.2,42.2,NULL,NULL);
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
INSERT INTO `customers` VALUES (1,NULL,NULL,'9860060771','Rahul Sharma'),(2,NULL,NULL,'9146747054','Ram'),(3,NULL,NULL,'5968741235','Poonam');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotels`
--

LOCK TABLES `hotels` WRITE;
/*!40000 ALTER TABLE `hotels` DISABLE KEYS */;
INSERT INTO `hotels` VALUES (1,'Sector 12, Connaught Place, New Delhi, 110001','2025-06-30 10:22:12.747921','sneha.elitehotel@example.com','y1','yas','Luxury',_binary '','7470541111','z','2025-07-14 11:08:24.273708'),(2,NULL,'2025-06-30 14:05:47.129647','taj@gmail.com','GSTT1234','Hotel Taj','Luxury',_binary '','9985947896','Rajeev','2025-06-30 14:05:47.129647'),(3,'pune','2025-07-06 17:47:10.295458','swaraj@gmail.com','p001','Swaraj','Luxury',_binary '','09146747054','raj','2025-07-06 17:47:10.303263'),(4,'','2025-07-07 12:24:05.674244','','','Rajv','',_binary '','','','2025-07-07 12:24:05.674244'),(5,'at post Tai/Dist ahamadnagar','2025-07-07 12:27:04.295803','vaibhavkadus@gmail.com','123','a','Luxury',_binary '','1234567890','b','2025-07-07 12:27:04.295803'),(6,'at post ','2025-07-10 14:35:10.960367','us3131@gmail.com','G1','a','Luxury',_binary '','7470540914','b','2025-07-10 14:35:10.960367');
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
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4kcfnm4xqk3rkrjpxaqx2rl7p` (`hotel_id`),
  KEY `FKdb050tk37qryv15hd932626th` (`user_id`),
  CONSTRAINT `FK4kcfnm4xqk3rkrjpxaqx2rl7p` FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`hotel_id`),
  CONSTRAINT `FKdb050tk37qryv15hd932626th` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (2,'Food',2000,1,_binary '','SOF100','Samosa 1',1,NULL),(3,'Beverage',10,1,_binary '','TEA001','Tea',100,NULL),(4,'Beverage',20,1,_binary '','COF001','Coffee',100,NULL),(6,'Beverage',10,2,_binary '','TEA001','Tea',100,NULL),(7,'Beverage',20,2,_binary '','COF001','Coffee',100,NULL),(12,'Food',2000,1,_binary '','1023','ab',1,NULL),(16,'Food',2500,1,_binary '','DBK001','Dal bati',10,NULL),(18,'Food',200,1,_binary '','KA001','kaju',10,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin12@taj.com','admin123'),(2,'ram@gmail.com','$2a$10$x93vwgiDva/l7FJVA7ipLOBJUbgt9NqdKFPAxD84h0nNAsgkIkPFy'),(3,'admin@gmail.com','$2a$10$ML1g9eY/Lpud/uR.uhwzF.u7wiOokrnPnFzOz5UN8IFReNIai6WJa'),(4,'admin1@gmail.com','$2a$10$Rb5.a6OcftFiQq4XpX4jAOxibbEbTRVRyoZjZowaqc72gmQ8GFMRi'),(5,'supriya@gmail.com','$2a$10$tsISImcE/NXiybEXuEQ7t.bynJn1ysu8iC/WI.5z2lZozV4cnwpoO'),(6,'ganesh@gmail.com','$2a$10$Pki.fBSyY/VuGal3fi9c/O1FD5KM9xhuoRsmhwoMR9DsAmtaPt/oy'),(7,'abhi555@gmail.com','$2a$10$6yVvrr0Ecjx55xpu4CDC/OpP.wqdZ//l6M0sf8aN7bBA6qcOoJC1G'),(9,'ram123@gmail.com','$2a$10$3O8bEwI9zwdRmU5yTIp31udRE4ij78O/jVKdFh4bkxcONLblQtG5m'),(10,'test@example.com','$2a$10$jc9v0I/QoksRFe2ZDOkDx.3tp2Wjl/SEdigxK3bDbcNHTKod5tzWO'),(11,'apitest@example.com','$2a$10$YgbWfM5DfSwiiHQ87r4KV.kfUMrRn0xcA20DSEvF7UrAd0SJfN93.'),(12,'amruta@gmail.com','$2a$10$/xLHYOW.nksx0zi3rmerAOaRgo.SPWKtk9GL8vceQ98Fg84pmXdB.'),(13,'poonam@gmail.com','$2a$10$qeEyfHKe3.BKwgbNPgSfqO/6fl8XCo7ylewgz9nsDLhTqMnpVX6JK');
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

-- Dump completed on 2025-07-17 16:20:55
