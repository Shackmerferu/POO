package py_poo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import py_poo.config.KeyBindings;
import py_poo.input.InputManager;

public class MenuPrincipal extends JFrame {
    protected InputManager input;
    protected JLabel tituloLbl;
    protected JLabel ctrlJ1;
    protected JLabel ctrlJ2;
    protected JPanel tarjetaCentral;

    protected boolean configMode;
    protected int configSelected;
    protected int configActionIndex = -1;
    protected long lastConfigKeyTime;
    protected String[] configActions;

    protected String[] getConfigActions() {
        return KeyBindings.getActionNames();
    }

    public MenuPrincipal(String tituloVentana, String tituloJuego, Color c1, String ctrJ1, String ctrJ2) {
        super(tituloVentana);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tituloLbl = new JLabel(tituloJuego, SwingConstants.CENTER);
        tituloLbl.setFont(new Font("Arial", Font.BOLD, 36));
        tituloLbl.setForeground(c1);
        add(tituloLbl, BorderLayout.NORTH);

        ctrlJ1 = new JLabel(ctrJ1, SwingConstants.CENTER);
        ctrlJ1.setFont(new Font("Arial", Font.PLAIN, 18));
        add(ctrlJ1, BorderLayout.WEST);

        ctrlJ2 = new JLabel(ctrJ2, SwingConstants.CENTER);
        ctrlJ2.setFont(new Font("Arial", Font.PLAIN, 18));
        add(ctrlJ2, BorderLayout.EAST);

        tarjetaCentral = new JPanel();
        tarjetaCentral.setBackground(Color.LIGHT_GRAY);
        add(tarjetaCentral, BorderLayout.CENTER);
    }

    public void actualizar() {
    }

    public void renderizar() {
    }

    public boolean isConfigMode() {
        return configMode;
    }

    public void setConfigMode(boolean configMode) {
        this.configMode = configMode;
        this.configActions = getConfigActions();
        configSelected = 0;
        configActionIndex = -1;
        lastConfigKeyTime = System.currentTimeMillis();
    }

    public void actualizarConfig() {
        long now = System.currentTimeMillis();
        if (input == null) return;

        if (configActionIndex >= 0) {
            if (now - lastConfigKeyTime < 120) return;
            for (int code = 0; code < 256; code++) {
                if (input.isKeyPressed(code)) {
                    KeyBindings.set(configActions[configActionIndex], code);
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
            configSelected = Math.min(configActions.length + 1, configSelected + 1);
        }
        if (input.isEnterPressed()) {
            if (configSelected < configActions.length) {
                configActionIndex = configSelected;
                lastConfigKeyTime = now;
            } else if (configSelected == configActions.length) {
                reiniciarDefaults();
            } else if (configSelected == configActions.length + 1) {
                configMode = false;
            }
        }
    }

    private void guardarConfiguracion() {
    }

    private void reiniciarDefaults() {
        for (String accion : KeyBindings.getActionNames()) {
            int defaultKey = obtenerDefault(accion);
            KeyBindings.set(accion, defaultKey);
        }
    }

    private int obtenerDefault(String accion) {
        // NOTA: W/S (87/83) estÃ¡n reservados como J1_UP/J1_DOWN para el
        // control del Jugador 2 en Pong. Por eso MUSIC usa M (77) en
        // lugar de W como indican los requisitos de Lode Runner.
        switch (accion) {
            case "J1_UP": return 87;
            case "J1_DOWN": return 83;
            case "UP": return 38;
            case "DOWN": return 40;
            case "LEFT": return 37;
            case "RIGHT": return 39;
            case "DIG": return 32;
            case "PAUSE": return 80;
            case "SOUND": return 17;
            case "SOUND_FX": return 81;
            case "MUSIC": return 77;
            case "FULLSCREEN": return 48;
            case "RESET": return 27;
            default: return -1;
        }
    }

    public void dibujarConfig(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 800, 600);

        g.setFont(new Font("Consolas", Font.BOLD, 28));
        g.setColor(Color.CYAN);
        g.drawString("CONFIGURAR TECLAS", 220, 60);

        g.setFont(new Font("Consolas", Font.PLAIN, 16));
        for (int i = 0; i < configActions.length; i++) {
            int y = 110 + i * 35;
            if (i == configSelected) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 180, y);
            } else {
                g.setColor(Color.WHITE);
            }
            String label = configActions[i].replace("_", " ");
            String key = KeyBindings.keyName(KeyBindings.get(configActions[i]));

            if (configActionIndex == i) {
                g.setColor(Color.GREEN);
                g.drawString(label + ": [ PRESIONA UNA TECLA ]", 210, y);
            } else {
                g.drawString(label + ": " + key, 210, y);
            }
        }

        int base = 110 + configActions.length * 35;
        String[] opciones = {"REINICIAR VALORES", "VOLVER"};
        for (int i = 0; i < opciones.length; i++) {
            int y = base + i * 35;
            int idx = configActions.length + i;
            if (idx == configSelected) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 180, y);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(opciones[i], 210, y);
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString("Flechas: mover  |  Enter: seleccionar / cambiar  |  Esc: salir", 180, 580);
    }

}