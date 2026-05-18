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
    }

    public void pause(){
        estado = EstadoJuego.PAUSA;
    }

    @Override
    protected void crearPartida() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearPartida'");
    }
}
