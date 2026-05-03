package tp1;

public class Temperatura {
    double CaF(double c){
        return (c*9/5) +32;
    }
    double CaK(double c){
        return c+273.15;
    }
    double FaC(double f){
        return (f-32) * 5/9;
    }
    double KaC(double k){
        return k-273.15;
    }
}
