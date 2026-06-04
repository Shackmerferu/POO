package py_poo.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ranking.RankingManager;
import py_poo.ranking.RankingManager.RankingEntry;
import py_poo.ui.MenuPrincipal;

public class MenuPong extends MenuPrincipal {
    private int seleccion;
    private RankingManager rankingManager;
    private List<RankingEntry> topRanking;
   

    public MenuPong(InputManager input, Object mouse) {
        super("Pong", "Menú Principal", Color.BLACK, "Jugar", "Salir");
        this.input = input;
        this.seleccion = 0;
        this.rankingManager = new RankingManager();
        this.topRanking = rankingManager.cargarDetalleTop("Pong%", 10);
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public void actualizar() {
    }

    public void dibujar(Graphics g) {
        if (isConfigMode()) {
            dibujarConfig(g);
            return;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(Color.GREEN);
        g.drawString("ARCADE PONG", 260, 100);

        String[] opciones = {"1 JUGADOR (VS IA)", "2 JUGADORES", "CONFIG", "SALIR"};
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g.setColor(Color.YELLOW);
                g.drawString("> " + opciones[i], 100, 200 + i * 35);
            } else {
                g.setColor(Color.WHITE);
                g.drawString("  " + opciones[i], 100, 200 + i * 35);
            }
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.GRAY);
        g.drawString("W/S o Flechas para mover | ENTER para seleccionar", 100, 420);
        g.drawString("Controles: W/S (J1)  |  Flechas Arriba/Abajo (J2)", 100, 440);

        // Dibujar Ranking Top 10
        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.setColor(Color.CYAN);
        g.drawString("--- TOP 10 RANKING ---", 450, 180);

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(Color.WHITE);
        if (topRanking == null || topRanking.isEmpty()) {
            g.drawString("Aún no hay puntajes.", 450, 220);
        } else {
            int y = 220;
            for (int i = 0; i < topRanking.size(); i++) {
                RankingEntry entry = topRanking.get(i);

                String detalle = entry.juego().replace("Pong", "").trim();
                if (detalle.isEmpty()) {
                    detalle = entry.puntaje() + " pts";
                }

                String texto = String.format("%d. %s  %s", (i + 1), entry.jugador(), detalle);
                g.drawString(texto, 450, y);
                y += 20;
            }
        }
    }
}
