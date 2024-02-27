package application;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawingThread extends AnimationTimer {

    private final Canvas canvas;

    private final GraphicsContext gc;

    private final Tetris tetris;

    private long lasttime = -1;

    public DrawingThread(Canvas canvas, Tetris tetris) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.tetris = tetris;
    }

    @Override
    public void handle(long now) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        tetris.draw(gc);
        if (lasttime > 0) {
            tetris.simulate((now - lasttime) / 1e9);
        }
        lasttime = now;
    }


}
