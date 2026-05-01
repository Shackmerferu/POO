import java.util.ArrayList;


public class Categoria{

    private final ArrayList<VideoJuego> Juegos;


    public Categoria(){
        Juegos = new ArrayList<>();
    }

    public void agregar(VideoJuego juego){
        Juegos.add(juego);
    }   

    public boolean eliminar(VideoJuego juego){
        boolean removed = false;
        for(int i=0;i<Juegos.size();i++ ){
            if(Juegos.get(i).getNombre().equals(juego.getNombre())){
                Juegos.remove(i);
                removed=true;
            }
        }
        return removed;
    }

    public void listar(){
        if(Juegos.isEmpty()){
            for(VideoJuego juego: Juegos){
                System.out.print(juego);
            }
        }
    }



}

/*
public class CategoriaConArreglo{
    private VideoJuego[] Juegos;
    private int cantidad;

    public CategoriaConArreglo(){
        Juegos = new VideoJuego[100]; // Tamaño fijo
        cantidad = 0;
    }

    public void agregar(VideoJuego juego){
        if(cantidad < Juegos.length){
            Juegos[cantidad] = juego;
            cantidad++;
        }
    }

    public boolean eliminar(VideoJuego juego){
        boolean removed = false;
        for(int i = 0; i < cantidad; i++){
            if(Juegos[i].getNombre().equals(juego.getNombre())){
                for(int j = i; j < cantidad - 1; j++){
                    Juegos[j] = Juegos[j + 1];
                }
                cantidad--;
                removed = true;
            }
        }
        return removed;
    }

    public void listar(){
        if(cantidad > 0){
            for(int i = 0; i < cantidad; i++){
                System.out.print(Juegos[i]);
            }
        }
    }
}

import java.util.Vector;

public class CategoriaConVector{
    private final Vector<VideoJuego> Juegos;

    public CategoriaConVector(){
        Juegos = new Vector<>();
    }

    public void agregar(VideoJuego juego){
        Juegos.add(juego);
    }

    public boolean eliminar(VideoJuego juego){
        boolean removed = false;
        for(int i = 0; i < Juegos.size(); i++){
            if(Juegos.get(i).getNombre().equals(juego.getNombre())){
                Juegos.remove(i);
                removed = true;
            }
        }
        return removed;
    }

    public void listar(){
        if(Juegos.size() > 0){
            for(VideoJuego juego : Juegos){
                System.out.print(juego);
            }
        }
    }
}
*/   