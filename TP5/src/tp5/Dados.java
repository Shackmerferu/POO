package tp5;

import java.util.Random;


public class Dados extends VideoJuego {
    @SuppressWarnings("FieldMayBeFinal")
    private Random rand = new Random();
    public Dados(String nombre, double precio, short pegi) {
        super(nombre, precio, pegi);
        System.out.println("Se crea un objeto de tipo Dados");
    }

    @Override
    public void jugar(){
        System.out.println("Vamos a jugar Dados");
        resultado=String.valueOf(rand.nextInt(6) + 1);
    }
    @Override
    public String getNombre(){
        return this.nombre;
    }

    @Override
    public String toString(){
        return super.toString() + "esto es Dados" ;
    }

    

}
