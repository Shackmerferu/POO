package py_poo.spaceinvaders;

import py_poo.entities.ObjetoGrafico;

public class Escudo extends ObjetoGrafico {
    private int resistencia;
    private SegmentoEscudo[7] segmentos;
    
    public Escudo(int X, int Y, int NumeroP){
        super();
        this.setSprite("imagenes/Space Invaders/Invaders/space__0008_ShieldFull.png");
        this.setDimension(new java.awt.Dimension(60,30));
        this.setPunto(new java.awt.Point(X, Y));
        this.NumeroP = NumeroP;
    }
    public void recibirDanio() {
    }
}
