-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for cinema
CREATE DATABASE IF NOT EXISTS `cinema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cinema`;

-- Dumping structure for table cinema.chairs
CREATE TABLE IF NOT EXISTS `chairs` (
  `id` int NOT NULL,
  `position` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.chairs: ~0 rows (approximately)

-- Dumping structure for table cinema.movies
CREATE TABLE IF NOT EXISTS `movies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age_limit` int DEFAULT NULL,
  `quality` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.movies: ~2 rows (approximately)
INSERT INTO `movies` (`id`, `name`, `image_url`, `description`, `age_limit`, `quality`) VALUES
	(7, 'iron man', NULL, 'hay vl', 12, 100),
	(8, 'a', NULL, 'adfasdf', 23, 11);

-- Dumping structure for table cinema.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL,
  `admin_id` int NOT NULL,
  `total` int NOT NULL,
  `user_id` int NOT NULL,
  `promotion_code_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.orders: ~0 rows (approximately)

-- Dumping structure for table cinema.order_details
CREATE TABLE IF NOT EXISTS `order_details` (
  `id` int NOT NULL,
  `tickets_id` int NOT NULL,
  `orders_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.order_details: ~0 rows (approximately)

-- Dumping structure for table cinema.price
CREATE TABLE IF NOT EXISTS `price` (
  `id` int NOT NULL AUTO_INCREMENT,
  `movies_id` int DEFAULT NULL,
  `schedules_id` int DEFAULT NULL,
  `price` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_price_movies` (`movies_id`),
  KEY `FK_price_schedules` (`schedules_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.price: ~2 rows (approximately)
INSERT INTO `price` (`id`, `movies_id`, `schedules_id`, `price`) VALUES
	(5, 7, 5, 1000000),
	(6, 8, 6, 23327);

-- Dumping structure for table cinema.promotion_code
CREATE TABLE IF NOT EXISTS `promotion_code` (
  `id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `point` int DEFAULT NULL,
  `type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `value` int DEFAULT NULL,
  `price` int DEFAULT NULL,
  `effect` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.promotion_code: ~2 rows (approximately)
INSERT INTO `promotion_code` (`id`, `point`, `type`, `value`, `price`, `effect`, `description`) VALUES
	('KMA1', 20, '%', 5, 10000, NULL, 'giảm 5% cho giá trị đơn hàng'),
	('KMA3', 10, '%', 6, 5000, NULL, 'GIảm 6% cho đơn hàng từ 5000 vnd');

-- Dumping structure for table cinema.rooms
CREATE TABLE IF NOT EXISTS `rooms` (
  `id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.rooms: ~0 rows (approximately)

-- Dumping structure for table cinema.room_chairs
CREATE TABLE IF NOT EXISTS `room_chairs` (
  `id` int NOT NULL,
  `chair_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.room_chairs: ~0 rows (approximately)

-- Dumping structure for table cinema.room_movies_schedules
CREATE TABLE IF NOT EXISTS `room_movies_schedules` (
  `id` int NOT NULL,
  `movies_id` int DEFAULT NULL,
  `schedules_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.room_movies_schedules: ~0 rows (approximately)

-- Dumping structure for table cinema.schedules
CREATE TABLE IF NOT EXISTS `schedules` (
  `id` int NOT NULL AUTO_INCREMENT,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.schedules: ~2 rows (approximately)
INSERT INTO `schedules` (`id`, `start_time`, `end_time`) VALUES
	(5, '2023-10-23 22:41:40', '2023-10-23 23:41:40'),
	(6, '2023-10-23 22:41:40', '2023-10-23 23:41:40');

-- Dumping structure for table cinema.tickets
CREATE TABLE IF NOT EXISTS `tickets` (
  `id` int NOT NULL,
  `date` date DEFAULT NULL,
  `price_id` int DEFAULT NULL,
  `room_chair_id` int DEFAULT NULL,
  `schedules_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.tickets: ~0 rows (approximately)

-- Dumping structure for table cinema.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0',
  `date_of_birth` date DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `point` int DEFAULT NULL,
  `money` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cinema.users: ~1 rows (approximately)
INSERT INTO `users` (`id`, `name`, `email`, `password`, `gender`, `date_of_birth`, `role`, `phone`, `image_url`, `point`, `money`) VALUES
	(1, 'a', 'a', 'a', 0, '2001-06-23', '0', '090990990', 'adsfasdfasdf', 10, 1000);

-- Dumping structure for table cinema.users_key
CREATE TABLE IF NOT EXISTS `users_key` (
  `id` int NOT NULL,
  `public_key_modulus` text,
  `public_key_exponent` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table cinema.users_key: ~3 rows (approximately)
INSERT INTO `users_key` (`id`, `public_key_modulus`, `public_key_exponent`) VALUES
	(1, '139305256138961376591331178262674518727835273572331033340363485098982185407214901534566145860688712440224375741189522739672364566870851249616605257731736831774993510662511614163172996031488912152273777321080052987126621946967361827532016618164628562947366727658323331659212831862873737179688393888950645346687', '65537'),
	(2, '123907223582618527372236361548899239382748283964272127907348450859624022515643919067596635800470959302912822662223094257854045763511860054338101648687349251086218041891098949108594676407152003387050975667996322666978732272544098700768362260938813127352372434436619050851919489504284760069435545648874775962757', '65537');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
