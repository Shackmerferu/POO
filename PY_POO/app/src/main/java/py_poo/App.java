package py_poo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class
App {
    public static void main(String[] args) {
            
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { 
            e.printStackTrace();
        }

     
        SwingUtilities.invokeLater(() -> {
            Launcher launcher = new Launcher();
            launcher.setVisible(true);
        });
    }
}