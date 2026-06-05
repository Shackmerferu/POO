package py_poo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Barra extends Bloque {
    private Sprite spriteBarra;
    private int tileSize;

    // RUTAS DE IMAGENES DE LA BARRA - CAMBIAR AQUI
    private static final String RUTA_BARRA = "imagenes/Lode Runner/barra.png";
    private static final String RUTA_BARRA_AFUERA = "imagenes/Lode Runner/barra afuera.png";

    public Barra(int tileX, int tileY, int tileSize) {
        this.tileSize = tileSize;
        this.punto = new java.awt.Point(tileX * tileSize, tileY * tileSize);
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        cargarSprite();
    }

    private void cargarSprite() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img = cr.cargarImagen(RUTA_BARRA);
        if (img != null) {
            spriteBarra = new Sprite(img);
        }
    }

    public void deslizar() {}
    public void colgar() {}

    @Override
    public void display(Graphics g) {
        if (spriteBarra != null) {
            spriteBarra.dibujar(g, punto.x, punto.y, tileSize, tileSize);
        } else if (sprite != null) {
            g.drawImage(sprite, punto.x, punto.y, tileSize, tileSize, null);
        }
    }
}
