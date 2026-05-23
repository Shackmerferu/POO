package py_poo.loderunner;

import java.util.List;

import py_poo.entities.ObjetoGrafico;

public abstract class Nivel {
    protected int Numero;
    protected String[] Mapa;
    protected List<ObjetoGrafico> Entidad;

    public void cargar() {
    }

    public void actualizar() {
    }

    public void renderizar() {
    }

    public void finalizarNivel() {
    }
}
