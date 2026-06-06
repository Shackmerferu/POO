package py_poo.loderunner;

import java.util.ArrayList;
import java.util.List;

import py_poo.entities.Agujero;
import py_poo.entities.Barra;
import py_poo.entities.Escalera;
import py_poo.entities.Ladrillo;
import py_poo.entities.Moneda;
import py_poo.entities.ObjetoGrafico;
import py_poo.entities.ParticulaLadrillo;
import py_poo.entities.Puerta;
public class Nivel {
    // caracteres del mapa para cada tipo de tile
    public static final char VACIO = ' '; // espacio vacío
    public static final char LADRILLO = '='; // ladrillo destruible
    public static final char LADRILLO_IRROMPIBLE = '#'; // ladrillo indestructible
    public static final char ESCALERA = 'H'; // escalera
    public static final char BARRA = '-'; // barra horizontal
    public static final char MONEDA = '$'; // oro
    public static final char AGUJERO = 'A'; // agujero
    public static final char GUARDIA = 'E'; // enemigo guardia
    public static final char RECOLECTOR = 'P'; // jugador
    public static final char PUERTA = 'X'; // puerta de salida

    protected int Numero; // número de nivel
    protected String[] Mapa; // datos del mapa en crudo
    protected char[][] mapa; // matriz de tiles del nivel
    protected int tile_size = 40; // tamaño de cada tile en píxeles
    protected List<ObjetoGrafico> Entidades; // lista de entidades gráficas del nivel
    protected List<Ladrillo> ladrillos; // ladrillos destruibles
    protected List<Ladrillo> ladrillosIrrompibles; // ladrillos indestructibles
    protected List<Escalera> escaleras; // escaleras del nivel
    protected List<Barra> barras; // barras horizontales
    protected List<Moneda> monedas; // monedas de oro
    protected List<Agujero> agujeros; // agujeros activos
    protected List<ParticulaLadrillo> particulas; // partículas de ladrillos rotos
    protected int escapeLadderX = -1; // tile X de la escalera de escape
    protected int escapeLadderY = -1; // tile Y de la escalera de escape
    protected boolean escapeLadderActiva; // true si la escalera de escape se activó
    protected int spawnRecolectorX; // tile X de spawn del jugador
    protected int spawnRecolectorY; // tile Y de spawn del jugador
    protected Puerta puertaSalida; // puerta de salida del nivel
    protected List<int[]> spawnGuardias; // posiciones de spawn de los guardias
    protected int totalOro; // total de monedas en el nivel
    public int tiempoLimite = 120; // tiempo límite del nivel en segundos

    // constructor vacío
    public Nivel() {
        this(0, null);
    }

    // constructor con número de nivel y datos del mapa
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
        this.particulas = new ArrayList<>();
        this.spawnGuardias = new ArrayList<>();
        this.totalOro = 0;
    }

    public int getNumero() { return Numero; } // retorna número de nivel
    public int getTile_size() { return tile_size; } // retorna tamaño de tile

    // retorna el caracter del tile en (x, y), fuera del mapa retorna irrompible
    public char getTile(int x, int y) {
        if (x < 0 || x >= mapa.length || y < 0 || y >= mapa[0].length) return LADRILLO_IRROMPIBLE;
        return mapa[x][y];
    }

    // true si el tile es sólido (ladrillo o irrompible)
    public boolean esSolido(int x, int y) {
        char t = getTile(x, y);
        return t == LADRILLO || t == LADRILLO_IRROMPIBLE;
    }

    // true si el tile es un ladrillo cavable (destruible)
    public boolean esLadrilloCavable(int x, int y) {
        char t = getTile(x, y);
        return t == LADRILLO;
    }

    // true si el tile es escalera
    public boolean esEscalera(int x, int y) {
        return getTile(x, y) == ESCALERA;
    }

    // true si el tile es barra
    public boolean esBarra(int x, int y) {
        return getTile(x, y) == BARRA;
    }

    // true si el tile es moneda
    public boolean esMoneda(int x, int y) {
        return getTile(x, y) == MONEDA;
    }

    // true si el tile está vacío
    public boolean esVacio(int x, int y) {
        return getTile(x, y) == VACIO;
    }

    // asigna un caracter en la posición (x, y) del mapa
    public void setTile(int x, int y, char c) {
        if (x >= 0 && x < mapa.length && y >= 0 && y < mapa[0].length) {
            mapa[x][y] = c;
        }
    }

    // agrega una entidad gráfica a la lista
    public void agregarEntidad(ObjetoGrafico entidad) {
        if (Entidades != null) {
            Entidades.add(entidad);
        }
    }

    // carga el mapa desde los datos, creando todas las entidades del nivel
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
        particulas.clear();
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
                        puertaSalida = null;
                        Ladrillo liPuerta = new Ladrillo(x, y, tile_size, true);
                        ladrillosIrrompibles.add(liPuerta);
                        Entidades.add(liPuerta);
                        mapa[x][y] = LADRILLO_IRROMPIBLE;
                        break;
                }
            }
        }
    }

    // actualiza estado de todas las entidades del nivel cada frame
    public void actualizar() {
        for (Ladrillo l : ladrillos) l.actualizar();
        for (Ladrillo l : ladrillosIrrompibles) l.actualizar();
        for (Escalera e : escaleras) e.actualizar();
        for (Moneda m : monedas) m.actualizar();
        List<Agujero> aEliminar = new ArrayList<>();
        for (Agujero a : agujeros) {
            a.actualizar(); // actualiza contador del agujero
            if (!a.isAbierto()) { // si se cerró
                Ladrillo l = a.getLadrilloAsociado();
                if (l != null) {
                    l.iniciarRegen(); // regenera el ladrillo
                    int lx = (int)(l.getX() / tile_size);
                    int ly = (int)(l.getY() / tile_size);
                    setTile(lx, ly, LADRILLO); // restaura el tile
                }
                aEliminar.add(a); // marca para eliminar
            }
        }
        agujeros.removeAll(aEliminar); // elimina agujeros cerrados
        List<ParticulaLadrillo> pEliminar = new ArrayList<>();
        for (ParticulaLadrillo p : particulas) {
            p.actualizar();
            if (!p.isActivo()) pEliminar.add(p); // partículas que expiraron
        }
        particulas.removeAll(pEliminar);
        Entidades.removeAll(pEliminar);
    }

    public void renderizar() {} // renderizado del nivel

    // activa la escalera de escape cuando se recolecta todo el oro
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
            if (yy == escapeLadderY) {
                puertaSalida = new Puerta(escapeLadderX, escapeLadderY, tile_size);
                puertaSalida.mostrar();
                Entidades.add(puertaSalida);
            } else {
                Escalera es = new Escalera(escapeLadderX, yy, tile_size);
                escaleras.add(es);
                Entidades.add(es);
            }
        }
    }

    // limpia todas las entidades al finalizar el nivel
    public void finalizarNivel() {
        if (Entidades != null) Entidades.clear();
    }

    public int getAnchoMapa() { return mapa != null ? mapa.length : 0; } // ancho del mapa en tiles
    public int getAltoMapa() { return mapa != null ? mapa[0].length : 0; } // alto del mapa en tiles
    public int getAnchoPixels() { return getAnchoMapa() * tile_size; } // ancho en píxeles
    public int getAltoPixels() { return getAltoMapa() * tile_size; } // alto en píxeles

    // busca una moneda no recolectada en la posición (x, y)
    public Moneda getMonedaEn(int x, int y) {
        for (Moneda m : monedas) {
            if (!m.isRecolectada() && m.getBounds().contains(x, y)) return m;
        }
        return null;
    }

    // cava un ladrillo en la posición (tileX, tileY), creando un agujero
    public boolean cavarEn(int tileX, int tileY) {
        if (!esLadrilloCavable(tileX, tileY)) return false;
        Ladrillo ladrillo = null;
        for (Ladrillo l : ladrillos) {
            int lx = (int)(l.getX() / tile_size);
            int ly = (int)(l.getY() / tile_size);
            if (lx == tileX && ly == tileY) {
                if (l.getEstado() != Ladrillo.Estado.NORMAL) return false;
                ladrillo = l;
                break;
            }
        }
        if (ladrillo == null) return false;
        setTile(tileX, tileY, VACIO);
        ladrillo.iniciarBreaking();
        if (tileY > 0) {
            ParticulaLadrillo p = new ParticulaLadrillo(tileX * tile_size, (tileY - 1) * tile_size, tile_size);
            particulas.add(p);
        }
        Agujero agujero = new Agujero(tileX * tile_size, tileY * tile_size, ladrillo);
        agujeros.add(agujero);
        return true;
    }
}
