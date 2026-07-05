package py_poo.loderunner;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Agujero;
import py_poo.entities.Moneda;
import py_poo.entities.Personaje;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.input.InputManager;
import py_poo.interfaces.GameEvent;
import py_poo.interfaces.GameEventListener;
import py_poo.utils.CargadorRecursos;

// Personaje principal del juego controlado por el jugador (héroe).
// Puede moverse, cavar agujeros, recolectar oro y morir si un guardia lo toca o un agujero se cierra sobre él.
public class Recolector extends Personaje {
    public static final int VELOCIDAD = 2; // velocidad de movimiento del héroe (píxeles por frame)
    public static final int VIDAS_INICIALES = 5; // cantidad de vidas con las que empieza cada partida

    private int oroRecolectado; // contador de monedas recolectadas en el nivel actual
    private int nivelOroTotal; // cantidad total de monedas disponibles en el nivel
    private boolean enEscalera; // true si está pisando una escalera
    private boolean enBarra; // true si está colgado de una barra
    private boolean cayendo; // true si está en caída libre
    private boolean cavoEsteFrame; // true si el jugador cavó un ladrillo en este frame (para efectos de sonido)
    private boolean enAire; // true si está en el aire sin soporte bajo los pies
    private boolean muriendo; // true si está en animación de muerte
    private int muriendoTimer; // frames que lleva en estado muriendo (timeout de seguridad)
    private static final int MURIENDO_TIMEOUT = 120; // frames máximos en muriendo (~2 seg)
    private boolean seMovio; // true si el jugador presionó una tecla de movimiento este frame
    private int tileX, tileY; // coordenadas de tile del spawn (para reiniciar posición)
    private int tileSize; // tamaño en píxeles de cada tile del nivel
    private InputManager input; // gestor de entrada del teclado para leer las teclas del jugador
    private Nivel nivel; // referencia al nivel actual para consultar tiles
    private List<Guardia> guardias; // lista de guardias del nivel (para detectar si tapan agujeros)
    private GameEventListener listener; // listener para notificar eventos al juego

    private Animacion animParado; // animación cuando está quieto
    private Animacion animCaminando; // animación cuando camina
    private Animacion animEscalera; // animación cuando está en escalera
    private Animacion animBarra; // animación cuando está en barra
    private Animacion animCayendo; // animación cuando está cayendo
    private Animacion animMuriendo; // animación cuando está muriendo
    private Animacion animCavando; // animación cuando está cavando

    private String skin = "original"; // skin activa ("original" o "alternativo")

    // RUTAS DE IMAGENES DEL RECOLECTOR - CAMBIAR AQUI
    private static final String RUTA_CAMINANDO = "imagenes/Lode Runner/personajes/%s/caminando (%d).png";
    private static final String RUTA_ESCALERA = "imagenes/Lode Runner/personajes/%s/escalera (%d).png";
    private static final String RUTA_BARRA = "imagenes/Lode Runner/personajes/%s/barra (%d).png";
    private static final String RUTA_CAYENDO = "imagenes/Lode Runner/personajes/%s/cayendo (%d).png";
    private static final String RUTA_MUERTO = "imagenes/Lode Runner/personajes/%s/muerto (%d).png";
    private static final String RUTA_CAVANDO = "imagenes/Lode Runner/personajes/%s/cavando (%d).png";
    
    private static final int FRAMES_ANIM = 4;
    private static final int FRAMES_MUERTO = 8;

    // Constructor: crea al héroe en la posición (tileX, tileY) con el tamaño de tile dado
    // Inicializa vidas, dirección, oro y carga las animaciones
    public Recolector(int tileX, int tileY, int tileSize) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileSize = tileSize;
        this.vidas = VIDAS_INICIALES;
        this.direccion = 1;
        this.oroRecolectado = 0;
        this.cargarAnimaciones();
        this.bounds = new java.awt.Rectangle(tileX * tileSize, tileY * tileSize, 16, 16);
    }

    // Carga las imágenes de animación del recolector desde los archivos de recursos
    private void cargarAnimaciones() {
        CargadorRecursos cr = new CargadorRecursos();

        List<Sprite> framesParado = new ArrayList<>();
        for (int i = 1; i <= FRAMES_ANIM; i++) {
            BufferedImage img = cr.cargarImagen(String.format(RUTA_CAMINANDO, skin, i));
            if (img != null) { framesParado.add(new Sprite(img)); break; }
        }
        if (!framesParado.isEmpty()) {
            animParado = new Animacion(framesParado, 500);
        }

        animCaminando = cargarAnimacion(cr, RUTA_CAMINANDO, skin, FRAMES_ANIM, 150);
        animEscalera = cargarAnimacion(cr, RUTA_ESCALERA, skin, FRAMES_ANIM, 200);
        animBarra = cargarAnimacion(cr, RUTA_BARRA, skin, FRAMES_ANIM, 200);
        animCayendo = cargarAnimacion(cr, RUTA_CAYENDO, skin, FRAMES_ANIM, 200);
        animMuriendo = cargarAnimacion(cr, RUTA_MUERTO, skin, FRAMES_MUERTO, 200);
        if (animMuriendo != null) animMuriendo.setRepitiendo(false);
        animCavando  = cargarAnimacion(cr, RUTA_CAVANDO, skin, FRAMES_ANIM, 200);
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

    // Asigna el gestor de entrada para leer las teclas del jugador
    public void setInputManager(InputManager input) {
        this.input = input;
    }

    // Asigna el nivel actual al héroe
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    // Asigna la lista de guardias del nivel (necesaria para detectar si tapan agujeros)
    public void setGuardias(List<Guardia> guardias) {
        this.guardias = guardias;
    }

    public void setGameEventListener(GameEventListener listener) {
        this.listener = listener;
    }

    @Override
    // Procesa la entrada del jugador cada frame, ejecuta el movimiento correspondiente
    // y aplica gravedad. Cavar solo es posible si no está en escalera, barra o cayendo.
    public void mover() {
        if (input == null) return;
        cavoEsteFrame = false;
        seMovio = false;
        if (!cayendo && !muriendo) {
            if (input.isLeftPressed()) { moverIzquierda(); seMovio = true; }
            if (input.isRightPressed()) { moverDerecha(); seMovio = true; }
            if (input.isUpPressed()) { moverArriba(); seMovio = true; }
            if (input.isDownPressed()) { moverAbajo(); seMovio = true; }
            if (input.isDigPressed()) {
                if (direccion < 0) cavarIzquierda();
                else cavarDerecha();
            }
        }
        if (muriendo) {
            muriendoTimer++;
            if (animMuriendo == null || animMuriendo.termino() || muriendoTimer >= MURIENDO_TIMEOUT) {
                muriendo = false;
                perderVida();
                if (listener != null) listener.onEvent(GameEvent.HERO_DEATH);
            }
        }
        aplicarGravedad();
        detectarPlataforma();
        actualizar();
    }

    // Aplica gravedad al héroe: si está cayendo baja; si no tiene soporte empieza a caer
    // Si durante la caída la cabeza encuentra una barra, el héroe se agarra de ella
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

    // Verifica si hay soporte (sólido, escalera, guardia en agujero o agujero cerrándose)
    // Las barras NO son soporte: solo se agarran con la cabeza al caer
    private boolean tieneSoporte(int tx, int ty) {
        if (nivel.esSolido(tx, ty) || nivel.esEscalera(tx, ty))
            return true;
        if (hayGuardiaEnTile(tx, ty))
            return true;
        if (hayAgujeroSeguro(tx, ty))
            return true;
        return false;
    }

    // Detecta si el héroe está sobre escalera, barra o en el aire, actualizando los flags
    // La barra se detecta cuando la CABEZA del héroe está a su nivel (no los pies)
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

    // Actualiza todas las animaciones del héroe cada frame
    public void actualizar() {
        if (animParado != null) animParado.actualizar();
        if (animCaminando != null) animCaminando.actualizar();
        if (animEscalera != null) animEscalera.actualizar();
        if (animBarra != null) animBarra.actualizar();
        if (animCayendo != null) animCayendo.actualizar();
        if (animCavando != null) animCavando.actualizar();
        if (animMuriendo != null) animMuriendo.actualizar();
    }

    // Mueve al héroe un paso a la izquierda si no hay tile sólido bloqueando
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

    // Mueve al héroe un paso a la derecha si no hay tile sólido bloqueando
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

    // Sube al héroe por una escalera o barra si es posible
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

    // Baja al héroe por escalera o lo agarra a una si camina al borde de una
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

    // Cava un ladrillo a la izquierda del héroe si es posible
    public void cavarIzquierda() {
        if (enEscalera || enBarra || cayendo || nivel == null) return;
        int tyPies = (int)(getY() + tileSize) / tileSize;
        if (nivel.cavarEn(getTileX() - 1, tyPies)) {
            cavoEsteFrame = true;
            if (listener != null) listener.onEvent(GameEvent.DIG);
        }
    }

    public void cavarDerecha() {
        if (enEscalera || enBarra || cayendo || nivel == null) return;
        int tyPies = (int)(getY() + tileSize) / tileSize;
        if (nivel.cavarEn(getTileX() + 1, tyPies)) {
            cavoEsteFrame = true;
            if (listener != null) listener.onEvent(GameEvent.DIG);
        }
    }

    // Incrementa el contador de oro recolectado
    public void recogerOro() {
        oroRecolectado++;
    }

    // Reinicia la posición del héroe a su tile de spawn, cancelando caídas y estados
    public void reiniciarPosicion() {
        setX(tileX * tileSize);
        setY(tileY * tileSize);
        cayendo = false;
        enEscalera = false;
        enBarra = false;
        enAire = false;
    }

    // Reduce una vida y reinicia la posición
    public void perderVida() {
        vidas--;
        reiniciarPosicion();
    }

    // Retorna la cantidad de oro recolectado en el nivel actual
    public int getOroRecolectado() {
        return oroRecolectado;
    }

    // Establece el total de oro que hay en el nivel
    public void setNivelOroTotal(int total) {
        this.nivelOroTotal = total;
    }

    // Retorna el total de oro disponible en el nivel
    public int getNivelOroTotal() {
        return nivelOroTotal;
    }

    // True si el héroe ya recolectó todo el oro del nivel
    public boolean nivelCompleto() {
        return oroRecolectado >= nivelOroTotal;
    }

    public boolean cavoEsteFrame() { return cavoEsteFrame; }

    public void verificarCaidaEnAgujero() {
        if (nivel == null || muriendo) return;
        int hTx = getTileX();
        int hTy = (int)((getY() + tileSize - 1) / tileSize);
        for (Agujero a : nivel.agujeros) {
            int aTx = (int)a.getX() / tileSize;
            int aTy = (int)a.getY() / tileSize;
            if (hTx != aTx || hTy != aTy) continue;
            if (a.getTiempoRestante() > 1) continue;
            boolean guardiaTapa = false;
            for (Guardia g : guardias) {
                if (g.enAgujero() && g.getTileX() == aTx && g.getTileY() == aTy) {
                    guardiaTapa = true;
                    break;
                }
            }
            if (guardiaTapa) continue;
            muriendo = true;
            muriendoTimer = 0;
            if (animMuriendo != null) animMuriendo.reiniciar();
            return;
        }
    }

    public void verificarColisionGuardias() {
        if (nivel == null || guardias == null) return;
        for (Guardia g : guardias) {
            if (!getBounds().intersects(g.getBounds())) continue;
            if (g.enAgujero() || g.getIA().isSaliendo()) continue;
            boolean puedeBajar = input.isDownPressed() || input.isSPressed();
            boolean heroearriba = getY() + getHeight() <= g.getY() + g.getHeight() + 5;
            boolean hayAgujeroAbierto = false;
            for (Agujero a : nivel.agujeros) {
                if (a.isAbierto()) { hayAgujeroAbierto = true; break; }
            }
            if (heroearriba && hayAgujeroAbierto && !puedeBajar) {
                int headTy = (int)(getY() - 1) / nivel.getTile_size();
                if (headTy >= 0 && !nivel.esSolido(getTileX(), headTy)) {
                    setY(g.getY() - getHeight());
                } else {
                    perderVida();
                    if (listener != null) listener.onEvent(GameEvent.HERO_DEATH);
                }
            } else {
                perderVida();
                if (listener != null) listener.onEvent(GameEvent.HERO_DEATH);
            }
            return;
        }
    }

    public void recolectarMonedas() {
        if (nivel == null) return;
        for (Moneda m : nivel.monedas) {
            if (!m.isRecolectada() && getBounds().intersects(m.getBounds())) {
                m.recolectar();
                oroRecolectado++;
                if (listener != null) listener.onEvent(GameEvent.COIN_COLLECTED);
                return;
            }
        }
    }

    public boolean isEnEscalera() { return enEscalera; }
    public void setEnEscalera(boolean v) { this.enEscalera = v; }
    public boolean isEnBarra() { return enBarra; }
    public void setEnBarra(boolean v) { this.enBarra = v; }
    public boolean isCayendo() { return cayendo; }
    public void setCayendo(boolean v) { this.cayendo = v; }
    public boolean isEnAire() { return enAire; }
    public void setEnAire(boolean v) { this.enAire = v; }

    // Verifica si el héroe está sobre un agujero completamente abierto (tiempoRestante >= 120)
    // Los agujeros con guardia dentro o cerrándose (< 120) se consideran seguros
    public boolean estaEnAgujero() {
        if (nivel == null) return false;
        for (Agujero a : nivel.agujeros) {
            if (getBounds().intersects(a.getBounds())) {
                if (hayGuardiaEnAgujero(a)) continue;
                if (a.getTiempoRestante() < 120) continue;
                return true;
            }
        }
        return false;
    }

    // True si hay un guardia atrapado en el agujero dado (el guardia tapa el hueco)
    private boolean hayGuardiaEnAgujero(Agujero a) {
        if (guardias == null) return false;
        int aTx = (int)a.getX() / tileSize;
        int aTy = (int)a.getY() / tileSize;
        for (Guardia g : guardias) {
            if (g.enAgujero() && g.getTileX() == aTx && g.getTileY() == aTy) {
                return true;
            }
        }
        return false;
    }

    // True si hay un guardia atrapado en el tile dado (sirve como soporte para caminar)
    private boolean hayGuardiaEnTile(int tx, int ty) {
        if (guardias == null) return false;
        for (Guardia g : guardias) {
            if (g.enAgujero() && g.getTileX() == tx && g.getTileY() == ty) {
                return true;
            }
        }
        return false;
    }

    // True si el agujero en el tile dado se está cerrando (< 120 restante), permitiendo caminar sobre él
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

    // Retorna la columna (tile X) actual basada en el centro del personaje
    public int getTileX() { return (int)((getX() + tileSize / 2) / tileSize); }
    // Retorna la fila (tile Y) actual basada en el centro del personaje
    public int getTileY() { return (int)((getY() + tileSize / 2) / tileSize); }

    @Override
    // Dibuja al héroe con la animación adecuada según su estado (escalera, barra, caminando o quieto)
    public void display(Graphics g) {
        Sprite s = null;
        if (muriendo) s = animMuriendo != null ? animMuriendo.obtenerFrame() : null;
        else if (enEscalera) s = animEscalera != null ? animEscalera.obtenerFrame() : null;
        else if (enBarra) s = animBarra != null ? animBarra.obtenerFrame() : null;
        else if (cayendo) s = animCayendo != null ? animCayendo.obtenerFrame() : null;
        else if (cavoEsteFrame) s = animCavando != null ? animCavando.obtenerFrame() : null;
        else if (seMovio) s = animCaminando != null ? animCaminando.obtenerFrame() : null;
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
