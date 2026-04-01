import java.util.Scanner;
public class Example {
    public static void main(String[] args) {
        Scanner comando = new Scanner(System.in);
        System.out.println("Ingresar comando: ");
        boolean running = true;
        while (running) {
            switch (comando.nextLine()) {
                case "empezar":
                    System.out.println("¡Empezado!");
                    break;
                case "salir":
                    System.out.println("¡Adios!");
                    running = false;
                    break;
                default:
                    System.out.println("Comando invalido");
                    break;
            }
        }
        comando.close();
    }
}
