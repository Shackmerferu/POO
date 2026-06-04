package py_poo.spaceinvaders;

import java.util.Random;

import py_poo.entities.ObjetoGrafico;
import py_poo.core.Constantes;
public class NaveNodriza extends ObjetoGrafico {

    private static Random random = new Random();
    private int velocidad=2;
    public NaveNodriza() {
        super();
        this.setSprite("imagenes/Space Invaders/Invaders/space__0007_UFO.png");
        this.setDimension(new java.awt.Dimension(50,30));
        this.setPunto(new java.awt.Point(-50, 25));
    }

    public static int puntaje() {
        return (random.nextInt(6) + 1) * 50;
    }
    @Override
    public void actualizar() {
        this.setX(this.getX()+velocidad);

        if (this.getX()>Constantes.WIDTH){
            this.marcarParaEliminar();
        }
    }

}