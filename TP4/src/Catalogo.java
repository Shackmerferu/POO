import java.util.ArrayList;

public class Catalogo{
    private ArrayList<VideoJuego> Juego;
    
    public Catalogo(){
        Juego=new ArrayList<VideoJuego>();
    }
    public void agregar(VideoJuego juegonew){
        Juego.add(juegonew);
    }
    public void eliminar(String nombre){
         int jueguito=-1;
        for(int i=0; i<Juego.size();i++){
            if (Juego.get(i).getNombre().equalsIgnoreCase(nombre))
            jueguito=i;
        }
       if(jueguito!=-1){ 
        Juego.remove(jueguito);
        System.out.println("Juego Borrado Exitosamente");}
        else{
        System.out.println("Juego No Encontrado");}
    }  
    
    public void mostrar(){
       for (int i = 0; i < Juego.size(); i++) {
        VideoJuego juego = Juego.get(i);
        System.out.println(juego); 
        }
    }




}