package py_poo.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Bala;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.interfaces.Armado;

public class EnemigoA extends Enemigo implements Armado{

    private static Animacion animacionFlota(){
        List<Sprite> fotograma=new ArrayList<>();
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0000_A1.png"));
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0001_A2.png"));

        return new Animacion(fotograma,200);
    }

    
    
    public EnemigoA(int X, int Y) {
        super(X, Y, animacionFlota());
       
        this.puntosxKill = 30; 
    }

    
    @Override
    public Bala Disparar() {
       int centroX = (int) this.getX() + (this.getWidth() / 2) - 2; 
        
       
        int origenY = (int) this.getY() + this.getHeight();
        
        return new Laser(centroX, origenY, 5 , "imagenes/Space Invaders/Projectiles/Projectile_Enemy.png");
    }

  
    
}
