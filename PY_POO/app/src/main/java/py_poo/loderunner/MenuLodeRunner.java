package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.input.MouseManager;
import py_poo.ui.Boton;
import py_poo.ui.MenuPrincipal;

public class MenuLodeRunner extends MenuPrincipal {
    private Boton botonJugar;
    private Boton botonSalir;
    private InputManager input;
    private MouseManager mouse;

    public MenuLodeRunner(InputManager input, MouseManager mouse) {
        super("Lode Runner - Men Principal", "LODE RUNNER", java.awt.Color.GREEN, "J1: W / S", "J2: Flechas");
        this.input = input;
        this.mouse = mouse;
    }

    public void actualizar() {
        if (input.isUpPressed()) {
            botonJugar.setSeleccionado(true);
            botonSalir.setSeleccionado(false);
        } else if (input.isDownPressed()) {
            botonJugar.setSeleccionado(false);
            botonSalir.setSeleccionado(true);
        }
    }

    public void renderizar(Graphics g) {}

    public void dibujar(java.awt.Graphics g) {
        g.setColor(new Color(25, 27, 34));
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(new Color(255, 210, 60));
        g.drawString("LODE RUNNER", Constantes.WIDTH / 2 - 200, 160);

        g.setFont(new Font("Consolas", Font.PLAIN, 16));
        g.setColor(new Color(200, 200, 200));
        g.drawString("Recolecta todo el oro y escapa por la puerta!", 160, 220);

        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.setColor(new Color(255, 210, 60));
        g.drawString("PRESIONA ENTER PARA JUGAR", Constantes.WIDTH / 2 - 160, 320);

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(new Color(230, 140, 60));
        g.drawString("Controles:", Constantes.WIDTH / 2 - 60, 390);
        g.setColor(new Color(180, 180, 180));
        g.drawString("Flechas: Moverse", Constantes.WIDTH / 2 - 100, 420);
        g.drawString("Z: Cavar izquierda   X: Cavar derecha", Constantes.WIDTH / 2 - 160, 445);
        g.drawString("W/S: Subir/Bajar escaleras", Constantes.WIDTH / 2 - 140, 470);
        g.drawString("P: Pausa   ESC: Menu   Ctrl: Sonido", Constantes.WIDTH / 2 - 160, 495);

        g.setColor(new Color(100, 100, 100));
        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.drawString("v1.0 - Programacion Orientada a Objetos", Constantes.WIDTH / 2 - 160, 560);
    }
}
