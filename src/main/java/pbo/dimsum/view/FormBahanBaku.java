package pbo.dimsum.view;

import pbo.dimsum.dao.BahanBakuDAO;
import pbo.dimsum.dao.OperasiCRUD;
import pbo.dimsum.model.BahanBaku;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class FormBahanBaku extends JFrame {
    private final JTextField fieldId = new JTextField();
    private final JTextField fieldNama = new JTextField();
    private final JTextField fieldStok = new JTextField();
    private final JComboBox<String> comboSatuan = new JComboBox<>(
            new String[]{"kg", "pcs", "gram", "liter", "bungkus"});
    private final JTextField fieldCari = new JTextField(18);
    private final JLabel labelJumlah = new JLabel("Jumlah data: 0");
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nama Bahan", "Stok Gudang", "Satuan"}, 0);
    private final JTable tabel = new JTable(model);
    private final OperasiCRUD<BahanBaku> dao = new BahanBakuDAO();

    public FormBahanBaku() {
        setTitle("Kelola Data Bahan Baku Gudang");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        fieldId.setEditable(false);
        buatTampilan();
        tampilkanSemua();
    }

    private void buatTampilan() {
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("ID Bahan (Otomatis):")); form.add(fieldId);
        form.add(new JLabel("Nama Bahan:")); form.add(fieldNama);
        form.add(new JLabel("Stok Gudang:")); form.add(fieldStok);
        form.add(new JLabel("Satuan:")); form.add(comboSatuan);

        JButton tombolSimpan = new JButton("Simpan");
        JButton tombolUbah = new JButton("Ubah");
        JButton tombolHapus = new JButton("Hapus");
        JButton tombolBersih = new JButton("Bersihkan");
        JPanel panelTombol = new JPanel();
        panelTombol.add(tombolSimpan);
        panelTombol.add(tombolUbah);
        panelTombol.add(tombolHapus);
        panelTombol.add(tombolBersih);

        JButton tombolCari = new JButton("Cari");
        JButton tombolRefresh = new JButton("Refresh");
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCari.add(new JLabel("Cari nama bahan:"));
        panelCari.add(fieldCari);
        panelCari.add(tombolCari);
        panelCari.add(tombolRefresh);
        panelCari.add(labelJumlah);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(form, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);
        add(panelAtas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(panelCari, BorderLayout.SOUTH);

        tombolSimpan.addActionListener(e -> simpan());
        tombolUbah.addActionListener(e -> ubah());
        tombolHapus.addActionListener(e -> hapus());
        tombolBersih.addActionListener(e -> bersihkan());
        tombolCari.addActionListener(e -> cari());
        tombolRefresh.addActionListener(e -> refresh());
        tabel.getSelectionModel().addListSelectionListener(this::pilihBaris);
    }

    private void isiTabel(List<BahanBaku> daftar) {
        model.setRowCount(0);
        for (BahanBaku bahan : daftar) {
            model.addRow(new Object[]{
                    bahan.getIdBahan(), bahan.getNamaBahan(),
                    bahan.getStokGudang(), bahan.getSatuan()
            });
        }
        labelJumlah.setText("Jumlah data: " + daftar.size());
    }

    private void tampilkanSemua() {
        try {
            isiTabel(dao.getAll());
        } catch (SQLException ex) {
            tampilKesalahan("Data tidak dapat dimuat", ex);
        }
    }

    private void cari() {
        String kata = fieldCari.getText().trim();
        if (kata.isEmpty()) {
            tampilkanSemua();
            return;
        }
        try {
            isiTabel(dao.search(kata));
        } catch (SQLException ex) {
            tampilKesalahan("Pencarian gagal", ex);
        }
    }

    private void refresh() {
        fieldCari.setText("");
        tampilkanSemua();
        bersihkan();
    }

    private boolean validasiInput() {
        String nama = fieldNama.getText().trim();
        String stok = fieldStok.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama bahan wajib diisi.");
            fieldNama.requestFocus();
            return false;
        }
        try {
            int nilaiStok = Integer.parseInt(stok);
            if (nilaiStok < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stok harus berupa angka minimal 0.");
            fieldStok.requestFocus();
            return false;
        }
        return true;
    }

    private BahanBaku ambilBahanDariForm() {
        int id = fieldId.getText().isEmpty() ? 0 : Integer.parseInt(fieldId.getText());
        return new BahanBaku(
                id,
                fieldNama.getText().trim(),
                Integer.parseInt(fieldStok.getText().trim()),
                (String) comboSatuan.getSelectedItem()
        );
    }

    private void simpan() {
        if (!validasiInput()) return;
        try {
            boolean berhasil = dao.insert(ambilBahanDariForm());
            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
                tampilkanSemua();
                bersihkan();
            }
        } catch (SQLException ex) {
            tampilKesalahan("Data gagal disimpan", ex);
        }
    }

    private void pilihBaris(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) return;
        int baris = tabel.getSelectedRow();
        if (baris >= 0) {
            fieldId.setText(model.getValueAt(baris, 0).toString());
            fieldNama.setText(model.getValueAt(baris, 1).toString());
            fieldStok.setText(model.getValueAt(baris, 2).toString());
            comboSatuan.setSelectedItem(model.getValueAt(baris, 3).toString());
        }
    }

    private void ubah() {
        if (tabel.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah.");
            return;
        }
        if (!validasiInput()) return;
        try {
            boolean berhasil = dao.update(ambilBahanDariForm());
            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah.");
                tampilkanSemua();
                bersihkan();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
            }
        } catch (SQLException ex) {
            tampilKesalahan("Data gagal diubah", ex);
        }
    }

    private void hapus() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus.");
            return;
        }
        int id = Integer.parseInt(model.getValueAt(baris, 0).toString());
        String nama = model.getValueAt(baris, 1).toString();
        int jawaban = JOptionPane.showConfirmDialog(this,
                "Hapus bahan " + nama + "?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);
        if (jawaban != JOptionPane.YES_OPTION) return;
        try {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                tampilkanSemua();
                bersihkan();
            }
        } catch (SQLException ex) {
            tampilKesalahan("Data gagal dihapus", ex);
        }
    }

    private void bersihkan() {
        fieldId.setText("");
        fieldNama.setText("");
        fieldStok.setText("");
        comboSatuan.setSelectedIndex(0);
        tabel.clearSelection();
        fieldNama.requestFocus();
    }

    private void tampilKesalahan(String pesan, SQLException ex) {
        System.out.println(pesan + ": " + ex.getMessage());
        JOptionPane.showMessageDialog(this,
                pesan + ". Periksa koneksi dan data.",
                "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
