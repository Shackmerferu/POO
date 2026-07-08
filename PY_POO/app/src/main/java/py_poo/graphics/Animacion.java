package py_poo.graphics;

import java.awt.Graphics;
import java.util.List;

public class Animacion {
    private List<Sprite> frames;
    private int frameActual;
    private long tiempoPorFrame;
    private long ultimoTiempo;
    private boolean repitiendo;

    public Animacion(List<Sprite> frames, long tiempoPorFrameMs) {
        this.frames = frames;
        this.tiempoPorFrame = tiempoPorFrameMs;
        this.frameActual = 0;
        this.ultimoTiempo = System.currentTimeMillis();
        this.repitiendo = true;
    }

    public void actualizar() {
        if (frames == null || frames.isEmpty()) return;
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoTiempo >= tiempoPorFrame) {
            ultimoTiempo = ahora;
            frameActual++;
            if (frameActual >= frames.size()) {
                if (repitiendo) {
                    frameActual = 0;
                } else {
                    frameActual = frames.size() - 1;
                }
            }
        }
    }

    public Sprite obtenerFrame() {
        if (frames == null || frames.isEmpty()) return null;
        return frames.get(frameActual);
    }

    public void reiniciar() {
        frameActual = 0;
        ultimoTiempo = System.currentTimeMillis();
    }

    public void setRepitiendo(boolean repitiendo) {
        this.repitiendo = repitiendo;
    }

    public boolean termino() {
        return !repitiendo && frameActual >= frames.size() - 1;
    }

    public void dibujar(Graphics g, int x, int y) {
        Sprite s = obtenerFrame();
        if (s != null) {
            s.dibujar(g, x, y);
        }
    }

    public void dibujar(Graphics g, int x, int y, int ancho, int alto) {
        Sprite s = obtenerFrame();
        if (s != null) {
            s.dibujar(g, x, y, ancho, alto);
        }
    }
}
