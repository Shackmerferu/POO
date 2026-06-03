package py_poo.config;

import java.util.HashMap;
import java.util.Map;

public class KeyBindings {
    private static Map<String, Integer> bindings = new HashMap<>();

    static {
        bindings.put("J1_UP", 87);
        bindings.put("J1_DOWN", 83);
        bindings.put("UP", 38);
        bindings.put("DOWN", 40);
        bindings.put("LEFT", 37);
        bindings.put("RIGHT", 39);
        bindings.put("PAUSE", 80);
        bindings.put("SOUND", 17);
        bindings.put("FULLSCREEN", 48);
        bindings.put("RESET", 27);
    }

    public static int get(String action) {
        return bindings.getOrDefault(action, -1);
    }

    public static void set(String action, int keyCode) {
        bindings.put(action, keyCode);
    }

    public static String keyName(int keyCode) {
        switch (keyCode) {
            case 10: return "Enter";
            case 17: return "Ctrl";
            case 27: return "Esc";
            case 32: return "Space";
            case 37: return "Left";
            case 38: return "Up";
            case 39: return "Right";
            case 40: return "Down";
            case 80: return "P";
            case 83: return "S";
            case 87: return "W";
            case 48: return "0";
            default:
                if (keyCode >= 65 && keyCode <= 90) return String.valueOf((char) keyCode);
                if (keyCode >= 48 && keyCode <= 57) return String.valueOf((char) keyCode);
                return "Key(" + keyCode + ")";
        }
    }

    public static String[] getActionNames() {
        return new String[]{
            "J1_UP", "J1_DOWN",
            "UP", "DOWN", "LEFT", "RIGHT",
            "PAUSE", "SOUND", "FULLSCREEN", "RESET"
        };
    }
}
