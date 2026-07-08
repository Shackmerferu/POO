package py_poo.loderunner;

import java.util.Random;

// IA que controla el comportamiento de los guardias del juego Lode Runner
public class IA_Guardia {
    private Random rand; // generador de números aleatorios para decisiones de movimiento
    private int direccionPreferida; // dirección hacia la que prefiere moverse al vagar (1 derecha, -1 izquierda)
    private int contadorCambio; // contador de frames para temporizar cambios de comportamiento

    private static final int CAMBIO_CADENCIA = 60; // frames entre cambios de dirección al vagar (~1 segundo a 60fps)
    private static final int CAMPO_VISION = 200; // alcance en píxeles para detectar al héroe y comenzar a perseguirlo

    // Enumeración de todos los estados de comportamiento que puede tener un guardia
    public enum Comportamiento {
        PERSEGUIR,   // persigue activamente al héroe
        VAGAR,       // deambula aleatoriamente por el nivel
        ATRAPADO,    // cayó en un agujero y no puede moverse
        REAPARECER,  // está siendo reposicionado tras estar atrapado
        REANIMACION, // periodo de inmovilidad tras reaparecer
        SALIENDO     // está saliendo de un agujero (escape)
    }

    private Comportamiento estado; // estado actual del comportamiento del guardia
    private int tiempoAtrapado; // frames acumulados que el guardia lleva atrapado en un agujero
    private int tiempoReanimacion; // frames acumulados en estado de reanimación tras reaparecer

    private static final int TIEMPO_MAX_ATRAPADO = 180; // frames máximos atrapado antes de forzar reaparición (~3s)
    private static final int TIEMPO_REANIMACION = 90; // frames que dura la inmovilidad tras reaparecer (~1.5s)
    private static final int TIEMPO_ESCAPE = 150; // frames mínimos atrapado para poder escapar del agujero (~2.5s)
    private static final int TIEMPO_ESPERA_ESCAPE = 60; // frames que espera quieto tras salir del agujero (~1s)

    // Inicializa la IA: dirección aleatoria, contador en cero, comienza vagando
    public IA_Guardia() {
        this.rand = new Random();
        this.direccionPreferida = rand.nextBoolean() ? 1 : -1;
        this.contadorCambio = 0;
        this.estado = Comportamiento.VAGAR;
    }

    // Calcula y retorna el movimiento que debe realizar el guardia según su estado actual y la posición del héroe
    // Retorna: -1 = izquierda, 1 = derecha, -2 = arriba, 2 = abajo, 0 = quieto
    public int calcularMovimiento(int guardiaX, int guardiaY, int heroeX, int heroeY,
                                   boolean puedeIzq, boolean puedeDer,
                                   boolean puedeSubir, boolean puedeBajar,
                                   boolean enEscalera, boolean enBarra) {
        contadorCambio++;

        boolean heroeEnRango = Math.abs(guardiaX - heroeX) < CAMPO_VISION
                            && Math.abs(guardiaY - heroeY) < CAMPO_VISION;

        switch (estado) {
            case ATRAPADO:
                tiempoAtrapado++;
                if (tiempoAtrapado >= TIEMPO_MAX_ATRAPADO) {
                    estado = Comportamiento.REAPARECER;
                }
                return 0;

            case SALIENDO:
                return 0;

            case REAPARECER:
                return 0;

            case REANIMACION:
                tiempoReanimacion++;
                if (tiempoReanimacion >= TIEMPO_REANIMACION) {
                    estado = Comportamiento.VAGAR;
                }
                return 0;

            case PERSEGUIR:
                if (!heroeEnRango && contadorCambio % CAMBIO_CADENCIA == 0 && rand.nextDouble() < 0.7) {
                    estado = Comportamiento.VAGAR;
                }
                if (estado == Comportamiento.VAGAR) {
                    return calcularVagar(guardiaX, guardiaY, heroeX, heroeY,
                                        puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                        enEscalera, enBarra);
                }
                return calcularPersecucion(guardiaX, guardiaY, heroeX, heroeY,
                                           puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                           enEscalera, enBarra);

            case VAGAR:
            default:
                if (heroeEnRango) {
                    estado = Comportamiento.PERSEGUIR;
                } else if (contadorCambio % CAMBIO_CADENCIA == 0 && rand.nextDouble() < 0.3) {
                    estado = Comportamiento.PERSEGUIR;
                }
                if (estado == Comportamiento.PERSEGUIR) {
                    return calcularPersecucion(guardiaX, guardiaY, heroeX, heroeY,
                                              puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                              enEscalera, enBarra);
                }
                return calcularVagar(guardiaX, guardiaY, heroeX, heroeY,
                                     puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                     enEscalera, enBarra);
        }
    }

    // Calcula el movimiento de persecución: se acerca al héroe priorizando vertical si está en escalera/barra
    private int calcularPersecucion(int gx, int gy, int hx, int hy,
                                     boolean izq, boolean der,
                                     boolean subir, boolean bajar,
                                     boolean enEscalera, boolean enBarra) {
        if (enEscalera || enBarra) {
            if (hy < gy && subir) return -2;
            if (hy > gy && bajar) return 2;
        }

        if (Math.abs(gx - hx) > Math.abs(gy - hy)) {
            if (hx < gx && izq) return -1;
            if (hx > gx && der) return 1;
        } else {
            if (hy < gy && subir) return -2;
            if (hy > gy && bajar) return 2;
        }

        if (izq) return -1;
        if (der) return 1;
        return 0;
    }

    // Calcula movimiento aleatorio de vago: cambia dirección periódicamente y deambula
    private int calcularVagar(int gx, int gy, int hx, int hy,
                               boolean izq, boolean der,
                               boolean subir, boolean bajar,
                               boolean enEscalera, boolean enBarra) {
        if (contadorCambio % CAMBIO_CADENCIA == 0) {
            direccionPreferida = rand.nextBoolean() ? 1 : -1;
        }

        if (enEscalera || enBarra) {
            if (rand.nextBoolean() && subir) return -2;
            if (rand.nextBoolean() && bajar) return 2;
        }

        if (direccionPreferida > 0 && der) return 1;
        if (direccionPreferida < 0 && izq) return -1;

        if (der) return 1;
        if (izq) return -1;
        return 0;
    }

    public Comportamiento getEstado() { return estado; }
    public void setEstado(Comportamiento e) { this.estado = e; }

    // Marca al guardia como atrapado en un agujero y reinicia el contador
    public void atrapar() {
        estado = Comportamiento.ATRAPADO;
        tiempoAtrapado = 0;
    }

    // Marca al guardia como saliendo del agujero (escape voluntario)
    public void salir() {
        estado = Comportamiento.SALIENDO;
        tiempoAtrapado = 0;
    }

    // Libera al guardia después de escapar: vuelve al estado VAGAR
    public void reaparecer() {
        estado = Comportamiento.VAGAR;
        tiempoAtrapado = 0;
    }

    public int getTiempoAtrapado() { return tiempoAtrapado; }

    // Incrementa el contador de atrapado solo si el guardia está efectivamente atrapado
    public void incrementarTiempoAtrapado() {
        if (estado == Comportamiento.ATRAPADO) {
            tiempoAtrapado++;
        }
    }

    public static int getTiempoEscape() { return TIEMPO_ESCAPE; }
    public static int getTiempoEsperaEscape() { return TIEMPO_ESPERA_ESCAPE; }
    public boolean isSaliendo() { return estado == Comportamiento.SALIENDO; }

    // Inicia el estado de reanimación tras reaparecer: el guardia se queda quieto un tiempo
    public void reanimar() {
        estado = Comportamiento.REANIMACION;
        tiempoAtrapado = 0;
        tiempoReanimacion = 0;
    }

    // Fuerza el cambio al estado de persecución
    public void cambiarAPersecucion() {
        estado = Comportamiento.PERSEGUIR;
    }

    // Retorna true si el guardia está actualmente persiguiendo al héroe
    public boolean isPersiguiendo() {
        return estado == Comportamiento.PERSEGUIR;
    }
}
