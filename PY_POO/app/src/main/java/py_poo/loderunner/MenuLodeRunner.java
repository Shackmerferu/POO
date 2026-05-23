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
        super("Lode Runner - Menú Principal", "LODE RUNNER", java.awt.Color.GREEN, "J1: W / S", "J2: Flechas");
        this.input = input;
        this.mouse = mouse;
        int centerX = Constantes.WIDTH / 2 - 100;
        int centerY = Constantes.HEIGHT / 2 - 50;
        botonJugar = new Boton("Jugar", centerX, centerY, 200, 50, () -> {
            botonJugar.click();
            System.out.println("Iniciar juego...");
        });
        botonSalir = new Boton("Salir", centerX, centerY + 70, 200, 50, () -> {
            botonSalir.click();
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
        String titulo = "LODE RUNNER";
        int textWidth = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (Constantes.WIDTH - textWidth) / 2, 150);

        botonJugar.renderizar(g);
        botonSalir.renderizar(g);
    }
//Revisar todo esto, es para dejarlo funcional.
   public void dibujar(java.awt.Graphics g) {
    // 1. Fondo gris oscuro (tipo piedra de mina / cueva)
    g.setColor(new java.awt.Color(25, 27, 34)); 
    g.fillRect(0, 0, 800, 600); 

    // 2. Título principal en Amarillo Oro brillante
    g.setFont(new java.awt.Font("Consolas", java.awt.Font.BOLD, 45));
    g.setColor(new java.awt.Color(255, 210, 60)); // Color oro
    g.drawString("LODE RUNNER", 265, 200);

    // 3. Texto de instrucción en un Blanco Hueso para que no sature
    g.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 18));
    g.setColor(new java.awt.Color(240, 240, 240));
    g.drawString("PRESIONA 'ENTER' PARA HACER GUITA LOCO", 200, 340);
    
    // 4. Instrucciones de los controles en un tono Ámbar/Naranja suave
    g.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
    g.setColor(new java.awt.Color(230, 140, 60));
    g.drawString("Controles: Flechas (Moverse)  |  Z / X (Cavar Izq / Der)", 185, 410);
}
}
