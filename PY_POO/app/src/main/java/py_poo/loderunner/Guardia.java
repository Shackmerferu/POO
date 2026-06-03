package py_poo.loderunner;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import py_poo.entities.Personaje;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Guardia extends Personaje {
    public static final int VELOCIDAD = 2;

    private IA_Guardia ia;
    private boolean enEscalera;
    private boolean enBarra;
    private boolean cayendo;
    private boolean cargandoOro;
    private boolean enAire;
    private int tileSize;
    private int spawnTileX;
    private int spawnTileY;

    private Animacion animCaminando;
    private Animacion animAtrapado;

    // RUTAS DE IMAGENES DEL GUARDIA - CAMBIAR AQUI
    private static final String RUTA_GUARDIA = "imagenes/Lode Runner/personaje (1).png";

    public Guardia(int tileX, int tileY, int tileSize) {
        this.spawnTileX = tileX;
        this.spawnTileY = tileY;
        this.tileSize = tileSize;
        this.ia = new IA_Guardia();
        this.vidas = 1;
        this.direccion = -1;
        this.cargandoOro = false;
        setX(tileX * tileSize);
        setY(tileY * tileSize);
        cargarAnimaciones();
    }

    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img = cr.cargarImagen(RUTA_GUARDIA);
        if (img != null) {
            Sprite s = new Sprite(img);
            animCaminando = new Animacion(List.of(s), 200);
            animAtrapado = new Animacion(List.of(s), 300);
        }
    }

    public void actualizar() {
        if (animCaminando != null) animCaminando.actualizar();
        if (animAtrapado != null) animAtrapado.actualizar();
    }

    public void moverIzquierda() {
        if (cayendo) return;
        direccion = -1;
        setX(getX() - VELOCIDAD);
    }

    public void moverDerecha() {
        if (cayendo) return;
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

    public void reaparecer() {
        setX(spawnTileX * tileSize + tileSize / 4);
        setY(spawnTileY * tileSize);
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        enAire = false;
        cargandoOro = false;
        ia.reaparecer();
    }

    public int getTileX() { return (int)((getX() + tileSize / 2) / tileSize); }
    public int getTileY() { return (int)((getY() + tileSize / 2) / tileSize); }

    public IA_Guardia getIA() { return ia; }
    public boolean isEnEscalera() { return enEscalera; }
    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
    public boolean isCargandoOro() { return cargandoOro; }
    public void setCargandoOro(boolean v) { this.cargandoOro = v; }
    public boolean isEnAire() { return enAire; }
    public void setEnAire(boolean v) { this.enAire = v; }

    @Override
    public void display(Graphics g) {
        Sprite s = (ia.getEstado() == IA_Guardia.Comportamiento.ATRAPADO && animAtrapado != null)
            ? animAtrapado.obtenerFrame()
            : (animCaminando != null ? animCaminando.obtenerFrame() : null);
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
