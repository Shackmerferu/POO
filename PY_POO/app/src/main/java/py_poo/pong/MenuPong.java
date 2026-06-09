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

    // --- SKINS ---
    private int skinPaletas1 = 0;
    private int skinPaletas2 = 0;
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
    public int getSkinPaleta1() { return skinPaletas1; }
    public void setSkinPaleta1(int skin) { this.skinPaletas1 = skin; }

    public int getSkinPaleta2() { return skinPaletas2; }
    public void setSkinPaleta2(int skin) { this.skinPaletas2 = skin; }
    public void recargarRanking() {
        this.topRanking = rankingManager.cargarDetalleTop("Pong%", 10);
    }
    public void setConfigMode(boolean configMode) {
        this.configMode = configMode;
        configSelected = 0;
        configActionIndex = -1;
        lastConfigKeyTime = System.currentTimeMillis();
    }

    public void actualizar() {
    }

    // ═══════════════════════════════════════════════════════════════
    // LÓGICA DE CONFIGURACIÓN (SKINS + TECLAS)
    // ═══════════════════════════════════════════════════════════════
    public void actualizarConfig() {
        long now = System.currentTimeMillis();
        String[] actions = KeyBindings.getActionNames();

        // Ahora solo hay 1 opción (Skin) antes de la lista de teclas
        int offsetTeclas = 2;
        int indexReset = offsetTeclas + actions.length; // Posición de "RESET VALORES"
        int indexVolver = indexReset + 1; // Posición de "VOLVER"
        int totalOpciones = indexVolver + 1;

        // 1. Si está esperando que toquemos una tecla nueva...
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

        // 2. Navegación arriba y abajo
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

        // 3. Selección al apretar ENTER
        if (input.isEnterPressed() && (now - lastConfigKeyTime > 150)) {
            lastConfigKeyTime = now;

            if (configSelected == 0) {
                skinPaletas1 = (skinPaletas1 + 1) % 3; // Skin J1
            } else if (configSelected == 1) {
                skinPaletas2 = (skinPaletas2 + 1) % 3; // Skin J2
            } else if (configSelected >= offsetTeclas && configSelected < indexReset) {
                configActionIndex = configSelected - offsetTeclas;
            } else if (configSelected == indexReset) {
                skinPaletas1 = 0;
                skinPaletas2 = 0;
            } else if (configSelected == indexVolver) {
                configMode = false;
            }
        }
    }

    public void dibujarConfig(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 800, 600);

        g.setFont(new Font("Consolas", Font.BOLD, 28));
        g.setColor(Color.CYAN);
        g.drawString("CONFIGURACIÓN", 280, 60);

        g.setFont(new Font("Consolas", Font.PLAIN, 18));

        String[] actions = KeyBindings.getActionNames();
        int offsetTeclas = 2; // Solo Skin antes de las teclas
        int indexReset = offsetTeclas + actions.length;
        int indexVolver = indexReset + 1;

        // Bucle para dibujar todas las opciones dinámicamente
        for (int i = 0; i <= indexVolver; i++) {
            int y = 110 + i * 28;

            // Dibujar cursor
            if (i == configSelected) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 150, y);
            } else {
                g.setColor(Color.WHITE);
            }

            // Textos según la opción
            if (i == 0) {
                String extra = skinPaletas1 == 0 ? "[ORIGINAL]" : (skinPaletas1 == 1 ? "[SAMURAI]" : "[LUNAR]");
                g.drawString("Skin Jugador 1: " + extra, 180, y);
            }
            else if (i == 1) {
                String extra = skinPaletas2 == 0 ? "[ORIGINAL]" : (skinPaletas2 == 1 ? "[SAMURAI]" : "[LUNAR]");
                g.drawString("Skin Jugador 2: " + extra, 180, y);
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

    // ═══════════════════════════════════════════════════════════════
    // RENDERIZADO DEL MENÚ PRINCIPAL
    // ═══════════════════════════════════════════════════════════════
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

        // Ranking
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