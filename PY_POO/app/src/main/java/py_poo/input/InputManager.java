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
}
