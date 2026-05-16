package py_poo.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.input.InputManager;

public class JuegoPong extends VideoJuego {
    private boolean OpJuego;
    private InputManager input;

    public void setOpJuego(boolean opJuego) {
        this.OpJuego = opJuego;
    }

    public void iniciar() {
        super.iniciar();
        this.estado = EstadoJuego.MENU;
    }

    public void actualizar() {
        switch (estado) {
            case MENU -> {
                if (input != null) {
                    if (input.isKeyPressed('1')) {
                        OpJuego = false;
                        estado = EstadoJuego.JUGANDO;
                    } else if (input.isKeyPressed('2')) {
                        OpJuego = true;
                        estado = EstadoJuego.JUGANDO;
                    }
                }
            }
            case JUGANDO -> {
                // lógica de la partida
            }
            case PAUSA -> {
                // lógica de pausa
            }
        }
    }

    @Override
    public void renderizar(Graphics g) {
        if (estado == EstadoJuego.MENU) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 600);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Dialog", Font.BOLD, 36));
            g.drawString("PONG", 340, 150);
            g.setFont(new Font("Dialog", Font.PLAIN, 20));
            g.drawString("Presiona 1 — 1 vs 1", 300, 280);
            g.drawString("Presiona 2 — 1 vs Bot", 300, 330);
        }
    }

    public void pause(){
        estado = EstadoJuego.PAUSA;
    }
}
