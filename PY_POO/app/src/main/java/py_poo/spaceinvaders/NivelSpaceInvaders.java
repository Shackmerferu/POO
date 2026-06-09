package py_poo.spaceinvaders;

import java.util.HashMap;
import java.util.List;
import py_poo.entities.ObjetoGrafico;


public class NivelSpaceInvaders {
    public void generarOleadas(HashMap<String, Enemigo> flotaE, List<ObjetoGrafico> Entidades, int nivelActual, int skinInvasores) {
        int filas = 5;
        int columnas = 10;
        
        
        int bajadaExtra = nivelActual * 40; 
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int posX = 50 + (j * 50); 
                int posY = 50 + (i * 40) + bajadaExtra; 
                //Cantidad de bichos
                Enemigo bicho = null; 
                if (i == 0) {
                    bicho = new EnemigoA(posX, posY);
                    if (skinInvasores == 1) bicho.setSprite("imagenes/Space Invaders/Invaders/space__0001_A2.png");
                } else if (i == 1 || i == 3) {
                    bicho = new EnemigoB(posX, posY);
                    if (skinInvasores == 1) bicho.setSprite("imagenes/Space Invaders/Invaders/space__0003_B2.png");
                } else {
                    bicho = new EnemigoC(posX, posY);
                    if (skinInvasores == 1) bicho.setSprite("imagenes/Space Invaders/Invaders/space__0005_C2.png");
                }
              
                String clave = i + "," + j; 
                flotaE.put(clave, bicho);
                Entidades.add(bicho);
            }
        }
    }
}
