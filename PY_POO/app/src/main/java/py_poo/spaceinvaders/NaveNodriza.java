package py_poo.spaceinvaders;

import java.util.Random;

import py_poo.entities.ObjetoGrafico;
import py_poo.core.Constantes;
public class NaveNodriza extends ObjetoGrafico {

    private static Random random = new Random();
    private int velocidad=2;
    public NaveNodriza(String ruta) {
        super();
        this.setSprite(ruta);
        this.setDimension(new java.awt.Dimension(50,30));
        this.setPunto(new java.awt.Point(-50, 25));
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

}