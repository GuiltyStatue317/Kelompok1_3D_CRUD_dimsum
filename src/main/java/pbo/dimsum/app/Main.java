package pbo.dimsum.app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import pbo.dimsum.view.FormLogin;

public class Main {

    static void main() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println("Look and feel default digunakan.");
        }

        SwingUtilities.invokeLater(() -> new FormLogin().setVisible(true));
    }
}
