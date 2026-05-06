import java.util.Random;


public class Dados extends VideoJuego {
    public Dados() {
        super("Dados Genérico", 0.0, "Sin Editorial", "PEGI 3");
        System.out.println("Se ha creado un objeto de tipo Dados vacío.");
    }
    public Dados(String nombre, double precio, String editorial,String pegi){
        super(nombre,precio,editorial,pegi);
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