import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        System.out.println("--- Creando el vector de Juegos ---");

        // Creamos un vector (array) de tipo VideoJuego con espacio para 2 elementos
        VideoJuego[] vectorJuegos = new VideoJuego[2];

        // Llenamos el vector con instancias de las subclases concretas
        vectorJuegos[0] = new Dados("Simulador de Dados", 0, "CasinoDev", "18+");
        vectorJuegos[1] = new PiedraPapelTijera("PPT Championship", 5 , "Indie", "E");

        System.out.println("\n--- Jugando y mostrando resultados ---");

        // Recorremos el vector con un bucle for-each
        for (VideoJuego juegoActual : vectorJuegos) {
            // El polimorfismo en acción: cada objeto ejecuta su propia versión de jugar()
            juegoActual.jugar();

            // Todos comparten el método mostrarResultado() heredado de VideoJuego
            juegoActual.mostrarResultado();
        }
    }
}

