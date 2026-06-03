package py_poo.loderunner;

import java.util.Random;

public class IA_Guardia {
    private Random rand;
    private int direccionPreferida;
    private int contadorCambio;
    private static final int CAMBIO_CADENCIA = 60;

    public enum Comportamiento {
        PERSEGUIR, VAGAR, ATRAPADO, REAPARECER
    }

    private Comportamiento estado;
    private int tiempoAtrapado;
    private static final int TIEMPO_MAX_ATRAPADO = 120;

    public IA_Guardia() {
        this.rand = new Random();
        this.direccionPreferida = rand.nextBoolean() ? 1 : -1;
        this.contadorCambio = 0;
        this.estado = Comportamiento.VAGAR;
    }

    public int calcularMovimiento(int guardiaX, int guardiaY, int heroeX, int heroeY,
                                   boolean puedeIzq, boolean puedeDer,
                                   boolean puedeSubir, boolean puedeBajar,
                                   boolean enEscalera, boolean enBarra) {
        contadorCambio++;

        switch (estado) {
            case ATRAPADO:
                tiempoAtrapado++;
                if (tiempoAtrapado >= TIEMPO_MAX_ATRAPADO) {
                    estado = Comportamiento.REAPARECER;
                }
                return 0;

            case REAPARECER:
                return 0;

            case PERSEGUIR:
                return calcularPersecucion(guardiaX, guardiaY, heroeX, heroeY,
                                           puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                           enEscalera, enBarra);

            case VAGAR:
            default:
                return calcularVagar(guardiaX, guardiaY, heroeX, heroeY,
                                     puedeIzq, puedeDer, puedeSubir, puedeBajar,
                                     enEscalera, enBarra);
        }
    }

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

    public void atrapar() {
        estado = Comportamiento.ATRAPADO;
        tiempoAtrapado = 0;
    }

    public void reaparecer() {
        estado = Comportamiento.VAGAR;
        tiempoAtrapado = 0;
    }

    public void cambiarAPersecucion() {
        estado = Comportamiento.PERSEGUIR;
    }

    public boolean isPersiguiendo() {
        return estado == Comportamiento.PERSEGUIR;
    }
}
