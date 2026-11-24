package pbo.dimsum.view;

import pbo.dimsum.dao.UserDAO;
import pbo.dimsum.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FormLogin extends JFrame {
    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnLogin = new JButton("LOGIN");
    private final UserDAO userDAO = new UserDAO();

    public FormLogin() {
        setTitle("Login - Sistem Manajemen Dapur Dimsum");
        setSize(400, 270);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblJudul = new JLabel("DAPUR DIMSUM", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel lblSub = new JLabel("Silakan Login ke Akun Anda", SwingConstants.CENTER);
        JPanel panelJudul = new JPanel(new GridLayout(2, 1, 0, 4));
        panelJudul.add(lblJudul);
        panelJudul.add(lblSub);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 12));
        panelForm.add(new JLabel("Username:"));
        panelForm.add(txtUsername);
        panelForm.add(new JLabel("Password:"));
        panelForm.add(txtPassword);

        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogin.setPreferredSize(new Dimension(0, 36));
        btnLogin.addActionListener(e -> prosesLogin());

        mainPanel.add(panelJudul, BorderLayout.NORTH);
        mainPanel.add(panelForm, BorderLayout.CENTER);
        mainPanel.add(btnLogin, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void prosesLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi!");
            return;
        }

        try {
            User session = userDAO.login(user, pass);
            if (session != null) {
                JOptionPane.showMessageDialog(this, "Selamat datang, " + session.getNamaKaryawan());
                new FormMainMenu(session).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Database: " + ex.getMessage());
        }
    }
}
