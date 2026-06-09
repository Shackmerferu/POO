
classDiagram
    %% INTERFACES
    class JuegoLoopable {
        <<interface>>
        +iniciar() void
        +actualizar() void
        +renderizar(Graphics g) void
        +finalizar() void
    }

    class Movible {
        <<interface>>
        +Mover() void
    }

    class Armado {
        <<interface>>
        +Disparar() Bala
    }

    class GameEventListener {
        <<interface>>
        +onHeroDeath() void
        +onGameOver() void
        +onCoinCollected() void
        +onDig() void
    }

    %% ENUMS
    class EstadoJuego {
        <<enum>>
        MENU
        JUGANDO
        PAUSA
        GAME_OVER
        VICTORIA
    }

    class Ladrillo~Estado~ {
        <<enum>>
        NORMAL
        BREAKING
        ROTO
        REGENERATING
    }

    class IA_Guardia~Comportamiento~ {
        <<enum>>
        PERSEGUIR
        VAGAR
        ATRAPADO
        REAPARECER
        REANIMACION
        SALIENDO
    }

    %% PACKAGE: py_poo
    class App {
        +main(String[] args) void
    }

    class Launcher {
        +C_BG Color
        +C_SURFACE Color
        +C_CARD Color
        +C_CARD_HOV Color
        +C_CARD_SEL Color
        +C_BORDER Color
        +C_BORDER_GOLD Color
        +C_TEXT Color
        +C_TEXT2 Color
        +C_TEXT3 Color
        +C_GOLD Color
        +C_GOLD_BG Color
        +C_RED Color
        +C_RED_BG Color
        +F_TITLE Font
        +F_SEC Font
        +F_BODY Font
        +F_CARD Font
        +F_SMALL Font
        +F_ICON Font
        +F_BTN Font
        -recursos CargadorRecursos
        -player String
        -focused int
        -carouselPanel JPanel
        -tabGamesPanel JPanel
        -storePanel JPanel
        -detailBar JPanel
        -bodyPanel JPanel
        -carouselScroll JScrollPane
        -bodyLayout CardLayout
        -sessionBtn JLabel
        -detailNameLbl JLabel
        -activeTab String
        +Launcher()
        -buildTopBar() JPanel
        -buildBody() JPanel
        -rebuildCarousel() void
        -scrollToFocused() void
        -buildDetailBar() JPanel
        -updateDetailBar() void
        -buildBottomBar() JPanel
        -openSession() void
        -updateSessionLabel() void
        -openAddGame() void
        -removeSelected() void
        -openGameConfig() void
        -openGlobalSettings() void
        -launchGame() void
        -crearJuego(String) VideoJuego
        -switchTab(String) void
        -clearFocus() void
        -darkPanel(LayoutManager) JPanel
        -darkPanel() JPanel
        -styledLabel(String, Font, Color) JLabel
        -darkField() JTextField
        -darkCombo(String[]) JComboBox~String~
        -darkCheck(String, boolean) JCheckBox
        -addFormRow(JPanel, String, JComponent) void
        -dialog(String) JDialog
        -hline() JSeparator
        -styleScrollBar(JScrollPane) void
        +aa(Graphics) Graphics2D
        -round(int, int, int, int, int) RoundRectangle2D~Float~
        -paintRnd(Graphics2D, JComponent, Color, Color, float, int) void
        -hover(JLabel, Color, Color, Consumer~MouseEvent~) MouseAdapter
    }

    %% PACKAGE: py_poo.core
    class Constantes {
        +WIDTH int
        +HEIGHT int
        +FPS int
    }

    class GameLoop {
        -instancia GameLoop
        -videojuego JuegoLoopable
        -deltaTime double
        -isFullscreen boolean
        +GameLoop(String, int, int)
        +setVideoJuego(JuegoLoopable) void
        +getVideoJuego() JuegoLoopable
        +getDeltaTime() double
        +getTeclado() Keyboard
        +getRaton() Mouse
        +getRuedaRaton() MouseWheel
        +terminarJuego() void
        +toggleFullscreen() void
        +toggleFullscreenStatic() void
        +isFullscreen() boolean
        #gameStartup() void
        #gameUpdate(double) void
        #gameDraw(Graphics2D) void
        #gameShutdown() void
        +run(int) void
    }

    %% PACKAGE: py_poo.engine
    class VideoJuego {
        <<abstract>>
        #Nombre String
        #Activo boolean
        #estado EstadoJuego
        #Puntuacion List~Integer~
        #NivelActual Nivel
        #Entidades List~ObjetoGrafico~
        -ResX int
        -ResY int
        #Fullscreen boolean
        #Jugador List~Jugador~
        #Resultado String
        #rankingManager RankingManager
        #nombreJugadorPrincipal String
        #input InputManager
        #soundEnabled boolean
        #soundFxEnabled boolean
        #musicEnabled boolean
        #configManager ConfigManager
        #camara Camara
        -lastBackslashState boolean
        -lastPauseState boolean
        -lastQState boolean
        -lastMState boolean
        +iniciar() void
        +actualizar() void
        -manejarControlesGlobales() void
        +finalizar() void
        #finalizar(EstadoJuego, String) void
        #pausa() void
        #crearPartida() void
        #actualizarLogicaJuego() void
        #reiniciar() void
        +cargarNivel() void
        +getResultado() String
        +renderizar(Graphics) void
        +getGanador() String
        +getPerdedor() String
        +getpuntaje() List~Integer~
        +iniciapuntaje(Jugador, Jugador) void
        +sumarPunto(int, int) void
        +resetPuntaje() void
        +setNombreJugador(String) void
        +setNombreJuego(String) void
        +getNombreJuego() String
        #getNivelActual() int
    }

    class Jugador {
        -Nombre String
        -id int
        -contadorid int
        +Jugador(String)
        +setNombre(String) void
        +getNombre() String
        +getid() int
    }

    class Camara {
        -X int
        -Y int
        +getX() int
        +getY() int
        +mover() void
        +seguirJugador(ObjetoGrafico, Nivel) void
    }

    %% PACKAGE: py_poo.entities
    class ObjetoGrafico {
        <<abstract>>
        #sprite BufferedImage
        #dimension Dimension
        #punto Point
        #hitbox Hitbox
        #paraEliminar boolean
        +ObjetoGrafico()
        +ObjetoGrafico(String)
        +ObjetoGrafico(String, Dimension, Point)
        +getSprite() BufferedImage
        +setSprite(String) void
        +desaparecer() void
        +display(Graphics) void
        +setDimension(Dimension) void
        +setPunto(Point) void
        +getWidth() int
        +getHeight() int
        +getX() double
        +setX(double) void
        +getY() double
        +setY(double) void
        +getHitbox() Hitbox
        +getBounds() Rectangle
        +getPunto() Point
        +isParaEliminar() boolean
        +marcarParaEliminar() void
        +actualizar() void
    }

    class Personaje {
        <<abstract>>
        #vidas int
        #direccion int
        +mover() void
        +getVidas() int
        +recibirDanio(int) void
        +setVidas(int) void
        +agregarVida(int) void
    }

    class Bala {
        -danio int
        +Mover() void
        +impactar() void
    }

    class Bloque {
        <<abstract>>
        #destruible boolean
        #valor int
        +getValor() int
        +recoger() void
        +destruir() void
    }

    class Ladrillo {
        -animNormal Animacion
        -animBreaking Animacion
        -animRegen Animacion
        -tileSize int
        -irrompible boolean
        -estado Estado
        +Ladrillo(int, int, int, boolean)
        -cargarAnimaciones() void
        +iniciarBreaking() void
        +iniciarRegen() void
        +getEstado() Estado
        +isRoto() boolean
        +isIrrompible() boolean
        +actualizar() void
        +display(Graphics) void
    }

    class Escalera {
        -animacion Animacion
        -tileSize int
        +Escalera(int, int, int)
        -cargarAnimacion() void
        +display(Graphics) void
    }

    class Barra {
        -spriteBarra Sprite
        -tileSize int
        +Barra(int, int, int)
        -cargarSprite() void
        +deslizar() void
        +colgar() void
        +display(Graphics) void
    }

    class Moneda {
        -recolectada boolean
        -animacion Animacion
        -tileSize int
        +Moneda(int, int, int)
        -cargarAnimacion() void
        +recolectar() void
        +isRecolectada() boolean
        +recoger() void
        +actualizar() void
        +display(Graphics) void
    }

    class Agujero {
        -TIEMPO_CIERRE int
        -contador int
        -abierto boolean
        -ladrilloAsociado Ladrillo
        +Agujero(int, int)
        +Agujero(int, int, Ladrillo)
        +getLadrilloAsociado() Ladrillo
        +display(Graphics) void
        +actualizar() void
        +abrir() void
        +cerrar() void
        +isAbierto() boolean
        +getTiempoRestante() int
        +getProgreso() float
    }

    class ParticulaLadrillo {
        -animacion Animacion
        -tileSize int
        -activo boolean
        +ParticulaLadrillo(int, int, int)
        -cargarAnimacion() void
        +actualizar() void
        +isActivo() boolean
        +display(Graphics) void
    }

    class Puerta {
        -spritePuerta Sprite
        -tileSize int
        -visible boolean
        +Puerta(int, int, int)
        -cargarSprite() void
        +mostrar() void
        +ocultar() void
        +isVisible() boolean
        +display(Graphics) void
    }

    %% PACKAGE: py_poo.graphics
    class Sprite {
        -imagen BufferedImage
        +Sprite(BufferedImage)
        +Sprite(String)
        +getImagen() BufferedImage
        +dibujar(Graphics, int, int) void
        +dibujar(Graphics, int, int, int, int) void
        +getWidth() int
        +getHeight() int
    }

    class SpriteSheet {
        -sprites List~Sprite~
        +SpriteSheet(BufferedImage, int, int)
        +SpriteSheet(List~BufferedImage~)
        +obtenerSprite(int) Sprite
        +size() int
    }

    class Animacion {
        -frames List~Sprite~
        -frameActual int
        -tiempoPorFrame long
        -ultimoTiempo long
        -repitiendo boolean
        +Animacion(List~Sprite~, long)
        +actualizar() void
        +obtenerFrame() Sprite
        +reiniciar() void
        +setRepitiendo(boolean) void
        +termino() boolean
        +dibujar(Graphics, int, int) void
        +dibujar(Graphics, int, int, int, int) void
    }

    %% PACKAGE: py_poo.input
    class InputManager {
        -lastEnterTime long
        -lastMenuUpTime long
        -lastMenuDownTime long
        -COOLDOWN_MS long
        +isKeyPressed(int) boolean
        +isEnterPressed() boolean
        +isWPressed() boolean
        +isSPressed() boolean
        +isUpPressed() boolean
        +isDownPressed() boolean
        +isLeftPressed() boolean
        +isRightPressed() boolean
        +isPPressed() boolean
        +isCtrlPressed() boolean
        +isBackslashPressed() boolean
        +isEscapePressed() boolean
        +isQPressed() boolean
        +isMPressed() boolean
        +isDigPressed() boolean
        +isSpacePressed() boolean
        +isMenuUpPressed() boolean
        +isMenuDownPressed() boolean
    }

    class MouseManager {
        +getX() int
        +getY() int
        +isLeftPressed() boolean
        +isRightPressed() boolean
        +isMiddlePressed() boolean
    }

    %% PACKAGE: py_poo.collision
    class CollisionManager {
        +verificarColisiones(List~ObjetoGrafico~) void
        +colisiona(ObjetoGrafico, ObjetoGrafico) boolean
    }

    class Hitbox {
        -x int
        -y int
        -width int
        -height int
        +Hitbox(int, int, int, int)
        +setPosicion(int, int) void
        +setDimension(int, int) void
        +getBounds() Rectangle
        +getX() int
        +getY() int
        +getWidth() int
        +getHeight() int
    }

    %% PACKAGE: py_poo.config
    class ConfigManager {
        -ARCHIVO String
        -volumen float
        -fullscreen boolean
        -soundEnabled boolean
        -soundFxEnabled boolean
        -musicEnabled boolean
        -keyBindings Map~String, Integer~
        +ConfigManager()
        +cargar() void
        +guardar() void
        -aplicarKeyBindings() void
        +getVolumen() float
        +setVolumen(float) void
        +isFullscreen() boolean
        +setFullscreen(boolean) void
        +isSoundEnabled() boolean
        +setSoundEnabled(boolean) void
        +isSoundFxEnabled() boolean
        +setSoundFxEnabled(boolean) void
        +isMusicEnabled() boolean
        +setMusicEnabled(boolean) void
        +leer() void
        +escribir() void
    }

    class KeyBindings {
        -bindings Map~String, Integer~
        +get(String) int
        +set(String, int) void
        +keyName(int) String
        +getActionNames() String[]
    }

    %% PACKAGE: py_poo.audio
    class FXPlayer {
        -sonido Map~String, Clip~
        -volumen int
        +FXPlayer()
        +cargarSonidoRecurso(String, String) void
        +reproducir(String) void
        +detener(String) void
        +setVolumen(String, String) void
        +mutear() void
        +repetir(String) void
    }

    %% PACKAGE: py_poo.ranking
    class RankingManager {
        -DEFAULT_DB_PATH String
        -dbUrl String
        -puntajes List~Integer~
        +RankingManager()
        +RankingManager(String)
        +agregarPuntaje() void
        +agregarPuntaje(String, String, int, int) void
        +guardarRanking() void
        +cargarRanking() void
        +getPuntajes() List~Integer~
        +cargarPuntajesTop(String, int) List~Integer~
        +cargarDetalleTop(String, int) List~RankingEntry~
        -inicializarTabla() void
        -crearCarpetaSiNoExiste(String) void
    }

    class RankingEntry {
        <<record>>
        +jugador String
        +juego String
        +Nivel int
        +puntaje int
        +fecha String
    }

    %% PACKAGE: py_poo.ui
    class MenuPrincipal {
        #input InputManager
        #tituloLbl JLabel
        #ctrlJ1 JLabel
        #ctrlJ2 JLabel
        #tarjetaCentral JPanel
        #configMode boolean
        #configSelected int
        #configActionIndex int
        #lastConfigKeyTime long
        #configActions String[]
        #getConfigActions() String[]
        +MenuPrincipal(String, String, Color, String, String)
        +actualizar() void
        +renderizar() void
        +isConfigMode() boolean
        +setConfigMode(boolean) void
        +actualizarConfig() void
        -guardarConfiguracion() void
        -reiniciarDefaults() void
        -obtenerDefault(String) int
        +dibujarConfig(Graphics) void
    }

    %% PACKAGE: py_poo.utils
    class CargadorRecursos {
        +cargarImagen(String) BufferedImage
        +cargarSonido() void
    }

    %% PACKAGE: py_poo.loderunner
    class JuegoLodeRunner {
        -input InputManager
        -menu MenuLodeRunner
        -heroe Recolector
        -guardias List~Guardia~
        -niveles List~Nivel~
        -nivelIdx int
        -puntosJ1 int
        -rankingRegistrado boolean
        -tiempoNivel int
        -fondo BufferedImage
        -fxPlayer FXPlayer
        -musicaIniciada boolean
        +JuegoLodeRunner()
        +iniciar() void
        +pause() void
        +renderizar(Graphics) void
        #crearPartida() void
        -cargarNivelActual() void
        #actualizarLogicaJuego() void
        -gestionarMusica() void
        +onHeroDeath() void
        +onGameOver() void
        +onCoinCollected() void
        +onDig() void
        +getGanador() String
        +getPerdedor() String
        +setNombreJugador(String) void
    }

    class MenuLodeRunner {
        -seleccion int
        -rankingManager RankingManager
        -topRanking List~RankingEntry~
        +MenuLodeRunner(InputManager, Object)
        +getSeleccion() int
        +setSeleccion(int) void
        +recargarRanking() void
        #getConfigActions() String[]
        +actualizar() void
        +dibujar(Graphics) void
    }

    class Recolector {
        +VELOCIDAD int
        +VIDAS_INICIALES int
        -oroRecolectado int
        -nivelOroTotal int
        -enEscalera boolean
        -enBarra boolean
        -cayendo boolean
        -cavoEsteFrame boolean
        -enAire boolean
        -tileX int
        -tileY int
        -tileSize int
        -input InputManager
        -nivel Nivel
        -guardias List~Guardia~
        -listener GameEventListener
        -animParado Animacion
        -animCaminando Animacion
        -animEscalera Animacion
        -animBarra Animacion
        +Recolector(int, int, int)
        -cargarAnimaciones() void
        +setInputManager(InputManager) void
        +setNivel(Nivel) void
        +setGuardias(List~Guardia~) void
        +setGameEventListener(GameEventListener) void
        +mover() void
        -aplicarGravedad() void
        -tieneSoporte(int, int) boolean
        -detectarPlataforma() void
        +actualizar() void
        +moverIzquierda() void
        +moverDerecha() void
        +moverArriba() void
        +moverAbajo() void
        +cavarIzquierda() void
        +cavarDerecha() void
        +recogerOro() void
        +reiniciarPosicion() void
        +perderVida() void
        +getOroRecolectado() int
        +setNivelOroTotal(int) void
        +getNivelOroTotal() int
        +nivelCompleto() boolean
        +cavoEsteFrame() boolean
        +verificarCaidaEnAgujero() void
        +verificarColisionGuardias() void
        +recolectarMonedas() void
        +isEnEscalera() boolean
        +isEnBarra() boolean
        +isCayendo() boolean
        +isEnAire() boolean
        +estaEnAgujero() boolean
        -hayGuardiaEnAgujero(Agujero) boolean
        -hayGuardiaEnTile(int, int) boolean
        -hayAgujeroSeguro(int, int) boolean
        +getTileX() int
        +getTileY() int
        +display(Graphics) void
    }

    class Guardia {
        +VELOCIDAD double
        -ia IA_Guardia
        -heroe Recolector
        -nivel Nivel
        -enAgujero boolean
        -enEscalera boolean
        -enBarra boolean
        -cayendo boolean
        -monedaCargada Moneda
        -enAire boolean
        -tileSize int
        -spawnTileX int
        -spawnTileY int
        -animCaminando Animacion
        -animAtrapado Animacion
        -tiempoEsperaEscape int
        -contadorAtascado int
        +Guardia(int, int, int)
        -cargarAnimaciones() void
        +actualizar() void
        +moverIzquierda() void
        +moverDerecha() void
        +moverArriba() void
        +moverAbajo() void
        +reaparecer() void
        +getTileX() int
        +getTileY() int
        +getIA() IA_Guardia
        +isEnEscalera() boolean
        +setHeroe(Recolector) void
        +setNivel(Nivel) void
        +mover() void
        -tieneSoporte(int, int) boolean
        -hayAgujeroSeguro(int, int) boolean
        -aplicarGravedad() void
        -detectarPlataforma() void
        +iniciarEscape(int) void
        +setEnEscalera(boolean) void
        +isEnBarra() boolean
        +isCayendo() boolean
        +isCargandoOro() boolean
        +getMonedaCargada() Moneda
        +soltarMoneda() void
        +intentarRecolectarOro() void
        +manejarColisionAgujero(List~Agujero~, List~Guardia~) boolean
        +isEnAire() boolean
        +enAgujero() boolean
        +display(Graphics) void
    }

    class IA_Guardia {
        -rand Random
        -direccionPreferida int
        -contadorCambio int
        -CAMBIO_CADENCIA int
        -CAMPO_VISION int
        -estado Comportamiento
        -tiempoAtrapado int
        -tiempoReanimacion int
        -TIEMPO_MAX_ATRAPADO int
        -TIEMPO_REANIMACION int
        -TIEMPO_ESCAPE int
        -TIEMPO_ESPERA_ESCAPE int
        +IA_Guardia()
        +calcularMovimiento(int, int, int, int, boolean, boolean, boolean, boolean, boolean, boolean) int
        -calcularPersecucion(int, int, int, int, boolean, boolean, boolean, boolean, boolean, boolean) int
        -calcularVagar(int, int, int, int, boolean, boolean, boolean, boolean, boolean, boolean) int
        +getEstado() Comportamiento
        +setEstado(Comportamiento) void
        +atrapar() void
        +salir() void
        +reaparecer() void
        +getTiempoAtrapado() int
        +incrementarTiempoAtrapado() void
        +getTiempoEscape() int
        +getTiempoEsperaEscape() int
        +isSaliendo() boolean
        +reanimar() void
        +cambiarAPersecucion() void
        +isPersiguiendo() boolean
    }

    class Nivel {
        #Numero int
        #Mapa String[]
        #mapa char[][]
        #tile_size int
        #Entidades List~ObjetoGrafico~
        #ladrillos List~Ladrillo~
        #ladrillosIrrompibles List~Ladrillo~
        #escaleras List~Escalera~
        #barras List~Barra~
        #monedas List~Moneda~
        #agujeros List~Agujero~
        #particulas List~ParticulaLadrillo~
        #escapeLadderX int
        #escapeLadderY int
        #escapeLadderActiva boolean
        #spawnRecolectorX int
        #spawnRecolectorY int
        #puertaSalida Puerta
        #spawnGuardias List~int[]~
        #totalOro int
        +tiempoLimite int
        +Nivel()
        +Nivel(int, String[])
        +getNumero() int
        +getTile_size() int
        +getTile(int, int) char
        +esSolido(int, int) boolean
        +esLadrilloCavable(int, int) boolean
        +esEscalera(int, int) boolean
        +esBarra(int, int) boolean
        +esMoneda(int, int) boolean
        +esVacio(int, int) boolean
        +setTile(int, int, char) void
        +agregarEntidad(ObjetoGrafico) void
        +cargar() void
        +actualizar() void
        +renderizar() void
        +sincronizarEntidades(List~ObjetoGrafico~) void
        +activarEscape() void
        +finalizarNivel() void
        +getAnchoMapa() int
        +getAltoMapa() int
        +getAnchoPixels() int
        +getAltoPixels() int
        +getMonedaEn(int, int) Moneda
        +cavarEn(int, int) boolean
    }

    class Nivel1 {
        -MAPA String[]
        +Nivel1()
    }

    class Nivel2 {
        -MAPA String[]
        +Nivel2()
    }

    class Nivel3 {
        -MAPA String[]
        +Nivel3()
    }

    %% PACKAGE: py_poo.pong
    class JuegoPong {
        -OpJuego boolean
        -input InputManager
        -menu MenuPong
        -paleta1 Paleta
        -paleta2 Paleta
        -pelota PelotaPong
        -collisionManager CollisionManager
        -fxPlayer FXPlayer
        -puntosJ1 int
        -puntosJ2 int
        -PUNTOS_MAX int
        -fondo BufferedImage
        -modoIA boolean
        -ia IA_Pong
        -rankingRegistrado boolean
        +setOpJuego(boolean) void
        +setPuntosMax(int) void
        +iniciar() void
        +pause() void
        +renderizar(Graphics) void
        #crearPartida() void
        +getGanador() String
        +getPerdedor() String
        #actualizarLogicaJuego() void
        -registrarRankingFinal() void
    }

    class MenuPong {
        -input InputManager
        -seleccion int
        -configMode boolean
        -configSelected int
        -configActionIndex int
        -lastConfigKeyTime long
        -rankingManager RankingManager
        -topRanking List~RankingEntry~
        +MenuPong(InputManager, Object)
        +getSeleccion() int
        +setSeleccion(int) void
        +isConfigMode() boolean
        +setConfigMode(boolean) void
        +actualizarConfig() void
        +dibujarConfig(Graphics) void
        +actualizar() void
        +dibujar(Graphics) void
    }

    class Paleta {
        -velocidad int
        -input InputManager
        -idJugador int
        +Paleta(InputManager, int)
        +Mover() void
        +ResetearPOS() void
        +dibujar(Graphics) void
    }

    class PelotaPong {
        -dx double
        -dy double
        -velocidadBase double
        +PelotaPong()
        +mover() void
        +rebotarParedes() void
        +rebotarPaleta(Paleta) void
        +salioIzquierda() boolean
        +salioDerecha() boolean
        +aumentarVelocidad() void
        +display(Graphics) void
        +reiniciar() void
        +reiniciar(boolean) void
    }

    class IA_Pong {
        -dificultad int
        -pelota PelotaPong
        -paleta Paleta
        -margenError int
        -velocidad double
        -puntosRonda int
        -margenMinimo int
        +IA_Pong(PelotaPong, Paleta, int)
        +setDificultad(int) void
        +incrementarDificultad() void
        +calcularMovimiento() void
    }

    %% PACKAGE: py_poo.spaceinvaders
    class JuegoSpaceInvaders {
        -fxPlayer FXPlayer
        -input InputManager
        -menu MenuSpaceInvaders
        -navecita NaveJugador
        -flotaE HashMap~String, Enemigo~
        -direccionflotaX int
        -velocidadflotaY int
        -disparo Laser
        -ultimoMovimientoFlota long
        -platoVolador NaveNodriza
        -nivelDeFlota int
        -puntaje int
        -contadorDisparos int
        -nivel NivelSpaceInvaders
        -rankingManager RankingManager
        -rankingRegistrado boolean
        -skinsNave String[]
        -skinsLaser String[]
        -skinsLaserEnemigo String[]
        +iniciar() void
        #actualizarLogicaJuego() void
        +pause() void
        +renderizar(Graphics) void
        #crearPartida() void
        +getGanador() String
        +getPerdedor() String
        -registrarRankingFinal() void
    }

    class MenuSpaceInvaders {
        -juego JuegoSpaceInvaders
        -seleccion int
        -delay int
        -ultimoTiempo long
        -configMode boolean
        -configSelected int
        -configActionIndex int
        -lastConfigKeyTime long
        -pantallaCompleta boolean
        -sonidoActivado boolean
        -skinNave int
        -skinInvasores int
        -skinProyectiles int
        -pistaMusical int
        -velocidad int
        -opcionesConfig String[]
        -rankingManager RankingManager
        -topRanking List~RankingEntry~
        +MenuSpaceInvaders(InputManager, JuegoSpaceInvaders)
        +getSeleccion() int
        +setSeleccion(int) void
        +recargarRanking() void
        +setVisible(boolean) void
        +actualizar() void
        +dibujarConfig(Graphics) void
        +dibujar(Graphics) void
        +setConfigMode(boolean) void
        +actualizarConfig() void
        +isConfigMode() boolean
        +isSonidoActivado() boolean
        +getSkinNave() int
        +getSkinInvasores() int
        +getSkinProyectiles() int
    }

    class Enemigo {
        #puntosxKill int
        -animacion Animacion
        +Enemigo(int, int, Animacion)
        +actualizacionAnimacion() void
        +display(Graphics) void
        +getPuntos() int
    }

    class EnemigoA {
        +EnemigoA(int, int)
        -animacionFlota() Animacion
        +Disparar() Bala
    }

    class EnemigoB {
        +EnemigoB(int, int)
        -animacionFlota() Animacion
    }

    class EnemigoC {
        +EnemigoC(int, int)
        -animacionFlota() Animacion
    }

    class NaveJugador {
        -laserSkin String
        +NaveJugador(int, int, String, String)
        +Disparar() Laser
    }

    class Laser {
        -velocidad int
        +Laser(int, int, int, String)
        +getVelocidad() int
        +Mover() void
        +actualizar() void
    }

    class NaveNodriza {
        -random Random
        -velocidad int
        +NaveNodriza()
        +puntaje(int) int
        +actualizar() void
    }

    class Escudo {
        -segmentos SegmentoEscudo[]
        +Escudo(int, int)
        +getSegmentos() SegmentoEscudo[]
    }

    class SegmentoEscudo {
        -estado int
        -archivoSano String
        -archivoDaniado String
        +SegmentoEscudo(int, int, String, String)
        -actualizarSprite() void
        +recibirDanio() void
        +getEstado() int
    }

    class Murido {
        -creadoEn long
        +Murido(int, int, int)
        +actualizar() void
    }

    class NivelSpaceInvaders {
        +generarOleadas(HashMap~String, Enemigo~, List~ObjetoGrafico~, int, int) void
    }

    %% ═══════════════════════════════════════════════════════════════
    %% RELACIONES DE HERENCIA
    %% ═══════════════════════════════════════════════════════════════
    Launcher --|> JFrame : hereda de
    MenuPrincipal --|> JFrame : hereda de
    MenuLodeRunner --|> MenuPrincipal : hereda de
    MenuPong --|> MenuPrincipal : hereda de
    MenuSpaceInvaders --|> MenuPrincipal : hereda de
    GameLoop --|> Game : hereda de
    VideoJuego ..|> JuegoLoopable : implementa
    JuegoLodeRunner --|> VideoJuego : hereda de
    JuegoSpaceInvaders --|> VideoJuego : hereda de
    JuegoPong --|> VideoJuego : hereda de
    JuegoLodeRunner ..|> GameEventListener : implementa
    Personaje --|> ObjetoGrafico : hereda de
    Bloque --|> ObjetoGrafico : hereda de
    Bala --|> ObjetoGrafico : hereda de
    Guardia --|> Personaje : hereda de
    Recolector --|> Personaje : hereda de
    Enemigo --|> Personaje : hereda de
    NaveJugador --|> Personaje : hereda de
    Ladrillo --|> Bloque : hereda de
    Escalera --|> Bloque : hereda de
    Barra --|> Bloque : hereda de
    Moneda --|> Bloque : hereda de
    Agujero --|> Bloque : hereda de
    Laser --|> Bala : hereda de
    Puerta --|> ObjetoGrafico : hereda de
    ParticulaLadrillo --|> ObjetoGrafico : hereda de
    NaveNodriza --|> ObjetoGrafico : hereda de
    Escudo --|> ObjetoGrafico : hereda de
    SegmentoEscudo --|> ObjetoGrafico : hereda de
    Murido --|> ObjetoGrafico : hereda de
    PelotaPong --|> ObjetoGrafico : hereda de
    Paleta --|> ObjetoGrafico : hereda de
    EnemigoA --|> Enemigo : hereda de
    EnemigoB --|> Enemigo : hereda de
    EnemigoC --|> Enemigo : hereda de
    Bala ..|> Movible : implementa
    Paleta ..|> Movible : implementa
    EnemigoA ..|> Armado : implementa
    NaveJugador ..|> Armado : implementa
    Nivel1 --|> Nivel : hereda de
    Nivel2 --|> Nivel : hereda de
    Nivel3 --|> Nivel : hereda de

    %% ═══════════════════════════════════════════════════════════════
    %% RELACIONES DE COMPOSICIÓN Y ASOCIACIÓN
    %% ═══════════════════════════════════════════════════════════════
    App "1" --> "1" Launcher : crear
    Launcher "1" --> "1" CargadorRecursos : usar
    Launcher "1" --> "1" GameLoop : crear
    Launcher ..> Constantes : usar constantes
    Launcher "1" --> "1" JuegoPong : crear
    Launcher "1" --> "1" JuegoSpaceInvaders : crear
    Launcher "1" --> "1" JuegoLodeRunner : crear

    GameLoop "1" --> "1" JuegoLoopable : ejecutar
    GameLoop ..> Constantes : usar constantes

    VideoJuego "1" --> "1" ConfigManager : gestionar
    VideoJuego "1" --> "1" InputManager : usar
    VideoJuego "1" --> "1" Camara : usar
    VideoJuego "1" --> "1" RankingManager : gestionar
    VideoJuego "1" --> "*" EstadoJuego : usar
    VideoJuego "1" --> "*" Jugador : contener
    VideoJuego "1" --> "1" Nivel : gestionar
    VideoJuego "1" --> "*" ObjetoGrafico : contener
    VideoJuego "1" --> "1" GameLoop : usar

    Camara "1" --> "1" ObjetoGrafico : seguir
    Camara "1" --> "1" Nivel : consultar
    Camara ..> Constantes : usar constantes

    ObjetoGrafico "1" --> "1" Hitbox : contener

    CollisionManager "1" --> "*" ObjetoGrafico : verificar

    InputManager "1" --> "1" KeyBindings : consultar
    InputManager "1" --> "1" GameLoop : obtener teclado

    MouseManager "1" --> "1" GameLoop : obtener raton

    ConfigManager "1" --> "1" KeyBindings : persistir

    MenuPrincipal "1" --> "1" InputManager : usar
    MenuPrincipal "1" --> "1" KeyBindings : consultar

    MenuLodeRunner "1" --> "1" RankingManager : consultar
    MenuLodeRunner ..> Constantes : usar constantes

    MenuPong "1" --> "1" RankingManager : consultar
    MenuPong "1" --> "1" KeyBindings : consultar
    MenuPong ..> Constantes : usar constantes

    MenuSpaceInvaders "1" --> "1" JuegoSpaceInvaders : referenciar
    MenuSpaceInvaders "1" --> "1" RankingManager : consultar
    MenuSpaceInvaders "1" --> "1" KeyBindings : consultar
    MenuSpaceInvaders ..> Constantes : usar constantes

    JuegoLodeRunner "1" --> "1" InputManager : usar
    JuegoLodeRunner "1" --> "1" MenuLodeRunner : gestionar
    JuegoLodeRunner "1" --> "1" Recolector : contener
    JuegoLodeRunner "1" --> "*" Guardia : contener
    JuegoLodeRunner "1" --> "*" Nivel : gestionar
    JuegoLodeRunner "1" --> "*" Nivel1 : crear
    JuegoLodeRunner "1" --> "*" Nivel2 : crear
    JuegoLodeRunner "1" --> "*" Nivel3 : crear
    JuegoLodeRunner ..> Constantes : usar constantes
    JuegoLodeRunner "1" --> "1" FXPlayer : reproducir sonidos
    JuegoLodeRunner "1" --> "1" GameLoop : usar

    JuegoSpaceInvaders "1" --> "1" FXPlayer : reproducir sonidos
    JuegoSpaceInvaders "1" --> "1" InputManager : usar
    JuegoSpaceInvaders "1" --> "1" MenuSpaceInvaders : gestionar
    JuegoSpaceInvaders "1" --> "1" NaveJugador : contener
    JuegoSpaceInvaders "1" --> "*" Enemigo : contener
    JuegoSpaceInvaders "1" --> "1" Laser : disparar
    JuegoSpaceInvaders "1" --> "1" NaveNodriza : generar
    JuegoSpaceInvaders "1" --> "1" NivelSpaceInvaders : gestionar
    JuegoSpaceInvaders ..> Constantes : usar constantes
    JuegoSpaceInvaders "1" --> "*" Escudo : crear
    JuegoSpaceInvaders "1" --> "*" SegmentoEscudo : usar
    JuegoSpaceInvaders "1" --> "*" Murido : crear
    JuegoSpaceInvaders "1" --> "1" GameLoop : usar

    JuegoPong "1" --> "1" InputManager : usar
    JuegoPong "1" --> "1" MenuPong : gestionar
    JuegoPong "1" --> "2" Paleta : contener
    JuegoPong "1" --> "1" PelotaPong : contener
    JuegoPong "1" --> "1" CollisionManager : usar
    JuegoPong "1" --> "1" FXPlayer : reproducir sonidos
    JuegoPong "1" --> "1" IA_Pong : usar
    JuegoPong ..> Constantes : usar constantes

    Nivel "1" --> "*" Ladrillo : contener
    Nivel "1" --> "*" Escalera : contener
    Nivel "1" --> "*" Barra : contener
    Nivel "1" --> "*" Moneda : contener
    Nivel "1" --> "*" Agujero : contener
    Nivel "1" --> "*" ParticulaLadrillo : contener
    Nivel "1" --> "1" Puerta : contener
    Nivel "1" --> "*" ObjetoGrafico : contener

    Guardia "1" --> "1" IA_Guardia : usar
    Guardia "1" --> "1" Recolector : perseguir
    Guardia "1" --> "1" Nivel : explorar
    Guardia "1" --> "1" Moneda : cargar
    Guardia "1" --> "*" Animacion : usar
    Guardia "1" --> "*" Agujero : detectar

    Recolector "1" --> "1" InputManager : usar
    Recolector "1" --> "1" Nivel : explorar
    Recolector "1" --> "*" Guardia : detectar
    Recolector "1" --> "1" GameEventListener : notificar
    Recolector "1" --> "*" Animacion : usar

    Enemigo "1" --> "1" Animacion : usar

    EnemigoA "1" --> "1" Laser : disparar

    NaveJugador "1" --> "1" Laser : disparar

    Ladrillo "1" --> "*" Animacion : usar
    Ladrillo "1" --> "1" CargadorRecursos : usar
    Escalera "1" --> "1" Animacion : usar
    Escalera "1" --> "1" CargadorRecursos : usar
    Barra "1" --> "1" Sprite : usar
    Barra "1" --> "1" CargadorRecursos : usar
    Moneda "1" --> "1" Animacion : usar
    Moneda "1" --> "1" CargadorRecursos : usar
    Agujero "1" --> "1" Ladrillo : asociar
    Puerta "1" --> "1" Sprite : usar
    Puerta "1" --> "1" CargadorRecursos : usar
    ParticulaLadrillo "1" --> "1" Animacion : usar
    ParticulaLadrillo "1" --> "1" CargadorRecursos : usar

    Paleta "1" --> "1" InputManager : leer
    Paleta ..> Constantes : usar constantes

    PelotaPong ..> Constantes : usar constantes

    NaveNodriza ..> Constantes : usar constantes

    Escudo "1" --> "*" SegmentoEscudo : contener

    Animacion "1" --> "*" Sprite : animar

    SpriteSheet "1" --> "*" Sprite : contener

    IA_Pong "1" --> "1" PelotaPong : seguir
    IA_Pong "1" --> "1" Paleta : controlar
    IA_Pong ..> Constantes : usar constantes

    RankingManager "1" --> "*" Nivel : persistir nivel

