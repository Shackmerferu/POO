package py_poo.pong;

import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.entities.ObjetoGrafico;


public class PelotaPong extends ObjetoGrafico {


    private double dx;
    private double dy;
    private double velocidadBase;

    //  CONSTRUCTOR
    public PelotaPong() {
        super("imagenes/Pong/Pelota Pong.png");
        this.velocidadBase = 3.5;

        // Forzamos la "caja de colisión" a ser de 16x16 píxeles, aunque el dibujo original sea de 130x130.
        // Esto es clave para que los choques se sientan justos y precisos.
        setDimension(new java.awt.Dimension(16, 16));
        reiniciar(); // Coloca la pelota en el centro al nacer
    }

    // Se ejecuta constantemente para actualizar la posición de la pelota
    public void mover() {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    // Rebote simple contra el Techo y el Piso
    public void rebotarParedes() {
        // Choca contra el Techo
        if (getY() <= 0) {
            setY(0);     // La despega de la pared por si se queda trabada
            dy = -dy;    // Invierte su dirección vertical (si subía, ahora baja)
        }
        // Choca contra el Piso
        if (getY() >= Constantes.HEIGHT - getHeight()) {
            setY(Constantes.HEIGHT - getHeight());
            dy = -dy;    // Invierte su dirección
        }
    }


    public void rebotarPaleta(Paleta p) {
        // Calculamos los centros exactos de la pelota y la paleta
        double centroPelota = getY() + getHeight() / 2.0;
        double centroPaleta = p.getY() + p.getHeight() / 2.0;

        //  Vemos a qué distancia del centro de la paleta pegó la pelota
        double diferencia = centroPelota - centroPaleta;

        //  Dividimos la paleta (que mide 100px) en 8 "segmentos" o pedacitos
        double alturaSegmento = p.getHeight() / 8.0;

        // Matemáticas para saber en cuál de los 8 pedacitos golpeó (del 0 al 7)
        int segmento = (int) ((diferencia + p.getHeight() / 2.0) / alturaSegmento);
        if (segmento < 0) segmento = 0; // Seguridad para no salirnos del límite
        if (segmento > 7) segmento = 7;

        //  Asignamos un ángulo de rebote según el segmento golpeado.
        // Si pega en las puntas rebota muy inclinado (80 o -80 grados). Si pega al medio rebota casi recto (10 o -10 grados).
        double[] angulosGrados = {-80, -55, -35, -10, 10, 35, 55, 80};


        double anguloRad = Math.toRadians(angulosGrados[segmento]);

        //  Calculamos la velocidad actual total usando el Teorema de Pitágoras (a² + b² = c²)
        double speed = Math.max(velocidadBase, Math.sqrt(dx * dx + dy * dy));

        //  Averiguamos para qué lado tiene que salir disparada (izquierda o derecha)
        int direccion = dx > 0 ? -1 : 1;

        //  Aplicamos trigonometría pura para darle su nueva trayectoria
        dx = direccion * speed * Math.cos(anguloRad);
        dy = speed * Math.sin(anguloRad);


        if (dx > 0) { // Si sale hacia la derecha
            setX(p.getX() + p.getWidth());
        } else {      // Si sale hacia la izquierda
            setX(p.getX() - getWidth());
        }

        // 9. Cada vez que hay un pelotazo, la pelota se vuelve más rápida
        aumentarVelocidad();
    }

    // --- CONDICIONES DE PUNTO (Salida de la pantalla) ---
    public boolean salioIzquierda() {
        return getX() + getWidth() < 0;
    }

    public boolean salioDerecha() {
        return getX() > Constantes.WIDTH;
    }

    // --- INCREMENTO DE DIFICULTAD ---
    public void aumentarVelocidad() {
        double factor = 1.05; // Aumenta la velocidad un 5% en cada golpe
        double maxVel = 8.0;  // Límite máximo para que la pelota no atraviese paredes por ir tan rápido

        if (Math.abs(dx) < maxVel) {
            dx *= factor;
        }
        if (Math.abs(dy) < maxVel) {
            dy *= factor;
        }
    }

    // --- RENDERIZADO (Dibujo) ---
    @Override
    public void display(Graphics g) {
        if (sprite != null) {
            // Dibuja la imagen achicada al tamaño lógico (16x16)
            g.drawImage(sprite, (int) getX(), (int) getY(), getWidth(), getHeight(), null);
        }
    }

    // --- SOBRECARGA DE MÉTODOS (Polimorfismo estático) ---

    // Versión 1: Saque inicial al azar
    public void reiniciar() {
        // Centrar en pantalla
        setX(Constantes.WIDTH / 2.0 - getWidth() / 2.0);
        setY(Constantes.HEIGHT / 2.0 - getHeight() / 2.0);
        // Math.random() < 0.5 es como tirar una moneda: 50% chance de ir a izquierda, 50% a derecha
        dx = Math.random() < 0.5 ? velocidadBase : -velocidadBase;
        dy = 0; // Sale recta, sin inclinación vertical
    }

    // Versión 2: Saque dirigido al que acaba de perder el punto (Regla oficial de Pong)
    public void reiniciar(boolean haciaLaDerecha) {
        setX(Constantes.WIDTH / 2.0 - getWidth() / 2.0);
        setY(Constantes.HEIGHT / 2.0 - getHeight() / 2.0);
        // Usa el parámetro booleano para forzar la dirección del saque
        dx = haciaLaDerecha ? velocidadBase : -velocidadBase;
        dy = 0;
    }
}