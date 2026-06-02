package py_poo.spaceinvaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ui.MenuPrincipal;

public class MenuSpaceInvaders extends MenuPrincipal {
    
    private InputManager input;
    private JuegoSpaceInvaders juego;
    private int seleccion;
    private int delay = 150;
    private long ultimoTiempo;

    public MenuSpaceInvaders(InputManager input, JuegoSpaceInvaders juego) {
        super("Space Invaders", "Menú Principal", Color.CYAN, "Moverse: ◄ / ►", "Disparo: ESPACIO");
        this.input = input;
        this.juego = juego;
        this.seleccion = 0;
        this.ultimoTiempo = System.currentTimeMillis();

        // APAGAMOS LA INTERFAZ NATIVA DE SWING (El cuadrado gris y las etiquetas)
        // De esta forma dejamos el lienzo limpio para usar Graphics
        if (this.tarjetaCentral != null) this.tarjetaCentral.setVisible(false);
        if (this.tituloLbl != null) this.tituloLbl.setVisible(false);
        if (this.ctrlJ1 != null) this.ctrlJ1.setVisible(false);
        if (this.ctrlJ2 != null) this.ctrlJ2.setVisible(false);
        this.setVisible(false);
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }
    @Override
    public void setVisible(boolean b) {
       
        super.setVisible(false); 
        
        this.dispose(); 
    }
    @Override
    public void actualizar() {
        // Lógica para mover el cursor con un pequeño 'delay' para que no vuele
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoTiempo > delay) {
            
            if (input.isUpPressed()) {
                seleccion--;
                if (seleccion < 0) seleccion = 2; // Vuelve a la última opción
                ultimoTiempo = tiempoActual;
            }
            
            if (input.isDownPressed()) {
                seleccion++;
                if (seleccion > 2) seleccion = 0; // Vuelve a la primera opción
                ultimoTiempo = tiempoActual;
            }
        }
    }

    // Igual al de Pong, dibuja directamente sobre el motor
    public void dibujar(Graphics g) {
        // 1. Fondo negro espacial
        g.setColor(Color.BLACK);
        // Si no tenés Constantes.WIDTH, podés usar getWidth() y getHeight()
        g.fillRect(0, 0, 800, 600); 

        // 2. Título principal
        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(Color.CYAN); 
        g.drawString("SPACE INVADERS", 220, 200); 

        // 3. Opciones del menú (Índices: 0, 1, 2)
        String[] opciones = {"INICIAR PARTIDA", "OPCIONES", "SALIR AL LAUNCHER"};
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                // Opción resaltada con la flechita
                g.setColor(Color.YELLOW);
                g.drawString("> " + opciones[i], 280, 310 + i * 35);
            } else {
                g.setColor(Color.WHITE);
                g.drawString("  " + opciones[i], 280, 310 + i * 35);
            }
        }

        // 4. Controles al pie
        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.GRAY);
        g.drawString("Flechas Arriba/Abajo para mover | ENTER para seleccionar", 185, 420);
    }
}