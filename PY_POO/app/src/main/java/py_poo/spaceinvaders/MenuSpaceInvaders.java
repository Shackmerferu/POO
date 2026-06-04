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
    private boolean pantallaCompleta = false;
    private boolean sonidoActivado = true;
    private int skinNave = 0;       
    private int skinInvasores = 0;  
    private int skinProyectiles = 0;
    private int pistaMusical = 0;   
    private int velocidad = 1;
    private final String[] opcionesConfig = {
        "Modo Pantalla", "Sonido General", "Skin Nave", "Skin Invasores", 
        "Skin Proyectiles", "Pista Musical", "Velocidad Invasores", 
        "Configurar Teclas", "RESET VALORES", "VOLVER"
    };




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
    }
    public void dibujarConfig(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 800, 600);

        g.setFont(new Font("Consolas", Font.BOLD, 28));
        g.setColor(Color.CYAN); 
        g.drawString("CONFIGURACIÓN", 280, 60);

        g.setFont(new Font("Consolas", Font.PLAIN, 18));
        for (int i = 0; i < opcionesConfig.length; i++) {
            int y = 110 + i * 40;
            
            if (i == configSelected) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 150, y);
            } else {
                g.setColor(Color.WHITE);
            }
            
            
            String extra = "";
            if (i == 0) extra = pantallaCompleta ? " [PANTALLA COMPLETA]" : " [VENTANA]";
            else if (i == 1) extra = sonidoActivado ? " [ACTIVADO]" : " [DESACTIVADO]";
            else if (i == 2) extra = skinNave == 0 ? " [ORIGINAL]" : " [ALTERNATIVA]";
            else if (i == 3) extra = skinInvasores == 0 ? " [ORIGINAL]" : " [ALTERNATIVA]";
            else if (i == 4) extra = skinProyectiles == 0 ? " [ORIGINAL]" : " [ALTERNATIVA]";
            else if (i == 5) extra = pistaMusical == 0 ? " [ORIGINAL]" : " [TEMA 2]";
            else if (i == 6) {
                if(velocidad == 0) extra = " [LENTA]";
                else if(velocidad == 1) extra = " [MEDIA]";
                else extra = " [RÁPIDA]";
            }

           
            if (i == 7 && configActionIndex >= 0) {
                g.setColor(Color.GREEN);
                g.drawString(opcionesConfig[i] + ": [ PRESIONA UNA TECLA ]", 180, y);
            } else {
                g.drawString(opcionesConfig[i] + extra, 180, y);
            }
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString("Flechas: mover  |  Enter: cambiar/seleccionar  |  Esc: volver", 180, 560);
    }
    public void dibujar(Graphics g) {
       if (isConfigMode()) {
            dibujarConfig(g); 
            return;           
        }
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

        
        if (now - lastConfigKeyTime > delay) {
            if (input.isUpPressed() || input.isWPressed()) {
                configSelected--;
                if (configSelected < 0) configSelected = opcionesConfig.length - 1;
                lastConfigKeyTime = now;
            }
            if (input.isDownPressed() || input.isSPressed()) {
                configSelected++;
                if (configSelected >= opcionesConfig.length) configSelected = 0;
                lastConfigKeyTime = now;
            }
        }

        
        if (input.isEnterPressed() && (now - lastConfigKeyTime > 150)) {
            lastConfigKeyTime = now;
            switch(configSelected) {
                case 0: pantallaCompleta = !pantallaCompleta; break;
                case 1: sonidoActivado = !sonidoActivado; break;
                case 2: skinNave = (skinNave + 1) % 2; break; 
                case 3: skinInvasores = (skinInvasores + 1) % 2; break;
                case 4: skinProyectiles = (skinProyectiles + 1) % 2; break;
                case 5: pistaMusical = (pistaMusical + 1) % 2; break;
                case 6: velocidad = (velocidad + 1) % 3; break; 
                case 7: 
                    
                    configActionIndex = 0; 
                    break; 
                case 8: 
                    pantallaCompleta = false;
                    sonidoActivado = true;
                    skinNave = 0; skinInvasores = 0; skinProyectiles = 0;
                    pistaMusical = 0; velocidad = 1;
                    break;
                case 9: 
                    configMode = false; 
                    break; 
            }
        }
    }

    
    public boolean isConfigMode() {
        return configMode;
    }

   
}