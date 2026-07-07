package py_poo.spaceinvaders;

import java.util.Random;

import py_poo.core.Constantes;
import py_poo.entities.Personaje;

public class NaveNodriza extends Personaje {

    private static Random random = new Random();
    private int velocidad=2;
    public NaveNodriza(String ruta) {
        super();
        this.setSprite(ruta);
        this.setBounds(new java.awt.Rectangle(-50, 25, 50, 30));
    }

    public int puntaje(int contadorDisparos) {
       if (contadorDisparos >= 23 && (contadorDisparos - 23) % 15 == 0) {
        return 300; 
    }  else{
        return (random.nextInt(6) + 1) * 50;
    }
}
    @Override
    public void actualizar() {
        this.setX(this.getX()+velocidad);

        if (this.getX()>Constantes.WIDTH){
            this.marcarParaEliminar();
        }
    }

    @Override
    public void crearExplosion(int x, int y) {
        super.crearExplosion(x, y);
    }

    @Override
    protected String getRutaExplosion() {
        return "imagenes/Space Invaders/Invaders/space__0009_EnemyExplosion.png";
    }

}