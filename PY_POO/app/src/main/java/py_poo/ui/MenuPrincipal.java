package py_poo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipal extends JFrame{
    protected JLabel tituloLbl;
    protected JLabel ctrlJ1;
    protected JLabel ctrlJ2;
    protected JPanel tarjetaCentral;

    public MenuPrincipal(String tituloVentana, String tituloJuego, Color c1, String ctrJ1, String ctrJ2) {
        super(tituloVentana);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tituloLbl = new JLabel(tituloJuego, SwingConstants.CENTER);
        tituloLbl.setFont(new Font("Arial", Font.BOLD, 36));
        tituloLbl.setForeground(c1);
        add(tituloLbl, BorderLayout.NORTH);

        ctrlJ1 = new JLabel(ctrJ1, SwingConstants.CENTER);
        ctrlJ1.setFont(new Font("Arial", Font.PLAIN, 18));
        add(ctrlJ1, BorderLayout.WEST);

        ctrlJ2 = new JLabel(ctrJ2, SwingConstants.CENTER);
        ctrlJ2.setFont(new Font("Arial", Font.PLAIN, 18));
        add(ctrlJ2, BorderLayout.EAST);

        tarjetaCentral = new JPanel();
        tarjetaCentral.setBackground(Color.LIGHT_GRAY);
        add(tarjetaCentral, BorderLayout.CENTER);
    }

    public void actualizar() {
    }

    public void renderizar() {
    }
}
