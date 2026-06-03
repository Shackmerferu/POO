package py_poo.spaceinvaders;

import py_poo.entities.Bala;
import py_poo.interfaces.Armado;

public class EnemigoC extends Enemigo {

    public EnemigoC(int X, int Y) {
        super(X, Y);
        this.setSprite("imagenes/Space Invaders/Invaders/space__0004_C1.png"); 
        this.puntosxKill = 10; 
    }
}