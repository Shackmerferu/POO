package py_poo.entities;

public class Agujero extends Bloque {
    private static final int TIEMPO_CIERRE = 300;
    private int contador;
    private boolean abierto;

    public Agujero(int x, int y) {
        this.punto = new java.awt.Point(x, y);
        this.dimension = new java.awt.Dimension(40, 40);
        this.abierto = true;
        this.contador = 0;
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
