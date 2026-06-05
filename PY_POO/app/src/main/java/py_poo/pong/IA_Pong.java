package py_poo.pong;

import py_poo.core.Constantes;

public class IA_Pong {


    private int dificultad;
    private PelotaPong pelota;
    private Paleta paleta;

    // A mayor margen de error, más tonta y lenta de reflejos es la IA.
    private int margenError;
    private double velocidad;   // Qué tan rápido se puede mover la paleta de la computadora
    private int puntosRonda;    // Contador para saber cuántos puntos van y adaptar la dificultad

   // para que no sera imposible de ganar
    private int margenMinimo;

    // CONSTRUCTOR
    public IA_Pong(PelotaPong pelota, Paleta paleta, int dificultad) {
        this.pelota = pelota;
        this.paleta = paleta;
        this.puntosRonda = 0;       // Arranca el partido en 0
        setDificultad(dificultad);  // Configura sus habilidades iniciales
    }

    //  CONFIGURACIÓN DE NIVELES
    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
        // Dependiendo del nivel, ajustamos los reflejos y la velocidad física
        switch (dificultad) {
            case 1: // FÁCIL: Lenta y con mucho margen de error (50px de punto ciego)
                velocidad = 2.5; margenError = 50; margenMinimo = 20; break;
            case 2: // MEDIO: Más rápida y más atenta
                velocidad = 2.9; margenError = 30; margenMinimo = 10; break;
            case 3: // DIFÍCIL: Rápida y casi sin margen de error
                velocidad = 3.3; margenError = 10; margenMinimo = 3;  break;
            default: // Por defecto arranca en Fácil por seguridad
                velocidad = 2.5; margenError = 50; margenMinimo = 20;
        }
    }

    // Este método se llama cada vez que alguien hace un punto (en JuegoPong.java).
    public void incrementarDificultad() {
        puntosRonda++; // Registra que hubo un punto

        // Cada 1 punto (% 1 == 0), la IA reduce su margen de error en 1 pixel (se vuelve más precisa).
        // Math.max evita que el error baje del "margenMinimo", para que nunca sea 100% invencible.
        if (puntosRonda % 1 == 0) {
            margenError = Math.max(margenMinimo, margenError - 1);
        }

        // Cada 1 punto, la IA también se vuelve un poquito más rápida moviéndose.
        // El límite máximo de velocidad es 4.5 para que no rompa la física del juego.
        if (puntosRonda % 1 == 0 && velocidad < 4.5) {
            velocidad += 0.2;
        }
    }

    //
    public void calcularMovimiento() {
        // Medida de seguridad: Si no hay pelota o paleta, no hace nada para no crashear
        if (pelota == null || paleta == null) return;

        // 1. Calcular dónde está el centro exacto de la pelota y de la paleta
        double centroPelota = pelota.getY() + pelota.getHeight() / 2.0;
        double centroPaleta = paleta.getY() + paleta.getHeight() / 2.0;

        // 2. Tomar decisiones de movimiento
        // Si la pelota está por encima de la paleta (teniendo en cuenta el punto ciego) -> Sube
        if (centroPelota < centroPaleta - margenError) {
            paleta.setY(paleta.getY() - velocidad); // Restar Y mueve hacia arriba
        }
        // Si la pelota está por debajo de la paleta -> Baja
        else if (centroPelota > centroPaleta + margenError) {
            paleta.setY(paleta.getY() + velocidad); // Sumar Y mueve hacia abajo
        }
        // (Si no se cumple ninguna, significa que la pelota está centrada y la IA se queda quieta)

        // 3. Colisiones con los bordes de la pantalla (Para que la IA no se salga de la cancha)
        // Techo
        if (paleta.getY() < 0) {
            paleta.setY(0);
        }
        // Piso (Alto de la pantalla menos el alto de la paleta)
        if (paleta.getY() > Constantes.HEIGHT - paleta.getHeight()) {
            paleta.setY(Constantes.HEIGHT - paleta.getHeight());
        }
    }
}