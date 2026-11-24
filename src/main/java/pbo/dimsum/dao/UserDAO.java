package pbo.dimsum.dao;

import pbo.dimsum.config.Koneksi;
import pbo.dimsum.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id_user, username, password, nama_karyawan, role "
                + "FROM users WHERE username = ? AND password = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setIdUser(rs.getInt("id_user"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setNamaKaryawan(rs.getString("nama_karyawan"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        }
        return null;
    }
}
