package py_poo.loderunner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import py_poo.entities.Moneda;
import py_poo.entities.Personaje;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

public class Guardia extends Personaje {
    public static final double VELOCIDAD = 1.7;

    private IA_Guardia ia;
    private Recolector heroe;
    private Nivel nivel;
    private boolean enAgujero;
    private boolean enEscalera;
    private boolean enBarra;
    private boolean cayendo;
    private Moneda monedaCargada;
    private boolean enAire;
    private int tileSize;
    private int spawnTileX;
    private int spawnTileY;

    private Animacion animCaminando;
    private Animacion animAtrapado;

    // RUTAS DE IMAGENES DEL GUARDIA - CAMBIAR AQUI
    private static final String RUTA_GUARDIA = "imagenes/Lode Runner/personaje (2).png";

    public Guardia(int tileX, int tileY, int tileSize) {
        this.spawnTileX = tileX;
        this.spawnTileY = tileY;
        this.tileSize = tileSize;
        this.ia = new IA_Guardia();
        this.vidas = 1;
        this.direccion = -1;
        this.monedaCargada = null;
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
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
        if (cayendo || nivel == null) return;
        double newX = getX() - VELOCIDAD;
        int tx = (int)newX / tileSize;
        int ty = enEscalera ? (int)(getY() + tileSize / 2) / tileSize
                : (int)(getY() + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, ty)) return;
        direccion = -1;
        setX(newX);
    }

    public void moverDerecha() {
        if (cayendo || nivel == null) return;
        double newX = getX() + VELOCIDAD;
        int tx = (int)(newX + tileSize - 1) / tileSize;
        int ty = enEscalera ? (int)(getY() + tileSize / 2) / tileSize
                : (int)(getY() + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, ty)) return;
        direccion = 1;
        setX(newX);
    }

    public void moverArriba() {
        if (!enEscalera || cayendo || nivel == null) return;
        double nuevaY = getY() - VELOCIDAD;
        int tx = getTileX();
        if (nivel.esSolido(tx, (int)nuevaY / tileSize)) return;
        int tyPies = (int)(nuevaY + tileSize - 1) / tileSize;
        if (nivel.esEscalera(tx, tyPies) || nivel.esBarra(tx, tyPies)
            || nivel.esSolido(tx, tyPies)) {
            setY(nuevaY);
        } else if (nivel.esEscalera(tx, tyPies + 1) || nivel.esBarra(tx, tyPies + 1)) {
            setY(tyPies * tileSize);
        }
    }

    public void moverAbajo() {
        if (cayendo || nivel == null) return;
        int tx = getTileX();
        int tyPies2 = (int)(getY() + tileSize) / tileSize;
        if (!enEscalera) {
            if (nivel.esEscalera(tx, tyPies2)) {
                enEscalera = true;
                setY(tyPies2 * tileSize - tileSize);
            }
            return;
        }
        double nuevaY = getY() + VELOCIDAD;
        int tyPies = (int)(nuevaY + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, tyPies)) return;
        if (nivel.esEscalera(tx, tyPies) || nivel.esBarra(tx, tyPies)) {
            setY(nuevaY);
        }
    }

    public void reaparecer() {
        setX(spawnTileX * tileSize + tileSize / 4);
        setY(spawnTileY * tileSize);
        enAgujero = false;
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        enAire = false;
        if (monedaCargada != null) {
            monedaCargada = null;
        }
        ia.reaparecer();
    }

    public int getTileX() { return (int)((getX() + tileSize / 2) / tileSize); }
    public int getTileY() { return (int)((getY() + tileSize / 2) / tileSize); }

    public IA_Guardia getIA() { return ia; }
    public boolean isEnEscalera() { return enEscalera; }
    public void setHeroe(Recolector heroe) { this.heroe = heroe; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    @Override
    public void mover() {
        if (enAgujero) return;
        if (heroe == null || nivel == null) return;

        aplicarGravedad();

        if (!cayendo) {
            int hx = (int)heroe.getX();
            int hy = (int)heroe.getY();
            int gx = (int)getX();
            int gy = (int)getY();
            int tx = getTileX();
            int ty = getTileY();

            boolean puedeIzq = !nivel.esSolido(tx - 1, ty);
            boolean puedeDer = !nivel.esSolido(tx + 1, ty);
            boolean puedeSubir = nivel.esEscalera(tx, ty - 1);
            boolean puedeBajar = nivel.esEscalera(tx, ty + 1);

            int dir = ia.calcularMovimiento(gx, gy, hx, hy, puedeIzq, puedeDer, puedeSubir, puedeBajar, enEscalera, enBarra);

            if (dir == -1) moverIzquierda();
            else if (dir == 1) moverDerecha();
            else if (dir == -2) moverArriba();
            else if (dir == 2) moverAbajo();
        }

        detectarPlataforma();
        actualizar();
    }

    private boolean tieneSoporte(int tx, int ty) {
        return nivel.esSolido(tx, ty) || nivel.esEscalera(tx, ty) || nivel.esBarra(tx, ty);
    }

    private void aplicarGravedad() {
        if (nivel == null) return;
        int txL = (int)getX() / tileSize;
        int txR = (int)(getX() + tileSize - 1) / tileSize;
        int tyPies = (int)(getY() + tileSize) / tileSize;

        if (cayendo) {
            setY(getY() + VELOCIDAD);
            if (tieneSoporte(txL, tyPies) || tieneSoporte(txR, tyPies)) {
                setY(tyPies * tileSize - tileSize);
                cayendo = false;
            }
        } else if (!enEscalera && !enBarra
            && !tieneSoporte(txL, tyPies)
            && !tieneSoporte(txR, tyPies)) {
            cayendo = true;
        }
    }

    private void detectarPlataforma() {
        if (nivel == null) return;
        int txL = (int)getX() / tileSize;
        int txR = (int)(getX() + tileSize - 1) / tileSize;
        int tyPies = (int)(getY() + tileSize - 1) / tileSize;
        int tyPies2 = (int)(getY() + tileSize) / tileSize;
        enEscalera = nivel.esEscalera(txL, tyPies) || nivel.esEscalera(txR, tyPies);
        enBarra = nivel.esBarra(txL, tyPies) || nivel.esBarra(txR, tyPies);
        if (!enEscalera && !enBarra
            && (nivel.esEscalera(txL, tyPies2) || nivel.esEscalera(txR, tyPies2))) {
            enEscalera = true;
        }
        enAire = !cayendo && !enEscalera && !enBarra
            && !tieneSoporte(txL, tyPies2)
            && !tieneSoporte(txR, tyPies2);
    }

    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
    public boolean isCargandoOro() { return monedaCargada != null; }
    public Moneda getMonedaCargada() { return monedaCargada; }
    public void setMonedaCargada(Moneda m) { this.monedaCargada = m; }
    public boolean isEnAire() { return enAire; }
    public void setEnAire(boolean v) { this.enAire = v; }
    public boolean enAgujero() { return enAgujero; }
    public void enAgujero(boolean v) { this.enAgujero = v; }

    @Override
    public void display(Graphics g) {
        Sprite s = (ia.getEstado() == IA_Guardia.Comportamiento.ATRAPADO && animAtrapado != null)
            ? animAtrapado.obtenerFrame()
            : (animCaminando != null ? animCaminando.obtenerFrame() : null);
        if (s != null) {
            int x = (int)getX();
            int y = (int)getY();
            if (direccion < 0) {
                s.dibujar(g, x, y, tileSize, tileSize);
            } else {
                g.drawImage(s.getImagen(), x + tileSize, y, -tileSize, tileSize, null);
            }
            if (monedaCargada != null) {
                g.setColor(Color.YELLOW);
                g.fillOval(x + tileSize / 4, y - 8, tileSize / 2, 8);
            }
        } else if (sprite != null) {
            g.drawImage(sprite, (int)getX(), (int)getY(), tileSize, tileSize, null);
        }
    }
}
