package tp4;

import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private final ArrayList<Naipe> cartas = new ArrayList<>();

    public Mazo(){
        for (Palo p: Palo.values()){
            if(p != Palo.COMODIN){
                for (short v = 1; v <= 13; v++) {
                    cartas.add(new Naipe(p, v));
                }
            }
        }
        cartas.add(new Naipe(Palo.COMODIN, (short) 0));
        cartas.add(new Naipe(Palo.COMODIN, (short) 0));
    }

    public void agregarNaipe(Palo p, short v){
        cartas.add(new Naipe(p, v));
        System.out.println(Naipe.getUltimo());
    }

    @Override
    public String toString() {
        return cartas.toString();
    }

    public void mezclar(){
        Collections.shuffle(cartas);
    }
}