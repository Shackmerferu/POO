package py_poo.spaceinvaders;

import py_poo.entities.ObjetoGrafico;

public class Escudo extends ObjetoGrafico {
    
    private SegmentoEscudo[] segmentos;

    public Escudo(int X, int Y) {
        super();
        this.setDimension(new java.awt.Dimension(60, 30));
        this.setPunto(new java.awt.Point(X, Y));
        
        this.segmentos = new SegmentoEscudo[9];
        
        // 1. Arreglo estricto de los 9 Sprites SANOS
        String[] sanos = {
            "00_space__0008_ShieldFull.png",
            "01_space__0008_ShieldFull.png",
            "02_space__0008_ShieldFull.png",
            "03_space__0008_ShieldFull.png",
            "04_space__0008_ShieldFull.png", 
            "05_space__0008_ShieldFull.png",
            "06_space__0008_ShieldFull.png",
            "07_space__0008_ShieldFull.png", // Túnel sano
            "08_space__0008_ShieldFull.png"
        };
        
        segmentos[0] = new SegmentoEscudo(X, Y, sanos[0], "1.png");
        segmentos[1] = new SegmentoEscudo(X + 20, Y, sanos[1], "2.png");
        segmentos[2] = new SegmentoEscudo(X + 40, Y, sanos[2], "3.png");
        
     
        segmentos[3] = new SegmentoEscudo(X, Y + 10, sanos[3], "4.png");
        segmentos[4] = new SegmentoEscudo(X + 20, Y + 10, sanos[4], "5.png"); 
        segmentos[5] = new SegmentoEscudo(X + 40, Y + 10, sanos[5], "6.png");
        
       
        segmentos[6] = new SegmentoEscudo(X, Y + 20, sanos[6], "7.png");
        segmentos[7] = new SegmentoEscudo(X + 20, Y + 20, sanos[7], "5.png"); 
        segmentos[8] = new SegmentoEscudo(X + 40, Y + 20, sanos[8], "9.png");
    }

    public SegmentoEscudo[] getSegmentos() {
        return this.segmentos;
    }
}