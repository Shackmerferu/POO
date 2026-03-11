package Ej1;
import java.time.LocalTime;

public class VideoJuego {

    private static int cantidadDeVideoJuegosCreados = 0;
    private String nombre;
    private LocalTime fechaDeLanzamiento;
    private double precio;

    public void jugar() {
        System.out.println("A jugar!!!");
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Name:" + nombre + " Lanzamiento:" + this.fechaDeLanzamiento + " Precio(ARS):" + this.precio;
    }
    //Es polimorfismo estatico
    public VideoJuego(String nombre , double precio) {
        this.precio = precio;
        this.nombre = nombre;
        this.fechaDeLanzamiento = LocalTime.now();
        cantidadDeVideoJuegosCreados++;
    }

    public VideoJuego(String nombre , int precio) {
        this.precio = precio;
        this.fechaDeLanzamiento = LocalTime.now();
        cantidadDeVideoJuegosCreados++;
    }

    public VideoJuego(String nombre , String precio) {
        this.precio = Double.parseDouble(precio);
        this.fechaDeLanzamiento = LocalTime.now();
        cantidadDeVideoJuegosCreados++;
    }

    public static int getCantidadDeVideoJuegosCreados() {
        return cantidadDeVideoJuegosCreados;
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