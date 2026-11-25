package pbo.dimsum.view;

import pbo.dimsum.config.PenangananError;
import pbo.dimsum.dao.DimsumDAO;
import pbo.dimsum.dao.OperasiCRUD;
import pbo.dimsum.model.Dimsum;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class FormDimsum extends JFrame {

    private final JTextField fieldId = new JTextField();
    private final JTextField fieldNama = new JTextField();
    private final JTextField fieldStok = new JTextField();
    private final JTextField fieldCari = new JTextField(18);
    private final JLabel labelJumlah = new JLabel("Jumlah data: 0");
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nama Dimsum", "Stok Matang (pcs)"}, 0);
    private final JTable tabel = new JTable(model);
    private final OperasiCRUD<Dimsum> dao = new DimsumDAO();

    public FormDimsum() {
        setTitle("Kelola Data Produk Dimsum");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        fieldId.setEditable(false);
        buatTampilan();
        tampilkanSemua();
    }

    private void buatTampilan() {
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("ID Dimsum (Otomatis):"));
        form.add(fieldId);
        form.add(new JLabel("Nama Dimsum:"));
        form.add(fieldNama);
        form.add(new JLabel("Stok Matang:"));
        form.add(fieldStok);

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
        panelCari.add(new JLabel("Cari nama dimsum:"));
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

        tombolSimpan.addActionListener(_ -> simpan());
        tombolUbah.addActionListener(_ -> ubah());
        tombolHapus.addActionListener(_ -> hapus());
        tombolBersih.addActionListener(_ -> bersihkan());
        tombolCari.addActionListener(_ -> cari());
        tombolRefresh.addActionListener(_ -> refresh());
        tabel.getSelectionModel().addListSelectionListener(this::pilihBaris);
    }

    private void isiTabel(List<Dimsum> daftar) {
        model.setRowCount(0);
        for (Dimsum dimsum : daftar) {
            model.addRow(new Object[]{
                    dimsum.getIdDimsum(), dimsum.getNamaDimsum(), dimsum.getStokMatang()
            });
        }
        labelJumlah.setText("Jumlah data: " + daftar.size());
    }

    private void tampilkanSemua() {
        try {
            isiTabel(dao.getAll());
        } catch (SQLException ex) {
            PenangananError.tampilKesalahan(this, "Data tidak dapat dimuat", "Periksa koneksi dan data.", ex);
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
            PenangananError.tampilKesalahan(this, "Pencarian gagal", "Periksa koneksi dan data.", ex);
        }
    }

    private void refresh() {
        fieldCari.setText("");
        tampilkanSemua();
        bersihkan();
    }

    private boolean inputTidakValid() {
        String nama = fieldNama.getText().trim();
        String stok = fieldStok.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dimsum wajib diisi.");
            fieldNama.requestFocus();
            return true;
        }
        try {
            int nilaiStok = Integer.parseInt(stok);
            if (nilaiStok < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stok harus berupa angka minimal 0.");
            fieldStok.requestFocus();
            return true;
        }
        return false;
    }

    private Dimsum ambilDimsumDariForm() {
        int id = fieldId.getText().isEmpty() ? 0 : Integer.parseInt(fieldId.getText());
        return new Dimsum(
                id,
                fieldNama.getText().trim(),
                Integer.parseInt(fieldStok.getText().trim())
        );
    }

    private void simpan() {
        if (inputTidakValid()) {
            return;
        }
        try {
            boolean berhasil = dao.insert(ambilDimsumDariForm());
            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
                tampilkanSemua();
                bersihkan();
            }
        } catch (SQLException ex) {
            PenangananError.tampilKesalahan(this, "Data gagal disimpan", "Periksa koneksi dan data.", ex);
        }
    }

    private void pilihBaris(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        int baris = tabel.getSelectedRow();
        if (baris >= 0) {
            fieldId.setText(model.getValueAt(baris, 0).toString());
            fieldNama.setText(model.getValueAt(baris, 1).toString());
            fieldStok.setText(model.getValueAt(baris, 2).toString());
        }
    }

    private void ubah() {
        if (tabel.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah.");
            return;
        }
        if (inputTidakValid()) {
            return;
        }
        try {
            boolean berhasil = dao.update(ambilDimsumDariForm());
            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah.");
                tampilkanSemua();
                bersihkan();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
            }
        } catch (SQLException ex) {
            PenangananError.tampilKesalahan(this, "Data gagal diubah", "Periksa koneksi dan data.", ex);
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
                "Hapus dimsum " + nama + "?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);
        if (jawaban != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                tampilkanSemua();
                bersihkan();
            }
        } catch (SQLException ex) {
            PenangananError.tampilKesalahan(this, "Data gagal dihapus", "Periksa koneksi dan data.", ex);
        }
    }

    private void bersihkan() {
        fieldId.setText("");
        fieldNama.setText("");
        fieldStok.setText("");
        tabel.clearSelection();
        fieldNama.requestFocus();
    }
}
