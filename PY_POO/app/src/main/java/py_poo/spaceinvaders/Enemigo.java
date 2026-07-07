package py_poo.spaceinvaders;

import java.awt.Graphics;

import py_poo.entities.Personaje;
import py_poo.graphics.Animacion;

public class Enemigo extends Personaje {
    protected int puntosxKill;
    private Animacion animacion;
   
    public Enemigo(int X, int Y, Animacion animacion) {
        super();
        this.animacion=animacion;
        this.setBounds(new java.awt.Rectangle(X, Y, 30, 30));
       
    }

    public void actualizacionAnimacion(){ //animacion para enemigos
        if(this.animacion!=null){
            this.animacion.actualizar();
        }
    }
    @Override
    //Dibuja Alien
    public void display(Graphics g){
        if(this.animacion != null){
            this.animacion.dibujar(g, (int)this.getX(), (int)this.getY(),this.getWidth(), this.getHeight());
        }
    }

    public int getPuntos() {
        return puntosxKill;
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