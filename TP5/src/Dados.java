import java.util.Random;


public class Dados extends VideoJuego {
        public Dados(){
        super("Dadin", 120, "JuanPi", "+21");
        System.out.println("Se crea un objeto de tipo Dados");
    }
    @Override
    void jugar(){
    int  n = (int) (Math.random() * 6) + 1;
    this.resultado= n + ""; 
    System.out.println(resultado);
    }
    @Override
    String getNombre(){
        return this.nombre;
    }
}