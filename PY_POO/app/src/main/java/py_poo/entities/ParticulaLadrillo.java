package py_poo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class ParticulaLadrillo extends ObjetoGrafico {
    private Animacion animacion;
    private int tileSize;
    private boolean activo;

    public ParticulaLadrillo(int x, int y, int tileSize) {
        this.tileSize = tileSize;
        this.activo = true;
        this.bounds = new java.awt.Rectangle(x, y, tileSize, tileSize);
        cargarAnimacion();
    }

    private void cargarAnimacion() {
        CargadorRecursos cr = new CargadorRecursos();
        List<Sprite> frames = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            BufferedImage img = cr.cargarImagen("imagenes/Lode Runner/particulas ladrillo (" + i + ").png");
            if (img != null) frames.add(new Sprite(img));
        }
        if (!frames.isEmpty()) {
            animacion = new Animacion(frames, 100);
            animacion.setRepitiendo(false);
        }
    }

    public void actualizar() {
        if (animacion != null) animacion.actualizar();
        if (animacion != null && animacion.termino()) {
            activo = false;
        }
    }

    public boolean isActivo() { return activo; }

    @Override
    public void display(Graphics g) {
        if (!activo || animacion == null) return;
        animacion.dibujar(g, bounds.x, bounds.y, tileSize, tileSize);
    }
}
