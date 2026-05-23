package py_poo.spaceinvaders;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import py_poo.input.InputManager;
import py_poo.ui.MenuPrincipal;

public class MenuSpaceInvaders extends MenuPrincipal {

    private JuegoSpaceInvaders juego;

   
    public MenuSpaceInvaders(InputManager input, JuegoSpaceInvaders juego) {
      
        super(
            "Space Invaders - Menú Principal", 
            "SPACE INVADERS", 
            Color.CYAN, // Color c1 característico para el espacio
            "Moverse: ◄ / ►", // Texto para el lado izquierdo
            "Disparo: ESPACIO" // Texto para el lado derecho
        );
        this.juego = juego;

       
        for (Component comp : this.tarjetaCentral.getComponents()) {
            if (comp instanceof JButton) {
                JButton boton = (JButton) comp;
                
                // Reconfiguramos la acción según el texto del botón nativo
                if (boton.getText().equals("INICIAR PARTIDA")) {
                    // Borramos acciones viejas por las dudas e inyectamos la de Space Invaders
                    for (ActionListener al : boton.getActionListeners()) boton.removeActionListener(al);
                    boton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MenuSpaceInvaders.this.setVisible(false);
                            MenuSpaceInvaders.this.dispose();
                            if (juego != null) {
                                juego.crearPartida(); // Inicia el Space Invaders
                            }
                        }
                    });
                } 
                
                else if (boton.getText().equals("OPCIONES")) {
                    for (ActionListener al : boton.getActionListeners()) boton.removeActionListener(al);
                    boton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(null, "Dificultad: Invasión Alfa\nVidas: 3", "Opciones - Space Invaders", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
            }
        }
    }
}