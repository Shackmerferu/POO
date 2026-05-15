package py_poo.engine;

import java.util.List;
import py_poo.entities.Entidad;

public abstract class VideoJuego {
    protected String Nombre;
    protected boolean Activo;
    protected int Puntuacion;
    protected Nivel NivelActual;
    protected List<Entidad> Entidades;
    private int ResX;
    private int ResY;
    protected boolean Fullscreen;
    private VideoJuego Juego;
    private List<Jugador> Jugador;
    private String Resultado;

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
