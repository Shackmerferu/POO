package py_poo.spaceinvaders;

import py_poo.entities.Bala;
import py_poo.interfaces.Armado;

public class EnemigoA extends Enemigo implements Armado{

    public EnemigoA(int X, int Y) {
        super(X, Y);
        this.setSprite("imagenes/Space Invaders/Invaders/space__0001_Enemy2.png"); 
        this.puntosxKill = 20; 
    }

    @Override
    public Bala Disparar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Disparar'");
    }

  
    
}
