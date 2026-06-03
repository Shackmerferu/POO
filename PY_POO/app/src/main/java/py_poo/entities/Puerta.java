package py_poo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Puerta extends ObjetoGrafico {
    private Sprite spritePuerta;
    private int tileSize;
    private boolean visible;

    // RUTA DE IMAGEN DE LA PUERTA - CAMBIAR AQUI
    private static final String RUTA_PUERTA = "imagenes/Lode Runner/puerta.png";

    public Puerta(int tileX, int tileY, int tileSize) {
        this.tileSize = tileSize;
        this.visible = false;
        this.punto = new java.awt.Point(tileX * tileSize, tileY * tileSize);
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        cargarSprite();
    }

    private void cargarSprite() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img = cr.cargarImagen(RUTA_PUERTA);
        if (img != null) {
            spritePuerta = new Sprite(img);
        }
    }

    public void mostrar() { this.visible = true; }
    public void ocultar() { this.visible = false; }
    public boolean isVisible() { return visible; }

    @Override
    public void display(Graphics g) {
        if (!visible) return;
        if (spritePuerta != null) {
            spritePuerta.dibujar(g, punto.x, punto.y, tileSize, tileSize);
        } else if (sprite != null) {
            g.drawImage(sprite, punto.x, punto.y, tileSize, tileSize, null);
        }
    }
}
