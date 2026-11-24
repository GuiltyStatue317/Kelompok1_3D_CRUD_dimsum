package pbo.dimsum.view;

import pbo.dimsum.dao.UserDAO;
import pbo.dimsum.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class FormLogin extends JFrame {
    private final JTextField fieldUsername = new JTextField();
    private final JPasswordField fieldPassword = new JPasswordField();
    private final JButton tombolLogin = new JButton("LOGIN");
    private final UserDAO userDao = new UserDAO();

    public FormLogin() {
        setTitle("Login - Sistem Manajemen Dapur Dimsum");
        setSize(400, 270);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buatTampilan();
    }

    private void buatTampilan() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel labelJudul = new JLabel("DAPUR DIMSUM", SwingConstants.CENTER);
        labelJudul.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel labelSub = new JLabel("Silakan Login ke Akun Anda", SwingConstants.CENTER);
        JPanel panelJudul = new JPanel(new GridLayout(2, 1, 0, 4));
        panelJudul.add(labelJudul);
        panelJudul.add(labelSub);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 12));
        panelForm.add(new JLabel("Username:"));
        panelForm.add(fieldUsername);
        panelForm.add(new JLabel("Password:"));
        panelForm.add(fieldPassword);

        tombolLogin.setFont(new Font("SansSerif", Font.BOLD, 13));
        tombolLogin.setPreferredSize(new Dimension(0, 36));
        tombolLogin.addActionListener(e -> prosesLogin());

        mainPanel.add(panelJudul, BorderLayout.NORTH);
        mainPanel.add(panelForm, BorderLayout.CENTER);
        mainPanel.add(tombolLogin, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void prosesLogin() {
        String user = fieldUsername.getText().trim();
        String pass = new String(fieldPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi.");
            return;
        }

        try {
            User inputUser = new User(0, user, pass, "Guest", "Koki");
            User session = userDao.login(inputUser.getUsername(), inputUser.getPassword());
            if (session != null) {
                JOptionPane.showMessageDialog(this, "Selamat datang, " + session.getNamaKaryawan());
                new FormMainMenu(session).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            tampilKesalahan("Gagal terhubung ke database", ex);
        }
    }

    private void tampilKesalahan(String pesan, SQLException ex) {
        System.out.println(pesan + ": " + ex.getMessage());
        JOptionPane.showMessageDialog(this,
                pesan + ". Periksa koneksi database.",
                "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
