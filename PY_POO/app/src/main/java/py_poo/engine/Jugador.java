package py_poo.engine;

public  class Jugador {
    private String Nombre;
    private  int id;
    private static int contadorid = 0;
    public Jugador(){
        this.id=contadorid++;
        this.Nombre="Player";
    }

    public Jugador(String Nombre){
        this.id=contadorid++;
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
