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
    public static final int VELOCIDAD = 2; // velocidad de movimiento del recolector
    public static final int VIDAS_INICIALES = 3; // vidas con las que empieza

    private int oroRecolectado; // oro recolectado en el nivel actual
    private int nivelOroTotal; // total de oro disponible en el nivel
    private boolean enEscalera; // indica si está sobre una escalera
    private boolean enBarra; // indica si está sobre una barra
    private boolean cayendo; // indica si está cayendo
    private boolean cavoEsteFrame; // true si cavó en este frame
    private boolean enAire; // indica si está en el aire sin soporte
    private int tileX, tileY; // tile de spawn del recolector
    private int tileSize; // tamaño en píxeles de cada tile
    private InputManager input; // gestor de entrada del jugador
    private Nivel nivel; // nivel actual

    private Animacion animParado; // animación quieto
    private Animacion animCaminando; // animación caminando
    private Animacion animEscalera; // animación en escalera
    private Animacion animBarra; // animación en barra

    // RUTAS DE IMAGENES DEL RECOLECTOR - CAMBIAR AQUI
    private static final String RUTA_PERSONAJE_1 = "imagenes/Lode Runner/personaje (1).png";

    // constructor: crea recolector en tile y tamaño dados
    public Recolector(int tileX, int tileY, int tileSize) {
        this.tileX = tileX; // guarda tile de spawn
        this.tileY = tileY;
        this.tileSize = tileSize;
        this.vidas = VIDAS_INICIALES; // asigna vidas iniciales
        this.direccion = 1; // mira a derecha por defecto
        this.oroRecolectado = 0; // empieza sin oro
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        this.cargarAnimaciones(); // carga sprites
        setX(tileX * tileSize); // posición en píxeles
        setY(tileY * tileSize);
    }

    // carga las imágenes de animación del recolector
    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img1 = cr.cargarImagen(RUTA_PERSONAJE_1);


        if (img1 != null) {
            Sprite s1 = new Sprite(img1);
            animParado = new Animacion(List.of(s1), 500); // animación quieto
            animCaminando = new Animacion(List.of(s1), 150); // animación caminando
            animEscalera = new Animacion(List.of(s1), 200); // animación escalera
            animBarra = new Animacion(List.of(s1), 200); // animación barra
        }
    }

    // asigna el gestor de entrada para controlar el personaje
    public void setInputManager(InputManager input) {
        this.input = input;
    }

    // asigna el nivel en el que está el recolector
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    @Override
    // procesa la entrada del jugador y mueve al recolector
    public void mover() {
        if (input == null) return;
        if (estaEnAgujero()) return; // no se mueve si está en agujero
        cavoEsteFrame = false; // reinicia flag de cavado
        if (!cayendo) {
            if (input.isLeftPressed()) moverIzquierda(); // mueve izquierda
            if (input.isRightPressed()) moverDerecha(); // mueve derecha
            if (input.isUpPressed()) moverArriba(); // mueve arriba
            if (input.isDownPressed()) moverAbajo(); // mueve abajo
            if (input.isDigPressed()) {
                if (direccion < 0) cavarIzquierda(); // cava a izquierda
                else cavarDerecha(); // cava a derecha
            }
        }
        aplicarGravedad(); // aplica caída
        detectarPlataforma(); // detecta escaleras/barras
        actualizar(); // actualiza animaciones
    }

    // aplica gravedad al recolector si no hay soporte
    private void aplicarGravedad() {
        if (nivel == null) return;
        int txL = (int)getX() / tileSize; // tile izquierdo de pies
        int txR = (int)(getX() + tileSize - 1) / tileSize; // tile derecho de pies
        int tyPies = (int)(getY() + tileSize) / tileSize;

        if (cayendo) {
            setY(getY() + VELOCIDAD); // cae
            if (tieneSoporte(txL, tyPies) || tieneSoporte(txR, tyPies)) {
                setY(tyPies * tileSize - tileSize); // aterriza
                cayendo = false;
            }
        } else if (!enEscalera && !enBarra
            && !tieneSoporte(txL, tyPies)
            && !tieneSoporte(txR, tyPies)) {
            cayendo = true; // empieza caída
        }
    }

    // verifica si hay soporte en el tile dado
    private boolean tieneSoporte(int tx, int ty) {
        return nivel.esSolido(tx, ty) || nivel.esEscalera(tx, ty) || nivel.esBarra(tx, ty);
    }

    // detecta escaleras, barras y estado de aire bajo el personaje
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

    // actualiza las animaciones cada frame
    public void actualizar() {
        if (animParado != null) animParado.actualizar();
        if (animCaminando != null) animCaminando.actualizar();
        if (animEscalera != null) animEscalera.actualizar();
        if (animBarra != null) animBarra.actualizar();
    }

    // mueve al recolector a la izquierda
    public void moverIzquierda() {
        if (cayendo || nivel == null) return;
        double newX = getX() - VELOCIDAD;
        int tx = (int)newX / tileSize;
        int ty = enEscalera ? (int)(getY() + tileSize / 2) / tileSize
                : (int)(getY() + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, ty)) return; // bloqueado
        direccion = -1; // mira a izquierda
        setX(newX);
    }

    // mueve al recolector a la derecha
    public void moverDerecha() {
        if (cayendo || nivel == null) return;
        double newX = getX() + VELOCIDAD;
        int tx = (int)(newX + tileSize - 1) / tileSize;
        int ty = enEscalera ? (int)(getY() + tileSize / 2) / tileSize
                : (int)(getY() + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, ty)) return; // bloqueado
        direccion = 1; // mira a derecha
        setX(newX);
    }

    // mueve al recolector hacia arriba por escalera/barra
    public void moverArriba() {
        if (!enEscalera || cayendo || nivel == null) return;
        int tx = getTileX();
        double nuevaY = getY() - VELOCIDAD;
        if (nivel.esSolido(tx, (int)nuevaY / tileSize)) return; // bloqueado arriba
        int tyPies = (int)(nuevaY + tileSize - 1) / tileSize;
        if (nivel.esEscalera(tx, tyPies) || nivel.esBarra(tx, tyPies)
            || nivel.esSolido(tx, tyPies)) {
            setY(nuevaY);
        } else if (nivel.esEscalera(tx, tyPies + 1) || nivel.esBarra(tx, tyPies + 1)) {
            setY(tyPies * tileSize); // ajusta al escalón
        }
    }

    // mueve al recolector hacia abajo por escalera
    public void moverAbajo() {
        if (cayendo || nivel == null) return;
        int tx = getTileX();
        int tyPies2 = (int)(getY() + tileSize) / tileSize;
        if (!enEscalera) {
            if (nivel.esEscalera(tx, tyPies2)) {
                enEscalera = true; // se agarra a escalera
                setY(tyPies2 * tileSize - tileSize);
            }
            return;
        }
        double nuevaY = getY() + VELOCIDAD;
        int tyPies = (int)(nuevaY + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, tyPies)) return; // bloqueado abajo
        if (nivel.esEscalera(tx, tyPies) || nivel.esBarra(tx, tyPies)) {
            setY(nuevaY);
        }
    }

    // cava un ladrillo a la izquierda del personaje
    public void cavarIzquierda() {
        if (enEscalera || enBarra || cayendo || nivel == null) return;
        int tyPies = (int)(getY() + tileSize) / tileSize;
        if (nivel.cavarEn(getTileX() - 1, tyPies)) {
            cavoEsteFrame = true; // marcó que cavó este frame
        }
    }

    // cava un ladrillo a la derecha del personaje
    public void cavarDerecha() {
        if (enEscalera || enBarra || cayendo || nivel == null) return;
        int tyPies = (int)(getY() + tileSize) / tileSize;
        if (nivel.cavarEn(getTileX() + 1, tyPies)) {
            cavoEsteFrame = true; // marcó que cavó este frame
        }
    }

    // incrementa el contador de oro recolectado
    public void recogerOro() {
        oroRecolectado++;
    }

    // reinicia posición del recolector al spawn
    public void reiniciarPosicion() {
        setX(tileX * tileSize);
        setY(tileY * tileSize);
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        enAire = false;
    }

    // reduce una vida y reinicia posición
    public void perderVida() {
        vidas--;
        reiniciarPosicion();
    }

    // retorna el oro recolectado en el nivel
    public int getOroRecolectado() {
        return oroRecolectado;
    }

    // establece el total de oro disponible en el nivel
    public void setNivelOroTotal(int total) {
        this.nivelOroTotal = total;
    }

    // retorna el total de oro del nivel
    public int getNivelOroTotal() {
        return nivelOroTotal;
    }

    // true si recolectó todo el oro del nivel
    public boolean nivelCompleto() {
        return oroRecolectado >= nivelOroTotal;
    }

    public boolean cavoEsteFrame() { return cavoEsteFrame; } // true si cavó este frame
    public boolean isEnEscalera() { return enEscalera; }
    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
    public boolean isEnAire() { return enAire; }
    public void setEnAire(boolean v) { this.enAire = v; }
    // verifica si el recolector está intersectando algún agujero
    public boolean estaEnAgujero() {
        if (nivel == null) return false;
        for (py_poo.entities.Agujero a : nivel.agujeros) {
            if (getBounds().intersects(a.getBounds())) return true;
        }
        return false;
    }

    public int getTileX() { return (int)((getX() + tileSize / 2) / tileSize); } // tile X actual
    public int getTileY() { return (int)((getY() + tileSize / 2) / tileSize); } // tile Y actual

    @Override
    // dibuja al recolector con la animación adecuada según su estado
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
                s.dibujar(g, x, y, tileSize, tileSize); // mirando izquierda
            } else {
                g.drawImage(s.getImagen(), x + tileSize, y, -tileSize, tileSize, null); // invertido a derecha
            }
        } else if (sprite != null) {
            g.drawImage(sprite, (int)getX(), (int)getY(), tileSize, tileSize, null);
        }
    }
}
