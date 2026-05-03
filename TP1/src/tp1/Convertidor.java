package tp1;

public class Convertidor {
    public  static void main(String[] args) {
        double valor = Double.parseDouble(args[0]);
        String escala = args[1];

        Temperatura conv = new Temperatura();

        double c, f, k;

        if(escala.equalsIgnoreCase("C")){
            c = valor;
            f = conv.CaF(c);
            k = conv.CaK(c);
        }
        else if(escala.equalsIgnoreCase("F")){
            c = conv.FaC(valor);
            f = valor;
            k = conv.CaK(c);
        }
        else{
            c = conv.KaC(valor);
            f = conv.CaF(c);
            k = valor;
        }

        System.out.println(f + " °F, " + k + " °K, " + c + " °C");
    }
    }

