package py_poo.loderunner;

// Nivel 1 del Lode Runner - nivel inicial de dificultad baja
public class Nivel1 extends Nivel {

    // mapa del nivel 1: #=irrompible, ==ladrillo, H=escalera, -=barra, $=oro, E=guardia, P=jugador, X=puerta
    private static final String[] MAPA = {
    "##########################X#",
    "#  E     =  $        ---  $H#",
    "#===##==H=======H===   ###H#",
    "#       H                 H#",
    "#   $   H              $  H#",
    "#=#== ##H#==#       ======##",
    "#       H                  #",
    "#   $E  H --------  E      #",
    "# ======H        ======    #",
    "#       H $       $        #",
    "##=== ======H  ===##====== #",
    "#           H              #",
    "#        ===H===           #",
    "#      $  P H      $       #",
    "#==========================#",
    "############################"
    };

    public Nivel1() {
        super(1, MAPA);
    }
}
