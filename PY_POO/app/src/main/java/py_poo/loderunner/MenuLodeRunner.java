package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.input.MouseManager;
import py_poo.ranking.RankingManager;
import py_poo.ranking.RankingManager.RankingEntry;
import py_poo.ui.MenuPrincipal;

public class MenuLodeRunner extends MenuPrincipal {
    private int seleccion;
    private RankingManager rankingManager;
    private List<RankingEntry> topRanking;

    public MenuLodeRunner(InputManager input, MouseManager mouse) {
        super("Lode Runner - Menu Principal", "LODE RUNNER", java.awt.Color.GREEN, "J1: W / A / S / D / X", null);
        this.input = input;
        this.seleccion = 0;
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public void actualizar() {
    }

    public void renderizar(Graphics g) {}

    public void dibujar(java.awt.Graphics g) {
        if (isConfigMode()) {
            dibujarConfig(g);
            return;
        }
        
        g.setColor(new Color(25, 27, 34));
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(new Color(255, 210, 60));
        g.drawString("LODE RUNNER", Constantes.WIDTH / 2 - 200, 160);

        g.setFont(new Font("Consolas", Font.PLAIN, 16));
        g.setColor(new Color(200, 200, 200));
        g.drawString("Recolecta todo el oro y escapa por la puerta!", 160, 220);

        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.setColor(new Color(255, 210, 60));
        g.drawString("PRESIONA ENTER PARA JUGAR", Constantes.WIDTH / 2 - 160, 320);

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        g.setColor(new Color(230, 140, 60));
        g.drawString("Controles:", Constantes.WIDTH / 2 - 60, 390);
        g.setColor(new Color(180, 180, 180));
        g.drawString("Flechas: Moverse", Constantes.WIDTH / 2 - 100, 420);
        g.drawString("X: Cavar", Constantes.WIDTH / 2 - 160, 445);
        g.drawString("W/A/S/D: Moverse", Constantes.WIDTH / 2 - 140, 470);
        g.drawString("P: Pausa   ESC: Menu   Ctrl: Sonido", Constantes.WIDTH / 2 - 160, 495);

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

        g.setColor(new Color(100, 100, 100));
        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.drawString("v1.0 - Programacion Orientada a Objetos", Constantes.WIDTH / 2 - 160, 560);
    }
}
