-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 10, 2019 at 12:05 AM
-- Server version: 10.1.34-MariaDB
-- PHP Version: 5.6.37

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `carpark`
--
DROP DATABASE IF EXISTS `carpark`;
CREATE DATABASE IF NOT EXISTS `carpark` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `carpark`;

-- --------------------------------------------------------

--
-- Table structure for table `entrance`
--

DROP TABLE IF EXISTS `entrance`;
CREATE TABLE IF NOT EXISTS `entrance` (
  `pkid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `CashierID` varchar(10) DEFAULT NULL,
  `CashierName` varchar(15) DEFAULT NULL,
  `EntranceID` varchar(5) DEFAULT NULL,
  `CardNumber` varchar(12) DEFAULT NULL,
  `PlateNumber` varchar(8) DEFAULT NULL,
  `ParkerType` varchar(1) DEFAULT NULL,
  `TimeIN` datetime DEFAULT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `entrance`
--

INSERT INTO `entrance` (`pkid`, `CashierID`, `CashierName`, `EntranceID`, `CardNumber`, `PlateNumber`, `ParkerType`, `TimeIN`) VALUES
(1, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:32:13'),
(2, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:32:21'),
(3, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:32:26'),
(4, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:32:31'),
(5, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:32:36'),
(6, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:33:02'),
(7, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:33:14'),
(8, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:57:12'),
(9, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:57:16'),
(10, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:57:24'),
(11, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 17:58:58'),
(12, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-21 18:01:18'),
(13, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-22 14:19:30'),
(14, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-22 14:27:11'),
(15, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-22 14:48:04'),
(16, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-22 14:58:49'),
(17, '12340000', 'Unmanned', 'EN01', '', '', 'R', '2019-06-22 15:05:42'),
(18, '12340000', 'Unmanned', 'EN01', 'BD1E6E01', '', 'R', '2019-06-23 09:18:06');

-- --------------------------------------------------------

--
-- Table structure for table `exit_trans`
--

DROP TABLE IF EXISTS `exit_trans`;
CREATE TABLE IF NOT EXISTS `exit_trans` (
  `pkid` bigint(36) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `void` tinyint(1) NOT NULL DEFAULT '0',
  `voidRefID` varchar(254) DEFAULT NULL,
  `ReceiptNumber` varchar(128) NOT NULL,
  `CashierName` varchar(15) DEFAULT NULL,
  `EntranceID` varchar(5) DEFAULT NULL,
  `ExitID` varchar(4) NOT NULL,
  `CardNumber` varchar(12) DEFAULT NULL,
  `PlateNumber` varchar(8) DEFAULT NULL,
  `ParkerType` varchar(2) DEFAULT NULL,
  `Amount` float DEFAULT NULL,
  `DateTimeIN` datetime NOT NULL,
  `DateTimeOUT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `HoursParked` int(11) NOT NULL,
  `MinutesParked` int(11) NOT NULL,
  `SettlementRef` varchar(128) NOT NULL,
  `SettlementName` varchar(50) DEFAULT NULL,
  `SettlementAddr` varchar(50) DEFAULT NULL,
  `SettlementTIN` varchar(18) DEFAULT NULL,
  `SettlementBusStyle` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `exit_trans`
--

INSERT INTO `exit_trans` (`pkid`, `void`, `voidRefID`, `ReceiptNumber`, `CashierName`, `EntranceID`, `ExitID`, `CardNumber`, `PlateNumber`, `ParkerType`, `Amount`, `DateTimeIN`, `DateTimeOUT`, `HoursParked`, `MinutesParked`, `SettlementRef`, `SettlementName`, `SettlementAddr`, `SettlementTIN`, `SettlementBusStyle`) VALUES
(000000000000000000000000000000000001, 0, NULL, 'EX01000000000001', '00001000', 'EN01', 'EX01', '11E920A9', 'MCV346', 'R', 30, '2019-07-09 03:27:11', '2019-07-08 19:47:05', 0, 19, '', '', '', '', ''),
(000000000000000000000000000000000002, 0, NULL, 'EX01000000000002', '00001000', 'EN01', 'EX01', '61B050A9', 'DFG457', 'P', 15, '2019-07-09 03:26:58', '2019-07-08 19:47:39', 0, 20, '', '', '', '', ''),
(000000000000000000000000000000000003, 0, NULL, 'EX01000000000003', '00001000', 'EN01', 'EX01', '617E50A9', 'BOBO234', 'M', 20, '2019-07-09 03:26:38', '2019-07-08 19:48:11', 0, 21, '', '', '', '', ''),
(000000000000000000000000000000000004, 0, NULL, 'EX01000000000004', '00001000', 'EN01', 'EX01', '01465BA9', 'NXWS346', 'BC', 20, '2019-07-09 03:28:26', '2019-07-08 19:48:47', 0, 20, '', '', '', '', ''),
(000000000000000000000000000000000005, 0, NULL, 'EX01000000000005', '00001000', 'EN01', 'EX01', '31474FA9', 'CCV346', 'EM', 20, '2019-07-09 03:28:18', '2019-07-08 19:49:26', 0, 21, '', '', '', '', ''),
(000000000000000000000000000000000006, 0, NULL, 'EX01000000000006', '00001000', 'EN01', 'EX01', 'A1774CA9', 'PCD377', 'J', 20, '2019-07-09 03:28:11', '2019-07-08 19:49:54', 0, 21, '', '', '', '', ''),
(000000000000000000000000000000000007, 0, NULL, 'EX01000000000007', '00001000', 'EN01', 'EX01', '41F348A9', 'POS3556', 'Q', 0, '2019-07-09 03:27:53', '2019-07-08 19:50:41', 0, 22, '', '', '', '', ''),
(000000000000000000000000000000000008, 0, NULL, 'EX01000000000008', '00001000', 'EN01', 'EX01', '81375BA9', 'LDF347', 'V', 0, '2019-07-09 03:27:45', '2019-07-08 19:51:46', 0, 23, '', '', '', '', ''),
(000000000000000000000000000000000009, 0, NULL, 'EX01000000000009', '00001000', 'EN01', 'EX01', '313647A9', 'LCW346', 'NQ', 30, '2019-07-09 03:27:36', '2019-07-08 20:07:30', 0, 39, '', '', '', '', ''),
(000000000000000000000000000000000010, 0, NULL, 'EX01000000000010', '00001000', 'EN01', 'EX01', 'B1DB58A9', 'FJE4367', 'TC', 20, '2019-07-09 03:27:28', '2019-07-08 20:08:39', 0, 41, '', '', '', '', ''),
(000000000000000000000000000000000011, 0, NULL, 'EX01000000000011', '00001000', 'EN01', 'EX01', 'E1EF4BA9', 'BVCN235', 'TN', 20, '2019-07-09 03:27:18', '2019-07-08 20:09:11', 0, 41, '', '', '', '', ''),
(000000000000000000000000000000000012, 0, NULL, 'EX01000000000012', '00001000', 'EN01', 'EX01', '81375BA9', 'DND4357', 'PW', 30, '2019-07-09 03:55:58', '2019-07-08 20:09:47', 0, 13, '', '', '', '', ''),
(000000000000000000000000000000000013, 0, NULL, 'EX01000000000013', '00001000', 'EN01', 'EX01', '41F348A9', 'XCV457', 'D', 0, '2019-07-09 03:55:53', '2019-07-08 20:10:41', 0, 14, '', '', '', '', ''),
(000000000000000000000000000000000014, 0, NULL, 'EX01000000000014', '00001000', 'EN01', 'EX01', '41F348A9', 'LOS590', 'L', 330, '2019-07-09 02:00:00', '2019-07-08 20:11:51', 2, 11, '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `void_trans`
--

DROP TABLE IF EXISTS `void_trans`;
CREATE TABLE IF NOT EXISTS `void_trans` (
  `pkid` bigint(36) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `void` tinyint(1) NOT NULL DEFAULT '0',
  `voidRefID` varchar(254) DEFAULT NULL,
  `ReceiptNumber` varchar(128) NOT NULL,
  `CashierName` varchar(15) DEFAULT NULL,
  `EntranceID` varchar(5) DEFAULT NULL,
  `ExitID` varchar(4) NOT NULL,
  `CardNumber` varchar(12) DEFAULT NULL,
  `PlateNumber` varchar(8) DEFAULT NULL,
  `ParkerType` varchar(2) DEFAULT NULL,
  `Amount` float DEFAULT NULL,
  `DateTimeIN` datetime NOT NULL,
  `DateTimeOUT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `HoursParked` int(11) NOT NULL,
  `MinutesParked` int(11) NOT NULL,
  `SettlementRef` varchar(128) NOT NULL,
  `SettlementName` varchar(50) DEFAULT NULL,
  `SettlementAddr` varchar(50) DEFAULT NULL,
  `SettlementTIN` varchar(50) DEFAULT NULL,
  `SettlementBusStyle` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
--
-- Database: `carslots`
--
DROP DATABASE IF EXISTS `carslots`;
CREATE DATABASE IF NOT EXISTS `carslots` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `carslots`;

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `pkId` int(11) NOT NULL AUTO_INCREMENT,
  `totalSlots` int(11) NOT NULL,
  `currentSlots` int(11) NOT NULL,
  `adjustments` int(11) NOT NULL,
  PRIMARY KEY (`pkId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`pkId`, `totalSlots`, `currentSlots`, `adjustments`) VALUES
(1, 300, -2046, 0);
--
-- Database: `colltrain`
--
DROP DATABASE IF EXISTS `colltrain`;
CREATE DATABASE IF NOT EXISTS `colltrain` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `colltrain`;

-- --------------------------------------------------------

--
-- Table structure for table `bfct`
--

DROP TABLE IF EXISTS `bfct`;
CREATE TABLE IF NOT EXISTS `bfct` (
  `pkID` int(11) NOT NULL AUTO_INCREMENT,
  `logINID` bigint(32) UNSIGNED NOT NULL,
  `SentinelID` varchar(4) NOT NULL,
  `userCode` varchar(10) NOT NULL,
  `userName` varchar(25) NOT NULL,
  `loginStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `logoutStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `extendedCount` int(11) NOT NULL DEFAULT '0',
  `extendedAmount` float NOT NULL DEFAULT '0',
  `overnightCount` int(11) NOT NULL DEFAULT '0',
  `overnightAmount` float NOT NULL DEFAULT '0',
  `carServed` int(11) NOT NULL DEFAULT '0',
  `totalAmount` float NOT NULL DEFAULT '0',
  `retailCount` int(11) NOT NULL DEFAULT '0',
  `retailAmount` float NOT NULL DEFAULT '0',
  `vipCount` int(11) NOT NULL DEFAULT '0',
  `vipAmount` float NOT NULL DEFAULT '0',
  `motorcycleCount` int(11) NOT NULL DEFAULT '0',
  `motorcycleAmount` float NOT NULL DEFAULT '0',
  `graceperiodCount` int(11) NOT NULL DEFAULT '0',
  `graceperiodAmount` float NOT NULL DEFAULT '0',
  `lostCount` int(11) NOT NULL DEFAULT '0',
  `lostAmount` float NOT NULL DEFAULT '0',
  `deliveryCount` int(11) NOT NULL DEFAULT '0',
  `deliveryAmount` float NOT NULL DEFAULT '0',
  `seniorCount` int(11) NOT NULL DEFAULT '0',
  `seniorAmount` float NOT NULL DEFAULT '0',
  `qcseniorCount` int(11) NOT NULL DEFAULT '0',
  `qcseniorAmount` float NOT NULL DEFAULT '0',
  `nonqcseniorCount` int(11) NOT NULL DEFAULT '0',
  `nonqcseniorAmount` float NOT NULL DEFAULT '0',
  `bpomotorCount` int(11) NOT NULL DEFAULT '0',
  `bpomotorAmount` float NOT NULL DEFAULT '0',
  `busCount` int(11) NOT NULL DEFAULT '0',
  `busAmount` float NOT NULL DEFAULT '0',
  `trucksCount` int(11) NOT NULL DEFAULT '0',
  `trucksAmount` float NOT NULL DEFAULT '0',
  `pwdCount` int(11) NOT NULL DEFAULT '0',
  `pwdAmount` float NOT NULL DEFAULT '0',
  `carsCount` int(11) NOT NULL DEFAULT '0',
  `carsAmount` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`pkID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bir`
--

DROP TABLE IF EXISTS `bir`;
CREATE TABLE IF NOT EXISTS `bir` (
  `logINID` bigint(32) UNSIGNED NOT NULL,
  `SentinelID` varchar(4) NOT NULL,
  `userCode` varchar(10) NOT NULL,
  `userName` varchar(25) NOT NULL,
  `loginStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `logoutStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `extendedCount` int(11) NOT NULL DEFAULT '0',
  `extendedAmount` float NOT NULL DEFAULT '0',
  `overnightCount` int(11) NOT NULL DEFAULT '0',
  `overnightAmount` float NOT NULL DEFAULT '0',
  `carServed` int(11) NOT NULL DEFAULT '0',
  `totalAmount` float NOT NULL DEFAULT '0',
  `regularCount` int(11) NOT NULL DEFAULT '0',
  `regularAmount` float NOT NULL DEFAULT '0',
  `vipCount` int(11) NOT NULL DEFAULT '0',
  `vipAmount` float NOT NULL DEFAULT '0',
  `motorcycleCount` int(11) NOT NULL DEFAULT '0',
  `motorcycleAmount` float NOT NULL DEFAULT '0',
  `graceperiodCount` int(11) NOT NULL DEFAULT '0',
  `graceperiodAmount` float NOT NULL DEFAULT '0',
  `lostCount` int(11) NOT NULL DEFAULT '0',
  `lostAmount` float NOT NULL DEFAULT '0',
  `deliveryCount` int(11) NOT NULL DEFAULT '0',
  `deliveryAmount` float NOT NULL DEFAULT '0',
  `qcseniorCount` int(11) NOT NULL DEFAULT '0',
  `qcseniorAmount` float NOT NULL DEFAULT '0',
  `nonqcseniorCount` int(11) NOT NULL DEFAULT '0',
  `nonqcseniorAmount` float NOT NULL DEFAULT '0',
  `promoCount` int(11) NOT NULL DEFAULT '0',
  `promoAmount` float NOT NULL DEFAULT '0',
  `jeepCount` int(11) NOT NULL DEFAULT '0',
  `jeepAmount` float NOT NULL DEFAULT '0',
  `taxiCount` int(11) NOT NULL DEFAULT '0',
  `taxiAmount` float NOT NULL DEFAULT '0',
  `pwdCount` int(11) NOT NULL DEFAULT '0',
  `pwdAmount` float NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `cedar`
--

DROP TABLE IF EXISTS `cedar`;
CREATE TABLE IF NOT EXISTS `cedar` (
  `logINID` bigint(32) UNSIGNED NOT NULL AUTO_INCREMENT,
  `SentinelID` varchar(4) NOT NULL,
  `userCode` varchar(10) NOT NULL,
  `userName` varchar(25) NOT NULL,
  `loginStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `logoutStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `extendedCount` int(11) NOT NULL DEFAULT '0',
  `extendedAmount` float NOT NULL DEFAULT '0',
  `overnightCount` int(11) NOT NULL DEFAULT '0',
  `overnightAmount` float NOT NULL DEFAULT '0',
  `carServed` int(11) NOT NULL DEFAULT '0',
  `totalAmount` float NOT NULL DEFAULT '0',
  `retailCount` int(11) NOT NULL DEFAULT '0',
  `retailAmount` float NOT NULL DEFAULT '0',
  `vipCount` int(11) NOT NULL DEFAULT '0',
  `vipAmount` float NOT NULL DEFAULT '0',
  `motorcycleCount` int(11) NOT NULL DEFAULT '0',
  `motorcycleAmount` float NOT NULL DEFAULT '0',
  `graceperiodCount` int(11) NOT NULL DEFAULT '0',
  `graceperiodAmount` float NOT NULL DEFAULT '0',
  `lostCount` int(11) NOT NULL DEFAULT '0',
  `lostAmount` float NOT NULL DEFAULT '0',
  `deliveryCount` int(11) NOT NULL DEFAULT '0',
  `deliveryAmount` float NOT NULL DEFAULT '0',
  `seniorCount` int(11) NOT NULL DEFAULT '0',
  `seniorAmount` float NOT NULL DEFAULT '0',
  `nonqcseniorCount` int(11) NOT NULL DEFAULT '0',
  `nonqcseniorAmount` float NOT NULL DEFAULT '0',
  `bpomotorCount` int(11) NOT NULL DEFAULT '0',
  `bpomotorAmount` float NOT NULL DEFAULT '0',
  `promoCount` int(11) NOT NULL DEFAULT '0',
  `promoAmount` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`logINID`)
) ENGINE=InnoDB AUTO_INCREMENT=15400001000143224 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cedar`
--

INSERT INTO `cedar` (`logINID`, `SentinelID`, `userCode`, `userName`, `loginStamp`, `logoutStamp`, `extendedCount`, `extendedAmount`, `overnightCount`, `overnightAmount`, `carServed`, `totalAmount`, `retailCount`, `retailAmount`, `vipCount`, `vipAmount`, `motorcycleCount`, `motorcycleAmount`, `graceperiodCount`, `graceperiodAmount`, `lostCount`, `lostAmount`, `deliveryCount`, `deliveryAmount`, `seniorCount`, `seniorAmount`, `nonqcseniorCount`, `nonqcseniorAmount`, `bpomotorCount`, `bpomotorAmount`, `promoCount`, `promoAmount`) VALUES
(13600001000812, 'EX01', 'teller1', 'teller1', '2018-12-07 00:01:02', '2018-12-07 05:28:26', 0, 0, 0, 0, 122, 4728.75, 112, 4470, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 258.75, 0, 0, 0, 0, 0, 0),
(122000010007122, 'EX01', 'teller1', 'teller1', '2019-02-01 23:01:23', '2019-02-09 01:22:34', 0, 0, 29, 10200, 2022, 126273, 1952, 121890, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 66, 2842.03, 0, 0, 0, 0, 3, 1540),
(128000010007350, 'EX01', 'teller1', 'teller1', '2019-01-08 23:35:00', '2019-01-09 00:00:58', 0, 0, 2, 2000, 2, 2770, 2, 2770, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(128000010008122, 'EX01', 'teller1', 'teller1', '2019-01-09 00:01:22', '2019-01-09 13:38:57', 0, 0, 0, 0, 299, 14693.4, 291, 14410, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 283.386, 0, 0, 0, 0, 0, 0),
(129000020007525, 'EX01', 'teller2', 'teller2', '2019-01-09 23:05:25', '2019-01-09 23:42:06', 0, 0, 2, 2000, 5, 2925, 5, 2925, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(134000010007627, 'EX01', 'teller1', 'teller1', '2019-01-14 23:06:27', '2019-01-14 23:36:18', 0, 0, 1, 300, 1, 760, 1, 760, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(134000010009579, 'EX01', 'teller1', 'teller1', '2018-12-05 01:57:09', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(138000010007447, 'EX01', 'teller1', 'teller1', '2019-01-18 23:04:48', '2019-01-19 02:11:35', 0, 0, 1, 300, 13, 1255, 13, 1255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(140000020008950, 'EX01', 'teller2', 'teller2', '2018-12-11 00:09:50', '2018-12-11 00:12:05', 0, 0, 2, 600, 5, 1525, 5, 1525, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(142000010008596, 'EX01', 'teller1', 'teller1', '2018-12-13 00:59:06', '2018-12-13 14:00:33', 0, 0, 1, 1000, 335, 18108.1, 322, 17570, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 538.022, 0, 0, 0, 0, 0, 0),
(143000010006370, 'EX01', 'teller1', 'teller1', '2019-01-23 22:37:01', '2019-01-24 05:35:04', 0, 0, 3, 900, 105, 6606.25, 102, 6520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 86.25, 0, 0, 0, 0, 0, 0),
(143000010007143, 'EX01', 'teller1', 'teller1', '2018-12-13 23:14:03', '2018-12-14 14:24:15', 0, 0, 0, 0, 358, 18524, 347, 18220, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 303.92, 0, 0, 0, 0, 0, 0),
(148000010007320, 'EX01', 'teller1', 'teller1', '2018-12-18 23:03:20', '2018-12-19 23:14:16', 0, 0, 0, 0, 4, 215, 4, 215, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1260000100073644, 'EX01', 'teller1', 'teller1', '2019-01-06 23:36:44', '2019-01-07 23:23:02', 0, 0, 0, 0, 3, 105, 3, 105, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1270000100072316, 'EX01', 'teller1', 'teller1', '2019-01-07 23:23:16', '2019-01-08 23:34:17', 0, 0, 0, 0, 346, 18493.1, 329, 17840, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 653.022, 0, 0, 0, 0, 0, 0),
(1290000100074228, 'EX01', 'teller1', 'teller1', '2019-01-09 23:42:28', '2019-01-10 11:34:25', 0, 0, 6, 8000, 624, 45358.5, 605, 44615, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 743.374, 0, 0, 0, 0, 0, 0),
(1290000200019357, 'EX01', 'teller2', 'teller2', '2019-01-10 11:35:07', '2019-01-10 14:02:57', 0, 0, 0, 0, 28, 2780, 28, 2780, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1300000100065257, 'EX01', 'teller1', 'teller1', '2019-01-10 22:52:57', '2019-01-11 13:42:20', 0, 0, 3, 2400, 179, 12219.6, 173, 11965, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 254.636, 0, 0, 0, 0, 0, 0),
(1310000100015519, 'EX01', 'teller1', 'teller1', '2019-02-11 07:05:19', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1310000100074141, 'EX01', 'teller1', 'teller1', '2019-01-11 23:41:41', '2019-01-12 07:26:22', 0, 0, 2, 600, 156, 9626.95, 151, 9360, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 266.954, 0, 0, 0, 0, 0, 0),
(1310000200015280, 'EX01', 'teller2', 'teller2', '2019-01-12 07:28:00', '2019-01-12 13:32:20', 0, 0, 0, 0, 36, 2553.75, 35, 2525, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 28.75, 0, 0, 0, 0, 0, 0),
(1320000100071721, 'EX01', 'teller1', 'teller1', '2019-01-12 23:17:21', '2019-01-13 08:26:29', 0, 0, 3, 900, 170, 9441.06, 166, 9285, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 156.068, 0, 0, 0, 0, 0, 0),
(1330000100016329, 'EX01', 'teller1', 'teller1', '2018-12-04 08:32:09', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1330000100017274, 'EX01', 'teller1', 'teller1', '2018-12-04 09:27:04', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1330000200015277, 'EX01', 'teller2', 'teller2', '2018-12-04 07:27:07', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1330000200016315, 'EX01', 'teller2', 'teller2', '2018-12-04 08:31:05', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000100011036, 'EX01', '00001000', 'teller1', '2018-09-08 03:00:36', '2018-09-08 03:01:04', 0, 0, 0, 0, 1, 50, 1, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000100011122, 'EX01', 'teller1', 'teller1', '2018-12-05 03:01:22', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000100074325, 'EX01', 'teller1', 'teller1', '2019-01-14 23:43:25', '2019-01-15 13:37:19', 0, 0, 3, 900, 290, 16756, 273, 16255, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 11, 501.056, 0, 0, 0, 0, 0, 0),
(1340000100095751, 'EX01', 'teller1', 'teller1', '2018-12-05 01:57:51', '0000-00-00 00:00:00', 0, 0, 0, 0, 3, 105, 3, 105, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200011248, 'EX01', '00002000', 'teller2', '2018-09-08 03:02:48', '2018-09-10 03:21:21', 0, 0, 0, 0, 10, 420, 9, 420, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200011338, 'EX01', 'teller2', 'teller2', '2018-12-05 03:03:38', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200011547, 'EX01', 'teller2', 'teller2', '2018-12-05 03:05:47', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200084643, 'EX01', 'teller2', 'teller2', '2018-12-05 00:46:43', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1350000100011373, 'EX01', 'teller1', 'teller1', '2019-02-09 01:22:57', '2019-02-11 07:04:34', 0, 0, 8, 2400, 515, 32337.4, 491, 28560, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 3432.41, 0, 0, 0, 0, 3, 310),
(1350000100064244, 'EX01', 'teller1', 'teller1', '2019-01-15 22:42:45', '2019-01-16 22:36:21', 0, 0, 1, 300, 344, 19060.5, 333, 18580, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 480.522, 0, 0, 0, 0, 0, 0),
(1350000100065856, 'EX01', 'teller1', 'teller1', '2018-12-05 22:58:56', '2018-12-06 00:25:06', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1350000100082645, 'EX01', 'teller1', 'teller1', '2018-12-06 00:26:45', '2018-12-06 05:44:06', 0, 0, 0, 0, 69, 2540, 65, 2425, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 115, 0, 0, 0, 0, 0, 0),
(1360000100063641, 'EX01', 'teller1', 'teller1', '2019-01-16 22:36:42', '2019-01-17 05:41:05', 0, 0, 5, 1500, 125, 8347.32, 114, 7990, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 357.318, 0, 0, 0, 0, 0, 0),
(1370000100015442, 'EX01', '00001000', 'teller1', '2018-09-11 07:04:42', '2018-11-10 08:30:04', 0, 0, 0, 0, 13, 885, 11, 885, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1370000100063447, 'EX01', 'teller1', 'teller1', '2019-01-17 22:34:47', '2019-01-18 05:28:03', 0, 0, 7, 2400, 113, 10808, 107, 10615, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 193.034, 0, 0, 0, 0, 0, 0),
(1370000100074440, 'EX01', 'teller1', 'teller1', '2018-12-07 23:44:40', '2018-12-08 13:59:05', 0, 0, 0, 0, 140, 7431.25, 136, 7210, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 221.25, 0, 0, 0, 0, 0, 0),
(1370000200022743, 'EX01', 'teller2', 'teller2', '2018-12-08 14:07:43', '2018-12-08 23:37:25', 0, 0, 5, 5000, 5, 6850, 5, 6850, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1380000100016326, 'EX01', '00001000', 'teller1', '2018-11-10 08:32:06', '2018-11-22 03:13:22', 0, 0, 0, 0, 12, 175, 5, 175, 4, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1380000100073833, 'EX01', 'teller1', 'teller1', '2018-12-08 23:38:33', '2018-12-09 12:41:56', 0, 0, 3, 3000, 253, 16500, 241, 16020, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 480, 0, 0, 0, 0, 0, 0),
(1390000100064922, 'EX01', 'teller1', 'teller1', '2019-01-19 22:49:23', '2019-01-20 12:49:27', 0, 0, 8, 2400, 261, 19474.3, 252, 19195, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 279.284, 0, 0, 0, 0, 0, 0),
(1390000100065942, 'EX01', 'teller1', 'teller1', '2018-12-09 22:59:42', '2018-12-10 02:44:23', 0, 0, 0, 0, 23, 798.75, 22, 770, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 28.75, 0, 0, 0, 0, 0, 0),
(1400000100063034, 'EX01', 'teller1', 'teller1', '2019-01-20 22:30:34', '2019-01-21 13:27:18', 0, 0, 1, 300, 309, 18253.3, 298, 17855, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 398.386, 0, 0, 0, 0, 0, 0),
(1400000100081224, 'EX01', 'teller1', 'teller1', '2018-12-11 00:12:24', '2018-12-11 13:31:56', 0, 0, 5, 1500, 325, 20473.8, 319, 20100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 373.738, 0, 0, 0, 0, 0, 0),
(1410000100063637, 'EX01', 'teller1', 'teller1', '2019-01-21 22:36:37', '2019-01-22 13:04:24', 0, 0, 1, 300, 313, 17324.5, 300, 16725, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 599.624, 0, 0, 0, 0, 0, 0),
(1410000100081330, 'EX01', 'teller1', 'teller1', '2018-12-12 00:13:30', '2018-12-12 13:18:09', 0, 0, 0, 0, 325, 15850, 314, 15390, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 459.988, 0, 0, 0, 0, 0, 0),
(1420000100064521, 'EX01', 'teller1', 'teller1', '2019-01-22 22:45:21', '2019-01-23 13:29:42', 0, 0, 2, 900, 338, 19920.5, 318, 19140, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 780.34, 0, 0, 0, 0, 0, 0),
(1430000200013386, 'EX01', 'teller2', 'teller2', '2019-01-24 05:38:06', '2019-01-24 13:04:16', 0, 0, 0, 0, 190, 11771.4, 185, 11525, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 246.42, 0, 0, 0, 0, 0, 0),
(1440000100011596, 'EX01', '00001000', 'teller1', '2019-02-24 03:59:06', '0000-00-00 00:00:00', 0, 0, 3, 3000, 15, 3705, 15, 3705, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1440000100064116, 'EX01', 'teller1', 'teller1', '2019-01-24 22:41:16', '2019-01-25 05:33:01', 0, 0, 5, 1500, 110, 8599.09, 104, 8365, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 234.102, 0, 0, 0, 0, 0, 0),
(1440000100075431, 'EX01', 'teller1', 'teller1', '2018-12-14 23:54:31', '2018-12-17 23:20:26', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1450000100065444, 'EX01', 'teller1', 'teller1', '2019-01-25 22:54:45', '2019-01-26 13:25:06', 0, 0, 2, 600, 140, 7733.03, 138, 7655, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 78.034, 0, 0, 0, 0, 0, 0),
(1460000100071240, 'EX01', 'teller1', 'teller1', '2019-01-26 23:12:41', '2019-01-27 12:38:47', 0, 0, 0, 0, 233, 11490.7, 223, 11080, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 410.704, 0, 0, 0, 0, 0, 0),
(1470000100063822, 'EX01', 'teller1', 'teller1', '2019-01-27 22:38:22', '2019-02-01 23:00:21', 0, 0, 1, 600, 288, 16295.3, 283, 16090, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 205.352, 0, 0, 0, 0, 0, 0),
(1470000100072049, 'EX01', 'teller1', 'teller1', '2018-12-17 23:20:49', '2018-12-18 23:03:04', 0, 0, 2, 2000, 325, 19211.5, 315, 18850, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 361.42, 0, 0, 0, 0, 0, 0),
(1490000100071434, 'EX01', 'teller1', 'teller1', '2018-12-19 23:14:35', '2018-12-20 05:55:26', 0, 0, 2, 2000, 120, 7961.6, 115, 7750, 0, 0, 1, 35, 0, 0, 0, 0, 0, 0, 4, 176.602, 0, 0, 0, 0, 0, 0),
(1500000100084838, 'EX01', 'teller1', 'teller1', '2018-12-21 00:48:38', '2019-01-06 23:36:04', 0, 0, 3, 479002000000, 3, 22190, 3, 22190, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13200002000162733, 'EX01', 'teller2', 'teller2', '2019-01-13 08:27:33', '2019-01-14 05:29:23', 0, 0, 3, 900, 239, 12770.3, 230, 12450, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 320.352, 0, 0, 0, 0, 0, 0),
(13300001000163511, 'EX01', 'teller1', 'teller1', '2018-12-04 08:35:11', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000164325, 'EX01', 'teller1', 'teller1', '2018-12-04 08:43:25', '0000-00-00 00:00:00', 0, 0, 0, 0, 5, 175, 5, 175, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000165140, 'EX01', 'teller1', 'teller1', '2018-12-04 08:51:40', '0000-00-00 00:00:00', 0, 0, 0, 0, 4, 140, 4, 140, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000165530, 'EX01', 'teller1', 'teller1', '2018-12-04 08:55:30', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000171746, 'EX01', 'teller1', 'teller1', '2018-12-04 09:17:46', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000171832, 'EX01', 'teller1', 'teller1', '2018-12-04 09:18:32', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000174536, 'EX01', 'teller1', 'teller1', '2018-12-04 09:45:36', '0000-00-00 00:00:00', 0, 0, 0, 0, 2, 170, 2, 170, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300002000132946, 'EX01', 'teller2', 'teller2', '2019-01-14 05:29:46', '2019-01-14 13:50:47', 0, 0, 0, 0, 227, 13538.5, 217, 13095, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 443.556, 0, 0, 0, 0, 0, 0),
(13300002000152859, 'EX01', 'teller2', 'teller2', '2018-12-04 07:28:59', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300002000152948, 'EX01', 'teller2', 'teller2', '2018-12-04 07:29:48', '0000-00-00 00:00:00', 0, 0, 0, 0, 1, 35, 1, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300002000163946, 'EX01', 'teller2', 'teller2', '2018-12-04 08:39:46', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400001000101622, 'EX01', 'teller1', 'teller1', '2018-12-05 02:16:22', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400001000105853, 'EX01', 'teller1', 'teller1', '2018-12-05 02:58:53', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400001000151046, 'EX01', 'teller1', 'teller1', '2018-12-05 07:10:46', '2018-12-05 07:15:18', 0, 0, 0, 0, 4, 140, 4, 140, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400002000114718, 'EX01', 'teller2', 'teller2', '2018-12-05 03:47:19', '2018-12-05 03:47:34', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400002000115350, 'EX01', 'teller2', 'teller2', '2018-12-05 03:53:50', '2018-12-05 07:10:19', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13500002000134448, 'EX01', 'teller2', 'teller2', '2018-12-06 05:44:48', '2018-12-06 23:58:48', 0, 0, 0, 0, 185, 10961.2, 180, 10875, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 86.25, 0, 0, 0, 0, 0, 0),
(13600001000112154, 'EX01', '00001000', 'teller1', '2018-09-10 03:21:54', '2018-09-11 07:03:49', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13600002000132925, 'EX01', 'teller2', 'teller2', '2018-12-07 05:29:25', '2018-12-07 14:29:17', 0, 0, 3, 3000, 205, 17826.2, 199, 17625, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 201.25, 0, 0, 0, 0, 0, 0),
(13600002000134117, 'EX01', 'teller2', 'teller2', '2019-01-17 05:41:17', '2019-01-17 13:22:53', 0, 0, 0, 0, 191, 12711, 186, 12485, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 225.886, 0, 0, 0, 0, 0, 0),
(13700001000205921, 'EX01', '00001000', 'teller1', '2019-02-17 12:59:21', '0000-00-00 00:00:00', 0, 0, 23, 6900, 175, 19815, 173, 19505, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 310),
(13700002000132816, 'EX01', 'teller2', 'teller2', '2019-01-18 05:28:16', '2019-01-18 13:41:26', 0, 0, 0, 0, 195, 11734.8, 190, 11550, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 184.818, 0, 0, 0, 0, 0, 0),
(13800001000104217, 'EX01', 'teller1', 'teller1', '2019-01-19 02:42:18', '2019-01-19 13:46:19', 0, 0, 3, 900, 166, 10544.8, 158, 10130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 414.806, 0, 0, 0, 0, 0, 0),
(13900001000104529, 'EX01', 'teller1', 'teller1', '2018-12-10 02:45:30', '2018-12-10 13:07:02', 0, 0, 0, 0, 208, 10951.2, 200, 10750, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 201.25, 0, 0, 0, 0, 0, 0),
(14000002000213230, 'EX01', 'teller2', 'teller2', '2018-12-11 13:32:30', '2018-12-12 00:13:00', 0, 0, 1, 1000, 2, 1520, 2, 1520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14100002000211837, 'EX01', 'teller2', 'teller2', '2018-12-12 13:18:37', '2018-12-13 00:58:43', 0, 0, 0, 0, 5, 193.75, 4, 165, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 28.75, 0, 0, 0, 0, 0, 0),
(14400001000115533, 'EX01', '00001000', 'teller1', '2019-02-24 03:55:33', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14400002000133319, 'EX01', 'teller2', 'teller2', '2019-01-25 05:33:19', '2019-01-25 13:21:32', 0, 0, 0, 0, 208, 13069.3, 201, 12560, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 509.26, 0, 0, 0, 0, 0, 0),
(14900002000135543, 'EX01', 'teller2', 'teller2', '2018-12-20 05:55:43', '2018-12-20 13:19:28', 0, 0, 0, 0, 197, 10941.8, 190, 10775, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 106.784, 0, 0, 0, 0, 0, 0),
(15000002000111414, 'EX01', '00002000', 'teller2', '2018-11-22 03:14:14', '2018-11-23 04:22:56', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15100001000122326, 'EX01', '00001000', 'teller1', '2018-11-23 04:23:27', '2018-11-23 05:49:27', 0, 0, 0, 0, 2, 70, 2, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15100002000134954, 'EX01', '00002000', 'teller2', '2018-11-23 05:49:54', '0000-00-00 00:00:00', 0, 0, 0, 0, 112, 4370.5, 109, 4265, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15400001000143223, 'EX01', 'teller1', 'teller1', '2018-11-26 06:32:23', '0000-00-00 00:00:00', 0, 0, 0, 0, 12, 520, 11, 520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `logINID` bigint(32) UNSIGNED NOT NULL AUTO_INCREMENT,
  `SentinelID` varchar(4) NOT NULL,
  `userCode` varchar(10) NOT NULL,
  `userName` varchar(25) NOT NULL,
  `loginStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `logoutStamp` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `extendedCount` int(11) NOT NULL DEFAULT '0',
  `extendedAmount` float NOT NULL DEFAULT '0',
  `overnightCount` int(11) NOT NULL DEFAULT '0',
  `overnightAmount` float NOT NULL DEFAULT '0',
  `carServed` int(11) NOT NULL DEFAULT '0',
  `totalAmount` float NOT NULL DEFAULT '0',
  `regularCount` int(11) NOT NULL DEFAULT '0',
  `regularAmount` float NOT NULL DEFAULT '0',
  `vipCount` int(11) NOT NULL DEFAULT '0',
  `vipAmount` float NOT NULL DEFAULT '0',
  `motorcycleCount` int(11) NOT NULL DEFAULT '0',
  `motorcycleAmount` float NOT NULL DEFAULT '0',
  `graceperiodCount` int(11) NOT NULL DEFAULT '0',
  `graceperiodAmount` float NOT NULL DEFAULT '0',
  `lostCount` int(11) NOT NULL DEFAULT '0',
  `lostAmount` float NOT NULL DEFAULT '0',
  `deliveryCount` int(11) NOT NULL DEFAULT '0',
  `deliveryAmount` float NOT NULL DEFAULT '0',
  `seniorCount` int(11) NOT NULL DEFAULT '0',
  `seniorAmount` float NOT NULL DEFAULT '0',
  `nonqcseniorCount` int(11) NOT NULL DEFAULT '0',
  `nonqcseniorAmount` float NOT NULL DEFAULT '0',
  `bpomotorCount` int(11) NOT NULL DEFAULT '0',
  `bpomotorAmount` float NOT NULL DEFAULT '0',
  `promoCount` int(11) NOT NULL DEFAULT '0',
  `promoAmount` float NOT NULL DEFAULT '0',
  `pwdCount` int(11) NOT NULL DEFAULT '0',
  `pwdAmount` float NOT NULL DEFAULT '0',
  `jeepCount` int(11) NOT NULL DEFAULT '0',
  `jeepAmount` float NOT NULL DEFAULT '0',
  `tricycleCount` int(11) NOT NULL DEFAULT '0',
  `tricycleAmount` float NOT NULL DEFAULT '0',
  `bpoemployeeCount` int(11) NOT NULL DEFAULT '0',
  `bpoemployeeAmount` float NOT NULL DEFAULT '0',
  `employeesCount` int(11) NOT NULL DEFAULT '0',
  `employeesAmount` float NOT NULL DEFAULT '0',
  `tenantsCount` int(11) NOT NULL DEFAULT '0',
  `tenantsAmount` float NOT NULL DEFAULT '0',
  `voidsCount` int(11) NOT NULL DEFAULT '0',
  `voidsAmount` float NOT NULL DEFAULT '0',
  `refundCount` int(11) NOT NULL DEFAULT '0',
  `refundAmount` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`logINID`)
) ENGINE=InnoDB AUTO_INCREMENT=15400001000143224 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`logINID`, `SentinelID`, `userCode`, `userName`, `loginStamp`, `logoutStamp`, `extendedCount`, `extendedAmount`, `overnightCount`, `overnightAmount`, `carServed`, `totalAmount`, `regularCount`, `regularAmount`, `vipCount`, `vipAmount`, `motorcycleCount`, `motorcycleAmount`, `graceperiodCount`, `graceperiodAmount`, `lostCount`, `lostAmount`, `deliveryCount`, `deliveryAmount`, `seniorCount`, `seniorAmount`, `nonqcseniorCount`, `nonqcseniorAmount`, `bpomotorCount`, `bpomotorAmount`, `promoCount`, `promoAmount`, `pwdCount`, `pwdAmount`, `jeepCount`, `jeepAmount`, `tricycleCount`, `tricycleAmount`, `bpoemployeeCount`, `bpoemployeeAmount`, `employeesCount`, `employeesAmount`, `tenantsCount`, `tenantsAmount`, `voidsCount`, `voidsAmount`, `refundCount`, `refundAmount`) VALUES
(13600001000812, 'EX01', 'teller1', 'teller1', '2018-12-07 00:01:02', '2018-12-07 05:28:26', 0, 0, 0, 0, 122, 4728.75, 112, 4470, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 258.75, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(122000010007122, 'EX01', 'teller1', 'teller1', '2019-02-01 23:01:23', '2019-02-09 01:22:34', 0, 0, 29, 10200, 2022, 126273, 1952, 121890, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 66, 2842.03, 0, 0, 0, 0, 3, 1540, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(128000010007350, 'EX01', 'teller1', 'teller1', '2019-01-08 23:35:00', '2019-01-09 00:00:58', 0, 0, 2, 2000, 2, 2770, 2, 2770, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(128000010008122, 'EX01', 'teller1', 'teller1', '2019-01-09 00:01:22', '2019-01-09 13:38:57', 0, 0, 0, 0, 299, 14693.4, 291, 14410, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 283.386, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(129000020007525, 'EX01', 'teller2', 'teller2', '2019-01-09 23:05:25', '2019-01-09 23:42:06', 0, 0, 2, 2000, 5, 2925, 5, 2925, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(134000010007627, 'EX01', 'teller1', 'teller1', '2019-01-14 23:06:27', '2019-01-14 23:36:18', 0, 0, 1, 300, 1, 760, 1, 760, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(134000010009579, 'EX01', 'teller1', 'teller1', '2018-12-05 01:57:09', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(138000010007447, 'EX01', 'teller1', 'teller1', '2019-01-18 23:04:48', '2019-01-19 02:11:35', 0, 0, 1, 300, 13, 1255, 13, 1255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(140000020008950, 'EX01', 'teller2', 'teller2', '2018-12-11 00:09:50', '2018-12-11 00:12:05', 0, 0, 2, 600, 5, 1525, 5, 1525, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(142000010008596, 'EX01', 'teller1', 'teller1', '2018-12-13 00:59:06', '2018-12-13 14:00:33', 0, 0, 1, 1000, 335, 18108.1, 322, 17570, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 538.022, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(143000010006370, 'EX01', 'teller1', 'teller1', '2019-01-23 22:37:01', '2019-01-24 05:35:04', 0, 0, 3, 900, 105, 6606.25, 102, 6520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 86.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(143000010007143, 'EX01', 'teller1', 'teller1', '2018-12-13 23:14:03', '2018-12-14 14:24:15', 0, 0, 0, 0, 358, 18524, 347, 18220, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 303.92, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(148000010007320, 'EX01', 'teller1', 'teller1', '2018-12-18 23:03:20', '2018-12-19 23:14:16', 0, 0, 0, 0, 4, 215, 4, 215, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1260000100073644, 'EX01', 'teller1', 'teller1', '2019-01-06 23:36:44', '2019-01-07 23:23:02', 0, 0, 0, 0, 3, 105, 3, 105, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1270000100072316, 'EX01', 'teller1', 'teller1', '2019-01-07 23:23:16', '2019-01-08 23:34:17', 0, 0, 0, 0, 346, 18493.1, 329, 17840, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 653.022, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1290000100074228, 'EX01', 'teller1', 'teller1', '2019-01-09 23:42:28', '2019-01-10 11:34:25', 0, 0, 6, 8000, 624, 45358.5, 605, 44615, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 743.374, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1290000200019357, 'EX01', 'teller2', 'teller2', '2019-01-10 11:35:07', '2019-01-10 14:02:57', 0, 0, 0, 0, 28, 2780, 28, 2780, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1300000100065257, 'EX01', 'teller1', 'teller1', '2019-01-10 22:52:57', '2019-01-11 13:42:20', 0, 0, 3, 2400, 179, 12219.6, 173, 11965, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 254.636, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1310000100015519, 'EX01', 'teller1', 'teller1', '2019-02-11 07:05:19', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1310000100074141, 'EX01', 'teller1', 'teller1', '2019-01-11 23:41:41', '2019-01-12 07:26:22', 0, 0, 2, 600, 156, 9626.95, 151, 9360, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 266.954, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1310000200015280, 'EX01', 'teller2', 'teller2', '2019-01-12 07:28:00', '2019-01-12 13:32:20', 0, 0, 0, 0, 36, 2553.75, 35, 2525, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 28.75, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1320000100071721, 'EX01', 'teller1', 'teller1', '2019-01-12 23:17:21', '2019-01-13 08:26:29', 0, 0, 3, 900, 170, 9441.06, 166, 9285, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 156.068, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1330000100016329, 'EX01', 'teller1', 'teller1', '2018-12-04 08:32:09', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1330000100017274, 'EX01', 'teller1', 'teller1', '2018-12-04 09:27:04', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1330000200015277, 'EX01', 'teller2', 'teller2', '2018-12-04 07:27:07', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1330000200016315, 'EX01', 'teller2', 'teller2', '2018-12-04 08:31:05', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000100011036, 'EX01', '00001000', 'teller1', '2018-09-08 03:00:36', '2018-09-08 03:01:04', 0, 0, 0, 0, 1, 50, 1, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000100011122, 'EX01', 'teller1', 'teller1', '2018-12-05 03:01:22', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000100074325, 'EX01', 'teller1', 'teller1', '2019-01-14 23:43:25', '2019-01-15 13:37:19', 0, 0, 3, 900, 290, 16756, 273, 16255, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 11, 501.056, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000100095751, 'EX01', 'teller1', 'teller1', '2018-12-05 01:57:51', '0000-00-00 00:00:00', 0, 0, 0, 0, 3, 105, 3, 105, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200011248, 'EX01', '00002000', 'teller2', '2018-09-08 03:02:48', '2018-09-10 03:21:21', 0, 0, 0, 0, 10, 420, 9, 420, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200011338, 'EX01', 'teller2', 'teller2', '2018-12-05 03:03:38', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200011547, 'EX01', 'teller2', 'teller2', '2018-12-05 03:05:47', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1340000200084643, 'EX01', 'teller2', 'teller2', '2018-12-05 00:46:43', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1350000100011373, 'EX01', 'teller1', 'teller1', '2019-02-09 01:22:57', '2019-02-11 07:04:34', 0, 0, 8, 2400, 515, 32337.4, 491, 28560, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 3432.41, 0, 0, 0, 0, 3, 310, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1350000100064244, 'EX01', 'teller1', 'teller1', '2019-01-15 22:42:45', '2019-01-16 22:36:21', 0, 0, 1, 300, 344, 19060.5, 333, 18580, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 480.522, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1350000100065856, 'EX01', 'teller1', 'teller1', '2018-12-05 22:58:56', '2018-12-06 00:25:06', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1350000100082645, 'EX01', 'teller1', 'teller1', '2018-12-06 00:26:45', '2018-12-06 05:44:06', 0, 0, 0, 0, 69, 2540, 65, 2425, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 115, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1360000100063641, 'EX01', 'teller1', 'teller1', '2019-01-16 22:36:42', '2019-01-17 05:41:05', 0, 0, 5, 1500, 125, 8347.32, 114, 7990, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 357.318, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1370000100015442, 'EX01', '00001000', 'teller1', '2018-09-11 07:04:42', '2018-11-10 08:30:04', 0, 0, 0, 0, 13, 885, 11, 885, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1370000100063447, 'EX01', 'teller1', 'teller1', '2019-01-17 22:34:47', '2019-01-18 05:28:03', 0, 0, 7, 2400, 113, 10808, 107, 10615, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 193.034, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1370000100074440, 'EX01', 'teller1', 'teller1', '2018-12-07 23:44:40', '2018-12-08 13:59:05', 0, 0, 0, 0, 140, 7431.25, 136, 7210, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 221.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1370000200022743, 'EX01', 'teller2', 'teller2', '2018-12-08 14:07:43', '2018-12-08 23:37:25', 0, 0, 5, 5000, 5, 6850, 5, 6850, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1380000100016326, 'EX01', '00001000', 'teller1', '2018-11-10 08:32:06', '2018-11-22 03:13:22', 0, 0, 0, 0, 12, 175, 5, 175, 4, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1380000100073833, 'EX01', 'teller1', 'teller1', '2018-12-08 23:38:33', '2018-12-09 12:41:56', 0, 0, 3, 3000, 253, 16500, 241, 16020, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 480, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1390000100064922, 'EX01', 'teller1', 'teller1', '2019-01-19 22:49:23', '2019-01-20 12:49:27', 0, 0, 8, 2400, 261, 19474.3, 252, 19195, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 279.284, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1390000100065942, 'EX01', 'teller1', 'teller1', '2018-12-09 22:59:42', '2018-12-10 02:44:23', 0, 0, 0, 0, 23, 798.75, 22, 770, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 28.75, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1400000100063034, 'EX01', 'teller1', 'teller1', '2019-01-20 22:30:34', '2019-01-21 13:27:18', 0, 0, 1, 300, 309, 18253.3, 298, 17855, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 398.386, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1400000100081224, 'EX01', 'teller1', 'teller1', '2018-12-11 00:12:24', '2018-12-11 13:31:56', 0, 0, 5, 1500, 325, 20473.8, 319, 20100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 373.738, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1410000100063637, 'EX01', 'teller1', 'teller1', '2019-01-21 22:36:37', '2019-01-22 13:04:24', 0, 0, 1, 300, 313, 17324.5, 300, 16725, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 599.624, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1410000100081330, 'EX01', 'teller1', 'teller1', '2018-12-12 00:13:30', '2018-12-12 13:18:09', 0, 0, 0, 0, 325, 15850, 314, 15390, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 459.988, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1420000100064521, 'EX01', 'teller1', 'teller1', '2019-01-22 22:45:21', '2019-01-23 13:29:42', 0, 0, 2, 900, 338, 19920.5, 318, 19140, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 780.34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1430000200013386, 'EX01', 'teller2', 'teller2', '2019-01-24 05:38:06', '2019-01-24 13:04:16', 0, 0, 0, 0, 190, 11771.4, 185, 11525, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 246.42, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1440000100011596, 'EX01', '00001000', 'teller1', '2019-02-24 03:59:06', '0000-00-00 00:00:00', 0, 0, 3, 3000, 15, 3705, 15, 3705, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1440000100064116, 'EX01', 'teller1', 'teller1', '2019-01-24 22:41:16', '2019-01-25 05:33:01', 0, 0, 5, 1500, 110, 8599.09, 104, 8365, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 234.102, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1440000100075431, 'EX01', 'teller1', 'teller1', '2018-12-14 23:54:31', '2018-12-17 23:20:26', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1450000100065444, 'EX01', 'teller1', 'teller1', '2019-01-25 22:54:45', '2019-01-26 13:25:06', 0, 0, 2, 600, 140, 7733.03, 138, 7655, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 78.034, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1460000100071240, 'EX01', 'teller1', 'teller1', '2019-01-26 23:12:41', '2019-01-27 12:38:47', 0, 0, 0, 0, 233, 11490.7, 223, 11080, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 410.704, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1470000100063822, 'EX01', 'teller1', 'teller1', '2019-01-27 22:38:22', '2019-02-01 23:00:21', 0, 0, 1, 600, 288, 16295.3, 283, 16090, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 205.352, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1470000100072049, 'EX01', 'teller1', 'teller1', '2018-12-17 23:20:49', '2018-12-18 23:03:04', 0, 0, 2, 2000, 325, 19211.5, 315, 18850, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 361.42, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1490000100071434, 'EX01', 'teller1', 'teller1', '2018-12-19 23:14:35', '2018-12-20 05:55:26', 0, 0, 2, 2000, 120, 7961.6, 115, 7750, 0, 0, 1, 35, 0, 0, 0, 0, 0, 0, 4, 176.602, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(1500000100084838, 'EX01', 'teller1', 'teller1', '2018-12-21 00:48:38', '2019-01-06 23:36:04', 0, 0, 3, 479002000000, 3, 22190, 3, 22190, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13200002000162733, 'EX01', 'teller2', 'teller2', '2019-01-13 08:27:33', '2019-01-14 05:29:23', 0, 0, 3, 900, 239, 12770.3, 230, 12450, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 320.352, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000163511, 'EX01', 'teller1', 'teller1', '2018-12-04 08:35:11', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000164325, 'EX01', 'teller1', 'teller1', '2018-12-04 08:43:25', '0000-00-00 00:00:00', 0, 0, 0, 0, 5, 175, 5, 175, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000165140, 'EX01', 'teller1', 'teller1', '2018-12-04 08:51:40', '0000-00-00 00:00:00', 0, 0, 0, 0, 4, 140, 4, 140, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000165530, 'EX01', 'teller1', 'teller1', '2018-12-04 08:55:30', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000171746, 'EX01', 'teller1', 'teller1', '2018-12-04 09:17:46', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000171832, 'EX01', 'teller1', 'teller1', '2018-12-04 09:18:32', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300001000174536, 'EX01', 'teller1', 'teller1', '2018-12-04 09:45:36', '0000-00-00 00:00:00', 0, 0, 0, 0, 2, 170, 2, 170, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300002000132946, 'EX01', 'teller2', 'teller2', '2019-01-14 05:29:46', '2019-01-14 13:50:47', 0, 0, 0, 0, 227, 13538.5, 217, 13095, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 443.556, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300002000152859, 'EX01', 'teller2', 'teller2', '2018-12-04 07:28:59', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300002000152948, 'EX01', 'teller2', 'teller2', '2018-12-04 07:29:48', '0000-00-00 00:00:00', 0, 0, 0, 0, 1, 35, 1, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13300002000163946, 'EX01', 'teller2', 'teller2', '2018-12-04 08:39:46', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400001000101622, 'EX01', 'teller1', 'teller1', '2018-12-05 02:16:22', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400001000105853, 'EX01', 'teller1', 'teller1', '2018-12-05 02:58:53', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400001000151046, 'EX01', 'teller1', 'teller1', '2018-12-05 07:10:46', '2018-12-05 07:15:18', 0, 0, 0, 0, 4, 140, 4, 140, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400002000114718, 'EX01', 'teller2', 'teller2', '2018-12-05 03:47:19', '2018-12-05 03:47:34', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13400002000115350, 'EX01', 'teller2', 'teller2', '2018-12-05 03:53:50', '2018-12-05 07:10:19', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13500002000134448, 'EX01', 'teller2', 'teller2', '2018-12-06 05:44:48', '2018-12-06 23:58:48', 0, 0, 0, 0, 185, 10961.2, 180, 10875, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 86.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13600001000112154, 'EX01', '00001000', 'teller1', '2018-09-10 03:21:54', '2018-09-11 07:03:49', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13600002000132925, 'EX01', 'teller2', 'teller2', '2018-12-07 05:29:25', '2018-12-07 14:29:17', 0, 0, 3, 3000, 205, 17826.2, 199, 17625, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 201.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13600002000134117, 'EX01', 'teller2', 'teller2', '2019-01-17 05:41:17', '2019-01-17 13:22:53', 0, 0, 0, 0, 191, 12711, 186, 12485, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 225.886, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13700001000205921, 'EX01', '00001000', 'teller1', '2019-02-17 12:59:21', '0000-00-00 00:00:00', 0, 0, 23, 6900, 175, 19815, 173, 19505, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 310, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13700002000132816, 'EX01', 'teller2', 'teller2', '2019-01-18 05:28:16', '2019-01-18 13:41:26', 0, 0, 0, 0, 195, 11734.8, 190, 11550, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 184.818, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13800001000104217, 'EX01', 'teller1', 'teller1', '2019-01-19 02:42:18', '2019-01-19 13:46:19', 0, 0, 3, 900, 166, 10544.8, 158, 10130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 414.806, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(13900001000104529, 'EX01', 'teller1', 'teller1', '2018-12-10 02:45:30', '2018-12-10 13:07:02', 0, 0, 0, 0, 208, 10951.2, 200, 10750, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 201.25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14000002000213230, 'EX01', 'teller2', 'teller2', '2018-12-11 13:32:30', '2018-12-12 00:13:00', 0, 0, 1, 1000, 2, 1520, 2, 1520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14100002000211837, 'EX01', 'teller2', 'teller2', '2018-12-12 13:18:37', '2018-12-13 00:58:43', 0, 0, 0, 0, 5, 193.75, 4, 165, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 28.75, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14400001000115533, 'EX01', '00001000', 'teller1', '2019-02-24 03:55:33', '0000-00-00 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14400002000133319, 'EX01', 'teller2', 'teller2', '2019-01-25 05:33:19', '2019-01-25 13:21:32', 0, 0, 0, 0, 208, 13069.3, 201, 12560, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 509.26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14500001000201514, 'EX01', '00001000', 'teller1', '2019-07-20 12:15:18', '0000-00-00 00:00:00', 0, 0, 1, 0, 15, 468.56, 8, 268.57, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 228.56, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(14900002000135543, 'EX01', 'teller2', 'teller2', '2018-12-20 05:55:43', '2018-12-20 13:19:28', 0, 0, 0, 0, 197, 10941.8, 190, 10775, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 106.784, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15000002000111414, 'EX01', '00002000', 'teller2', '2018-11-22 03:14:14', '2018-11-23 04:22:56', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15100001000122326, 'EX01', '00001000', 'teller1', '2018-11-23 04:23:27', '2018-11-23 05:49:27', 0, 0, 0, 0, 2, 70, 2, 70, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15100002000134954, 'EX01', '00002000', 'teller2', '2018-11-23 05:49:54', '0000-00-00 00:00:00', 0, 0, 0, 0, 112, 4370.5, 109, 4265, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(15400001000143223, 'EX01', 'teller1', 'teller1', '2018-11-26 06:32:23', '0000-00-00 00:00:00', 0, 0, 0, 0, 12, 520, 11, 520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
--
-- Database: `crdplt`
--
DROP DATABASE IF EXISTS `crdplt`;
CREATE DATABASE IF NOT EXISTS `crdplt` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `crdplt`;

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `areaID` varchar(4) NOT NULL,
  `entranceID` varchar(4) NOT NULL,
  `cardNumber` varchar(10) NOT NULL,
  `plateNumber` varchar(8) NOT NULL,
  `trtype` varchar(2) NOT NULL,
  `isLost` tinyint(1) NOT NULL,
  `gSStamp` varchar(12) NOT NULL DEFAULT '0',
  `datetimeIN` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datetimeINStamp` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`cardNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`areaID`, `entranceID`, `cardNumber`, `plateNumber`, `trtype`, `isLost`, `gSStamp`, `datetimeIN`, `datetimeINStamp`) VALUES
('P1', 'EN01', '01465BA9', 'CNV457', 'R', 0, '0', '2019-07-08 19:55:30', '1562615730'),
('P1', 'EN01', '11E920A9', 'HJKDF45', 'R', 0, '0', '2019-07-08 19:55:12', '1562615712'),
('P1', 'EN01', '31474FA9', 'XKS346', 'R', 0, '0', '2019-07-08 19:55:36', '1562615736'),
('P1', 'EN01', '41DD4BA9', 'XLC3265', 'R', 0, '0', '2019-07-08 19:28:03', '1562614083'),
('P1', 'EN01', '617E50A9', 'BDF545', 'R', 0, '0', '2019-07-08 19:55:24', '1562615724'),
('P1', 'EN01', '61B050A9', 'NCB356', 'R', 0, '0', '2019-07-08 19:55:18', '1562615718'),
('P1', 'EN01', 'A1774CA9', 'ZLSS346', 'R', 0, '0', '2019-07-08 19:55:41', '1562615741');
--
-- Database: `detectors`
--
DROP DATABASE IF EXISTS `detectors`;
CREATE DATABASE IF NOT EXISTS `detectors` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `detectors`;

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detectorName` varchar(20) NOT NULL,
  `minimum` int(11) NOT NULL,
  `maximum` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`id`, `detectorName`, `minimum`, `maximum`) VALUES
(1, 'Detector1', 51, 2000),
(2, 'Detector2', 52, 2000),
(3, 'Detector3', 53, 2000),
(4, 'Detector4', 54, 2000),
(5, 'Detector5', 55, 2000);
--
-- Database: `extcrd`
--
DROP DATABASE IF EXISTS `extcrd`;
CREATE DATABASE IF NOT EXISTS `extcrd` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `extcrd`;

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `areaID` varchar(4) NOT NULL,
  `entranceID` varchar(4) NOT NULL,
  `cardNumber` varchar(8) NOT NULL,
  `plateNumber` varchar(8) NOT NULL,
  `trtype` varchar(2) NOT NULL,
  `isLost` tinyint(1) NOT NULL,
  `datetimeIN` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datetimeINStamp` varchar(12) DEFAULT NULL,
  `datetimePaid` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `datetimePaidStamp` varchar(12) DEFAULT NULL,
  `datetimeNextDue` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `datetimeNextDueStamp` varchar(12) DEFAULT '0',
  `amountPaid` float NOT NULL,
  PRIMARY KEY (`cardNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- Database: `logs`
--
DROP DATABASE IF EXISTS `logs`;
CREATE DATABASE IF NOT EXISTS `logs` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `logs`;

-- --------------------------------------------------------

--
-- Table structure for table `activitycodes`
--

DROP TABLE IF EXISTS `activitycodes`;
CREATE TABLE IF NOT EXISTS `activitycodes` (
  `pkId` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`pkId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `activitycodes`
--

INSERT INTO `activitycodes` (`pkId`, `code`, `description`) VALUES
(1, '01', 'Program Reboot'),
(2, 'L1', 'Login'),
(3, 'L0', 'Logout'),
(4, 'XE', 'POS Being used while Deactivated'),
(5, 'Z1', 'Print today\'s ZReading using Mastercard 1'),
(6, 'V0', 'Void Transaction Started'),
(7, 'E0', 'Exit Processed'),
(8, 'LC', 'Lost Card Created');

-- --------------------------------------------------------

--
-- Table structure for table `audit`
--

DROP TABLE IF EXISTS `audit`;
CREATE TABLE IF NOT EXISTS `audit` (
  `transactionID` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sentinelID` varchar(4) NOT NULL,
  `activityCode` varchar(3) DEFAULT NULL,
  `activityOwner` varchar(50) DEFAULT NULL,
  `activityDate` timestamp NULL DEFAULT NULL,
  `activityDetails` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`transactionID`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `audit`
--

INSERT INTO `audit` (`transactionID`, `sentinelID`, `activityCode`, `activityOwner`, `activityDate`, `activityDetails`) VALUES
(1, 'EX01', '01', '', '2019-07-08 19:46:15', NULL),
(2, 'EX01', 'L1', '00001000', '2019-07-08 19:46:28', NULL),
(3, 'EX01', 'E0', '00001000', '2019-07-08 19:47:05', 'EX01000000000001'),
(4, 'EX01', 'E0', '00001000', '2019-07-08 19:47:39', 'EX01000000000002'),
(5, 'EX01', 'E0', '00001000', '2019-07-08 19:48:12', 'EX01000000000003'),
(6, 'EX01', 'E0', '00001000', '2019-07-08 19:48:47', 'EX01000000000004'),
(7, 'EX01', 'E0', '00001000', '2019-07-08 19:49:26', 'EX01000000000005'),
(8, 'EX01', 'E0', '00001000', '2019-07-08 19:49:54', 'EX01000000000006'),
(9, 'EX01', 'E0', '00001000', '2019-07-08 19:50:41', 'EX01000000000007'),
(10, 'EX01', 'E0', '00001000', '2019-07-08 19:51:46', 'EX01000000000008'),
(11, 'EX01', 'E0', '00001000', '2019-07-08 20:07:31', 'EX01000000000009'),
(12, 'EX01', 'E0', '00001000', '2019-07-08 20:08:39', 'EX01000000000010'),
(13, 'EX01', 'E0', '00001000', '2019-07-08 20:09:11', 'EX01000000000011'),
(14, 'EX01', 'E0', '00001000', '2019-07-08 20:09:47', 'EX01000000000012'),
(15, 'EX01', 'E0', '00001000', '2019-07-08 20:10:41', 'EX01000000000013'),
(16, 'EX01', 'LC', '00001000', '2019-07-08 20:11:34', '41F348A9'),
(17, 'EX01', 'E0', '00001000', '2019-07-08 20:11:51', 'EX01000000000014'),
(18, 'EX01', 'L0', '00001000', '2019-07-08 20:20:58', NULL),
(19, 'EX01', 'L1', '00001000', '2019-07-08 20:21:38', NULL),
(20, 'EX01', 'RX', 'Angelo Dizon', '2019-07-08 20:22:43', '1340000100042138'),
(21, 'EX01', 'RX', 'Angelo Dizon', '2019-07-08 20:24:38', '1340000100042138'),
(22, 'EX01', '01', '00001000', '2019-07-08 20:40:12', NULL),
(23, 'EX01', '01', '00001000', '2019-07-08 20:42:16', NULL),
(24, 'EX01', 'XE', '00001000', '2019-07-08 20:45:09', NULL),
(25, 'EX01', 'XE', '00001000', '2019-07-08 20:45:11', NULL),
(26, 'EX01', 'XE', '00001000', '2019-07-08 20:45:20', NULL),
(27, 'EX01', '01', '00001000', '2019-07-08 20:47:15', NULL),
(28, 'EX01', '01', '00001000', '2019-07-18 01:32:47', NULL),
(29, 'EX01', '01', '00001000', '2019-07-18 01:34:21', NULL),
(30, 'EX01', '01', '00001000', '2019-07-18 10:28:32', NULL),
(31, 'EX01', '01', '00001000', '2019-07-18 10:29:35', NULL),
(32, 'EX01', '01', '00001000', '2019-07-18 10:38:49', NULL),
(33, 'EX01', '01', '00001000', '2019-07-18 10:39:55', NULL),
(34, 'EX01', '01', '00001000', '2019-07-18 20:27:54', NULL),
(35, 'EX01', '01', '00001000', '2019-07-18 20:56:07', NULL),
(36, 'EX01', '01', '00001000', '2019-07-18 20:59:25', NULL),
(37, 'EX01', '01', '00001000', '2019-07-18 21:01:30', NULL),
(38, 'EX01', '01', '00001000', '2019-07-18 21:10:58', NULL),
(39, 'EX01', '01', '00001000', '2019-07-18 21:15:56', NULL),
(40, 'EX01', '01', '00001000', '2019-07-18 21:48:46', NULL),
(41, 'EX01', '01', '00001000', '2019-07-19 00:56:45', NULL),
(42, 'EX01', '01', '00001000', '2019-07-19 00:59:01', NULL),
(43, 'EX01', '01', '00001000', '2019-07-19 01:01:56', NULL),
(44, 'EX01', '01', '00001000', '2019-07-19 01:03:14', NULL),
(45, 'EX01', '01', '00001000', '2019-07-19 01:04:05', NULL),
(46, 'EX01', '01', '00001000', '2019-07-19 01:04:52', NULL),
(47, 'EX01', '01', '00001000', '2019-07-19 03:53:14', NULL),
(48, 'EX01', 'L0', '00001000', '2019-07-19 03:53:43', NULL),
(49, 'EX01', '01', '', '2019-07-19 03:57:42', NULL),
(50, 'EX01', 'L1', '00001000', '2019-07-19 03:57:56', NULL),
(51, 'EX01', '01', '', '2019-07-20 12:14:36', NULL),
(52, 'EX01', 'L1', '00001000', '2019-07-20 12:15:17', NULL),
(53, 'EX01', '01', '00001000', '2019-07-20 12:22:53', NULL),
(54, 'EX01', '01', '00001000', '2019-07-20 12:29:46', NULL),
(55, 'EX01', '01', '00001000', '2019-07-20 12:30:55', NULL),
(56, 'EX01', '01', '00001000', '2019-07-20 12:32:29', NULL),
(57, 'EX01', '01', '00001000', '2019-07-20 12:33:01', NULL),
(58, 'EX01', '01', '00001000', '2019-07-20 12:40:33', NULL),
(59, 'EX01', '01', '00001000', '2019-07-20 12:43:17', NULL),
(60, 'EX01', '01', '00001000', '2019-07-20 14:09:28', NULL),
(61, 'EX01', '01', '00001000', '2019-07-20 14:10:48', NULL),
(62, 'EX01', '01', '00001000', '2019-07-20 14:13:33', NULL),
(63, 'EX01', '01', '00001000', '2019-07-20 14:14:13', NULL),
(64, 'EX01', '01', '00001000', '2019-07-20 14:16:34', NULL),
(65, 'EX01', '01', '00001000', '2019-07-20 14:19:26', NULL),
(66, 'EX01', '01', '00001000', '2019-07-20 14:22:03', NULL),
(67, 'EX01', '01', '00001000', '2019-07-20 14:24:14', NULL),
(68, 'EX01', '01', '00001000', '2019-07-20 14:26:06', NULL),
(69, 'EX01', '01', '00001000', '2019-07-20 14:27:21', NULL),
(70, 'EX01', '01', '00001000', '2019-07-20 14:31:37', NULL),
(71, 'EX01', '01', '00001000', '2019-07-20 14:33:33', NULL),
(72, 'EX01', '01', '00001000', '2019-07-20 14:35:04', NULL),
(73, 'EX01', '01', '00001000', '2019-07-20 14:37:44', NULL),
(74, 'EX01', '01', '00001000', '2019-07-21 00:34:14', NULL),
(75, 'EX01', '01', '00001000', '2019-07-21 00:52:50', NULL),
(76, 'EX01', '01', '00001000', '2019-07-21 00:56:08', NULL),
(77, 'EX01', '01', '00001000', '2019-07-21 00:58:35', NULL),
(78, 'EX01', '01', '00001000', '2019-07-21 01:06:59', NULL),
(79, 'EX01', '01', '00001000', '2019-07-21 01:09:35', NULL),
(80, 'EX01', '01', '00001000', '2019-07-21 01:25:46', NULL),
(81, 'EX01', '01', '00001000', '2019-07-21 01:26:57', NULL),
(82, 'EX01', '01', '00001000', '2019-07-21 01:28:47', NULL),
(83, 'EX01', '01', '00001000', '2019-07-21 01:29:36', NULL),
(84, 'EX01', '01', '00001000', '2019-07-21 01:31:10', NULL),
(85, 'EX01', '01', '00001000', '2019-07-21 01:32:30', NULL),
(86, 'EX01', '01', '00001000', '2019-07-21 01:36:02', NULL),
(87, 'EX01', '01', '00001000', '2019-07-21 01:36:33', NULL),
(88, 'EX01', 'E0', '00001000', '2019-07-21 01:40:38', 'EX01000000000015'),
(89, 'EX01', '01', '00001000', '2019-07-21 02:05:03', NULL),
(90, 'EX01', '01', '00001000', '2019-07-21 02:07:52', NULL),
(91, 'EX01', '01', '00001000', '2019-07-21 02:11:53', NULL),
(92, 'EX01', '01', '00001000', '2019-07-21 02:13:58', NULL),
(93, 'EX01', '01', '00001000', '2019-07-21 02:25:24', NULL),
(94, 'EX01', '01', '00001000', '2019-07-21 02:27:16', NULL),
(95, 'EX01', '01', '00001000', '2019-07-21 02:31:16', NULL),
(96, 'EX01', '01', '00001000', '2019-07-21 02:50:41', NULL),
(97, 'EX01', '01', '00001000', '2019-07-21 03:01:56', NULL),
(98, 'EX01', '01', '00001000', '2019-07-21 03:02:42', NULL),
(99, 'EX01', '01', '00001000', '2019-07-21 03:09:29', NULL),
(100, 'EX01', '01', '00001000', '2019-07-21 03:16:14', NULL),
(101, 'EX01', '01', '00001000', '2019-07-21 03:17:28', NULL),
(102, 'EX01', '01', '00001000', '2019-07-21 03:26:13', NULL),
(103, 'EX01', '01', '00001000', '2019-07-21 03:30:27', NULL),
(104, 'EX01', '01', '00001000', '2019-07-21 03:39:13', NULL),
(105, 'EX01', '01', '00001000', '2019-07-21 03:39:55', NULL),
(106, 'EX01', '01', '00001000', '2019-07-21 03:46:27', NULL),
(107, 'EX01', '01', '00001000', '2019-07-21 03:58:18', NULL),
(108, 'EX01', '01', '00001000', '2019-07-21 04:03:00', NULL),
(109, 'EX01', '01', '00001000', '2019-07-21 04:08:50', NULL),
(110, 'EX01', '01', '00001000', '2019-07-21 04:30:43', NULL),
(111, 'EX01', '01', '00001000', '2019-07-21 04:32:42', NULL),
(112, 'EX01', '01', '00001000', '2019-07-21 04:48:06', NULL),
(113, 'EX01', '01', '00001000', '2019-07-21 11:46:27', NULL),
(114, 'EX01', '01', '00001000', '2019-07-21 11:49:19', NULL),
(115, 'EX01', '01', '00001000', '2019-07-21 11:51:29', NULL),
(116, 'EX01', '01', '00001000', '2019-07-21 11:56:11', NULL),
(117, 'EX01', '01', '00001000', '2019-07-21 11:58:16', NULL),
(118, 'EX01', '01', '00001000', '2019-07-21 12:00:11', NULL),
(119, 'EX01', '01', '00001000', '2019-07-21 12:05:54', NULL),
(120, 'EX01', '01', '00001000', '2019-07-21 12:08:02', NULL),
(121, 'EX01', '01', '00001000', '2019-07-21 12:09:08', NULL),
(122, 'EX01', '01', '00001000', '2019-07-21 12:17:52', NULL),
(123, 'EX01', '01', '00001000', '2019-07-21 12:26:11', NULL),
(124, 'EX01', '01', '00001000', '2019-07-21 12:27:42', NULL),
(125, 'EX01', '01', '00001000', '2019-07-21 12:28:18', NULL),
(126, 'EX01', '01', '00001000', '2019-07-21 12:34:02', NULL),
(127, 'EX01', '01', '00001000', '2019-07-21 12:36:55', NULL),
(128, 'EX01', '01', '00001000', '2019-07-21 12:54:11', NULL),
(129, 'EX01', '01', '00001000', '2019-07-21 13:01:53', NULL),
(130, 'EX01', 'E0', '00001000', '2019-07-21 13:02:33', 'EX01000000000016'),
(131, 'EX01', 'E0', '00001000', '2019-07-21 13:02:45', 'EX01000000000017'),
(132, 'EX01', '01', '00001000', '2019-07-21 13:09:14', NULL),
(133, 'EX01', '01', '00001000', '2019-07-21 13:44:41', NULL),
(134, 'EX01', '01', '00001000', '2019-07-21 13:50:00', NULL),
(135, 'EX01', '01', '00001000', '2019-07-21 13:53:02', NULL),
(136, 'EX01', '01', '00001000', '2019-07-21 13:54:09', NULL),
(137, 'EX01', '01', '00001000', '2019-07-21 13:57:18', NULL),
(138, 'EX01', '01', '00001000', '2019-07-21 13:58:22', NULL),
(139, 'EX01', '01', '00001000', '2019-07-21 14:03:05', NULL),
(140, 'EX01', 'E0', '00001000', '2019-07-21 14:03:55', 'EX01000000000018'),
(141, 'EX01', '01', '00001000', '2019-07-22 18:33:42', NULL),
(142, 'EX01', '01', '00001000', '2019-07-22 18:36:04', NULL),
(143, 'EX01', '01', '00001000', '2019-07-22 18:42:09', NULL),
(144, 'EX01', '01', '00001000', '2019-07-22 18:45:00', NULL),
(145, 'EX01', '01', '00001000', '2019-07-22 18:45:48', NULL),
(146, 'EX01', '01', '00001000', '2019-07-22 19:00:07', NULL),
(147, 'EX01', '01', '00001000', '2019-07-22 19:04:30', NULL),
(148, 'EX01', '01', '00001000', '2019-07-22 19:08:58', NULL),
(149, 'EX01', '01', '00001000', '2019-07-22 19:15:00', NULL),
(150, 'EX01', '01', '00001000', '2019-07-22 19:16:07', NULL),
(151, 'EX01', '01', '00001000', '2019-07-22 19:19:11', NULL),
(152, 'EX01', '01', '00001000', '2019-07-22 19:20:43', NULL),
(153, 'EX01', '01', '00001000', '2019-07-22 19:26:43', NULL),
(154, 'EX01', '01', '00001000', '2019-07-22 19:28:26', NULL),
(155, 'EX01', '01', '00001000', '2019-07-22 19:29:55', NULL),
(156, 'EX01', '01', '00001000', '2019-07-22 22:32:22', NULL),
(157, 'EX01', '01', '00001000', '2019-07-22 22:34:26', NULL),
(158, 'EX01', '01', '00001000', '2019-07-22 22:37:41', NULL),
(159, 'EX01', '01', '00001000', '2019-07-22 22:40:00', NULL),
(160, 'EX01', '01', '00001000', '2019-07-22 23:00:52', NULL),
(161, 'EX01', '01', '00001000', '2019-07-22 23:07:56', NULL),
(162, 'EX01', '01', '00001000', '2019-07-22 23:15:26', NULL),
(163, 'EX01', '01', '00001000', '2019-07-22 23:17:29', NULL),
(164, 'EX01', '01', '00001000', '2019-07-22 23:19:47', NULL),
(165, 'EX01', '01', '00001000', '2019-07-22 23:21:51', NULL),
(166, 'EX01', '01', '00001000', '2019-07-22 23:23:26', NULL),
(167, 'EX01', '01', '00001000', '2019-07-22 23:25:07', NULL),
(168, 'EX01', '01', '00001000', '2019-07-22 23:26:57', NULL),
(169, 'EX01', '01', '00001000', '2019-07-22 23:27:47', NULL),
(170, 'EX01', '01', '00001000', '2019-07-22 23:28:32', NULL),
(171, 'EX01', '01', '00001000', '2019-07-22 23:29:09', NULL),
(172, 'EX01', '01', '00001000', '2019-07-22 23:29:39', NULL),
(173, 'EX01', '01', '00001000', '2019-07-22 23:31:06', NULL),
(174, 'EX01', '01', '00001000', '2019-07-22 23:33:19', NULL),
(175, 'EX01', '01', '00001000', '2019-07-22 23:33:37', NULL),
(176, 'EX01', '01', '00001000', '2019-07-22 23:34:21', NULL),
(177, 'EX01', '01', '00001000', '2019-07-22 23:35:19', NULL),
(178, 'EX01', '01', '00001000', '2019-07-23 02:56:01', NULL),
(179, 'EX01', '01', '00001000', '2019-07-23 03:21:20', NULL),
(180, 'EX01', '01', '00001000', '2019-07-23 15:15:28', NULL),
(181, 'EX01', '01', '00001000', '2019-07-23 15:16:29', NULL),
(182, 'EX01', '01', '00001000', '2019-07-23 22:18:28', NULL),
(183, 'EX01', '01', '00001000', '2019-07-23 22:25:56', NULL),
(184, 'EX01', '01', '00001000', '2019-07-23 22:26:57', NULL),
(185, 'EX01', '01', '00001000', '2019-07-23 22:41:36', NULL),
(186, 'EX01', '01', '00001000', '2019-07-23 22:43:41', NULL),
(187, 'EX01', '01', '00001000', '2019-07-23 22:44:39', NULL),
(188, 'EX01', '01', '00001000', '2019-07-23 22:48:26', NULL),
(189, 'EX01', '01', '00001000', '2019-07-23 22:49:50', NULL),
(190, 'EX01', '01', '00001000', '2019-07-23 22:50:38', NULL),
(191, 'EX01', '01', '00001000', '2019-07-23 22:54:29', NULL),
(192, 'EX01', '01', '00001000', '2019-07-23 22:56:35', NULL),
(193, 'EX01', '01', '00001000', '2019-07-23 22:58:54', NULL),
(194, 'EX01', '01', '00001000', '2019-07-23 23:00:33', NULL),
(195, 'EX01', '01', '00001000', '2019-07-23 23:01:41', NULL),
(196, 'EX01', '01', '00001000', '2019-07-23 23:02:32', NULL),
(197, 'EX01', '01', '00001000', '2019-07-23 23:03:54', NULL),
(198, 'EX01', '01', '00001000', '2019-07-23 23:04:45', NULL),
(199, 'EX01', '01', '00001000', '2019-07-23 23:05:24', NULL),
(200, 'EX01', '01', '00001000', '2019-07-23 23:06:37', NULL),
(201, 'EX01', '01', '00001000', '2019-07-23 23:08:21', NULL),
(202, 'EX01', '01', '00001000', '2019-07-23 23:51:03', NULL),
(203, 'EX01', '01', '00001000', '2019-07-23 23:59:44', NULL),
(204, 'EX01', '01', '00001000', '2019-07-24 03:00:58', NULL),
(205, 'EX01', '01', '00001000', '2019-07-24 06:06:01', NULL),
(206, 'EX01', '01', '00001000', '2019-07-28 11:24:04', NULL),
(207, 'EX01', '01', '00001000', '2019-07-28 11:30:42', NULL),
(208, 'EX01', '01', '00001000', '2019-07-28 11:31:52', NULL),
(209, 'EX01', '01', '00001000', '2019-07-28 11:35:22', NULL),
(210, 'EX01', '01', '00001000', '2019-07-28 11:39:13', NULL),
(211, 'EX01', '01', '00001000', '2019-07-28 16:08:19', NULL),
(212, 'EX01', '01', '00001000', '2019-07-28 16:13:36', NULL),
(213, 'EX01', '01', '00001000', '2019-07-28 16:15:44', NULL),
(214, 'EX01', '01', '00001000', '2019-07-28 16:24:53', NULL),
(215, 'EX01', '01', '00001000', '2019-07-28 16:25:34', NULL),
(216, 'EX01', '01', '00001000', '2019-07-28 16:32:33', NULL),
(217, 'EX01', '01', '00001000', '2019-07-28 16:40:26', NULL),
(218, 'EX01', '01', '00001000', '2019-07-28 16:43:36', NULL),
(219, 'EX01', '01', '00001000', '2019-07-28 16:46:32', NULL),
(220, 'EX01', '01', '00001000', '2019-07-28 16:59:23', NULL),
(221, 'EX01', '01', '00001000', '2019-07-28 17:38:50', NULL),
(222, 'EX01', '01', '00001000', '2019-07-28 17:55:21', NULL),
(223, 'EX01', '01', '00001000', '2019-07-28 18:21:49', NULL),
(224, 'EX01', '01', '00001000', '2019-07-28 18:42:11', NULL),
(225, 'EX01', '01', '00001000', '2019-07-28 18:44:30', NULL),
(226, 'EX01', '01', '00001000', '2019-07-28 18:48:36', NULL),
(227, 'EX01', '01', '00001000', '2019-07-28 18:49:36', NULL),
(228, 'EX01', '01', '00001000', '2019-07-28 18:53:09', NULL),
(229, 'EX01', '01', '00001000', '2019-07-28 19:23:04', NULL),
(230, 'EX01', '01', '00001000', '2019-07-30 01:10:51', NULL),
(231, 'EX01', '01', '00001000', '2019-07-30 01:36:05', NULL),
(232, 'EX01', '01', '00001000', '2019-07-30 01:40:30', NULL),
(233, 'EX01', '01', '00001000', '2019-07-30 01:42:13', NULL),
(234, 'EX01', '01', '00001000', '2019-07-30 01:52:05', NULL),
(235, 'EX01', '01', '00001000', '2019-07-30 02:03:44', NULL),
(236, 'EX01', '01', '00001000', '2019-07-30 02:04:48', NULL),
(237, 'EX01', '01', '00001000', '2019-07-30 02:07:00', NULL),
(238, 'EX01', '01', '00001000', '2019-07-30 02:08:06', NULL),
(239, 'EX01', '01', '00001000', '2019-07-30 02:11:29', NULL),
(240, 'EX01', '01', '00001000', '2019-07-30 02:12:25', NULL),
(241, 'EX01', '01', '00001000', '2019-07-30 02:17:41', NULL),
(242, 'EX01', '01', '00001000', '2019-07-30 02:27:40', NULL),
(243, 'EX01', '01', '00001000', '2019-07-30 02:29:50', NULL),
(244, 'EX01', '01', '00001000', '2019-07-30 02:42:29', NULL),
(245, 'EX01', 'E0', '00001000', '2019-07-30 02:43:40', 'EX01000000000024'),
(246, 'EX01', '01', '00001000', '2019-07-30 02:45:42', NULL),
(247, 'EX01', '01', '00001000', '2019-07-30 05:23:56', NULL),
(248, 'EX01', 'E0', '00001000', '2019-07-30 05:29:12', 'EX01000000000029'),
(249, 'EX01', 'E0', '00001000', '2019-07-30 05:29:12', 'EX01000000000028'),
(250, 'EX01', 'E0', '00001000', '2019-07-30 05:29:12', 'EX01000000000029'),
(251, 'EX01', 'E0', '00001000', '2019-07-30 05:29:14', 'EX01000000000029'),
(252, 'EX01', '01', '00001000', '2019-07-30 05:33:30', NULL),
(253, 'EX01', '01', '00001000', '2019-07-30 05:39:51', NULL),
(254, 'EX01', 'E0', '00001000', '2019-07-30 05:41:55', 'EX01000000000031'),
(255, 'EX01', '01', '00001000', '2019-07-30 05:46:57', NULL),
(256, 'EX01', 'E0', '00001000', '2019-07-30 05:47:47', 'EX01000000000032'),
(257, 'EX01', '01', '00001000', '2019-07-30 05:51:04', NULL),
(258, 'EX01', 'E0', '00001000', '2019-07-30 05:51:16', 'EX01000000000033');

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `pkID` int(11) NOT NULL AUTO_INCREMENT,
  `logNum` varchar(128) NOT NULL,
  PRIMARY KEY (`pkID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`pkID`, `logNum`) VALUES
(1, 'ÿÛ †cƒ¸$R');
--
-- Database: `netmanager`
--
DROP DATABASE IF EXISTS `netmanager`;
CREATE DATABASE IF NOT EXISTS `netmanager` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `netmanager`;

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tableName` varchar(20) NOT NULL,
  `lastTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`id`, `tableName`, `lastTime`) VALUES
(1, 'crdplt', '2018-10-19 18:28:43'),
(2, 'exit_trans', '2018-11-09 12:51:04'),
(3, 'colltrain', '2018-09-16 21:32:57'),
(4, 'zread', '2018-09-16 21:32:58');
--
-- Database: `parkertypes`
--
DROP DATABASE IF EXISTS `parkertypes`;
CREATE DATABASE IF NOT EXISTS `parkertypes` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `parkertypes`;

-- --------------------------------------------------------

--
-- Table structure for table `bfct_types`
--

DROP TABLE IF EXISTS `bfct_types`;
CREATE TABLE IF NOT EXISTS `bfct_types` (
  `ptypeID` int(11) NOT NULL AUTO_INCREMENT,
  `parkertype` varchar(2) NOT NULL,
  `ptypename` varchar(12) NOT NULL,
  `ptypedesc` varchar(24) NOT NULL,
  `btnimg` varchar(128) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '0',
  `numOfReceipts` tinyint(1) NOT NULL DEFAULT '0',
  `Discounted` tinyint(1) NOT NULL DEFAULT '0',
  `DiscountPercentage` double NOT NULL,
  PRIMARY KEY (`ptypeID`),
  UNIQUE KEY `parkertype_2` (`parkertype`),
  UNIQUE KEY `parkertype_4` (`parkertype`),
  UNIQUE KEY `parkertype_5` (`parkertype`),
  KEY `parkertype` (`parkertype`),
  KEY `parkertype_3` (`parkertype`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bfct_types`
--

INSERT INTO `bfct_types` (`ptypeID`, `parkertype`, `ptypename`, `ptypedesc`, `btnimg`, `ACTIVE`, `numOfReceipts`, `Discounted`, `DiscountPercentage`) VALUES
(1, 'R', 'Bus', 'P250 Loading', 'http://127.0.0.1/img/bus.png', 1, 1, 0, 0),
(2, 'V', 'VIP', 'Free Parking', 'http://127.0.0.1/img/vip.png', 1, 1, 0, 0),
(3, 'UB', 'UnloadingBus', 'P100 Unloading', 'http://127.0.0.1/img/delivery.png', 0, 1, 0, 0),
(4, 'G', 'GracePeriod', '', 'http://127.0.0.1/img/generic.png', 1, 1, 0, 0),
(5, 'L', 'Lost', 'P50 plus P200', 'http://127.0.0.1/img/lost.png', 1, 1, 0, 0),
(6, 'C', 'Cars', 'Free if Validated', 'http://127.0.0.1/img/retail.png', 1, 1, 0, 0),
(7, 'Q', 'Senior', 'Free for Senior', 'http://127.0.0.1/img/qcsenior.png', 0, 2, 1, 100),
(8, 'NQ', 'NonQCSenior', 'Discounted 20%', 'http://127.0.0.1/img/senior.png', 1, 2, 1, 20),
(9, 'PW', 'PWD', 'Discounted 20%', 'http://127.0.0.1/img/pwd.png', 1, 2, 1, 20),
(10, 'M', 'Motorcycle', 'Motorcycles', 'http://127.0.0.1/img/motorcycle.png', 1, 1, 0, 0),
(11, 'D', 'Delivery', 'Delivery Parking', 'http://127.0.0.1/img/delivery.png', 1, 1, 0, 0),
(12, 'T', 'Trucks', 'Trucks', 'http://127.0.0.1/img/trucks.png', 1, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `bir_main`
--

DROP TABLE IF EXISTS `bir_main`;
CREATE TABLE IF NOT EXISTS `bir_main` (
  `ptypeID` int(11) NOT NULL AUTO_INCREMENT,
  `parkertype` varchar(2) NOT NULL,
  `ptypename` varchar(12) NOT NULL,
  `ptypedesc` varchar(24) NOT NULL,
  `btnimg` varchar(128) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '0',
  `numOfReceipts` tinyint(1) NOT NULL DEFAULT '0',
  `Discounted` tinyint(1) NOT NULL DEFAULT '0',
  `DiscountPercentage` double DEFAULT NULL,
  PRIMARY KEY (`ptypeID`),
  UNIQUE KEY `parkertype_2` (`parkertype`),
  UNIQUE KEY `parkertype_4` (`parkertype`),
  UNIQUE KEY `parkertype_5` (`parkertype`),
  KEY `parkertype` (`parkertype`),
  KEY `parkertype_3` (`parkertype`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bir_main`
--

INSERT INTO `bir_main` (`ptypeID`, `parkertype`, `ptypename`, `ptypedesc`, `btnimg`, `ACTIVE`, `numOfReceipts`, `Discounted`, `DiscountPercentage`) VALUES
(1, 'R', 'Regular', 'P250 Loading', 'http://127.0.0.1/img/retail.png', 1, 1, 0, 0),
(2, 'V', 'VIP', 'Free Parking', 'http://127.0.0.1/img/vip.png', 1, 1, 0, 0),
(4, 'G', 'GracePeriod', '', 'http://127.0.0.1/img/generic.png', 1, 1, 0, 0),
(5, 'L', 'Lost', 'P50 plus P200', 'http://127.0.0.1/img/lost.png', 1, 1, 0, 0),
(6, 'P', 'Promo', 'Free if Validated', 'http://127.0.0.1/img/promo.png', 1, 1, 0, 0),
(7, 'Q', 'QCSenior', 'Free for Senior', 'http://127.0.0.1/img/qcsenior.png', 1, 2, 1, 100),
(8, 'NQ', 'NonQCSenior', 'Discounted 20%', 'http://127.0.0.1/img/senior.png', 1, 2, 1, 20),
(9, 'PW', 'PWD', 'Discounted 20%', 'http://127.0.0.1/img/pwd.png', 1, 2, 1, 20),
(10, 'M', 'Motorcycle', 'Motorcycles', 'http://127.0.0.1/img/motorcycle.png', 1, 1, 0, 0),
(23, 'J', 'Jeep', 'Free for 1 hour', 'http://127.0.0.1/img/jeepney.png', 1, 1, 0, 0),
(24, 'TC', 'Tricycle', 'Free', 'http://127.0.0.1/img/tricycle.png', 1, 1, 0, 0),
(25, 'D', 'Delivery', 'Free for 1 hour', 'http://127.0.0.1/img/deliveryred.png', 1, 1, 0, 0),
(27, 'BC', 'BPOEmployee', 'Free for 24hours', 'http://127.0.0.1/img/bpo_car.png', 1, 1, 0, 0),
(28, 'EM', 'Employees', 'One Car Only Free', 'http://127.0.0.1/img/employee.png', 1, 1, 0, 0),
(29, 'TN', 'Tenants', 'One Car Only Free', 'http://127.0.0.1/img/tenant.png', 1, 1, 0, 0),
(30, 'VD', 'Voids', 'Void Parkers', 'http://127.0.0.1/img/', 1, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `bir_types`
--

DROP TABLE IF EXISTS `bir_types`;
CREATE TABLE IF NOT EXISTS `bir_types` (
  `ptypeID` int(11) NOT NULL AUTO_INCREMENT,
  `parkertype` varchar(2) NOT NULL,
  `ptypename` varchar(12) NOT NULL,
  `ptypedesc` varchar(24) NOT NULL,
  `btnimg` varchar(128) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '0',
  `numOfReceipts` tinyint(1) NOT NULL DEFAULT '0',
  `Discounted` tinyint(1) NOT NULL DEFAULT '0',
  `DiscountPercentage` double NOT NULL,
  PRIMARY KEY (`ptypeID`),
  UNIQUE KEY `parkertype_2` (`parkertype`),
  UNIQUE KEY `parkertype_4` (`parkertype`),
  UNIQUE KEY `parkertype_5` (`parkertype`),
  KEY `parkertype` (`parkertype`),
  KEY `parkertype_3` (`parkertype`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bir_types`
--

INSERT INTO `bir_types` (`ptypeID`, `parkertype`, `ptypename`, `ptypedesc`, `btnimg`, `ACTIVE`, `numOfReceipts`, `Discounted`, `DiscountPercentage`) VALUES
(1, 'R', 'Regular', 'P250 Loading', 'http://127.0.0.1/img/bus.png', 1, 1, 0, 0),
(2, 'V', 'VIP', 'Free Parking', 'http://127.0.0.1/img/vip.png', 1, 1, 0, 0),
(3, 'UB', 'UnloadingBus', 'P100 Unloading', 'http://127.0.0.1/img/delivery.png', 0, 1, 0, 0),
(4, 'G', 'GracePeriod', '', 'http://127.0.0.1/img/generic.png', 1, 1, 0, 0),
(5, 'L', 'Lost', 'P50 plus P200', 'http://127.0.0.1/img/lost.png', 1, 1, 0, 0),
(6, 'P', 'Promo', 'Free if Validated', 'http://127.0.0.1/img/retail.png', 1, 1, 0, 0),
(7, 'Q', 'Senior', 'Free for Senior', 'http://127.0.0.1/img/qcsenior.png', 0, 2, 1, 100),
(8, 'NQ', 'NonQCSenior', 'Discounted 20%', 'http://127.0.0.1/img/senior.png', 1, 2, 1, 20),
(9, 'PW', 'PWD', 'Discounted 20%', 'http://127.0.0.1/img/pwd.png', 1, 2, 1, 20),
(10, 'M', 'Motorcycle', 'Motorcycles', 'http://127.0.0.1/img/motorcycle.png', 1, 1, 0, 0),
(11, 'D', 'Delivery', 'Delivery Parking', 'http://127.0.0.1/img/delivery.png', 1, 1, 0, 0),
(12, 'T', 'Trucks', 'Trucks', 'http://127.0.0.1/img/trucks.png', 1, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `cedar_types`
--

DROP TABLE IF EXISTS `cedar_types`;
CREATE TABLE IF NOT EXISTS `cedar_types` (
  `ptypeID` int(11) NOT NULL AUTO_INCREMENT,
  `parkertype` varchar(2) NOT NULL,
  `ptypename` varchar(12) NOT NULL,
  `ptypedesc` varchar(24) NOT NULL,
  `btnimg` varchar(128) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '0',
  `numOfReceipts` tinyint(4) NOT NULL,
  `Discounted` tinyint(4) NOT NULL,
  `DiscountPercentage` double NOT NULL,
  PRIMARY KEY (`ptypeID`),
  UNIQUE KEY `parkertype_2` (`parkertype`),
  UNIQUE KEY `parkertype_4` (`parkertype`),
  UNIQUE KEY `parkertype_5` (`parkertype`),
  KEY `parkertype` (`parkertype`),
  KEY `parkertype_3` (`parkertype`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cedar_types`
--

INSERT INTO `cedar_types` (`ptypeID`, `parkertype`, `ptypename`, `ptypedesc`, `btnimg`, `ACTIVE`, `numOfReceipts`, `Discounted`, `DiscountPercentage`) VALUES
(1, 'R', 'Retail', 'P35 plus P25 succeeding', 'http://127.0.0.1/img/retail.png', 1, 1, 0, 0),
(2, 'V', 'VIP', 'Free Parking', 'http://127.0.0.1/img/vip.png', 0, 1, 0, 0),
(3, 'M', 'Motorcycle', 'P35 plus P25 succeeding', 'http://127.0.0.1/img/motorcycle.png', 1, 1, 0, 0),
(4, 'G', 'GracePeriod', '', 'http://127.0.0.1/img/generic.png', 0, 1, 0, 0),
(5, 'L', 'Lost', 'P50 plus P200', 'http://127.0.0.1/img/lost.png', 1, 1, 0, 0),
(6, 'D', 'Delivery', '1st 2hrs free. P100/hr', 'http://127.0.0.1/img/delivery.png', 0, 1, 0, 0),
(7, 'Q', 'Senior', '20% DSC Senior or PWD', 'http://127.0.0.1/img/senior.png', 1, 1, 0, 0),
(8, 'NQ', 'NonQCSenior', 'P50 First 10 Hours', 'http://127.0.0.1/img/bpo_car.png', 0, 1, 0, 0),
(9, 'BM', 'BPOMotor', 'P35 First 10 Hours', 'http://127.0.0.1/img/bpo_motor.png', 0, 1, 0, 0),
(10, 'P', 'Promo', 'Discounted', 'http://127.0.0.1/img/promo.png', 1, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `ever_types`
--

DROP TABLE IF EXISTS `ever_types`;
CREATE TABLE IF NOT EXISTS `ever_types` (
  `ptypeID` int(11) NOT NULL AUTO_INCREMENT,
  `parkertype` varchar(2) NOT NULL,
  `ptypename` varchar(12) NOT NULL,
  `ptypedesc` varchar(24) NOT NULL,
  `btnimg` varchar(128) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ptypeID`),
  UNIQUE KEY `parkertype_2` (`parkertype`),
  UNIQUE KEY `parkertype_4` (`parkertype`),
  UNIQUE KEY `parkertype_5` (`parkertype`),
  KEY `parkertype` (`parkertype`),
  KEY `parkertype_3` (`parkertype`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ever_types`
--

INSERT INTO `ever_types` (`ptypeID`, `parkertype`, `ptypename`, `ptypedesc`, `btnimg`, `ACTIVE`) VALUES
(1, 'R', 'Retail', 'P50 First 10hrs', 'http://127.0.0.1/img/retail.png', 1),
(2, 'V', 'VIP', 'Free Parking', 'http://127.0.0.1/img/vip.png', 1),
(3, 'M', 'Motorcycle', 'P35 First 10 Hours', 'http://127.0.0.1/img/motorcycle.png', 1),
(4, 'G', 'GracePeriod', '', 'http://127.0.0.1/img/generic.png', 1),
(5, 'L', 'Lost', 'P50 plus P200', 'http://127.0.0.1/img/lost.png', 1),
(6, 'D', 'Delivery', 'Free if Validated', 'http://127.0.0.1/img/delivery.png', 1),
(7, 'Q', 'QCSenior', 'Free for QC Senior', 'http://127.0.0.1/img/qcsenior.png', 1),
(8, 'NQ', 'NonQCSenior', 'Discounted 20%', 'http://127.0.0.1/img/senior.png', 1),
(9, 'JP', 'Jeep', 'Free', 'http://127.0.0.1/img/jeepney.png', 1),
(10, 'TX', 'Taxi', 'Free', 'http://127.0.0.1/img/taxi.png', 1);

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `ptypeID` int(11) NOT NULL AUTO_INCREMENT,
  `parkertype` varchar(2) NOT NULL,
  `ptypename` varchar(12) NOT NULL,
  `ptypedesc` varchar(24) NOT NULL,
  `btnimg` varchar(128) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '0',
  `numOfReceipts` tinyint(1) NOT NULL DEFAULT '0',
  `Discounted` tinyint(1) NOT NULL DEFAULT '0',
  `DiscountPercentage` double DEFAULT NULL,
  PRIMARY KEY (`ptypeID`),
  UNIQUE KEY `parkertype_2` (`parkertype`),
  UNIQUE KEY `parkertype_4` (`parkertype`),
  UNIQUE KEY `parkertype_5` (`parkertype`),
  KEY `parkertype` (`parkertype`),
  KEY `parkertype_3` (`parkertype`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`ptypeID`, `parkertype`, `ptypename`, `ptypedesc`, `btnimg`, `ACTIVE`, `numOfReceipts`, `Discounted`, `DiscountPercentage`) VALUES
(1, 'R', 'Regular', 'P250 Loading', 'http://127.0.0.1/img/retail.png', 1, 1, 0, 0),
(2, 'V', 'VIP', 'Free Parking', 'http://127.0.0.1/img/vip.png', 1, 1, 0, 0),
(4, 'G', 'GracePeriod', '', 'http://127.0.0.1/img/generic.png', 1, 1, 0, 0),
(5, 'L', 'Lost', 'P50 plus P200', 'http://127.0.0.1/img/lost.png', 1, 1, 0, 0),
(6, 'P', 'Promo', 'Free if Validated', 'http://127.0.0.1/img/promo.png', 1, 1, 0, 0),
(7, 'Q', 'Senior', 'Free for Senior', 'http://127.0.0.1/img/qcsenior.png', 1, 2, 1, 100),
(8, 'NQ', 'NonQCSenior', 'Discounted 20%', 'http://127.0.0.1/img/senior.png', 1, 2, 1, 20),
(9, 'PW', 'PWD', 'Discounted 20%', 'http://127.0.0.1/img/pwd.png', 1, 2, 1, 20),
(10, 'M', 'Motorcycle', 'Motorcycles', 'http://127.0.0.1/img/motorcycle.png', 1, 1, 0, 0),
(23, 'J', 'Jeep', 'Free for 1 hour', 'http://127.0.0.1/img/jeepney.png', 1, 1, 0, 0),
(24, 'TC', 'Tricycle', 'Free', 'http://127.0.0.1/img/tricycle.png', 1, 1, 0, 0),
(25, 'D', 'Delivery', 'Free for 1 hour', 'http://127.0.0.1/img/deliveryred.png', 1, 1, 0, 0),
(27, 'BC', 'BPOEmployee', 'Free for 24hours', 'http://127.0.0.1/img/bpo_car.png', 1, 1, 0, 0),
(28, 'EM', 'Employees', 'One Car Only Free', 'http://127.0.0.1/img/employee.png', 1, 1, 0, 0),
(29, 'TN', 'Tenants', 'One Car Only Free', 'http://127.0.0.1/img/tenant.png', 1, 1, 0, 0),
(30, 'VD', 'Voids', 'Void Parkers', 'http://127.0.0.1/img/', 1, 1, 0, 0);
--
-- Database: `parkingqueue`
--
DROP DATABASE IF EXISTS `parkingqueue`;
CREATE DATABASE IF NOT EXISTS `parkingqueue` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `parkingqueue`;

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `entry` tinyint(1) NOT NULL,
  `seconds` int(11) NOT NULL,
  `max_seconds` int(11) NOT NULL,
  `type` varchar(10) NOT NULL,
  `override` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`id`, `entry`, `seconds`, `max_seconds`, `type`, `override`) VALUES
(1, 1, 8, 15, 'CARS', 0);
--
-- Database: `pos_users`
--
DROP DATABASE IF EXISTS `pos_users`;
CREATE DATABASE IF NOT EXISTS `pos_users` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `pos_users`;

-- --------------------------------------------------------

--
-- Table structure for table `bfct_users`
--

DROP TABLE IF EXISTS `bfct_users`;
CREATE TABLE IF NOT EXISTS `bfct_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `usercode` varchar(12) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bfct_users`
--

INSERT INTO `bfct_users` (`id`, `username`, `password`, `usercode`) VALUES
(1, 'Nora', '295eee4732a766ce0f9db0937465dd3a', '00001000'),
(2, 'zeny', 'dcb4ce1333b6820a59fae8d47b43f7e2', '00002000'),
(3, 'romeo', '788d66fb00f8f81848bebfe7f6a490a0', '00003000'),
(4, 'ariel', '38a12d3f96f61447c067e9f8ce79d700', '00004000'),
(5, 'mean', 'a5d1879ccf7270146bb882286e66d3f3', '00005000'),
(6, 'charry', '11f264f48c3755e5e6a8833ec9711b64', '00006000'),
(7, 'reliever', '2149a326a7ec788e685cc3bbfbf3d429', '00007000'),
(8, 'Theoretics', '9806564fbd7930ccd21fb182ce470287', '12345678'),
(9, 'teller1', '8f2ffd75dd4cd9e86ed995b7728a75e2', '00001000');

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `usercode` varchar(12) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`id`, `username`, `password`, `usercode`) VALUES
(1, 'teller1', '8f2ffd75dd4cd9e86ed995b7728a75e2', '00001000'),
(2, 'teller2', '2c5999f61fa9931a6136abb5551b9ecf', '00002000'),
(3, 'Corazon', '4a0b8af46ff64cb42e651f9f2bc6b277', '00003000'),
(4, 'Winky', 'e9a2111886e9130c3625bce3246772b1', '00004000'),
(5, 'Rica', '47aefffb7557c50baaaa455590fbd3d0', '00005000'),
(10, 'Lizel', '6713d8c13a39a0ecb0e74976b61286ac', '00006000'),
(11, 'teller7', '98dcb404ad3e13a9155b3f05a3b55de4', '00007000'),
(12, 'teller8', '72d189b230113f3db63f652db493b183', '00008000'),
(14, 'Ginelyn', '74e1e124a05889a2704b1a52646de950', '00010000'),
(15, 'Regina', '3f6e48307cab7917eed3ba4dbf72f870', '00009000');
--
-- Database: `ratesparam`
--
DROP DATABASE IF EXISTS `ratesparam`;
CREATE DATABASE IF NOT EXISTS `ratesparam` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ratesparam`;

-- --------------------------------------------------------

--
-- Table structure for table `bfctrates`
--

DROP TABLE IF EXISTS `bfctrates`;
CREATE TABLE IF NOT EXISTS `bfctrates` (
  `pkID` int(11) NOT NULL AUTO_INCREMENT,
  `trtype` varchar(2) NOT NULL,
  `name` varchar(20) NOT NULL,
  `GracePeriod` int(11) NOT NULL,
  `ExitGracePeriod` int(11) NOT NULL,
  `OTCutoff` int(11) NOT NULL,
  `OTCutoff1stWaived` tinyint(1) NOT NULL,
  `OTPrice` varchar(5) NOT NULL,
  `LostPrice` varchar(5) DEFAULT NULL,
  `TreatNextDayAsNewDay` tinyint(1) NOT NULL,
  `EveryNthHour` int(11) NOT NULL DEFAULT '0',
  `NthHourRate` varchar(5) NOT NULL DEFAULT '+0',
  `SucceedingRate` varchar(5) NOT NULL DEFAULT '+0',
  `FractionThereOf` tinyint(1) NOT NULL DEFAULT '1',
  `Hr0` varchar(5) NOT NULL,
  `Hr0Waived1st` tinyint(1) NOT NULL,
  `Hr0plus` varchar(5) NOT NULL,
  `Hr0plusWaived1st` tinyint(1) NOT NULL,
  `Hr1` varchar(5) NOT NULL,
  `Hr1Waived1st` tinyint(1) NOT NULL,
  `Hr1plus` varchar(5) NOT NULL,
  `Hr1plusWaived1st` tinyint(1) NOT NULL,
  `Hr2` varchar(5) NOT NULL,
  `Hr2Waived1st` tinyint(1) NOT NULL,
  `Hr2plus` varchar(5) NOT NULL,
  `Hr2plusWaived1st` tinyint(1) NOT NULL,
  `Hr3` varchar(5) NOT NULL,
  `Hr3Waived1st` tinyint(1) NOT NULL,
  `Hr3plus` varchar(5) NOT NULL,
  `Hr3plusWaived1st` tinyint(1) NOT NULL,
  `Hr4` varchar(5) NOT NULL,
  `Hr4Waived1st` tinyint(1) NOT NULL,
  `Hr4plus` varchar(5) NOT NULL,
  `Hr4plusWaived1st` tinyint(1) NOT NULL,
  `Hr5` varchar(5) NOT NULL,
  `Hr5Waived1st` tinyint(1) NOT NULL,
  `Hr5plus` varchar(5) NOT NULL,
  `Hr5plusWaived1st` tinyint(1) NOT NULL,
  `Hr6` varchar(5) NOT NULL,
  `Hr6Waived1st` tinyint(1) NOT NULL,
  `Hr6plus` varchar(5) NOT NULL,
  `Hr6plusWaived1st` tinyint(1) NOT NULL,
  `Hr7` varchar(5) NOT NULL,
  `Hr7Waived1st` tinyint(1) NOT NULL,
  `Hr7plus` varchar(5) NOT NULL,
  `Hr7plusWaived1st` tinyint(1) NOT NULL,
  `Hr8` varchar(5) NOT NULL,
  `Hr8Waived1st` tinyint(1) NOT NULL,
  `Hr8plus` varchar(5) NOT NULL,
  `Hr8plusWaived1st` tinyint(1) NOT NULL,
  `Hr9` varchar(5) NOT NULL,
  `Hr9Waived1st` tinyint(1) NOT NULL,
  `Hr9plus` varchar(5) NOT NULL,
  `Hr9plusWaived1st` tinyint(1) NOT NULL,
  `Hr10` varchar(5) NOT NULL,
  `Hr10Waived1st` tinyint(1) NOT NULL,
  `Hr10plus` varchar(5) NOT NULL,
  `Hr10plusWaived1st` tinyint(1) NOT NULL,
  `Hr11` varchar(5) NOT NULL,
  `Hr11Waived1st` tinyint(1) NOT NULL,
  `Hr11plus` varchar(5) NOT NULL,
  `Hr11plusWaived1st` tinyint(1) NOT NULL,
  `Hr12` varchar(5) NOT NULL,
  `Hr12Waived1st` tinyint(1) NOT NULL,
  `Hr12plus` varchar(5) NOT NULL,
  `Hr12plusWaived1st` tinyint(1) NOT NULL,
  `Hr13` varchar(5) NOT NULL,
  `Hr13Waived1st` tinyint(1) NOT NULL,
  `Hr13plus` varchar(5) NOT NULL,
  `Hr13plusWaived1st` tinyint(1) NOT NULL,
  `Hr14` varchar(5) NOT NULL,
  `Hr14Waived1st` tinyint(1) NOT NULL,
  `Hr14plus` varchar(5) NOT NULL,
  `Hr14plusWaived1st` tinyint(1) NOT NULL,
  `Hr15` varchar(5) NOT NULL,
  `Hr15Waived1st` tinyint(1) NOT NULL,
  `Hr15plus` varchar(5) NOT NULL,
  `Hr15plusWaived1st` tinyint(1) NOT NULL,
  `Hr16` varchar(5) NOT NULL,
  `Hr16Waived1st` tinyint(1) NOT NULL,
  `Hr16plus` varchar(5) NOT NULL,
  `Hr16plusWaived1st` tinyint(1) NOT NULL,
  `Hr17` varchar(5) NOT NULL,
  `Hr17Waived1st` tinyint(1) NOT NULL,
  `Hr17plus` varchar(5) NOT NULL,
  `Hr17plusWaived1st` tinyint(1) NOT NULL,
  `Hr18` varchar(5) NOT NULL,
  `Hr18Waived1st` tinyint(1) NOT NULL,
  `Hr18plus` varchar(5) NOT NULL,
  `Hr18plusWaived1st` tinyint(1) NOT NULL,
  `Hr19` varchar(5) NOT NULL,
  `Hr19Waived1st` tinyint(1) NOT NULL,
  `Hr19plus` varchar(5) NOT NULL,
  `Hr19plusWaived1st` tinyint(1) NOT NULL,
  `Hr20` varchar(5) NOT NULL,
  `Hr20Waived1st` tinyint(1) NOT NULL,
  `Hr20plus` varchar(5) NOT NULL,
  `Hr20plusWaived1st` tinyint(1) NOT NULL,
  `Hr21` varchar(5) NOT NULL,
  `Hr21Waived1st` tinyint(1) NOT NULL,
  `Hr21plus` varchar(5) NOT NULL,
  `Hr21plusWaived1st` tinyint(1) NOT NULL,
  `Hr22` varchar(5) NOT NULL,
  `Hr22Waived1st` tinyint(1) NOT NULL,
  `Hr22plus` varchar(5) NOT NULL,
  `Hr22plusWaived1st` tinyint(1) NOT NULL,
  `Hr23` varchar(5) NOT NULL,
  `Hr23Waived1st` tinyint(1) NOT NULL,
  `Hr23plus` varchar(5) NOT NULL,
  `Hr23plusWaived1st` tinyint(1) NOT NULL,
  `Hr24` varchar(5) NOT NULL,
  `Hr24Waived1st` tinyint(1) NOT NULL,
  PRIMARY KEY (`pkID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bfctrates`
--

INSERT INTO `bfctrates` (`pkID`, `trtype`, `name`, `GracePeriod`, `ExitGracePeriod`, `OTCutoff`, `OTCutoff1stWaived`, `OTPrice`, `LostPrice`, `TreatNextDayAsNewDay`, `EveryNthHour`, `NthHourRate`, `SucceedingRate`, `FractionThereOf`, `Hr0`, `Hr0Waived1st`, `Hr0plus`, `Hr0plusWaived1st`, `Hr1`, `Hr1Waived1st`, `Hr1plus`, `Hr1plusWaived1st`, `Hr2`, `Hr2Waived1st`, `Hr2plus`, `Hr2plusWaived1st`, `Hr3`, `Hr3Waived1st`, `Hr3plus`, `Hr3plusWaived1st`, `Hr4`, `Hr4Waived1st`, `Hr4plus`, `Hr4plusWaived1st`, `Hr5`, `Hr5Waived1st`, `Hr5plus`, `Hr5plusWaived1st`, `Hr6`, `Hr6Waived1st`, `Hr6plus`, `Hr6plusWaived1st`, `Hr7`, `Hr7Waived1st`, `Hr7plus`, `Hr7plusWaived1st`, `Hr8`, `Hr8Waived1st`, `Hr8plus`, `Hr8plusWaived1st`, `Hr9`, `Hr9Waived1st`, `Hr9plus`, `Hr9plusWaived1st`, `Hr10`, `Hr10Waived1st`, `Hr10plus`, `Hr10plusWaived1st`, `Hr11`, `Hr11Waived1st`, `Hr11plus`, `Hr11plusWaived1st`, `Hr12`, `Hr12Waived1st`, `Hr12plus`, `Hr12plusWaived1st`, `Hr13`, `Hr13Waived1st`, `Hr13plus`, `Hr13plusWaived1st`, `Hr14`, `Hr14Waived1st`, `Hr14plus`, `Hr14plusWaived1st`, `Hr15`, `Hr15Waived1st`, `Hr15plus`, `Hr15plusWaived1st`, `Hr16`, `Hr16Waived1st`, `Hr16plus`, `Hr16plusWaived1st`, `Hr17`, `Hr17Waived1st`, `Hr17plus`, `Hr17plusWaived1st`, `Hr18`, `Hr18Waived1st`, `Hr18plus`, `Hr18plusWaived1st`, `Hr19`, `Hr19Waived1st`, `Hr19plus`, `Hr19plusWaived1st`, `Hr20`, `Hr20Waived1st`, `Hr20plus`, `Hr20plusWaived1st`, `Hr21`, `Hr21Waived1st`, `Hr21plus`, `Hr21plusWaived1st`, `Hr22`, `Hr22Waived1st`, `Hr22plus`, `Hr22plusWaived1st`, `Hr23`, `Hr23Waived1st`, `Hr23plus`, `Hr23plusWaived1st`, `Hr24`, `Hr24Waived1st`) VALUES
(1, 'R', 'Bus', 0, 20, 0, 0, '0', '500', 2, 10, '200', '10', 1, '250', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '250', 0),
(2, 'UB', 'UnloadingBus', 0, 20, 0, 0, '0', '500', 1, 5, '200', '10', 1, '100', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '100', 0),
(3, 'BC', 'BPOCar', 0, 20, 0, 0, '0', '500', 0, 10, '200', '10', 1, '50', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '50', 0),
(4, 'NQ', 'NonQCSenior', 0, 20, 0, 0, '0', '500', 2, 10, '200', '10', 1, '24', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '24', 0),
(5, 'Q', 'Senior', 0, 20, 0, 0, '0', '500', 1, 10, '200', '10', 1, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '10', 0),
(6, 'C', 'Cars', 0, 20, 0, 0, '0', '500', 2, 0, '0', '10', 1, '30', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '30', 0),
(8, 'PW', 'PWD', 0, 20, 0, 0, '0', '500', 2, 10, '200', '10', 1, '24', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '24', 0),
(9, 'D', 'Delivery', 0, 20, 0, 0, '0', '500', 1, 10, '200', '10', 1, '30', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '30', 0),
(10, 'T', 'Trucks', 0, 20, 0, 0, '0', '500', 1, 10, '200', '10', 1, '500', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '500', 0),
(11, 'M', 'Motorcycle', 0, 20, 0, 0, '0', '500', 2, 0, '0', '10', 1, '30', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '0', 0, '30', 0);

-- --------------------------------------------------------

--
-- Table structure for table `birrates`
--

DROP TABLE IF EXISTS `birrates`;
CREATE TABLE IF NOT EXISTS `birrates` (
  `pkID` int(11) NOT NULL AUTO_INCREMENT,
  `trtype` varchar(2) NOT NULL,
  `name` varchar(20) NOT NULL,
  `GracePeriod` int(11) NOT NULL,
  `OTCutoff` int(11) NOT NULL,
  `OTCutoff1stWaived` tinyint(1) NOT NULL,
  `OTPrice` varchar(5) NOT NULL,
  `LostPrice` varchar(5) DEFAULT NULL,
  `TreatNextDayAsNewDay` tinyint(1) NOT NULL,
  `EveryNthHour` int(11) NOT NULL DEFAULT '0',
  `NthHourRate` varchar(5) NOT NULL DEFAULT '+0',
  `SucceedingRate` varchar(5) NOT NULL DEFAULT '+0',
  `FractionThereOf` tinyint(1) NOT NULL DEFAULT '1',
  `Hr0` varchar(5) NOT NULL,
  `Hr0Waived1st` tinyint(1) NOT NULL,
  `Hr0plus` varchar(5) NOT NULL,
  `Hr0plusWaived1st` tinyint(1) NOT NULL,
  `Hr1` varchar(5) NOT NULL,
  `Hr1Waived1st` tinyint(1) NOT NULL,
  `Hr1plus` varchar(5) NOT NULL,
  `Hr1plusWaived1st` tinyint(1) NOT NULL,
  `Hr2` varchar(5) NOT NULL,
  `Hr2Waived1st` tinyint(1) NOT NULL,
  `Hr2plus` varchar(5) NOT NULL,
  `Hr2plusWaived1st` tinyint(1) NOT NULL,
  `Hr3` varchar(5) NOT NULL,
  `Hr3Waived1st` tinyint(1) NOT NULL,
  `Hr3plus` varchar(5) NOT NULL,
  `Hr3plusWaived1st` tinyint(1) NOT NULL,
  `Hr4` varchar(5) NOT NULL,
  `Hr4Waived1st` tinyint(1) NOT NULL,
  `Hr4plus` varchar(5) NOT NULL,
  `Hr4plusWaived1st` tinyint(1) NOT NULL,
  `Hr5` varchar(5) NOT NULL,
  `Hr5Waived1st` tinyint(1) NOT NULL,
  `Hr5plus` varchar(5) NOT NULL,
  `Hr5plusWaived1st` tinyint(1) NOT NULL,
  `Hr6` varchar(5) NOT NULL,
  `Hr6Waived1st` tinyint(1) NOT NULL,
  `Hr6plus` varchar(5) NOT NULL,
  `Hr6plusWaived1st` tinyint(1) NOT NULL,
  `Hr7` varchar(5) NOT NULL,
  `Hr7Waived1st` tinyint(1) NOT NULL,
  `Hr7plus` varchar(5) NOT NULL,
  `Hr7plusWaived1st` tinyint(1) NOT NULL,
  `Hr8` varchar(5) NOT NULL,
  `Hr8Waived1st` tinyint(1) NOT NULL,
  `Hr8plus` varchar(5) NOT NULL,
  `Hr8plusWaived1st` tinyint(1) NOT NULL,
  `Hr9` varchar(5) NOT NULL,
  `Hr9Waived1st` tinyint(1) NOT NULL,
  `Hr9plus` varchar(5) NOT NULL,
  `Hr9plusWaived1st` tinyint(1) NOT NULL,
  `Hr10` varchar(5) NOT NULL,
  `Hr10Waived1st` tinyint(1) NOT NULL,
  `Hr10plus` varchar(5) NOT NULL,
  `Hr10plusWaived1st` tinyint(1) NOT NULL,
  `Hr11` varchar(5) NOT NULL,
  `Hr11Waived1st` tinyint(1) NOT NULL,
  `Hr11plus` varchar(5) NOT NULL,
  `Hr11plusWaived1st` tinyint(1) NOT NULL,
  `Hr12` varchar(5) NOT NULL,
  `Hr12Waived1st` tinyint(1) NOT NULL,
  `Hr12plus` varchar(5) NOT NULL,
  `Hr12plusWaived1st` tinyint(1) NOT NULL,
  `Hr13` varchar(5) NOT NULL,
  `Hr13Waived1st` tinyint(1) NOT NULL,
  `Hr13plus` varchar(5) NOT NULL,
  `Hr13plusWaived1st` tinyint(1) NOT NULL,
  `Hr14` varchar(5) NOT NULL,
  `Hr14Waived1st` tinyint(1) NOT NULL,
  `Hr14plus` varchar(5) NOT NULL,
  `Hr14plusWaived1st` tinyint(1) NOT NULL,
  `Hr15` varchar(5) NOT NULL,
  `Hr15Waived1st` tinyint(1) NOT NULL,
  `Hr15plus` varchar(5) NOT NULL,
  `Hr15plusWaived1st` tinyint(1) NOT NULL,
  `Hr16` varchar(5) NOT NULL,
  `Hr16Waived1st` tinyint(1) NOT NULL,
  `Hr16plus` varchar(5) NOT NULL,
  `Hr16plusWaived1st` tinyint(1) NOT NULL,
  `Hr17` varchar(5) NOT NULL,
  `Hr17Waived1st` tinyint(1) NOT NULL,
  `Hr17plus` varchar(5) NOT NULL,
  `Hr17plusWaived1st` tinyint(1) NOT NULL,
  `Hr18` varchar(5) NOT NULL,
  `Hr18Waived1st` tinyint(1) NOT NULL,
  `Hr18plus` varchar(5) NOT NULL,
  `Hr18plusWaived1st` tinyint(1) NOT NULL,
  `Hr19` varchar(5) NOT NULL,
  `Hr19Waived1st` tinyint(1) NOT NULL,
  `Hr19plus` varchar(5) NOT NULL,
  `Hr19plusWaived1st` tinyint(1) NOT NULL,
  `Hr20` varchar(5) NOT NULL,
  `Hr20Waived1st` tinyint(1) NOT NULL,
  `Hr20plus` varchar(5) NOT NULL,
  `Hr20plusWaived1st` tinyint(1) NOT NULL,
  `Hr21` varchar(5) NOT NULL,
  `Hr21Waived1st` tinyint(1) NOT NULL,
  `Hr21plus` varchar(5) NOT NULL,
  `Hr21plusWaived1st` tinyint(1) NOT NULL,
  `Hr22` varchar(5) NOT NULL,
  `Hr22Waived1st` tinyint(1) NOT NULL,
  `Hr22plus` varchar(5) NOT NULL,
  `Hr22plusWaived1st` tinyint(1) NOT NULL,
  `Hr23` varchar(5) NOT NULL,
  `Hr23Waived1st` tinyint(1) NOT NULL,
  `Hr23plus` varchar(5) NOT NULL,
  `Hr23plusWaived1st` tinyint(1) NOT NULL,
  `Hr24` varchar(5) NOT NULL,
  `Hr24Waived1st` tinyint(1) NOT NULL,
  PRIMARY KEY (`pkID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `birrates`
--

INSERT INTO `birrates` (`pkID`, `trtype`, `name`, `GracePeriod`, `OTCutoff`, `OTCutoff1stWaived`, `OTPrice`, `LostPrice`, `TreatNextDayAsNewDay`, `EveryNthHour`, `NthHourRate`, `SucceedingRate`, `FractionThereOf`, `Hr0`, `Hr0Waived1st`, `Hr0plus`, `Hr0plusWaived1st`, `Hr1`, `Hr1Waived1st`, `Hr1plus`, `Hr1plusWaived1st`, `Hr2`, `Hr2Waived1st`, `Hr2plus`, `Hr2plusWaived1st`, `Hr3`, `Hr3Waived1st`, `Hr3plus`, `Hr3plusWaived1st`, `Hr4`, `Hr4Waived1st`, `Hr4plus`, `Hr4plusWaived1st`, `Hr5`, `Hr5Waived1st`, `Hr5plus`, `Hr5plusWaived1st`, `Hr6`, `Hr6Waived1st`, `Hr6plus`, `Hr6plusWaived1st`, `Hr7`, `Hr7Waived1st`, `Hr7plus`, `Hr7plusWaived1st`, `Hr8`, `Hr8Waived1st`, `Hr8plus`, `Hr8plusWaived1st`, `Hr9`, `Hr9Waived1st`, `Hr9plus`, `Hr9plusWaived1st`, `Hr10`, `Hr10Waived1st`, `Hr10plus`, `Hr10plusWaived1st`, `Hr11`, `Hr11Waived1st`, `Hr11plus`, `Hr11plusWaived1st`, `Hr12`, `Hr12Waived1st`, `Hr12plus`, `Hr12plusWaived1st`, `Hr13`, `Hr13Waived1st`, `Hr13plus`, `Hr13plusWaived1st`, `Hr14`, `Hr14Waived1st`, `Hr14plus`, `Hr14plusWaived1st`, `Hr15`, `Hr15Waived1st`, `Hr15plus`, `Hr15plusWaived1st`, `Hr16`, `Hr16Waived1st`, `Hr16plus`, `Hr16plusWaived1st`, `Hr17`, `Hr17Waived1st`, `Hr17plus`, `Hr17plusWaived1st`, `Hr18`, `Hr18Waived1st`, `Hr18plus`, `Hr18plusWaived1st`, `Hr19`, `Hr19Waived1st`, `Hr19plus`, `Hr19plusWaived1st`, `Hr20`, `Hr20Waived1st`, `Hr20plus`, `Hr20plusWaived1st`, `Hr21`, `Hr21Waived1st`, `Hr21plus`, `Hr21plusWaived1st`, `Hr22`, `Hr22Waived1st`, `Hr22plus`, `Hr22plusWaived1st`, `Hr23`, `Hr23Waived1st`, `Hr23plus`, `Hr23plusWaived1st`, `Hr24`, `Hr24Waived1st`) VALUES
(1, 'R', 'Retail', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+30', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(2, 'M', 'Motorcycle', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(4, 'NQ', 'NonQCSenior', 30, 2, 0, '+0', '+300', 0, 10, '+200', '+10', 1, '+21', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(5, 'Q', 'QCSenior', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(6, 'D', 'Delivery', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(8, 'P', 'Promo', 30, 2, 0, '+0', '+300', 1, 10, '+200', '+10', 1, '+38', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+200', 0),
(9, 'PW', 'PWD', 30, 2, 0, '+0', '+300', 0, 10, '+200', '+10', 1, '+21', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0);

-- --------------------------------------------------------

--
-- Table structure for table `cedarrates`
--

DROP TABLE IF EXISTS `cedarrates`;
CREATE TABLE IF NOT EXISTS `cedarrates` (
  `pkID` int(11) NOT NULL AUTO_INCREMENT,
  `trtype` varchar(2) NOT NULL,
  `name` varchar(20) NOT NULL,
  `GracePeriod` int(11) NOT NULL,
  `ExitGracePeriod` int(11) NOT NULL,
  `OTCutoff` int(11) NOT NULL,
  `OTCutoff1stWaived` tinyint(1) NOT NULL,
  `OTPrice` varchar(5) NOT NULL,
  `LostPrice` varchar(5) DEFAULT NULL,
  `TreatNextDayAsNewDay` tinyint(1) NOT NULL,
  `EveryNthHour` int(11) NOT NULL DEFAULT '0',
  `NthHourRate` varchar(5) NOT NULL DEFAULT '+0',
  `SucceedingRate` varchar(5) NOT NULL DEFAULT '+0',
  `FractionThereOf` tinyint(1) NOT NULL DEFAULT '1',
  `Hr0` varchar(7) NOT NULL,
  `Hr0Waived1st` tinyint(1) NOT NULL,
  `Hr0plus` varchar(7) NOT NULL,
  `Hr0plusWaived1st` tinyint(1) NOT NULL,
  `Hr1` varchar(7) NOT NULL,
  `Hr1Waived1st` tinyint(1) NOT NULL,
  `Hr1plus` varchar(7) NOT NULL,
  `Hr1plusWaived1st` tinyint(1) NOT NULL,
  `Hr2` varchar(7) NOT NULL,
  `Hr2Waived1st` tinyint(1) NOT NULL,
  `Hr2plus` varchar(7) NOT NULL,
  `Hr2plusWaived1st` tinyint(1) NOT NULL,
  `Hr3` varchar(7) NOT NULL,
  `Hr3Waived1st` tinyint(1) NOT NULL,
  `Hr3plus` varchar(7) NOT NULL,
  `Hr3plusWaived1st` tinyint(1) NOT NULL,
  `Hr4` varchar(7) NOT NULL,
  `Hr4Waived1st` tinyint(1) NOT NULL,
  `Hr4plus` varchar(7) NOT NULL,
  `Hr4plusWaived1st` tinyint(1) NOT NULL,
  `Hr5` varchar(7) NOT NULL,
  `Hr5Waived1st` tinyint(1) NOT NULL,
  `Hr5plus` varchar(7) NOT NULL,
  `Hr5plusWaived1st` tinyint(1) NOT NULL,
  `Hr6` varchar(7) NOT NULL,
  `Hr6Waived1st` tinyint(1) NOT NULL,
  `Hr6plus` varchar(7) NOT NULL,
  `Hr6plusWaived1st` tinyint(1) NOT NULL,
  `Hr7` varchar(7) NOT NULL,
  `Hr7Waived1st` tinyint(1) NOT NULL,
  `Hr7plus` varchar(7) NOT NULL,
  `Hr7plusWaived1st` tinyint(1) NOT NULL,
  `Hr8` varchar(7) NOT NULL,
  `Hr8Waived1st` tinyint(1) NOT NULL,
  `Hr8plus` varchar(7) NOT NULL,
  `Hr8plusWaived1st` tinyint(1) NOT NULL,
  `Hr9` varchar(7) NOT NULL,
  `Hr9Waived1st` tinyint(1) NOT NULL,
  `Hr9plus` varchar(7) NOT NULL,
  `Hr9plusWaived1st` tinyint(1) NOT NULL,
  `Hr10` varchar(7) NOT NULL,
  `Hr10Waived1st` tinyint(1) NOT NULL,
  `Hr10plus` varchar(7) NOT NULL,
  `Hr10plusWaived1st` tinyint(1) NOT NULL,
  `Hr11` varchar(7) NOT NULL,
  `Hr11Waived1st` tinyint(1) NOT NULL,
  `Hr11plus` varchar(7) NOT NULL,
  `Hr11plusWaived1st` tinyint(1) NOT NULL,
  `Hr12` varchar(7) NOT NULL,
  `Hr12Waived1st` tinyint(1) NOT NULL,
  `Hr12plus` varchar(7) NOT NULL,
  `Hr12plusWaived1st` tinyint(1) NOT NULL,
  `Hr13` varchar(7) NOT NULL,
  `Hr13Waived1st` tinyint(1) NOT NULL,
  `Hr13plus` varchar(7) NOT NULL,
  `Hr13plusWaived1st` tinyint(1) NOT NULL,
  `Hr14` varchar(7) NOT NULL,
  `Hr14Waived1st` tinyint(1) NOT NULL,
  `Hr14plus` varchar(7) NOT NULL,
  `Hr14plusWaived1st` tinyint(1) NOT NULL,
  `Hr15` varchar(7) NOT NULL,
  `Hr15Waived1st` tinyint(1) NOT NULL,
  `Hr15plus` varchar(7) NOT NULL,
  `Hr15plusWaived1st` tinyint(1) NOT NULL,
  `Hr16` varchar(7) NOT NULL,
  `Hr16Waived1st` tinyint(1) NOT NULL,
  `Hr16plus` varchar(7) NOT NULL,
  `Hr16plusWaived1st` tinyint(1) NOT NULL,
  `Hr17` varchar(7) NOT NULL,
  `Hr17Waived1st` tinyint(1) NOT NULL,
  `Hr17plus` varchar(7) NOT NULL,
  `Hr17plusWaived1st` tinyint(1) NOT NULL,
  `Hr18` varchar(7) NOT NULL,
  `Hr18Waived1st` tinyint(1) NOT NULL,
  `Hr18plus` varchar(7) NOT NULL,
  `Hr18plusWaived1st` tinyint(1) NOT NULL,
  `Hr19` varchar(7) NOT NULL,
  `Hr19Waived1st` tinyint(1) NOT NULL,
  `Hr19plus` varchar(7) NOT NULL,
  `Hr19plusWaived1st` tinyint(1) NOT NULL,
  `Hr20` varchar(7) NOT NULL,
  `Hr20Waived1st` tinyint(1) NOT NULL,
  `Hr20plus` varchar(7) NOT NULL,
  `Hr20plusWaived1st` tinyint(1) NOT NULL,
  `Hr21` varchar(7) NOT NULL,
  `Hr21Waived1st` tinyint(1) NOT NULL,
  `Hr21plus` varchar(7) NOT NULL,
  `Hr21plusWaived1st` tinyint(1) NOT NULL,
  `Hr22` varchar(7) NOT NULL,
  `Hr22Waived1st` tinyint(1) NOT NULL,
  `Hr22plus` varchar(7) NOT NULL,
  `Hr22plusWaived1st` tinyint(1) NOT NULL,
  `Hr23` varchar(7) NOT NULL,
  `Hr23Waived1st` tinyint(1) NOT NULL,
  `Hr23plus` varchar(7) NOT NULL,
  `Hr23plusWaived1st` tinyint(1) NOT NULL,
  `Hr24` varchar(7) NOT NULL,
  `Hr24Waived1st` tinyint(1) NOT NULL,
  PRIMARY KEY (`pkID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cedarrates`
--

INSERT INTO `cedarrates` (`pkID`, `trtype`, `name`, `GracePeriod`, `ExitGracePeriod`, `OTCutoff`, `OTCutoff1stWaived`, `OTPrice`, `LostPrice`, `TreatNextDayAsNewDay`, `EveryNthHour`, `NthHourRate`, `SucceedingRate`, `FractionThereOf`, `Hr0`, `Hr0Waived1st`, `Hr0plus`, `Hr0plusWaived1st`, `Hr1`, `Hr1Waived1st`, `Hr1plus`, `Hr1plusWaived1st`, `Hr2`, `Hr2Waived1st`, `Hr2plus`, `Hr2plusWaived1st`, `Hr3`, `Hr3Waived1st`, `Hr3plus`, `Hr3plusWaived1st`, `Hr4`, `Hr4Waived1st`, `Hr4plus`, `Hr4plusWaived1st`, `Hr5`, `Hr5Waived1st`, `Hr5plus`, `Hr5plusWaived1st`, `Hr6`, `Hr6Waived1st`, `Hr6plus`, `Hr6plusWaived1st`, `Hr7`, `Hr7Waived1st`, `Hr7plus`, `Hr7plusWaived1st`, `Hr8`, `Hr8Waived1st`, `Hr8plus`, `Hr8plusWaived1st`, `Hr9`, `Hr9Waived1st`, `Hr9plus`, `Hr9plusWaived1st`, `Hr10`, `Hr10Waived1st`, `Hr10plus`, `Hr10plusWaived1st`, `Hr11`, `Hr11Waived1st`, `Hr11plus`, `Hr11plusWaived1st`, `Hr12`, `Hr12Waived1st`, `Hr12plus`, `Hr12plusWaived1st`, `Hr13`, `Hr13Waived1st`, `Hr13plus`, `Hr13plusWaived1st`, `Hr14`, `Hr14Waived1st`, `Hr14plus`, `Hr14plusWaived1st`, `Hr15`, `Hr15Waived1st`, `Hr15plus`, `Hr15plusWaived1st`, `Hr16`, `Hr16Waived1st`, `Hr16plus`, `Hr16plusWaived1st`, `Hr17`, `Hr17Waived1st`, `Hr17plus`, `Hr17plusWaived1st`, `Hr18`, `Hr18Waived1st`, `Hr18plus`, `Hr18plusWaived1st`, `Hr19`, `Hr19Waived1st`, `Hr19plus`, `Hr19plusWaived1st`, `Hr20`, `Hr20Waived1st`, `Hr20plus`, `Hr20plusWaived1st`, `Hr21`, `Hr21Waived1st`, `Hr21plus`, `Hr21plusWaived1st`, `Hr22`, `Hr22Waived1st`, `Hr22plus`, `Hr22plusWaived1st`, `Hr23`, `Hr23Waived1st`, `Hr23plus`, `Hr23plusWaived1st`, `Hr24`, `Hr24Waived1st`) VALUES
(1, 'R', 'Retail', 0, 10, 0, 0, '+300', '+200', 1, 10, '+200', '+25', 1, '+35', 0, '+0', 0, '+0', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0),
(2, 'M', 'Motorcycle', 0, 10, 0, 0, '+300', '+200', 1, 5, '+200', '+25', 1, '+35', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+25', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0, '+0', 0, '+25', 0),
(3, 'BC', 'BPOCar', 15, 0, 2, 0, '+200', '+200', 0, 10, '+200', '+10', 1, '+50', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+200', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+200', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(4, 'BM', 'BPOMotor', 15, 0, 2, 0, '+200', '+200', 0, 10, '+200', '+10', 1, '+29', 0, '+0', 0, '+20', 0, '+0', 0, '+20', 0, '+0', 0, '+20', 0, '+0', 0, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+200', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+200', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(5, 'Q', 'Senior', 0, 10, 0, 0, '+300', '+200', 1, 10, '+200', '+20.5', 1, '+28.75', 0, '+0', 0, '+0', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+525', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0, '+0', 0, '+20.534', 0),
(6, 'D', 'Delivery', 15, 0, 2, 0, '+200', '+200', 1, 0, '+0', '+10', 1, '+0', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+100', 0, '+0', 0, '+200', 0),
(8, 'P', 'Promo', 0, 10, 0, 0, '+300', '+200', 1, 10, '+200', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+10', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0);

-- --------------------------------------------------------

--
-- Table structure for table `everrates`
--

DROP TABLE IF EXISTS `everrates`;
CREATE TABLE IF NOT EXISTS `everrates` (
  `pkID` int(11) NOT NULL AUTO_INCREMENT,
  `trtype` varchar(2) NOT NULL,
  `name` varchar(20) NOT NULL,
  `GracePeriod` int(11) NOT NULL,
  `OTCutoff` int(11) NOT NULL,
  `OTCutoff1stWaived` tinyint(1) NOT NULL,
  `OTPrice` varchar(5) NOT NULL,
  `LostPrice` varchar(5) DEFAULT NULL,
  `TreatNextDayAsNewDay` tinyint(1) NOT NULL,
  `EveryNthHour` int(11) NOT NULL DEFAULT '0',
  `NthHourRate` varchar(5) NOT NULL DEFAULT '+0',
  `SucceedingRate` varchar(5) NOT NULL DEFAULT '+0',
  `FractionThereOf` tinyint(1) NOT NULL DEFAULT '1',
  `Hr0` varchar(5) NOT NULL,
  `Hr0Waived1st` tinyint(1) NOT NULL,
  `Hr0plus` varchar(5) NOT NULL,
  `Hr0plusWaived1st` tinyint(1) NOT NULL,
  `Hr1` varchar(5) NOT NULL,
  `Hr1Waived1st` tinyint(1) NOT NULL,
  `Hr1plus` varchar(5) NOT NULL,
  `Hr1plusWaived1st` tinyint(1) NOT NULL,
  `Hr2` varchar(5) NOT NULL,
  `Hr2Waived1st` tinyint(1) NOT NULL,
  `Hr2plus` varchar(5) NOT NULL,
  `Hr2plusWaived1st` tinyint(1) NOT NULL,
  `Hr3` varchar(5) NOT NULL,
  `Hr3Waived1st` tinyint(1) NOT NULL,
  `Hr3plus` varchar(5) NOT NULL,
  `Hr3plusWaived1st` tinyint(1) NOT NULL,
  `Hr4` varchar(5) NOT NULL,
  `Hr4Waived1st` tinyint(1) NOT NULL,
  `Hr4plus` varchar(5) NOT NULL,
  `Hr4plusWaived1st` tinyint(1) NOT NULL,
  `Hr5` varchar(5) NOT NULL,
  `Hr5Waived1st` tinyint(1) NOT NULL,
  `Hr5plus` varchar(5) NOT NULL,
  `Hr5plusWaived1st` tinyint(1) NOT NULL,
  `Hr6` varchar(5) NOT NULL,
  `Hr6Waived1st` tinyint(1) NOT NULL,
  `Hr6plus` varchar(5) NOT NULL,
  `Hr6plusWaived1st` tinyint(1) NOT NULL,
  `Hr7` varchar(5) NOT NULL,
  `Hr7Waived1st` tinyint(1) NOT NULL,
  `Hr7plus` varchar(5) NOT NULL,
  `Hr7plusWaived1st` tinyint(1) NOT NULL,
  `Hr8` varchar(5) NOT NULL,
  `Hr8Waived1st` tinyint(1) NOT NULL,
  `Hr8plus` varchar(5) NOT NULL,
  `Hr8plusWaived1st` tinyint(1) NOT NULL,
  `Hr9` varchar(5) NOT NULL,
  `Hr9Waived1st` tinyint(1) NOT NULL,
  `Hr9plus` varchar(5) NOT NULL,
  `Hr9plusWaived1st` tinyint(1) NOT NULL,
  `Hr10` varchar(5) NOT NULL,
  `Hr10Waived1st` tinyint(1) NOT NULL,
  `Hr10plus` varchar(5) NOT NULL,
  `Hr10plusWaived1st` tinyint(1) NOT NULL,
  `Hr11` varchar(5) NOT NULL,
  `Hr11Waived1st` tinyint(1) NOT NULL,
  `Hr11plus` varchar(5) NOT NULL,
  `Hr11plusWaived1st` tinyint(1) NOT NULL,
  `Hr12` varchar(5) NOT NULL,
  `Hr12Waived1st` tinyint(1) NOT NULL,
  `Hr12plus` varchar(5) NOT NULL,
  `Hr12plusWaived1st` tinyint(1) NOT NULL,
  `Hr13` varchar(5) NOT NULL,
  `Hr13Waived1st` tinyint(1) NOT NULL,
  `Hr13plus` varchar(5) NOT NULL,
  `Hr13plusWaived1st` tinyint(1) NOT NULL,
  `Hr14` varchar(5) NOT NULL,
  `Hr14Waived1st` tinyint(1) NOT NULL,
  `Hr14plus` varchar(5) NOT NULL,
  `Hr14plusWaived1st` tinyint(1) NOT NULL,
  `Hr15` varchar(5) NOT NULL,
  `Hr15Waived1st` tinyint(1) NOT NULL,
  `Hr15plus` varchar(5) NOT NULL,
  `Hr15plusWaived1st` tinyint(1) NOT NULL,
  `Hr16` varchar(5) NOT NULL,
  `Hr16Waived1st` tinyint(1) NOT NULL,
  `Hr16plus` varchar(5) NOT NULL,
  `Hr16plusWaived1st` tinyint(1) NOT NULL,
  `Hr17` varchar(5) NOT NULL,
  `Hr17Waived1st` tinyint(1) NOT NULL,
  `Hr17plus` varchar(5) NOT NULL,
  `Hr17plusWaived1st` tinyint(1) NOT NULL,
  `Hr18` varchar(5) NOT NULL,
  `Hr18Waived1st` tinyint(1) NOT NULL,
  `Hr18plus` varchar(5) NOT NULL,
  `Hr18plusWaived1st` tinyint(1) NOT NULL,
  `Hr19` varchar(5) NOT NULL,
  `Hr19Waived1st` tinyint(1) NOT NULL,
  `Hr19plus` varchar(5) NOT NULL,
  `Hr19plusWaived1st` tinyint(1) NOT NULL,
  `Hr20` varchar(5) NOT NULL,
  `Hr20Waived1st` tinyint(1) NOT NULL,
  `Hr20plus` varchar(5) NOT NULL,
  `Hr20plusWaived1st` tinyint(1) NOT NULL,
  `Hr21` varchar(5) NOT NULL,
  `Hr21Waived1st` tinyint(1) NOT NULL,
  `Hr21plus` varchar(5) NOT NULL,
  `Hr21plusWaived1st` tinyint(1) NOT NULL,
  `Hr22` varchar(5) NOT NULL,
  `Hr22Waived1st` tinyint(1) NOT NULL,
  `Hr22plus` varchar(5) NOT NULL,
  `Hr22plusWaived1st` tinyint(1) NOT NULL,
  `Hr23` varchar(5) NOT NULL,
  `Hr23Waived1st` tinyint(1) NOT NULL,
  `Hr23plus` varchar(5) NOT NULL,
  `Hr23plusWaived1st` tinyint(1) NOT NULL,
  `Hr24` varchar(5) NOT NULL,
  `Hr24Waived1st` tinyint(1) NOT NULL,
  PRIMARY KEY (`pkID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `everrates`
--

INSERT INTO `everrates` (`pkID`, `trtype`, `name`, `GracePeriod`, `OTCutoff`, `OTCutoff1stWaived`, `OTPrice`, `LostPrice`, `TreatNextDayAsNewDay`, `EveryNthHour`, `NthHourRate`, `SucceedingRate`, `FractionThereOf`, `Hr0`, `Hr0Waived1st`, `Hr0plus`, `Hr0plusWaived1st`, `Hr1`, `Hr1Waived1st`, `Hr1plus`, `Hr1plusWaived1st`, `Hr2`, `Hr2Waived1st`, `Hr2plus`, `Hr2plusWaived1st`, `Hr3`, `Hr3Waived1st`, `Hr3plus`, `Hr3plusWaived1st`, `Hr4`, `Hr4Waived1st`, `Hr4plus`, `Hr4plusWaived1st`, `Hr5`, `Hr5Waived1st`, `Hr5plus`, `Hr5plusWaived1st`, `Hr6`, `Hr6Waived1st`, `Hr6plus`, `Hr6plusWaived1st`, `Hr7`, `Hr7Waived1st`, `Hr7plus`, `Hr7plusWaived1st`, `Hr8`, `Hr8Waived1st`, `Hr8plus`, `Hr8plusWaived1st`, `Hr9`, `Hr9Waived1st`, `Hr9plus`, `Hr9plusWaived1st`, `Hr10`, `Hr10Waived1st`, `Hr10plus`, `Hr10plusWaived1st`, `Hr11`, `Hr11Waived1st`, `Hr11plus`, `Hr11plusWaived1st`, `Hr12`, `Hr12Waived1st`, `Hr12plus`, `Hr12plusWaived1st`, `Hr13`, `Hr13Waived1st`, `Hr13plus`, `Hr13plusWaived1st`, `Hr14`, `Hr14Waived1st`, `Hr14plus`, `Hr14plusWaived1st`, `Hr15`, `Hr15Waived1st`, `Hr15plus`, `Hr15plusWaived1st`, `Hr16`, `Hr16Waived1st`, `Hr16plus`, `Hr16plusWaived1st`, `Hr17`, `Hr17Waived1st`, `Hr17plus`, `Hr17plusWaived1st`, `Hr18`, `Hr18Waived1st`, `Hr18plus`, `Hr18plusWaived1st`, `Hr19`, `Hr19Waived1st`, `Hr19plus`, `Hr19plusWaived1st`, `Hr20`, `Hr20Waived1st`, `Hr20plus`, `Hr20plusWaived1st`, `Hr21`, `Hr21Waived1st`, `Hr21plus`, `Hr21plusWaived1st`, `Hr22`, `Hr22Waived1st`, `Hr22plus`, `Hr22plusWaived1st`, `Hr23`, `Hr23Waived1st`, `Hr23plus`, `Hr23plusWaived1st`, `Hr24`, `Hr24Waived1st`) VALUES
(1, 'R', 'Retail', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+30', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(2, 'M', 'Motorcycle', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(3, 'BC', 'BPOCar', 30, 2, 0, '+0', '+300', 0, 10, '+200', '+10', 1, '+50', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(4, 'NQ', 'NonQCSenior', 30, 2, 0, '+0', '+300', 0, 10, '+200', '+10', 1, '+21', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(5, 'Q', 'QCSenior', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(6, 'D', 'Delivery', 30, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(8, 'P', 'Promo', 30, 2, 0, '+0', '+300', 1, 10, '+200', '+10', 1, '+38', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+10', 0, '+0', 0, '+200', 0),
(9, 'TX', 'Taxi', 0, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(10, 'JP', 'Jeep', 0, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0);

-- --------------------------------------------------------

--
-- Table structure for table `flatrate`
--

DROP TABLE IF EXISTS `flatrate`;
CREATE TABLE IF NOT EXISTS `flatrate` (
  `pkID` int(11) NOT NULL AUTO_INCREMENT,
  `trtype` varchar(2) NOT NULL,
  `name` varchar(20) NOT NULL,
  `GracePeriod` int(11) NOT NULL,
  `ExitGracePeriod` int(11) NOT NULL,
  `OTCutoff` int(11) NOT NULL,
  `OTCutoff1stWaived` tinyint(1) NOT NULL,
  `OTPrice` varchar(5) NOT NULL,
  `LostPrice` varchar(5) DEFAULT NULL,
  `TreatNextDayAsNewDay` tinyint(1) NOT NULL,
  `EveryNthHour` int(11) NOT NULL DEFAULT '0',
  `NthHourRate` varchar(5) NOT NULL DEFAULT '+0',
  `SucceedingRate` varchar(5) NOT NULL DEFAULT '+0',
  `FractionThereOf` tinyint(1) NOT NULL DEFAULT '1',
  `Hr0` varchar(5) NOT NULL,
  `Hr0Waived1st` tinyint(1) NOT NULL,
  `Hr0plus` varchar(5) NOT NULL,
  `Hr0plusWaived1st` tinyint(1) NOT NULL,
  `Hr1` varchar(5) NOT NULL,
  `Hr1Waived1st` tinyint(1) NOT NULL,
  `Hr1plus` varchar(5) NOT NULL,
  `Hr1plusWaived1st` tinyint(1) NOT NULL,
  `Hr2` varchar(5) NOT NULL,
  `Hr2Waived1st` tinyint(1) NOT NULL,
  `Hr2plus` varchar(5) NOT NULL,
  `Hr2plusWaived1st` tinyint(1) NOT NULL,
  `Hr3` varchar(5) NOT NULL,
  `Hr3Waived1st` tinyint(1) NOT NULL,
  `Hr3plus` varchar(5) NOT NULL,
  `Hr3plusWaived1st` tinyint(1) NOT NULL,
  `Hr4` varchar(5) NOT NULL,
  `Hr4Waived1st` tinyint(1) NOT NULL,
  `Hr4plus` varchar(5) NOT NULL,
  `Hr4plusWaived1st` tinyint(1) NOT NULL,
  `Hr5` varchar(5) NOT NULL,
  `Hr5Waived1st` tinyint(1) NOT NULL,
  `Hr5plus` varchar(5) NOT NULL,
  `Hr5plusWaived1st` tinyint(1) NOT NULL,
  `Hr6` varchar(5) NOT NULL,
  `Hr6Waived1st` tinyint(1) NOT NULL,
  `Hr6plus` varchar(5) NOT NULL,
  `Hr6plusWaived1st` tinyint(1) NOT NULL,
  `Hr7` varchar(5) NOT NULL,
  `Hr7Waived1st` tinyint(1) NOT NULL,
  `Hr7plus` varchar(5) NOT NULL,
  `Hr7plusWaived1st` tinyint(1) NOT NULL,
  `Hr8` varchar(5) NOT NULL,
  `Hr8Waived1st` tinyint(1) NOT NULL,
  `Hr8plus` varchar(5) NOT NULL,
  `Hr8plusWaived1st` tinyint(1) NOT NULL,
  `Hr9` varchar(5) NOT NULL,
  `Hr9Waived1st` tinyint(1) NOT NULL,
  `Hr9plus` varchar(5) NOT NULL,
  `Hr9plusWaived1st` tinyint(1) NOT NULL,
  `Hr10` varchar(5) NOT NULL,
  `Hr10Waived1st` tinyint(1) NOT NULL,
  `Hr10plus` varchar(5) NOT NULL,
  `Hr10plusWaived1st` tinyint(1) NOT NULL,
  `Hr11` varchar(5) NOT NULL,
  `Hr11Waived1st` tinyint(1) NOT NULL,
  `Hr11plus` varchar(5) NOT NULL,
  `Hr11plusWaived1st` tinyint(1) NOT NULL,
  `Hr12` varchar(5) NOT NULL,
  `Hr12Waived1st` tinyint(1) NOT NULL,
  `Hr12plus` varchar(5) NOT NULL,
  `Hr12plusWaived1st` tinyint(1) NOT NULL,
  `Hr13` varchar(5) NOT NULL,
  `Hr13Waived1st` tinyint(1) NOT NULL,
  `Hr13plus` varchar(5) NOT NULL,
  `Hr13plusWaived1st` tinyint(1) NOT NULL,
  `Hr14` varchar(5) NOT NULL,
  `Hr14Waived1st` tinyint(1) NOT NULL,
  `Hr14plus` varchar(5) NOT NULL,
  `Hr14plusWaived1st` tinyint(1) NOT NULL,
  `Hr15` varchar(5) NOT NULL,
  `Hr15Waived1st` tinyint(1) NOT NULL,
  `Hr15plus` varchar(5) NOT NULL,
  `Hr15plusWaived1st` tinyint(1) NOT NULL,
  `Hr16` varchar(5) NOT NULL,
  `Hr16Waived1st` tinyint(1) NOT NULL,
  `Hr16plus` varchar(5) NOT NULL,
  `Hr16plusWaived1st` tinyint(1) NOT NULL,
  `Hr17` varchar(5) NOT NULL,
  `Hr17Waived1st` tinyint(1) NOT NULL,
  `Hr17plus` varchar(5) NOT NULL,
  `Hr17plusWaived1st` tinyint(1) NOT NULL,
  `Hr18` varchar(5) NOT NULL,
  `Hr18Waived1st` tinyint(1) NOT NULL,
  `Hr18plus` varchar(5) NOT NULL,
  `Hr18plusWaived1st` tinyint(1) NOT NULL,
  `Hr19` varchar(5) NOT NULL,
  `Hr19Waived1st` tinyint(1) NOT NULL,
  `Hr19plus` varchar(5) NOT NULL,
  `Hr19plusWaived1st` tinyint(1) NOT NULL,
  `Hr20` varchar(5) NOT NULL,
  `Hr20Waived1st` tinyint(1) NOT NULL,
  `Hr20plus` varchar(5) NOT NULL,
  `Hr20plusWaived1st` tinyint(1) NOT NULL,
  `Hr21` varchar(5) NOT NULL,
  `Hr21Waived1st` tinyint(1) NOT NULL,
  `Hr21plus` varchar(5) NOT NULL,
  `Hr21plusWaived1st` tinyint(1) NOT NULL,
  `Hr22` varchar(5) NOT NULL,
  `Hr22Waived1st` tinyint(1) NOT NULL,
  `Hr22plus` varchar(5) NOT NULL,
  `Hr22plusWaived1st` tinyint(1) NOT NULL,
  `Hr23` varchar(5) NOT NULL,
  `Hr23Waived1st` tinyint(1) NOT NULL,
  `Hr23plus` varchar(5) NOT NULL,
  `Hr23plusWaived1st` tinyint(1) NOT NULL,
  `Hr24` varchar(5) NOT NULL,
  `Hr24Waived1st` tinyint(1) NOT NULL,
  PRIMARY KEY (`pkID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `flatrate`
--

INSERT INTO `flatrate` (`pkID`, `trtype`, `name`, `GracePeriod`, `ExitGracePeriod`, `OTCutoff`, `OTCutoff1stWaived`, `OTPrice`, `LostPrice`, `TreatNextDayAsNewDay`, `EveryNthHour`, `NthHourRate`, `SucceedingRate`, `FractionThereOf`, `Hr0`, `Hr0Waived1st`, `Hr0plus`, `Hr0plusWaived1st`, `Hr1`, `Hr1Waived1st`, `Hr1plus`, `Hr1plusWaived1st`, `Hr2`, `Hr2Waived1st`, `Hr2plus`, `Hr2plusWaived1st`, `Hr3`, `Hr3Waived1st`, `Hr3plus`, `Hr3plusWaived1st`, `Hr4`, `Hr4Waived1st`, `Hr4plus`, `Hr4plusWaived1st`, `Hr5`, `Hr5Waived1st`, `Hr5plus`, `Hr5plusWaived1st`, `Hr6`, `Hr6Waived1st`, `Hr6plus`, `Hr6plusWaived1st`, `Hr7`, `Hr7Waived1st`, `Hr7plus`, `Hr7plusWaived1st`, `Hr8`, `Hr8Waived1st`, `Hr8plus`, `Hr8plusWaived1st`, `Hr9`, `Hr9Waived1st`, `Hr9plus`, `Hr9plusWaived1st`, `Hr10`, `Hr10Waived1st`, `Hr10plus`, `Hr10plusWaived1st`, `Hr11`, `Hr11Waived1st`, `Hr11plus`, `Hr11plusWaived1st`, `Hr12`, `Hr12Waived1st`, `Hr12plus`, `Hr12plusWaived1st`, `Hr13`, `Hr13Waived1st`, `Hr13plus`, `Hr13plusWaived1st`, `Hr14`, `Hr14Waived1st`, `Hr14plus`, `Hr14plusWaived1st`, `Hr15`, `Hr15Waived1st`, `Hr15plus`, `Hr15plusWaived1st`, `Hr16`, `Hr16Waived1st`, `Hr16plus`, `Hr16plusWaived1st`, `Hr17`, `Hr17Waived1st`, `Hr17plus`, `Hr17plusWaived1st`, `Hr18`, `Hr18Waived1st`, `Hr18plus`, `Hr18plusWaived1st`, `Hr19`, `Hr19Waived1st`, `Hr19plus`, `Hr19plusWaived1st`, `Hr20`, `Hr20Waived1st`, `Hr20plus`, `Hr20plusWaived1st`, `Hr21`, `Hr21Waived1st`, `Hr21plus`, `Hr21plusWaived1st`, `Hr22`, `Hr22Waived1st`, `Hr22plus`, `Hr22plusWaived1st`, `Hr23`, `Hr23Waived1st`, `Hr23plus`, `Hr23plusWaived1st`, `Hr24`, `Hr24Waived1st`) VALUES
(1, 'R', 'Retail', 5, 0, 2, 0, '+0', '+300', 2, 0, '+0', '+10', 1, '+40', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(2, 'M', 'Motorcycle', 5, 0, 2, 0, '+0', '+300', 2, 0, '+0', '+0', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(4, 'NQ', 'NonQCSenior', 5, 0, 2, 0, '+0', '+300', 2, 10, '+200', '+10', 1, '+40', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(5, 'Q', 'QCSenior', 5, 0, 2, 0, '+0', '+300', 2, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(6, 'D', 'Delivery', 5, 0, 2, 0, '+0', '+300', 1, 0, '+0', '+0', 1, '+0', 0, '+0', 0, '+10', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(8, 'P', 'Promo', 5, 0, 2, 0, '+0', '+300', 2, 10, '+200', '+10', 1, '+15', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(9, 'PW', 'PWD', 5, 0, 2, 0, '+0', '+300', 2, 10, '+200', '+10', 1, '+30', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 1, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(25, 'J', 'Jeep', 5, 0, 2, 0, '+0', '+300', 2, 10, '+200', '+10', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(26, 'TC', 'Tricycle', 5, 0, 2, 0, '+0', '+300', 2, 10, '+200', '+10', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(31, 'BC', 'BPOEmployee', 5, 0, 2, 0, '+0', '+300', 1, 10, '+200', '+10', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(32, 'EM', 'Employees', 5, 0, 2, 0, '+0', '+300', 1, 10, '+200', '+10', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0),
(33, 'TN', 'Tenants', 5, 0, 2, 0, '+0', '+300', 1, 10, '+200', '+10', 1, '+20', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0, '+0', 0);

-- --------------------------------------------------------

--
-- Table structure for table `types`
--

DROP TABLE IF EXISTS `types`;
CREATE TABLE IF NOT EXISTS `types` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `typename` varchar(12) NOT NULL,
  `typecode` varchar(4) NOT NULL,
  `typedesc` varchar(20) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `types`
--

INSERT INTO `types` (`pkid`, `typename`, `typecode`, `typedesc`, `ACTIVE`) VALUES
(1, 'flatrate', 'flr1', 'Flat Rate', 1);
--
-- Database: `slotsavailable`
--
DROP DATABASE IF EXISTS `slotsavailable`;
CREATE DATABASE IF NOT EXISTS `slotsavailable` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `slotsavailable`;

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `pkId` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) NOT NULL,
  `available` int(11) NOT NULL,
  PRIMARY KEY (`pkId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`pkId`, `type`, `available`) VALUES
(1, 'motor', 287),
(2, 'car', 77);
--
-- Database: `terminals`
--
DROP DATABASE IF EXISTS `terminals`;
CREATE DATABASE IF NOT EXISTS `terminals` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `terminals`;

-- --------------------------------------------------------

--
-- Table structure for table `entry_terminals`
--

DROP TABLE IF EXISTS `entry_terminals`;
CREATE TABLE IF NOT EXISTS `entry_terminals` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(24) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `location` varchar(24) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `entry_terminals`
--

INSERT INTO `entry_terminals` (`id`, `name`, `status`, `location`) VALUES
(1, 'EN01', 0, 'CPT1 Entry');

-- --------------------------------------------------------

--
-- Table structure for table `exit_terminals`
--

DROP TABLE IF EXISTS `exit_terminals`;
CREATE TABLE IF NOT EXISTS `exit_terminals` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(24) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `location` varchar(24) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `exit_terminals`
--

INSERT INTO `exit_terminals` (`id`, `name`, `status`, `location`) VALUES
(1, 'EX02', 1, 'Gate 2'),
(2, 'EX03', 1, 'Gate 3'),
(3, 'EX04', 1, 'Gate 4'),
(4, 'EX01', 1, 'Gate 1');
--
-- Database: `unidb`
--
DROP DATABASE IF EXISTS `unidb`;
CREATE DATABASE IF NOT EXISTS `unidb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `unidb`;

-- --------------------------------------------------------

--
-- Table structure for table `birdate`
--

DROP TABLE IF EXISTS `birdate`;
CREATE TABLE IF NOT EXISTS `birdate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `LastDate` date DEFAULT NULL,
  `GenerateBy` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bir_acountdb`
--

DROP TABLE IF EXISTS `bir_acountdb`;
CREATE TABLE IF NOT EXISTS `bir_acountdb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  `IDNo` varchar(50) DEFAULT NULL,
  `Username` varchar(20) DEFAULT NULL,
  `Password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bir_print_log`
--

DROP TABLE IF EXISTS `bir_print_log`;
CREATE TABLE IF NOT EXISTS `bir_print_log` (
  `BIR_User` varchar(30) DEFAULT NULL,
  `BIR_ID` varchar(30) DEFAULT NULL,
  `Log_Date` datetime DEFAULT NULL,
  `OR_Num` varchar(255) DEFAULT NULL,
  `T_Num` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `companydb`
--

DROP TABLE IF EXISTS `companydb`;
CREATE TABLE IF NOT EXISTS `companydb` (
  `Company` varchar(255) DEFAULT 'Unnamed',
  `Address` varchar(255) DEFAULT '-',
  `Tel` varchar(255) DEFAULT '-',
  `TIN` varchar(255) DEFAULT '-',
  `Permit` varchar(255) DEFAULT '-',
  `ParkingSlot` bigint(20) DEFAULT '0',
  `ParkingArea` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `flatrate_schedule`
--

DROP TABLE IF EXISTS `flatrate_schedule`;
CREATE TABLE IF NOT EXISTS `flatrate_schedule` (
  `Day` int(20) DEFAULT NULL,
  `StartTime` time DEFAULT NULL,
  `IsStart` tinyint(1) DEFAULT NULL,
  `IsEnd` tinyint(1) DEFAULT NULL,
  `GraceEX` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `historydb`
--

DROP TABLE IF EXISTS `historydb`;
CREATE TABLE IF NOT EXISTS `historydb` (
  `ID` bigint(255) NOT NULL AUTO_INCREMENT,
  `CardCode` varchar(255) DEFAULT NULL,
  `Plate` varchar(255) DEFAULT NULL,
  `DTime` datetime DEFAULT NULL,
  `Lane` varchar(30) DEFAULT NULL,
  `PIC` longblob,
  `PIC2` longblob,
  `Zone` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hourlydb`
--

DROP TABLE IF EXISTS `hourlydb`;
CREATE TABLE IF NOT EXISTS `hourlydb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Vehicle` varchar(70) DEFAULT NULL,
  `Mark` varchar(20) DEFAULT NULL,
  `grace` bigint(20) DEFAULT NULL,
  `FirstMinutes` bigint(20) DEFAULT NULL,
  `firstAmount` double(10,2) DEFAULT NULL,
  `IntAmount` double(10,2) DEFAULT NULL,
  `Flatrate` double(10,2) DEFAULT NULL,
  `OverNight` double(10,2) DEFAULT NULL,
  `LostCard` double(10,2) DEFAULT NULL,
  `Command` varchar(10) DEFAULT NULL,
  `OV_Start` time DEFAULT NULL,
  `OV_End` time DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  `OV24` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `incomereport`
--

DROP TABLE IF EXISTS `incomereport`;
CREATE TABLE IF NOT EXISTS `incomereport` (
  `ID` bigint(255) NOT NULL AUTO_INCREMENT,
  `TRno` varchar(255) DEFAULT NULL,
  `Cardcode` varchar(255) DEFAULT NULL,
  `Plate` varchar(255) DEFAULT NULL,
  `Operator` varchar(255) DEFAULT NULL,
  `PC` varchar(255) DEFAULT NULL,
  `Timein` datetime DEFAULT NULL,
  `TimeOut` datetime DEFAULT NULL,
  `BusnessDate` date DEFAULT NULL,
  `Total` double(10,2) DEFAULT NULL,
  `Vat` double DEFAULT NULL,
  `NonVat` double DEFAULT NULL,
  `VatExemp` double DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `Tender` double(10,2) DEFAULT NULL,
  `Change` double(10,2) DEFAULT NULL,
  `Regular` double(10,2) DEFAULT NULL,
  `Overnight` double(10,2) DEFAULT NULL,
  `Lostcard` double(10,2) DEFAULT NULL,
  `Payment` varchar(100) DEFAULT NULL,
  `DiscountType` varchar(255) DEFAULT NULL,
  `DiscountAmount` double DEFAULT NULL,
  `DiscountReference` varchar(255) DEFAULT NULL,
  `Cash` double(10,2) DEFAULT NULL,
  `Credit` double(10,2) DEFAULT NULL,
  `CreditCardid` varchar(255) DEFAULT NULL,
  `CreditCardType` varchar(255) DEFAULT NULL,
  `VoucherAmount` double(10,2) DEFAULT NULL,
  `GPRef` varchar(255) DEFAULT NULL,
  `GPDiscount` double(10,2) DEFAULT NULL,
  `GPoint` double(10,2) DEFAULT NULL,
  `CompliType` varchar(100) DEFAULT NULL,
  `Compli` double(10,2) DEFAULT NULL,
  `CompliRef` varchar(100) DEFAULT NULL,
  `PrepaidType` varchar(100) DEFAULT NULL,
  `Prepaid` double(10,2) DEFAULT NULL,
  `PrepaidRef` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `TRno` (`TRno`,`Cardcode`,`Plate`,`Operator`,`PC`,`BusnessDate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `operators`
--

DROP TABLE IF EXISTS `operators`;
CREATE TABLE IF NOT EXISTS `operators` (
  `ID` bigint(255) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Username` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `NickName` varchar(255) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pos_report`
--

DROP TABLE IF EXISTS `pos_report`;
CREATE TABLE IF NOT EXISTS `pos_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `TR` bigint(20) DEFAULT NULL,
  `DT` date DEFAULT NULL,
  `OR_From` varchar(100) DEFAULT NULL,
  `OR_To` varchar(100) DEFAULT NULL,
  `VAT` double(10,2) DEFAULT NULL,
  `VAT_Sale` double(10,2) DEFAULT NULL,
  `Total` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `security`
--

DROP TABLE IF EXISTS `security`;
CREATE TABLE IF NOT EXISTS `security` (
  `ID` bigint(255) NOT NULL AUTO_INCREMENT,
  `Name` varchar(60) DEFAULT NULL,
  `Username` varchar(60) DEFAULT NULL,
  `Password` varchar(60) DEFAULT NULL,
  `R_name` varchar(100) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `taxdb`
--

DROP TABLE IF EXISTS `taxdb`;
CREATE TABLE IF NOT EXISTS `taxdb` (
  `Tax` double(10,2) DEFAULT NULL,
  `TaxLp` double(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblcard`
--

DROP TABLE IF EXISTS `tblcard`;
CREATE TABLE IF NOT EXISTS `tblcard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CardCode` varchar(100) DEFAULT NULL,
  `CardID` varchar(254) DEFAULT NULL,
  `Islost` tinyint(1) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblcompli`
--

DROP TABLE IF EXISTS `tblcompli`;
CREATE TABLE IF NOT EXISTS `tblcompli` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CompliType` varchar(100) DEFAULT NULL,
  `CompliAmount` double(10,2) DEFAULT NULL,
  `CompliAll` tinyint(1) DEFAULT NULL,
  `CompliVat` tinyint(1) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblcreditcard`
--

DROP TABLE IF EXISTS `tblcreditcard`;
CREATE TABLE IF NOT EXISTS `tblcreditcard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CreditCardType` varchar(255) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbldiscount`
--

DROP TABLE IF EXISTS `tbldiscount`;
CREATE TABLE IF NOT EXISTS `tbldiscount` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DiscountType` varchar(255) DEFAULT NULL,
  `VatExempt` tinyint(1) DEFAULT NULL,
  `Percentage` double(10,2) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblemplogs`
--

DROP TABLE IF EXISTS `tblemplogs`;
CREATE TABLE IF NOT EXISTS `tblemplogs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `EmpCardCode` varchar(200) DEFAULT NULL,
  `LogTime` datetime DEFAULT NULL,
  `Location` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblemployee`
--

DROP TABLE IF EXISTS `tblemployee`;
CREATE TABLE IF NOT EXISTS `tblemployee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `EmpName` varchar(200) DEFAULT NULL,
  `EmpCardCode` varchar(200) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblprepaid`
--

DROP TABLE IF EXISTS `tblprepaid`;
CREATE TABLE IF NOT EXISTS `tblprepaid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `PrepaidType` varchar(100) DEFAULT NULL,
  `PrepaidAmount` double(10,2) DEFAULT NULL,
  `PrepaidAll` tinyint(1) DEFAULT NULL,
  `PrepaidVAT` tinyint(1) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tbltimeframe`
--

DROP TABLE IF EXISTS `tbltimeframe`;
CREATE TABLE IF NOT EXISTS `tbltimeframe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CHname` varchar(255) DEFAULT NULL,
  `StartT` time DEFAULT NULL,
  `Timerange` bigint(20) DEFAULT NULL,
  `Succeed` double(10,2) DEFAULT NULL,
  `FixRate` double(10,2) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  `Overnight` double(10,2) DEFAULT NULL,
  `LostCard` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblviplogs`
--

DROP TABLE IF EXISTS `tblviplogs`;
CREATE TABLE IF NOT EXISTS `tblviplogs` (
  `CardId` varchar(255) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `CardCode` varchar(255) DEFAULT NULL,
  `Timein` varchar(255) DEFAULT NULL,
  `TimeOut` varchar(255) DEFAULT NULL,
  `PrkType` varchar(255) DEFAULT NULL,
  `Plate` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblvoucherincome`
--

DROP TABLE IF EXISTS `tblvoucherincome`;
CREATE TABLE IF NOT EXISTS `tblvoucherincome` (
  `ids` bigint(20) NOT NULL AUTO_INCREMENT,
  `TRno` varchar(255) NOT NULL,
  `BusnessDate` date DEFAULT NULL,
  `VoucherType` varchar(255) DEFAULT NULL,
  `VoucherRef` varchar(255) DEFAULT NULL,
  `VoucherAmount` double(10,2) DEFAULT NULL,
  `Operator` varchar(255) DEFAULT NULL,
  `timeout` datetime DEFAULT NULL,
  `POS` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ids`,`TRno`),
  UNIQUE KEY `id` (`ids`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tblvouchers`
--

DROP TABLE IF EXISTS `tblvouchers`;
CREATE TABLE IF NOT EXISTS `tblvouchers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `VoucherType` varchar(255) DEFAULT NULL,
  `AmountValue` double(10,2) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `timeindb`
--

DROP TABLE IF EXISTS `timeindb`;
CREATE TABLE IF NOT EXISTS `timeindb` (
  `ID` bigint(8) NOT NULL AUTO_INCREMENT,
  `CardCode` varchar(100) DEFAULT NULL,
  `Vehicle` varchar(100) DEFAULT NULL,
  `Plate` varchar(100) DEFAULT NULL,
  `Timein` datetime DEFAULT NULL,
  `Operator` varchar(100) DEFAULT NULL,
  `PC` varchar(100) DEFAULT NULL,
  `PIC` mediumblob,
  `PIC2` mediumblob,
  `Lane` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CardCode` (`CardCode`,`Vehicle`,`Plate`,`Timein`,`Operator`,`PC`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `timeindb`
--

INSERT INTO `timeindb` (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES
(28, '42C46C6A', 'CAR', 'BND457', '2019-07-30 10:46:49', NULL, 'Entry Zone 2', NULL, NULL, 'ENTRY'),
(29, 'E263736A', 'CAR', 'NCM346', '2019-07-30 10:46:57', NULL, 'Entry Zone 2', NULL, NULL, 'ENTRY');

-- --------------------------------------------------------

--
-- Table structure for table `usercontroldb`
--

DROP TABLE IF EXISTS `usercontroldb`;
CREATE TABLE IF NOT EXISTS `usercontroldb` (
  `Id` bigint(255) NOT NULL AUTO_INCREMENT,
  `RuleName` varchar(30) DEFAULT NULL,
  `ServerSet` tinyint(1) DEFAULT NULL,
  `UserSet` tinyint(1) DEFAULT NULL,
  `DeviceSet` tinyint(1) DEFAULT NULL,
  `ChargingSet` tinyint(1) DEFAULT NULL,
  `ReportSet` tinyint(1) DEFAULT NULL,
  `TerminateSet` tinyint(1) DEFAULT NULL,
  `Setreference` tinyint(1) DEFAULT NULL,
  `Cashier` tinyint(1) DEFAULT NULL,
  `LogReport` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vipdb`
--

DROP TABLE IF EXISTS `vipdb`;
CREATE TABLE IF NOT EXISTS `vipdb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `CardID` varchar(255) DEFAULT NULL,
  `ParkType` varchar(255) DEFAULT NULL,
  `Plate` varchar(255) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `CardCode` varchar(255) DEFAULT NULL,
  `MaxUse` decimal(10,0) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- Database: `wcdb`
--
DROP DATABASE IF EXISTS `wcdb`;
CREATE DATABASE IF NOT EXISTS `wcdb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `wcdb`;

-- --------------------------------------------------------

--
-- Table structure for table `prediction`
--

DROP TABLE IF EXISTS `prediction`;
CREATE TABLE IF NOT EXISTS `prediction` (
  `goalNum` varchar(12) NOT NULL,
  `cardNum` varchar(12) NOT NULL,
  `posDiff` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prediction`
--

INSERT INTO `prediction` (`goalNum`, `cardNum`, `posDiff`) VALUES
('1', '1', '1'),
('1', '2', '1'),
('2', '1', '3');
--
-- Database: `zread`
--
DROP DATABASE IF EXISTS `zread`;
CREATE DATABASE IF NOT EXISTS `zread` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `zread`;

-- --------------------------------------------------------

--
-- Table structure for table `lastdate`
--

DROP TABLE IF EXISTS `lastdate`;
CREATE TABLE IF NOT EXISTS `lastdate` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT,
  `sentinelID` varchar(5) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lastdate`
--

INSERT INTO `lastdate` (`pkid`, `sentinelID`, `date`) VALUES
(1, 'EX01', '2019-07-08 20:49:27');

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

DROP TABLE IF EXISTS `main`;
CREATE TABLE IF NOT EXISTS `main` (
  `terminalnum` varchar(4) NOT NULL,
  `datetimeOut` datetime NOT NULL,
  `datetimeIn` datetime NOT NULL,
  `todaysale` double UNSIGNED NOT NULL,
  `vatablesale` double UNSIGNED NOT NULL,
  `12vat` double UNSIGNED NOT NULL,
  `beginOR` bigint(20) UNSIGNED ZEROFILL NOT NULL,
  `endOR` bigint(20) UNSIGNED ZEROFILL NOT NULL,
  `beginTrans` varchar(36) NOT NULL,
  `endTrans` varchar(36) NOT NULL,
  `beginningVoidNo` varchar(36) NOT NULL DEFAULT '0',
  `endingVoidNo` varchar(36) NOT NULL DEFAULT '0',
  `oldGrand` double UNSIGNED NOT NULL,
  `newGrand` double UNSIGNED NOT NULL,
  `discounts` double NOT NULL DEFAULT '0',
  `voids` double NOT NULL DEFAULT '0',
  `zCount` bigint(32) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tellerCode` varchar(12) NOT NULL,
  `logINID` bigint(32) UNSIGNED NOT NULL,
  PRIMARY KEY (`zCount`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`terminalnum`, `datetimeOut`, `datetimeIn`, `todaysale`, `vatablesale`, `12vat`, `beginOR`, `endOR`, `beginTrans`, `endTrans`, `beginningVoidNo`, `endingVoidNo`, `oldGrand`, `newGrand`, `discounts`, `voids`, `zCount`, `tellerCode`, `logINID`) VALUES
('EX01', '2019-07-09 04:21:00', '2019-07-09 03:46:29', 543, 484.821429871821, 58.17857012817901, 00000000000000000001, 00000000000000000014, '0000000000000001', '0000000000000018', '0', '0', 0, 543, 48, 0, 1, '00001000', 1340000100034626),
('EX01', '0000-00-00 00:00:00', '2019-07-09 04:21:39', 0, 0, 0, 00000000000000000014, 00000000000000000000, '0000000000000019', '0', '0', '0', 543, 0, 0, 0, 2, '00001000', 1340000100042138),
('EX01', '0000-00-00 00:00:00', '2019-07-20 20:15:18', 0, 0, 0, 00000000000000000014, 00000000000000000000, '0000000000000052', '0', '0', '0', 543, 0, 0, 0, 3, '00001000', 14500001000201514);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
