package py_poo.pong;

import java.awt.Graphics;

import py_poo.core.Constantes;
import py_poo.entities.ObjetoGrafico;

public class PelotaPong extends ObjetoGrafico {
    private double dx;
    private double dy;
    private double velocidadBase;

    public PelotaPong() {
        super("imagenes/Pelota Pong.png");
        this.velocidadBase = 3.5;
        setDimension(new java.awt.Dimension(16, 16)); // fuerza tamano logico 16x16 (sprite 130x130 escalado)
        reiniciar();
    }

    public void mover() {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    public void rebotarParedes() {
        if (getY() <= 0) {
            setY(0);
            dy = -dy;
        }
        if (getY() >= Constantes.HEIGHT - getHeight()) {
            setY(Constantes.HEIGHT - getHeight());
            dy = -dy;
        }
    }

    public void rebotarPaleta(Paleta p) {
        dx = -dx;

        double centroPelota = getY() + getHeight() / 2.0;
        double centroPaleta = p.getY() + p.getHeight() / 2.0;
        double diferencia = centroPelota - centroPaleta;
        double maxDesvio = p.getHeight() / 2.0;
        double factorAngulo = diferencia / maxDesvio;

        dy = factorAngulo * velocidadBase;

        // empujar la pelota fuera de la paleta para evitar rebotes multiples
        if (dx > 0) {
            setX(p.getX() + p.getWidth());
        } else {
            setX(p.getX() - getWidth());
        }

        aumentarVelocidad();
    }

    public boolean salioIzquierda() {
        return getX() + getWidth() < 0;
    }

    public boolean salioDerecha() {
        return getX() > Constantes.WIDTH;
    }

    public void aumentarVelocidad() {
        double factor = 1.05;
        double maxVel = 8.0;
        if (Math.abs(dx) < maxVel) {
            dx *= factor;
        }
        if (Math.abs(dy) < maxVel) {
            dy *= factor;
        }
    }

    @Override
    public void display(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int) getX(), (int) getY(), getWidth(), getHeight(), null); // sprite 130x130 escalado a 16x16
        }
    }

    public void reiniciar() {
        setX(Constantes.WIDTH / 2.0 - getWidth() / 2.0);
        setY(Constantes.HEIGHT / 2.0 - getHeight() / 2.0);
        dx = Math.random() < 0.5 ? velocidadBase : -velocidadBase;

    }

    public void reiniciar(boolean haciaLaDerecha) { // sirve hacia el lado del perdedor
        setX(Constantes.WIDTH / 2.0 - getWidth() / 2.0);
        setY(Constantes.HEIGHT / 2.0 - getHeight() / 2.0);
        dx = haciaLaDerecha ? velocidadBase : -velocidadBase;

    }
}
