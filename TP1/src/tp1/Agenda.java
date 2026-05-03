package tp1;

import java.util.HashMap;
import java.util.Scanner;

public class Agenda {
    public static void main(String[] args) {

        HashMap<String, Contacto> guia = new HashMap<>();
        try (Scanner sc = new Scanner(System.in)) {
            guia.put("Juan", new Contacto("Juan", "1111"));
            guia.put("Ana", new Contacto("Ana", "2222"));
            guia.put("Pedro", new Contacto("Pedro", "3333"));
            guia.put("Maria", new Contacto("Maria", "4444"));
            guia.put("Luis", new Contacto("Luis", "5555"));
            
            
            boolean salir = true;
            while (salir) {
                
                System.out.println("1 - Buscar contacto");
                System.out.println("2 - Agregar contacto");
                System.out.println("3 - Salir");
                int opcion = sc.nextInt();
                sc.nextLine();
                switch (opcion) {
                    case 1 ->                     {
                        System.out.println("Ingrese nombre:");
                        String nombre = sc.nextLine();
                        Contacto c = guia.get(nombre);
                        if (c != null) {
                            System.out.println(c.getNombre() + " - " + c.getNumero());
                        } else {
                            System.out.println("Contacto no encontrado");
                        }                          }
                    case 2 ->                     {
                        System.out.println("Nombre:");
                        String nombre = sc.nextLine();
                        System.out.println("Telefono:");
                        String telefono = sc.nextLine();
                        guia.put(nombre, new Contacto(nombre, telefono));
                        System.out.println("Contacto agregado");
                    }
                    case 3 -> {
                        salir = false;
                        System.out.println("Programa finalizado");
                    }
                    default -> {
                    }
                }
            }
        }
        }
    }



