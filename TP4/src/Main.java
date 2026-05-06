//import java.util.Scanner;

public class Main {
     public static void main(String[] args){
       /* 
        Prueba p = new Prueba();  
        p.mensaje();
        */
        /*
        Scanner sc = new Scanner(System.in);
        Catalogo catalogo = new Catalogo();

        int opcion;

        do {
            System.out.println("\n1. Agregar");
            System.out.println("2. Listar");
            System.out.println("3. Eliminar");
            System.out.println("0. Salir");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {

                case 1:
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();

                    System.out.print("Precio: ");
                    double precio = sc.nextDouble();
                    sc.nextLine();

                    Categoria cat = new Categoria("General", "Default");

                    VideoJuego v = new VideoJuego(nombre,precio, cat);
                    catalogo.agregar(v);
                    break;

                case 2:
                    catalogo.mostrar();
                    break;

                case 3:
                    System.out.print("Nombre a eliminar: ");
                    String eliminar = sc.nextLine();
                    catalogo.eliminar(eliminar);
                    break;
            }

        } while (opcion != 0);

        sc.close();
    }
     */
      
        // Crear mazo
        Mazo mazo = new Mazo();

        // Crear naipes
        Naipe n1 = new Naipe("Espadas", 7);
        Naipe n2 = new Naipe("Oro", 1);
        Naipe n3 = new Naipe("Copas", 12);
        Naipe n4 = new Naipe("Bastos", 3);

        // Agregar al mazo
        mazo.agregar(n1);
        mazo.agregar(n2);
        mazo.agregar(n3);
        mazo.agregar(n4);

        // Mostrar mazo original
        System.out.println("Mazo original:");
        System.out.println(mazo);

        // Mostrar último naipe creado
        System.out.println("Último naipe creado:");
        System.out.println(Naipe.ultiCard());

        // Mezclar mazo
        mazo.mezclar();

        // Mostrar mazo mezclado
        System.out.println("Mazo mezclado:");
        System.out.println(mazo);
        
    }  
}
