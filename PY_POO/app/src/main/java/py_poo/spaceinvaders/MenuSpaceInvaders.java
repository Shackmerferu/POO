package py_poo.spaceinvaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.config.KeyBindings;
import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ui.MenuPrincipal;

public class MenuSpaceInvaders extends MenuPrincipal {
    
    private InputManager input;
    private JuegoSpaceInvaders juego;
    private int seleccion;
    private int delay = 150;
    private long ultimoTiempo;
    private boolean configMode;
    private int configSelected;
    private int configActionIndex = -1;
    private long lastConfigKeyTime;

    public MenuSpaceInvaders(InputManager input, JuegoSpaceInvaders juego) {
        super("Space Invaders", "Menú Principal", Color.CYAN, "Moverse: ◄ / ►", "Disparo: ESPACIO");
        this.input = input;
        this.juego = juego;
        this.seleccion = 0;
        this.ultimoTiempo = System.currentTimeMillis();

      
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
      
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoTiempo > delay) {
            
            if (input.isUpPressed()) {
                seleccion--;
                if (seleccion < 0) seleccion = 2;
            }
            
            if (input.isDownPressed()) {
                seleccion++;
                if (seleccion > 2) seleccion = 0; 
                ultimoTiempo = tiempoActual;
            }
        }
    }

    public void dibujar(Graphics g) {
       
        g.setColor(Color.BLACK);
       
        g.fillRect(0, 0, 800, 600); 

      
        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(Color.CYAN); 
        g.drawString("SPACE INVADERS", 220, 200); 

        
        String[] opciones = {"INICIAR PARTIDA", "OPCIONES", "SALIR AL LAUNCHER"};
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
        g.drawString("Flechas Arriba/Abajo para mover | ENTER para seleccionar", 185, 420);
    }

    public void setConfigMode(boolean configMode) {
        this.configMode = configMode;
        configSelected = 0;
        configActionIndex = -1;
        lastConfigKeyTime = System.currentTimeMillis();
    }
    public void actualizarConfig() {
        long now = System.currentTimeMillis();

        if (configActionIndex >= 0) {
            if (now - lastConfigKeyTime < 120) return;
            for (int code = 0; code < 256; code++) {
                if (input.isKeyPressed(code)) {
                    KeyBindings.set(KeyBindings.getActionNames()[configActionIndex], code);
                    lastConfigKeyTime = now;
                    configActionIndex = -1;
                    break;
                }
            }
            return;
        }

        if (input.isMenuUpPressed() || input.isWPressed()) {
            configSelected = Math.max(0, configSelected - 1);
        }
        if (input.isMenuDownPressed() || input.isSPressed()) {
            String[] actions = KeyBindings.getActionNames();
            configSelected = Math.min(actions.length, configSelected + 1);
        }
        if (input.isEnterPressed()) {
            String[] actions = KeyBindings.getActionNames();
            if (configSelected == actions.length) {
                configMode = false;
            } else {
                configActionIndex = configSelected;
                lastConfigKeyTime = now;
            }
        }
    }

    
    public boolean isConfigMode() {
        return configMode;
    }

   
}