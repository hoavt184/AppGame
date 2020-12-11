-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 11, 2020 lúc 06:07 PM
-- Phiên bản máy phục vụ: 10.4.16-MariaDB
-- Phiên bản PHP: 7.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `btl`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tbl_user`
--

CREATE TABLE `tbl_user` (
  `userName` varchar(50) NOT NULL,
  `passWord` varchar(50) NOT NULL,
  `score` float NOT NULL DEFAULT 0,
  `totalWinMatch` int(11) NOT NULL DEFAULT 0,
  `averageMoveWinMatch` float NOT NULL DEFAULT 0,
  `totalLostMatch` int(11) NOT NULL DEFAULT 0,
  `averageMoveLostMatch` float NOT NULL DEFAULT 0,
  `totalMoveWinMatch` int(11) NOT NULL DEFAULT 0,
  `totalMoveLost` int(11) NOT NULL DEFAULT 0,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `tbl_user`
--

INSERT INTO `tbl_user` (`userName`, `passWord`, `score`, `totalWinMatch`, `averageMoveWinMatch`, `totalLostMatch`, `averageMoveLostMatch`, `totalMoveWinMatch`, `totalMoveLost`, `name`) VALUES
('11111', '1111', 0, 0, 0, 0, 0, 0, 0, ''),
('234', '1111', 0, 0, 0, 0, 0, 0, 0, ''),
('a', 'a', 19, 31, 19.32, 34, 3.41176, 483, 116, ''),
('b', 'b', 10, 11, 19.75, 32, 5.75, 158, 184, ''),
('b1', 'b', 0, 0, 0, 0, 0, 0, 0, 'b'),
('c', 'c', 0, 0, 0, 0, 0, 0, 0, ''),
('client1', '1', 0, 0, 0, 5, 0.2, 0, 1, ''),
('client2', '1', 0, 0, 0, 0, 0, 0, 0, '1111'),
('d', 'd', 1, 1, 21, 1, 8, 21, 8, ''),
('hoavt', '1', 2, 5, 30, 8, 1.875, 150, 15, '');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`userName`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
