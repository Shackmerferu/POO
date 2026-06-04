package py_poo.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import py_poo.config.KeyBindings;
import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ranking.RankingManager;
import py_poo.ranking.RankingManager.RankingEntry;
import py_poo.ui.MenuPrincipal;

public class MenuPong extends MenuPrincipal {
    private InputManager input;
    private int seleccion;
    private boolean configMode;
    private int configSelected;
    private int configActionIndex = -1;
    private long lastConfigKeyTime;
    private RankingManager rankingManager;
    private List<RankingEntry> topRanking;
   

    public MenuPong(InputManager input, Object mouse) {
        super("Pong", "Menú Principal", Color.BLACK, "Jugar", "Salir");
        this.input = input;
        this.seleccion = 0;
        this.rankingManager = new RankingManager();
        this.topRanking = rankingManager.cargarDetalleTop("Pong%", 10);
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public boolean isConfigMode() {
        return configMode;
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

    public void dibujarConfig(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 800, 600);

        g.setFont(new Font("Consolas", Font.BOLD, 28));
        g.setColor(Color.CYAN);
        g.drawString("CONFIGURAR TECLAS", 220, 60);

        String[] actions = KeyBindings.getActionNames();
        g.setFont(new Font("Consolas", Font.PLAIN, 16));
        for (int i = 0; i < actions.length; i++) {
            int y = 110 + i * 35;
            if (i == configSelected) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 180, y);
            } else {
                g.setColor(Color.WHITE);
            }
            String label = actions[i].replace("_", " ");
            String key = KeyBindings.keyName(KeyBindings.get(actions[i]));

            if (configActionIndex == i) {
                g.setColor(Color.GREEN);
                g.drawString(label + ": [ PRESIONA UNA TECLA ]", 210, y);
            } else {
                g.drawString(label + ": " + key, 210, y);
            }
        }

        int y = 110 + actions.length * 35;
        if (configSelected == actions.length) {
            g.setColor(Color.YELLOW);
            g.drawString("> ", 180, y);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString("VOLVER", 210, y);

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString("Flechas: mover  |  Enter: seleccionar / cambiar  |  Esc: salir", 180, 580);
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
        g.drawString("ARCADE PONG", 260, 100);

        String[] opciones = {"1 JUGADOR (VS IA)", "2 JUGADORES", "CONFIG", "SALIR"};
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
        g.drawString("W/S o Flechas para mover | ENTER para seleccionar", 100, 420);
        g.drawString("Controles: W/S (J1)  |  Flechas Arriba/Abajo (J2)", 100, 440);

        // Dibujar Ranking Top 10
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
                
                String detalle = entry.juego().replace("Pong", "").trim();
                if (detalle.isEmpty()) {
                    detalle = entry.puntaje() + " pts";
                }
                
                String texto = String.format("%d. %s  %s", (i + 1), entry.jugador(), detalle);
                g.drawString(texto, 450, y);
                y += 20;
            }
        }
    }
}
