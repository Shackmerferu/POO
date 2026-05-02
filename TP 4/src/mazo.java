import java.util.ArrayList;
import java.util.Collections;

public class mazo {
    private ArrayList<naipe> naipes;

    // constructor — inicializa la lista vacía
    public mazo() {
        naipes = new ArrayList<>();
    }

    // agrega un naipe al mazo
    public void agregarNaipe(naipe n) {
        naipes.add(n);
    }

    // mezcla el mazo aleatoriamente usando Collections.shuffle()
    public void mezclar() {
        Collections.shuffle(naipes);
    }

    // toString — muestra todos los naipes del mazo
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Mazo (" + naipes.size() + " naipes) ===\n");
        for (naipe n : naipes) {
            sb.append("  " + n + "\n"); // llama a Naipe.toString() solo
        }
        return sb.toString();
    }
}

