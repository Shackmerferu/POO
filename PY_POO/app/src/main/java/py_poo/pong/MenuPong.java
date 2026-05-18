package py_poo.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.input.MouseManager;
import py_poo.ui.Boton;
import py_poo.ui.MenuPrincipal;

public class MenuPong extends MenuPrincipal {
    private Boton botonJugar;
    private Boton botonSalir;
    private InputManager input;
    private MouseManager mouse;

    public MenuPong(InputManager input, MouseManager mouse) {
        this.input = input;
        this.mouse = mouse;
        int centerX = Constantes.WIDTH / 2 - 100;
        int centerY = Constantes.HEIGHT / 2 - 50;
        botonJugar = new Boton("Jugar", centerX, centerY, 200, 50, () -> {
            // Acción al hacer clic en "Jugar"
            System.out.println("Iniciar juego...");
        });
        botonSalir = new Boton("Salir", centerX, centerY + 70, 200, 50, () -> {
            // Acción al hacer clic en "Salir"
            System.out.println("Salir del juego...");
            System.exit(0);
        });
    }

    @Override
    public void actualizar() {
        if (input.isUpPressed()) {
            botonJugar.setSeleccionado(true);
            botonSalir.setSeleccionado(false);
        } else if (input.isDownPressed()) {
            botonJugar.setSeleccionado(false);
            botonSalir.setSeleccionado(true);
        }

        if (input.isEnterPressed()) {
            if (botonJugar.contains(mouse.getX(), mouse.getY())) {
                botonJugar.click();
            } else if (botonSalir.contains(mouse.getX(), mouse.getY())) {
                botonSalir.click();
            }
        }
    }

    public void renderizar(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        Font fuenteTitulo = g.getFont().deriveFont(Font.BOLD, 48f);
        g.setFont(fuenteTitulo);
        g.setColor(Color.WHITE);
        String titulo = "PONG";
        int textWidth = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (Constantes.WIDTH - textWidth) / 2, 150);

        botonJugar.renderizar(g);
        botonSalir.renderizar(g);
    }
}
