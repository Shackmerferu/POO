package py_poo.loderunner;

public class Nivel3 extends Nivel {

    private static final String[] MAPA = {
        "##########################X#",
        "# $    --------      $    H#",
        "#=======       H=======   H#",
        "#      H   $   H       $  H#",
        "# ---- H=======H ---------H#",
        "#    $ H       H       E  H#",
        "#======H  $ $  H ==========#",
        "#      H-------H           #",
        "# $    H       H    $      #",
        "#======H ===== H=======H   # ",
        "#      H   $   H       H   #",
        "# ---- H=======H ----- H   #",
        "#      H       H     $ H   #",
        "#======H ----- H=========  #",
        "#P     H    $  H        E  #",
        "############################"
    };

    public Nivel3() {
        super(3, MAPA);
    }
}
