package py_poo.loderunner;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import py_poo.entities.Moneda;
import py_poo.entities.Personaje;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Recolector extends Personaje {
    public static final int VELOCIDAD = 3;
    public static final int VIDAS_INICIALES = 5;

    private int oroRecolectado;
    private int nivelOroTotal;
    private boolean enEscalera;
    private boolean enBarra;
    private boolean cayendo;
    private boolean cavandoIzq;
    private boolean cavandoDer;
    private boolean enAire;
    private int tileX, tileY;
    private int tileSize;

    private Animacion animParado;
    private Animacion animCaminando;
    private Animacion animEscalera;
    private Animacion animBarra;

    // RUTAS DE IMAGENES DEL RECOLECTOR - CAMBIAR AQUI
    private static final String RUTA_PERSONAJE_1 = "imagenes/Lode Runner/personaje (1).png";
    private static final String RUTA_PERSONAJE_2 = "imagenes/Lode Runner/personaje (2).png";

    public Recolector(int tileX, int tileY, int tileSize) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileSize = tileSize;
        this.vidas = VIDAS_INICIALES;
        this.direccion = 1;
        this.oroRecolectado = 0;
        this.cargarAnimaciones();
        setX(tileX * tileSize);
        setY(tileY * tileSize);
    }

    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img1 = cr.cargarImagen(RUTA_PERSONAJE_1);
        BufferedImage img2 = cr.cargarImagen(RUTA_PERSONAJE_2) != null ? cr.cargarImagen(RUTA_PERSONAJE_2) : img1;

        if (img1 != null) {
            Sprite s1 = new Sprite(img1);
            Sprite s2 = new Sprite(img2 != null ? img2 : img1);
            animParado = new Animacion(List.of(s1), 500);
            animCaminando = new Animacion(List.of(s1, s2), 150);
            animEscalera = new Animacion(List.of(s1), 200);
            animBarra = new Animacion(List.of(s1), 200);
        }
    }

    public void actualizar() {
        if (animParado != null) animParado.actualizar();
        if (animCaminando != null) animCaminando.actualizar();
        if (animEscalera != null) animEscalera.actualizar();
        if (animBarra != null) animBarra.actualizar();
    }

    public void moverIzquierda() {
        if (cayendo || cavandoIzq || cavandoDer) return;
        direccion = -1;
        setX(getX() - VELOCIDAD);
    }

    public void moverDerecha() {
        if (cayendo || cavandoIzq || cavandoDer) return;
        direccion = 1;
        setX(getX() + VELOCIDAD);
    }

    public void moverArriba() {
        if (!enEscalera || cayendo) return;
        setY(getY() - VELOCIDAD);
    }

    public void moverAbajo() {
        if (!enEscalera && !enAire || cayendo) return;
        setY(getY() + VELOCIDAD);
    }

    public void cavarIzquierda() {
        if (enEscalera || enBarra || cayendo) return;
        cavandoIzq = true;
    }

    public void cavarDerecha() {
        if (enEscalera || enBarra || cayendo) return;
        cavandoDer = true;
    }

    public void recogerOro() {
        oroRecolectado++;
    }

    public void reiniciarPosicion() {
        setX(tileX * tileSize);
        setY(tileY * tileSize);
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        cavandoIzq = false;
        cavandoDer = false;
        enAire = false;
    }

    public void perderVida() {
        vidas--;
        reiniciarPosicion();
    }

    public int getOroRecolectado() {
        return oroRecolectado;
    }

    public void setNivelOroTotal(int total) {
        this.nivelOroTotal = total;
    }

    public int getNivelOroTotal() {
        return nivelOroTotal;
    }

    public boolean nivelCompleto() {
        return oroRecolectado >= nivelOroTotal;
    }

    public boolean isEnEscalera() { return enEscalera; }
    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
    public boolean isCavandoIzq() { return cavandoIzq; }
    public void setCavandoIzq(boolean v) { this.cavandoIzq = v; }
    public boolean isCavandoDer() { return cavandoDer; }
    public void setCavandoDer(boolean v) { this.cavandoDer = v; }
    public boolean isEnAire() { return enAire; }
    public void setEnAire(boolean v) { this.enAire = v; }

    public int getTileX() { return (int)((getX() + tileSize / 2) / tileSize); }
    public int getTileY() { return (int)((getY() + tileSize / 2) / tileSize); }

    @Override
    public void display(Graphics g) {
        Sprite s = null;
        if (enEscalera) s = animEscalera != null ? animEscalera.obtenerFrame() : null;
        else if (enBarra) s = animBarra != null ? animBarra.obtenerFrame() : null;
        else if (Math.abs(getX() - (tileX * tileSize)) > 2 || cayendo) s = animCaminando != null ? animCaminando.obtenerFrame() : null;
        else s = animParado != null ? animParado.obtenerFrame() : null;

        if (s != null) {
            int x = (int)getX();
            int y = (int)getY();
            if (direccion < 0) {
                g.drawImage(s.getImagen(), x + tileSize, y, -tileSize, tileSize, null);
            } else {
                s.dibujar(g, x, y, tileSize, tileSize);
            }
        } else if (sprite != null) {
            g.drawImage(sprite, (int)getX(), (int)getY(), tileSize, tileSize, null);
        }
    }
}
