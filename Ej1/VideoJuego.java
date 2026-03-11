package Ej1;
import java.time.LocalTime;

public class VideoJuego{
    private int cantidadDeVideoJuegosCreados;
    private String nombre;
    private LocalTime fechaDeLanzamiento;
    private double precio;

    public void jugar(){System.out.println("A jugar!!!");}
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Name:" + nombre + "Cantidad:" + this.cantidadDeVideoJuegosCreados + "Lanzamiento:" + this.fechaDeLanzamiento + "Precio(ARS):" + this.precio;
    }
    //Es poliformismo estatico
    public VideoJuego(double precio){
        this.precio = precio;
    }

    public VideoJuego(int precio){
        this.precio = precio;
    }
    
    public VideoJuego(String precio){
        this.precio = Double.parseDouble(precio);
    }
    public String getNombre() {
        return nombre;
    }
    public void setFechaDeLanzamiento(LocalTime fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    public LocalTime getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }

}