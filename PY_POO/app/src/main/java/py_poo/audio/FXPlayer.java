package py_poo.audio;
import javax.sound.sampled.FloatControl;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class FXPlayer{
    private Map<String, Clip> sonido;
    private static int volumen;

    public FXPlayer(){
        this.sonido = new HashMap<>();
    }
    public void cargarSonido(String nombre, String ruta){
        try{
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url == null) {
                System.err.println("No se pudo encontrar el recurso de sonido: " + ruta);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();
            
            clip.open(audio);

            sonido.put(nombre,clip);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public void reproducir(String nombre){
        Clip clip = sonido.get(nombre);
    
        if(clip != null){
            clip.setFramePosition(0);
            clip.start();
        }
    
    }
    public void detener(String nombre){

        Clip clip = sonido.get(nombre);

        if (clip != null) {
            clip.stop();
        }
    }

    public void setVolumen(String nombre, String nivel) {
        Clip clip = sonido.get(nombre);

        if (clip != null) {
            // Buscamos el control de volumen de ese clip
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Configuramos los 3 niveles
            switch (nivel.toLowerCase()) {
                case "bajo":
                    gainControl.setValue(-20.0f); // Reduce mucho el volumen
                    break;
                case "medio":
                    gainControl.setValue(-10.0f); // Reduce la mitad del volumen
                    break;
                case "fuerte":
                default:
                    gainControl.setValue(0.0f);   // Volumen normal/máximo del archivo
                    break;
            }
        }
    }


    public void mutear(){}
    public void repetir(String nombre) {

        Clip clip = sonido.get(nombre);

        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
