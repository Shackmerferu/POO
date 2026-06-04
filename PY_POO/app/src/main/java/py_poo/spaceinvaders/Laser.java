package py_poo.spaceinvaders;

import py_poo.entities.Bala;

public class Laser extends Bala {
   private int velocidad; 
    public Laser(int X , int  Y, int velocidad, String sprite ){
        super();
        this.setSprite(sprite);
        this.setDimension(new java.awt.Dimension(5, 15));
        this.setPunto(new java.awt.Point(X, Y));
        this.velocidad = velocidad;
    }
    
    public int getVelocidad() {
        return velocidad;
    }
    
    public void Mover() {
        this.setY(this.getY()+this.velocidad);
    }
    @Override
public void actualizar() {

    Mover(); 
    if (this.getY() + this.getHeight() < 0 || this.getY() > 600) {
        this.marcarParaEliminar();
    }
}
}
