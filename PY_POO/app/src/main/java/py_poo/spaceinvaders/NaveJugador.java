package py_poo.spaceinvaders;

import py_poo.entities.Personaje;
import py_poo.interfaces.Armado;

public class NaveJugador extends Personaje implements Armado {
    private String laserSkin;

    public NaveJugador(int X, int Y, String naveSkin, String laserSkin){
        super();
        this.setSprite(naveSkin);
        this.laserSkin = laserSkin;
        this.setDimension(dimension = new java.awt.Dimension(40, 30));
        this.setPunto(punto = new java.awt.Point(X, Y));
        this.vidas= 3;
    }
    
    @Override
    public void display(java.awt.Graphics g) {
        g.drawImage(getSprite(), (int) getX(), (int) getY(), 
        getWidth(), getHeight(), null);
    }
   
    @Override
    public Laser Disparar() {
        
     int centroX = (int) this.getX() + (this.getWidth() / 2) - 8;  
     return new Laser(centroX, (int) this.getY(), -5 ,  this.laserSkin);
    }
}
