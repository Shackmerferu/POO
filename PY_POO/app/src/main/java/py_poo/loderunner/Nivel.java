package py_poo.loderunner;

import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Agujero;
import py_poo.entities.Barra;
import py_poo.entities.Escalera;
import py_poo.entities.Ladrillo;
import py_poo.entities.Moneda;
import py_poo.entities.ObjetoGrafico;
public class Nivel {
    public static final char VACIO = ' ';
    public static final char LADRILLO = '=';
    public static final char LADRILLO_IRROMPIBLE = '#';
    public static final char ESCALERA = 'H';
    public static final char BARRA = '-';
    public static final char MONEDA = '$';
    public static final char AGUJERO = 'A';
    public static final char GUARDIA = 'E';
    public static final char RECOLECTOR = 'P';
    public static final char PUERTA = 'X';

    protected int Numero;
    protected String[] Mapa;
    protected char[][] mapa;
    protected int tile_size = 40;
    protected List<ObjetoGrafico> Entidades;
    protected List<Ladrillo> ladrillos;
    protected List<Ladrillo> ladrillosIrrompibles;
    protected List<Escalera> escaleras;
    protected List<Barra> barras;
    protected List<Moneda> monedas;
    protected List<Agujero> agujeros;
    protected int escapeLadderX = -1;
    protected int escapeLadderY = -1;
    protected boolean escapeLadderActiva;
    protected int spawnRecolectorX;
    protected int spawnRecolectorY;
    protected List<int[]> spawnGuardias;
    protected int totalOro;

    public Nivel() {
        this(0, null);
    }

    public Nivel(int numero, String[] mapaData) {
        this.Numero = numero;
        this.Mapa = mapaData;
        this.Entidades = new ArrayList<>();
        this.ladrillos = new ArrayList<>();
        this.ladrillosIrrompibles = new ArrayList<>();
        this.escaleras = new ArrayList<>();
        this.barras = new ArrayList<>();
        this.monedas = new ArrayList<>();
        this.agujeros = new ArrayList<>();
        this.spawnGuardias = new ArrayList<>();
        this.totalOro = 0;
    }

    public int getNumero() { return Numero; }
    public int getTile_size() { return tile_size; }

    public char getTile(int x, int y) {
        if (x < 0 || x >= mapa.length || y < 0 || y >= mapa[0].length) return LADRILLO_IRROMPIBLE;
        return mapa[x][y];
    }

    public boolean esSolido(int x, int y) {
        char t = getTile(x, y);
        return t == LADRILLO || t == LADRILLO_IRROMPIBLE;
    }

    public boolean esLadrilloCavable(int x, int y) {
        char t = getTile(x, y);
        return t == LADRILLO;
    }

    public boolean esEscalera(int x, int y) {
        return getTile(x, y) == ESCALERA;
    }

    public boolean esBarra(int x, int y) {
        return getTile(x, y) == BARRA;
    }

    public boolean esMoneda(int x, int y) {
        return getTile(x, y) == MONEDA;
    }

    public boolean esVacio(int x, int y) {
        return getTile(x, y) == VACIO;
    }

    public void setTile(int x, int y, char c) {
        if (x >= 0 && x < mapa.length && y >= 0 && y < mapa[0].length) {
            mapa[x][y] = c;
        }
    }

    public void agregarEntidad(ObjetoGrafico entidad) {
        if (Entidades != null) {
            Entidades.add(entidad);
        }
    }

    public void cargar() {
        if (Mapa == null) return;
        int filas = Mapa.length;
        int cols = Mapa[0].length();
        mapa = new char[cols][filas];
        spawnGuardias.clear();
        ladrillos.clear();
        ladrillosIrrompibles.clear();
        escaleras.clear();
        barras.clear();
        monedas.clear();
        agujeros.clear();
        totalOro = 0;

        for (int y = 0; y < filas; y++) {
            String fila = Mapa[y];
            for (int x = 0; x < cols && x < fila.length(); x++) {
                char c = fila.charAt(x);
                mapa[x][y] = c;
                switch (c) {
                    case LADRILLO:
                        Ladrillo l = new Ladrillo(x, y, tile_size, false);
                        ladrillos.add(l);
                        Entidades.add(l);
                        break;
                    case LADRILLO_IRROMPIBLE:
                        Ladrillo li = new Ladrillo(x, y, tile_size, true);
                        ladrillosIrrompibles.add(li);
                        Entidades.add(li);
                        break;
                    case ESCALERA:
                        Escalera e = new Escalera(x, y, tile_size);
                        escaleras.add(e);
                        Entidades.add(e);
                        break;
                    case BARRA:
                        Barra b = new Barra(x, y, tile_size);
                        barras.add(b);
                        Entidades.add(b);
                        break;
                    case MONEDA:
                        Moneda m = new Moneda(x, y, tile_size);
                        monedas.add(m);
                        Entidades.add(m);
                        totalOro++;
                        break;
                    case RECOLECTOR:
                        spawnRecolectorX = x;
                        spawnRecolectorY = y;
                        mapa[x][y] = VACIO;
                        break;
                    case GUARDIA:
                        spawnGuardias.add(new int[]{x, y});
                        mapa[x][y] = VACIO;
                        break;
                    case PUERTA:
                        escapeLadderX = x;
                        escapeLadderY = y;
                        mapa[x][y] = VACIO;
                        break;
                }
            }
        }
    }

    public void actualizar() {
        for (Ladrillo l : ladrillos) l.actualizar();
        for (Escalera e : escaleras) e.actualizar();
        for (Moneda m : monedas) m.actualizar();
        List<Agujero> aEliminar = new ArrayList<>();
        for (Agujero a : agujeros) {
            a.actualizar();
            if (!a.isAbierto()) aEliminar.add(a);
        }
        agujeros.removeAll(aEliminar);
    }

    public void renderizar() {}

    public void activarEscape() {
        if (escapeLadderX < 0) return;
        escapeLadderActiva = true;
        for (int yy = 0; yy <= escapeLadderY; yy++) {
            char existing = mapa[escapeLadderX][yy];
            if (existing == ESCALERA) continue;
            mapa[escapeLadderX][yy] = ESCALERA;
            if (existing == LADRILLO || existing == LADRILLO_IRROMPIBLE) {
                Ladrillo toRemove = null;
                List<Ladrillo> lista = existing == LADRILLO ? ladrillos : ladrillosIrrompibles;
                for (Ladrillo l : lista) {
                    if ((int)(l.getX() / tile_size) == escapeLadderX && (int)(l.getY() / tile_size) == yy) {
                        toRemove = l; break;
                    }
                }
                if (toRemove != null) {
                    lista.remove(toRemove);
                    Entidades.remove(toRemove);
                }
            }
            Escalera es = new Escalera(escapeLadderX, yy, tile_size);
            escaleras.add(es);
            Entidades.add(es);
        }
    }

    public void finalizarNivel() {
        if (Entidades != null) Entidades.clear();
    }

    public int getAnchoMapa() { return mapa != null ? mapa.length : 0; }
    public int getAltoMapa() { return mapa != null ? mapa[0].length : 0; }
    public int getAnchoPixels() { return getAnchoMapa() * tile_size; }
    public int getAltoPixels() { return getAltoMapa() * tile_size; }

    public Moneda getMonedaEn(int x, int y) {
        for (Moneda m : monedas) {
            if (!m.isRecolectada() && m.getBounds().contains(x, y)) return m;
        }
        return null;
    }

    public boolean cavarEn(int tileX, int tileY) {
        if (esLadrilloCavable(tileX, tileY)) {
            setTile(tileX, tileY, VACIO);
            Ladrillo aEliminar = null;
            for (Ladrillo l : ladrillos) {
                int lx = (int)(l.getX() / tile_size);
                int ly = (int)(l.getY() / tile_size);
                if (lx == tileX && ly == tileY) {
                    l.romper();
                    aEliminar = l;
                    break;
                }
            }
            if (aEliminar != null) {
                ladrillos.remove(aEliminar);
                Entidades.remove(aEliminar);
            }
            Agujero agujero = new Agujero(tileX * tile_size, tileY * tile_size);
            agujeros.add(agujero);
            return true;
        }
        return false;
    }
}
