package py_poo.pong;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ui.MenuPrincipal;

public class MenuPong extends MenuPrincipal {
    private InputManager input;
    private int seleccion;

    public MenuPong(InputManager input, Object mouse) {
        super("Pong", "Menú Principal", Color.BLACK, "Jugar", "Salir");
        this.input = input;
        this.seleccion = 0;
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public void actualizar() {
    }

    public void dibujar(Graphics g) {
        if (isConfigMode()) {
            dibujarConfig(g);
            return;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(Color.GREEN);
        g.drawString("ARCADE PONG", 260, 200);

        String[] opciones = {"1 JUGADOR (VS IA)", "2 JUGADORES", "CONFIG", "SALIR"};
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g.setColor(Color.YELLOW);
                g.drawString("> " + opciones[i], 280, 310 + i * 35);
            } else {
                g.setColor(Color.WHITE);
                g.drawString("  " + opciones[i], 280, 310 + i * 35);
            }
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.GRAY);
        g.drawString("W/S o Flechas para mover | ENTER para seleccionar", 205, 420);
        g.drawString("Controles: W/S (J1)  |  Flechas Arriba/Abajo (J2)", 205, 440);
    }
}
