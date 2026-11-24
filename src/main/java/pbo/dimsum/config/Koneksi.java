package pbo.dimsum.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/db_crud_dimsum";
    private static final String USR = "root";
    private static final String PAW = "";
    private Koneksi() {}
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USR, PAW);
    }
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Koneksi berhasil: " + conn.getCatalog());
        } catch (SQLException ex) {
            System.out.println("Koneksi gagal: " + ex.getMessage());
        }
    }
}