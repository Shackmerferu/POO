package py_poo.entities;

import java.util.ArrayList;

import py_poo.interfaces.Movible;

public class Paleta extends ObjetoGrafico implements Movible {
    private ArrayList<Integer> Segmento = new ArrayList<>();
    
    @Override
    public void Mover() {
    }

    public void ResetearPOS(){} //devuelve a la posicion inicial las paletas despues del punto

}
