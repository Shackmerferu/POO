package py_poo.spaceinvaders;


import py_poo.entities.ObjetoGrafico;
import py_poo.interfaces.Movible;

public class Laser extends ObjetoGrafico implements Movible {
   private int velocidad; 
    public Laser(int X , int  Y, int velocidad, String sprite ){
        
        this.setSprite(sprite);
        this.setDimension(new java.awt.Dimension(5, 15));
        this.setPunto(new java.awt.Point(X, Y));
        this.velocidad = velocidad;
    }
    

    public void Mover(){
        
    }
    public int getVelocidad() {
        return velocidad;
    }
    @Override
    public void actualizar() {
        this.setY(this.getY()+this.velocidad);
        
        if (this.getY() + this.getHeight() < 0 || this.getY() > 600) {
        this.marcarParaEliminar();
        }
    }
}
