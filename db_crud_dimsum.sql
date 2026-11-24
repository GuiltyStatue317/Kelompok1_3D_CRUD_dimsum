-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 24 Nov 2026 pada 21.34
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_crud_dimsum`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `bahan_baku`
--

CREATE TABLE `bahan_baku` (
  `id_bahan` int(11) NOT NULL,
  `nama_bahan` varchar(100) NOT NULL,
  `stok_gudang` int(11) NOT NULL DEFAULT 0,
  `satuan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `bahan_baku`
--

INSERT INTO `bahan_baku` (`id_bahan`, `nama_bahan`, `stok_gudang`, `satuan`) VALUES
(1, 'Daging Ayam Fillet', 50, 'kg'),
(2, 'Udang Kupas', 3, 'kg'),
(3, 'Kulit Pangsit Dimsum', 7263, 'pcs'),
(4, 'Tepung Sagu', 30, 'kg');

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_permintaan`
--

CREATE TABLE `detail_permintaan` (
  `id_detail` int(11) NOT NULL,
  `id_permintaan` varchar(20) DEFAULT NULL,
  `id_bahan` int(11) DEFAULT NULL,
  `jumlah_diambil` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `detail_permintaan`
--

INSERT INTO `detail_permintaan` (`id_detail`, `id_permintaan`, `id_bahan`, `jumlah_diambil`) VALUES
(1, 'TRX-20261124-001', 2, 22),
(2, 'TRX-20261124-001', 3, 892);

-- --------------------------------------------------------

--
-- Struktur dari tabel `dimsum`
--

CREATE TABLE `dimsum` (
  `id_dimsum` int(11) NOT NULL,
  `nama_dimsum` varchar(100) NOT NULL,
  `stok_matang` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `dimsum`
--

INSERT INTO `dimsum` (`id_dimsum`, `nama_dimsum`, `stok_matang`) VALUES
(1, 'Siomay Ayam Udang', 120),
(2, 'Hakau Udang', 80),
(3, 'Lumpia Kulit Tahu', 150);

-- --------------------------------------------------------

--
-- Struktur dari tabel `permintaan_bahan`
--

CREATE TABLE `permintaan_bahan` (
  `id_permintaan` varchar(20) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `tgl_permintaan` datetime DEFAULT current_timestamp(),
  `status` enum('Diproses','Selesai') DEFAULT 'Diproses'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `permintaan_bahan`
--

INSERT INTO `permintaan_bahan` (`id_permintaan`, `id_user`, `tgl_permintaan`, `status`) VALUES
('TRX-20261124-001', 1, '2026-11-24 23:13:03', 'Selesai');

-- --------------------------------------------------------

--
-- Struktur dari tabel `supplier`
--

CREATE TABLE `supplier` (
  `id_supplier` int(11) NOT NULL,
  `nama_supplier` varchar(100) NOT NULL,
  `no_telp` varchar(15) DEFAULT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `supplier`
--

INSERT INTO `supplier` (`id_supplier`, `nama_supplier`, `no_telp`, `alamat`) VALUES
(1, 'PT Segar Jaya Unggas', '08127211001', 'Jl. Soekarno Hatta, Bandar Lampung'),
(2, 'CV Lautan Udang Lampung', '08136922002', 'Jl. Yos Sudarso, Teluk Betung');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_karyawan` varchar(100) NOT NULL,
  `role` enum('Staff','Koki') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id_user`, `username`, `password`, `nama_karyawan`, `role`) VALUES
(1, 'staff1', 'admin123', 'Siti Rahmawati (Gudang)', 'Staff'),
(2, 'chef_eko', 'koki123', 'Eko Prasetyo (Dapur)', 'Koki');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `bahan_baku`
--
ALTER TABLE `bahan_baku`
  ADD PRIMARY KEY (`id_bahan`);

--
-- Indeks untuk tabel `detail_permintaan`
--
ALTER TABLE `detail_permintaan`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_permintaan` (`id_permintaan`),
  ADD KEY `id_bahan` (`id_bahan`);

--
-- Indeks untuk tabel `dimsum`
--
ALTER TABLE `dimsum`
  ADD PRIMARY KEY (`id_dimsum`);

--
-- Indeks untuk tabel `permintaan_bahan`
--
ALTER TABLE `permintaan_bahan`
  ADD PRIMARY KEY (`id_permintaan`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id_supplier`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `bahan_baku`
--
ALTER TABLE `bahan_baku`
  MODIFY `id_bahan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `detail_permintaan`
--
ALTER TABLE `detail_permintaan`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `dimsum`
--
ALTER TABLE `dimsum`
  MODIFY `id_dimsum` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `supplier`
--
ALTER TABLE `supplier`
  MODIFY `id_supplier` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `detail_permintaan`
--
ALTER TABLE `detail_permintaan`
  ADD CONSTRAINT `detail_permintaan_ibfk_1` FOREIGN KEY (`id_permintaan`) REFERENCES `permintaan_bahan` (`id_permintaan`) ON DELETE CASCADE,
  ADD CONSTRAINT `detail_permintaan_ibfk_2` FOREIGN KEY (`id_bahan`) REFERENCES `bahan_baku` (`id_bahan`);

--
-- Ketidakleluasaan untuk tabel `permintaan_bahan`
--
ALTER TABLE `permintaan_bahan`
  ADD CONSTRAINT `permintaan_bahan_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
