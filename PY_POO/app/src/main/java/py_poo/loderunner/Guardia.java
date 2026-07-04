package py_poo.loderunner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Agujero;
import py_poo.entities.Moneda;
import py_poo.entities.ObjetoGrafico;
import py_poo.entities.Personaje;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.utils.CargadorRecursos;

// Guardia enemigo que patrulla el nivel, puede caer en agujeros, recolectar oro y perseguir al héroe
public class Guardia extends Personaje {
    public static final double VELOCIDAD = 1.5; // velocidad de movimiento del guardia (más lento que el héroe)

    private IA_Guardia ia; // inteligencia artificial que decide sus movimientos y estados
    private Recolector heroe; // referencia al héroe para perseguirlo
    private Nivel nivel; // nivel en el que se encuentra para consultar tiles
    private boolean enAgujero; // true si está atrapado dentro de un agujero
    private boolean enEscalera; // true si está pisando una escalera
    private boolean enBarra; // true si está colgado de una barra
    private boolean cayendo; // true si está en caída libre
    private Moneda monedaCargada; // moneda que lleva cargada (null si no tiene ninguna)
    private boolean enAire; // true si está en el aire sin soporte bajo los pies
    private int tileSize; // tamaño en píxeles de cada tile del nivel
    private int spawnTileX; // tile X de aparición inicial (para reapariciones)
    private int spawnTileY; // tile Y de aparición inicial

    private Animacion animCaminando; // animación al caminar
    private Animacion animAtrapado; // animación cuando está atrapado en un agujero
    private Animacion animEscalera; // animación cuando está en escalera
    private Animacion animBarra; // animación cuando está en barra
    private Animacion animCayendo; // animación cuando está cayendo

    private String skin = "original"; // skin activa ("original" o "alternativo")

    private int tiempoEsperaEscape; // frames restantes de espera quieto tras salir del agujero
    private int contadorAtascado; // frames acumulados sin poder moverse (anti-atasco)

    // RUTAS DE IMAGENES DEL GUARDIA - CAMBIAR AQUI
    private static final String RUTA_CAMINANDO_2 = "imagenes/Lode Runner/personajes/%s/caminando - 2 (%d).png";
    private static final String RUTA_CAMINANDO = "imagenes/Lode Runner/personajes/%s/caminando - 2(1).png";
    private static final String RUTA_MUERTO_2 = "imagenes/Lode Runner/personajes/%s/muerto - 2 (%d).png";
    private static final String RUTA_ESCALERA_2 = "imagenes/Lode Runner/personajes/%s/escalera - 2 (%d).png";
    private static final String RUTA_BARRA_2 = "imagenes/Lode Runner/personajes/%s/barra - 2 (%d).png";
    private static final String RUTA_CAYENDO_2 = "imagenes/Lode Runner/personajes/%s/cayendo - 2 (%d).png";
    private static final int FRAMES_ANIM = 4;

    // Constructor: crea guardia en la posición de tile y con el tamaño de tile dados
    // Inicializa su IA, vidas, dirección y carga las animaciones
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

    // Carga las imágenes de animación del guardia desde los archivos de recursos
    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();

        animCaminando = cargarAnimacion(cr, RUTA_CAMINANDO_2, skin, FRAMES_ANIM, 200);
        if (animCaminando == null) {
            animCaminando = cargarAnimacion(cr, RUTA_CAMINANDO, skin, 1, 200);
        }

        animEscalera = cargarAnimacion(cr, RUTA_ESCALERA_2, skin, FRAMES_ANIM, 200);
        animBarra = cargarAnimacion(cr, RUTA_BARRA_2, skin, FRAMES_ANIM, 200);
        animCayendo = cargarAnimacion(cr, RUTA_CAYENDO_2, skin, FRAMES_ANIM, 200);
        animAtrapado = cargarAnimacion(cr, RUTA_MUERTO_2, skin, FRAMES_ANIM, 300);
    }

    public void setSkin(String skin) {
        if (skin.equals("original") || skin.equals("alternativo")) {
            this.skin = skin;
            cargarAnimaciones();
        }
    }

    public String getSkin() { return skin; }

    private Animacion cargarAnimacion(CargadorRecursos cr, String template, String skin, int frames, long tiempoMs) {
        List<Sprite> lista = new ArrayList<>();
        for (int i = 1; i <= frames; i++) {
            BufferedImage img = cr.cargarImagen(String.format(template, skin, i));
            if (img != null) lista.add(new Sprite(img));
        }
        if (!lista.isEmpty()) return new Animacion(lista, tiempoMs);
        return null;
    }

    // Actualiza las animaciones del guardia cada frame
    public void actualizar() {
        if (animCaminando != null) animCaminando.actualizar();
        if (animAtrapado != null) animAtrapado.actualizar();
        if (animEscalera != null) animEscalera.actualizar();
        if (animBarra != null) animBarra.actualizar();
        if (animCayendo != null) animCayendo.actualizar();
    }

    // Mueve al guardia un paso a la izquierda si no hay tile sólido bloqueando
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

    // Mueve al guardia un paso a la derecha si no hay tile sólido bloqueando
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

    // Sube al guardia por una escalera o barra si es posible
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

    // Baja al guardia por una escalera o se agarra a una si está en el borde
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

    // Reposiciona al guardia en un tile aleatorio de la fila superior del nivel,
    // evitando el tile del héroe, paredes sólidas y la puerta
    public void reaparecer() {
        if (nivel != null && heroe != null) {
            int w = nivel.getAnchoMapa();
            int h = nivel.getAltoMapa();
            if (w > 0 && h > 0) {
                int hTx = heroe.getTileX();
                int rx, ry;
                int intentos = 0;
                do {
                    rx = (int)(Math.random() * w);
                    ry = 0;
                    while (ry < h && (nivel.esSolido(rx, ry) || nivel.getTile(rx, ry) == Nivel.PUERTA)) {
                        ry++;
                    }
                    intentos++;
                } while ((rx == hTx || ry >= h) && intentos < 20);
                if (ry < h) {
                    setX(rx * tileSize);
                    setY(ry * tileSize);
                } else {
                    setX(rx * tileSize);
                    setY(0);
                }
            }
        } else {
            setY(0);
        }
        enAgujero = false;
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        enAire = false;
        if (monedaCargada != null) {
            monedaCargada = null;
        }
        ia.reanimar();
    }

    // Retorna la columna (tile X) actual del guardia basada en su centro
    public int getTileX() { return (int)((getX() + tileSize / 2) / tileSize); }
    // Retorna la fila (tile Y) actual del guardia basada en su centro
    public int getTileY() { return (int)((getY() + tileSize / 2) / tileSize); }

    public IA_Guardia getIA() { return ia; }
    public boolean isEnEscalera() { return enEscalera; }
    public void setHeroe(Recolector heroe) { this.heroe = heroe; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    @Override
    // Lógica de movimiento del guardia ejecutada cada frame:
    // - Si está atrapado en agujero no se mueve
    // - Si está saliendo del agujero espera quieto el tiempo de espera
    // - Aplica gravedad, calcula dirección según IA y ejecuta el movimiento
    // - Detecta escaleras/barras y actualiza animaciones
    public void mover() {
        if (enAgujero) {
            actualizar();
            return;
        }
        if (heroe == null || nivel == null) return;

        if (ia.isSaliendo()) {
            tiempoEsperaEscape--;
            if (tiempoEsperaEscape <= 0) {
                ia.reaparecer();
            }
            actualizar();
            return;
        }

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

            if (dir == -1) { moverIzquierda(); contadorAtascado = 0; }
            else if (dir == 1) { moverDerecha(); contadorAtascado = 0; }
            else if (dir == -2) { moverArriba(); contadorAtascado = 0; }
            else if (dir == 2) { moverAbajo(); contadorAtascado = 0; }
            else {
                contadorAtascado++;
                if (contadorAtascado > 30) {
                    contadorAtascado = 0;
                    if (enEscalera || enBarra) {
                        double nuevoY = getY() + tileSize / 2;
                        int tyPies = (int)(nuevoY + tileSize - 1) / tileSize;
                        if (!nivel.esSolido(getTileX(), tyPies)) {
                            setY(nuevoY);
                        } else {
                            setY(tyPies * tileSize - tileSize);
                        }
                        enEscalera = false;
                        enBarra = false;
                    }
                    ia.setEstado(IA_Guardia.Comportamiento.VAGAR);
                }
            }
        }

        detectarPlataforma();
        actualizar();
    }

    // Verifica si hay soporte sólido, escalera o agujero cerrándose en el tile dado
    // Las barras NO son soporte: solo se agarran con la cabeza al caer
    private boolean tieneSoporte(int tx, int ty) {
        if (nivel.esSolido(tx, ty) || nivel.esEscalera(tx, ty))
            return true;
        if (hayAgujeroSeguro(tx, ty))
            return true;
        return false;
    }

    // True si hay un agujero cerrándose (seguro para caminar) en el tile dado
    private boolean hayAgujeroSeguro(int tx, int ty) {
        if (nivel == null) return false;
        for (Agujero a : nivel.agujeros) {
            int aTx = (int)a.getX() / tileSize;
            int aTy = (int)a.getY() / tileSize;
            if (aTx == tx && aTy == ty && a.getTiempoRestante() < 120) {
                return true;
            }
        }
        return false;
    }

    // Aplica gravedad al guardia: si está cayendo baja, si no tiene soporte empieza a caer
    // Si durante la caída la cabeza encuentra una barra, el guardia se agarra de ella
    private void aplicarGravedad() {
        if (nivel == null) return;
        int txL = (int)getX() / tileSize;
        int txR = (int)(getX() + tileSize - 1) / tileSize;
        int tyPies = (int)(getY() + tileSize) / tileSize;

        if (cayendo) {
            setY(getY() + VELOCIDAD);
            txL = (int)getX() / tileSize;
            txR = (int)(getX() + tileSize - 1) / tileSize;
            int tyCabeza = (int)(getY()) / tileSize;
            if (nivel.esBarra(txL, tyCabeza) || nivel.esBarra(txR, tyCabeza)) {
                setY(tyCabeza * tileSize);
                cayendo = false;
                enBarra = true;
                return;
            }
            tyPies = (int)(getY() + tileSize) / tileSize;
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

    // Detecta si el guardia está sobre escalera, barra o en el aire, actualizando flags
    // La barra se detecta cuando la CABEZA del guardia está a su nivel (no los pies)
    private void detectarPlataforma() {
        if (nivel == null) return;
        int txL = (int)getX() / tileSize;
        int txR = (int)(getX() + tileSize - 1) / tileSize;
        int tyPies = (int)(getY() + tileSize - 1) / tileSize;
        int tyPies2 = (int)(getY() + tileSize) / tileSize;
        int tyCabeza = (int)(getY()) / tileSize;
        enEscalera = nivel.esEscalera(txL, tyPies) || nivel.esEscalera(txR, tyPies);
        enBarra = nivel.esBarra(txL, tyCabeza) || nivel.esBarra(txR, tyCabeza);
        if (!enEscalera && !enBarra
            && (nivel.esEscalera(txL, tyPies2) || nivel.esEscalera(txR, tyPies2))) {
            enEscalera = true;
        }
        enAire = !cayendo && !enEscalera && !enBarra
            && !tieneSoporte(txL, tyPies2)
            && !tieneSoporte(txR, tyPies2);
    }

    // Inicia la secuencia de escape del agujero: sube un tile y entra en estado SALIENDO
    public void iniciarEscape(int holeTileY) {
        enAgujero = false;
        setY((holeTileY - 1) * tileSize);
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        enAire = false;
        tiempoEsperaEscape = IA_Guardia.getTiempoEsperaEscape();
        ia.salir();
    }

    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
    public boolean isCargandoOro() { return monedaCargada != null; }
    public Moneda getMonedaCargada() { return monedaCargada; }
    public void setMonedaCargada(Moneda m) { this.monedaCargada = m; }

    public void soltarMoneda() {
        if (monedaCargada == null || nivel == null) return;
        int tx = getTileX();
        int ty = getTileY();
        if (nivel.esSolido(tx, ty)) {
            int arriba = Math.max(0, ty - 1);
            if (!nivel.esSolido(tx, arriba)) {
                ty = arriba;
            }
        }
        Moneda suelta = new Moneda(tx, ty, tileSize);
        nivel.monedas.add(suelta);
        setMonedaCargada(null);
    }

    public void intentarRecolectarOro() {
        if (nivel == null) return;
        if (isCargandoOro()) {
            if (Math.random() < 0.005) {
                soltarMoneda();
            }
        } else {
            for (Moneda m : nivel.monedas) {
                if (!m.isRecolectada() && getBounds().intersects(m.getBounds())) {
                    m.recolectar();
                    setMonedaCargada(m);
                    break;
                }
            }
        }
    }

    public boolean manejarColisionAgujero(List<Agujero> agujeros, List<Guardia> otrosGuardias) {
        if (nivel == null) return false;
        if (enAgujero) {
            ia.incrementarTiempoAtrapado();
            for (Agujero a : agujeros) {
                boolean enEsteAgujero = getBounds().intersects(a.getBounds());
                if (!enEsteAgujero) {
                    int aTx = (int)a.getX() / tileSize;
                    int aTy = (int)a.getY() / tileSize;
                    if (getTileX() == aTx && (getY() + tileSize) / tileSize == aTy) {
                        enEsteAgujero = true;
                    }
                }
                if (enEsteAgujero) {
                    int ta = ia.getTiempoAtrapado();
                    if (ta >= IA_Guardia.getTiempoEscape() && ta < a.getTiempoRestante()) {
                        iniciarEscape((int)a.getY() / tileSize);
                        return false;
                    } else if (ia.getEstado() == IA_Guardia.Comportamiento.REAPARECER) {
                        setY(a.getY() - tileSize);
                        if (Math.random() < 0.5) {
                            setX(getX() - tileSize);
                        } else {
                            setX(getX() + tileSize);
                        }
                        if (!getBounds().intersects(a.getBounds())) {
                            enAgujero(false);
                            ia.reaparecer();
                        }
                    }
                    return false;
                }
            }
            soltarMoneda();
            reaparecer();
            return true;
        }
        for (Agujero a : agujeros) {
            boolean colision = getBounds().intersects(a.getBounds());
            if (!colision) {
                int aTx = (int)a.getX() / tileSize;
                int aTy = (int)a.getY() / tileSize;
                if (getTileX() == aTx && (getY() + tileSize) / tileSize == aTy) {
                    colision = true;
                }
            }
            if (!colision) continue;
            boolean ocupado = false;
            for (Guardia otro : otrosGuardias) {
                if (otro != this && otro.enAgujero() && otro.getBounds().intersects(a.getBounds())) {
                    ocupado = true;
                    break;
                }
            }
            if (ocupado) return false;
            if (monedaCargada != null) soltarMoneda();
            enAgujero(true);
            setCayendo(false);
            setX(a.getX());
            setY(a.getY());
            ia.atrapar();
            return false;
        }
        return false;
    }

    public boolean isEnAire() { return enAire; }
    public void setEnAire(boolean v) { this.enAire = v; }
    public boolean enAgujero() { return enAgujero; }
    public void enAgujero(boolean v) { this.enAgujero = v; }

    @Override
    // Dibuja al guardia en pantalla con el sprite correspondiente a su estado actual
    public void display(Graphics g) {
        Sprite s = null;
        if (ia.getEstado() == IA_Guardia.Comportamiento.ATRAPADO) {
            s = animAtrapado != null ? animAtrapado.obtenerFrame() : null;
        } else if (enEscalera) {
            s = animEscalera != null ? animEscalera.obtenerFrame() : null;
        } else if (enBarra) {
            s = animBarra != null ? animBarra.obtenerFrame() : null;
        } else if (cayendo) {
            s = animCayendo != null ? animCayendo.obtenerFrame() : null;
        } else {
            s = animCaminando != null ? animCaminando.obtenerFrame() : null;
        }
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
