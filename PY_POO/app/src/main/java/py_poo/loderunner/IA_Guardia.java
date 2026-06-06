package py_poo.loderunner;

import java.util.Random;

public class IA_Guardia {
    private Random rand; // generador de números aleatorios para decisiones
    private int direccionPreferida; // dirección preferida al vagar: 1 derecha, -1 izquierda
    private int contadorCambio; // contador de frames para cambios de comportamiento
    private static final int CAMBIO_CADENCIA = 60; // frames entre cambios de dirección
    private static final int CAMPO_VISION = 200; // píxeles de alcance para detectar al héroe

    public enum Comportamiento {
        PERSEGUIR, VAGAR, ATRAPADO, REAPARECER, REANIMACION // estados possibles de la IA
    }

    private Comportamiento estado; // estado actual del comportamiento
    private int tiempoAtrapado; // tiempo que lleva atrapado en un agujero
    private int tiempoReanimacion; // tiempo de reanimación tras reaparecer
    private static final int TIEMPO_MAX_ATRAPADO = 120; // frames máximos atrapado antes de reaparecer
    private static final int TIEMPO_REANIMACION = 90; // frames de reanimación tras respawn

    public IA_Guardia() {
        this.rand = new Random(); // inicializa generador aleatorio
        this.direccionPreferida = rand.nextBoolean() ? 1 : -1; // elige dirección inicial aleatoria
        this.contadorCambio = 0; // empieza contador en cero
        this.estado = Comportamiento.VAGAR; // comienza vagando
    }

    // calcula el movimiento del guardia según su estado y posición del héroe
    // parámetros: posiciones del guardia y héroe, booleanos de movimiento posible, estado en escalera/barra
    // retorna: -1 izquierda, 1 derecha, -2 arriba, 2 abajo, 0 quieto
    public int calcularMovimiento(int guardiaX, int guardiaY, int heroeX, int heroeY,
                                   boolean puedeIzq, boolean puedeDer,
                                   boolean puedeSubir, boolean puedeBajar,
                                   boolean enEscalera, boolean enBarra) {
        contadorCambio++; // incrementa contador de frames

        boolean heroeEnRango = Math.abs(guardiaX - heroeX) < CAMPO_VISION // verifica si el héroe está visible
                            && Math.abs(guardiaY - heroeY) < CAMPO_VISION;

        switch (estado) {
            case ATRAPADO:
                tiempoAtrapado++; // cuenta frames atrapado
                if (tiempoAtrapado >= TIEMPO_MAX_ATRAPADO) {
                    estado = Comportamiento.REAPARECER; // cambia a reaparecer tras tiempo límite
                }
                return 0; // quieto mientras atrapado

            case REAPARECER:
                return 0; // quieto mientras reaparece

            case REANIMACION:
                tiempoReanimacion++; // cuenta frames de reanimación
                if (tiempoReanimacion >= TIEMPO_REANIMACION) {
                    estado = Comportamiento.VAGAR; // termina reanimación, empieza a vagar
                }
                return 0; // quieto mientras se reanima

            case PERSEGUIR:
                if (!heroeEnRango && contadorCambio % CAMBIO_CADENCIA == 0 && rand.nextDouble() < 0.7) {
                    estado = Comportamiento.VAGAR; // pierde interés y vuelve a vagar
                }
                if (estado == Comportamiento.VAGAR) {
                    return calcularVagar(guardiaX, guardiaY, heroeX, heroeY, // si cambió a vagar, calcula movimiento
                                        puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                        enEscalera, enBarra);
                }
                return calcularPersecucion(guardiaX, guardiaY, heroeX, heroeY, // persigue al héroe
                                           puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                           enEscalera, enBarra);

            case VAGAR:
            default:
                if (heroeEnRango) {
                    estado = Comportamiento.PERSEGUIR; // detecta al héroe y empieza a perseguir
                } else if (contadorCambio % CAMBIO_CADENCIA == 0 && rand.nextDouble() < 0.3) {
                    estado = Comportamiento.PERSEGUIR; // cambio probabilístico a persecución
                }
                if (estado == Comportamiento.PERSEGUIR) {
                    return calcularPersecucion(guardiaX, guardiaY, heroeX, heroeY,
                                              puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                              enEscalera, enBarra);
                }
                return calcularVagar(guardiaX, guardiaY, heroeX, heroeY, // calcula movimiento errático
                                     puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                     enEscalera, enBarra);
        }
    }

    // calcula movimiento de persecución hacia el héroe
    private int calcularPersecucion(int gx, int gy, int hx, int hy,
                                     boolean izq, boolean der,
                                     boolean subir, boolean bajar,
                                     boolean enEscalera, boolean enBarra) {
        if (enEscalera || enBarra) { // prioriza movimiento vertical si está en escalera/barra
            if (hy < gy && subir) return -2;
            if (hy > gy && bajar) return 2;
        }

        if (Math.abs(gx - hx) > Math.abs(gy - hy)) { // más lejos horizontalmente
            if (hx < gx && izq) return -1;
            if (hx > gx && der) return 1;
        } else { // más lejos verticalmente
            if (hy < gy && subir) return -2;
            if (hy > gy && bajar) return 2;
        }

        if (izq) return -1; // movimiento por defecto hacia izquierda
        if (der) return 1; // movimiento por defecto hacia derecha
        return 0; // no puede moverse
    }

    // calcula movimiento aleatorio de vago
    private int calcularVagar(int gx, int gy, int hx, int hy,
                               boolean izq, boolean der,
                               boolean subir, boolean bajar,
                               boolean enEscalera, boolean enBarra) {
        if (contadorCambio % CAMBIO_CADENCIA == 0) {
            direccionPreferida = rand.nextBoolean() ? 1 : -1; // cambia dirección periódicamente
        }

        if (enEscalera || enBarra) { // movimiento vertical aleatorio en escalera/barra
            if (rand.nextBoolean() && subir) return -2;
            if (rand.nextBoolean() && bajar) return 2;
        }

        if (direccionPreferida > 0 && der) return 1; // intenta ir en dirección preferida
        if (direccionPreferida < 0 && izq) return -1;

        if (der) return 1; // cae a derecha si puede
        if (izq) return -1; // cae a izquierda si puede
        return 0;
    }

    public Comportamiento getEstado() { return estado; } // retorna el estado actual de la IA
    public void setEstado(Comportamiento e) { this.estado = e; } // asigna un estado a la IA

    public void atrapar() { // marca al guardia como atrapado
        estado = Comportamiento.ATRAPADO;
        tiempoAtrapado = 0; // reinicia contador de atrapado
    }

    public void reaparecer() { // guardia queda libre después de escapar del agujero
        estado = Comportamiento.VAGAR;
        tiempoAtrapado = 0; // reinicia contador de atrapado
    }

    public void reanimar() { // guardia vuelve a su spawn con tiempo de reanimación
        estado = Comportamiento.REANIMACION;
        tiempoAtrapado = 0;
        tiempoReanimacion = 0; // reinicia contador de reanimación
    }

    public void cambiarAPersecucion() { // fuerza cambio a estado persecución
        estado = Comportamiento.PERSEGUIR;
    }

    public boolean isPersiguiendo() { // retorna true si está en persecución
        return estado == Comportamiento.PERSEGUIR;
    }
}
