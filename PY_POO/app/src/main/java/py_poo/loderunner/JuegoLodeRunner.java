package py_poo.loderunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import py_poo.audio.FXPlayer;
import py_poo.collision.CollisionManager;
import py_poo.core.Constantes;
import py_poo.core.GameLoop;
import py_poo.engine.EstadoJuego;
import py_poo.engine.Jugador;
import py_poo.engine.VideoJuego;
import py_poo.entities.Agujero;
import py_poo.entities.Escalera;
import py_poo.entities.Moneda;
import py_poo.entities.ParticulaLadrillo;
import py_poo.entities.Puerta;
import py_poo.input.InputManager;
import py_poo.utils.CargadorRecursos;

public class JuegoLodeRunner extends VideoJuego {

    private InputManager input; // gestor de entrada del jugador
    private MenuLodeRunner menu; // menú principal del juego
    private CollisionManager collisionManager; // gestor de colisiones
    private Recolector heroe; // personaje del jugador
    private List<Guardia> guardias; // lista de guardias enemigos
    private List<Nivel> niveles; // lista de niveles del juego
    private int nivelIdx; // índice del nivel actual
    private int puntosJ1; // puntos acumulados del jugador
    private boolean rankingRegistrado; // true si ya se registró el puntaje
    private int tiempoNivel; // contador de tiempo transcurrido en el nivel
    private BufferedImage fondo; // imagen de fondo
    private FXPlayer fxPlayer; // reproductor de efectos de sonido
    private boolean musicaIniciada; // true si la música ya empezó

    // constructor: establece nombre del juego
    public JuegoLodeRunner() {
        this.Nombre = "Lode Runner";
    }

    @Override
    // inicia el juego: configura input, menú, collision manager y carga recursos
    public void iniciar() {
        super.iniciar();
        this.input = new InputManager();
        super.input = this.input;
        this.menu = new MenuLodeRunner(input, null);
        this.collisionManager = new CollisionManager();
        this.puntosJ1 = 0;
        this.rankingRegistrado = false;
        this.estado = EstadoJuego.MENU;
        this.musicaIniciada = false;
        CargadorRecursos cr = new CargadorRecursos();
        this.fondo = cr.cargarImagen("imagenes/Lode Runner/fondo negro.png");

        fxPlayer = new FXPlayer();
        fxPlayer.cargarSonidoRecurso("punto", "sonidos/punto.wav"); // sonido al recoger oro
        fxPlayer.cargarSonidoRecurso("paleta", "sonidos/paleta.wav"); // sonido al cavar
        fxPlayer.cargarSonidoRecurso("empieza", "sonidos/Empieza.wav"); // sonido de evento
        fxPlayer.cargarSonidoRecurso("soundtrack", "sonidos/SoundTrack.wav"); // música de fondo
    }

    // pausa el juego
    public void pause() {
        estado = EstadoJuego.PAUSA;
    }

    @Override
    // renderiza todos los elementos del juego en pantalla
    public void renderizar(Graphics g) {
        Graphics2D g2d = (g instanceof Graphics2D) ? (Graphics2D) g : null;
        if (g2d != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        if (this.estado == EstadoJuego.MENU && menu != null) {
            menu.dibujar(g);
            return;
        }
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, Constantes.WIDTH, Constantes.HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);
        }

        super.renderizar(g);

        if (estado == EstadoJuego.JUGANDO && heroe != null) {
            g.setFont(new Font("Consolas", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString("NIVEL " + (nivelIdx + 1), 10, 25);
            g.drawString("ORO: " + heroe.getOroRecolectado() + "/" + heroe.getNivelOroTotal(), 10, 48);
            g.drawString("VIDAS: " + heroe.getVidas(), 10, 71);
            g.drawString("PUNTOS: " + puntosJ1, 10, 94);
            if (NivelActual != null) {
                int restante = Math.max(0, NivelActual.tiempoLimite * 60 - tiempoNivel) / 60;
                g.drawString("TIEMPO: " + restante + "s", 10, 117);
            } else {
                g.drawString("TIEMPO: --", 10, 117);
            }
        }

        if (estado == EstadoJuego.PAUSA) {
            g.setFont(new Font("Consolas", Font.BOLD, 48));
            g.setColor(Color.YELLOW);
            g.drawString("PAUSA", Constantes.WIDTH / 2 - 80, Constantes.HEIGHT / 2);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            g.setColor(Color.GRAY);
            g.drawString("P = reanudar  |  ESC = menu", Constantes.WIDTH / 2 - 160, Constantes.HEIGHT / 2 + 50);
        }

        if (estado == EstadoJuego.VICTORIA) {
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            g.setColor(new Color(80, 255, 80));
            g.drawString("¡VICTORIA!", Constantes.WIDTH / 2 - 130, 120);

            g.setFont(new Font("Consolas", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Puntaje: " + puntosJ1, Constantes.WIDTH / 2 - 80, 180);
            g.drawString("Niveles completados: " + niveles.size(), Constantes.WIDTH / 2 - 130, 210);

            g.setFont(new Font("Consolas", Font.BOLD, 18));
            g.setColor(Color.CYAN);
            g.drawString("--- TOP 10 ---", Constantes.WIDTH / 2 - 70, 260);

            var top = new py_poo.ranking.RankingManager().cargarDetalleTop("Lode%", 10);
            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            g.setColor(Color.WHITE);
            int y = 290;
            if (top == null || top.isEmpty()) {
                g.drawString("Aún no hay puntajes.", Constantes.WIDTH / 2 - 80, y);
            } else {
                for (int i = 0; i < top.size(); i++) {
                    var entry = top.get(i);
                    String texto = String.format("%d. %s  N%d  %d pts", (i + 1), entry.jugador(), entry.Nivel(), entry.puntaje());
                    g.drawString(texto, Constantes.WIDTH / 2 - 150, y);
                    y += 22;
                }
            }

            g.setFont(new Font("Consolas", Font.PLAIN, 18));
            g.setColor(Color.GRAY);
            g.drawString("ENTER = volver al menú", Constantes.WIDTH / 2 - 120, Constantes.HEIGHT - 80);
        }

        if (estado == EstadoJuego.GAME_OVER) {
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            g.setColor(new Color(255, 80, 80));
            g.drawString("GAME OVER", Constantes.WIDTH / 2 - 140, 120);

            g.setFont(new Font("Consolas", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Puntaje: " + puntosJ1, Constantes.WIDTH / 2 - 80, 180);
            g.drawString("Nivel: " + (nivelIdx + 1), Constantes.WIDTH / 2 - 60, 210);

            g.setFont(new Font("Consolas", Font.BOLD, 18));
            g.setColor(Color.CYAN);
            g.drawString("--- TOP 10 ---", Constantes.WIDTH / 2 - 70, 260);

            var top = new py_poo.ranking.RankingManager().cargarDetalleTop("Lode%", 10);
            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            g.setColor(Color.WHITE);
            int y = 290;
            if (top == null || top.isEmpty()) {
                g.drawString("Aún no hay puntajes.", Constantes.WIDTH / 2 - 80, y);
            } else {
                for (int i = 0; i < top.size(); i++) {
                    var entry = top.get(i);
                    String texto = String.format("%d. %s  N%d  %d pts", (i + 1), entry.jugador(), entry.Nivel(), entry.puntaje());
                    g.drawString(texto, Constantes.WIDTH / 2 - 150, y);
                    y += 22;
                }
            }

            g.setFont(new Font("Consolas", Font.PLAIN, 18));
            g.setColor(Color.GRAY);
            g.drawString("ENTER = volver al menú", Constantes.WIDTH / 2 - 120, Constantes.HEIGHT - 80);
        }

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 100));
        String snd = "FX:" + (soundFxEnabled ? "ON" : "OFF") + " MUS:" + (musicEnabled ? "ON" : "OFF")
            + " Q=FX M=MUS CTRL=GLOBAL \\=FULL";
        g.drawString(snd, 10, Constantes.HEIGHT - 10);
    }

    @Override
    // crea una nueva partida, inicializa niveles y carga el primer nivel
    protected void crearPartida() {
        niveles = new ArrayList<>();
        niveles.add(new Nivel1());
        niveles.add(new Nivel2());
        niveles.add(new Nivel3());

        nivelIdx = 0; // empieza en nivel 1
        puntosJ1 = 0; // puntos en cero
        rankingRegistrado = false;
        tiempoNivel = 0; // cronómetro en cero
        Jugador.clear();
        Jugador.add(new Jugador(nombreJugadorPrincipal));
        estado = EstadoJuego.JUGANDO; // cambia a estado jugando

        musicaIniciada = false;
        cargarNivelActual(); // carga el primer nivel
    }

    // carga el nivel actual, creando héroe, guardias y entidades del mapa
    private void cargarNivelActual() {
        if (nivelIdx >= niveles.size()) {
            estado = EstadoJuego.VICTORIA;
            finalizar(EstadoJuego.VICTORIA, "Ganaste todos los niveles!");
            return;
        }

        Nivel nivel = niveles.get(nivelIdx);
        tiempoNivel = 0;
        nivel.cargar();
        this.NivelActual = nivel;

        int tx = nivel.spawnRecolectorX;
        int ty = nivel.spawnRecolectorY;
        if (tx == 0 && ty == 0) {
            tx = 1;
            ty = 1;
        }

        heroe = new Recolector(tx, ty, nivel.getTile_size());
        heroe.setInputManager(input);
        heroe.setNivel(nivel);
        heroe.setNivelOroTotal(nivel.totalOro);
        Entidades.clear();
        guardias = new ArrayList<>();
        for (var l : nivel.ladrillos)
            Entidades.add(l);
        for (var l : nivel.ladrillosIrrompibles)
            Entidades.add(l);
        for (var e : nivel.escaleras)
            Entidades.add(e);
        for (var b : nivel.barras)
            Entidades.add(b);
        for (var m : nivel.monedas)
            Entidades.add(m);
        for (int[] sp : nivel.spawnGuardias) {
            Guardia g = new Guardia(sp[0], sp[1], nivel.getTile_size());
            g.setHeroe(heroe);
            g.setNivel(nivel);
            guardias.add(g);
            Entidades.add(g);
        }
        heroe.setGuardias(guardias);
        Entidades.add(heroe);
    }
    @Override
    // lógica principal del juego ejecutada cada frame
    protected void actualizarLogicaJuego() {
        if (this.estado == EstadoJuego.MENU) {
            if (menu.isConfigMode()) {
                menu.actualizarConfig();
                configManager.guardar();
                return;
            }
            if (input.isMenuUpPressed() || input.isWPressed()) {
                menu.setSeleccion(Math.max(0, menu.getSeleccion() - 1));
            }
            if (input.isMenuDownPressed() || input.isSPressed()) {
                menu.setSeleccion(Math.min(2, menu.getSeleccion() + 1));
            }
            if (input.isEnterPressed()) {
                if (menu.getSeleccion() == 2) {
                    GameLoop.terminarJuego();
                    return;
                }
                if (menu.getSeleccion() == 1) {
                    menu.setConfigMode(true);
                    return;
                }
                crearPartida();
            }
            return;
        }
        if (this.estado == EstadoJuego.GAME_OVER) {
            if (input.isEnterPressed()) {
                this.estado = EstadoJuego.MENU;
                musicaIniciada = false;
            }
            return;
        }

        if (estado == EstadoJuego.VICTORIA) {
            if (input.isEnterPressed()) {
                estado = EstadoJuego.MENU;
                musicaIniciada = false;
            }
            return;
        }

        if (estado != EstadoJuego.JUGANDO || heroe == null || NivelActual == null) return;

        gestionarMusica();
        Nivel nivelActual = (Nivel) this.NivelActual;

            heroe.mover();
            if (camara != null) {
                camara.seguirJugador(heroe, nivelActual);
            }
            if (soundEnabled && soundFxEnabled && heroe.cavoEsteFrame()) {
                fxPlayer.reproducir("paleta");
            }

            // RECOLECTOR vs agujeros (muerte cuando el agujero se cierra encima del héroe)
            for (Agujero a : nivelActual.agujeros) {
                if (!collisionManager.colisiona(a, heroe)) continue;
                if (a.getTiempoRestante() > 1) continue; // sigue abierto → cae seguro
                boolean guardiaTapa = false;
                for (Guardia g : guardias) {
                    if (g.enAgujero() && collisionManager.colisiona(a, g)) {
                        guardiaTapa = true;
                        break;
                    }
                }
                if (guardiaTapa) continue;
                heroe.perderVida();
                if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                if (heroe.getVidas() <= 0) {
                    if (!rankingRegistrado) {
                        rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", nivelIdx + 1, puntosJ1);
                        if (menu != null) menu.recargarRanking();
                        rankingRegistrado = true;
                    }
                    heroe.desaparecer();
                    this.estado = EstadoJuego.GAME_OVER;
                    fxPlayer.detener("soundtrack");
                    return;
                } else {
                    int vidasGuardadas = heroe.getVidas();
                    nivelActual.finalizarNivel();
                    cargarNivelActual();
                    heroe.setVidas(vidasGuardadas);
                    return;
                }
            }

            tiempoNivel++;
            nivelActual.actualizar();

            for (Guardia g : guardias) {
                if (g != null) g.mover();
            }

            // JUGADOR vs monedas
            for (var it = nivelActual.monedas.iterator(); it.hasNext();) {
                var m = it.next();
                if (!m.isRecolectada() && collisionManager.colisiona(heroe, m)) {
                    m.recolectar();
                    heroe.recogerOro();
                    puntosJ1 += 100;
                    if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("punto");
                }
            }

            // GUARDIAS vs oro (incluye drop periódico)
            for (Guardia g : guardias) {
                if (g == null) continue;
                if (g.isCargandoOro()) {
                    if (Math.random() < 0.005) {
                        soltarOroGuardia(g, nivelActual);
                    }
                } else {
                    for (var m : nivelActual.monedas) {
                        if (!m.isRecolectada() && collisionManager.colisiona(g, m)) {
                            m.recolectar();
                            g.setMonedaCargada(m);
                            break;
                        }
                    }
                }
            }

            // GUARDIAS vs agujeros (antes que JUGADOR para marcar enAgujero)
            int tileSize = nivelActual.getTile_size();
            for (Guardia g : guardias) {
                if (g == null) continue;
                if (g.enAgujero()) {
                    g.getIA().incrementarTiempoAtrapado();
                    boolean enAlgunAgujero = false;
                    for (Agujero a : nivelActual.agujeros) {
                        if (collisionManager.colisiona(a, g)) {
                            enAlgunAgujero = true;
                        } else {
                            int aTx = (int)a.getX() / tileSize;
                            int aTy = (int)a.getY() / tileSize;
                            if (g.getTileX() == aTx && (g.getY() + tileSize) / tileSize == aTy) {
                                enAlgunAgujero = true;
                            }
                        }
                        if (enAlgunAgujero) {
                            int ta = g.getIA().getTiempoAtrapado();
                            if (ta >= IA_Guardia.getTiempoEscape() && ta < a.getTiempoRestante()) {
                                g.iniciarEscape((int)a.getY() / tileSize);
                                break;
                            } else if (g.getIA().getEstado() == IA_Guardia.Comportamiento.REAPARECER) {
                                g.setY(a.getY() - tileSize);
                                if (Math.random() < 0.5) {
                                    g.setX(g.getX() - tileSize);
                                } else {
                                    g.setX(g.getX() + tileSize);
                                }
                                if (!collisionManager.colisiona(a, g)) {
                                    g.enAgujero(false);
                                    g.getIA().reaparecer();
                                }
                            }
                            break;
                        }
                    }
                    if (!enAlgunAgujero) {
                        soltarOroGuardia(g, nivelActual);
                        g.reaparecer();
                        puntosJ1 += 200;
                    }
                } else {
                    for (Agujero a : nivelActual.agujeros) {
                        boolean colision = false;
                        if (collisionManager.colisiona(a, g)) {
                            colision = true;
                        } else {
                            int aTx = (int)a.getX() / tileSize;
                            int aTy = (int)a.getY() / tileSize;
                            if (g.getTileX() == aTx && (g.getY() + tileSize) / tileSize == aTy) {
                                colision = true;
                            }
                        }
                        if (colision) {
                            boolean ocupado = false;
                            for (Guardia otro : guardias) {
                                if (otro != g && otro.enAgujero() && collisionManager.colisiona(a, otro)) {
                                    ocupado = true;
                                    break;
                                }
                            }
                            if (ocupado) break;
                            if (g.isCargandoOro()) {
                                soltarOroGuardia(g, nivelActual);
                            }
                            g.enAgujero(true);
                            g.setCayendo(false);
                            g.setX(a.getX());
                            g.setY(a.getY());
                            g.getIA().atrapar();
                            break;
                        }
                    }
                }
            }

            // JUGADOR vs guardias
            for (Guardia g : guardias) {
                if (g == null) continue;
                if (collisionManager.colisiona(g, heroe)) {
                    if (g.enAgujero() || g.getIA().isSaliendo()) continue;
                    boolean puedeBajar = input.isDownPressed() || input.isSPressed();
                    boolean heroearriba = heroe.getY() + heroe.getHeight() <= g.getY() + g.getHeight() + 5;
                    boolean hayAgujeroAbierto = false;
                    for (Agujero a : nivelActual.agujeros) {
                        if (a.isAbierto()) { hayAgujeroAbierto = true; break; }
                    }
                    if (heroearriba && hayAgujeroAbierto && !puedeBajar) {
                        int headTy = (int)(heroe.getY() - 1) / nivelActual.getTile_size();
                        if (headTy >= 0 && !nivelActual.esSolido(heroe.getTileX(), headTy)) {
                            heroe.setY(g.getY() - heroe.getHeight());
                        } else {
                            heroe.perderVida();
                            if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                            if (heroe.getVidas() <= 0) {
                                if (!rankingRegistrado) {
                                    rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", nivelIdx + 1, puntosJ1);
                                    if (menu != null) menu.recargarRanking();
                                    rankingRegistrado = true;
                                }
                                heroe.desaparecer();
                                this.estado = EstadoJuego.GAME_OVER;
                                fxPlayer.detener("soundtrack");
                                return;
                            } else {
                                int vidasGuardadas = heroe.getVidas();
                                nivelActual.finalizarNivel();
                                cargarNivelActual();
                                heroe.setVidas(vidasGuardadas);
                                return;
                            }
                        }
                    } else {
                        heroe.perderVida();
                        if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                        if (heroe.getVidas() <= 0) {
                            if (!rankingRegistrado) {
                                rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", nivelIdx + 1, puntosJ1);
                                if (menu != null) menu.recargarRanking();
                                rankingRegistrado = true;
                            }
                            heroe.desaparecer();
                            this.estado = EstadoJuego.GAME_OVER;
                            fxPlayer.detener("soundtrack");
                            return;
                        } else {
                            int vidasGuardadas = heroe.getVidas();
                            nivelActual.finalizarNivel();
                            cargarNivelActual();
                            heroe.setVidas(vidasGuardadas);
                            return;
                        }
                    }
                }
            }

            // Sincronizar entidades dinámicas de Nivel al renderer
            for (Agujero a : nivelActual.agujeros) {
                if (!Entidades.contains(a)) Entidades.add(0, a);
            }
            for (ParticulaLadrillo p : nivelActual.particulas) {
                if (!Entidades.contains(p)) Entidades.add(p);
            }
            for (Escalera e : nivelActual.escaleras) {
                if (!Entidades.contains(e)) Entidades.add(e);
            }
            Puerta puerta = nivelActual.puertaSalida;
            if (puerta != null && !Entidades.contains(puerta)) {
                Entidades.add(puerta);
            }
            Entidades.removeIf(e ->
                (e instanceof ParticulaLadrillo && !nivelActual.particulas.contains(e))
                || (e instanceof Agujero && !nivelActual.agujeros.contains(e))
                || (e instanceof Escalera && !nivelActual.escaleras.contains(e))
                || (e instanceof Puerta && e != nivelActual.puertaSalida));

            // Escalera de escape
            if (heroe.nivelCompleto() && !nivelActual.escapeLadderActiva) {
                nivelActual.activarEscape();
            }

        if (nivelActual.escapeLadderActiva) {
            int htx = (int)((heroe.getX() + nivelActual.getTile_size() / 2) / nivelActual.getTile_size());
            int hty = (int)((heroe.getY() + nivelActual.getTile_size() / 2) / nivelActual.getTile_size());
            if (htx == nivelActual.escapeLadderX && hty == nivelActual.escapeLadderY) {
                int bonusTiempo = Math.max(0, (nivelActual.tiempoLimite * 60 - tiempoNivel) / 6);
                puntosJ1 += 500 + bonusTiempo;
                heroe.setVidas(heroe.getVidas() + 1);
                if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
                nivelActual.finalizarNivel();
                nivelIdx++;
                if (nivelIdx >= niveles.size()) {
                    rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", nivelIdx, puntosJ1);
                    if (menu != null) menu.recargarRanking();
                    estado = EstadoJuego.VICTORIA;
                    fxPlayer.detener("La Bestia Pop.mp3");
                    return;
                }
                cargarNivelActual();
            }
        }
    }

    private void gestionarMusica() {
        if (soundEnabled && musicEnabled) {
            if (!musicaIniciada) {
                fxPlayer.repetir("La Bestia Pop.mp3");
                musicaIniciada = true;
            }
        } else if (musicaIniciada) {
            fxPlayer.detener("La Bestia Pop.mp3");
            musicaIniciada = false;
        }
    }

    @Override
    public void onHeroDeath() {
        if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("empieza");
        Nivel nivelActual = (Nivel) this.NivelActual;
        int vidasGuardadas = heroe.getVidas();
        nivelActual.finalizarNivel();
        cargarNivelActual();
        heroe.setVidas(vidasGuardadas);
    }

    @Override
    public void onGameOver() {
        if (!rankingRegistrado) {
            rankingManager.agregarPuntaje(nombreJugadorPrincipal, "Lode Runner", nivelIdx + 1, puntosJ1);
            if (menu != null) menu.recargarRanking();
            rankingRegistrado = true;
        }
        heroe.desaparecer();
        estado = EstadoJuego.GAME_OVER;
        fxPlayer.detener("La Bestia Pop.mp3");
    }

    @Override
    public void onCoinCollected() {
        puntosJ1 += 100;
        if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("punto");
    }

    @Override
    public void onDig() {
        if (soundEnabled && soundFxEnabled) fxPlayer.reproducir("paleta");
    }

    @Override
    public String getGanador() { return nombreJugadorPrincipal; } // retorna nombre del ganador

    @Override
    public String getPerdedor() { return nombreJugadorPrincipal; } // retorna nombre del perdedor

    // suelta la moneda que lleva cargada un guardia en su posición actual
    private void soltarOroGuardia(Guardia g, Nivel nivel) {
        if (!g.isCargandoOro()) return;
        int tx = g.getTileX();
        int ty = g.getTileY();
        Moneda suelta = new Moneda(tx, ty, nivel.getTile_size());
        nivel.monedas.add(suelta); // agrega moneda al nivel
        Entidades.add(suelta); // agrega moneda al renderer
        g.setMonedaCargada(null); // guardia ya no lleva moneda
    }

    // asigna el nombre del jugador principal
    public void setNombreJugador(String nombre) {
        this.nombreJugadorPrincipal = nombre;
    }

    @Override
    // reinicia el juego, deteniendo música y estado
    protected void reiniciar() {
        fxPlayer.detener("soundtrack");
        musicaIniciada = false;
        super.reiniciar();
    }
}
