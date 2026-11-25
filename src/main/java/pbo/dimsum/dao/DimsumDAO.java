package pbo.dimsum.dao;

import pbo.dimsum.config.Koneksi;
import pbo.dimsum.model.Dimsum;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DimsumDAO implements OperasiCRUD<Dimsum> {

    public List<Dimsum> getAll() throws SQLException {
        List<Dimsum> list = new ArrayList<>();
        String sql = "SELECT * FROM dimsum ORDER BY id_dimsum";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Dimsum> search(String keyword) throws SQLException {
        List<Dimsum> list = new ArrayList<>();
        String sql = "SELECT * FROM dimsum WHERE nama_dimsum LIKE ? ORDER BY id_dimsum";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean insert(Dimsum d) throws SQLException {
        String sql = "INSERT INTO dimsum (nama_dimsum, stok_matang) VALUES (?, ?)";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNamaDimsum());
            ps.setInt(2, d.getStokMatang());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Dimsum d) throws SQLException {
        String sql = "UPDATE dimsum SET nama_dimsum = ?, stok_matang = ? WHERE id_dimsum = ?";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNamaDimsum());
            ps.setInt(2, d.getStokMatang());
            ps.setInt(3, d.getIdDimsum());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int idDimsum) throws SQLException {
        String sql = "DELETE FROM dimsum WHERE id_dimsum = ?";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDimsum);
            return ps.executeUpdate() > 0;
        }
    }

    private Dimsum mapRow(ResultSet rs) throws SQLException {
        Dimsum d = new Dimsum();
        d.setIdDimsum(rs.getInt("id_dimsum"));
        d.setNamaDimsum(rs.getString("nama_dimsum"));
        d.setStokMatang(rs.getInt("stok_matang"));
        return d;
    }
}
