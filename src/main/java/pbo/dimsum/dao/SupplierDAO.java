package pbo.dimsum.dao;

import pbo.dimsum.config.Koneksi;
import pbo.dimsum.model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO implements OperasiCRUD<Supplier> {

    public List<Supplier> getAll() throws SQLException {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier ORDER BY id_supplier";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Supplier> search(String keyword) throws SQLException {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier WHERE nama_supplier LIKE ? OR alamat LIKE ? ORDER BY id_supplier";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pola = "%" + keyword + "%";
            ps.setString(1, pola);
            ps.setString(2, pola);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean insert(Supplier s) throws SQLException {
        String sql = "INSERT INTO supplier (nama_supplier, no_telp, alamat) VALUES (?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNamaSupplier());
            ps.setString(2, s.getNoTelp());
            ps.setString(3, s.getAlamat());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Supplier s) throws SQLException {
        String sql = "UPDATE supplier SET nama_supplier = ?, no_telp = ?, alamat = ? WHERE id_supplier = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNamaSupplier());
            ps.setString(2, s.getNoTelp());
            ps.setString(3, s.getAlamat());
            ps.setInt(4, s.getIdSupplier());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int idSupplier) throws SQLException {
        String sql = "DELETE FROM supplier WHERE id_supplier = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSupplier);
            return ps.executeUpdate() > 0;
        }
    }

    private Supplier mapRow(ResultSet rs) throws SQLException {
        Supplier s = new Supplier();
        s.setIdSupplier(rs.getInt("id_supplier"));
        s.setNamaSupplier(rs.getString("nama_supplier"));
        s.setNoTelp(rs.getString("no_telp"));
        s.setAlamat(rs.getString("alamat"));
        return s;
    }
}
