package py_poo.spaceinvaders;

import py_poo.entities.Bala;
import py_poo.interfaces.Armado;

public class EnemigoA extends Enemigo implements Armado{

    public EnemigoA(int X, int Y) {
        super(X, Y);
        this.setSprite("imagenes/Space Invaders/Invaders/space__0000_A1.png"); 
        this.puntosxKill = 30; 
    }

    @Override
    public Bala Disparar() {
       int centroX = (int) this.getX() + (this.getWidth() / 2) - 2; 
        
       
        int origenY = (int) this.getY() + this.getHeight();
        
        return new Laser(centroX, origenY, 5 , "imagenes/Space Invaders/Projectiles/Projectile_Enemy.png");
    }

  
    
}
