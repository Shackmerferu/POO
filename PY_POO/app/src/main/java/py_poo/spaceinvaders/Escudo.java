package py_poo.spaceinvaders;

import py_poo.entities.ObjetoGrafico;

public class Escudo extends ObjetoGrafico {
    
    private SegmentoEscudo[] segmentos;

    public Escudo(int X, int Y) {
        super();
        this.setBounds(new java.awt.Rectangle(X, Y, 60, 30));
        
        this.segmentos = new SegmentoEscudo[9];
        
     //Arreglo de los 9 Sprites SANOS
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
        
        segmentos[0] = new SegmentoEscudo(X, Y, sanos[0], "sprite_0.png");
        segmentos[1] = new SegmentoEscudo(X + 20, Y, sanos[1], "sprite_2.png");
        segmentos[2] = new SegmentoEscudo(X + 40, Y, sanos[2], "sprite_1.png");
        
     
        segmentos[3] = new SegmentoEscudo(X, Y + 10, sanos[3], "sprite_3.png");
        segmentos[4] = new SegmentoEscudo(X + 20, Y + 10, sanos[4], "sprite_4.png"); 
        segmentos[5] = new SegmentoEscudo(X + 40, Y + 10, sanos[5], "sprite_5.png");
        
       
        segmentos[6] = new SegmentoEscudo(X, Y + 20, sanos[6], "sprite_6.png");
        segmentos[7] = new SegmentoEscudo(X + 20, Y + 20, sanos[7], "sprite_7.png"); 
        segmentos[8] = new SegmentoEscudo(X + 40, Y + 20, sanos[8], "sprite_8.png");
    }

    public SegmentoEscudo[] getSegmentos() {
        return this.segmentos;
    }
}