import java.util.ArrayList;

public class main{
    public static void main(String[] args) {
    /* 
        Pong pong = new Pong();
        PiedraPapelTijera ppt = new PiedraPapelTijera();
        Dados dados = new Dados();

        System.out.println("----- Pong -----");
        System.out.println(pong.getNombre());
        System.out.println(pong);
        pong.jugar();

        System.out.println("----- Piedra Papel Tijera -----");
        System.out.println(ppt.getNombre());
        System.out.println(ppt);
        ppt.jugar();

        System.out.println("----- Dados -----");
        System.out.println(dados.getNombre());
        System.out.println(dados);
        dados.jugar();*/
        /*
         ArrayList<Multiplicable> lista = new ArrayList<>();

      
        NumeroEntero num = new NumeroEntero(5);

        int[] vec = {1, 2, 3};
        MiVector vector = new MiVector(vec);

        int[][] mat = {
            {1, 2},
            {3, 4}
        };
        MiMatriz matriz = new MiMatriz(mat);

      
        lista.add(num);
        lista.add(vector);
        lista.add(matriz);

      
        for (Multiplicable obj : lista) {
            obj.multiplicar(2);
            obj.mostrarResultado();
            System.out.println("-----");
        }*/
        System.out.println("\n--- Iniciando Dados ---");
        VideoJuego misDados = new Dados(); // Polimorfismo: subclase en variable de superclase
        misDados.jugar();
        System.out.print("El dado muestra: ");
        misDados.mostrarResultado();
        System.out.println("Detalles: " + misDados.toString());

        // 3. PRUEBA DE LA CLASE PIEDRA, PAPEL O TIJERA
        System.out.println("\n--- Iniciando Piedra, Papel o Tijera ---");
        VideoJuego miPPT = new PiedraPapelTijera();
        miPPT.jugar();
        System.out.print("La jugada fue: ");
        miPPT.mostrarResultado();
        System.out.println("Detalles: " + miPPT.toString());

        // 4. VERIFICACIÓN DE ATRIBUTOS COMUNES
        System.out.println("\n--- Resumen de Sesión ---");
        System.out.println("Juego 1: " + misDados.getNombre());
        System.out.println("Juego 2: " + miPPT.getNombre());
    
        }
}



