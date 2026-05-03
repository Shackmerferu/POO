package tp5;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Inicio de la ejecución ---");

        // Instanciamos usando diferentes constructores (Polimorfismo Estático)
        
        // Polimorfismo: variable de tipo padre, instancia de tipo hijo
        VideoJuego juego1 = new Pong("Retro Pong", 1500.0, (short) 3);
        VideoJuego juego2 = new Dados("Dados Pro", 500.0, (short) 0);
        VideoJuego juego3 = new PiedraPapelTijeras("Jan-Ken-Pon", 0.0, (short) 3);

        System.out.println("\n--- Estado de los objetos ---");
        System.out.println(juego1.toString());
        System.out.println(juego2.toString());
        System.out.println(juego3.toString());

        System.out.println("\n--- Acciones ---");
        juego1.jugar();
        juego2.jugar();
        juego3.jugar();

        System.out.println("\n--- Reporte Final ---");
        System.out.println("Videojuegos totales: " + VideoJuego.getCantidadDeVideoJuegosCreados());
    }
}