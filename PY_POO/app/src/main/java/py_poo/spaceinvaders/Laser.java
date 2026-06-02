package py_poo.spaceinvaders;

import py_poo.entities.Bala;

public class Laser extends Bala {
   
    public Laser(int X , int  Y ){
        super();
        this.setSprite("D:\\Facultad\\Programacion Orientada a Objetos\\RepositoriosProyecto\\POO\\PY_POO\\app\\src\\main\\resources\\imagenes\\Space Invaders\\Projectiles\\Projectile_Player.png");
        this.setDimension(new java.awt.Dimension(5, 15));
        this.setPunto(new java.awt.Point(X, Y));
    }
    public void Mover() {
        this.setY(this.getY()-5);
    }
}
