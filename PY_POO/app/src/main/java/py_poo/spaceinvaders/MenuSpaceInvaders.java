package py_poo.spaceinvaders;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.input.MouseManager;
import py_poo.ui.Boton;
import py_poo.ui.MenuPrincipal;
public class MenuSpaceInvaders extends MenuPrincipal {
    
    private Boton botonJugar;
    private Boton botonSalir;
    private InputManager input;
    private MouseManager mouse;

    public MenuSpaceInvaders(InputManager input, MouseManager mouse) {
        super(input);
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
        String titulo = "S P A C E   I N V A D E R S";
        int textWidth = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (Constantes.WIDTH - textWidth) / 2, 150);

        botonJugar.renderizar(g);
        botonSalir.renderizar(g);
    }
//Revisar todo esto, es para dejarlo funcional.
  public void dibujar(java.awt.Graphics g) {
    // 1. Pintamos el fondo negro (el espacio exterior)
    g.setColor(java.awt.Color.BLACK);
    g.fillRect(0, 0, 800, 600); 

    // 2. Configuramos el título principal con temática espacial
    g.setFont(new java.awt.Font("Consolas", java.awt.Font.BOLD, 45));
    g.setColor(java.awt.Color.GREEN); 
    g.drawString("SPACE INVADERS", 240, 200);

    // 3. Texto de instrucción principal
    g.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 18));
    g.setColor(java.awt.Color.WHITE);
    g.drawString("PRESIONA 'ENTER' PARA DEFENDER LA TIERRA", 195, 340);
    
    // 4. Instrucciones de los controles para la nave
    g.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
    g.setColor(java.awt.Color.GRAY);
    g.drawString("Controles: Flechas Izq/Der (Moverse)  |  Espacio (Disparar)", 160, 400);
}
}


