package py_poo.loderunner;

public class Nivel2 extends Nivel {

    private static final String[] MAPA = {
        "##########################X#",
        "# $      H=====H      $  H #",
        "#======= H     H ======= H #",
        "#      H H  $  H H       H #",
        "# ---- H H=====H H ----- H #",
        "#    $ H       H H       H #",
        "#======H ----- H H====== H #",
        "#      H   $   H H      HE #",
        "#$$$$$$H=======H H====== H #",
        "#      H       H H       H #",
        "#======H ----- H H ----- H #",
        "#      H   $   H H       H #",
        "# ---- H======== H====== H #",
        "#      H         H       H #",
        "#P     H    $    H      E  #",
        "############################"
    };

    public Nivel2() {
        super(2, MAPA);
    }
}
