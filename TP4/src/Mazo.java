import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private ArrayList<Naipe> Naipes;

  public Mazo(){
        Naipes=new ArrayList<Naipe>();
    }
    public void agregar(Naipe card){
        Naipes.add(card);
    }
    public void mezclar(){
         Collections.shuffle(Naipes);
    }
    @Override
    public String toString() {
        String resultado = "";
        for(int i=0;i<Naipes.size();i++){
        resultado += Naipes.get(i)+ "\n";
        }
        return resultado;
    }
}
