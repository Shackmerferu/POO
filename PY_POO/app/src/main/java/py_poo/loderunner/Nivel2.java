package py_poo.loderunner;

// Nivel 2 del Lode Runner - dificultad media con más guardias y escaleras
public class Nivel2 extends Nivel {

    // mapa del nivel 2
    private static final String[] MAPA = {
        "##########################X#",
        "# $  E   H=###=H      $   H#",
        "#======H H     H H========H#",
        "#      H H  $  H H        H#",
        "# ---- H ======= H -----  H#",
        "#    $ H         H   $    H#",
        "#  ====H -----   H====== H##",
        "#   E  H   $     H   ----H #",
        "#==##= H===##==  H====   H #",
        "#      H         H         #",
        "#======H -----   H -----   #",
        "#      H   $     H   E     #",
        "# ---- H=========H=======  #",
        "#P     H    $    H         #",
        "#==========================#",
        "############################"
    };

    public Nivel2() {
        super(2, MAPA);
    }
}
