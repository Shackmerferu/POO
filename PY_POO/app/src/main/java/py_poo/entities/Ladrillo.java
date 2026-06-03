package py_poo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Ladrillo extends Bloque {
    private Animacion animacion;
    private int tileSize;
    private boolean roto;
    private boolean irrompible;

    // RUTAS DE IMAGENES DEL LADRILLO - CAMBIAR AQUI
    private static final String RUTA_LADRILLO = "imagenes/Lode Runner/ladrillo (1).png";
    private static final String RUTA_LADRILLO_SOLIDO = "imagenes/Lode Runner/ladrillo solido.png";

    public Ladrillo(int x, int y, int tileSize, boolean irrompible) {
        this.tileSize = tileSize;
        this.irrompible = irrompible;
        this.roto = false;
        this.destruible = !irrompible;
        this.punto = new java.awt.Point(x * tileSize, y * tileSize);
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        cargarAnimacion();
    }

    private void cargarAnimacion() {
        CargadorRecursos cr = new CargadorRecursos();
        if (irrompible) {
            BufferedImage img = cr.cargarImagen(RUTA_LADRILLO_SOLIDO);
            if (img != null) {
                List<Sprite> frames = new ArrayList<>();
                frames.add(new Sprite(img));
                animacion = new Animacion(frames, 500);
            }
        } else {
            List<Sprite> frames = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                BufferedImage img = cr.cargarImagen("imagenes/Lode Runner/ladrillo (" + i + ").png");
                if (img != null) frames.add(new Sprite(img));
            }
            if (!frames.isEmpty()) {
                animacion = new Animacion(frames, 150);
            }
        }
    }

    public void romper() {
        if (!irrompible) {
            this.roto = true;
        }
    }

    public boolean isRoto() {
        return roto;
    }

    public boolean isIrrompible() {
        return irrompible;
    }

    public void actualizar() {
        if (animacion != null) {
            animacion.actualizar();
        }
    }

    @Override
    public void display(Graphics g) {
        if (roto) return;
        if (animacion != null) {
            animacion.dibujar(g, punto.x, punto.y, tileSize, tileSize);
        } else if (sprite != null) {
            g.drawImage(sprite, punto.x, punto.y, tileSize, tileSize, null);
        }
    }
}
