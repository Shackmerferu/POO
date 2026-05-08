import java.time.LocalTime;

abstract class VideoJuego {
    protected String nombre;
    protected LocalTime fechaDeLanzamiento;
    protected String editorial;
    protected double precio;
    protected String pegi;
    protected String resultado;

    public VideoJuego(String nombre, double precio, String editorial,String pegi){
        this.nombre=nombre;
        this.precio=precio;
        this.editorial=editorial;
        this.fechaDeLanzamiento = LocalTime.now();
        this.pegi=pegi;

    }
    public VideoJuego(String nombre , int precio,  String editorial,String pegi){
        this(nombre, (double) precio, editorial, pegi);
    }
     public VideoJuego(String nombre , String precio,  String editorial,String pegi){
        this(nombre, Double.parseDouble(precio), editorial, pegi);
    }
    public void mostrarResultado(){
        System.out.println(resultado);
    }
    abstract void jugar();
    abstract String getNombre();
    @Override
    public String toString() {
        return "Name:" + nombre + " Lanzamiento:" + this.fechaDeLanzamiento + " Precio(ARS):" + this.precio +"| Editorial: "+ this.editorial +"Pegi"+ this.pegi;
    }
}
