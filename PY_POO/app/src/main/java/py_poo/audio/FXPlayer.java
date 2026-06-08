package py_poo.audio;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class FXPlayer {

    // VARIABLES DE LA CLASE

    // Usamos un Mapa (HashMap) para guardar los sonidos.
    //  guardamos un nombre clave (ej: "salto")
    // y lo asociamos con su archivo de audio cargado  (Clip).
    private Map<String, Clip> sonido;

    // Variable preparada por si en el futuro queremos hacer un control de volumen general.
    private static int volumen;

    // CONSTRUCTOR
    public FXPlayer() {
        // Inicializamos el mapa vacío cada vez que creamos un reproductor nuevo
        this.sonido = new HashMap<>();
    }

    /*
    Este método  lee el archivo .wav del disco duro UNA SOLA VEZ y lo deja
     listo en la memoria RAM. Así, cuando el jugador choca, el sonido sale instantáneo sin lag.*/
    public void cargarSonidoRecurso(String nombre, String ruta) {
        try {
            // Buscamos el archivo dentro de la carpeta compilada 'resources' de nuestro proyecto Gradle
            java.net.URL url = getClass().getClassLoader().getResource(ruta);

            // Si te equivocas en el nombre del archivo o la ruta, te avisa por consola en vez de crashear
            if (url == null) {
                System.err.println("No se pudo encontrar el recurso de sonido: " + ruta);
                return;
            }

            // Convertimos el archivo encontrado en un flujo de audio entendible para Java
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);

            // Un "Clip" es un espacio de memoria donde Java guarda un sonido corto para reproducirlo rápido
            Clip clip = AudioSystem.getClip();

            // Abrimos el audio dentro de ese Clip
            clip.open(audio);

            // Guardamos el Clip listo en nuestro diccionario, asociado al "apodo" que elegimos (ej: "fondo")
            sonido.put(nombre, clip);

        } catch (Exception e) {
            // Si ocurre algún error (ej: el archivo no es .wav), lo imprime en la consola
            e.printStackTrace();
        }
    }

    // --- REPRODUCIR EFECTOS DE SONIDO
    // Buscambia el sonido por su nombre y lo reproduce una sola vez.
    public void reproducir(String nombre) {
        Clip clip = sonido.get(nombre); // Busca el audio en el diccionario 

    if (clip == null) {
        System.out.println("--> [AUDIO ERROR] Intentaste reproducir '" + nombre + "' pero NO está cargado en el mapa.");
        return;
    }

    System.out.println("--> [AUDIO OK] Tocando el sonido: '" + nombre + "' | ¿Está corriendo?: " + clip.isRunning());

    try {
        clip.setFramePosition(0);
        clip.start(); 
    } catch (Exception e) {
        System.out.println("--> [AUDIO CRASH] Falló el .start() de '" + nombre + "'. Motivo:");
        e.printStackTrace();
    }
        if (clip != null) {
            //  setFramePosition(0) rebobina el audio al segundo cero.
            //  el sonido arranca de nuevo al instante en lugar de esperar a terminar.
            clip.setFramePosition(0);
            clip.start(); // Le da Play
        }
    }

    //  DETENER AUDIO
    // Útil para cortar la música de fondo al salir al menú.
    public void detener(String nombre) {
        Clip clip = sonido.get(nombre);

        if (clip != null) {
            clip.stop(); // Le da Stop
        }
    }

    // --- CONTROL DE VOLUMEN ---
    // Cambia el volumen del sonido usando decibeles (dB).
    public void setVolumen(String nombre, String nivel) {
        Clip clip = sonido.get(nombre);

        if (clip != null) {
            // Obtenemos el "control maestro" de ganancia (volumen) de este audio específico
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Usamos un switch para traducir palabras amigables a valores técnicos (decibeles)
            switch (nivel.toLowerCase()) {
                case "bajo":
                    gainControl.setValue(-30.0f); // -30 dB reduce  el volumen
                    break;
                case "medio":
                    gainControl.setValue(-10.0f); // Reduce moderadamente el volumen
                    break;
                case "fuerte":
                default:
                    gainControl.setValue(0.0f);   // 0.0 dB es el volumen original del archivo .wav sin alteraciones
                    break;
            }
        }
    }


    public void mutear() {}

    // REPRODUCCIÓN(Bucle)
    // Especial para la banda sonora (SoundTrack). Cuando termina, vuelve a empezar sola.
    public void repetir(String nombre) {
        Clip clip = sonido.get(nombre);

        if (clip != null) {
            // LOOP_CONTINUOUSLY es una constante de Java que le dice al Clip que repita hasta el infinito o hasta que
            //utlicemos la funcion detener
            clip.loop(clip.LOOP_CONTINUOUSLY);
        }
    }
}