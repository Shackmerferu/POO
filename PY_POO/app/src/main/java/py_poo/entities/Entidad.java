package py_poo.entities;

import java.awt.Graphics;

public abstract class Entidad {
    protected float x;
    protected float y;
    protected int ancho;
    protected int alto;
    protected float velocidad;
    protected boolean activa;

    public void actualizar() {
    }

    public void renderizar(Graphics g) {
    }

    public void destruir() {
    }
}
