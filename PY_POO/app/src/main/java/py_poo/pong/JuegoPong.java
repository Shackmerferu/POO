package py_poo.pong;

import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.input.InputManager;

public  class JuegoPong extends VideoJuego {
    private static final int PUNTAJE_VICTORIA = 15;
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
       super.crearPartida();
    }

    @Override
    public String getGanador() {
        if (Puntuacion == null || Puntuacion.size() < 2) {
            return null;
        }

        int puntajeJ1 = Puntuacion.get(0);
        int puntajeJ2 = Puntuacion.get(1);

        if (puntajeJ1 >= PUNTAJE_VICTORIA && puntajeJ1 > puntajeJ2) {
            return "Jugador 1";
        }
        if (puntajeJ2 >= PUNTAJE_VICTORIA && puntajeJ2 > puntajeJ1) {
            return "Jugador 2";
        }

        return null;
    }

    @Override
    public String getPerdedor() {
        String ganador = getGanador();
        if (ganador == null) {
            return null;
        }
        return "Jugador 1".equals(ganador) ? "Jugador 2" : "Jugador 1";
    }
}
