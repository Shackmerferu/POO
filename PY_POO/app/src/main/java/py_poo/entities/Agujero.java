package py_poo.entities;

import java.awt.Color;
import java.awt.Graphics;

// Representa un agujero en el suelo creado al cavar un ladrillo.
// Permanece abierto un tiempo determinado y luego se cierra automáticamente,
// regenerando el ladrillo asociado.
public class Agujero extends Bloque {
    private static final int TIEMPO_CIERRE = 300; // frames que tarda en cerrarse (~5s a 60fps)
    private int contador; // frames transcurridos desde que se abrió
    private boolean abierto; // true mientras el agujero está abierto
    private Ladrillo ladrilloAsociado; // ladrillo que fue cavado para crear este agujero

    // Constructor simple con coordenadas (sin ladrillo asociado)
    public Agujero(int x, int y) {
        this(x, y, null);
    }

    // Constructor con posición y ladrillo asociado (para regeneración al cerrarse)
    public Agujero(int x, int y, Ladrillo asociado) {
        this.punto = new java.awt.Point(x, y);
        this.dimension = new java.awt.Dimension(40, 40);
        this.abierto = true;
        this.contador = 0;
        this.ladrilloAsociado = asociado;
    }

    // Retorna el ladrillo que fue destruido para crear este agujero
    public Ladrillo getLadrilloAsociado() { return ladrilloAsociado; }

    @Override
    // Dibuja el agujero como un rectángulo semitransparente negro
    public void display(Graphics g) {
        if (!abierto) return;
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(punto.x, punto.y, dimension.width, dimension.height);
    }

    // Avanza el contador del agujero un frame; si llega al límite, lo cierra
    public void actualizar() {
        if (abierto) {
            contador++;
            if (contador >= TIEMPO_CIERRE) {
                cerrar();
            }
        }
    }

    // Abre el agujero (reinicia contador)
    public void abrir() {
        this.abierto = true;
        this.contador = 0;
    }

    // Cierra el agujero (marca como no abierto, el ladrillo se regenerará después)
    public void cerrar() {
        this.abierto = false;
    }

    // True si el agujero está actualmente abierto
    public boolean isAbierto() {
        return abierto;
    }

    // Retorna los frames restantes antes de que el agujero se cierre
    public int getTiempoRestante() {
        return TIEMPO_CIERRE - contador;
    }

    // Retorna el progreso de cierre como valor entre 0.0 y 1.0
    public float getProgreso() {
        return Math.min(1f, (float)contador / TIEMPO_CIERRE);
    }
}
