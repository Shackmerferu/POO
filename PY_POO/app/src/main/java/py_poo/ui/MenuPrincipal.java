package py_poo.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import py_poo.config.KeyBindings;
import py_poo.input.InputManager;

public class MenuPrincipal {
    protected InputManager input;
    private boolean configMode;
    private int configSelected;
    private int configActionIndex;
    private long lastConfigKeyTime;

    public MenuPrincipal(InputManager input) {
        this.input = input;
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
            configSelected = Math.min(KeyBindings.getActionNames().length, configSelected + 1);
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
}
