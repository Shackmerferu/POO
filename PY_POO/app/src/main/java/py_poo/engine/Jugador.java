package py_poo.engine;

public final class Jugador {
    private String Nombre;
    private static int id = 0;
    public Jugador(){
        this.id=id++;
        this.Nombre="Player";
    }
    public Jugador(String Nombre){
        this.id=id++;
        this.Nombre=Nombre;
    }
    public void setNombre(String Nombre){
        this.Nombre=Nombre;
    }
    public String getNombre(){
        return Nombre;
    }

    public int getid(){
        return this.id;
    }
}
