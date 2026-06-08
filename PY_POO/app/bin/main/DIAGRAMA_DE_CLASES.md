# Diagrama de Clases — Proyecto POO

---

## Interfaces

### `<<interface>> JuegoLoopable`
| |
|---|
| + iniciar(): void |
| + actualizar(): void |
| + renderizar(g: Graphics): void |
| + finalizar(): void |

### `<<interface>> Movible`
| |
|---|
| + Mover(): void |

### `<<interface>> Armado`
| |
|---|
| + Disparar(): Bala |

### `<<interface>> Colisionable`
| |
|---|
| + colisionar(entidad: ObjetoGrafico): void |
| + getBounds(): Object |
| + getcolision(): Rectangle |
| + setcolision(Dimension: int): Void |

### `<<interface>> Renderizable`
| |
|---|
| + renderizar(g: Graphics): void |

### `<<interface>> Sonoro`
| |
|---|
| + reproducirSonido(sonido: String): void |

### `<<interface>> Configurable`
| |
|---|
| + cargarConfiguracion(): void |
| + guardarConfiguracion(): void |

---

## Core

### `GameLoop` (Singleton)
*extends `com.entropyinteractive.Game`*
| |
|---|
| - instancia: GameLoop |
| - videojuego: JuegoLoopable |
| - deltaTime: double |
| - isFullscreen: boolean |
|---|
| + GameLoop(title: String, width: int, height: int) |
| + setVideoJuego(vj: JuegoLoopable): void |
| + getVideoJuego(): JuegoLoopable |
| + getDeltaTime(): double |
| + getTeclado(): Keyboard |
| + getRaton(): Mouse |
| + getRuedaRaton(): MouseWheel |
| + terminarJuego(): void |
| + toggleFullscreen(): void |
| + toggleFullscreenStatic(): void |
| + isFullscreen(): boolean |
| + gameStartup(): void |
| + gameUpdate(delta: double): void |
| + gameDraw(g: Graphics2D): void |
| + gameShutdown(): void |
| + run(fps: int): void |

### `Constantes`
| |
|---|
| + WIDTH: int = 800 |
| + HEIGHT: int = 600 |
| + FPS: int = 120 |

---

## Engine

### `<<enum>> EstadoJuego`
| |
|---|
| MENU |
| JUGANDO |
| PAUSA |
| GAME_OVER |
| VICTORIA |

### `<<abstract>> VideoJuego`
*implements `JuegoLoopable`*
| |
|---|
| # Nombre: String |
| # Activo: boolean |
| # estado: EstadoJuego |
| # Puntuacion: List<Integer> |
| # NivelActual: Nivel |
| # Entidades: List<ObjetoGrafico> |
| - ResX: int |
| - ResY: int |
| # Fullscreen: boolean |
| # Jugador: List<Jugador> |
| - Resultado: String |
| # rankingManager: RankingManager |
| # nombreJugadorPrincipal: String |
| # input: InputManager |
| # soundEnabled: boolean |
| # soundFxEnabled: boolean |
| # musicEnabled: boolean |
| # configManager: ConfigManager |
| # camara: Camara |
| - lastBackslashState: boolean |
| - lastPauseState: boolean |
| - lastQState: boolean |
| - lastMState: boolean |
|---|
| + iniciar(): void |
| + actualizar(): void |
| - manejarControlesGlobales(): void |
| + finalizar(): void |
| # finalizar(estadoFinal: EstadoJuego, resultado: String): void |
| # pausa(): void |
| # crearPartida(): void |
| # abstract actualizarLogicaJuego(): void |
| # reiniciar(): void |
| + cargarNivel(): void |
| + getResultado(): String |
| + renderizar(g: Graphics): void |
| + abstract getGanador(): String |
| + abstract getPerdedor(): String |
| + getpuntaje(): List<Integer> |
| + iniciapuntaje(J1: Jugador, J2: Jugador): void |
| + sumarPunto(id: int, Puntaje: int): void |
| + resetPuntaje(): void |
| + setNombreJugador(nombre: String): void |
| + setNombreJuego(nombre: String): void |
| + getNombreJuego(): String |
| # getNivelActual(): int |

### `Jugador`
| |
|---|
| - Nombre: String |
| - id: int |
| - contadorid: int |
|---|
| + Jugador() |
| + Jugador(Nombre: String) |
| + setNombre(nombre: String): void |
| + getNombre(): String |
| + getid(): int |

### `Camara`
| |
|---|
| - X: int |
| - Y: int |
|---|
| + getX(): int |
| + getY(): int |
| + mover(): void |
| + seguirJugador(obj: ObjetoGrafico, nivel: Nivel): void |

### `<<abstract>> Escena`
| |
|---|
| + iniciar(): void |
| + actualizar(): void |
| + renderizar(): void |
| + cerrar(): void |

### `Temporizador`
| |
|---|
| - Tiempo: long |
|---|
| + iniciar(): void |
| + reiniciar(): void |
| + obtenerTiempo(): long |

---

## Entities

### `<<abstract>> ObjetoGrafico`
| |
|---|
| # sprite: BufferedImage |
| # dimension: Dimension |
| # punto: Point |
| # hitbox: Hitbox |
| # paraEliminar: boolean |
|---|
| + ObjetoGrafico() |
| + ObjetoGrafico(sprite: String) |
| + ObjetoGrafico(sprite: String, dimension: Dimension, punto: Point) |
| + getSprite(): BufferedImage |
| + setSprite(sprite: String): void |
| + desaparecer(): void |
| + display(g: Graphics): void |
| + setDimension(dimension: Dimension): void |
| + setPunto(punto: Point): void |
| + getWidth(): int |
| + getHeight(): int |
| + getX(): double |
| + setX(x: double): void |
| + getY(): double |
| + setY(y: double): void |
| + getHitbox(): Hitbox |
| + getBounds(): Rectangle |
| + getPunto(): Point |
| + isParaEliminar(): boolean |
| + marcarParaEliminar(): void |
| + actualizar(): void |

### `<<abstract>> Personaje`
*extends `ObjetoGrafico`*
| |
|---|
| # vidas: int |
| # direccion: int |
|---|
| + mover(): void |
| + recibirDanio(cantidad: int): void |
| + getVidas(): int |
| + setVidas(vidas: int): void |

### `<<abstract>> Bloque`
*extends `ObjetoGrafico`*
| |
|---|
| # destruible: boolean |
| # valor: int |
|---|
| + getValor(): int |
| + recoger(): void |
| + destruir(): void |

### `Bala`
*extends `ObjetoGrafico` implements `Movible`*
| |
|---|
| - danio: int |
|---|
| + Mover(): void |
| + impactar(): void |

### `Ladrillo`
*extends `Bloque`*
| |
|---|
| - animNormal: Animacion |
| - animBreaking: Animacion |
| - animRegen: Animacion |
| - tileSize: int |
| - irrompible: boolean |
| - estado: Estado |
|---|
| + Ladrillo(x: int, y: int, tileSize: int, irrompible: boolean) |
| - cargarAnimaciones(): void |
| + iniciarBreaking(): void |
| + iniciarRegen(): void |
| + getEstado(): Estado |
| + isRoto(): boolean |
| + isIrrompible(): boolean |
| + actualizar(): void |
| + display(g: Graphics): void |

#### `<<enum>> Ladrillo.Estado`
| |
|---|
| NORMAL |
| BREAKING |
| ROTO |
| REGENERATING |

### `Agujero`
*extends `Bloque`*
| |
|---|
| - TIEMPO_CIERRE: int = 240 |
| - contador: int |
| - abierto: boolean |
| - ladrilloAsociado: Ladrillo |
|---|
| + Agujero(x: int, y: int) |
| + Agujero(x: int, y: int, asociado: Ladrillo) |
| + getLadrilloAsociado(): Ladrillo |
| + display(g: Graphics): void |
| + actualizar(): void |
| + abrir(): void |
| + cerrar(): void |
| + isAbierto(): boolean |
| + getTiempoRestante(): int |
| + getProgreso(): float |

### `Escalera`
*extends `Bloque`*
| |
|---|
| - animacion: Animacion |
| - tileSize: int |
|---|
| + Escalera(tileX: int, tileY: int, tileSize: int) |
| - cargarAnimacion(): void |
| + display(g: Graphics): void |

### `Barra`
*extends `Bloque`*
| |
|---|
| - spriteBarra: Sprite |
| - tileSize: int |
|---|
| + Barra(tileX: int, tileY: int, tileSize: int) |
| - cargarSprite(): void |
| + deslizar(): void |
| + colgar(): void |
| + display(g: Graphics): void |

### `Moneda`
*extends `Bloque`*
| |
|---|
| - recolectada: boolean |
| - animacion: Animacion |
| - tileSize: int |
|---|
| + Moneda(tileX: int, tileY: int, tileSize: int) |
| - cargarAnimacion(): void |
| + recolectar(): void |
| + isRecolectada(): boolean |
| + recoger(): void |
| + actualizar(): void |
| + display(g: Graphics): void |

### `Puerta`
*extends `ObjetoGrafico`*
| |
|---|
| - spritePuerta: Sprite |
| - tileSize: int |
| - visible: boolean |
|---|
| + Puerta(tileX: int, tileY: int, tileSize: int) |
| + mostrar(): void |
| + ocultar(): void |
| + isVisible(): boolean |
| + display(g: Graphics): void |

### `ParticulaLadrillo`
*extends `ObjetoGrafico`*
| |
|---|
| - animacion: Animacion |
| - tileSize: int |
| - activo: boolean |
|---|
| + ParticulaLadrillo(x: int, y: int, tileSize: int) |
| - cargarAnimacion(): void |
| + actualizar(): void |
| + isActivo(): boolean |
| + display(g: Graphics): void |

---

## Lode Runner

### `JuegoLodeRunner`
*extends `VideoJuego`*
| |
|---|
| - input: InputManager |
| - menu: MenuLodeRunner |
| - collisionManager: CollisionManager |
| - heroe: Recolector |
| - guardias: List<Guardia> |
| - niveles: List<Nivel> |
| - nivelIdx: int |
| - puntosJ1: int |
| - rankingRegistrado: boolean |
| - tiempoNivel: int |
| - fondo: BufferedImage |
| - fxPlayer: FXPlayer |
| - musicaIniciada: boolean |
|---|
| + JuegoLodeRunner() |
| + iniciar(): void |
| + pause(): void |
| + renderizar(g: Graphics): void |
| # crearPartida(): void |
| - cargarNivelActual(): void |
| # actualizarLogicaJuego(): void |
| + getGanador(): String |
| + getPerdedor(): String |
| - soltarOroGuardia(g: Guardia, nivel: Nivel): void |
| + setNombreJugador(nombre: String): void |
| # reiniciar(): void |

### `Recolector`
*extends `Personaje`*
| |
|---|
| + VELOCIDAD: int = 2 |
| + VIDAS_INICIALES: int = 3 |
| - oroRecolectado: int |
| - nivelOroTotal: int |
| - enEscalera: boolean |
| - enBarra: boolean |
| - cayendo: boolean |
| - cavoEsteFrame: boolean |
| - enAire: boolean |
| - tileX: int |
| - tileY: int |
| - tileSize: int |
| - input: InputManager |
| - nivel: Nivel |
| - animParado: Animacion |
| - animCaminando: Animacion |
| - animEscalera: Animacion |
| - animBarra: Animacion |
|---|
| + Recolector(tileX: int, tileY: int, tileSize: int) |
| - cargarAnimaciones(): void |
| + setInputManager(input: InputManager): void |
| + setNivel(nivel: Nivel): void |
| + mover(): void |
| - aplicarGravedad(): void |
| - tieneSoporte(tx: int, ty: int): boolean |
| - detectarPlataforma(): void |
| + actualizar(): void |
| + moverIzquierda(): void |
| + moverDerecha(): void |
| + moverArriba(): void |
| + moverAbajo(): void |
| + cavarIzquierda(): void |
| + cavarDerecha(): void |
| + recogerOro(): void |
| + reiniciarPosicion(): void |
| + perderVida(): void |
| + getOroRecolectado(): int |
| + setNivelOroTotal(total: int): void |
| + getNivelOroTotal(): int |
| + nivelCompleto(): boolean |
| + cavoEsteFrame(): boolean |
| + isEnEscalera(): boolean |
| + setEnEscalera(v: boolean): void |
| + isEnBarra(): boolean |
| + setEnBarra(v: boolean): void |
| + isCayendo(): boolean |
| + setCayendo(v: boolean): void |
| + isEnAire(): boolean |
| + setEnAire(v: boolean): void |
| + getTileX(): int |
| + getTileY(): int |
| + display(g: Graphics): void |

### `Guardia`
*extends `Personaje`*
| |
|---|
| + VELOCIDAD: double = 1.7 |
| - ia: IA_Guardia |
| - heroe: Recolector |
| - nivel: Nivel |
| - enAgujero: boolean |
| - enEscalera: boolean |
| - enBarra: boolean |
| - cayendo: boolean |
| - monedaCargada: Moneda |
| - enAire: boolean |
| - tileSize: int |
| - spawnTileX: int |
| - spawnTileY: int |
| - animCaminando: Animacion |
| - animAtrapado: Animacion |
|---|
| + Guardia(tileX: int, tileY: int, tileSize: int) |
| - cargarAnimaciones(): void |
| + actualizar(): void |
| + moverIzquierda(): void |
| + moverDerecha(): void |
| + moverArriba(): void |
| + moverAbajo(): void |
| + reaparecer(): void |
| + getTileX(): int |
| + getTileY(): int |
| + getIA(): IA_Guardia |
| + isEnEscalera(): boolean |
| + setHeroe(heroe: Recolector): void |
| + setNivel(nivel: Nivel): void |
| + mover(): void |
| - tieneSoporte(tx: int, ty: int): boolean |
| - aplicarGravedad(): void |
| - detectarPlataforma(): void |
| + setEnEscalera(v: boolean): void |
| + isEnBarra(): boolean |
| + setEnBarra(v: boolean): void |
| + isCayendo(): boolean |
| + setCayendo(v: boolean): void |
| + isCargandoOro(): boolean |
| + getMonedaCargada(): Moneda |
| + setMonedaCargada(m: Moneda): void |
| + isEnAire(): boolean |
| + setEnAire(v: boolean): void |
| + enAgujero(): boolean |
| + enAgujero(v: boolean): void |
| + display(g: Graphics): void |

### `IA_Guardia`
| |
|---|
| - rand: Random |
| - direccionPreferida: int |
| - contadorCambio: int |
| - CAMBIO_CADENCIA: int = 60 |
| - estado: Comportamiento |
| - tiempoAtrapado: int |
| - TIEMPO_MAX_ATRAPADO: int = 120 |
|---|
| + IA_Guardia() |
| + calcularMovimiento(guardiaX: int, guardiaY: int, heroeX: int, heroeY: int, puedeIzq: boolean, puedeDer: boolean, puedeSubir: boolean, puedeBajar: boolean, enEscalera: boolean, enBarra: boolean): int |
| - calcularPersecucion(...): int |
| - calcularVagar(...): int |
| + getEstado(): Comportamiento |
| + setEstado(e: Comportamiento): void |
| + atrapar(): void |
| + reaparecer(): void |
| + cambiarAPersecucion(): void |
| + isPersiguiendo(): boolean |

#### `<<enum>> IA_Guardia.Comportamiento`
| |
|---|
| PERSEGUIR |
| VAGAR |
| ATRAPADO |
| REAPARECER |

### `Nivel`
| |
|---|
| + VACIO: char = ' ' |
| + LADRILLO: char = '=' |
| + LADRILLO_IRROMPIBLE: char = '#' |
| + ESCALERA: char = 'H' |
| + BARRA: char = '-' |
| + MONEDA: char = '$' |
| + AGUJERO: char = 'A' |
| + GUARDIA: char = 'E' |
| + RECOLECTOR: char = 'P' |
| + PUERTA: char = 'X' |
| # Numero: int |
| # Mapa: String[] |
| # mapa: char[][] |
| # tile_size: int = 40 |
| # Entidades: List<ObjetoGrafico> |
| # ladrillos: List<Ladrillo> |
| # ladrillosIrrompibles: List<Ladrillo> |
| # escaleras: List<Escalera> |
| # barras: List<Barra> |
| # monedas: List<Moneda> |
| # agujeros: List<Agujero> |
| # particulas: List<ParticulaLadrillo> |
| # escapeLadderX: int |
| # escapeLadderY: int |
| # escapeLadderActiva: boolean |
| # spawnRecolectorX: int |
| # spawnRecolectorY: int |
| # spawnGuardias: List<int[]> |
| # totalOro: int |
| + tiempoLimite: int = 120 |
|---|
| + Nivel() |
| + Nivel(numero: int, mapaData: String[]) |
| + getNumero(): int |
| + getTile_size(): int |
| + getTile(x: int, y: int): char |
| + esSolido(x: int, y: int): boolean |
| + esLadrilloCavable(x: int, y: int): boolean |
| + esEscalera(x: int, y: int): boolean |
| + esBarra(x: int, y: int): boolean |
| + esMoneda(x: int, y: int): boolean |
| + esVacio(x: int, y: int): boolean |
| + setTile(x: int, y: int, c: char): void |
| + agregarEntidad(entidad: ObjetoGrafico): void |
| + cargar(): void |
| + actualizar(): void |
| + renderizar(): void |
| + activarEscape(): void |
| + finalizarNivel(): void |
| + getAnchoMapa(): int |
| + getAltoMapa(): int |
| + getAnchoPixels(): int |
| + getAltoPixels(): int |
| + getMonedaEn(x: int, y: int): Moneda |
| + cavarEn(tileX: int, tileY: int): boolean |

### `Nivel1`
*extends `Nivel`*
| |
|---|
| + Nivel1() |

### `Nivel2`
*extends `Nivel`*
| |
|---|
| + Nivel2() |

### `Nivel3`
*extends `Nivel`*
| |
|---|
| + Nivel3() |

### `MenuLodeRunner`
*extends `MenuPrincipal`*
| |
|---|
| - seleccion: int |
| - rankingManager: RankingManager |
| - topRanking: List<RankingEntry> |
|---|
| + MenuLodeRunner(input: InputManager, mouse: Object) |
| + getSeleccion(): int |
| + setSeleccion(s: int): void |
| + recargarRanking(): void |
| + actualizar(): void |
| + dibujar(g: Graphics): void |

---

## Pong

### `JuegoPong`
*extends `VideoJuego`*
| |
|---|
| - OpJuego: boolean |
| - input: InputManager |
| - menu: MenuPong |
| - paleta1: Paleta |
| - paleta2: Paleta |
| - pelota: PelotaPong |
| - collisionManager: CollisionManager |
| - puntosJ1: int |
| - puntosJ2: int |
| - PUNTOS_MAX: int = 11 |
| - fondo: BufferedImage |
| - modoIA: boolean |
| - ia: IA_Pong |
| - rankingRegistrado: boolean |
|---|
| + setOpJuego(opJuego: boolean): void |
| + setPuntosMax(puntos: int): void |
| + iniciar(): void |
| + pause(): void |
| + renderizar(g: Graphics): void |
| # crearPartida(): void |
| + getGanador(): String |
| + getPerdedor(): String |
| # actualizarLogicaJuego(): void |
| - registrarRankingFinal(): void |

### `MenuPong`
*extends `MenuPrincipal`*
| |
|---|
| - seleccion: int |
| - rankingManager: RankingManager |
| - topRanking: List<RankingEntry> |
|---|
| + MenuPong(input: InputManager, mouse: Object) |
| + getSeleccion(): int |
| + setSeleccion(s: int): void |
| + actualizar(): void |
| + dibujar(g: Graphics): void |

### `Paleta`
*extends `ObjetoGrafico` implements `Movible`*
| |
|---|
| - velocidad: int = 3 |
| - input: InputManager |
| - idJugador: int |
|---|
| + Paleta(input: InputManager, idJugador: int) |
| + Mover(): void |
| + ResetearPOS(): void |
| + dibujar(g: Graphics): void |

### `PelotaPong`
*extends `ObjetoGrafico`*
| |
|---|
| - dx: double |
| - dy: double |
| - velocidadBase: double |
|---|
| + PelotaPong() |
| + mover(): void |
| + rebotarParedes(): void |
| + rebotarPaleta(p: Paleta): void |
| + salioIzquierda(): boolean |
| + salioDerecha(): boolean |
| + aumentarVelocidad(): void |
| + display(g: Graphics): void |
| + reiniciar(): void |
| + reiniciar(haciaLaDerecha: boolean): void |

### `IA_Pong`
| |
|---|
| - dificultad: int |
| - pelota: PelotaPong |
| - paleta: Paleta |
| - margenError: int |
| - velocidad: double |
| - puntosRonda: int |
| - margenMinimo: int |
|---|
| + IA_Pong(pelota: PelotaPong, paleta: Paleta, dificultad: int) |
| + setDificultad(dificultad: int): void |
| + incrementarDificultad(): void |
| + calcularMovimiento(): void |

---

## Space Invaders

### `JuegoSpaceInvaders`
*extends `VideoJuego`*
| |
|---|
| - input: InputManager |
| - menu: MenuSpaceInvaders |
| - navecita: NaveJugador |
| - flotaE: HashMap<String, Enemigo> |
| - direccionflotaX: int = 2 |
| - velocidadflotaY: int = 15 |
| - ultimoDisparo: long |
|---|
| + iniciar(): void |
| # actualizarLogicaJuego(): void |
| + pause(): void |
| + renderizar(g: Graphics): void |
| # crearPartida(): void |
| + getGanador(): String |
| + getPerdedor(): String |

### `MenuSpaceInvaders`
*extends `MenuPrincipal`*
| |
|---|
| - juego: JuegoSpaceInvaders |
| - seleccion: int |
| - delay: int = 150 |
| - ultimoTiempo: long |
|---|
| + MenuSpaceInvaders(input: InputManager, juego: JuegoSpaceInvaders) |
| + getSeleccion(): int |
| + setSeleccion(s: int): void |
| + setVisible(v: boolean): void |
| + actualizar(): void |
| + dibujar(g: Graphics): void |

### `NaveJugador`
*extends `Personaje` implements `Armado`*
| |
|---|
| + NaveJugador(X: int, Y: int) |
| + Disparar(): Laser |

### `Enemigo`
*extends `Personaje`*
| |
|---|
| # puntosxKill: int |
|---|
| + Enemigo(X: int, Y: int) |
| + getPuntos(): int |

### `EnemigoA`
*extends `Enemigo` implements `Armado`*
| |
|---|
| + EnemigoA(X: int, Y: int) |
| + Disparar(): Bala |

### `EnemigoB`
*extends `Enemigo`*
| |
|---|
| + EnemigoB(X: int, Y: int) |

### `EnemigoC`
*extends `Enemigo`*
| |
|---|
| + EnemigoC(X: int, Y: int) |

### `Laser`
*extends `Bala`*
| |
|---|
| - velocidad: int |
|---|
| + Laser(X: int, Y: int, velocidad: int, string: String) |
| + Mover(): void |
| + actualizar(): void |

### `Escudo`
*extends `ObjetoGrafico`*
| |
|---|
| - resistencia: int |
|---|
| + recibirDanio(): void |

### `NaveNodriza`
*extends `ObjetoGrafico`*
| |
|---|
| - random: Random |
|---|
| + NaveNodriza(sprite: String) |
| + puntaje(): int |
| + cruzarPantalla(): void |

### `NivelSpaceInvaders`
*extends `Nivel`*
| |
|---|
| + generarOleadas(): void |

---

## UI

### `<<abstract>> MenuPrincipal`
*extends `JFrame`*
| |
|---|
| # input: InputManager |
| # tituloLbl: JLabel |
| # ctrlJ1: JLabel |
| # ctrlJ2: JLabel |
| # tarjetaCentral: JPanel |
| # configMode: boolean |
| # configSelected: int |
| # configActionIndex: int |
| # lastConfigKeyTime: long |
|---|
| + MenuPrincipal(tituloVentana: String, tituloJuego: String, c1: Color, ctrJ1: String, ctrJ2: String) |
| + actualizar(): void |
| + renderizar(): void |
| + isConfigMode(): boolean |
| + setConfigMode(v: boolean): void |
| + actualizarConfig(): void |
| - guardarConfiguracion(): void |
| - reiniciarDefaults(): void |
| - obtenerDefault(accion: String): int |
| + dibujarConfig(g: Graphics): void |

---

## Graphics

### `Sprite`
| |
|---|
| - imagen: BufferedImage |
|---|
| + Sprite(imagen: BufferedImage) |
| + getImagen(): BufferedImage |
| + dibujar(g: Graphics, x: int, y: int): void |
| + dibujar(g: Graphics, x: int, y: int, ancho: int, alto: int): void |
| + getWidth(): int |
| + getHeight(): int |

### `SpriteSheet`
| |
|---|
| - sprites: List<Sprite> |
|---|
| + SpriteSheet(hoja: BufferedImage, frameWidth: int, frameHeight: int) |
| + SpriteSheet(imagenes: List<BufferedImage>) |
| + obtenerSprite(index: int): Sprite |
| + size(): int |

### `Animacion`
| |
|---|
| - frames: List<Sprite> |
| - frameActual: int |
| - tiempoPorFrame: long |
| - ultimoTiempo: long |
| - repitiendo: boolean |
|---|
| + Animacion(frames: List<Sprite>, tiempoPorFrameMs: long) |
| + actualizar(): void |
| + obtenerFrame(): Sprite |
| + reiniciar(): void |
| + setRepitiendo(repitiendo: boolean): void |
| + termino(): boolean |
| + dibujar(g: Graphics, x: int, y: int): void |
| + dibujar(g: Graphics, x: int, y: int, ancho: int, alto: int): void |

### `Renderizador`
| |
|---|
| + renderizarEntidad(entidad: ObjetoGrafico): void |
| + limpiarPantalla(): void |

---

## Input

### `InputManager`
| |
|---|
| - lastEnterTime: long |
| - lastMenuUpTime: long |
| - lastMenuDownTime: long |
| - COOLDOWN_MS: long = 120 |
|---|
| + isKeyPressed(keyCode: int): boolean |
| + isEnterPressed(): boolean |
| + isWPressed(): boolean |
| + isSPressed(): boolean |
| + isUpPressed(): boolean |
| + isDownPressed(): boolean |
| + isLeftPressed(): boolean |
| + isRightPressed(): boolean |
| + isPPressed(): boolean |
| + isCtrlPressed(): boolean |
| + isBackslashPressed(): boolean |
| + isEscapePressed(): boolean |
| + isQPressed(): boolean |
| + isMPressed(): boolean |
| + isDigPressed(): boolean |
| + isSpacePressed(): boolean |
| + isMenuUpPressed(): boolean |
| + isMenuDownPressed(): boolean |

### `MouseManager`
| |
|---|
| + getX(): int |
| + getY(): int |
| + isLeftPressed(): boolean |
| + isRightPressed(): boolean |
| + isMiddlePressed(): boolean |

---

## Collision

### `CollisionManager`
| |
|---|
| + verificarColisiones(entidades: List<ObjetoGrafico>): void |
| + colisiona(a: ObjetoGrafico, b: ObjetoGrafico): boolean |

### `Hitbox`
| |
|---|
| - x: int |
| - y: int |
| - width: int |
| - height: int |
|---|
| + Hitbox(x: int, y: int, width: int, height: int) |
| + setPosicion(x: int, y: int): void |
| + setDimension(width: int, height: int): void |
| + getBounds(): Rectangle |
| + getX(): int |
| + getY(): int |
| + getWidth(): int |
| + getHeight(): int |

---

## Audio

### `FXPlayer`
| |
|---|
| - sonido: Map<String, Clip> |
| - volumen: int |
|---|
| + FXPlayer() |
| + cargarSonido(nombre: String, ruta: String): void |
| + cargarSonidoRecurso(nombre: String, resourcePath: String): void |
| + reproducir(nombre: String): void |
| + detener(nombre: String): void |
| + mutear(): void |
| + repetir(nombre: String): void |

---

## Config

### `ConfigManager`
| |
|---|
| - ARCHIVO: String = "configuracion.txt" |
| - volumen: float |
| - fullscreen: boolean |
| - soundEnabled: boolean |
| - soundFxEnabled: boolean |
| - musicEnabled: boolean |
| - keyBindings: Map<String, Integer> |
|---|
| + ConfigManager() |
| + cargar(): void |
| + guardar(): void |
| - aplicarKeyBindings(): void |
| + getVolumen(): float |
| + setVolumen(v: float): void |
| + isFullscreen(): boolean |
| + setFullscreen(v: boolean): void |
| + isSoundEnabled(): boolean |
| + setSoundEnabled(v: boolean): void |
| + isSoundFxEnabled(): boolean |
| + setSoundFxEnabled(v: boolean): void |
| + isMusicEnabled(): boolean |
| + setMusicEnabled(v: boolean): void |
| + leer(): void |
| + escribir(): void |

### `KeyBindings`
| |
|---|
| - bindings: Map<String, Integer> |
|---|
| + get(action: String): int |
| + set(action: String, keyCode: int): void |
| + keyName(keyCode: int): String |
| + getActionNames(): String[] |

---

## Ranking

### `RankingManager`
| |
|---|
| - DEFAULT_DB_PATH: String = "app-data/ranking.db" |
| - dbUrl: String |
| - puntajes: List<Integer> |
|---|
| + RankingManager() |
| + RankingManager(dbPath: String) |
| + agregarPuntaje(): void |
| + agregarPuntaje(jugador: String, juego: String, nivel: int, puntaje: int): void |
| + guardarRanking(): void |
| + cargarRanking(): void |
| + getPuntajes(): List<Integer> |
| + cargarPuntajesTop(juego: String, limite: int): List<Integer> |
| + cargarDetalleTop(juego: String, limite: int): List<RankingEntry> |
| - inicializarTabla(): void |
| - crearCarpetaSiNoExiste(dbPath: String): void |

### `<<record>> RankingEntry`
| |
|---|
| + jugador: String |
| + juego: String |
| + Nivel: int |
| + puntaje: int |
| + fecha: String |

---

## Utils

### `CargadorRecursos`
| |
|---|
| + cargarImagen(ruta: String): BufferedImage |
| + cargarSonido(): void |

### `RandomUtils`
| |
|---|
| + randomInt(): int |
| + randomFloat(): float |

### `MathUtils`
| |
|---|
| + clamp(): int |
| + distancia(): double |

---

## Launcher

### `App`
| |
|---|
| + main(args: String[]): void |

### `Launcher`
*extends `JFrame`*
*(contiene clases internas `RoundBtn`, `TabLbl`, `GameEntry`)*
| |
|---|
| - recursos: CargadorRecursos |
| - games: List<GameEntry> |
| - player: String |
| - focused: int |
| - carouselPanel: JPanel |
| - tabGamesPanel: JPanel |
| - storePanel: JPanel |
| - detailBar: JPanel |
| - bodyPanel: JPanel |
| - carouselScroll: JScrollPane |
| - bodyLayout: CardLayout |
| - sessionBtn: JLabel |
| - tabGames: TabLbl |
| - tabStore: TabLbl |
| - detailNameLbl: JLabel |
| - activeTab: String |
|---|
| + Launcher() |
| - buildTopBar(): JPanel |
| - buildBody(): JPanel |
| + rebuildCarousel(): void |
| - buildCard(game: GameEntry, sel: boolean, idx: int): JPanel |
| - scrollToFocused(): void |
| - buildDetailBar(): JPanel |
| + updateDetailBar(): void |
| - buildBottomBar(): JPanel |
| - openSession(): void |
| - openAddGame(): void |
| - removeSelected(): void |
| - openGameConfig(): void |
| - openGlobalSettings(): void |
| + launchGame(): void |
| - crearJuego(nombre: String): VideoJuego |

---

## Tabla de Relaciones

| Tabla | Relación con tabla | Tipo de relación | Cardinalidad |
|---|---|---|---|
| GameLoop | com.entropyinteractive.Game | Herencia | — |
| GameLoop | JuegoLoopable | Asociación | 1 |
| GameLoop | VideoJuego | Asociación | 1 |
| EstadoJuego | VideoJuego | Asociación | 1 |
| Jugador | VideoJuego | Agregación | 0..* |
| Camara | VideoJuego | Composición | 1 |
| Camara | ObjetoGrafico | Asociación | 1 |
| Camara | Nivel | Asociación | 1 |
| VideoJuego | JuegoLoopable | Implementación | — |
| VideoJuego | EstadoJuego | Asociación | 1 |
| VideoJuego | Nivel | Asociación | 0..1 |
| VideoJuego | ObjetoGrafico | Composición | 0..* |
| VideoJuego | Jugador | Agregación | 0..* |
| VideoJuego | InputManager | Asociación | 1 |
| VideoJuego | Camara | Composición | 1 |
| VideoJuego | ConfigManager | Composición | 1 |
| VideoJuego | RankingManager | Composición | 1 |
| VideoJuego | JuegoLodeRunner | Herencia | — |
| VideoJuego | JuegoPong | Herencia | — |
| VideoJuego | JuegoSpaceInvaders | Herencia | — |
| ObjetoGrafico | BufferedImage | Asociación | 0..1 |
| ObjetoGrafico | Dimension | Composición | 1 |
| ObjetoGrafico | Point | Composición | 1 |
| ObjetoGrafico | Hitbox | Composición | 0..1 |
| ObjetoGrafico | Personaje | Herencia | — |
| ObjetoGrafico | Bloque | Herencia | — |
| ObjetoGrafico | Bala | Herencia | — |
| ObjetoGrafico | ParticulaLadrillo | Herencia | — |
| ObjetoGrafico | Escudo | Herencia | — |
| ObjetoGrafico | NaveNodriza | Herencia | — |
| ObjetoGrafico | PelotaPong | Herencia | — |
| ObjetoGrafico | Paleta | Herencia | — |
| ObjetoGrafico | Puerta | Herencia | — |
| Personaje | ObjetoGrafico | Herencia | — |
| Personaje | Recolector | Herencia | — |
| Personaje | Guardia | Herencia | — |
| Personaje | Enemigo | Herencia | — |
| Personaje | NaveJugador | Herencia | — |
| Bloque | ObjetoGrafico | Herencia | — |
| Bloque | Ladrillo | Herencia | — |
| Bloque | Agujero | Herencia | — |
| Bloque | Escalera | Herencia | — |
| Bloque | Barra | Herencia | — |
| Bloque | Moneda | Herencia | — |
| Bala | ObjetoGrafico | Herencia | — |
| Bala | Movible | Implementación | — |
| Bala | Laser | Herencia | — |
| Ladrillo | Bloque | Herencia | — |
| Ladrillo | Animacion (normal) | Composición | 1 |
| Ladrillo | Animacion (breaking) | Composición | 1 |
| Ladrillo | Animacion (regen) | Composición | 1 |
| Ladrillo | Ladrillo.Estado | Asociación | 1 |
| Ladrillo | Nivel | Composición | 0..* |
| Ladrillo | Agujero | Asociación | 0..1 |
| Agujero | Bloque | Herencia | — |
| Agujero | Ladrillo | Asociación | 0..1 |
| Agujero | Nivel | Composición | 0..* |
| Agujero | Guardia | Asociación | 0..* |
| Escalera | Bloque | Herencia | — |
| Escalera | Animacion | Composición | 0..1 |
| Escalera | Nivel | Composición | 0..* |
| Barra | Bloque | Herencia | — |
| Barra | Sprite | Composición | 0..1 |
| Barra | Nivel | Composición | 0..* |
| Moneda | Bloque | Herencia | — |
| Moneda | Animacion | Composición | 0..1 |
| Moneda | Nivel | Composición | 0..* |
| Moneda | Recolector | Asociación | 0..* |
| Moneda | Guardia | Asociación | 0..1 |
| Puerta | ObjetoGrafico | Herencia | — |
| Puerta | Sprite | Composición | 0..1 |
| ParticulaLadrillo | ObjetoGrafico | Herencia | — |
| ParticulaLadrillo | Animacion | Composición | 1 |
| ParticulaLadrillo | Nivel | Composición | 0..* |
| Recolector | Personaje | Herencia | — |
| Recolector | InputManager | Asociación | 1 |
| Recolector | Nivel | Asociación | 1 |
| Recolector | JuegoLodeRunner | Composición | 1 |
| Recolector | Moneda | Asociación | 0..* |
| Recolector | Guardia | Asociación | 0..* |
| Recolector | Animacion (parado) | Composición | 1 |
| Recolector | Animacion (caminando) | Composición | 1 |
| Recolector | Animacion (escalera) | Composición | 1 |
| Recolector | Animacion (barra) | Composición | 1 |
| Guardia | Personaje | Herencia | — |
| Guardia | IA_Guardia | Composición | 1 |
| Guardia | Recolector | Asociación | 1 |
| Guardia | Nivel | Asociación | 1 |
| Guardia | Moneda | Asociación | 0..1 |
| Guardia | Agujero | Asociación | 0..* |
| Guardia | JuegoLodeRunner | Composición | 0..* |
| Guardia | Animacion (caminando) | Composición | 1 |
| Guardia | Animacion (atrapado) | Composición | 1 |
| IA_Guardia | IA_Guardia.Comportamiento | Asociación | 1 |
| IA_Guardia | Guardia | Composición | 1 |
| IA_Guardia | Random | Composición | 1 |
| Nivel | Ladrillo | Composición | 0..* |
| Nivel | Ladrillo (irrompibles) | Composición | 0..* |
| Nivel | Escalera | Composición | 0..* |
| Nivel | Barra | Composición | 0..* |
| Nivel | Moneda | Composición | 0..* |
| Nivel | Agujero | Composición | 0..* |
| Nivel | ParticulaLadrillo | Composición | 0..* |
| Nivel | ObjetoGrafico | Composición | 0..* |
| Nivel | VideoJuego | Asociación | 0..1 |
| Nivel | JuegoLodeRunner | Asociación | 1 |
| Nivel | Nivel1 | Herencia | — |
| Nivel | Nivel2 | Herencia | — |
| Nivel | Nivel3 | Herencia | — |
| Nivel1 | Nivel | Herencia | — |
| Nivel2 | Nivel | Herencia | — |
| Nivel3 | Nivel | Herencia | — |
| JuegoLodeRunner | VideoJuego | Herencia | — |
| JuegoLodeRunner | InputManager | Composición | 1 |
| JuegoLodeRunner | MenuLodeRunner | Composición | 1 |
| JuegoLodeRunner | CollisionManager | Composición | 1 |
| JuegoLodeRunner | Recolector | Composición | 1 |
| JuegoLodeRunner | Guardia | Composición | 0..* |
| JuegoLodeRunner | Nivel | Composición | 1..* |
| JuegoLodeRunner | FXPlayer | Composición | 1 |
| JuegoLodeRunner | CargadorRecursos | Dependencia | — |
| JuegoLodeRunner | Agujero | Asociación | 0..* |
| JuegoLodeRunner | ParticulaLadrillo | Asociación | 0..* |
| JuegoPong | VideoJuego | Herencia | — |
| JuegoPong | InputManager | Composición | 1 |
| JuegoPong | MenuPong | Composición | 1 |
| JuegoPong | Paleta | Composición | 2 |
| JuegoPong | PelotaPong | Composición | 1 |
| JuegoPong | CollisionManager | Composición | 1 |
| JuegoPong | IA_Pong | Composición | 0..1 |
| MenuPrincipal | JFrame | Herencia | — |
| MenuPrincipal | InputManager | Asociación | 1 |
| MenuPrincipal | MenuLodeRunner | Herencia | — |
| MenuPrincipal | MenuPong | Herencia | — |
| MenuPrincipal | MenuSpaceInvaders | Herencia | — |
| MenuLodeRunner | MenuPrincipal | Herencia | — |
| MenuLodeRunner | RankingManager | Asociación | 1 |
| MenuLodeRunner | RankingEntry | Asociación | 0..* |
| MenuPong | MenuPrincipal | Herencia | — |
| MenuPong | RankingManager | Asociación | 1 |
| MenuPong | RankingEntry | Asociación | 0..* |
| MenuSpaceInvaders | MenuPrincipal | Herencia | — |
| MenuSpaceInvaders | JuegoSpaceInvaders | Asociación | 1 |
| Paleta | ObjetoGrafico | Herencia | — |
| Paleta | Movible | Implementación | — |
| Paleta | InputManager | Asociación | 1 |
| PelotaPong | ObjetoGrafico | Herencia | — |
| IA_Pong | PelotaPong | Asociación | 1 |
| IA_Pong | Paleta | Asociación | 1 |
| JuegoSpaceInvaders | VideoJuego | Herencia | — |
| JuegoSpaceInvaders | InputManager | Composición | 1 |
| JuegoSpaceInvaders | MenuSpaceInvaders | Composición | 1 |
| JuegoSpaceInvaders | NaveJugador | Composición | 1 |
| JuegoSpaceInvaders | Enemigo | Composición | 0..* |
| NaveJugador | Personaje | Herencia | — |
| NaveJugador | Armado | Implementación | — |
| Enemigo | Personaje | Herencia | — |
| EnemigoA | Enemigo | Herencia | — |
| EnemigoA | Armado | Implementación | — |
| EnemigoB | Enemigo | Herencia | — |
| EnemigoC | Enemigo | Herencia | — |
| Laser | Bala | Herencia | — |
| Laser | Movible | Implementación | (heredado de Bala) |
| Escudo | ObjetoGrafico | Herencia | — |
| NaveNodriza | ObjetoGrafico | Herencia | — |
| NivelSpaceInvaders | Nivel | Herencia | — |
| Sprite | BufferedImage | Asociación | 1 |
| Sprite | Animacion | Composición | 0..* |
| SpriteSheet | BufferedImage | Asociación | 1 |
| SpriteSheet | Sprite | Composición | 0..* |
| Animacion | Sprite | Composición | 0..* |
| Animacion | Ladrillo | Composición | 3 |
| Animacion | Escalera | Composición | 0..1 |
| Animacion | Moneda | Composición | 0..1 |
| Animacion | Guardia | Composición | 2 |
| Animacion | ParticulaLadrillo | Composición | 1 |
| Animacion | Recolector | Composición | 4 |
| InputManager | VideoJuego | Asociación | 1 |
| InputManager | JuegoLodeRunner | Asociación | 1 |
| InputManager | Recolector | Asociación | 1 |
| InputManager | MenuPrincipal | Asociación | 1 |
| CollisionManager | ObjetoGrafico | Dependencia | — |
| Hitbox | ObjetoGrafico | Composición | 0..1 |
| Hitbox | Rectangle | Asociación | 1 |
| ConfigManager | VideoJuego | Composición | 1 |
| ConfigManager | KeyBindings | Asociación | 1 |
| KeyBindings | Map<String, Integer> | Composición | 1 |
| FXPlayer | Map<String, Clip> | Composición | 0..* |
| FXPlayer | JuegoLodeRunner | Composición | 1 |
| RankingManager | VideoJuego | Composición | 1 |
| RankingManager | JuegoLodeRunner | Asociación | 1 |
| RankingManager | MenuLodeRunner | Asociación | 1 |
| RankingManager | RankingEntry | Asociación | 0..* |
| CargadorRecursos | BufferedImage | Dependencia | — |
| CargadorRecursos | JuegoLodeRunner | Dependencia | — |
| Launcher | JFrame | Herencia | — |
| Launcher | GameEntry | Composición | 0..* |
| Launcher | JuegoLodeRunner | Asociación | 1 |
| Launcher | JuegoPong | Asociación | 1 |
| Launcher | JuegoSpaceInvaders | Asociación | 1 |
