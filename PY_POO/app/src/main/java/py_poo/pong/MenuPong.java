package py_poo.pong;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import py_poo.ui.MenuPrincipal; 

public class MenuPong extends MenuPrincipal {

    private JuegoPong juego;
    

    public MenuPong(py_poo.input.InputManager input, JuegoPong juego) {
       
        super(
            "Arcade Pong - Menú Principal", 
            "ARCADE PONG", 
            Color.GREEN,
            "J1: W / S", 
            "J2: Flechas" 
        );
        this.juego = juego;

      
        this.tarjetaCentral.setLayout(new BoxLayout(this.tarjetaCentral, BoxLayout.Y_AXIS));

        
        JButton btnNuevaPartida = new JButton("NUEVA PARTIDA");
        JButton btnOpciones = new JButton("OPCIONES");
        JButton btnSalir = new JButton("SALIR");

       
        btnNuevaPartida.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOpciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        this.tarjetaCentral.add(Box.createVerticalStrut(50));

        
        btnNuevaPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                MenuPong.this.setVisible(false);
                MenuPong.this.dispose();
                if (juego != null) {
                    juego.crearPartida(); 
                }
            }
        });

        
        btnOpciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Opciones, seguimos laburando loco para");
            }
        });

       
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuPong.this.setVisible(false);
                MenuPong.this.dispose();
                System.exit(0); 
            }
        });

        
        this.tarjetaCentral.add(btnNuevaPartida);
        this.tarjetaCentral.add(Box.createVerticalStrut(15));
        this.tarjetaCentral.add(btnOpciones);
        this.tarjetaCentral.add(Box.createVerticalStrut(15));
        this.tarjetaCentral.add(btnSalir);
    }


  
}