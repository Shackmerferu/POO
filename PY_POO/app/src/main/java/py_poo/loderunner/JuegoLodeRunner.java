package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import py_poo.core.Constantes;
import py_poo.engine.EstadoJuego;
import py_poo.engine.VideoJuego;
import py_poo.entities.Agujero;
import py_poo.entities.Barra;
import py_poo.entities.Escalera;
import py_poo.entities.Ladrillo;
import py_poo.entities.Moneda;
import py_poo.input.InputManager;

public class JuegoLodeRunner extends VideoJuego {

    private InputManager input;
    private MenuLodeRunner menu;

    private Recolector heroe;
    private List<Guardia> guardias;
    private List<Nivel> niveles;
    private int nivelIndex;

    private int puntaje;
    private boolean nivelCompletado;
    private boolean esperandoReinicio;
    private int tiempoEspera;

    private static final int ANIMACION_FRAME_SPEED = 8;
    private static final int VISOR_TILES = 12;
    private static final int VISOR_PIXELS = VISOR_TILES * 40;
    private int frameCounter;
    private boolean enTransicion;
    private float camX, camY;

    private Random rand = new Random();

    public JuegoLodeRunner() {
        this.Nombre = "Lode Runner";
    }

    @Override
    public void iniciar() {
        super.iniciar();
        this.input = new InputManager();
        super.input = this.input;
        this.menu = new MenuLodeRunner(input, null);
        this.puntaje = 0;
        this.estado = EstadoJuego.MENU;
        this.enTransicion = false;
        this.esperandoReinicio = false;
    }

    @Override
    protected void actualizarLogicaJuego() {
        if (input == null) return;

        switch (estado) {
            case MENU:
                actualizarMenu();
                break;
            case JUGANDO:
                actualizarJuego();
                break;
            case PAUSA:
                break;
            case GAME_OVER:
                actualizarGameOver();
                break;
            case VICTORIA:
                break;
        }
    }

    private void actualizarMenu() {
        if (input.isEnterPressed() && !enTransicion) {
            enTransicion = true;
            crearPartida();
        }
    }

    private void actualizarGameOver() {
        if (input.isEnterPressed()) {
            reiniciar();
        }
    }

    @Override
    protected void crearPartida() {
        super.crearPartida();
        this.puntaje = 0;
        this.nivelIndex = 0;
        this.niveles = new ArrayList<>();
        niveles.add(new Nivel1());
        niveles.add(new Nivel2());
        niveles.add(new Nivel3());

        this.guardias = new ArrayList<>();
        this.estado = EstadoJuego.JUGANDO;
        this.nivelCompletado = false;
        this.enTransicion = false;
        this.esperandoReinicio = false;

        cargarNivelActual();
    }

    private void cargarNivelActual() {
        if (nivelIndex >= niveles.size()) {
            estado = EstadoJuego.VICTORIA;
            finalizar(EstadoJuego.VICTORIA, "Ganaste todos los niveles!");
            return;
        }

        Nivel nivel = niveles.get(nivelIndex);
        nivel.cargar();
        this.NivelActual = nivel;

        int tx = nivel.spawnRecolectorX;
        int ty = nivel.spawnRecolectorY;
        if (tx == 0 && ty == 0) {
            tx = 1; ty = 1;
        }
        heroe = new Recolector(tx, ty, nivel.getTile_size());

        heroe.setNivelOroTotal(nivel.totalOro);

        Entidades.clear();
        Entidades.add(heroe);

        guardias.clear();
        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            guardias.add(g);
            Entidades.add(g);
        }

        for (Moneda m : nivel.monedas) Entidades.add(m);
        for (Escalera e : nivel.escaleras) Entidades.add(e);
        for (Barra b : nivel.barras) Entidades.add(b);
        for (Ladrillo l : nivel.ladrillos) Entidades.add(l);
        for (Ladrillo l : nivel.ladrillosIrrompibles) Entidades.add(l);
        nivelCompletado = false;
        System.out.println("Nivel " + (nivelIndex + 1) + " cargado. Oro total: " + nivel.totalOro);
    }

    private void reiniciarNivel() {
        Nivel nivel = niveles.get(nivelIndex);
        nivel.finalizarNivel();
        nivel.cargar();

        int tx = nivel.spawnRecolectorX;
        int ty = nivel.spawnRecolectorY;
        heroe = new Recolector(tx, ty, nivel.getTile_size());
        heroe.setNivelOroTotal(nivel.totalOro);
        heroe.setVidas(Recolector.VIDAS_INICIALES);

        Entidades.clear();
        Entidades.add(heroe);

        guardias.clear();
        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            guardias.add(g);
            Entidades.add(g);
        }

        for (Moneda m : nivel.monedas) Entidades.add(m);
        for (Escalera e : nivel.escaleras) Entidades.add(e);
        for (Barra b : nivel.barras) Entidades.add(b);
        for (Ladrillo l : nivel.ladrillos) Entidades.add(l);
        for (Ladrillo l : nivel.ladrillosIrrompibles) Entidades.add(l);
        estado = EstadoJuego.JUGANDO;
    }

    private void actualizarJuego() {
        if (heroe == null || NivelActual == null) return;

        frameCounter++;
        heroe.actualizar();
        for (Guardia g : guardias) g.actualizar();
        NivelActual.actualizar();

        if (heroe.getVidas() <= 0) {
            estado = EstadoJuego.GAME_OVER;
            finalizar(EstadoJuego.GAME_OVER, "Te quedaste sin vidas!");
            return;
        }

        manejarInputHeroe();
        aplicarGravedad();
        aplicarMovimientoGuardias();
        verificarRecoleccionOro();
        verificarColisionesGuardias();
        verificarAgujeros();
        verificarNivelCompleto();
        actualizarCamara();
    }

    private void manejarInputHeroe() {
        int tileX = heroe.getTileX();
        int tileY = heroe.getTileY();
        int ts = NivelActual.getTile_size();

        boolean sobreLadrillo = tilesAbajoSolido(heroe);
        boolean enEscalera = colisionaConEscalera(heroe);
        boolean enBarra = colisionaConBarra(heroe);
        boolean enVacioAbajo = !sobreLadrillo && !enEscalera;

        heroe.setEnEscalera(enEscalera);
        heroe.setEnBarra(enBarra);
        heroe.setEnAire(enVacioAbajo && !heroe.isCayendo());

        if (heroe.isCayendo()) {
            heroe.setY(heroe.getY() + Recolector.VELOCIDAD + 2);
            if (sobreLadrillo) {
                heroe.setY(tileY * ts);
                heroe.setCayendo(false);
            }
            return;
        }

        boolean puedeIzq = !tileLateralSolido(heroe, -1);
        boolean puedeDer = !tileLateralSolido(heroe, 1);

        if (enEscalera) {
            if (input.isUpPressed() || input.isWPressed()) heroe.moverArriba();
            else if (input.isDownPressed() || input.isSPressed()) heroe.moverAbajo();
            if (input.isLeftPressed() && puedeIzq) heroe.moverIzquierda();
            if (input.isRightPressed() && puedeDer) heroe.moverDerecha();
        } else if (enBarra) {
            if (input.isLeftPressed() && puedeIzq) heroe.moverIzquierda();
            if (input.isRightPressed() && puedeDer) heroe.moverDerecha();
            if (input.isDownPressed() || input.isSPressed()) {
                heroe.setEnBarra(false);
                heroe.setY(heroe.getY() + 3);
            }
        } else {
            if (input.isLeftPressed() && puedeIzq) heroe.moverIzquierda();
            if (input.isRightPressed() && puedeDer) heroe.moverDerecha();

            if ((input.isUpPressed() || input.isWPressed()) && colisionaConEscaleraArriba(heroe)) {
                heroe.setY(heroe.getY() - 2);
            }

            if ((input.isDownPressed() || input.isSPressed()) && colisionaConEscaleraAbajo(heroe)) {
                heroe.setY(heroe.getY() + Recolector.VELOCIDAD);
            } else if ((input.isDownPressed() || input.isSPressed()) && !sobreLadrillo && !enEscalera) {
                heroe.setY(heroe.getY() + Recolector.VELOCIDAD);
            }
        }

        if (input.isKeyPressed(90)) cavarEnLado(-1);
        if (input.isKeyPressed(88)) cavarEnLado(1);

        heroe.setX((double)clamp((float)heroe.getX(), 0, NivelActual.getAnchoPixels() - ts));
        if (NivelActual.escapeLadderActiva && heroe.nivelCompleto() && enEscalera) {
            heroe.setY((double)Math.min((float)heroe.getY(), NivelActual.getAltoPixels() - ts));
        } else {
            heroe.setY((double)clamp((float)heroe.getY(), 0, NivelActual.getAltoPixels() - ts));
        }
    }

    private boolean tilesAbajoSolido(Recolector h) {
        int ty = (int)((h.getY() + h.getHeight() + 2) / NivelActual.getTile_size());
        int tx1 = (int)(h.getX() / NivelActual.getTile_size());
        int tx2 = (int)((h.getX() + h.getWidth() - 1) / NivelActual.getTile_size());

        for (int tx = tx1; tx <= tx2; tx++) {
            if (NivelActual.esSolido(tx, ty)) return true;
        }
        return false;
    }

    private boolean tilesAbajoSolido(Guardia g) {
        int ty = (int)((g.getY() + g.getHeight() + 2) / NivelActual.getTile_size());
        int tx1 = (int)(g.getX() / NivelActual.getTile_size());
        int tx2 = (int)((g.getX() + g.getWidth() - 1) / NivelActual.getTile_size());

        for (int tx = tx1; tx <= tx2; tx++) {
            if (NivelActual.esSolido(tx, ty)) return true;
        }
        return false;
    }

    private boolean tileLateralSolido(Recolector h, int dir) {
        int tx = (int)((h.getX() + (dir > 0 ? h.getWidth() + 2 : -2)) / NivelActual.getTile_size());
        int ty1 = (int)(h.getY() / NivelActual.getTile_size());
        int ty2 = (int)((h.getY() + h.getHeight() - 1) / NivelActual.getTile_size());

        for (int ty = ty1; ty <= ty2; ty++) {
            if (NivelActual.esSolido(tx, ty)) return true;
        }
        return false;
    }

    private void cavarEnLado(int dir) {
        int tileX = heroe.getTileX() + dir;
        int tileY = heroe.getTileY() + 1;

        if (NivelActual.cavarEn(tileX, tileY)) {
            System.out.println("Cavado en (" + tileX + ", " + tileY + ")");
        }
    }

    private void aplicarGravedad() {
        if (heroe == null) return;
        if (heroe.isEnEscalera()) return;

        boolean abajoSolido = tilesAbajoSolido(heroe);
        if (!abajoSolido && !heroe.isEnEscalera()) {
            heroe.setCayendo(true);
            heroe.setY(heroe.getY() + Recolector.VELOCIDAD + 1);

            int tileX = heroe.getTileX();
            int tileY = (int)((heroe.getY() + heroe.getHeight()) / NivelActual.getTile_size());

            for (Agujero a : NivelActual.agujeros) {
                if (a.isAbierto()) {
                    int ax = a.getPunto().x / NivelActual.getTile_size();
                    int ay = a.getPunto().y / NivelActual.getTile_size();
                    if (tileX == ax && tileY == ay) {
                        heroe.setY((ay + 1) * NivelActual.getTile_size());
                    }
                }
            }
        } else {
            if (heroe.isCayendo()) {
                int ty = (int)((heroe.getY() + heroe.getHeight()) / NivelActual.getTile_size());
                heroe.setY((ty - 1) * NivelActual.getTile_size());
            }
            heroe.setCayendo(false);
        }
    }

    private void aplicarMovimientoGuardias() {
        for (Guardia g : guardias) {
            if (g == null) continue;

            if (g.getIA().getEstado() == IA_Guardia.Comportamiento.ATRAPADO) {
                g.getIA().atrapar();
                continue;
            }

            if (g.getIA().getEstado() == IA_Guardia.Comportamiento.REAPARECER) {
                int x = rand.nextInt(NivelActual.getAnchoMapa() - 2) + 1;
                g.setX(x * NivelActual.getTile_size());
                g.setY(NivelActual.getTile_size());
                g.getIA().reaparecer();
                continue;
            }

            boolean sobreSuelo = tilesAbajoSolido(g);
            int gTileX = g.getTileX();
            int gTileY = g.getTileY();
            boolean enEscalera = NivelActual.esEscalera(gTileX, gTileY);
            boolean enBarra = NivelActual.esBarra(gTileX, gTileY);
            g.setEnEscalera(enEscalera);
            g.setEnBarra(enBarra);

            if (!sobreSuelo && !enEscalera) {
                g.setCayendo(true);
                g.setY(g.getY() + Guardia.VELOCIDAD + 1);
                continue;
            } else {
                g.setCayendo(false);
            }

            if (frameCounter % 3 != 0) continue;

            int hTileX = heroe.getTileX();
            int hTileY = heroe.getTileY();
            int dist = Math.abs(gTileX - hTileX) + Math.abs(gTileY - hTileY);

            if (dist < 8) {
                g.getIA().cambiarAPersecucion();
            } else if (rand.nextInt(100) < 20) {
                g.getIA().setEstado(IA_Guardia.Comportamiento.VAGAR);
            }

            boolean puedeIzq = !tileLateralSolido(g, -1);
            boolean puedeDer = !tileLateralSolido(g, 1);

            int movimiento = g.getIA().calcularMovimiento(
                gTileX, gTileY, hTileX, hTileY,
                puedeIzq, puedeDer,
                enEscalera, enEscalera && sobreSuelo,
                enEscalera, enBarra
            );

            if (movimiento == -1 && puedeIzq) g.moverIzquierda();
            else if (movimiento == 1 && puedeDer) g.moverDerecha();
            else if (movimiento == -2 && enEscalera) g.moverArriba();
            else if (movimiento == 2 && enEscalera) g.moverAbajo();
            else {
                if (puedeIzq && rand.nextBoolean()) g.moverIzquierda();
                else if (puedeDer) g.moverDerecha();
            }

            g.setX((double)clamp((float)g.getX(), 0, NivelActual.getAnchoPixels() - NivelActual.getTile_size()));
            g.setY((double)clamp((float)g.getY(), 0, NivelActual.getAltoPixels() - NivelActual.getTile_size()));
        }
    }

    private boolean tileLateralSolido(Guardia g, int dir) {
        int tx = (int)((g.getX() + (dir > 0 ? g.getWidth() + 2 : -2)) / NivelActual.getTile_size());
        int ty1 = (int)(g.getY() / NivelActual.getTile_size());
        int ty2 = (int)((g.getY() + g.getHeight() - 1) / NivelActual.getTile_size());

        for (int ty = ty1; ty <= ty2; ty++) {
            if (NivelActual.esSolido(tx, ty)) return true;
        }
        return false;
    }

    private boolean tileLateralSolido(int tileX, int tileY) {
        return NivelActual.esSolido(tileX, tileY);
    }

    private boolean colisionaConEscalera(Recolector h) {
        for (Escalera e : NivelActual.escaleras) {
            int margen = 4;
            Rectangle hr = new Rectangle((int)h.getX() + margen, (int)h.getY() + margen,
                                         h.getWidth() - margen * 2, h.getHeight() - margen * 2);
            if (hr.intersects(e.getBounds())) return true;
        }
        return false;
    }

    private boolean colisionaConBarra(Recolector h) {
        for (Barra b : NivelActual.barras) {
            Rectangle hr = new Rectangle((int)h.getX(), (int)h.getY() + h.getHeight() / 2,
                                         h.getWidth(), h.getHeight() / 2);
            if (hr.intersects(b.getBounds())) return true;
        }
        return false;
    }

    private boolean colisionaConEscaleraArriba(Recolector h) {
        Rectangle arriba = new Rectangle((int)h.getX(), (int)h.getY() - 4,
                                         h.getWidth(), 4);
        for (Escalera e : NivelActual.escaleras) {
            if (arriba.intersects(e.getBounds())) return true;
        }
        return false;
    }

    private boolean colisionaConEscaleraAbajo(Recolector h) {
        Rectangle abajo = new Rectangle((int)h.getX(), (int)(h.getY() + h.getHeight()),
                                        h.getWidth(), 4);
        for (Escalera e : NivelActual.escaleras) {
            if (abajo.intersects(e.getBounds())) return true;
        }
        return false;
    }

    private void verificarRecoleccionOro() {
        int hx = (int)(heroe.getX() + heroe.getWidth() / 2);
        int hy = (int)(heroe.getY() + heroe.getHeight() / 2);
        Moneda m = NivelActual.getMonedaEn(hx, hy + 5);
        if (m != null && !m.isRecolectada()) {
            m.recolectar();
            heroe.recogerOro();
            puntaje += m.getValor();
            System.out.println("Oro! Total: " + heroe.getOroRecolectado() + "/" + heroe.getNivelOroTotal());

            if (heroe.nivelCompleto()) {
                NivelActual.activarEscape();
                System.out.println("Todos los oros recolectados! Escapa por la escalera hacia arriba!");
            }
        }
    }



    private void verificarColisionesGuardias() {
        for (Guardia g : guardias) {
            if (g == null) continue;
            if (heroe.getBounds().intersects(g.getBounds())) {
                heroe.perderVida();
                if (heroe.getVidas() > 0) {
                    reiniciarNivel();
                } else {
                    estado = EstadoJuego.GAME_OVER;
                    finalizar(EstadoJuego.GAME_OVER, "Te atraparon!");
                }
                return;
            }
        }
    }

    private void verificarAgujeros() {
        for (Agujero a : NivelActual.agujeros) {
            if (!a.isAbierto()) continue;
            int ax = a.getPunto().x / NivelActual.getTile_size();
            int ay = a.getPunto().y / NivelActual.getTile_size();

            int hx = heroe.getTileX();
            int hy = (int)((heroe.getY() + heroe.getHeight()) / NivelActual.getTile_size());

            if (hx == ax && hy == ay && !heroe.isEnEscalera()) {
                heroe.setCayendo(true);
                heroe.setY((ay + 1) * NivelActual.getTile_size());
            }

            for (Guardia g : guardias) {
                int gx = g.getTileX();
                int gy = (int)((g.getY() + g.getHeight()) / NivelActual.getTile_size());
                if (gx == ax && gy == ay && !g.isEnEscalera()) {
                    g.setCayendo(true);
                    g.getIA().atrapar();
                    g.setY((ay + 1) * NivelActual.getTile_size());
                }
            }
        }
    }

    private void verificarNivelCompleto() {
        if (NivelActual.escapeLadderActiva && heroe.nivelCompleto() && heroe.getY() + heroe.getHeight() < 0) {
            nivelCompletado = true;
            puntaje += 500;
            heroe.setVidas(heroe.getVidas() + 1);

            NivelActual.finalizarNivel();
            nivelIndex++;
            if (nivelIndex >= niveles.size()) {
                estado = EstadoJuego.VICTORIA;
                rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", puntaje);
                finalizar(EstadoJuego.VICTORIA, "Ganaste el juego!");
            } else {
                cargarNivelActual();
                heroe.setVidas(Math.max(heroe.getVidas(), 1));
            }
        }
    }

    private void actualizarCamara() {
        if (heroe == null) return;
        int anchoMundo = NivelActual.getAnchoPixels();
        int altoMundo = NivelActual.getAltoPixels();
        int tileSize = NivelActual.getTile_size();

        float centroCamX = (float)(heroe.getX() + heroe.getWidth() / 2f - VISOR_PIXELS / 2f);
        float centroCamY = (float)(heroe.getY() + heroe.getHeight() / 2f - VISOR_PIXELS / 2f);

        camX = clamp(centroCamX, 0, Math.max(0, anchoMundo - VISOR_PIXELS));
        camY = clamp(centroCamY, 0, Math.max(0, altoMundo - VISOR_PIXELS));
    }

    @Override
    public void renderizar(Graphics g) {
        if (estado == EstadoJuego.MENU) {
            if (menu != null) menu.dibujar(g);
            return;
        }

        if (estado == EstadoJuego.GAME_OVER) {
            renderizarGameOver(g);
            return;
        }

        if (estado == EstadoJuego.VICTORIA) {
            renderizarVictoria(g);
            return;
        }

        if (heroe == null || NivelActual == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            return;
        }

        int tileSize = NivelActual.getTile_size();
        int offsetX = (Constantes.WIDTH - VISOR_PIXELS) / 2;
        int offsetY = (Constantes.HEIGHT - VISOR_PIXELS) / 2;

        actualizarCamara();

        g.setColor(new Color(10, 10, 15));
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

        g.setColor(new Color(40, 35, 50));
        g.fillRect(offsetX - 2, offsetY - 2, VISOR_PIXELS + 4, VISOR_PIXELS + 4);

        g.setClip(offsetX, offsetY, VISOR_PIXELS, VISOR_PIXELS);
        Graphics g2 = g.create();
        g2.translate(offsetX - (int)camX, offsetY - (int)camY);

        for (Ladrillo l : NivelActual.ladrillos) {
            if (!l.isRoto()) l.display(g2);
        }
        for (Ladrillo l : NivelActual.ladrillosIrrompibles) {
            l.display(g2);
        }
        for (Escalera e : NivelActual.escaleras) {
            e.display(g2);
        }
        for (Barra b : NivelActual.barras) {
            b.display(g2);
        }
        for (Moneda m : NivelActual.monedas) {
            if (!m.isRecolectada()) m.display(g2);
        }

        heroe.display(g2);
        for (Guardia guardia : guardias) {
            guardia.display(g2);
        }

        g2.dispose();
        g.setClip(null);

        renderizarHUD(g);

        if (estado == EstadoJuego.PAUSA) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 48));
            String pausa = "PAUSA";
            int pw = g.getFontMetrics().stringWidth(pausa);
            g.drawString(pausa, (Constantes.WIDTH - pw) / 2, Constantes.HEIGHT / 2);
            g.setFont(new Font("Consolas", Font.PLAIN, 18));
            g.drawString("Presiona P para continuar", Constantes.WIDTH / 2 - 130, Constantes.HEIGHT / 2 + 40);
        }
    }

    private void renderizarHUD(Graphics g) {
        g.setFont(new Font("Consolas", Font.BOLD, 16));
        int x = 10;
        int y = 25;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, 250, 80);
        g.setColor(Color.WHITE);
        g.drawString("Nivel: " + (nivelIndex + 1) + "/" + niveles.size(), x, y);
        g.drawString("Vidas: " + heroe.getVidas(), x, y + 20);
        g.drawString("Oro: " + heroe.getOroRecolectado() + "/" + heroe.getNivelOroTotal(), x, y + 40);
        g.drawString("Puntaje: " + puntaje, x, y + 60);

        if (heroe.nivelCompleto() && NivelActual.escapeLadderActiva) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.drawString("!! TREPA HACIA LA CIMA !!", Constantes.WIDTH / 2 - 140, 30);
        }
    }

    private void renderizarGameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
        g.setColor(Color.RED);
        g.setFont(new Font("Consolas", Font.BOLD, 52));
        String txt = "GAME OVER";
        int tw = g.getFontMetrics().stringWidth(txt);
        g.drawString(txt, (Constantes.WIDTH - tw) / 2, Constantes.HEIGHT / 2 - 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        String sub = "Puntaje final: " + puntaje;
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (Constantes.WIDTH - sw) / 2, Constantes.HEIGHT / 2 + 20);
        g.drawString("Presiona ENTER para volver al menu", Constantes.WIDTH / 2 - 170, Constantes.HEIGHT / 2 + 60);
    }

    private void renderizarVictoria(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Consolas", Font.BOLD, 48));
        String txt = "VICTORIA!";
        int tw = g.getFontMetrics().stringWidth(txt);
        g.drawString(txt, (Constantes.WIDTH - tw) / 2, Constantes.HEIGHT / 2 - 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        String sub = "Puntaje final: " + puntaje;
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (Constantes.WIDTH - sw) / 2, Constantes.HEIGHT / 2 + 20);
    }

    @Override
    public String getGanador() {
        return nombreJugadorPrincipal;
    }

    @Override
    public String getPerdedor() {
        return nombreJugadorPrincipal;
    }

    private float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    public void setNombreJugador(String nombre) {
        this.nombreJugadorPrincipal = nombre;
    }
}
