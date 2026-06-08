package py_poo.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Bala;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.interfaces.Armado;

public class EnemigoB extends Enemigo {

    private static Animacion animacionFlota(){
        List<Sprite> fotograma=new ArrayList<>();
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0002_B1.png"));
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0003_B2.png"));

        return new Animacion(fotograma,200);
    }
    
    
    public EnemigoB(int X, int Y) {
        super(X, Y, animacionFlota());
        this.setSprite("imagenes/Space Invaders/Invaders/space__0002_B1.png"); 
        this.puntosxKill = 20; 
    }

   

  
    
}
