package py_poo.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private List<Sprite> sprites;

    public SpriteSheet(BufferedImage hoja, int frameWidth, int frameHeight) {
        sprites = new ArrayList<>();
        if (hoja == null) return;
        int cols = hoja.getWidth() / frameWidth;
        int rows = hoja.getHeight() / frameHeight;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                BufferedImage frame = hoja.getSubimage(x * frameWidth, y * frameHeight, frameWidth, frameHeight);
                sprites.add(new Sprite(frame));
            }
        }
    }

    public SpriteSheet(List<BufferedImage> imagenes) {
        sprites = new ArrayList<>();
        for (BufferedImage img : imagenes) {
            sprites.add(new Sprite(img));
        }
    }

    public Sprite obtenerSprite(int index) {
        if (index >= 0 && index < sprites.size()) {
            return sprites.get(index);
        }
        return null;
    }

    public int size() {
        return sprites.size();
    }
}
