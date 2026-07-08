package py_poo.loderunner;

// Nivel 3 del Lode Runner - dificultad alta con diseño complejo
public class Nivel3 extends Nivel {

    // mapa del nivel 3
    private static final String[] MAPA = {
        "##########################X#",
        "# $  E --------      $    H#",
        "#=======       H=======   H#",
        "#      H   $   H       $  H#",
        "# ---- H=======H ---------H#",
        "#    $ H       H       E  H#",
        "#======H       H ==========#",
        "#      H-------H           #",
        "# $    H       H    $      #",
        "#======H ===== H=======H   # ",
        "#      H   $   H       H   #",
        "#   $  H ===== H ----- H   #",
        "#======H       H           #",
        "#P     H       H        E  #",
        "#==========================#",
        "############################"
    };

    public Nivel3() {
        super(3, MAPA);
    }
}
