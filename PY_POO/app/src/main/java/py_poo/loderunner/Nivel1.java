package py_poo.loderunner;

public class Nivel1 extends Nivel {

    private static final String[] MAPA = {
    "##########################X#",
    "#  E    $       H        $H#",
    "#===##==H   ====H=     ###H#",
    "#       H       H $       H#",
    "#   $   H   ========   $  H#",
    "#=#== ##H#==#       ======##",
    "#       H                  #",
    "#   $E  H --------  E      #",
    "# ======H        ======    #",
    "#       H $       $        #",
    "##=== ======H  ===##====== #",
    "#           H              #",
    "#        ===H===           #",
    "#           H              #",
    "#      $  P H      $       #",
    "############################"
    };

    public Nivel1() {
        super(1, MAPA);
    }
}
