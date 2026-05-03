package tp5;


public class Pong extends VideoJuego {

    public Pong(String nombre, double precio, short pegi) {
        super(nombre, precio, pegi);
        System.out.println("Se crea un objeto de tipo Pong");
    }

    @Override
    public void jugar(){
        System.out.println("Vamos a jugar Pong");
    }
    @Override
    public String getNombre(){
        return this.nombre;
    }

    @Override
    public String toString(){
        return super.toString() + "esto es Pong" ;
    }

    

}
