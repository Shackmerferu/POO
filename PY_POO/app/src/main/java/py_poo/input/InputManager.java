package py_poo.input;

import com.entropyinteractive.Keyboard;
import py_poo.config.KeyBindings;
import py_poo.core.GameLoop;

public class InputManager {
    private long lastEnterTime;
    private long lastMenuUpTime;
    private long lastMenuDownTime;
    private static final long COOLDOWN_MS = 120;

    public boolean isKeyPressed(int keyCode) {
        Keyboard kb = GameLoop.getTeclado();
        return kb != null && kb.isKeyPressed(keyCode);
    }

    public boolean isEnterPressed() {
        long now = System.currentTimeMillis();
        if (isKeyPressed(10) && (now - lastEnterTime >= COOLDOWN_MS)) {
            lastEnterTime = now;
            return true;
        }
        return false;
    }

    public boolean isWPressed() {
        return isKeyPressed(KeyBindings.get("J1_UP"));
    }

    public boolean isSPressed() {
        return isKeyPressed(KeyBindings.get("J1_DOWN"));
    }

    public boolean isUpPressed() {
        return isKeyPressed(KeyBindings.get("UP"));
    }

    public boolean isDownPressed() {
        return isKeyPressed(KeyBindings.get("DOWN"));
    }

    public boolean isLeftPressed() {
        return isKeyPressed(KeyBindings.get("LEFT"));
    }

    public boolean isRightPressed() {
        return isKeyPressed(KeyBindings.get("RIGHT"));
    }

    public boolean isPPressed() {
        return isKeyPressed(KeyBindings.get("PAUSE"));
    }

    public boolean isCtrlPressed() {
        return isKeyPressed(KeyBindings.get("SOUND"));
    }

    public boolean isBackslashPressed() {
        return isKeyPressed(KeyBindings.get("FULLSCREEN"));
    }

    public boolean isEscapePressed() {
        return isKeyPressed(KeyBindings.get("RESET"));
    }

    public boolean isSpacePressed() {
        return isKeyPressed(32);
    }

    public boolean isMenuUpPressed() {
        long now = System.currentTimeMillis();
        if (isKeyPressed(KeyBindings.get("UP")) && (now - lastMenuUpTime >= COOLDOWN_MS)) {
            lastMenuUpTime = now;
            return true;
        }
        return false;
    }

    public boolean isMenuDownPressed() {
        long now = System.currentTimeMillis();
        if (isKeyPressed(KeyBindings.get("DOWN")) && (now - lastMenuDownTime >= COOLDOWN_MS)) {
            lastMenuDownTime = now;
            return true;
        }
        return false;
    }    public boolean isLeftPressed() {
        return isKeyPressed(37);
    }

    public boolean isRightPressed() {
        return isKeyPressed(39);
    }

    public boolean isSpacePressed() {
        return isKeyPressed(32);
    }


}
