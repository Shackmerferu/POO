package py_poo.input;

import com.entropyinteractive.Mouse;
import py_poo.core.GameLoop;

public class MouseManager {

    public int getX() {
        Mouse m = GameLoop.getRaton();
        return m != null ? m.getX() : 0;
    }

    public int getY() {
        Mouse m = GameLoop.getRaton();
        return m != null ? m.getY() : 0;
    }

    public boolean isLeftPressed() {
        Mouse m = GameLoop.getRaton();
        return m != null && m.isLeftButtonPressed();
    }

    public boolean isRightPressed() {
        Mouse m = GameLoop.getRaton();
        return m != null && m.isRightButtonPressed();
    }

    public boolean isMiddlePressed() {
        Mouse m = GameLoop.getRaton();
        return m != null && m.isMiddleButtonPressed();
    }
}
