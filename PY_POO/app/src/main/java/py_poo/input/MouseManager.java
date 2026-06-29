package py_poo.input;

import com.entropyinteractive.Mouse;
import py_poo.engine.VideoJuego;

public class MouseManager {

    public int getX() {
        Mouse m = VideoJuego.getRaton();
        return m != null ? m.getX() : 0;
    }

    public int getY() {
        Mouse m = VideoJuego.getRaton();
        return m != null ? m.getY() : 0;
    }

    public boolean isLeftPressed() {
        Mouse m = VideoJuego.getRaton();
        return m != null && m.isLeftButtonPressed();
    }

    public boolean isRightPressed() {
        Mouse m = VideoJuego.getRaton();
        return m != null && m.isRightButtonPressed();
    }

    public boolean isMiddlePressed() {
        Mouse m = VideoJuego.getRaton();
        return m != null && m.isMiddleButtonPressed();
    }
}
