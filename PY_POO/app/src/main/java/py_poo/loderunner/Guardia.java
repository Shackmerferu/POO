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
    public static final double VELOCIDAD = 1.7; // velocidad de movimiento del guardia

    private IA_Guardia ia; // inteligencia artificial del guardia
    private Recolector heroe; // referencia al héroe (recolector)
    private Nivel nivel; // nivel en el que se encuentra el guardia
    private boolean enAgujero; // indica si está atrapado en un agujero
    private boolean enEscalera; // indica si está sobre una escalera
    private boolean enBarra; // indica si está sobre una barra
    private boolean cayendo; // indica si está cayendo
    private Moneda monedaCargada; // moneda que lleva cargada si recolectó una
    private boolean enAire; // indica si está en el aire sin soporte
    private int tileSize; // tamaño en píxeles de cada tile
    private int spawnTileX; // tile X de aparición inicial
    private int spawnTileY; // tile Y de aparición inicial

    private Animacion animCaminando; // animación al caminar
    private Animacion animAtrapado; // animación cuando está atrapado

    // RUTAS DE IMAGENES DEL GUARDIA - CAMBIAR AQUI
    private static final String RUTA_GUARDIA = "imagenes/Lode Runner/personaje (2).png";

    // constructor: crea guardia en posición de tile y tamaño dado
    public Guardia(int tileX, int tileY, int tileSize) {
        this.spawnTileX = tileX; // guarda tile de reaparición
        this.spawnTileY = tileY;
        this.tileSize = tileSize;
        this.ia = new IA_Guardia(); // crea nueva IA
        this.vidas = 1; // una vida
        this.direccion = -1; // mira a izquierda por defecto
        this.monedaCargada = null; // sin moneda al inicio
        this.dimension = new java.awt.Dimension(tileSize, tileSize);
        setX(tileX * tileSize); // posición en píxeles
        setY(tileY * tileSize);
        cargarAnimaciones(); // carga sprites de animación
    }

    // carga las imágenes de animación del guardia
    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();
        BufferedImage img = cr.cargarImagen(RUTA_GUARDIA);
        if (img != null) {
            Sprite s = new Sprite(img);
            animCaminando = new Animacion(List.of(s), 200); // animación caminando
            animAtrapado = new Animacion(List.of(s), 300); // animación atrapado
        }
    }

    // actualiza las animaciones del guardia cada frame
    public void actualizar() {
        if (animCaminando != null) animCaminando.actualizar();
        if (animAtrapado != null) animAtrapado.actualizar();
    }

    // mueve al guardia hacia la izquierda si no hay obstáculo
    public void moverIzquierda() {
        if (cayendo || nivel == null) return;
        double newX = getX() - VELOCIDAD;
        int tx = (int)newX / tileSize;
        int ty = enEscalera ? (int)(getY() + tileSize / 2) / tileSize
                : (int)(getY() + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, ty)) return; // bloqueado por ladrillo
        direccion = -1; // mira a izquierda
        setX(newX); // actualiza posición
    }

    // mueve al guardia hacia la derecha si no hay obstáculo
    public void moverDerecha() {
        if (cayendo || nivel == null) return;
        double newX = getX() + VELOCIDAD;
        int tx = (int)(newX + tileSize - 1) / tileSize;
        int ty = enEscalera ? (int)(getY() + tileSize / 2) / tileSize
                : (int)(getY() + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, ty)) return; // bloqueado por ladrillo
        direccion = 1; // mira a derecha
        setX(newX); // actualiza posición
    }

    // mueve al guardia hacia arriba por escalera/barra
    public void moverArriba() {
        if (!enEscalera || cayendo || nivel == null) return;
        double nuevaY = getY() - VELOCIDAD;
        int tx = getTileX();
        if (nivel.esSolido(tx, (int)nuevaY / tileSize)) return; // bloqueado arriba
        int tyPies = (int)(nuevaY + tileSize - 1) / tileSize;
        if (nivel.esEscalera(tx, tyPies) || nivel.esBarra(tx, tyPies)
            || nivel.esSolido(tx, tyPies)) {
            setY(nuevaY); // sube normalmente
        } else if (nivel.esEscalera(tx, tyPies + 1) || nivel.esBarra(tx, tyPies + 1)) {
            setY(tyPies * tileSize); // ajusta posición al siguiente escalón
        }
    }

    // mueve al guardia hacia abajo por escalera
    public void moverAbajo() {
        if (cayendo || nivel == null) return;
        int tx = getTileX();
        int tyPies2 = (int)(getY() + tileSize) / tileSize;
        if (!enEscalera) {
            if (nivel.esEscalera(tx, tyPies2)) {
                enEscalera = true; // se sube a escalera si está en los pies
                setY(tyPies2 * tileSize - tileSize);
            }
            return;
        }
        double nuevaY = getY() + VELOCIDAD;
        int tyPies = (int)(nuevaY + tileSize - 1) / tileSize;
        if (nivel.esSolido(tx, tyPies)) return; // bloqueado abajo
        if (nivel.esEscalera(tx, tyPies) || nivel.esBarra(tx, tyPies)) {
            setY(nuevaY); // baja normalmente
        }
    }

    // reaparece al guardia en su posición inicial
    public void reaparecer() {
        setX(spawnTileX * tileSize + tileSize / 4);
        setY(spawnTileY * tileSize);
        enAgujero = false; // ya no está atrapado
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        enAire = false;
        if (monedaCargada != null) {
            monedaCargada = null; // suelta la moneda si llevaba una
        }
        ia.reanimar(); // inicia reanimación al volver al spawn
    }

    public int getTileX() { return (int)((getX() + tileSize / 2) / tileSize); } // tile X actual
    public int getTileY() { return (int)((getY() + tileSize / 2) / tileSize); } // tile Y actual

    public IA_Guardia getIA() { return ia; } // retorna la IA del guardia
    public boolean isEnEscalera() { return enEscalera; } // true si está en escalera
    public void setHeroe(Recolector heroe) { this.heroe = heroe; } // asigna el héroe a perseguir
    public void setNivel(Nivel nivel) { this.nivel = nivel; } // asigna el nivel

    @Override
    // actualiza lógica de movimiento del guardia cada frame
    public void mover() {
        if (enAgujero) return; // no se mueve si está atrapado
        if (heroe == null || nivel == null) return;

        aplicarGravedad(); // aplica caída si es necesario

        if (!cayendo) {
            int hx = (int)heroe.getX(); // posición X del héroe
            int hy = (int)heroe.getY(); // posición Y del héroe
            int gx = (int)getX(); // posición X del guardia
            int gy = (int)getY(); // posición Y del guardia
            int tx = getTileX(); // tile X del guardia
            int ty = getTileY(); // tile Y del guardia

            boolean puedeIzq = !nivel.esSolido(tx - 1, ty); // puede moverse a izquierda
            boolean puedeDer = !nivel.esSolido(tx + 1, ty); // puede moverse a derecha
            boolean puedeSubir = nivel.esEscalera(tx, ty - 1); // puede subir escalera
            boolean puedeBajar = nivel.esEscalera(tx, ty + 1); // puede bajar escalera

            // calcula dirección según IA
            int dir = ia.calcularMovimiento(gx, gy, hx, hy, puedeIzq, puedeDer, puedeSubir, puedeBajar, enEscalera, enBarra);

            if (dir == -1) moverIzquierda();
            else if (dir == 1) moverDerecha();
            else if (dir == -2) moverArriba();
            else if (dir == 2) moverAbajo();
        }

        detectarPlataforma(); // detecta escaleras/barras bajo los pies
        actualizar(); // actualiza animaciones
    }

    // verifica si hay soporte sólido, escalera o barra en el tile dado
    private boolean tieneSoporte(int tx, int ty) {
        return nivel.esSolido(tx, ty) || nivel.esEscalera(tx, ty) || nivel.esBarra(tx, ty);
    }

    // aplica gravedad al guardia si no hay soporte bajo sus pies
    private void aplicarGravedad() {
        if (nivel == null) return;
        int txL = (int)getX() / tileSize; // tile izquierdo de los pies
        int txR = (int)(getX() + tileSize - 1) / tileSize; // tile derecho de los pies
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
            cayendo = true; // empieza a caer
        }
    }

    // detecta si el guardia está en escalera, barra o aire
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
        enAire = !cayendo && !enEscalera && !enBarra // sin soporte y no cayendo = en aire
            && !tieneSoporte(txL, tyPies2)
            && !tieneSoporte(txR, tyPies2);
    }

    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
    public boolean isCargandoOro() { return monedaCargada != null; } // true si lleva moneda
    public Moneda getMonedaCargada() { return monedaCargada; }
    public void setMonedaCargada(Moneda m) { this.monedaCargada = m; }
    public boolean isEnAire() { return enAire; }
    public void setEnAire(boolean v) { this.enAire = v; }
    public boolean enAgujero() { return enAgujero; } // true si está en agujero
    public void enAgujero(boolean v) { this.enAgujero = v; }

    @Override
    // dibuja al guardia en pantalla con su animación correspondiente
    public void display(Graphics g) {
        Sprite s = (ia.getEstado() == IA_Guardia.Comportamiento.ATRAPADO && animAtrapado != null)
            ? animAtrapado.obtenerFrame() // sprite atrapado
            : (animCaminando != null ? animCaminando.obtenerFrame() : null); // sprite caminando
        if (s != null) {
            int x = (int)getX();
            int y = (int)getY();
            if (direccion < 0) {
                s.dibujar(g, x, y, tileSize, tileSize); // mira a izquierda
            } else {
                g.drawImage(s.getImagen(), x + tileSize, y, -tileSize, tileSize, null); // invertido a derecha
            }
            if (monedaCargada != null) { // dibuja moneda sobre el guardia si lleva una
                g.setColor(Color.YELLOW);
                g.fillOval(x + tileSize / 4, y - 8, tileSize / 2, 8);
            }
        } else if (sprite != null) {
            g.drawImage(sprite, (int)getX(), (int)getY(), tileSize, tileSize, null);
        }
    }
}
