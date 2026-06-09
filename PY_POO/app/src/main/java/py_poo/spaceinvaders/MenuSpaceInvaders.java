package py_poo.spaceinvaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import py_poo.config.KeyBindings;
import py_poo.core.Constantes;
import py_poo.core.GameLoop;
import py_poo.input.InputManager;
import py_poo.ranking.RankingManager;
import py_poo.ranking.RankingManager.RankingEntry;
import py_poo.ui.MenuPrincipal;

public class MenuSpaceInvaders extends MenuPrincipal {

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
    private String[] opcionesControles = {
        "Mover Izquierda",
        "Mover Derecha",
        "Disparar",
        "Pausa",
        "Volver"
    };
    private boolean teclasmenuControles = false; 
    private int seleccionOpcinesControles = 0;
    private RankingManager rankingManager;
    private List<RankingEntry> topRanking;

    public MenuSpaceInvaders(InputManager input, JuegoSpaceInvaders juego) {
        super("Space Invaders", "Menú Principal", Color.CYAN, "Moverse: ◄ / ►", "Disparo: ESPACIO");
        this.input = input;
        this.juego = juego;
        this.seleccion = 0;
        this.ultimoTiempo = System.currentTimeMillis();
        
        this.rankingManager = new RankingManager();
        this.topRanking = rankingManager.cargarDetalleTop("Space%", 10);
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public void recargarRanking() {
        this.topRanking = rankingManager.cargarDetalleTop("Space%", 10);
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

        if (teclasmenuControles) {
            g.setFont(new Font("Consolas", Font.BOLD, 28));
            g.setColor(Color.CYAN);
            g.drawString("CONFIGURAR CONTROLES", 240, 60);

            g.setFont(new Font("Consolas", Font.PLAIN, 18));
            String[] acciones = KeyBindings.getActionNames();

            for (int i = 0; i < opcionesControles.length; i++) {
                int y = 150 + i * 45;

                if (i == seleccionOpcinesControles) {
                    g.setColor(Color.YELLOW);
                    g.drawString("> ", 150, y);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawString("  ", 150, y);
                }

                if (i == seleccionOpcinesControles && configActionIndex >= 0) {
                    g.setColor(Color.GREEN);
                    g.drawString(opcionesControles[i] + ": [ PRESIONA UNA TECLA ]", 180, y);
                } else {
                    String bindActual = "";
                    if (i < 4 && i < acciones.length) {
                        int code = KeyBindings.get(acciones[i]);
                        bindActual = " [" + java.awt.event.KeyEvent.getKeyText(code) + "]";
                    }
                    g.drawString(opcionesControles[i] + bindActual, 180, y);
                }
            }

            g.setFont(new Font("Consolas", Font.PLAIN, 12));
            g.setColor(Color.GRAY);
            g.drawString("Flechas: mover  |  Enter: seleccionar  |  Asigna la tecla elegida", 180, 560);
            return;
        }

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

            g.drawString(opcionesConfig[i] + extra, 180, y);
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
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(Color.CYAN);
        g.drawString("SPACE INVADERS", 100, 100);

        String[] opciones = {"INICIAR PARTIDA", "OPCIONES", "SALIR AL LAUNCHER"};
        g.setFont(new Font("Consolas", Font.PLAIN, 20));

        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g.setColor(Color.YELLOW);
                g.drawString("> " + opciones[i], 100, 200 + i * 35);
            } else {
                g.setColor(Color.WHITE);
                g.drawString("  " + opciones[i], 100, 200 + i * 35);
            }
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.GRAY);
        g.drawString("Flechas Arriba/Abajo para mover | ENTER para seleccionar", 100, 420);

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(230, 140, 60));
        g.drawString("Controles:", 100, 450);
        g.setColor(new Color(180, 180, 180));
        g.drawString("Moverse: ◄ / ► o Flechas", 100, 470);
        g.drawString("Disparo: ESPACIO", 100, 485);
        g.drawString("P: Pausa Local", 100, 500);
        g.drawString("Esc: Volver al Menú", 100, 515);

        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.setColor(Color.CYAN);
        g.drawString("--- TOP 10 RANKING ---", 450, 180);

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.WHITE);
        if (topRanking == null || topRanking.isEmpty()) {
            g.drawString("Aún no hay puntajes.", 450, 220);
        } else {
            int y = 220;
            for (int i = 0; i < topRanking.size(); i++) {
                RankingEntry entry = topRanking.get(i);
                String texto = String.format("%d. %s  NIVEL: %d  %d pts", (i + 1), entry.jugador(), entry.Nivel(), entry.puntaje());
                g.drawString(texto, 450, y);
                y += 20;
            }
        }
    }

    public void setConfigMode(boolean configMode) {
        this.configMode = configMode;
        configSelected = 0;
        configActionIndex = -1;
        teclasmenuControles = false;
        seleccionOpcinesControles = 0;
        lastConfigKeyTime = System.currentTimeMillis();
    }

    public void actualizarConfig() {
        long now = System.currentTimeMillis();

        if (teclasmenuControles) {
            if (configActionIndex >= 0) {
                if (now - lastConfigKeyTime < 150) return;
                for (int code = 0; code < 256; code++) {
                    if (code == java.awt.event.KeyEvent.VK_ENTER) {
                    continue; 
                     }   
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
                    seleccionOpcinesControles--;
                    if (seleccionOpcinesControles < 0) seleccionOpcinesControles = opcionesControles.length - 1;
                    lastConfigKeyTime = now;
                }
                if (input.isDownPressed() || input.isSPressed()) {
                    seleccionOpcinesControles++;
                    if (seleccionOpcinesControles >= opcionesControles.length) seleccionOpcinesControles = 0;
                    lastConfigKeyTime = now;
                }
            }

            if (input.isEnterPressed() && (now - lastConfigKeyTime > 150)) {
                lastConfigKeyTime = now;
                if (seleccionOpcinesControles == 4) {
                    teclasmenuControles = false;
                } else {
                    configActionIndex = seleccionOpcinesControles;
                }
            }
            return;
        }

        if (configActionIndex >= 0) {
            if (now - lastConfigKeyTime < 120) return;
            for (int code = 0; code < 256; code++) {
                if (code == java.awt.event.KeyEvent.VK_ENTER) {
                    continue; 
                }
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
                case 0:
                    pantallaCompleta = !pantallaCompleta;
                    GameLoop.toggleFullscreenStatic();
                    break;
                case 1: sonidoActivado = !sonidoActivado; break;
                case 2: skinNave = (skinNave + 1) % 2; break; 
                case 3: skinInvasores = (skinInvasores + 1) % 2; break;
                case 4: skinProyectiles = (skinProyectiles + 1) % 2; break;
                case 5: pistaMusical = (pistaMusical + 1) % 2; break;
                case 6: velocidad = (velocidad + 1) % 3; break; 
                case 7: 
                    teclasmenuControles = true;
                    seleccionOpcinesControles = 0;
                    break; 
                case 8:
                    if (pantallaCompleta) {
                        GameLoop.toggleFullscreenStatic();
                    }
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

    public boolean isSonidoActivado() {
        return this.sonidoActivado;
    }

    public int getSkinNave() {
        return skinNave;
    }

    public int getSkinInvasores() {
        return skinInvasores;
    }

    public int getSkinProyectiles() {
        return skinProyectiles;
    }

    public int getVelocidad() {
        return velocidad;
    }
}