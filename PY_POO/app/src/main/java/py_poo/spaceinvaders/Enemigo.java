package py_poo.spaceinvaders;

import py_poo.entities.Personaje;

public class Enemigo extends Personaje {
    protected int puntosxKill;

    public Enemigo(int X, int Y) {
        super();
    
        this.setDimension(new java.awt.Dimension(30, 30));
        this.setPunto(new java.awt.Point(X, Y));
       
    }
    
    public int getPuntos() {
        return puntosxKill;
    }
}