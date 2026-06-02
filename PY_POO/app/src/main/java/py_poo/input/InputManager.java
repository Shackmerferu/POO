package py_poo.input;

import com.entropyinteractive.Keyboard;
import py_poo.core.GameLoop;

public class InputManager {

    public boolean isKeyPressed(int keyCode) {
        Keyboard kb = GameLoop.getTeclado();
        return kb != null && kb.isKeyPressed(keyCode);
    }

    public boolean isEnterPressed() {
        return isKeyPressed(10);
    }

    public boolean isUpPressed() {
        return isKeyPressed(38);
    }

    public boolean isDownPressed() {
        return isKeyPressed(40);
    }
    public boolean isWPressed() {
        return isKeyPressed(87);
    }
    public boolean isSPressed() {
        return isKeyPressed(83);
    }

    public boolean isLeftPressed() {
        return isKeyPressed(37);
    }

    public boolean isRightPressed() {
        return isKeyPressed(39);
    }

    public boolean isSpacePressed() {
        return isKeyPressed(32);
    }


}
