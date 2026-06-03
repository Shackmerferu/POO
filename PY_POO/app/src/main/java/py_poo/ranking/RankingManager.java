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
    private static final String DEFAULT_DB_PATH = "app-data/ranking.db";
    private final String dbUrl;
    private List<Integer> puntajes = new ArrayList<>();

    public record RankingEntry(String jugador, String juego,int Nivel, int puntaje, String fecha) {}

    public RankingManager() {
        this(DEFAULT_DB_PATH);
    }

    public RankingManager(String dbPath) {
        this.dbUrl = "jdbc:sqlite:" + dbPath;
        crearCarpetaSiNoExiste(dbPath);
        inicializarTabla();
    }

    public void agregarPuntaje() {
        throw new UnsupportedOperationException("Usar agregarPuntaje(jugador, juego, puntaje)");
    }

    public void agregarPuntaje(String jugador, String juego,int nivel, int puntaje) {
        final String sql = "INSERT INTO ranking (jugador, juego, nivel, puntaje) VALUES (?, ?, ?, ?)";
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

    public void cargarRanking() {
        puntajes = cargarPuntajesTop(null, 10);
    }

    public List<Integer> getPuntajes() {
        return Collections.unmodifiableList(puntajes);
    }

    public List<Integer> cargarPuntajesTop(String juego, int limite) {
        final String sql = (juego == null || juego.isBlank())
            ? "SELECT puntaje FROM ranking ORDER BY puntaje DESC, id ASC LIMIT ?"
            : "SELECT puntaje FROM ranking WHERE juego LIKE ? ORDER BY puntaje DESC, id ASC LIMIT ?";
        List<Integer> top = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (juego != null && !juego.isBlank()) {
                ps.setString(idx++, juego);
            }
            ps.setInt(idx, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    top.add(rs.getInt("puntaje"));
                }
            }
            return top;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo leer el ranking", e);
        }
    }

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

    private void inicializarTabla() {
        final String sql = """
            CREATE TABLE IF NOT EXISTS ranking (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                jugador TEXT NOT NULL,
                juego TEXT NOT NULL,
                 nivel   INTEGER NOT NULL DEFAULT 1,
                puntaje INTEGER NOT NULL,
                fecha TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))
            )
            """;
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }

    private void crearCarpetaSiNoExiste(String dbPath) {
        Path parent = Paths.get(dbPath).toAbsolutePath().getParent();
        if (parent == null) {
            return;
        }
        try {
            Files.createDirectories(parent);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear la carpeta para la base de datos", e);
        }
    }
}
