# PY_POO — Proyecto Integrador de Programación Orientada a Objetos

## Descripción

Laplicación de escritorio que reúne tres juegos clásicos desarrollados en **Java 21** con **Swing**:

- **Pong** — Ping-pong clásico contra IA.
- **Space Invaders** — Disparo a oleadas de enemigos con escudos y nave nodriza.
- **Lode Runner** — Plataformas con recolector de oro, guardias con IA, ladrillos cavables, escaleras y barras.

El proyecto utiliza **Gradle** como sistema de compilación, **SQLite** para el ranking de puntuaciones, y un motor de juego personalizado (`bucleJuego.jar`).

## Requisitos del sistema

- **Java Development Kit (JDK) 21** o superior.
- Gradle 9.2+ (opcional, se incluye el wrapper `gradlew.bat`).

Verificar la versión de Java instalada:

```bash
java -version
```

Debe mostrar `openjdk 21` o superior.

## Estructura del proyecto

```
PY_POO/
├── app/
│   ├── build.gradle          # Configuración de compilación
│   ├── src/
│   │   ├── main/java/py_poo/ # Código fuente
│   │   └── main/resources/   # Imágenes, sonidos, librerías
│   └── build/                # Archivos compilados
├── gradlew.bat               # Wrapper de Gradle (Windows)
├── gradlew                   # Wrapper de Gradle (Linux/Mac)
└── settings.gradle           # Configuración del proyecto
```

## Cómo compilar

### Compilar todo el proyecto:

```bash
gradlew.bat build
```

### Generar el FAT JAR (ejecutable autónomo con todas las dependencias):

```bash
gradlew.bat app:fatJar
```

El archivo generado se encuentra en:

```
app/build/libs/app-fat.jar
```

### Limpiar compilación anterior:

```bash
gradlew.bat clean
```

## Cómo ejecutar

### Opción 1 — FAT JAR (recomendada)

Un solo archivo que incluye el código y todas las dependencias:

```bash
java -jar app/build/libs/app-fat.jar
```

También se puede hacer doble clic sobre el archivo `app-fat.jar` si el sistema tiene asociados los archivos `.jar` con Java.

### Opción 2 — Desde Gradle

```bash
gradlew.bat app:run
```

### Opción 3 — Distribución completa

Genera un ZIP con el JAR y scripts de inicio:

```bash
gradlew.bat distZip
```

Luego extraer `app/build/distributions/app.zip` y ejecutar:

```
app/bin/app.bat        (Windows)
app/bin/app            (Linux/Mac)
```

## Controles

| Tecla     | Acción                  |
|-----------|-------------------------|
| Flechas / WASD | Movimiento del personaje |
| Espacio   | Cavar / Disparar        |
| P         | Pausa                   |
| ESC       | Volver al menú          |
| Q         | Mutear efectos          |
| M         | Mutear música           |
| 0         | Pantalla completa       |
| Enter     | Seleccionar en menús    |

## Notas

- Las imágenes, fuentes y sonidos deben estar en `app/src/main/resources/` para que el JAR los encuentre.
- La base de datos SQLite se crea automáticamente en `app-data/` al registrar el primer puntaje.
