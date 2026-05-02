public class Main {

        public static void main(String[] args) {

            mazo mazo = new mazo();

            // creamos naipes — cada new Naipe() actualiza ultimoNaipe
            naipe n1 = new naipe(1,  "Espadas");
            System.out.println("Último creado: " + naipe.getUltimoNaipe());

            naipe n2 = new naipe(7,  "Copas");
            System.out.println("Último creado: " + naipe.getUltimoNaipe());

            naipe n3 = new naipe(3,  "Oros");
            System.out.println("Último creado: " + naipe.getUltimoNaipe());

            naipe n4 = new naipe(12, "Bastos");
            System.out.println("Último creado: " + naipe.getUltimoNaipe());

            // agregamos todos al mazo
            mazo.agregarNaipe(n1);
            mazo.agregarNaipe(n2);
            mazo.agregarNaipe(n3);
            mazo.agregarNaipe(n4);

            // mostramos el mazo antes de mezclar
            System.out.println("\nAntes de mezclar:");
            System.out.println(mazo);

            // mezclamos
            mazo.mezclar();

            // mostramos el mazo después de mezclar
            System.out.println("Después de mezclar:");
            System.out.println(mazo);
        }
    }

