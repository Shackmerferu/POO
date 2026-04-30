package Videojuego;

public class Main {
    public static void main(String[] args) {
        Catalogo c = new Catalogo();

        c.agregar(new Videojuego("Skyrim", "RPG"));
        c.agregar(new Videojuego("FIFA", "Deportes"));

        c.listar();
    }
}