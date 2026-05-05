import java.util.ArrayList;

public class Main{
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
        }
    }
}



