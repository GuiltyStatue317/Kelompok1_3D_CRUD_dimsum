package pbo.dimsum.config;

import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public final class PenangananError {

    private PenangananError() {
    }

    public static void tampilKesalahan(Component parent, String pesan, String saran, SQLException ex) {
        System.out.println(pesan + ": " + ex.getMessage());
        JOptionPane.showMessageDialog(parent,
                pesan + ". " + saran,
                "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
