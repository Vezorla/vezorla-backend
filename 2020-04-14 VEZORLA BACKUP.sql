DROP TABLE IF EXISTS `spring_session`;
DROP TABLE IF EXISTS `spring_session_attributes`;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
);
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
);
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: vezorla
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `email` varchar(255) NOT NULL,
  `account_admin` bit(1) DEFAULT NULL,
  `account_type` char(1) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_confirmed` bit(1) DEFAULT NULL,
  `is_subscript` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_num` varchar(255) DEFAULT NULL,
  `pickup` bit(1) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `user_created` bit(1) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('aaron.warsylewicz@sait.ca',_binary '\0','C','111','Calgary','Canada','Aaron',_binary '\0',_binary '\0','Warsylewicz','AceiteDeOlivaVezorla105','4035551212',_binary '\0','T2J1R1','AB',_binary ''),('aaron@email.ca',_binary '\0','C',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,'123',NULL,_binary '\0',NULL,NULL,_binary ''),('aaron@mailinator.com',_binary '\0','C','','','','Aaron',_binary '\0',NULL,'Test',NULL,'4035551212',_binary '','T2J1R1','',_binary '\0'),('admin@email.ca',_binary '','A','Vezorla','Calgary','CA','Vezorla',_binary '',_binary '\0','Vezorla','123','4035871234',_binary '\0','T1T1T1','AB',_binary ''),('carlos@email.ca',_binary '\0','C',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,'123',NULL,_binary '\0',NULL,NULL,_binary ''),('jocelyn@email.ca',_binary '\0','C','60 Tree Blvd','Calgary','Canada','Jocelyn',_binary '\0',_binary '\0','Wegen','123','4037894561',_binary '\0','T6T7U7','AB',_binary ''),('johnathon@email.ca',_binary '\0','C','','','','Jonathon',_binary '\0',NULL,'K',NULL,'4031234567',_binary '','T1Y6Y6','',_binary '\0'),('matthew@email.ca',_binary '\0','C',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,'123',NULL,_binary '\0',NULL,NULL,_binary ''),('minh@email.ca',_binary '\0','C',NULL,NULL,NULL,NULL,_binary '\0',NULL,NULL,'123',NULL,_binary '\0',NULL,NULL,_binary '');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_discount`
--

DROP TABLE IF EXISTS `account_discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_discount` (
  `account_email` varchar(255) NOT NULL,
  `discount_code` varchar(255) NOT NULL,
  PRIMARY KEY (`account_email`,`discount_code`),
  KEY `FKqbyxiwccuw3sm5d7plyhodfg8` (`discount_code`),
  CONSTRAINT `FKqbyxiwccuw3sm5d7plyhodfg8` FOREIGN KEY (`discount_code`) REFERENCES `discount` (`code`),
  CONSTRAINT `FKsshirbwfkigbndgh01ovp7wlo` FOREIGN KEY (`account_email`) REFERENCES `account` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_discount`
--

LOCK TABLES `account_discount` WRITE;
/*!40000 ALTER TABLE `account_discount` DISABLE KEYS */;
INSERT INTO `account_discount` VALUES ('aaron.warsylewicz@sait.ca','FIRST'),('jocelyn@email.ca','FIRST'),('johnathon@email.ca','FIRST'),('aaron.warsylewicz@sait.ca','OLIVES'),('aaron@mailinator.com','OLIVES');
/*!40000 ALTER TABLE `account_discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `order_num` bigint NOT NULL AUTO_INCREMENT,
  `from_account` bit(1) DEFAULT NULL,
  `account_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`order_num`),
  KEY `FKfcu5t6ix2dc4g4prm54l5uoob` (`account_num`),
  CONSTRAINT `FKfcu5t6ix2dc4g4prm54l5uoob` FOREIGN KEY (`account_num`) REFERENCES `account` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,_binary '','admin@email.ca'),(2,_binary '','matthew@email.ca'),(3,_binary '','jocelyn@email.ca'),(4,_binary '','carlos@email.ca'),(5,_binary '','minh@email.ca'),(6,_binary '','aaron@email.ca'),(7,_binary '\0',NULL),(8,_binary '','jocelyn@email.ca'),(9,_binary '','jocelyn@email.ca'),(10,_binary '\0',NULL),(11,_binary '\0',NULL),(12,_binary '\0',NULL),(13,_binary '\0',NULL),(14,_binary '','aaron.warsylewicz@sait.ca'),(15,_binary '','aaron.warsylewicz@sait.ca');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `code` varchar(255) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `banner_message` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `is_highlighted` bit(1) DEFAULT NULL,
  `minimum_order_value` bigint DEFAULT NULL,
  `percent` float DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `product_prod_id` bigint DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FKbfw5vbhe9he5reqn8cn96dktn` (`product_prod_id`),
  CONSTRAINT `FKbfw5vbhe9he5reqn8cn96dktn` FOREIGN KEY (`product_prod_id`) REFERENCES `product` (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
INSERT INTO `discount` VALUES ('FIRST',_binary '','10% off your first purchase!','10% First Time Buying','2025-12-22 00:00:00.000000',_binary '',0,10,'2020-01-01 00:00:00.000000',NULL,NULL),('JANUARY',_binary '\0',NULL,'15% January Special','2020-01-31 00:00:00.000000',_binary '\0',0,15,'2020-01-01 00:00:00.000000',NULL,NULL),('OLIVES',_binary '',NULL,'20% Special','2020-08-31 00:00:00.000000',_binary '\0',0,20,'2020-01-01 00:00:00.000000',NULL,NULL);
/*!40000 ALTER TABLE `discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `invoice_num` bigint NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `discount` bigint DEFAULT NULL,
  `pickup` bit(1) DEFAULT NULL,
  `shipped` bit(1) DEFAULT NULL,
  `shipping_cost` bigint DEFAULT NULL,
  `subtotal` bigint DEFAULT NULL,
  `taxes` bigint DEFAULT NULL,
  `total` bigint DEFAULT NULL,
  `account_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`invoice_num`),
  KEY `FKsybykd8my21sid3a7alo4ycj6` (`account_num`),
  CONSTRAINT `FKsybykd8my21sid3a7alo4ycj6` FOREIGN KEY (`account_num`) REFERENCES `account` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,'2020-04-09',7132,_binary '',_binary '\0',0,64193,3209,67402,'johnathon@email.ca'),(2,'2020-04-09',19500,_binary '\0',_binary '\0',1000,175500,8775,185275,'jocelyn@email.ca'),(3,'2020-04-09',0,_binary '\0',_binary '\0',1000,15000,750,16750,'jocelyn@email.ca'),(4,'2020-04-14',5850,_binary '',_binary '\0',0,23400,1170,24570,'aaron@mailinator.com'),(5,'2020-04-14',5850,_binary '',_binary '\0',0,23400,1170,24570,'aaron@mailinator.com'),(6,'2020-04-14',5850,_binary '',_binary '\0',0,23400,1170,24570,'aaron@mailinator.com'),(7,'2020-04-14',2925,_binary '\0',_binary '\0',1000,26325,1316,28641,'aaron.warsylewicz@sait.ca'),(8,'2020-04-14',6000,_binary '\0',_binary '\0',1000,24000,1200,26200,'aaron.warsylewicz@sait.ca');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `line_item`
--

DROP TABLE IF EXISTS `line_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `line_item` (
  `line_num` bigint NOT NULL AUTO_INCREMENT,
  `current_name` varchar(255) DEFAULT NULL,
  `current_price` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `order_num` bigint DEFAULT NULL,
  `invoice_num` bigint DEFAULT NULL,
  `lot_num` varchar(255) DEFAULT NULL,
  `prod_num` bigint DEFAULT NULL,
  PRIMARY KEY (`line_num`),
  KEY `FKtdu7xf437jjxwdlg9f5sb9usl` (`order_num`),
  KEY `FKplbwociqe1ge9ykws5v4x1lrs` (`invoice_num`),
  KEY `FKlgxth7nve3qlrldbyly45oey3` (`lot_num`),
  KEY `FKrblqjkg45kk9kfltn251wfojn` (`prod_num`),
  CONSTRAINT `FKlgxth7nve3qlrldbyly45oey3` FOREIGN KEY (`lot_num`) REFERENCES `lot` (`lot_num`),
  CONSTRAINT `FKplbwociqe1ge9ykws5v4x1lrs` FOREIGN KEY (`invoice_num`) REFERENCES `invoice` (`invoice_num`),
  CONSTRAINT `FKrblqjkg45kk9kfltn251wfojn` FOREIGN KEY (`prod_num`) REFERENCES `product` (`prod_id`),
  CONSTRAINT `FKtdu7xf437jjxwdlg9f5sb9usl` FOREIGN KEY (`order_num`) REFERENCES `cart` (`order_num`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `line_item`
--

LOCK TABLES `line_item` WRITE;
/*!40000 ALTER TABLE `line_item` DISABLE KEYS */;
INSERT INTO `line_item` VALUES (1,'Extra Virgin Olive Oil ',15000,2,7,1,NULL,1),(2,'ORIGINAL Dark Chocolate',13775,3,7,1,NULL,5),(3,'Extra Virgin Olive Oil PICUAL',19500,10,3,2,NULL,4),(4,'Extra Virgin Olive Oil ',15000,1,8,3,NULL,1),(5,'Extra Virgin Olive Oil ROYAL',29250,1,10,4,NULL,2),(6,'Extra Virgin Olive Oil ROYAL',29250,1,11,5,NULL,2),(7,'Extra Virgin Olive Oil ROYAL',29250,1,12,6,NULL,2),(8,'Extra Virgin Olive Oil ROYAL',29250,1,13,7,NULL,2),(9,'Extra Virgin Olive Oil ',15000,2,14,8,NULL,1),(10,'Extra Virgin Olive Oil ',15000,2,15,NULL,NULL,1);
/*!40000 ALTER TABLE `line_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lot`
--

DROP TABLE IF EXISTS `lot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lot` (
  `lot_num` varchar(255) NOT NULL,
  `best_before` date DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `product_prod_id` bigint DEFAULT NULL,
  `purchase_order_po_num` bigint DEFAULT NULL,
  `warehouse_warehouse_num` bigint DEFAULT NULL,
  PRIMARY KEY (`lot_num`),
  KEY `FKdpg1svq8tu7ej0cyi6ewsb1m6` (`product_prod_id`),
  KEY `FK4kb5pg77n7b772bl13fheerqf` (`purchase_order_po_num`),
  KEY `FK9ct7tcgo9r43mxaepmma5gsde` (`warehouse_warehouse_num`),
  CONSTRAINT `FK4kb5pg77n7b772bl13fheerqf` FOREIGN KEY (`purchase_order_po_num`) REFERENCES `purchase_order` (`po_num`),
  CONSTRAINT `FK9ct7tcgo9r43mxaepmma5gsde` FOREIGN KEY (`warehouse_warehouse_num`) REFERENCES `warehouse` (`warehouse_num`),
  CONSTRAINT `FKdpg1svq8tu7ej0cyi6ewsb1m6` FOREIGN KEY (`product_prod_id`) REFERENCES `product` (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lot`
--

LOCK TABLES `lot` WRITE;
/*!40000 ALTER TABLE `lot` DISABLE KEYS */;
INSERT INTO `lot` VALUES ('1-1','2020-04-09',5000,95,1,1,1),('2-1','2020-04-09',2000,16,2,2,2),('3-1','2020-04-09',9000,0,4,3,1),('4-1','2020-04-09',3000,57,5,4,1);
/*!40000 ALTER TABLE `lot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `prod_id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `harvest_time` date DEFAULT NULL,
  `image_main` bigint DEFAULT NULL,
  `image_one` bigint DEFAULT NULL,
  `image_three` bigint DEFAULT NULL,
  `image_two` bigint DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `old_price` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `subdescription` varchar(255) DEFAULT NULL,
  `threshhold` int DEFAULT NULL,
  PRIMARY KEY (`prod_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,_binary '\0','3 Tins of 5 Litres. $50/Tin \nPre-order Now & Delivers on April - SPECIAL ENDS APRIL 12ND\nAVAILABLE ON APRIL 22ND 2020 (AS PER ESTIMATION ARRIVAL)\n\nType of Olive	100% Picual\n','2020-04-09',2,3,19,4,'Extra Virgin Olive Oil ',NULL,'15000','',0),(2,_binary '','15 Bottles of 500ml - $19.5/Bottle\n Pre-order Now & Delivers on April\nAVAILABLE ON APRIL 22ND 2020 (AS PER ESTIMATION ARRIVAL)','2020-04-09',17,6,NULL,7,'Extra Virgin Olive Oil ROYAL',NULL,'29250','',0),(4,_binary '','15 Bottles of 500ml - $13/Bottle\nPre-order Now & Delivers on April.\nAVAILABLE ON APRIL 22ND 2020 (AS PER ESTIMATION ARRIVAL)\n','2020-04-09',16,11,NULL,12,'Extra Virgin Olive Oil PICUAL',NULL,'19500','',0),(5,_binary '','25 Bars. $5.51/bar.\n6% extra virgin olive oil','2020-04-09',18,14,NULL,15,'ORIGINAL Dark Chocolate',NULL,'13775','',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order` (
  `po_num` bigint NOT NULL AUTO_INCREMENT,
  `date_ordered` date DEFAULT NULL,
  `date_received` date DEFAULT NULL,
  `received` bit(1) NOT NULL,
  PRIMARY KEY (`po_num`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
INSERT INTO `purchase_order` VALUES (1,'2020-04-09','2020-04-09',_binary '\0'),(2,'2020-04-09','2020-04-09',_binary '\0'),(3,'2020-04-09','2020-04-09',_binary '\0'),(4,'2020-04-09','2020-04-09',_binary '\0');
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse` (
  `warehouse_num` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `postal_code` varchar(255) NOT NULL,
  `province` varchar(255) NOT NULL,
  PRIMARY KEY (`warehouse_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
INSERT INTO `warehouse` VALUES (1,_binary '','Home','Calgary','4035871234','T2T2T2','AB'),(2,_binary '','Warehouse','Calgary','4037894561','T2T3T3','AB');
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-14 13:08:34
