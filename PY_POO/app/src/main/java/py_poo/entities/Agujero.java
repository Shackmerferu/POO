package py_poo.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Agujero extends Bloque {
    private static final int TIEMPO_CIERRE = 180;
    private int contador;
    private boolean abierto;
    private Ladrillo ladrilloAsociado;

    public Agujero(int x, int y) {
        this(x, y, null);
    }

    public Agujero(int x, int y, Ladrillo asociado) {
        this.punto = new java.awt.Point(x, y);
        this.dimension = new java.awt.Dimension(40, 40);
        this.abierto = true;
        this.contador = 0;
        this.ladrilloAsociado = asociado;
    }

    public Ladrillo getLadrilloAsociado() { return ladrilloAsociado; }

    @Override
    public void display(Graphics g) {
        if (!abierto) return;
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(punto.x, punto.y, dimension.width, dimension.height);
    }

    public void actualizar() {
        if (abierto) {
            contador++;
            if (contador >= TIEMPO_CIERRE) {
                cerrar();
            }
        }
    }

    public void abrir() {
        this.abierto = true;
        this.contador = 0;
    }

    public void cerrar() {
        this.abierto = false;
    }

    public boolean isAbierto() {
        return abierto;
    }

    public int getTiempoRestante() {
        return TIEMPO_CIERRE - contador;
    }

    public float getProgreso() {
        return Math.min(1f, (float)contador / TIEMPO_CIERRE);
    }
}
