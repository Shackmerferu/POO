package tp5;


public class PiedraPapelTijeras extends VideoJuego {

    public PiedraPapelTijeras(String nombre,double precio,short pegi){
        super(nombre,precio,pegi);
        System.out.println("Se crea un objeto de tipo PPT");
    }

    @Override
    public void jugar(){
        System.out.println("Vamos a jugar PPT");
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
