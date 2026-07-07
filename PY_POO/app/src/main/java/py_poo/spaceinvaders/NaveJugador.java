package py_poo.spaceinvaders;

import py_poo.core.Constantes;
import py_poo.entities.Personaje;
import py_poo.interfaces.Armado;
import py_poo.input.InputManager;

public class NaveJugador extends Personaje implements Armado {
    private String laserSkin;

    public NaveJugador(int X, int Y, String naveSkin, String laserSkin){
        super();
        this.setSprite(naveSkin);
        this.laserSkin = laserSkin;
        this.bounds = new java.awt.Rectangle(X, Y, 40, 30);
        this.vidas = 3;
    }
    
    public void Muerto(int X, int Y){
        this.bounds.setSize(30, 30);
        this.setSprite("imagenes/Space Invaders/Invaders/space__0010_PlayerExplosion.png");
    }
    public void Mover(InputManager input) {
        int limiteDerecho = Constantes.WIDTH - this.getWidth(); 

        if (input.isLeftPressed() && this.getX() > 0) {
            this.setX(this.getX() - 5);
        }
        if (input.isRightPressed() && this.getX() < limiteDerecho) {
            this.setX(this.getX() + 5);
        }
    }
    
    @Override
    public void display(java.awt.Graphics g) {
        g.drawImage(getSprite(), (int) getX(), (int) getY(), 
        getWidth(), getHeight(), null);
    }
   
    @Override
    public Laser Disparar() {
        int centroX = (int) this.getX() + (this.getWidth() / 2) - 6; 
        return new Laser(centroX, (int) this.getY(), -5 , this.laserSkin);
    }

    @Override
    public void crearExplosion(int x, int y) {
        super.crearExplosion(x, y);
    }

    @Override
    protected String getRutaExplosion() {
        return "imagenes/Space Invaders/Invaders/space__0010_PlayerExplosion.png";
    }
}