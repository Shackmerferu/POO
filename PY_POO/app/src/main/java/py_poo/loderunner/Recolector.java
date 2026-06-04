package py_poo.loderunner;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import py_poo.entities.Personaje;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.input.InputManager;
import py_poo.utils.CargadorRecursos;

public class Recolector extends Personaje {
    public static final int VELOCIDAD = 2;
    public static final int VIDAS_INICIALES = 3;

    private int oroRecolectado;
    private int nivelOroTotal;
    private boolean enEscalera;
    private boolean enBarra;
    private boolean cayendo;
    private boolean cavoEsteFrame;
    private boolean enAire;
    private int tileX, tileY;
    private int tileSize;
    private InputManager input;
    private Nivel nivel;

    private Animacion animParado;
    private Animacion animCaminando;
    private Animacion animEscalera;
    private Animacion animBarra;

    // RUTAS DE IMAGENES DEL RECOLECTOR - CAMBIAR AQUI
    private static final String RUTA_PERSONAJE_1 = "imagenes/Lode Runner/personaje (1).png";

    public Recolector(int tileX, int tileY, int tileSize) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileSize = tileSize;
        this.vidas = VIDAS_INICIALES;
        this.direccion = 1;
        this.oroRecolectado = 0;
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        this.cargarAnimaciones();
        setX(tileX * tileSize);
        setY(tileY * tileSize);
    }

    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img1 = cr.cargarImagen(RUTA_PERSONAJE_1);


        if (img1 != null) {
            Sprite s1 = new Sprite(img1);
            animParado = new Animacion(List.of(s1), 500);
            animCaminando = new Animacion(List.of(s1), 150);
            animEscalera = new Animacion(List.of(s1), 200);
            animBarra = new Animacion(List.of(s1), 200);
        }
    }

    public void setInputManager(InputManager input) {
        this.input = input;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    @Override
    public void mover() {
        if (input == null) return;
        cavoEsteFrame = false;
        if (!cayendo) {
            if (input.isLeftPressed()) moverIzquierda();
            if (input.isRightPressed()) moverDerecha();
            if (input.isUpPressed()) moverArriba();
            if (input.isDownPressed()) moverAbajo();
            if (input.isDigPressed()) {
                if (direccion < 0) cavarIzquierda();
                else cavarDerecha();
            }
        }
        aplicarGravedad();
        detectarPlataforma();
        actualizar();
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

    private boolean tieneSoporte(int tx, int ty) {
        return nivel.esSolido(tx, ty) || nivel.esEscalera(tx, ty) || nivel.esBarra(tx, ty);
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

    public void actualizar() {
        if (animParado != null) animParado.actualizar();
        if (animCaminando != null) animCaminando.actualizar();
        if (animEscalera != null) animEscalera.actualizar();
        if (animBarra != null) animBarra.actualizar();
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
        int tx = getTileX();
        double nuevaY = getY() - VELOCIDAD;
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

    public void cavarIzquierda() {
        if (enEscalera || enBarra || cayendo || nivel == null) return;
        int tyPies = (int)(getY() + tileSize) / tileSize;
        if (nivel.cavarEn(getTileX() - 1, tyPies)) {
            cavoEsteFrame = true;
        }
    }

    public void cavarDerecha() {
        if (enEscalera || enBarra || cayendo || nivel == null) return;
        int tyPies = (int)(getY() + tileSize) / tileSize;
        if (nivel.cavarEn(getTileX() + 1, tyPies)) {
            cavoEsteFrame = true;
        }
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

    public boolean cavoEsteFrame() { return cavoEsteFrame; }
    public boolean isEnEscalera() { return enEscalera; }
    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
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
                s.dibujar(g, x, y, tileSize, tileSize);
            } else {
                g.drawImage(s.getImagen(), x + tileSize, y, -tileSize, tileSize, null);
            }
        } else if (sprite != null) {
            g.drawImage(sprite, (int)getX(), (int)getY(), tileSize, tileSize, null);
        }
    }
}
