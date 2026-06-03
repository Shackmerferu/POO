package py_poo.spaceinvaders;

import java.awt.Point;

import py_poo.entities.Personaje;
import py_poo.interfaces.Armado;

public class NaveJugador extends Personaje implements Armado {
    public NaveJugador(int X, int Y){
        super();
        this.setSprite("D:\\Facultad\\Programacion Orientada a Objetos\\RepositoriosProyecto\\POO\\PY_POO\\app\\src\\main\\resources\\imagenes\\Space Invaders\\Invaders\\space__0006_Player");
        this.setDimension(dimension = new java.awt.Dimension(40, 30));
        this.setPunto(punto = new java.awt.Point(X, Y));
    }
    
   

    public void Disparar() {
     
    }
}
