-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 06-05-2026 a las 18:07:34
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- --------------------------------------------------------
-- 1. CREACIÓN DE LA BASE DE DATOS Y SELECCIÓN
-- --------------------------------------------------------

CREATE DATABASE IF NOT EXISTS `coffeeclicker` 
  DEFAULT CHARACTER SET utf8mb4 
  COLLATE utf8mb4_general_ci;

USE `coffeeclicker`;

-- --------------------------------------------------------
-- 2. ESTRUCTURA DE TABLAS (CON IF NOT EXISTS)
-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
-- (Se crea primero porque 'game' y 'setting' dependen de ella)
--
CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Estructura de tabla para la tabla `game`
--
CREATE TABLE IF NOT EXISTS `game` (
  `id_game` int(100) NOT NULL,
  `name_game` varchar(100) DEFAULT NULL,
  `money` double(100,2) DEFAULT 0.00,
  `hours` int(11) NOT NULL DEFAULT 0,
  `minutes` int(100) DEFAULT 0,
  `seconds` int(100) DEFAULT 0,
  `coffee_per_click` int(100) DEFAULT 1,
  `production_per_second` float NOT NULL DEFAULT 0,
  `username` varchar(50) DEFAULT NULL,
  `finished` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Estructura de tabla para la tabla `generador`
--
CREATE TABLE IF NOT EXISTS `generador` (
  `id_generator` int(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `id_game` int(100) DEFAULT NULL,
  `quantity` int(100) DEFAULT 0,
  `price` int(100) DEFAULT NULL,
  `period` double(100,1) DEFAULT NULL,
  `earning` double(100,1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Estructura de tabla para la tabla `setting`
--
CREATE TABLE IF NOT EXISTS `setting` (
  `id_setting` int(100) NOT NULL,
  `volume` int(100) DEFAULT NULL,
  `background` varchar(255) DEFAULT NULL,
  `skin` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Estructura de tabla para la tabla `stat`
--
CREATE TABLE IF NOT EXISTS `stat` (
  `id_stat` int(100) NOT NULL,
  `id_games` int(100) NOT NULL,
  `minute_mark` int(11) NOT NULL,
  `money_at_minute` double(100,2) NOT NULL,
  `manual_clicks_total` int(100) DEFAULT 0,
  `auto_generated_total` double(100,2) DEFAULT 0.00,
  `max_production_rate` float DEFAULT 0,
  `upgrades_expenses` double(100,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Estructura de tabla para la tabla `upgrade`
--
CREATE TABLE IF NOT EXISTS `upgrade` (
  `id_upgrade` int(100) NOT NULL,
  `id_generator` int(100) DEFAULT NULL,
  `id_game` int(100) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 0,
  `price` int(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------
-- 3. ÍNDICES Y AUTONUMÉRICOS (ALTER TABLES)
-- --------------------------------------------------------
-- Nota: Si la tabla ya existía, lanzar los ALTER TABLE sobre 
-- claves que ya existen generará un error en la base de datos.
-- Por diseño, un volcado estándar de phpMyAdmin asume que la 
-- base de datos está vacía al aplicar los ALTER. 

ALTER TABLE `game`
  ADD PRIMARY KEY (`id_game`),
  ADD KEY `username` (`username`);

ALTER TABLE `generador`
  ADD PRIMARY KEY (`id_generator`),
  ADD KEY `id_game` (`id_game`);

ALTER TABLE `setting`
  ADD PRIMARY KEY (`id_setting`),
  ADD KEY `username` (`username`);

ALTER TABLE `stat`
  ADD PRIMARY KEY (`id_stat`),
  ADD KEY `id_games` (`id_games`);

ALTER TABLE `upgrade`
  ADD PRIMARY KEY (`id_upgrade`),
  ADD KEY `id_generator` (`id_generator`),
  ADD KEY `id_game` (`id_game`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);

-- --------------------------------------------------------
-- 4. AUTO INCREMENTS
-- --------------------------------------------------------

ALTER TABLE `game`
  MODIFY `id_game` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

ALTER TABLE `generador`
  MODIFY `id_generator` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

ALTER TABLE `setting`
  MODIFY `id_setting` int(100) NOT NULL AUTO_INCREMENT;

ALTER TABLE `stat`
  MODIFY `id_stat` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

ALTER TABLE `upgrade`
  MODIFY `id_upgrade` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

-- --------------------------------------------------------
-- 5. RESTRICCIONES DE CLAVES FORÁNEAS (FOREIGN KEYS)
-- --------------------------------------------------------

ALTER TABLE `game`
  ADD CONSTRAINT `game_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE;

ALTER TABLE `generador`
  ADD CONSTRAINT `generador_ibfk_1` FOREIGN KEY (`id_game`) REFERENCES `game` (`id_game`) ON DELETE CASCADE;

ALTER TABLE `setting`
  ADD CONSTRAINT `setting_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE;

ALTER TABLE `stat`
  ADD CONSTRAINT `stat_ibfk_1` FOREIGN KEY (`id_games`) REFERENCES `game` (`id_game`) ON DELETE CASCADE;

ALTER TABLE `upgrade`
  ADD CONSTRAINT `upgrade_ibfk_1` FOREIGN KEY (`id_generator`) REFERENCES `generador` (`id_generator`) ON DELETE CASCADE,
  ADD CONSTRAINT `upgrade_ibfk_2` FOREIGN KEY (`id_game`) REFERENCES `game` (`id_game`) ON DELETE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
