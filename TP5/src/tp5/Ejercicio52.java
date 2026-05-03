package tp5;

import java.util.ArrayList;

public class Ejercicio52 {
    public static void main(String[] args) {
        ArrayList<Multiplicable> lista = new ArrayList<>();
        lista.add(new NumeroEntero(10));
        lista.add(new MiVector(new int[]{1, 2, 3}));
        lista.add(new MiMatriz(new int[][]{{1, 2}, {3, 4}}));

        int factor = 2;
        System.out.println("Multiplicando todos los elementos por: " + factor + "\n");

        for (Multiplicable m : lista) {
        m.multiplicar(factor);    
        m.mostrarResultado();     
        }
    }
}