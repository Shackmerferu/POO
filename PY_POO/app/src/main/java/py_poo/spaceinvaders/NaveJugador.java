package py_poo.spaceinvaders;

import java.awt.Point;

import py_poo.entities.Personaje;
import py_poo.interfaces.Armado;

public class NaveJugador extends Personaje implements Armado {
    public NaveJugador(int X, int Y){
        super();
        this.setSprite("imagenes/Space Invaders/Invaders/space__0006_Player.png");
        this.setDimension(dimension = new java.awt.Dimension(40, 30));
        this.setPunto(punto = new java.awt.Point(X, Y));
    }
    
   
    @Override
    public Laser Disparar() {
        
     int centroX = (int) this.getX() + (this.getWidth() / 2) - 8; ; 
     return new Laser(centroX, (int) this.getY(),-5 ,  "imagenes/Space Invaders/Projectiles/Projectile_Player.png");
    }
}
