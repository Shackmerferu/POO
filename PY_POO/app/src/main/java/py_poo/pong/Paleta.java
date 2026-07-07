package py_poo.pong;

import java.awt.Color;
import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.interfaces.Movible;


public class Paleta extends ObjetoGrafico implements Movible {


    private int velocidad = 3;       // Píxeles que se mueve por cada fotograma (frame)
    private InputManager input;      // Referencia al teclado para saber qué presiona el usuario
    private int idJugador;           // Identificador: 1 (Izquierda) o 2 (Derecha)

    //  CONSTRUCTOR
    public Paleta(InputManager input, int idJugador) {

        // para cargar una imagen distinta dependiendo de si es el Jugador 1 o 2.
        super(idJugador == 1 ? "imagenes/Pong/Paleta 1.png" : "imagenes/Pong/Paleta 2.png");

        this.input = input;
        this.idJugador = idJugador;

        // Forzamos el tamaño de la caja de colisión a 20 píxeles de ancho por 100 de alto.
        // Esto es vital para que la pelota rebote bien, sin importar el tamaño original del .png.
        bounds.setSize(20, 100);
    }

    //  MOVIMIENTO
    @Override
    public void Mover() {
        int direccionY = 0; // 0 significa quieto. -1 es arriba. 1 es abajo.

        // 1. Detectar teclas del Jugador 1 (W y S)
        if (idJugador == 1) {
            if (input.isWPressed()) {
                direccionY = -1; // En Java 2D, restar Y significa "subir" hacia la coordenada 0
            }
            if (input.isSPressed()) {
                direccionY = 1;  // Sumar Y significa "bajar"
            }
        }

        // 2. Detectar teclas del Jugador 2 (Flecha Arriba y Abajo)
        if (idJugador == 2) {
            if (input.isUpPressed()) {
                direccionY = -1;
            }
            if (input.isDownPressed()) {
                direccionY = 1;
            }
        }

        // 3. Aplicar el movimiento y chocar contra las paredes
        if (direccionY != 0) { // Solo si el jugador tocó una tecla...
            // Calculamos en qué coordenada quedaría la paleta en el próximo milisegundo
            int nuevaY = (int) (getY() + (direccionY * velocidad));

            // FÍSICA DE COLISIÓN (Techo y Piso):
            // nuevaY >= 0 -> Evita que se salga por arriba de la pantalla.
            // nuevaY <= (AlturaTotal - AlturaPaleta) -> Evita que se salga por abajo de la pantalla.
            if (nuevaY >= 0 && nuevaY <= (Constantes.HEIGHT - getHeight())) {
                setY(nuevaY); // Si el movimiento es válido, actualizamos la posición real
            }
        }
    }

    // posicionamiento de arranque
    // Coloca las paletas en sus lugares al empezar el juego o después de cada punto.
    public void ResetearPOS() {
        if (idJugador == 1) {
            setX(30); // Jugador 1: Pegado a la pared izquierda (con 30px de margen)
        } else {
            // Jugador 2: Ancho total de la pantalla, menos 30px de margen, menos el propio ancho de la paleta
            setX(Constantes.WIDTH - 30 - getWidth());
        }

        // Centrar verticalmente:
        // Mitad de la pantalla menos la mitad de la paleta (fórmula estándar para centrar en 2D)
        setY(Constantes.HEIGHT / 2.0 - getHeight() / 2.0);
    }

    // -
    public void dibujar(Graphics g) {
        //  Si la imagen se cargó bien desde la carpeta "imagenes/Pong/"
        if (sprite != null) {
            // Dibuja la imagen escalada a la fuerza a nuestro tamaño lógico (20x100)
            g.drawImage(sprite, (int) getX(), (int) getY(), getWidth(), getHeight(), null);
        }

        // Si borraste el .png sin querer, el juego no crashea, simplemente dibuja un rectángulo blanco.
        else {
            g.setColor(Color.WHITE);
            g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
        }
    }
}