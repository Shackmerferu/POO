package py_poo.pong;

import java.awt.Color;
import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.entities.ObjetoGrafico;
import py_poo.input.InputManager;
import py_poo.interfaces.Movible;

public class Paleta extends ObjetoGrafico implements Movible {
    private int velocidad = 3;
    private InputManager input;
    private int idJugador;

    public Paleta(InputManager input, int idJugador) {
        super(idJugador == 1 ? "imagenes/Paleta 1.png" : "imagenes/Paleta 2.png");
        this.input = input;
        this.idJugador = idJugador;
        setDimension(new java.awt.Dimension(20, 100)); // fuerza tamano logico (sprite x8 escalado al dibujar)
    }

    @Override
    public void Mover() {
       int direccion = 0;

       
        if (idJugador == 1) {
            if (input.isWPressed()) {
                direccion = -1; 
            }
            if (input.isSPressed()) {
                direccion = 1;  
            }
        }

       
        if (idJugador == 2) {
            if (input.isUpPressed()) {
                direccion = -1; 
            }
            if (input.isDownPressed()) {
                direccion = 1;  
            }
        }

        if (direccion != 0) {
            int nuevaY = (int) (getY() + (direccion * velocidad));
            
            
            if (nuevaY >= 0 && nuevaY <= (Constantes.HEIGHT - getHeight())) {
                setY(nuevaY);
            }
        }
    }

    public void ResetearPOS() {
        if (idJugador == 1) {
            setX(30);
        } else {
            setX(Constantes.WIDTH - 30 - getWidth());
        }
        setY(Constantes.HEIGHT / 2.0 - getHeight() / 2.0);
    }

    public void dibujar(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int) getX(), (int) getY(), getWidth(), getHeight(), null); // sprite x8 escalado a 20x100
        } else {
            g.setColor(Color.WHITE);
            g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
        }
    }
}
