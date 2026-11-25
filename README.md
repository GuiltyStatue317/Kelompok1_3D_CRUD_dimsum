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
1. Jalankan **MySQL** pada **XAMPP Control Panel** (atau Laragon/MySQL Server).
2. Anda **tidak perlu** membuat database atau meng-import skrip SQL secara manual di phpMyAdmin. Saat aplikasi pertama kali dijalankan, program akan otomatis membuat database **`db_crud_dimsum`**, 6 tabel beserta relasinya, serta mengisi data awalnya dari file **`src/main/resources/database.sql`**.
3. *(Opsional)* Sesuaikan konfigurasi host, port, atau password MySQL pada `src/main/java/pbo/dimsum/config/Koneksi.java` jika port MySQL Anda berbeda dari `3306` atau menggunakan password.

### 2. Menjalankan Program
* **Menggunakan File Executable JAR:**
1. Unduh file **`Kelompok1_3D_CRUD_dimsum-1.0.jar`** pada halaman **Releases** (atau *build* sendiri melalui terminal dengan perintah `./gradlew clean build` untuk menghasilkan *Fat JAR* di folder `build/libs/`).
2. Jalankan file `.jar` tersebut melalui terminal/CMD:
     ```bash
     java -jar Kelompok1_3D_CRUD_dimsum-1.0.jar
     ```
* **Menggunakan IntelliJ IDEA (Gradle):**
1. *Clone* atau buka folder proyek ini di IntelliJ IDEA.
2. Tunggu proses *Gradle Sync* mengunduh `mysql-connector-j:8.3.0`.
3. Jalankan file **`src/main/java/pbo/dimsum/app/Main.java`**.
* **Menggunakan Apache NetBeans (Java with Ant):**
1. Buat proyek *Java Application* baru (tanpa *Main Class*).
2. Tambahkan library `mysql-connector-j-8.3.0.jar` pada menu *Libraries*.
3. Salin folder `pbo` dari `src/main/java/pbo` serta file **`database.sql`** dari `src/main/resources/` ke dalam folder `src/` proyek NetBeans, lalu jalankan `pbo.dimsum.app.Main`.

---

## Akun Pengujian (Dummy Login)

| Role | Username | Password | Hak Akses |
| :--- | :--- | :--- | :--- |
| **Staff (Gudang)** | `staff1` | `admin123` | Akses penuh Data Master (*Supplier, Bahan Baku, Dimsum*) |
| **Koki (Dapur)** | `chef_cj` | `koki123` | Akses Transaksi Permintaan Bahan & Riwayat Transaksi |

---

## Referensi & Catatan Pengembangan

* **Modul Perkuliahan:** Struktur arsitektur berlapis (`config`, `model`, `dao`, `view`, `app`), penerapan konsep OOP (*Encapsulation*, *Inheritance*, *Abstract Class*, *Interface*, *Polymorphism*), serta pola dasar CRUD dan *exception handling* mengacu pada **Modul Praktik Pemrograman Berorientasi Objek (Pertemuan 1–15)** oleh  Dosen pengampu **Ahmad Zuhdi, S.Kom., M.Kom.**
* **Pengembangan Fitur Lanjutan:** Implementasi komponen tambahan yang tidak dibahas di dalam modul praktik seperti autentikasi *login* dan pembagian hak akses (*RBAC*), transaksi multi-item dengan mekanisme `commit`/`rollback`, serta inisialisasi database otomatis dari skrip SQL dikembangkan dengan bantuan referensi eksternal seperti dari Mata Kuliah lain dan termasuk AI.
