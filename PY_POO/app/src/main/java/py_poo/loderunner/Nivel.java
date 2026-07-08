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

// Representa un nivel del juego, con su mapa de tiles, listas de entidades y lógica de actualización.
// Contiene toda la información del nivel: ladrillos, escaleras, barras, monedas, agujeros, guardias, etc.
public class Nivel {
    // Caracteres del mapa para definir cada tipo de tile en los datos del nivel
    public static final char VACIO = ' '; // espacio vacío (aire)
    public static final char LADRILLO = '='; // ladrillo destruible (se puede cavar)
    public static final char LADRILLO_IRROMPIBLE = '#'; // ladrillo indestructible
    public static final char ESCALERA = 'H'; // escalera vertical
    public static final char BARRA = '-'; // barra horizontal
    public static final char MONEDA = '$'; // oro / moneda recolectable
    public static final char AGUJERO = 'A'; // agujero en el suelo
    public static final char GUARDIA = 'E'; // enemigo guardia
    public static final char RECOLECTOR = 'P'; // posición inicial del jugador
    public static final char PUERTA = 'X'; // puerta de salida del nivel

    protected int Numero; // número identificador del nivel
    protected String[] Mapa; // datos del mapa en crudo (formato de strings)
    protected char[][] mapa; // matriz de tiles del nivel (acceso rápido por coordenadas)
    protected int tile_size = 40; // tamaño en píxeles de cada tile
    protected List<ObjetoGrafico> Entidades; // lista de todas las entidades gráficas activas en el nivel
    protected List<Ladrillo> ladrillos; // ladrillos destruibles del nivel
    protected List<Ladrillo> ladrillosIrrompibles; // ladrillos indestructibles del nivel
    protected List<Escalera> escaleras; // escaleras del nivel
    protected List<Barra> barras; // barras horizontales del nivel
    protected List<Moneda> monedas; // monedas de oro del nivel
    protected List<Agujero> agujeros; // agujeros activos actualmente en el nivel
    protected List<ParticulaLadrillo> particulas; // partículas visuales de ladrillos rotos
    protected int escapeLadderX = -1; // tile X de la escalera de escape (puerta)
    protected int escapeLadderY = -1; // tile Y de la escalera de escape
    protected boolean escapeLadderActiva; // true si la escalera de escape ya se activó (todo el oro recolectado)
    protected int spawnRecolectorX; // tile X donde aparece el jugador al iniciar el nivel
    protected int spawnRecolectorY; // tile Y donde aparece el jugador
    protected Puerta puertaSalida; // puerta de salida (se muestra al activar escape)
    protected List<int[]> spawnGuardias; // lista de posiciones (tileX, tileY) donde aparecen los guardias
    protected int totalOro; // cantidad total de monedas en el nivel
    public int tiempoLimite = 120; // tiempo límite en segundos para completar el nivel

    // Constructor vacío
    public Nivel() {
        this(0, null);
    }

    // Constructor con número de nivel y datos del mapa en formato de strings
    // Inicializa todas las listas de entidades
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

    public int getNumero() { return Numero; }
    public int getTile_size() { return tile_size; }

    // Retorna el caracter del tile en la posición (x, y); si está fuera del mapa retorna LADRILLO_IRROMPIBLE
    public char getTile(int x, int y) {
        if (x < 0 || x >= mapa.length || y < 0 || y >= mapa[0].length) return LADRILLO_IRROMPIBLE;
        return mapa[x][y];
    }

    // True si el tile en (x, y) es sólido (ladrillo destruible o indestructible)
    public boolean esSolido(int x, int y) {
        char t = getTile(x, y);
        return t == LADRILLO || t == LADRILLO_IRROMPIBLE;
    }

    // True si el tile es un ladrillo destruible (se puede cavar)
    public boolean esLadrilloCavable(int x, int y) {
        char t = getTile(x, y);
        return t == LADRILLO;
    }

    // True si el tile es una escalera
    public boolean esEscalera(int x, int y) {
        return getTile(x, y) == ESCALERA;
    }

    // True si el tile es una barra horizontal
    public boolean esBarra(int x, int y) {
        return getTile(x, y) == BARRA;
    }

    // True si el tile es una moneda
    public boolean esMoneda(int x, int y) {
        return getTile(x, y) == MONEDA;
    }

    // True si el tile está vacío
    public boolean esVacio(int x, int y) {
        return getTile(x, y) == VACIO;
    }

    // Asigna un caracter en la posición (x, y) del mapa (con verificación de límites)
    public void setTile(int x, int y, char c) {
        if (x >= 0 && x < mapa.length && y >= 0 && y < mapa[0].length) {
            mapa[x][y] = c;
        }
    }

    // Agrega una entidad gráfica a la lista de entidades del nivel
    public void agregarEntidad(ObjetoGrafico entidad) {
        if (Entidades != null) {
            Entidades.add(entidad);
        }
    }

    // Carga el mapa desde los datos en crudo, creando todas las entidades (ladrillos, escaleras,
    // barras, monedas) y registrando las posiciones de spawn del jugador y los guardias.
    // Limpia cualquier estado previo del nivel.
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
                        mapa[x][y] = VACIO; // el spawn es un tile vacío
                        break;
                    case GUARDIA:
                        spawnGuardias.add(new int[]{x, y});
                        mapa[x][y] = VACIO; // el spawn es un tile vacío
                        break;
                    case PUERTA:
                        escapeLadderX = x;
                        escapeLadderY = y;
                        puertaSalida = null;
                        Ladrillo liPuerta = new Ladrillo(x, y, tile_size, true);
                        ladrillosIrrompibles.add(liPuerta);
                        Entidades.add(liPuerta);
                        mapa[x][y] = LADRILLO_IRROMPIBLE; // la puerta es un tile sólido hasta activarse
                        break;
                }
            }
        }
    }

    // Actualiza el estado de todas las entidades del nivel cada frame:
    // - Actualiza ladrillos, escaleras, monedas
    // - Actualiza contadores de agujeros; cuando un agujero se cierra, regenera el ladrillo
    // - Limpia partículas expiradas
    public void actualizar() {
        for (Ladrillo l : ladrillos) l.actualizar();
        for (Ladrillo l : ladrillosIrrompibles) l.actualizar();
        for (Escalera e : escaleras) e.actualizar();
        for (Moneda m : monedas) m.actualizar();
        List<Agujero> aEliminar = new ArrayList<>();
        for (Agujero a : agujeros) {
            a.actualizar();
            if (!a.isAbierto()) {
                Ladrillo l = a.getLadrilloAsociado();
                if (l != null) {
                    l.iniciarRegen();
                    int lx = (int)(l.getX() / tile_size);
                    int ly = (int)(l.getY() / tile_size);
                    setTile(lx, ly, LADRILLO);
                }
                aEliminar.add(a);
            }
        }
        agujeros.removeAll(aEliminar);
        List<ParticulaLadrillo> pEliminar = new ArrayList<>();
        for (ParticulaLadrillo p : particulas) {
            p.actualizar();
            if (!p.isActivo()) pEliminar.add(p);
        }
        particulas.removeAll(pEliminar);
        Entidades.removeAll(pEliminar);
    }

    public void renderizar() {}

    public void sincronizarEntidades(List<ObjetoGrafico> listaRender) {
        for (Agujero a : agujeros) {
            if (!listaRender.contains(a)) listaRender.add(0, a);
        }
        for (ParticulaLadrillo p : particulas) {
            if (!listaRender.contains(p)) listaRender.add(p);
        }
        for (Escalera e : escaleras) {
            if (!listaRender.contains(e)) listaRender.add(e);
        }
        for (Moneda m : monedas) {
            if (!listaRender.contains(m)) listaRender.add(m);
        }
        if (puertaSalida != null && !listaRender.contains(puertaSalida)) {
            listaRender.add(puertaSalida);
        }
        listaRender.removeIf(e ->
            (e instanceof ParticulaLadrillo && !particulas.contains(e))
            || (e instanceof Agujero && !agujeros.contains(e))
            || (e instanceof Escalera && !escaleras.contains(e))
            || (e instanceof Moneda && !monedas.contains(e))
            || (e instanceof Puerta && e != puertaSalida));
    }

    // Activa la escalera de escape (puerta) cuando el jugador recolectó todo el oro del nivel.
    // Convierte los tiles verticales en escaleras hasta la puerta y muestra la puerta de salida.
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

    // Limpia todas las entidades gráficas del nivel (usado al finalizar o reiniciar el nivel)
    public void finalizarNivel() {
        if (Entidades != null) Entidades.clear();
    }

    public int getAnchoMapa() { return mapa != null ? mapa.length : 0; } // ancho del mapa en tiles
    public int getAltoMapa() { return mapa != null ? mapa[0].length : 0; } // alto del mapa en tiles
    public int getAnchoPixels() { return getAnchoMapa() * tile_size; } // ancho en píxeles
    public int getAltoPixels() { return getAltoMapa() * tile_size; } // alto en píxeles

    // Busca y retorna una moneda no recolectada en la posición de píxeles (x, y)
    public Moneda getMonedaEn(int x, int y) {
        for (Moneda m : monedas) {
            if (!m.isRecolectada() && m.getBounds().contains(x, y)) return m;
        }
        return null;
    }

    // Cava un ladrillo en (tileX, tileY): lo marca como roto, crea un agujero y partículas visuales.
    // Retorna true si se pudo cavar, false si no hay ladrillo cavable allí.
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
