package py_poo.engine;

import java.util.List;
import py_poo.entities.Entidad;

public abstract class Nivel {
    protected int numero;
    protected String mapa;
    protected List<Entidad> entidades;

    public void cargar() {
    }

    public void actualizar() {
    }

    public void renderizar() {
    }

    public void finalizarNivel() {
    }
}
