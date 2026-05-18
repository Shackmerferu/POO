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
    private MenuPong menu;

    public void setOpJuego(boolean opJuego) {
        this.OpJuego = opJuego;
    }

    public void iniciar() {
        super.iniciar();
        this.estado = EstadoJuego.MENU;
        this.input = new InputManager();
        this.menu = new MenuPong(input, null);
        this.input = new InputManager();
        renderizar(null);
        actualizar();
    }

    public void actualizar() {
        switch (estado) {
            case MENU:
                if (input.isEnterPressed()) {
                    estado = EstadoJuego.JUGANDO;
                }
                break;
            case JUGANDO:
                
                break;
            case PAUSA:
                if (input.isEnterPressed()) {
                    estado = EstadoJuego.JUGANDO;
                }
                break;
        }
    }

    public void pause(){
        estado = EstadoJuego.PAUSA;
    }

    @Override
    protected void crearPartida() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearPartida'");
    }
    @Override
    public String getGanador(){
        return Nombre;

    }
    @Override
    public String getPerdedor(){
        return Nombre;

    }
}
