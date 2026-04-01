import java.util.Random;
public class Ramdom {
    private Random nrorandom;

    Ramdom() {
        this.nrorandom = new Random();
    }

    public void setNrorandom(Random nrorandom) {
        this.nrorandom = nrorandom;
    }

    public Random getNrorandom() {
        return nrorandom;

    }

    public int generarNumero() {
        return nrorandom.nextInt(100) + 1;
    }
}