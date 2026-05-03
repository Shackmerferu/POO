package tp1;

import java.util.Scanner;
import java.util.Vector;


public class Adivinanza {
    public static void main() {
        int intento;
        int max = 7;
        int cout = 0;
        boolean encontrado=true;

        Ramdom r = new Ramdom();
        int Nroadivinar = r.generarNumero();
        try (Scanner sc = new Scanner(System.in)) {
            Vector<Integer> intentos = new Vector<>();
            System.out.println("Adivine el numero");
            while (encontrado==true||cout<max) {
                
                
                System.out.println("ingrese un numero");
                intento = sc.nextInt();
                intentos.add(intento);
                cout++;
                
                if (intento == Nroadivinar) {
                    System.out.println("Adivinaste Capo!");
                    if (cout < 5) {
                        System.out.println("Maestro de la Adivinanza");
                    }
                    encontrado=false;
                }
                else if(intento<Nroadivinar){
                    System.out.println("el numero es mayor");
                }
                else {
                    System.out.println("el numero es menor");
                }
                
                
            }
            System.out.println("Intentos realizados:");
            for(int n : intentos){
                System.out.println(n);
            }   }
    }
}