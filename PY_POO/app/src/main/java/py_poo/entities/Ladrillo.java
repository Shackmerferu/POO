package py_poo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Ladrillo extends Bloque {
    public enum Estado { NORMAL, BREAKING, ROTO, REGENERATING }

    private Animacion animNormal;
    private Animacion animBreaking;
    private Animacion animRegen;
    private int tileSize;
    private boolean irrompible;
    private Estado estado;

    public Ladrillo(int x, int y, int tileSize, boolean irrompible) {
        this.tileSize = tileSize;
        this.irrompible = irrompible;
        this.estado = Estado.NORMAL;
        this.destruible = !irrompible;
        this.bounds = new java.awt.Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
        cargarAnimaciones();
    }

    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();

        if (irrompible) {
            BufferedImage img = cr.cargarImagen("imagenes/Lode Runner/ladrillo solido.png");
            if (img != null) {
                List<Sprite> frames = new ArrayList<>();
                frames.add(new Sprite(img));
                animNormal = new Animacion(frames, 500);
            }
        } else {
            BufferedImage img1 = cr.cargarImagen("imagenes/Lode Runner/ladrillo (1).png");
            if (img1 != null) {
                List<Sprite> frames = new ArrayList<>();
                frames.add(new Sprite(img1));
                animNormal = new Animacion(frames, 500);
            }

            List<Sprite> breakFrames = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                BufferedImage img = cr.cargarImagen("imagenes/Lode Runner/ladrillo2 (" + i + ").png");
                if (img != null) breakFrames.add(new Sprite(img));
            }
            if (!breakFrames.isEmpty()) {
                animBreaking = new Animacion(breakFrames, 100);
                animBreaking.setRepitiendo(false);
            }

            List<Sprite> regenFrames = new ArrayList<>();
            for (int i = 6; i >= 1; i--) {
                BufferedImage img = cr.cargarImagen("imagenes/Lode Runner/ladrillo (" + i + ").png");
                if (img != null) regenFrames.add(new Sprite(img));
            }
            if (!regenFrames.isEmpty()) {
                animRegen = new Animacion(regenFrames, 100);
                animRegen.setRepitiendo(false);
            }
        }
    }

    public void iniciarBreaking() {
        if (irrompible) return;
        estado = Estado.BREAKING;
        if (animBreaking != null) animBreaking.reiniciar();
    }

    public void iniciarRegen() {
        estado = Estado.REGENERATING;
        if (animRegen != null) animRegen.reiniciar();
    }

    public Estado getEstado() { return estado; }

    public boolean isRoto() { return estado == Estado.ROTO || estado == Estado.REGENERATING; }
    public boolean isIrrompible() { return irrompible; }

    public void actualizar() {
        if (animBreaking != null) animBreaking.actualizar();
        if (animRegen != null) animRegen.actualizar();

        if (estado == Estado.BREAKING && animBreaking != null && animBreaking.termino()) {
            estado = Estado.ROTO;
        }
        if (estado == Estado.REGENERATING && animRegen != null && animRegen.termino()) {
            estado = Estado.NORMAL;
        }
    }

    @Override
    public void display(Graphics g) {
        switch (estado) {
            case NORMAL:
                if (animNormal != null) animNormal.dibujar(g, bounds.x, bounds.y, tileSize, tileSize);
                break;
            case BREAKING:
                if (animBreaking != null) animBreaking.dibujar(g, bounds.x, bounds.y, tileSize, tileSize);
                break;
            case REGENERATING:
                if (animRegen != null) animRegen.dibujar(g, bounds.x, bounds.y, tileSize, tileSize);
                break;
            case ROTO:
                break;
        }
    }
}
