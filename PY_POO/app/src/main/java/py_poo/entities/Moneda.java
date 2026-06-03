package py_poo.entities;

import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Moneda extends Bloque {
    private boolean recolectada;
    private Animacion animacion;
    private int tileSize;

    // RUTAS DE IMAGENES DEL ORO - CAMBIAR AQUI
    private static final String RUTA_ORO_1 = "imagenes/Lode Runner/oro (1).png";
    private static final String RUTA_ORO_2 = "imagenes/Lode Runner/oro (2).png";
    private static final String RUTA_ORO_3 = "imagenes/Lode Runner/oro (3).png";

    public Moneda(int tileX, int tileY, int tileSize) {
        this.tileSize = tileSize;
        this.recolectada = false;
        this.valor = 100;
        this.punto = new java.awt.Point(tileX * tileSize, tileY * tileSize);
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        cargarAnimacion();
    }

    private void cargarAnimacion() {
        CargadorRecursos cr = new CargadorRecursos();
        List<Sprite> frames = new ArrayList<>();
        BufferedImage img1 = cr.cargarImagen(RUTA_ORO_1);
        BufferedImage img2 = cr.cargarImagen(RUTA_ORO_2);
        BufferedImage img3 = cr.cargarImagen(RUTA_ORO_3);
        if (img1 != null) frames.add(new Sprite(img1));
        if (img2 != null) frames.add(new Sprite(img2));
        if (img3 != null) frames.add(new Sprite(img3));
        if (!frames.isEmpty()) {
            animacion = new Animacion(frames, 250);
        }
    }

    public void recolectar() {
        this.recolectada = true;
    }

    public boolean isRecolectada() {
        return recolectada;
    }

    @Override
    public void recoger() {
        recolectar();
    }

    public void actualizar() {
        if (animacion != null) {
            animacion.actualizar();
        }
    }

    @Override
    public void display(Graphics g) {
        if (recolectada) return;
        if (animacion != null) {
            animacion.dibujar(g, punto.x, punto.y, tileSize, tileSize);
        } else if (sprite != null) {
            g.drawImage(sprite, punto.x, punto.y, tileSize, tileSize, null);
        }
    }
}
