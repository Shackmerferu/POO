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
        }
        System.out.println("\n--- Iniciando Dados ---");
        VideoJuego misDados = new Dados(); 
        misDados.jugar();
        System.out.print("El dado muestra: ");
        misDados.mostrarResultado();
        System.out.println("Detalles: " + misDados.toString());

        System.out.println("\n--- Iniciando Piedra, Papel o Tijera ---");
        VideoJuego miPPT = new PiedraPapelTijera();
        miPPT.jugar();
        System.out.print("La jugada fue: ");
        miPPT.mostrarResultado();
        System.out.println("Detalles: " + miPPT.toString());

     
        System.out.println("\n--- Resumen de Sesión ---");
        System.out.println("Juego 1: " + misDados.getNombre());
        System.out.println("Juego 2: " + miPPT.getNombre());
    */
   // --- 1. CREACIÓN DE CLIENTES (Asociación) ---
        // Primero creamos a las personas, porque las cuentas necesitan un titular
        Cliente cliente1 = new Cliente("Luis Antonio", "Funes", "Calle Falsa 123");
        Cliente cliente2 = new Cliente("Ana", "García", "Avenida Siempre Viva 742");

        // --- 2. INSTANCIACIÓN DE CUENTAS (Polimorfismo) ---
        // Nota: No podemos hacer "new Cuenta", pero sí "new CajaAhorro"
        CajaAhorro ahorroLuis = new CajaAhorro(cliente1, 5000.0);
        
        // Creamos una Cuenta Corriente con un saldo inicial y un límite de descubierto
        // (Asumiendo que tu constructor de CuentaCorriente recibe esos parámetros)
        Cuenta_Corriente corrienteAna = new Cuenta_Corriente(cliente2, 1000.0);

        System.out.println("\n--- ESTADO INICIAL ---");
        System.out.println("Titular: " + ahorroLuis.getTitular().getNombre() + " | Saldo: " + ahorroLuis.consultarSaldo());
        System.out.println("Titular: " + corrienteAna.getTitular().getNombre() + " | Saldo: " + corrienteAna.consultarSaldo());

        // --- 3. PRUEBA DE DEPÓSITOS Y EXTRACCIONES ---
        System.out.println("\n--- OPERACIONES ---");
        
        // Probar extracción normal en Caja de Ahorro
        ahorroLuis.Extraer(2000); 
        
        // Probar extracción que falla en Caja de Ahorro (no tiene descubierto)
        ahorroLuis.Extraer(4000); 

        // Probar extracción con descubierto en Cuenta Corriente
        // Ana tiene 1000, extrae 3000 -> Su saldo quedará en -2000
        corrienteAna.Extraer(3000);

        // --- 4. PRUEBA DE TRANSFERENCIA (El método final de la clase padre) ---
        System.out.println("\n--- TRANSFERENCIA ---");
        // Luis le transfiere 1000 a Ana
        // El método transferir usa extraer() de uno y depositar() del otro internamente
        ahorroLuis.Transferir(1000, corrienteAna);

        // --- 5. ESTADO FINAL ---
        System.out.println("\n--- ESTADO FINAL ---");
        System.out.println("Saldo Luis: " + ahorroLuis.consultarSaldo());
        System.out.println("Saldo Ana (debería ser -1000): " + corrienteAna.consultarSaldo());
    
        }
}



