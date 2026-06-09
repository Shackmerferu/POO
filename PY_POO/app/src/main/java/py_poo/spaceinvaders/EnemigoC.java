package py_poo.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Bala;
import py_poo.graphics.Animacion;
import py_poo.graphics.Sprite;
import py_poo.interfaces.Armado;

public class EnemigoC extends Enemigo {
    private static Animacion animacionFlota(int skin){
        List<Sprite> fotograma=new ArrayList<>();
        if(skin==0){
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0002_B1.png"));
        fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/space__0003_B2.png"));
        }else{
         fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/Invaders_nuevo/Invasor3_A10.png"));
         fotograma.add(new Sprite("imagenes/Space Invaders/Invaders/Invaders_nuevo/Invasor3_A20.png"));   
        }

        return new Animacion(fotograma,200);
    }

    public EnemigoC(int X, int Y, int skinInvasores) {
        super(X, Y,animacionFlota(skinInvasores));
        this.puntosxKill = 10; 
    }
}