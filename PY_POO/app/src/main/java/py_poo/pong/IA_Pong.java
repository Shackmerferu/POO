package py_poo.pong;

import py_poo.core.Constantes;

public class IA_Pong {
    private int dificultad;
    private PelotaPong pelota;
    private Paleta paleta;
    private int margenError;
    private double velocidad;
    private int puntosRonda;
    private int margenMinimo;

    public IA_Pong(PelotaPong pelota, Paleta paleta, int dificultad) {
        this.pelota = pelota;
        this.paleta = paleta;
        this.puntosRonda = 0;
        setDificultad(dificultad);
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
        velocidad = 3; // arranca igual que el jugador y luego aumenta con el puntaje
        switch (dificultad) {
            case 1: margenError = 30; margenMinimo = 15; break; // facil
            case 2: margenError = 15; margenMinimo = 3;  break; // medio
            case 3: margenError = 5;  margenMinimo = 1;  break; // dificil
            default: margenError = 15; margenMinimo = 3;
        }
    }

    public void incrementarDificultad() { // se vuelve mas precisa y rapida por cada punto
        puntosRonda++;
        margenError = Math.max(margenMinimo, margenError - 1); // reduce margen de error
        if (velocidad < 6.5 && puntosRonda % 2 == 0) {
            velocidad += 0.5; // aumenta velocidad hasta 6.5
        }
    }

    public void calcularMovimiento() {
        if (pelota == null || paleta == null) return;

        double centroPelota = pelota.getY() + pelota.getHeight() / 2.0;
        double centroPaleta = paleta.getY() + paleta.getHeight() / 2.0;

        if (centroPelota < centroPaleta - margenError) {
            paleta.setY(paleta.getY() - velocidad);
        } else if (centroPelota > centroPaleta + margenError) {
            paleta.setY(paleta.getY() + velocidad);
        }

        if (paleta.getY() < 0) paleta.setY(0);
        if (paleta.getY() > Constantes.HEIGHT - paleta.getHeight()) paleta.setY(Constantes.HEIGHT - paleta.getHeight());
    }
}
