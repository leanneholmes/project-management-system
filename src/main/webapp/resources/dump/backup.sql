-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: pms
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Current Database: `pms`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pms` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `pms`;

--
-- Table structure for table `budget`
--

DROP TABLE IF EXISTS `budget`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `budget` (
  `budget_id` int NOT NULL AUTO_INCREMENT,
  `budget_year` int NOT NULL,
  `budget_JS` float NOT NULL,
  `budget_SS` float NOT NULL,
  `budget_DS` float NOT NULL,
  `budget_P1` float NOT NULL,
  `budget_P2` float NOT NULL,
  `budget_P3` float NOT NULL,
  `budget_P4` float NOT NULL,
  `budget_P5` float NOT NULL,
  `budget_P6` float NOT NULL,
  PRIMARY KEY (`budget_id`),
  UNIQUE KEY `budget_id` (`budget_id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budget`
--

LOCK TABLES `budget` WRITE;
/*!40000 ALTER TABLE `budget` DISABLE KEYS */;
INSERT INTO `budget` VALUES (3,2023,0,0,0,0,0,0,0,0,0),(4,2023,0,0,0,0,0,0,0,0,0),(13,2023,0,0,0,0,0,0,0,0,0),(14,2023,0,0,0,0,0,0,0,0,0),(15,2023,0,0,0,0,0,0,0,0,0),(16,2023,0,0,0,0,0,0,0,0,0),(97,2023,0,0,0,0,0,0,0,0,0),(98,2023,0,0,0,0,0,0,0,0,0),(99,2023,0,0,0,0,0,0,0,0,0),(100,2023,0,0,0,0,0,0,0,0,0),(103,2023,0,0,0,0,0,0,0,0,0),(104,2023,0,0,0,0,0,0,0,0,0),(105,2023,0,0,0,0,0,0,0,0,0),(106,2023,0,0,0,0,0,0,0,0,0),(107,2023,0,0,0,0,0,0,123,0,0),(108,2023,0,0,0,0,0,0,123,0,0),(109,2023,0,0,0,0,0,0,123,0,0),(110,2023,0,0,0,0,0,0,123,0,0),(111,2023,0,0,0,0,0,0,0,0,0),(112,2023,0,0,0,0,0,0,0,0,0),(113,2023,0,0,0,0,0,0,0,0,0),(114,2023,0,0,0,0,0,0,0,0,0);
/*!40000 ALTER TABLE `budget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `emp_number` int NOT NULL AUTO_INCREMENT,
  `emp_username` varchar(255) NOT NULL,
  `emp_password` varchar(255) NOT NULL,
  `emp_name` varchar(255) NOT NULL,
  `emp_type` varchar(255) NOT NULL,
  `emp_deactivated` bit(1) NOT NULL DEFAULT b'0',
  `emp_overtime` int NOT NULL,
  `emp_flextime` int NOT NULL,
  `emp_grade_id` int NOT NULL,
  `emp_supervisor_id` int DEFAULT NULL,
  `emp_approver_id` int DEFAULT NULL,
  PRIMARY KEY (`emp_number`),
  UNIQUE KEY `emp_number` (`emp_number`),
  KEY `emp_supervisor_id` (`emp_supervisor_id`),
  KEY `emp_approver_id` (`emp_approver_id`),
  KEY `emp_grade_id` (`emp_grade_id`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`emp_supervisor_id`) REFERENCES `employee` (`emp_number`),
  CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`emp_approver_id`) REFERENCES `employee` (`emp_number`),
  CONSTRAINT `employee_ibfk_3` FOREIGN KEY (`emp_grade_id`) REFERENCES `paygrade` (`grade_id`),
  CONSTRAINT `check_emp_type` CHECK ((`emp_type` in (_cp850'Employee',_cp850'Admin',_cp850'HR',_cp850'PM')))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'blink','temp','Bruce Link','Admin',_binary '\0',0,0,9,NULL,1),(2,'nhughes','temp','Nick Hughes','PM',_binary '\0',0,0,9,1,1),(3,'maximoose','temp','Max Joe','HR',_binary '\0',0,0,5,2,2),(4,'benG','temp','Benjamin Friedman','Employee',_binary '\0',0,0,5,2,2),(5,'pm','temp','manager','PM',_binary '\0',0,0,9,1,1),(6,'emp1','temp','employee1','Employee',_binary '\0',0,0,5,5,5),(7,'emp2','temp','employee2','Employee',_binary '\0',0,0,5,5,5);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_project`
--

DROP TABLE IF EXISTS `employee_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_project` (
  `ep_id` int NOT NULL AUTO_INCREMENT,
  `ep_emp_id` int NOT NULL,
  `ep_project_id` int NOT NULL,
  PRIMARY KEY (`ep_id`),
  UNIQUE KEY `ep_id` (`ep_id`),
  KEY `ep_emp_id` (`ep_emp_id`),
  KEY `ep_project_id` (`ep_project_id`),
  CONSTRAINT `employee_project_ibfk_1` FOREIGN KEY (`ep_emp_id`) REFERENCES `employee` (`emp_number`) ON DELETE CASCADE,
  CONSTRAINT `employee_project_ibfk_2` FOREIGN KEY (`ep_project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_project`
--

LOCK TABLES `employee_project` WRITE;
/*!40000 ALTER TABLE `employee_project` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_work_package`
--

DROP TABLE IF EXISTS `employee_work_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_work_package` (
  `ewp_id` int NOT NULL AUTO_INCREMENT,
  `ewp_emp_id` int NOT NULL,
  `ewp_wp_id` varchar(512) NOT NULL,
  PRIMARY KEY (`ewp_id`),
  UNIQUE KEY `ewp_id` (`ewp_id`),
  KEY `ewp_emp_id` (`ewp_emp_id`),
  KEY `ewp_wp_id` (`ewp_wp_id`),
  CONSTRAINT `employee_work_package_ibfk_1` FOREIGN KEY (`ewp_emp_id`) REFERENCES `employee` (`emp_number`) ON DELETE CASCADE,
  CONSTRAINT `employee_work_package_ibfk_2` FOREIGN KEY (`ewp_wp_id`) REFERENCES `work_package` (`wp_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_work_package`
--

LOCK TABLES `employee_work_package` WRITE;
/*!40000 ALTER TABLE `employee_work_package` DISABLE KEYS */;
INSERT INTO `employee_work_package` VALUES (1,3,'P1WP1'),(2,4,'P1WP1'),(3,6,'P1WP1'),(4,6,'P1WP2'),(6,6,'P1WP4'),(7,7,'P1WP5'),(8,7,'P1WP6'),(9,3,'P1WP2'),(10,4,'P1WP2');
/*!40000 ALTER TABLE `employee_work_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paygrade`
--

DROP TABLE IF EXISTS `paygrade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paygrade` (
  `grade_id` int NOT NULL AUTO_INCREMENT,
  `grade_name` varchar(255) NOT NULL,
  `grade_cost` float NOT NULL,
  `grade_year` date NOT NULL,
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `grade_id` (`grade_id`),
  CONSTRAINT `check_grade_name` CHECK ((`grade_name` in (_cp850'JS',_cp850'SS',_cp850'DS',_cp850'P1',_cp850'P2',_cp850'P3',_cp850'P4',_cp850'P5',_cp850'P6')))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paygrade`
--

LOCK TABLES `paygrade` WRITE;
/*!40000 ALTER TABLE `paygrade` DISABLE KEYS */;
INSERT INTO `paygrade` VALUES (1,'JS',200,'2023-01-01'),(2,'SS',210,'2023-01-01'),(3,'DS',220,'2023-01-01'),(4,'P1',230,'2023-01-01'),(5,'P2',240,'2023-01-01'),(6,'P3',250,'2023-01-01'),(7,'P4',260,'2023-01-01'),(8,'P5',270,'2023-01-01'),(9,'P6',280,'2023-01-01');
/*!40000 ALTER TABLE `paygrade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `project_id` int NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) NOT NULL,
  `project_description` varchar(255) DEFAULT NULL,
  `project_allocated_budget` float DEFAULT NULL,
  `project_manager_number` int NOT NULL,
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `project_id` (`project_id`),
  KEY `project_manager_number` (`project_manager_number`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`project_manager_number`) REFERENCES `employee` (`emp_number`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'Test Project','Test Desc',1000,2),(2,'Test Project2','Test Desc',1000,5);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timesheet`
--

DROP TABLE IF EXISTS `timesheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `timesheet` (
  `timesheet_id` int NOT NULL AUTO_INCREMENT,
  `timesheet_status` varchar(255) NOT NULL,
  `timesheet_end_date` date NOT NULL,
  `timesheet_num_rows` int NOT NULL DEFAULT '0',
  `timesheet_signed_date` date DEFAULT NULL,
  `timesheet_response_date` date DEFAULT NULL,
  `timesheet_comments` varchar(512) DEFAULT NULL,
  `timesheet_emp` int DEFAULT NULL,
  `timesheet_emp_signature` blob,
  `timesheet_emp_name` varchar(255) DEFAULT NULL,
  `timesheet_approver` int DEFAULT NULL,
  `timesheet_approver_signature` blob,
  `timesheet_approver_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`timesheet_id`),
  UNIQUE KEY `timesheet_id` (`timesheet_id`),
  UNIQUE KEY `timesheet_emp` (`timesheet_emp`,`timesheet_end_date`),
  KEY `timesheet_approver` (`timesheet_approver`),
  CONSTRAINT `timesheet_ibfk_1` FOREIGN KEY (`timesheet_emp`) REFERENCES `employee` (`emp_number`) ON DELETE SET NULL,
  CONSTRAINT `timesheet_ibfk_2` FOREIGN KEY (`timesheet_approver`) REFERENCES `employee` (`emp_number`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timesheet`
--

LOCK TABLES `timesheet` WRITE;
/*!40000 ALTER TABLE `timesheet` DISABLE KEYS */;
INSERT INTO `timesheet` VALUES (1,'Draft','2023-04-07',0,NULL,NULL,NULL,2,NULL,'Nick Hughes',NULL,NULL,NULL),(2,'Pending','2023-04-28',0,'2023-04-06',NULL,NULL,4,_binary '{\"lines\":[[[131.44,79.8],[131.44,78.8],[131.44,78.8],[131.44,77.8],[130.44,76.8],[129.44,76.8],[128.44,75.8],[127.44,75.8],[127.44,73.8],[127.44,72.8],[126.44,72.8],[125.44,72.8],[123.44,70.8],[123.44,68.8],[122.44,68.8],[122.44,68.8],[121.44,68.8],[120.44,67.8],[119.44,66.8],[118.44,65.8],[117.44,64.8],[117.44,63.8],[115.44,62.8],[115.44,62.8],[114.44,60.8],[111.44,58.8],[111.44,57.8],[108.44,56.8],[107.44,54.8],[107.44,52.8],[107.44,52.8],[107.44,51.8],[107.44,50.8],[107.44,49.8],[107.44,48.8],[107.44,48.8],[107.44,46.8],[107.44,45.8],[108.44,44.8],[109.44,43.8],[110.44,42.8],[110.44,41.8],[111.44,41.8],[111.44,40.8],[112.44,39.8],[113.44,39.8],[115.44,38.8],[115.44,37.8],[116.44,37.8],[117.44,37.8],[117.44,36.8],[119.44,36.8],[119.44,36.8],[119.44,36.8],[119.44,35.8],[120.44,34.8],[121.44,33.8],[122.44,32.8],[123.44,32.8],[124.44,30.8],[127.44,27.8],[129.44,23.8],[131.44,20.8],[132.44,18.8],[135.44,17.8],[135.44,16.8],[136.44,16.8],[137.44,16.8],[138.44,16.8],[139.44,15.8],[140.44,15.8],[141.44,15.8],[143.44,15.8],[143.44,15.8],[145.44,15.8],[147.44,15.8],[148.44,15.8],[149.44,15.8],[151.44,16.8],[151.44,16.8],[153.44,17.8],[154.44,17.8],[155.44,18.8],[156.44,20.8],[157.44,20.8],[158.44,20.8],[159.44,20.8],[159.44,22.8],[162.44,22.8],[163.44,23.8],[164.44,24.8],[166.44,24.8],[167.44,24.8],[167.44,25.8],[169.44,26.8],[171.44,27.8],[173.44,27.8],[173.44,28.8],[175.44,28.8],[176.44,29.8],[177.44,30.8],[178.44,31.8],[181.44,36.8],[183.44,37.8],[183.44,40.8],[183.44,42.8],[183.44,45.8],[183.44,48.8],[183.44,50.8],[183.44,51.8],[183.44,52.8],[183.44,52.8],[183.44,53.8],[183.44,54.8],[183.44,55.8],[183.44,55.8],[182.44,56.8],[180.44,56.8],[179.44,57.8],[179.44,57.8],[178.44,58.8],[177.44,58.8],[176.44,60.8],[175.44,60.8],[174.44,60.8],[173.44,61.8],[171.44,62.8],[171.44,63.8],[171.44,64.8],[169.44,64.8],[168.44,65.8],[167.44,66.8],[167.44,67.8],[166.44,67.8],[165.44,68.8],[164.44,68.8],[163.44,69.8],[163.44,69.8],[162.44,69.8],[161.44,69.8],[160.44,70.8],[159.44,70.8],[159.44,70.8],[159.44,71.8],[157.44,72.8],[156.44,72.8],[155.44,72.8],[155.44,72.8],[154.44,72.8],[153.44,72.8],[151.44,72.8],[151.44,72.8],[150.44,72.8],[148.44,72.8],[147.44,72.8],[147.44,72.8],[145.44,72.8],[144.44,72.8],[143.44,72.8],[143.44,72.8],[142.44,72.8],[141.44,72.8],[140.44,72.8],[139.44,72.8],[139.44,72.8]],[[132.44,51.8],[133.44,52.8],[134.44,52.8],[135.44,52.8],[135.44,53.8],[135.44,53.8],[136.44,53.8],[138.44,53.8],[139.44,53.8],[139.44,53.8],[139.44,54.8],[140.44,54.8],[141.44,54.8],[143.44,54.8],[143.44,55.8],[143.44,55.8],[144.44,55.8],[145.44,55.8],[146.44,55.8],[147.44,55.8],[147.44,54.8],[148.44,54.8],[149.44,54.8],[149.44,53.8],[150.44,53.8],[150.44,52.8],[151.44,52.8],[151.44,52.8],[152.44,52.8],[152.44,51.8],[153.44,51.8],[153.44,50.8],[154.44,50.8],[154.44,49.8],[155.44,49.8],[155.44,48.8],[155.44,48.8]],[[138.44,32.8],[138.44,33.8],[138.44,34.8],[139.44,34.8],[139.44,35.8]],[[150.44,36.8],[150.44,38.8]]]}','Benjamin Friedman',NULL,NULL,NULL),(3,'Draft','2023-04-21',0,NULL,NULL,NULL,4,NULL,'Benjamin Friedman',NULL,NULL,NULL),(4,'Approved','2023-04-21',1,'2023-04-06','2023-04-06',NULL,3,_binary '{\"lines\":[[[10.59,25.6],[10.59,25.6],[10.59,26.6],[10.59,27.6],[10.59,28.6],[10.59,29.6],[10.59,29.6],[10.59,30.6],[10.59,31.6],[10.59,32.6],[10.59,33.6],[10.59,33.6],[10.59,34.6],[10.59,35.6],[10.59,36.6],[10.59,39.6],[10.59,41.6],[10.59,43.6],[10.59,44.6],[10.59,45.6],[10.59,46.6],[10.59,49.6],[10.59,49.6],[10.59,51.6],[10.59,53.6],[10.59,53.6],[10.59,55.6],[10.59,56.6],[10.59,57.6],[10.59,57.6],[10.59,58.6],[10.59,59.6],[10.59,60.6],[10.59,61.6],[10.59,62.6],[10.59,63.6],[10.59,64.6],[10.59,65.6],[10.59,65.6],[10.59,67.6],[10.59,68.6],[10.59,69.6],[10.59,69.6],[10.59,71.6],[10.59,73.6],[10.59,73.6],[10.59,74.6],[10.59,75.6],[10.59,77.6],[10.59,77.6],[10.59,76.6]],[[12.59,25.6],[13.59,25.6],[14.59,25.6],[14.59,25.6],[16.59,25.6],[17.59,25.6],[18.59,26.6],[21.59,26.6],[22.59,27.6],[22.59,27.6],[24.59,28.6],[26.59,28.6],[26.59,28.6],[27.59,29.6],[28.59,29.6],[29.59,29.6],[30.59,29.6],[30.59,29.6],[31.59,29.6],[32.59,29.6],[33.59,29.6],[34.59,29.6],[34.59,29.6],[35.59,29.6],[36.59,29.6],[38.59,29.6],[38.59,29.6],[39.59,29.6],[40.59,29.6],[41.59,29.6],[42.59,29.6],[42.59,29.6],[43.59,29.6],[44.59,29.6],[45.59,29.6],[46.59,29.6],[46.59,29.6],[46.59,29.6],[46.59,30.6]],[[15.59,49.6],[17.59,49.6],[18.59,49.6],[18.59,49.6],[19.59,49.6],[20.59,49.6],[21.59,49.6],[22.59,49.6],[22.59,49.6],[24.59,49.6],[25.59,49.6],[26.59,49.6],[27.59,49.6],[28.59,49.6],[29.59,49.6],[30.59,49.6],[30.59,49.6],[31.59,49.6],[32.59,49.6],[33.59,49.6],[34.59,49.6],[34.59,49.6],[35.59,49.6],[36.59,49.6],[37.59,49.6],[38.59,49.6],[38.59,49.6],[39.59,49.6],[40.59,49.6],[41.59,49.6],[41.59,50.6]],[[49.59,53.6],[48.59,54.6],[47.59,54.6],[46.59,54.6],[46.59,55.6],[46.59,55.6],[45.59,56.6],[44.59,56.6],[43.59,57.6],[42.59,57.6],[42.59,57.6],[42.59,57.6],[42.59,58.6],[41.59,58.6],[41.59,59.6],[39.59,59.6],[39.59,60.6],[38.59,60.6],[38.59,61.6],[38.59,61.6],[38.59,62.6],[38.59,63.6],[38.59,64.6],[40.59,64.6],[40.59,65.6],[40.59,65.6],[41.59,65.6],[41.59,66.6],[41.59,68.6],[42.59,69.6],[42.59,69.6],[42.59,69.6],[42.59,70.6],[43.59,70.6],[43.59,71.6],[43.59,72.6],[44.59,72.6],[44.59,73.6],[45.59,73.6],[46.59,73.6],[46.59,74.6],[46.59,74.6],[46.59,75.6],[47.59,75.6],[48.59,75.6],[49.59,76.6],[50.59,77.6],[50.59,77.6],[50.59,77.6],[51.59,77.6],[52.59,77.6],[52.59,78.6],[54.59,78.6],[54.59,78.6],[55.59,78.6],[56.59,78.6],[58.59,78.6],[58.59,78.6],[60.59,78.6],[61.59,78.6],[62.59,77.6],[63.59,76.6],[64.59,76.6],[65.59,75.6],[65.59,74.6],[66.59,74.6],[67.59,73.6],[67.59,73.6],[67.59,71.6],[68.59,71.6],[68.59,70.6],[69.59,70.6]],[[65.59,52.6],[64.59,52.6],[63.59,52.6],[62.59,52.6],[62.59,52.6],[61.59,52.6],[60.59,52.6],[59.59,52.6],[58.59,52.6],[58.59,52.6],[57.59,52.6],[56.59,52.6],[55.59,52.6],[55.59,53.6],[54.59,53.6],[54.59,53.6],[53.59,53.6],[52.59,53.6],[51.59,53.6]],[[82.59,27.6],[82.59,28.6],[82.59,30.6],[82.59,36.6],[83.59,43.6],[84.59,47.6],[85.59,51.6],[85.59,53.6],[85.59,54.6],[85.59,55.6],[85.59,57.6],[85.59,57.6],[85.59,58.6],[85.59,59.6],[85.59,61.6],[85.59,61.6],[85.59,62.6],[85.59,64.6],[85.59,65.6],[85.59,65.6],[85.59,66.6],[85.59,67.6],[85.59,68.6],[85.59,69.6],[85.59,69.6],[85.59,70.6],[85.59,71.6],[85.59,72.6],[85.59,73.6],[85.59,73.6],[85.59,74.6],[85.59,76.6],[85.59,77.6],[85.59,78.6],[86.59,80.6],[86.59,81.6],[86.59,80.6],[86.59,78.6],[85.59,75.6],[85.59,73.6]],[[86.59,60.6],[86.59,59.6],[87.59,58.6],[90.59,56.6],[93.59,51.6],[95.59,46.6],[98.59,43.6],[99.59,41.6],[100.59,41.6],[101.59,40.6],[102.59,40.6],[103.59,39.6],[104.59,39.6],[105.59,39.6],[105.59,38.6],[106.59,38.6],[106.59,37.6],[105.59,37.6]],[[90.59,57.6],[90.59,57.6],[90.59,57.6],[90.59,58.6],[91.59,58.6],[92.59,58.6],[93.59,58.6],[94.59,58.6],[94.59,59.6],[94.59,59.6],[95.59,61.6],[97.59,61.6],[98.59,62.6],[98.59,63.6],[98.59,64.6],[98.59,65.6],[99.59,65.6],[99.59,66.6],[100.59,67.6],[101.59,67.6],[101.59,68.6],[102.59,69.6],[102.59,70.6],[103.59,71.6],[103.59,73.6],[104.59,73.6],[104.59,73.6],[104.59,74.6],[104.59,75.6],[105.59,75.6],[105.59,76.6],[106.59,76.6],[106.59,77.6],[107.59,77.6]],[[137.59,27.6],[137.59,28.6],[138.59,28.6],[138.59,29.6],[140.59,30.6],[141.59,30.6],[142.59,32.6],[142.59,32.6],[142.59,33.6],[143.59,33.6],[144.59,34.6],[146.59,35.6],[146.59,36.6],[146.59,37.6],[148.59,38.6],[149.59,39.6],[149.59,41.6],[150.59,42.6],[151.59,43.6],[151.59,44.6],[152.59,44.6],[152.59,45.6],[153.59,45.6],[153.59,46.6],[153.59,47.6],[154.59,47.6],[154.59,48.6]],[[170.59,27.6],[170.59,28.6],[170.59,29.6],[168.59,32.6],[167.59,34.6],[165.59,37.6],[163.59,41.6],[162.59,41.6],[162.59,43.6],[162.59,43.6],[162.59,44.6],[160.59,44.6],[160.59,45.6],[160.59,45.6],[159.59,46.6],[159.59,47.6],[158.59,47.6],[158.59,48.6],[158.59,48.6],[158.59,49.6],[157.59,49.6],[157.59,50.6],[156.59,50.6]],[[156.59,50.6],[156.59,50.6],[156.59,51.6],[156.59,52.6],[156.59,53.6],[156.59,53.6],[156.59,54.6],[156.59,55.6],[156.59,56.6],[156.59,57.6],[156.59,57.6],[156.59,59.6],[156.59,60.6],[156.59,61.6],[156.59,62.6],[156.59,64.6],[156.59,65.6],[156.59,66.6],[156.59,67.6],[156.59,68.6],[156.59,69.6],[156.59,69.6],[156.59,70.6],[156.59,71.6],[156.59,72.6],[156.59,73.6],[156.59,74.6],[155.59,74.6]],[[185.59,59.6],[186.59,59.6],[186.59,59.6],[187.59,59.6],[188.59,59.6],[190.59,59.6],[191.59,59.6],[193.59,59.6],[194.59,59.6],[196.59,59.6],[197.59,59.6],[198.59,59.6],[198.59,59.6],[199.59,59.6],[200.59,59.6],[201.59,59.6],[202.59,59.6],[202.59,59.6],[204.59,59.6],[204.59,58.6],[204.59,57.6],[205.59,57.6],[205.59,57.6],[205.59,56.6],[205.59,55.6],[205.59,53.6],[205.59,53.6],[205.59,52.6],[205.59,51.6],[203.59,51.6],[202.59,50.6],[202.59,50.6],[200.59,50.6],[199.59,50.6],[198.59,50.6],[198.59,50.6],[197.59,50.6],[197.59,49.6],[196.59,49.6],[195.59,49.6],[194.59,49.6],[194.59,49.6],[193.59,49.6],[192.59,49.6],[191.59,49.6],[191.59,50.6],[190.59,50.6],[190.59,51.6],[190.59,51.6],[190.59,52.6],[190.59,53.6],[189.59,53.6],[189.59,54.6],[188.59,54.6],[188.59,55.6],[188.59,57.6],[188.59,57.6],[188.59,58.6],[188.59,59.6],[188.59,60.6],[187.59,60.6],[187.59,61.6],[187.59,61.6],[187.59,63.6],[187.59,64.6],[187.59,65.6],[188.59,65.6],[189.59,65.6],[189.59,66.6],[190.59,66.6],[190.59,66.6],[191.59,66.6],[192.59,66.6],[193.59,66.6],[194.59,66.6],[194.59,66.6],[195.59,66.6],[195.59,67.6],[196.59,67.6],[197.59,67.6],[197.59,68.6],[198.59,68.6],[198.59,69.6],[198.59,69.6],[199.59,69.6],[200.59,69.6],[201.59,69.6],[202.59,69.6],[202.59,69.6],[204.59,69.6],[205.59,69.6],[206.59,69.6]],[[234.59,53.6],[234.59,53.6],[233.59,52.6],[232.59,52.6],[231.59,52.6],[230.59,52.6],[229.59,52.6],[229.59,51.6],[228.59,51.6],[227.59,51.6],[226.59,51.6],[226.59,51.6],[226.59,50.6],[225.59,50.6],[224.59,50.6],[223.59,51.6],[223.59,53.6],[222.59,53.6],[222.59,54.6],[222.59,55.6],[222.59,56.6],[222.59,57.6],[222.59,57.6],[222.59,58.6],[222.59,59.6],[222.59,60.6],[222.59,61.6],[223.59,61.6],[224.59,61.6],[225.59,61.6],[226.59,61.6],[226.59,61.6],[227.59,61.6],[228.59,61.6],[229.59,61.6],[230.59,61.6],[230.59,61.6],[231.59,61.6],[231.59,61.6],[232.59,62.6],[233.59,63.6],[234.59,64.6],[234.59,64.6],[234.59,65.6],[235.59,65.6],[236.59,66.6],[236.59,68.6],[237.59,68.6],[237.59,69.6],[238.59,69.6],[238.59,69.6],[238.59,70.6],[238.59,71.6],[238.59,72.6],[238.59,73.6],[238.59,73.6],[238.59,73.6],[238.59,74.6],[237.59,74.6],[237.59,75.6],[236.59,75.6],[235.59,75.6],[234.59,75.6],[234.59,75.6],[233.59,75.6],[232.59,75.6],[231.59,75.6],[230.59,75.6],[230.59,75.6],[229.59,75.6],[228.59,75.6],[227.59,75.6],[226.59,75.6],[226.59,75.6],[225.59,74.6],[224.59,74.6],[224.59,73.6],[223.59,73.6],[223.59,73.6],[223.59,72.6],[223.59,71.6],[223.59,70.6],[223.59,69.6]]]}','Max Joe',2,_binary '{\"lines\":[[[59.59,30.2],[56.59,32.2],[54.59,33.2],[50.59,37.2],[36.59,48.2],[28.59,59.2],[25.59,66.2],[24.59,72.2],[24.59,74.2],[29.59,78.2],[35.59,81.2],[39.59,83.2],[44.59,88.2],[47.59,90.2],[48.59,92.2],[52.59,93.2],[59.59,93.2],[74.59,92.2],[96.59,90.2],[104.59,86.2],[109.59,83.2],[111.59,82.2],[113.59,82.2],[113.59,81.2],[112.59,81.2],[111.59,80.2],[105.59,75.2],[102.59,72.2],[100.59,71.2],[98.59,70.2],[98.59,70.2],[97.59,70.2],[98.59,70.2],[98.59,70.2],[104.59,66.2],[107.59,65.2],[110.59,65.2],[114.59,65.2],[117.59,65.2],[121.59,65.2],[124.59,65.2],[125.59,65.2],[126.59,65.2],[126.59,67.2],[127.59,68.2],[130.59,69.2],[130.59,69.2],[131.59,69.2],[131.59,69.2],[132.59,71.2],[132.59,73.2],[132.59,76.2],[133.59,79.2],[134.59,83.2],[137.59,92.2],[138.59,96.2],[139.59,99.2],[140.59,99.2],[141.59,99.2],[141.59,100.2]]]}','Nick Hughes');
/*!40000 ALTER TABLE `timesheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timesheet_row`
--

DROP TABLE IF EXISTS `timesheet_row`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `timesheet_row` (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `row_num` int DEFAULT NULL,
  `row_sat_hours` float DEFAULT '0',
  `row_sun_hours` float DEFAULT '0',
  `row_mon_hours` float DEFAULT '0',
  `row_tue_hours` float DEFAULT '0',
  `row_wed_hours` float DEFAULT '0',
  `row_thu_hours` float DEFAULT '0',
  `row_fri_hours` float DEFAULT '0',
  `row_notes` varchar(256) DEFAULT NULL,
  `row_timesheet_id` int NOT NULL,
  `row_project_id` int NOT NULL,
  `row_wp_id` varchar(255) NOT NULL,
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `row_id` (`row_id`),
  UNIQUE KEY `row_project_id` (`row_project_id`,`row_wp_id`),
  KEY `row_timesheet_id` (`row_timesheet_id`),
  KEY `row_wp_id` (`row_wp_id`),
  CONSTRAINT `timesheet_row_ibfk_1` FOREIGN KEY (`row_timesheet_id`) REFERENCES `timesheet` (`timesheet_id`) ON DELETE CASCADE,
  CONSTRAINT `timesheet_row_ibfk_2` FOREIGN KEY (`row_project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `timesheet_row_ibfk_3` FOREIGN KEY (`row_wp_id`) REFERENCES `work_package` (`wp_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timesheet_row`
--

LOCK TABLES `timesheet_row` WRITE;
/*!40000 ALTER TABLE `timesheet_row` DISABLE KEYS */;
INSERT INTO `timesheet_row` VALUES (1,0,11,0,0,11,0,0,0,NULL,4,1,'P1WP1');
/*!40000 ALTER TABLE `timesheet_row` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bugbuster`@`%`*/ /*!50003 TRIGGER `after_timesheet_row_insertion` AFTER INSERT ON `timesheet_row` FOR EACH ROW BEGIN
	DECLARE timesheetID INT;
    SET timesheetID = NEW.row_timesheet_id;

	
UPDATE timesheet
SET timesheet_num_rows = timesheet_num_rows + 1
WHERE timesheet_id = timesheetID;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bugbuster`@`%`*/ /*!50003 TRIGGER `after_timesheet_row_deletion` AFTER DELETE ON `timesheet_row` FOR EACH ROW BEGIN
	DECLARE timesheetID INT;
    SET timesheetID = OLD.row_timesheet_id;

	
UPDATE timesheet
SET timesheet_num_rows = timesheet_num_rows - 1
WHERE timesheet_id = timesheetID;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `work_package`
--

DROP TABLE IF EXISTS `work_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_package` (
  `wp_id` varchar(512) NOT NULL,
  `wp_name` varchar(255) NOT NULL,
  `wp_start_date` date DEFAULT NULL,
  `wp_end_date` date DEFAULT NULL,
  `wp_project_id` int NOT NULL,
  `wp_resp_eng_id` int NOT NULL,
  `wp_eng_est_id` int NOT NULL,
  `wp_rolling_est_id` int NOT NULL,
  `wp_parent_id` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`wp_id`),
  UNIQUE KEY `wp_id` (`wp_id`),
  UNIQUE KEY `wp_id_2` (`wp_id`,`wp_project_id`),
  KEY `wp_project_id` (`wp_project_id`),
  KEY `wp_resp_eng_id` (`wp_resp_eng_id`),
  KEY `wp_eng_est_id` (`wp_eng_est_id`),
  KEY `wp_rolling_est_id` (`wp_rolling_est_id`),
  KEY `wp_parent_id` (`wp_parent_id`),
  CONSTRAINT `work_package_ibfk_1` FOREIGN KEY (`wp_project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `work_package_ibfk_2` FOREIGN KEY (`wp_resp_eng_id`) REFERENCES `employee` (`emp_number`) ON DELETE CASCADE,
  CONSTRAINT `work_package_ibfk_3` FOREIGN KEY (`wp_eng_est_id`) REFERENCES `budget` (`budget_id`) ON DELETE CASCADE,
  CONSTRAINT `work_package_ibfk_4` FOREIGN KEY (`wp_rolling_est_id`) REFERENCES `budget` (`budget_id`) ON DELETE CASCADE,
  CONSTRAINT `work_package_ibfk_5` FOREIGN KEY (`wp_parent_id`) REFERENCES `work_package` (`wp_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_package`
--

LOCK TABLES `work_package` WRITE;
/*!40000 ALTER TABLE `work_package` DISABLE KEYS */;
INSERT INTO `work_package` VALUES ('P1WP1','Test WP 1','2023-03-01','2023-04-01',1,2,99,100,NULL),('P1WP11','Test rec','2023-04-07','2023-04-28',1,2,97,98,'P1WP1'),('P1WP2','Test WP 2','2023-03-02','2023-04-02',1,2,3,4,NULL),('P1WP4','Test WP 4','2023-03-04','2023-04-04',1,2,109,110,NULL),('P1WP41','TEs ','2023-04-07','2023-04-21',1,2,107,108,'P1WP4'),('P1WP5','Test WP 5','2023-03-05','2023-04-05',1,2,105,106,NULL),('P1WP51','Test ','2023-04-07','2023-04-28',1,2,103,104,'P1WP5'),('P1WP6','Test WP 6','2023-03-06','2023-05-06',1,2,113,114,NULL),('P1WP61','Tes','2023-04-07','2023-04-28',1,2,111,112,'P1WP6'),('P1WP7','Test WP 7','2023-03-07','2023-05-07',1,2,13,14,NULL),('P1WP8','Test WP 8','2023-03-08','2023-05-08',1,2,15,16,NULL);
/*!40000 ALTER TABLE `work_package` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-06 22:56:49
