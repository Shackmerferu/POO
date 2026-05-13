package py_poo.engine;

import java.util.List;
import py_poo.entities.Entidad;

public abstract class Juego {
    protected String nombre;
    protected boolean activo;
    protected int puntuacion;
    protected Nivel nivelActual;
    protected List<Entidad> entidades;
    private int ResX;
    private int ResY;
    protected boolean Fullscreen;
    private Juego juego;
    private List<Jugador> jugador;
    public void iniciar() {
    }

    public void actualizar() {
    }

    public void renderizar() {
    }

    public void finalizar() {
    }

    public void reiniciar() {
    }

    public void cargarNivel() {
    }
}
