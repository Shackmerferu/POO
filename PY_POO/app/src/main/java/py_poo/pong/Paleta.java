package py_poo.pong;

import java.util.ArrayList;

import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.interfaces.Movible;

public class Paleta extends ObjetoGrafico implements Movible {
    private ArrayList<Integer> Segmento = new ArrayList<Integer>(8);
    private int velocidad = 5;
    private InputManager input;
    private int idJugador; 

    public Paleta(InputManager input, int idJugador) {
        this.input = input;
        this.idJugador = idJugador;
    }

    @Override
    public void Mover() {
       int direccionY = 0;

       
        if (idJugador == 1) {
            if (input.isWPressed()) {
                direccionY = -1; 
            }
            if (input.isSPressed()) {
                direccionY = 1;  
            }
        }

       
        if (idJugador == 2) {
            if (input.isUpPressed()) {
                direccionY = -1; 
            }
            if (input.isDownPressed()) {
                direccionY = 1;  
            }
        }

        
        if (direccionY != 0) {
            int nuevaY = (int) (getY() + (direccionY * velocidad));
            
            
            if (nuevaY >= 0 && nuevaY <= (600 - 100)) { 
                setY(nuevaY);
            }
        } 
    }

    public void ResetearPOS(){} 
    public void dibujar(java.awt.Graphics g) {
       //falta definir lo de paleta, alto x ancho, color, etc
    }
}
