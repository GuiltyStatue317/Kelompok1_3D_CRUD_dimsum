package pbo.dimsum.view;

import pbo.dimsum.dao.BahanBakuDAO;
import pbo.dimsum.dao.DimsumDAO;
import pbo.dimsum.dao.SupplierDAO;
import pbo.dimsum.model.EntitasMaster;
import pbo.dimsum.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormMainMenu extends JFrame {
    private final User session;
    private final JLabel lblWelcome = new JLabel();
    private final JButton btnSupplier = new JButton("DATA SUPPLIER (Pemasok)");
    private final JButton btnBahanBaku = new JButton("DATA BAHAN BAKU (Gudang)");
    private final JButton btnDimsum = new JButton("DATA DIMSUM (Produk Matang)");
    private final JButton btnTransaksi = new JButton("PERMINTAAN BAHAN (Transaksi Dapur)");
    private final JButton btnRingkasan = new JButton("Ringkasan Data Master");
    private final JButton btnLogout = new JButton("Logout");

    public FormMainMenu(User session) {
        this.session = session;
        setTitle("Menu Utama - Sistem Manajemen Dapur Dimsum");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buatTampilan();
        aturHakAkses();
    }

    private void buatTampilan() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 20));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel panelAtas = new JPanel(new BorderLayout());
        lblWelcome.setText("Halo, " + session.getNamaKaryawan() + " [@" + session.getUsername() + "] - Role: " + session.getRole());
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 13));
        panelAtas.add(lblWelcome, BorderLayout.WEST);
        panelAtas.add(btnLogout, BorderLayout.EAST);

        JPanel panelGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        Font fontTombol = new Font("SansSerif", Font.BOLD, 13);
        btnSupplier.setFont(fontTombol);
        btnBahanBaku.setFont(fontTombol);
        btnDimsum.setFont(fontTombol);
        btnTransaksi.setFont(fontTombol);

        panelGrid.add(btnSupplier);
        panelGrid.add(btnBahanBaku);
        panelGrid.add(btnDimsum);
        panelGrid.add(btnTransaksi);

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBawah.add(btnRingkasan);

        mainPanel.add(panelAtas, BorderLayout.NORTH);
        mainPanel.add(panelGrid, BorderLayout.CENTER);
        mainPanel.add(panelBawah, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        btnSupplier.addActionListener(e -> new FormSupplier().setVisible(true));
        btnBahanBaku.addActionListener(e -> new FormBahanBaku().setVisible(true));
        btnDimsum.addActionListener(e -> new FormDimsum().setVisible(true));
        btnTransaksi.addActionListener(e -> new FormTransaksiBahan(session).setVisible(true));
        btnRingkasan.addActionListener(e -> tampilkanRingkasanPolimorfik());
        btnLogout.addActionListener(e -> {
            new FormLogin().setVisible(true);
            this.dispose();
        });
    }

    private void aturHakAkses() {
        if (session.isStaff()) {
            btnTransaksi.setEnabled(false);
        } else {
            btnSupplier.setEnabled(false);
            btnBahanBaku.setEnabled(false);
            btnDimsum.setEnabled(false);
        }
    }

    private void tampilkanRingkasanPolimorfik() {
        try {
            List<EntitasMaster> semuaMaster = new ArrayList<>();
            semuaMaster.addAll(new SupplierDAO().getAll());
            semuaMaster.addAll(new BahanBakuDAO().getAll());
            semuaMaster.addAll(new DimsumDAO().getAll());

            StringBuilder sb = new StringBuilder("=== RINGKASAN SELURUH DATA MASTER ===\n\n");
            for (EntitasMaster entitas : semuaMaster) {
                sb.append("- ").append(entitas.getInfoRingkas()).append("\n");
            }

            JTextArea area = new JTextArea(sb.toString(), 15, 42);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area),
                    "Ringkasan Data Master", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            System.out.println("Gagal memuat ringkasan: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Gagal memuat ringkasan data.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }
}
