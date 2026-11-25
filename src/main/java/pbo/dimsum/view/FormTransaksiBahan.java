package pbo.dimsum.view;

import pbo.dimsum.config.PenangananError;
import pbo.dimsum.dao.BahanBakuDAO;
import pbo.dimsum.dao.PermintaanDAO;
import pbo.dimsum.model.BahanBaku;
import pbo.dimsum.model.DetailPermintaan;
import pbo.dimsum.model.PermintaanBahan;
import pbo.dimsum.model.User;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormTransaksiBahan extends JFrame {

    private final User session;
    private final JTextField fieldNoTransaksi = new JTextField(16);
    private final JTextField fieldPetugas = new JTextField(18);
    private final JComboBox<BahanBaku> comboBahan = new JComboBox<>();
    private final JTextField fieldJumlah = new JTextField(8);
    private final JLabel labelTotalItem = new JLabel("Total item: 0");
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID Bahan", "Nama Bahan Baku", "Jumlah Diambil", "Satuan"}, 0);
    private final JTable tabel = new JTable(model);
    private final BahanBakuDAO bahanDao = new BahanBakuDAO();
    private final PermintaanDAO permintaanDao = new PermintaanDAO();

    public FormTransaksiBahan(User session) {
        this.session = session;
        setTitle("Transaksi Permintaan Bahan Baku Dapur");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        fieldNoTransaksi.setEditable(false);
        fieldPetugas.setEditable(false);
        fieldPetugas.setText(session.getNamaKaryawan() + " (" + session.getRole() + ")");
        buatTampilan();
        muatDataAwal();
    }

    private void buatTampilan() {
        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.add(new JLabel("No. Transaksi:"));
        form.add(fieldNoTransaksi);
        form.add(new JLabel("Petugas:"));
        form.add(fieldPetugas);
        form.add(new JLabel("Pilih Bahan:"));
        form.add(comboBahan);
        form.add(new JLabel("Jumlah Ambil:"));
        form.add(fieldJumlah);

        JButton tombolTambah = new JButton("Tambah ke Daftar");
        JButton tombolHapusItem = new JButton("Hapus Baris");
        JButton tombolBersih = new JButton("Kosongkan Daftar");
        JPanel panelTombol = new JPanel();
        panelTombol.add(tombolTambah);
        panelTombol.add(tombolHapusItem);
        panelTombol.add(tombolBersih);

        JButton tombolRiwayat = new JButton("Riwayat Transaksi");
        JButton tombolSimpan = new JButton("Simpan Permintaan");
        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBawah.add(labelTotalItem);
        panelBawah.add(tombolRiwayat);
        panelBawah.add(tombolSimpan);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(form, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);
        add(panelAtas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);

        tombolTambah.addActionListener(_ -> tambahItem());
        tombolHapusItem.addActionListener(_ -> hapusItem());
        tombolBersih.addActionListener(_ -> bersihkan());
        tombolSimpan.addActionListener(_ -> simpan());
        tombolRiwayat.addActionListener(_ -> lihatRiwayat());
    }

    private void muatDataAwal() {
        try {
            fieldNoTransaksi.setText(permintaanDao.generateIdPermintaan());
            comboBahan.removeAllItems();
            List<BahanBaku> daftar = bahanDao.getAll();
            for (BahanBaku bahan : daftar) {
                comboBahan.addItem(bahan);
            }
        } catch (SQLException ex) {
            PenangananError.tampilKesalahan(this, "Gagal memuat data awal", "Periksa koneksi dan ketersediaan stok.", ex);
        }
    }

    private void tambahItem() {
        BahanBaku bahan = (BahanBaku) comboBahan.getSelectedItem();
        if (bahan == null) {
            return;
        }

        int jumlah;
        try {
            jumlah = Integer.parseInt(fieldJumlah.getText().trim());
            if (jumlah <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah ambil harus berupa angka lebih dari 0.");
            fieldJumlah.requestFocus();
            return;
        }

        if (jumlah > bahan.getStokGudang()) {
            JOptionPane.showMessageDialog(this, "Stok gudang tidak mencukupi. Sisa stok: " + bahan.getStokGudang());
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            int idTabel = Integer.parseInt(model.getValueAt(i, 0).toString());
            if (idTabel == bahan.getIdBahan()) {
                int jumlahLama = Integer.parseInt(model.getValueAt(i, 2).toString());
                if (jumlahLama + jumlah > bahan.getStokGudang()) {
                    JOptionPane.showMessageDialog(this, "Total pengambilan melebihi stok gudang.");
                    return;
                }
                model.setValueAt(jumlahLama + jumlah, i, 2);
                fieldJumlah.setText("");
                return;
            }
        }

        model.addRow(new Object[]{
                bahan.getIdBahan(), bahan.getNamaBahan(), jumlah, bahan.getSatuan()
        });
        labelTotalItem.setText("Total item: " + model.getRowCount());
        fieldJumlah.setText("");
        fieldJumlah.requestFocus();
    }

    private void hapusItem() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris item yang akan dihapus.");
            return;
        }
        model.removeRow(baris);
        labelTotalItem.setText("Total item: " + model.getRowCount());
    }

    private void bersihkan() {
        model.setRowCount(0);
        labelTotalItem.setText("Total item: 0");
        fieldJumlah.setText("");
    }

    private void simpan() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Daftar permintaan bahan masih kosong.");
            return;
        }

        int jawaban = JOptionPane.showConfirmDialog(this,
                "Simpan transaksi " + fieldNoTransaksi.getText() + "?", "Konfirmasi Simpan",
                JOptionPane.YES_NO_OPTION);
        if (jawaban != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String idTrx = fieldNoTransaksi.getText();
            PermintaanBahan header = new PermintaanBahan(idTrx, session.getIdUser(), null, "Selesai");
            List<DetailPermintaan> daftarDetail = new ArrayList<>();

            for (int i = 0; i < model.getRowCount(); i++) {
                int idBahan = Integer.parseInt(model.getValueAt(i, 0).toString());
                int jumlah = Integer.parseInt(model.getValueAt(i, 2).toString());
                daftarDetail.add(new DetailPermintaan(0, idTrx, idBahan, jumlah));
            }

            if (permintaanDao.simpanTransaksi(header, daftarDetail)) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan dan stok telah diperbarui.");
                bersihkan();
                muatDataAwal();
            }
        } catch (SQLException ex) {
            PenangananError.tampilKesalahan(this, "Transaksi gagal disimpan", "Periksa koneksi dan ketersediaan stok.", ex);
        }
    }

    private void lihatRiwayat() {
        try {
            List<PermintaanBahan> riwayat = permintaanDao.getRiwayatHeader();
            StringBuilder sb = new StringBuilder("=== RIWAYAT PERMINTAAN BAHAN ===\n\n");
            for (PermintaanBahan p : riwayat) {
                sb.append(p.getIdPermintaan())
                        .append(" | Tgl: ").append(p.getTglPermintaan())
                        .append(" | Status: ").append(p.getStatus()).append("\n");
                for (DetailPermintaan d : permintaanDao.getDetailByTransaksi(p.getIdPermintaan())) {
                    sb.append("   -> Detail #").append(d.getIdDetail())
                            .append(" (").append(d.getIdPermintaan()).append(")")
                            .append(" - ID Bahan: ").append(d.getIdBahan())
                            .append(", Jumlah: ").append(d.getJumlahDiambil()).append("\n");
                }
            }
            JTextArea area = new JTextArea(sb.toString(), 16, 45);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area),
                    "Riwayat Transaksi Dapur", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            PenangananError.tampilKesalahan(this, "Gagal memuat riwayat transaksi", "Periksa koneksi dan ketersediaan stok.", ex);
        }
    }
}
