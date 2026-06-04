package py_poo.ranking;

import py_poo.loderunner.Nivel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingManager {
    //se crea el archivito para guardar en esta direccion
    private static final String DEFAULT_DB_PATH = "app-data/ranking.db";
    // lo que conecta java para usar la SQLlite
    private final String dbUrl;
    // lista temporal para los puntajes
    private List<Integer> puntajes = new ArrayList<>();

    public record RankingEntry(String jugador, String juego,int Nivel, int puntaje, String fecha) {}

    public RankingManager() {
        this(DEFAULT_DB_PATH);
    }
    // contructor donde se configura la ruta y crea la carpeta si no exite y crea la tabla
    public RankingManager(String dbPath) {
        // para conectar la java cpn SQL es necesario el "jdbc:sqlite:"
        this.dbUrl = "jdbc:sqlite:" + dbPath;
        crearCarpetaSiNoExiste(dbPath);
        inicializarTabla();
    }
    //
    public void agregarPuntaje() {
        throw new UnsupportedOperationException("Usar agregarPuntaje(jugador, juego, puntaje)");
    }
    // guarda los datos de quien jugo en que  juego y el nivel y puntaje
    public void agregarPuntaje(String jugador, String juego,int nivel, int puntaje) {
        final String sql = "INSERT INTO ranking (jugador, juego, nivel, puntaje) VALUES (?, ?, ?, ?)";
        //try catch que asegura que la conexion a la BD se cierre atomaticamente al terminar
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jugador);
            ps.setString(2, juego);
            ps.setInt(3, nivel);
            ps.setInt(4, puntaje);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo guardar el puntaje", e);
        }
    }

    public void guardarRanking() {
        // En SQLite los datos quedan persistidos al ejecutar INSERT/UPDATE/DELETE.
    }
        // carga los mejores 10 puntajes de cualquier juego a la lista, total es la lista temporal
    public void cargarRanking() {
        puntajes = cargarPuntajesTop(null, 10);
    }
// devuelve solo para que se lean los puntos
    public List<Integer> getPuntajes() {
        return Collections.unmodifiableList(puntajes);
    }
    // busca los valores mas altos, tenes que pasarle el juevo para asi solo pasa los puntos de ese juego
    public List<Integer> cargarPuntajesTop(String juego, int limite) {
        final String sql = (juego == null || juego.isBlank())
                //consulta de sql para ordenarlos de mayor a menor y el limite es para solo pedir los 10
                //tambien aca la consulta busca por el juego que pongamos gracias al LIKE ?(aca va el juego)
            ? "SELECT puntaje FROM ranking ORDER BY puntaje DESC, id ASC LIMIT ?"
            : "SELECT puntaje FROM ranking WHERE juego LIKE ? ORDER BY puntaje DESC, id ASC LIMIT ?";
        List<Integer> top = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            // Si nos pidieron un juego específico, reemplazamos el primer "?" con el nombre del juego
            if (juego != null && !juego.isBlank()) {
                ps.setString(idx++, juego);
            }
            // Reemplazamos el último "?" con el límite
            ps.setInt(idx, limite);

            // Ejecutamos la consulta y recibimos las filas resultantes en 'rs' que es tipo resultset
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    top.add(rs.getInt("puntaje")); // Agregamos cada puntaje a la lista
                }
            }
            return top;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo leer el ranking", e);
        }
    }

    // Hace lo mismo que el método anterior, pero en lugar de devolver solo el número,
    // devuelve TODOS los detalles (nombre, nivel, fecha) creando objetos 'RankingEntry'
    public List<RankingEntry> cargarDetalleTop(String juego, int limite) {
        final String sql = (juego == null || juego.isBlank())
                ? "SELECT jugador, juego, nivel, puntaje, fecha FROM ranking ORDER BY puntaje DESC, id ASC LIMIT ?"
                : "SELECT jugador, juego, nivel, puntaje, fecha FROM ranking WHERE juego LIKE ? ORDER BY puntaje DESC, id ASC LIMIT ?";

        List<RankingEntry> top = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (juego != null && !juego.isBlank()) {
                ps.setString(idx++, juego);
            }
            ps.setInt(idx, limite);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Por cada fila que encuentra en la base de datos, arma un objeto nuevo
                    top.add(new RankingEntry(
                            rs.getString("jugador"),
                            rs.getString("juego"),
                            rs.getInt("nivel"),
                            rs.getInt("puntaje"),
                            rs.getString("fecha")
                    ));
                }
            }
            return top;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo leer el detalle del ranking", e);
        }
    }



    // Crea la tabla adentro de la base de datos si es la primera vez que se abre el juego
    private void inicializarTabla() {
        // Triple comilla (""") permite escribir textos en varias líneas cómodamente en Java
        final String sql = """
            CREATE TABLE IF NOT EXISTS ranking (
                id INTEGER PRIMARY KEY AUTOINCREMENT,   -- Un ID único que se suma solo (1, 2, 3...)
                jugador TEXT NOT NULL,                  -- Nombre del jugador (Obligatorio)
                juego TEXT NOT NULL,                    -- Nombre del juego (Obligatorio)
                nivel INTEGER NOT NULL DEFAULT 1,       -- Nivel alcanzado (Por defecto es 1)
                puntaje INTEGER NOT NULL,               -- Puntos logrados
                fecha TEXT NOT NULL DEFAULT (datetime('now', 'localtime')) -- Guarda la fecha y hora exacta sola
            )
            """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql); // Ejecuta el comando para crear la tabla
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }

    // Crea la carpeta "app-data" en tu computadora si no existe, para evitar que SQLite tire error
    private void crearCarpetaSiNoExiste(String dbPath) {
        Path parent = Paths.get(dbPath).toAbsolutePath().getParent();
        if (parent == null) {
            return;
        }
        try {
            Files.createDirectories(parent); // Comando de Java para crear carpetas
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear la carpeta para la base de datos", e);
        }
    }
}