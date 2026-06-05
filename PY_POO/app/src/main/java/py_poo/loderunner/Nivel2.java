package py_poo.loderunner;

public class Nivel2 extends Nivel {

    private static final String[] MAPA = {
        "##########################X#",
        "# $  E   H=====H      $   H#",
        "#======= H     HHH======= H#",
        "#      H H  $    H        H#",
        "# ---- H ======  H -----  H#",
        "#    $ H         H   $    H#",
        "#  ====H -----   H====== H##",
        "#   E  H   $     H   ----H #",
        "#===== H=======  H====   H #",
        "#      H         H         #",
        "#======H -----   H -----   #",
        "#      H   $     H   E     #",
        "# ---- H=========H=======  #",
        "#      H         H         #",
        "#P     H    $    H         #",
        "############################"
    };

    public Nivel2() {
        super(2, MAPA);
    }
}
