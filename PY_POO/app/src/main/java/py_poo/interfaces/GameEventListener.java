package py_poo.interfaces;

public interface GameEventListener {
    void onHeroDeath();
    void onGameOver();
    void onCoinCollected();
    void onDig();
}
