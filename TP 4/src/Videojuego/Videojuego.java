package Videojuego;

public class Videojuego {
    private String nombre;
    private String genero;

    public Videojuego(String nombre, String genero) {
        this.nombre = nombre;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public String toString() {
        return nombre + " - " + genero;
    }
}