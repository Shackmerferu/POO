package py_poo.spaceinvaders;

import java.util.HashMap;
import java.util.List;
import py_poo.entities.ObjetoGrafico;


public class NivelSpaceInvaders {
    public void generarOleadas(HashMap<String, Enemigo> flotaE, List<ObjetoGrafico> Entidades, int nivelActual) {
        int filas = 4;
        int columnas = 10;
        
        
        int bajadaExtra = nivelActual * 40; 
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int posX = 50 + (j * 50); 
                int posY = 50 + (i * 40) + bajadaExtra; 
                
                Enemigo bicho = null; 
                if (i == 0) {
                    bicho = new EnemigoA(posX, posY); 
                } else if (i == 1 || i == 2) {
                    bicho = new EnemigoB(posX, posY); 
                } else {
                    bicho = new EnemigoC(posX, posY); 
                }
              
                String clave = i + "," + j; 
                flotaE.put(clave, bicho);
                Entidades.add(bicho);
            }
        }
    }
}
