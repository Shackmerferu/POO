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

    // --- VARIABLES DEL MENÚ ---
    private InputManager input;
    private int seleccion;

    // --- VARIABLES DE CONFIGURACIÓN ---
    private boolean configMode;
    private int configSelected;
    private int configActionIndex = -1;
    private long lastConfigKeyTime;

    // --- BASE DE DATOS ---
    private RankingManager rankingManager;
    private List<RankingEntry> topRanking;

    // --- CONFIGURACIÓN INDEPENDIENTE ---
    private int skinPaleta1 = 0;
    private int skinPaleta2 = 0;
    private int puntosMaxIndex = 0; // 0 -> 11pts, 1 -> 15pts, 2 -> 21pts
    private int volumenIndex = 0;   // 0 -> bajo, 1 -> medio, 2 -> alto

    // --- CONSTRUCTOR ---
    public MenuPong(InputManager input, Object mouse) {
        super("Pong", "Menú Principal", Color.BLACK, "Jugar", "Salir");
        this.input = input;
        this.seleccion = 0;

        this.rankingManager = new RankingManager();
        this.topRanking = rankingManager.cargarDetalleTop("Pong%", 10);
    }

    // --- GETTERS Y SETTERS BÁSICOS ---
    public int getSeleccion() { return seleccion; }
    public void setSeleccion(int seleccion) { this.seleccion = seleccion; }
    public boolean isConfigMode() { return configMode; }

    // GETTERS Y SETTERS PARA MEMORIA
    public int getSkinPaleta1() { return skinPaleta1; }
    public void setSkinPaleta1(int skin) { this.skinPaleta1 = skin; }

    public int getSkinPaleta2() { return skinPaleta2; }
    public void setSkinPaleta2(int skin) { this.skinPaleta2 = skin; }

    public int getPuntosMaxIndex() { return puntosMaxIndex; }
    public void setPuntosMaxIndex(int p) { this.puntosMaxIndex = p; }

    public int getVolumenIndex() { return volumenIndex; }
    public void setVolumenIndex(int v) { this.volumenIndex = v; }

    // Devuelve el valor real en números según lo elegido en el menú
    public int getPuntosMax() {
        if (puntosMaxIndex == 0) return 11;
        if (puntosMaxIndex == 1) return 15;
        return 21;
    }

    // Devuelve el texto exacto que el FXPlayer necesita
    public String getVolumenString() {
        if (volumenIndex == 0) return "bajo";
        if (volumenIndex == 1) return "medio";
        return "alto";
    }

    public void setConfigMode(boolean configMode) {
        this.configMode = configMode;
        configSelected = 0;
        configActionIndex = -1;
        lastConfigKeyTime = System.currentTimeMillis();
    }

    public void recargarRanking() {
        this.topRanking = rankingManager.cargarDetalleTop("Pong%", 10);
    }

    public void actualizar() {
    }

    // ═══════════════════════════════════════════════════════════════
    // LÓGICA DE CONFIGURACIÓN
    // ═══════════════════════════════════════════════════════════════
    public void actualizarConfig() {
        long now = System.currentTimeMillis();
        String[] actions = KeyBindings.getActionNames();

        // Ahora hay 4 opciones antes de la lista de teclas (Skin 1, Skin 2, Puntos, Volumen)
        int offsetTeclas = 4;
        int indexReset = offsetTeclas + actions.length; // Posición de "RESET VALORES"
        int indexVolver = indexReset + 1; // Posición de "VOLVER"
        int totalOpciones = indexVolver + 1;

        if (configActionIndex >= 0) {
            if (now - lastConfigKeyTime < 120) return;
            for (int code = 0; code < 256; code++) {
                if (input.isKeyPressed(code)) {
                    KeyBindings.set(actions[configActionIndex], code);
                    lastConfigKeyTime = now;
                    configActionIndex = -1;
                    break;
                }
            }
            return;
        }

        if (now - lastConfigKeyTime > 120) {
            if (input.isMenuUpPressed() || input.isWPressed()) {
                configSelected--;
                if (configSelected < 0) configSelected = totalOpciones - 1;
                lastConfigKeyTime = now;
            }
            if (input.isMenuDownPressed() || input.isSPressed()) {
                configSelected++;
                if (configSelected >= totalOpciones) configSelected = 0;
                lastConfigKeyTime = now;
            }
        }

        if (input.isEnterPressed() && (now - lastConfigKeyTime > 150)) {
            lastConfigKeyTime = now;

            if (configSelected == 0) {
                skinPaleta1 = (skinPaleta1 + 1) % 3;
            } else if (configSelected == 1) {
                skinPaleta2 = (skinPaleta2 + 1) % 3;
            } else if (configSelected == 2) {
                puntosMaxIndex = (puntosMaxIndex + 1) % 3;
            } else if (configSelected == 3) {
                volumenIndex = (volumenIndex + 1) % 3; // Alternar volumen
            } else if (configSelected >= offsetTeclas && configSelected < indexReset) {
                configActionIndex = configSelected - offsetTeclas;
            } else if (configSelected == indexReset) {
                // RESET VALORES
                skinPaleta1 = 0;
                skinPaleta2 = 0;
                puntosMaxIndex = 0;
                volumenIndex = 0;
            } else if (configSelected == indexVolver) {
                // VOLVER
                configMode = false;
            }
        }
    }

    public void dibujarConfig(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 800, 600);

        g.setFont(new Font("Consolas", Font.BOLD, 28));
        g.setColor(Color.CYAN);
        g.drawString("CONFIGURACIÓN", 280, 50);

        g.setFont(new Font("Consolas", Font.PLAIN, 18));

        String[] actions = KeyBindings.getActionNames();
        int offsetTeclas = 4;
        int indexReset = offsetTeclas + actions.length;
        int indexVolver = indexReset + 1;

        for (int i = 0; i <= indexVolver; i++) {
            // Ajustamos un poco la altura (y) para que todo quepa bien
            int y = 90 + i * 26;

            if (i == configSelected) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 150, y);
            } else {
                g.setColor(Color.WHITE);
            }

            if (i == 0) {
                String extra = skinPaleta1 == 0 ? "[ORIGINAL]" : (skinPaleta1 == 1 ? "[ALTERNA 1]" : "[ALTERNA 2]");
                g.drawString("Skin Jugador 1: " + extra, 180, y);
            }
            else if (i == 1) {
                String extra = skinPaleta2 == 0 ? "[ORIGINAL]" : (skinPaleta2 == 1 ? "[ALTERNA 1]" : "[ALTERNA 2]");
                g.drawString("Skin Jugador 2: " + extra, 180, y);
            }
            else if (i == 2) {
                String extra = puntosMaxIndex == 0 ? "[11]" : (puntosMaxIndex == 1 ? "[15]" : "[21]");
                g.drawString("Puntos para ganar: " + extra, 180, y);
            }
            else if (i == 3) {
                String extra = volumenIndex == 0 ? "[BAJO]" : (volumenIndex == 1 ? "[MEDIO]" : "[ALTO]");
                g.drawString("Volumen Música: " + extra, 180, y);
            }
            else if (i >= offsetTeclas && i < indexReset) {
                int actionIdx = i - offsetTeclas;
                String label = actions[actionIdx].replace("_", " ");
                String key = KeyBindings.keyName(KeyBindings.get(actions[actionIdx]));

                if (configActionIndex == actionIdx) {
                    g.setColor(Color.GREEN);
                    g.drawString(label + ": [ PRESIONA UNA TECLA ]", 180, y);
                } else {
                    g.drawString(label + ": " + key, 180, y);
                }
            }
            else if (i == indexReset) {
                g.drawString("RESET VALORES", 180, y);
            }
            else if (i == indexVolver) {
                g.drawString("VOLVER", 180, y);
            }
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString("Flechas: mover  |  Enter: cambiar / configurar  |  Esc: volver", 180, 560);
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

        String[] opciones = {"1 JUGADOR (VS IA)", "2 JUGADORES", "CONFIGURACIÓN", "SALIR"};
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
        g.drawString("Controles configurables en el menú de Opciones", 100, 440);

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
                if (detalle.isEmpty()) detalle = entry.puntaje() + " pts";
                String texto = String.format("%d. %s  %s", (i + 1), entry.jugador(), detalle);
                g.drawString(texto, 450, y);
                y += 20;
            }
        }
    }
}