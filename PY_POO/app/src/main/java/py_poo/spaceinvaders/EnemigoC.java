package py_poo.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Bala;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.interfaces.Armado;

public class EnemigoC extends Enemigo {
    private static Animacion animacionFlota(){
        List<Sprite> fotograma=new ArrayList<>();
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0004_C1.png"));
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0005_C2.png"));

        return new Animacion(fotograma,200);
    }

    public EnemigoC(int X, int Y) {
        


        super(X, Y,animacionFlota());
        this.setSprite("imagenes/Space Invaders/Invaders/space__0004_C1.png"); 
        this.puntosxKill = 10; 
    }
}