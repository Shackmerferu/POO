package tp5;

import java.util.Random;

public class PiedraPapelTijeras extends VideoJuego {
    @SuppressWarnings("FieldMayBeFinal")
    private Random rand = new Random();
    String[] jugada = {"Piedra","Papel","Tijera"};
    
    public PiedraPapelTijeras(String nombre,double precio,short pegi){
        super(nombre,precio,pegi);
        System.out.println("Se crea un objeto de tipo PPT");
    }

    @Override
    public void jugar(){
        System.out.println("Vamos a jugar PPT");
        resultado=jugada[rand.nextInt(jugada.length)];

    }
    @Override
    public String getNombre(){
        return this.nombre;
    }

    @Override
    public String toString(){
        return super.toString() + "esto es PPT" ;
    }

    

}
