package py_poo.audio;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class FXPlayer {
    private Map<String, Clip> sonido;
    private static int volumen;

    public FXPlayer() {
        this.sonido = new HashMap<>();
    }

    public void cargarSonido(String nombre, String ruta) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(ruta));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            sonido.put(nombre, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarSonidoRecurso(String nombre, String resourcePath) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Recurso de sonido no encontrado: " + resourcePath);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            sonido.put(nombre, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reproducir(String nombre) {
        Clip clip = sonido.get(nombre);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void detener(String nombre) {
        Clip clip = sonido.get(nombre);
        if (clip != null) {
            clip.stop();
        }
    }

    public void mutear() {
    }

    public void repetir(String nombre) {
        Clip clip = sonido.get(nombre);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
