package py_poo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Escalera extends Bloque {
    private Animacion animacion;
    private int tileSize;

    // RUTAS DE IMAGENES DE LA ESCALERA - CAMBIAR AQUI
    private static final String RUTA_ESCALERA_1 = "imagenes/Lode Runner/escalera (1).png";
    private static final String RUTA_ESCALERA_2 = "imagenes/Lode Runner/escalera (2).png";
    private static final String RUTA_ESCALERA_3 = "imagenes/Lode Runner/escalera (3).png";

    public Escalera(int tileX, int tileY, int tileSize) {
        this.tileSize = tileSize;
        this.punto = new java.awt.Point(tileX * tileSize, tileY * tileSize);
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        cargarAnimacion();
    }

    private void cargarAnimacion() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img = cr.cargarImagen(RUTA_ESCALERA_1);
        if (img != null) {
            List<Sprite> frames = new ArrayList<>();
            frames.add(new Sprite(img));
            animacion = new Animacion(frames, 100);
        }
    }

    @Override
    public void display(Graphics g) {
        if (animacion != null) {
            animacion.dibujar(g, punto.x, punto.y, tileSize, tileSize);
        } else if (sprite != null) {
            g.drawImage(sprite, punto.x, punto.y, tileSize, tileSize, null);
        }
    }
}
