package pbo.dimsum.dao;

import pbo.dimsum.config.Koneksi;
import pbo.dimsum.model.BahanBaku;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BahanBakuDAO implements OperasiCRUD<BahanBaku> {

    public List<BahanBaku> getAll() throws SQLException {
        List<BahanBaku> list = new ArrayList<>();
        String sql = "SELECT * FROM bahan_baku ORDER BY id_bahan";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<BahanBaku> search(String keyword) throws SQLException {
        List<BahanBaku> list = new ArrayList<>();
        String sql = "SELECT * FROM bahan_baku WHERE nama_bahan LIKE ? ORDER BY id_bahan";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean insert(BahanBaku b) throws SQLException {
        String sql = "INSERT INTO bahan_baku (nama_bahan, stok_gudang, satuan) VALUES (?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getNamaBahan());
            ps.setInt(2, b.getStokGudang());
            ps.setString(3, b.getSatuan());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(BahanBaku b) throws SQLException {
        String sql = "UPDATE bahan_baku SET nama_bahan = ?, stok_gudang = ?, satuan = ? WHERE id_bahan = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getNamaBahan());
            ps.setInt(2, b.getStokGudang());
            ps.setString(3, b.getSatuan());
            ps.setInt(4, b.getIdBahan());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int idBahan) throws SQLException {
        String sql = "DELETE FROM bahan_baku WHERE id_bahan = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBahan);
            return ps.executeUpdate() > 0;
        }
    }

    private BahanBaku mapRow(ResultSet rs) throws SQLException {
        BahanBaku b = new BahanBaku();
        b.setIdBahan(rs.getInt("id_bahan"));
        b.setNamaBahan(rs.getString("nama_bahan"));
        b.setStokGudang(rs.getInt("stok_gudang"));
        b.setSatuan(rs.getString("satuan"));
        return b;
    }
}
