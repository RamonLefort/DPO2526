-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-05-2026 a las 16:33:58
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `coffeeclicker`
--
CREATE DATABASE IF NOT EXISTS `coffeeclicker` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `coffeeclicker`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `game`
--

CREATE TABLE IF NOT EXISTS `game` (
  `id_game` int(100) NOT NULL AUTO_INCREMENT,
  `name_game` varchar(100) DEFAULT NULL,
  `money` double(100,2) DEFAULT 0.00,
  `hours` int(11) NOT NULL DEFAULT 0,
  `minutes` int(100) DEFAULT 0,
  `seconds` int(100) DEFAULT 0,
  `coffee_per_click` int(100) DEFAULT 1,
  `production_per_second` float NOT NULL DEFAULT 0,
  `username` varchar(50) DEFAULT NULL,
  `finished` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_game`),
  KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `game`
--

INSERT INTO `game` (`id_game`, `name_game`, `money`, `hours`, `minutes`, `seconds`, `coffee_per_click`, `production_per_second`, `username`, `finished`) VALUES
(1, 'Ejemplo', 1465.00, 0, 16, 49, 1, 3.64, 'Prueba', 0),
(2, 'Hola', 30.00, 0, 0, 0, 1, 0, 'Bala', 0),
(3, 'Prueba', 0.00, 0, 0, 19, 1, 0, 'Prueba', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `generador`
--

CREATE TABLE IF NOT EXISTS `generador` (
  `id_generator` int(100) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `id_game` int(100) DEFAULT NULL,
  `quantity` int(100) DEFAULT 0,
  `price` int(100) DEFAULT NULL,
  `period` double(100,1) DEFAULT NULL,
  `earning` double(100,1) DEFAULT NULL,
  PRIMARY KEY (`id_generator`),
  KEY `id_game` (`id_game`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `generador`
--

INSERT INTO `generador` (`id_generator`, `name`, `id_game`, `quantity`, `price`, `period`, `earning`) VALUES
(4, 'Barista', 1, 5, 549, 5000.0, 1.0),
(5, 'Espresso Machine', 1, 4, 757, 3000.0, 2.0),
(6, 'Coffee Plantation', 1, 0, 2000, 1000.0, 1.0),
(7, 'Barista', 3, 0, 15, 5000.0, 1.0),
(8, 'Espresso Machine', 3, 0, 150, 3000.0, 2.0),
(9, 'Coffee Plantation', 3, 0, 2000, 1000.0, 1.0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `setting`
--

CREATE TABLE IF NOT EXISTS `setting` (
  `id_setting` int(100) NOT NULL AUTO_INCREMENT,
  `volume` int(100) DEFAULT NULL,
  `background` varchar(255) DEFAULT NULL,
  `skin` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_setting`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `stat`
--

CREATE TABLE IF NOT EXISTS `stat` (
  `id_stat` int(100) NOT NULL AUTO_INCREMENT,
  `id_games` int(100) NOT NULL,
  `minute_mark` int(11) NOT NULL,
  `money_at_minute` double(100,2) NOT NULL,
  `manual_clicks_total` int(100) DEFAULT 0,
  `auto_generated_total` double(100,2) DEFAULT 0,
  `max_production_rate` float DEFAULT 0,
  `upgrades_expenses` double(100,2) DEFAULT 0,
  PRIMARY KEY (`id_stat`),
  KEY `id_games` (`id_games`),
  CONSTRAINT `stat_ibfk_1` FOREIGN KEY (`id_games`) 
    REFERENCES `game` (`id_game`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `upgrade`
--

CREATE TABLE IF NOT EXISTS `upgrade` (
  `id_upgrade` int(100) NOT NULL AUTO_INCREMENT,
  `id_generator` int(100) DEFAULT NULL,
  `id_game` int(100) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 0,
  `price` int(100) DEFAULT NULL,
  PRIMARY KEY (`id_upgrade`),
  KEY `id_generator` (`id_generator`),
  KEY `id_game` (`id_game`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`username`, `email`, `password`) VALUES
('Bala', 'bala@gmail.com', 'Bala@123'),
('Prueba', 'prueba@gmail.com', 'lolo123');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `game`
--
ALTER TABLE `game`
  ADD CONSTRAINT `game_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE;

--
-- Filtros para la tabla `generador`
--
ALTER TABLE `generador`
  ADD CONSTRAINT `generador_ibfk_1` FOREIGN KEY (`id_game`) REFERENCES `game` (`id_game`) ON DELETE CASCADE;

--
-- Filtros para la tabla `setting`
--
ALTER TABLE `setting`
  ADD CONSTRAINT `setting_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE;

--
-- Filtros para la tabla `stat`
--
ALTER TABLE `stat`
  ADD CONSTRAINT `stat_ibfk_1` FOREIGN KEY (`id_games`) REFERENCES `game` (`id_game`) ON DELETE CASCADE;

--
-- Filtros para la tabla `upgrade`
--
ALTER TABLE `upgrade`
  ADD CONSTRAINT `upgrade_ibfk_1` FOREIGN KEY (`id_generator`) REFERENCES `generador` (`id_generator`) ON DELETE CASCADE,
  ADD CONSTRAINT `upgrade_ibfk_2` FOREIGN KEY (`id_game`) REFERENCES `game` (`id_game`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
