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
        switch (dificultad) {
            case 1: velocidad = 2.5; margenError = 50; margenMinimo = 20; break;
            case 2: velocidad = 2.9; margenError = 30; margenMinimo = 10; break;
            case 3: velocidad = 3.3; margenError = 10; margenMinimo = 3;  break;
            default: velocidad = 2.5; margenError = 50; margenMinimo = 20;
        }
    }

    public void incrementarDificultad() {
        puntosRonda++;
        if (puntosRonda % 1 == 0) {
            margenError = Math.max(margenMinimo, margenError - 1);
        }
        if (puntosRonda % 1 == 0 && velocidad < 4.5) {
            velocidad += 0.2;
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
