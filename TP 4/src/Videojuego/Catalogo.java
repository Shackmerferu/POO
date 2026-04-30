package Videojuego;

import java.util.ArrayList;

public class Catalogo {
    private ArrayList<Videojuego> juegos;

    public Catalogo() {
        juegos = new ArrayList<>();
    }

    public void agregar(Videojuego v) {
        juegos.add(v);
    }

    public void eliminar(String nombre) {
        juegos.removeIf(j -> j.getNombre().equals(nombre));
    }

    public void listar() {
        for (Videojuego v : juegos) {
            System.out.println(v);
        }
    }
}