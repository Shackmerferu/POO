package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import py_poo.core.Constantes;
import py_poo.input.InputManager;
import py_poo.ranking.RankingManager;
import py_poo.ranking.RankingManager.RankingEntry;
import py_poo.ui.MenuPrincipal;

public class MenuLodeRunner extends MenuPrincipal {
    private int seleccion; // opción seleccionada actualmente (0=Jugar, 1=Config, 2=Salir)
    private RankingManager rankingManager; // gestor de rankings
    private List<RankingEntry> topRanking; // lista del top 10 de puntajes

    // constructor: crea el menú con los puntajes cargados
    public MenuLodeRunner(InputManager input, Object mouse) {
        super("Lode Runner - Menu Principal", "LODE RUNNER", Color.GREEN, "Jugar", "Salir");
        this.input = input;
        this.seleccion = 0; // empieza en "Jugar"
        this.rankingManager = new RankingManager();
        this.topRanking = rankingManager.cargarDetalleTop("Lode%", 10); // carga top 10
    }

    public int getSeleccion() { return seleccion; } // retorna opción seleccionada

    public void setSeleccion(int seleccion) { this.seleccion = seleccion; } // asigna opción seleccionada

    // recarga el ranking desde la base de datos
    public void recargarRanking() {
        this.topRanking = rankingManager.cargarDetalleTop("Lode%", 10);
    }

    @Override
    // retorna las acciones configurables del juego
    protected String[] getConfigActions() {
        return new String[]{"UP", "DOWN", "LEFT", "RIGHT", "DIG", "MUSIC", "FULLSCREEN", "RESET"};
    }

    public void actualizar() {} // actualización del menú

    // dibuja el menú principal con opciones y ranking top 10
    public void dibujar(Graphics g) {
        if (isConfigMode()) {
            dibujarConfig(g);
            return;
        }

        g.setColor(new Color(25, 27, 34));
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 45));
        g.setColor(new Color(255, 210, 60));
        g.drawString("LODE RUNNER", Constantes.WIDTH / 2 - 200, 100);

        String[] opciones = {"JUGAR", "CONFIG", "SALIR"};
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

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(230, 140, 60));
        g.drawString("Controles:", 100, 450);
        g.setColor(new Color(180, 180, 180));
        g.drawString("Flechas: Moverse", 100, 470);
        g.drawString("X: Cavar", 100, 485);
        g.drawString("W/A/S/D: Moverse", 100, 500);
        g.drawString("P: Pausa   ESC: Menu   Ctrl: Sonido", 100, 515);

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

                String texto = String.format("%d. %s  N%d  %d pts", (i + 1), entry.jugador(), entry.Nivel(), entry.puntaje());
                g.drawString(texto, 450, y);
                y += 20;
            }
        }
    }
}
