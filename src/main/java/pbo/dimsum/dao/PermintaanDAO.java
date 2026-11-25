package pbo.dimsum.dao;

import pbo.dimsum.config.Koneksi;
import pbo.dimsum.model.DetailPermintaan;
import pbo.dimsum.model.PermintaanBahan;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PermintaanDAO {

    public String generateIdPermintaan() throws SQLException {
        String tanggal = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefix = "TRX-" + tanggal + "-";
        String sql = "SELECT id_permintaan FROM permintaan_bahan WHERE id_permintaan LIKE ? ORDER BY id_permintaan DESC LIMIT 1";

        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastId = rs.getString("id_permintaan");
                    int nomorUrut = Integer.parseInt(lastId.substring(lastId.length() - 3)) + 1;
                    return prefix + String.format("%03d", nomorUrut);
                }
            }
        }
        return prefix + "001";
    }

    public List<PermintaanBahan> getRiwayatHeader() throws SQLException {
        List<PermintaanBahan> daftar = new ArrayList<>();
        String sql = "SELECT * FROM permintaan_bahan ORDER BY tgl_permintaan DESC";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PermintaanBahan p = new PermintaanBahan();
                p.setIdPermintaan(rs.getString("id_permintaan"));
                p.setIdUser(rs.getInt("id_user"));
                p.setTglPermintaan(rs.getTimestamp("tgl_permintaan"));
                p.setStatus(rs.getString("status"));
                daftar.add(p);
            }
        }
        return daftar;
    }

    public List<DetailPermintaan> getDetailByTransaksi(String idPermintaan) throws SQLException {
        List<DetailPermintaan> daftar = new ArrayList<>();
        String sql = "SELECT * FROM detail_permintaan WHERE id_permintaan = ?";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPermintaan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetailPermintaan d = new DetailPermintaan();
                    d.setIdDetail(rs.getInt("id_detail"));
                    d.setIdPermintaan(rs.getString("id_permintaan"));
                    d.setIdBahan(rs.getInt("id_bahan"));
                    d.setJumlahDiambil(rs.getInt("jumlah_diambil"));
                    daftar.add(d);
                }
            }
        }
        return daftar;
    }

    public boolean simpanTransaksi(PermintaanBahan header, List<DetailPermintaan> details) throws SQLException {
        String sqlHeader = "INSERT INTO permintaan_bahan (id_permintaan, id_user, tgl_permintaan, status) VALUES (?, ?, NOW(), ?)";
        String sqlDetail = "INSERT INTO detail_permintaan (id_permintaan, id_bahan, jumlah_diambil) VALUES (?, ?, ?)";
        String sqlPotongStok = "UPDATE bahan_baku SET stok_gudang = stok_gudang - ? WHERE id_bahan = ? AND stok_gudang >= ?";

        Connection conn = null;
        try {
            conn = Koneksi.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psHeader = conn.prepareStatement(sqlHeader)) {
                psHeader.setString(1, header.getIdPermintaan());
                psHeader.setInt(2, header.getIdUser());
                psHeader.setString(3, "Selesai");
                psHeader.executeUpdate();
            }

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail); PreparedStatement psStok = conn.prepareStatement(sqlPotongStok)) {

                for (DetailPermintaan item : details) {
                    psStok.setInt(1, item.getJumlahDiambil());
                    psStok.setInt(2, item.getIdBahan());
                    psStok.setInt(3, item.getJumlahDiambil());
                    int updated = psStok.executeUpdate();

                    if (updated == 0) {
                        throw new SQLException("Stok bahan ID " + item.getIdBahan() + " tidak mencukupi!");
                    }

                    psDetail.setString(1, header.getIdPermintaan());
                    psDetail.setInt(2, item.getIdBahan());
                    psDetail.setInt(3, item.getJumlahDiambil());
                    psDetail.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
