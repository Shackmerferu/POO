package py_poo.spaceinvaders;

import java.util.Random;

import py_poo.entities.ObjetoGrafico;

public class NaveNodriza extends ObjetoGrafico {

    private static Random random = new Random();

    public NaveNodriza(String sprite) {
        super(sprite);
    }

    public static int puntaje() {
        return (random.nextInt(6) + 1) * 50;
    }

    public void cruzarPantalla() {

    }
}