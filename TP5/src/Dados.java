import java.util.Random;


public class Dados extends VideoJuego {
    public Dados(String nombre , int precio,  String editorial,String pegi){
        super(nombre,precio,editorial,pegi);
    }

    @Override
    void jugar(){
        int nro_aleatorio=(int) (Math.random()*6)+1;
        this.resultado= String.valueOf(nro_aleatorio);
    }
    @Override
    String getNombre(){
        return  this.nombre;
    }
}