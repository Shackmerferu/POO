package py_poo.engine;

import java.util.List;
import py_poo.entities.Entidad;

public abstract class Juego {
    protected String nombre;
    protected boolean activo;
    protected int puntuacion;
    protected Nivel nivelActual;
    protected List<Entidad> entidades;

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
