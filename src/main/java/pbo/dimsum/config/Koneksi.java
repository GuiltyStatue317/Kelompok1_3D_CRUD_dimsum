package pbo.dimsum.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Koneksi {

    private static final String HOST = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "db_crud_dimsum";
    private static final String URL = HOST + DB_NAME;
    private static final String USR = "root";
    private static final String PAW = "";

    private static boolean isInitialized = false;

    private Koneksi() {
    }

    public static Connection getConnection() throws SQLException {
        if (!isInitialized) {
            initDatabaseIfNeeded();
            isInitialized = true;
        }
        return DriverManager.getConnection(URL, USR, PAW);
    }

    private static void initDatabaseIfNeeded() {
        try (Connection conn = DriverManager.getConnection(HOST, USR, PAW);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            stmt.executeUpdate("USE " + DB_NAME);

            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'users'");
            if (rs.next()) {
                return;
            }

            InputStream is = Koneksi.class.getResourceAsStream("/database.sql");
            if (is == null) {
                System.err.println("File database.sql tidak ditemukan di folder resources.");
                return;
            }

            String sqlScript = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty()
                            && !line.startsWith("--")
                            && !line.startsWith("/*")
                            && !line.equalsIgnoreCase("START TRANSACTION;")
                            && !line.equalsIgnoreCase("COMMIT;"))
                    .collect(Collectors.joining("\n"));

            for (String query : sqlScript.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.executeUpdate(query.trim());
                }
            }

            System.out.println("Database " + DB_NAME + " berhasil dibuat.");

        } catch (Exception e) {
            System.err.println("Gagal inisialisasi database: " + e.getMessage());
        }
    }

    static void main() {
        try (Connection conn = getConnection()) {
            System.out.println("Koneksi berhasil: " + conn.getCatalog());
        } catch (SQLException ex) {
            System.out.println("Koneksi gagal: " + ex.getMessage());
        }
    }
}
