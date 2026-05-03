package tp5;


public class Dados extends VideoJuego {

    public Dados(String nombre, double precio, short pegi) {
        super(nombre, precio, pegi);
        System.out.println("Se crea un objeto de tipo Dados");
    }

    @Override
    public void jugar(){
        System.out.println("Vamos a jugar Dados");
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
