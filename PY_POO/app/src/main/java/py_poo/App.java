package py_poo;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LauncherFrame launcherFrame = new LauncherFrame();
            launcherFrame.setVisible(true);

        });

    }
}