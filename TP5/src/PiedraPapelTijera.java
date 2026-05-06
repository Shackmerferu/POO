public class PiedraPapelTijera extends VideoJuego {
    public PiedraPapelTijera(String nombre, double precio, String editorial, String pegi){
        super(nombre,precio,editorial,pegi);
        System.out.println("se crea objeto de tipo PiedraPapelTijera ");
    }
@Override
void  jugar(){
        String[] opcion ={"piedra","papel","tijera"};
        int indicealeatorio = (int) (Math.random()*3);
        this.resultado = opcion[indicealeatorio];
}
@Override
    String getNombre(){
        return  this.nombre;
}
}
