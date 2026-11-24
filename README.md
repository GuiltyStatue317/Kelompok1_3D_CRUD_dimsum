# Sistem Manajemen Dapur Dimsum (CRUD & Transaksi Bahan Baku)

Aplikasi desktop berbasis **Java Swing** dan **JDBC (MySQL/MariaDB)** untuk mengelola data master gudang serta pencatatan transaksi permintaan bahan baku dari dapur di usaha kuliner dimsum.

Proyek ini disusun untuk memenuhi Tugas Akhir Mata Kuliah **Pemrograman Berorientasi Objek (PBO)** dengan menerapkan arsitektur modular 5 *package* (`config`, `model`, `dao`, `view`, `app`).

---

## Fitur Utama & Pembagian Hak Akses

Aplikasi menerapkan pembatasan hak akses berdasarkan peran (*Role-Based Access Control*):

1. **Role `Staff` (Bagian Gudang):**
   * Mengelola Data Master **Supplier** (Tambah, Ubah, Hapus, Cari).
   * Mengelola Data Master **Bahan Baku** & stok gudang.
   * Mengelola Data Master **Dimsum** (stok matang).
   * Melihat **Ringkasan Data Master** (*Polymorphism*).
2. **Role `Koki` (Bagian Dapur):**
   * Membuat **Transaksi Permintaan Bahan Baku** (nomor transaksi otomatis `TRX-YYYYMMDD-XXX`, keranjang item sementara, validasi ketersediaan stok, dan pemotongan stok gudang otomatis menggunakan transaksi database `commit`/`rollback`).
   * Melihat **Riwayat Transaksi** pengambilan bahan.

---

## Cara Menjalankan Aplikasi

### 1. Persiapan Database
1. Jalankan **Apache** dan **MySQL** pada **XAMPP Control Panel**.
2. Buka `http://localhost/phpmyadmin`.
3. Import atau jalankan skrip SQL dari file **`db_crud_dimsum.sql`** yang tersedia di repositori ini untuk membuat database `db_dapur_dimsum` beserta 6 tabel dan data awalnya.
4. *(Opsional)* Sesuaikan port MySQL pada `src/main/java/pbo/dimsum/config/Koneksi.java` jika port XAMPP Anda berbeda dari `3306`.

### 2. Menjalankan Program
* **Menggunakan IntelliJ IDEA (Gradle):**
  1. *Clone* atau buka folder proyek ini di IntelliJ IDEA.
  2. Tunggu proses *Gradle Sync* mengunduh `mysql-connector-j`.
  3. Jalankan file **`src/main/java/pbo/dimsum/app/Main.java`**.
* **Menggunakan Apache NetBeans (Java with Ant):**
  1. Buat proyek *Java Application* baru (tanpa *Main Class*).
  2. Tambahkan library `mysql-connector-j-x.x.x.jar` pada menu *Libraries*.
  3. Salin folder `pbo` dari `src/main/java/pbo` ke dalam folder `src/` proyek NetBeans, lalu jalankan `Main.java`.

---

## Akun Pengujian (Dummy Login)

| Role | Username | Password | Hak Akses |
| :--- | :--- | :--- | :--- |
| **Staff (Gudang)** | `admin1` | `admin123` | Akses penuh Data Master (*Supplier, Bahan Baku, Dimsum*) |
| **Koki (Dapur)** | `chef_eko` | `koki123` | Akses Transaksi Permintaan Bahan & Riwayat Transaksi |
