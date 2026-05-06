import java.util.Random;
public class PiedraPapelTijera extends VideoJuego {
    public PiedraPapelTijera() {
        super("Sin nombre", 0.0, "Sin editorial", "Desconocido"); // O los valores por defecto
        System.out.println("Se crea un objeto vacío");
    }
    public PiedraPapelTijera(String nombre, double precio, String editorial,String pegi){
        super(nombre,precio,editorial,pegi);
        System.out.println("Se crea un objeto de tipo PiedraPapelTijera");
    }
    @Override
    void jugar(){
        String[] opciones = {"piedra", "papel", "tijera"};
        int indice= (int) (Math.random() * opciones.length);
        this.resultado= opciones[indice]; 
        System.out.println("Vamo a jugar cra");

    }
    @Override
    String getNombre(){
        return this.nombre;
    }
}


