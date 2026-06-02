package py_poo.loderunner;

import java.util.List;

import py_poo.entities.ObjetoGrafico;

public abstract class Nivel {
    public static final char VACIO = ' ';
    public static final char LADRILLO = '#';
    public static final char LADRILLO_IRROMPIBLE = 'X';
    public static final char ESCALERA = 'E';
    public static final char BARRA = '-';
    public static final char MONEDA = 'O';
    public static final char AGUJERO = 'A';
    public static final char GUARDIA = 'G';
    public static final char RECOLECTOR = 'R';
    public static final char PUERTA = 'P';

    protected int Numero;
    protected String[] Mapa;
    protected char[][] mapa;
    protected int tile_size = 40;
    protected List<ObjetoGrafico> Entidades;

    public int getTile_size() {
        return tile_size;
    }

    public char getTile(int x, int y) {
        return mapa[x][y];
    }

    public void agregarEntidad(ObjetoGrafico entidad) {
        if (Entidades != null) {
            Entidades.add(entidad);
        }
    }

    public void cargar() {
    }

    public void actualizar() {
    }

    public void renderizar() {
    }

    public void finalizarNivel() {
    }
}
