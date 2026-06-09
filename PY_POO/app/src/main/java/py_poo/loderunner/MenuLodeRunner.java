package py_poo.loderunner;

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

public class MenuLodeRunner extends MenuPrincipal {
    private int seleccion;
    private RankingManager rankingManager;
    private List<RankingEntry> topRanking;

    private int skinPersonaje = 0;
    private boolean teclasmenuControles = false;
    private int seleccionOpcionesControles = 0;

    private final String[] opcionesConfig = {
        "Skin Personaje",
        "Configurar Teclas",
        "RESET VALORES",
        "VOLVER"
    };

    private final String[] opcionesControles = {
        "Mover Arriba (W)",
        "Mover Abajo (S)",
        "Mover Izquierda",
        "Mover Derecha",
        "Cavar (X)",
        "Pausa (P)",
        "Menú (ESC)",
        "Volver"
    };

    private static final String[] ACCIONES_CONTROLES = {
        "J1_UP", "J1_DOWN", "LEFT", "RIGHT", "DIG", "PAUSE", "RESET"
    };

    private final int DELAY = 150;
    private long lastNavTime;
    private long lastMainNavTime;

    public MenuLodeRunner(InputManager input, Object mouse) {
        super("Lode Runner - Menu Principal", "LODE RUNNER", Color.GREEN, "Jugar", "Salir");
        this.input = input;
        this.seleccion = 0;
        this.rankingManager = new RankingManager();
        this.topRanking = rankingManager.cargarDetalleTop("Lode%", 10);
    }

    public int getSeleccion() { return seleccion; }

    public void setSeleccion(int seleccion) { this.seleccion = seleccion; }

    public void recargarRanking() {
        this.topRanking = rankingManager.cargarDetalleTop("Lode%", 10);
    }

    public int getSkinPersonaje() { return skinPersonaje; }

    public boolean navegarMainMenu(int direccion) {
        long now = System.currentTimeMillis();
        if (now - lastMainNavTime < DELAY) return false;
        lastMainNavTime = now;
        int nueva = seleccion + direccion;
        if (nueva >= 0 && nueva <= 2) {
            seleccion = nueva;
            return true;
        }
        return false;
    }

    @Override
    protected String[] getConfigActions() {
        return new String[]{"UP", "DOWN", "LEFT", "RIGHT", "DIG", "MUSIC", "FULLSCREEN", "RESET"};
    }

    @Override
    public void setConfigMode(boolean configMode) {
        super.setConfigMode(configMode);
        teclasmenuControles = false;
        seleccionOpcionesControles = 0;
        lastNavTime = System.currentTimeMillis();
    }

    public void actualizar() {}

    public void dibujar(Graphics g) {
        if (isConfigMode()) {
            dibujarConfig(g);
            return;
        }

        g.setColor(new Color(25, 27, 34));
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(new Color(255, 210, 60));
        g.drawString("LODE RUNNER", Constantes.WIDTH / 2 - 200, 100);

        String[] opciones = {"JUGAR", "CONFIG", "SALIR"};
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

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(230, 140, 60));
        g.drawString("Controles:", 100, 450);
        g.setColor(new Color(180, 180, 180));
        g.drawString("Flechas: Moverse", 100, 470);
        g.drawString("X: Cavar", 100, 485);
        g.drawString("W/A/S/D: Moverse", 100, 500);
        g.drawString("P: Pausa   ESC: Menu   Ctrl: Sonido", 100, 515);

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
                String texto = String.format("%d. %s  N%d  %d pts", (i + 1), entry.jugador(), entry.Nivel(), entry.puntaje());
                g.drawString(texto, 450, y);
                y += 20;
            }
        }
    }

    @Override
    public void dibujarConfig(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 800, 600);

        if (teclasmenuControles) {
            g.setFont(new Font("Consolas", Font.BOLD, 28));
            g.setColor(Color.CYAN);
            g.drawString("CONFIGURAR CONTROLES", 240, 60);

            g.setFont(new Font("Consolas", Font.PLAIN, 18));
            for (int i = 0; i < opcionesControles.length; i++) {
                int y = 150 + i * 45;

                if (i == seleccionOpcionesControles) {
                    g.setColor(Color.YELLOW);
                    g.drawString("> ", 150, y);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawString("  ", 150, y);
                }

                if (i == seleccionOpcionesControles && configActionIndex >= 0) {
                    g.setColor(Color.GREEN);
                    g.drawString(opcionesControles[i] + ": [ PRESIONA UNA TECLA ]", 180, y);
                } else {
                    String bindActual = "";
                    if (i < ACCIONES_CONTROLES.length) {
                        int code = KeyBindings.get(ACCIONES_CONTROLES[i]);
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
            if (i == 0) extra = skinPersonaje == 0 ? " [ORIGINAL]" : " [ALTERNATIVA]";

            g.drawString(opcionesConfig[i] + extra, 180, y);
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString("Flechas: mover  |  Enter: cambiar/seleccionar  |  Esc: volver", 180, 560);
    }

    public void actualizarConfig() {
        long now = System.currentTimeMillis();

        if (teclasmenuControles) {
            if (configActionIndex >= 0) {
                if (now - lastConfigKeyTime < 150) return;
                for (int code = 0; code < 256; code++) {
                    if (code == java.awt.event.KeyEvent.VK_ENTER) continue;
                    if (input.isKeyPressed(code)) {
                        KeyBindings.set(ACCIONES_CONTROLES[configActionIndex], code);
                        lastConfigKeyTime = now;
                        configActionIndex = -1;
                        break;
                    }
                }
                return;
            }

            if (now - lastNavTime > DELAY) {
                if (input.isUpPressed() || input.isWPressed()) {
                    seleccionOpcionesControles--;
                    if (seleccionOpcionesControles < 0) seleccionOpcionesControles = opcionesControles.length - 1;
                    lastNavTime = now;
                }
                if (input.isDownPressed() || input.isSPressed()) {
                    seleccionOpcionesControles++;
                    if (seleccionOpcionesControles >= opcionesControles.length) seleccionOpcionesControles = 0;
                    lastNavTime = now;
                }
            }

            if (input.isEnterPressed() && (now - lastConfigKeyTime > 150)) {
                lastConfigKeyTime = now;
                if (seleccionOpcionesControles == opcionesControles.length - 1) {
                    teclasmenuControles = false;
                } else {
                    configActionIndex = seleccionOpcionesControles;
                }
            }
            return;
        }

        if (configActionIndex >= 0) {
            if (now - lastConfigKeyTime < 120) return;
            for (int code = 0; code < 256; code++) {
                if (code == java.awt.event.KeyEvent.VK_ENTER) continue;
                if (input.isKeyPressed(code)) {
                    KeyBindings.set(getConfigActions()[configActionIndex], code);
                    lastConfigKeyTime = now;
                    configActionIndex = -1;
                    break;
                }
            }
            return;
        }

        if (now - lastNavTime > DELAY) {
            if (input.isUpPressed() || input.isWPressed()) {
                configSelected--;
                if (configSelected < 0) configSelected = opcionesConfig.length - 1;
                lastNavTime = now;
            }
            if (input.isDownPressed() || input.isSPressed()) {
                configSelected++;
                if (configSelected >= opcionesConfig.length) configSelected = 0;
                lastNavTime = now;
            }
        }

        if (input.isEnterPressed() && (now - lastConfigKeyTime > 150)) {
            lastConfigKeyTime = now;
            switch (configSelected) {
                case 0:
                    skinPersonaje = (skinPersonaje + 1) % 2;
                    break;
                case 1:
                    teclasmenuControles = true;
                    seleccionOpcionesControles = 0;
                    break;
                case 2:
                    skinPersonaje = 0;
                    break;
                case 3:
                    configMode = false;
                    break;
            }
        }
    }

    public boolean isConfigMode() {
        return configMode;
    }
}
