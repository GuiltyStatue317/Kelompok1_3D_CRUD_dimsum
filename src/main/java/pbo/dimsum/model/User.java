package pbo.dimsum.model;

public class User {
    private int idUser;
    private String username;
    private String password;
    private String namaKaryawan;
    private String role;

    public User() {}

    public User(int idUser, String username, String password, String namaKaryawan, String role) {
        this.idUser = idUser;
        setUsername(username);
        setPassword(password);
        setNamaKaryawan(namaKaryawan);
        setRole(role);
    }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username != null && !username.isBlank()) {
            this.username = username;
        }
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password != null && !password.isBlank()) {
            this.password = password;
        }
    }

    public String getNamaKaryawan() { return namaKaryawan; }
    public void setNamaKaryawan(String namaKaryawan) {
        if (namaKaryawan != null && !namaKaryawan.isBlank()) {
            this.namaKaryawan = namaKaryawan;
        }
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isStaff() {
        return "Staff".equalsIgnoreCase(role);
    }
}
