package py_poo.engine;

import py_poo.core.Constantes;
import py_poo.entities.ObjetoGrafico;
import py_poo.loderunner.Nivel;

// La clase Cámara actúa como un "camarógrafo" virtual.
// Su trabajo es calcular qué parte de un nivel gigante se debe dibujar en la pantalla,
// manteniendo siempre al jugador (en este caso el Lode Runner) en el centro de la visión.
public class Camara {


    // Estas coordenadas (X, Y) son los limites de la camara
    // Todo lo que quede fuera de este rango de visión, no se dibuja.
    private int X;
    private int Y;



    public int getX() { return X; }
    public int getY() { return Y; }




    public void seguirJugador(ObjetoGrafico obj, Nivel nivel) {

        // Centrar la cámara en el jugador
        // Fórmula estándar de 2D: Posición del jugador - (Mitad de la pantalla) + (Mitad del jugador).
        // Esto asegura que el centro del personaje quede exactamente en el centro matemático de la ventana.
        int objetivoX = (int)obj.getX() - Constantes.WIDTH / 2 + obj.getWidth() / 2;
        int objetivoY = (int)obj.getY() - Constantes.HEIGHT / 2 + obj.getHeight() / 2;

        //  Límites de la cámara
        // Evitamos que la cámara muestre el "vacío" negro exterior si el jugador llega a los bordes del nivel.
        if (nivel != null) {

            // Calculamos hasta dónde puede llegar la cámara antes de chocar con el límite derecho o inferior.
            // Usamos Math.max(0, ...) como seguro por si el nivel llegara a ser más chico que la pantalla.
            int maxX = Math.max(0, nivel.getAnchoPixels() - Constantes.WIDTH);
            int maxY = Math.max(0, nivel.getAltoPixels() - Constantes.HEIGHT);

            // Aplicamos los topes matemáticos:
            // Math.max(0, ...) evita que la cámara pase hacia la izquierda o arriba (números negativos).
            // Math.min(..., maxX) evita que la cámara se pase de largo hacia la derecha o abajo.
            objetivoX = Math.max(0, Math.min(objetivoX, maxX));
            objetivoY = Math.max(0, Math.min(objetivoY, maxY));
        }

        // 3. Actualizamos la posición oficial de la cámara con los valores ya filtrados y seguros.
        X = objetivoX;
        Y = objetivoY;
    }
}