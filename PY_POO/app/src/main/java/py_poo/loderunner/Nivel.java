package py_poo.loderunner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import py_poo.entities.Moneda;
import py_poo.entities.ObjetoGrafico;
import py_poo.utils.CargadorRecursos;

public class Nivel {
    public static final char VACIO = ' ';
    public static final char LADRILLO = '#';
    public static final char LADRILLO_SOLIDO = 'X';
    public static final char ESCALERA = 'E';
    public static final char BARRA = '-';
    public static final char MONEDA = 'O';
    public static final char AGUJERO = 'A';
    public static final char GUARDIA = 'G';
    public static final char RECOLECTOR = 'R';
    public static final char PUERTA = 'P';

    protected int Numero;
    protected String[] Mapa;
    protected List<ObjetoGrafico> Entidad;
    protected int tileSize;
    protected BufferedImage tileset;
    protected CargadorRecursos cargador;
    protected Map<Character, Integer> tileIndices;

    private char[][] grid;
    private int gridWidth;
    private int gridHeight;

    private static final Color C_LADRILLO = new Color(180, 80, 30);
    private static final Color C_LADRILLO_SOLIDO = new Color(80, 80, 80);
    private static final Color C_ESCALERA = new Color(200, 180, 50);
    private static final Color C_BARRA = new Color(100, 150, 200);
    private static final Color C_MONEDA = new Color(255, 215, 0);
    private static final Color C_AGUJERO = new Color(10, 10, 10);
    private static final Color C_GUARDIA = new Color(220, 50, 50);
    private static final Color C_RECOLECTOR = new Color(50, 120, 220);
    private static final Color C_PUERTA = new Color(100, 200, 100);

    public Nivel() {
        this.Entidad = new ArrayList<>();
        this.tileSize = 40;
        this.cargador = new CargadorRecursos();
        this.tileIndices = new HashMap<>();
        configurarIndicesPorDefecto();
    }

    protected void configurarIndicesPorDefecto() {
        tileIndices.put(LADRILLO, 0);
        tileIndices.put(LADRILLO_SOLIDO, 1);
        tileIndices.put(ESCALERA, 2);
        tileIndices.put(BARRA, 3);
        tileIndices.put('B', 3);
        tileIndices.put(AGUJERO, 4);
        tileIndices.put(PUERTA, 5);
    }

    public void cargarTileset(String ruta) {
        this.tileset = cargador.cargarImagen(ruta);
    }

    public void setTileset(BufferedImage tileset) {
        this.tileset = tileset;
    }

    public void cargar() {
        Entidad.clear();
        if (Mapa == null || Mapa.length == 0) return;

        gridHeight = Mapa.length;
        gridWidth = 0;
        for (String row : Mapa) {
            if (row.length() > gridWidth) gridWidth = row.length();
        }

        grid = new char[gridHeight][gridWidth];

        for (int y = 0; y < gridHeight; y++) {
            String row = Mapa[y];
            for (int x = 0; x < gridWidth; x++) {
                char c = (x < row.length()) ? row.charAt(x) : VACIO;
                grid[y][x] = c;
                int px = x * tileSize;
                int py = y * tileSize;

                if (c == GUARDIA) {
                    Guardia guardia = new Guardia();
                    guardia.setX(px);
                    guardia.setY(py);
                    guardia.setDimension(new java.awt.Dimension(tileSize, tileSize));
                    Entidad.add(guardia);
                } else if (c == MONEDA) {
                    Moneda moneda = new Moneda();
                    moneda.setX(px + tileSize / 4);
                    moneda.setY(py + tileSize / 4);
                    moneda.setDimension(new java.awt.Dimension(tileSize / 2, tileSize / 2));
                    Entidad.add(moneda);
                }
            }
        }
    }

    public void actualizar() {
        for (ObjetoGrafico ent : Entidad) {
            if (ent instanceof Guardia) {
                ((Guardia) ent).perseguirHeroe();
            }
        }
    }

    public void renderizar(Graphics g) {
        if (grid == null) return;

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                int px = x * tileSize;
                int py = y * tileSize;
                dibujarTile(g, grid[y][x], px, py);
            }
        }

        for (ObjetoGrafico ent : Entidad) {
            if (ent instanceof Guardia) {
                g.setColor(C_GUARDIA);
                g.fillRect((int) ent.getX(), (int) ent.getY(), tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect((int) ent.getX(), (int) ent.getY(), tileSize, tileSize);
                g.setColor(Color.WHITE);
                g.fillOval((int) ent.getX() + tileSize / 4, (int) ent.getY() + 4, tileSize / 4, tileSize / 4);
            } else if (ent instanceof Moneda) {
                g.setColor(C_MONEDA);
                g.fillOval((int) ent.getX(), (int) ent.getY(), tileSize / 2, tileSize / 2);
                g.setColor(new Color(200, 170, 0));
                g.drawOval((int) ent.getX(), (int) ent.getY(), tileSize / 2, tileSize / 2);
            }
        }
    }

    private void dibujarTile(Graphics g, char c, int x, int y) {
        if (tileset != null && tileIndices.containsKey(c)) {
            int index = tileIndices.get(c);
            int sx = index * tileSize;
            g.drawImage(tileset, x, y, x + tileSize, y + tileSize,
                       sx, 0, sx + tileSize, tileSize, null);
            return;
        }
        switch (c) {
            case LADRILLO:
                g.setColor(C_LADRILLO);
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, tileSize, tileSize);
                g.drawLine(x, y + tileSize / 2, x + tileSize, y + tileSize / 2);
                g.drawLine(x + tileSize / 2, y, x + tileSize / 2, y + tileSize / 2);
                break;

            case LADRILLO_SOLIDO:
                g.setColor(C_LADRILLO_SOLIDO);
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, tileSize, tileSize);
                g.drawLine(x + 2, y + 2, x + tileSize - 2, y + tileSize - 2);
                g.drawLine(x + tileSize - 2, y + 2, x + 2, y + tileSize - 2);
                break;

            case ESCALERA:
                g.setColor(C_ESCALERA);
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, tileSize, tileSize);
                g.drawLine(x + 4, y, x + 4, y + tileSize);
                g.drawLine(x + tileSize - 4, y, x + tileSize - 4, y + tileSize);
                for (int i = 1; i < 4; i++) {
                    int ry = y + (tileSize * i / 4);
                    g.drawLine(x + 2, ry, x + tileSize - 2, ry);
                }
                break;

            case BARRA:
            case 'B':
                g.setColor(new Color(60, 60, 70));
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(C_BARRA);
                g.fillRect(x + 2, y + tileSize / 2 - 3, tileSize - 4, 6);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, tileSize, tileSize);
                break;

            case AGUJERO:
                g.setColor(C_AGUJERO);
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(new Color(30, 30, 30));
                g.drawRect(x, y, tileSize, tileSize);
                break;

            case PUERTA:
                g.setColor(C_PUERTA);
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, tileSize, tileSize);
                g.drawOval(x + tileSize / 4, y + tileSize / 4, tileSize / 2, tileSize / 2);
                break;

            case VACIO:
                break;
        }
    }

    public void finalizarNivel() {
        Entidad.clear();
        grid = null;
        tileset = null;
    }

    public char getTile(int x, int y) {
        if (grid == null || y < 0 || y >= gridHeight || x < 0 || x >= gridWidth)
            return VACIO;
        return grid[y][x];
    }

    public int getGridWidth() { return gridWidth; }
    public int getGridHeight() { return gridHeight; }
    public int getTileSize() { return tileSize; }
    public int getNumero() { return Numero; }
}
