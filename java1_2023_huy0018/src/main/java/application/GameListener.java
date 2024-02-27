package application;

public interface GameListener {
    void stateChanged(int level, int score);
    void gameOver();
}
