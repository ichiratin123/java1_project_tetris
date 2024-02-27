package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Mino implements DrawableSimulable{

    public enum MinoType {
        I, J, L, O, S, T, Z
    }

    private Color[][] grid;
    private Color color;
    private MinoType type;
    private int x,y;
    private int size;
    private double fallSpeed;
    private double delay = 0;
    private boolean gameState = true;

    public Mino(MinoType type, int blockSize, double fallSpeed) {
        this.size = blockSize;
        this.fallSpeed = fallSpeed;
        initializeMino(type);
    }

    public void setSimlater(boolean simulater){
        this.gameState = simulater;
    }

    private void initializeMino(MinoType type) {
        switch (type) {
            case I:
                color = Color.CYAN;
                grid = new Color[][]{
                        {color, color, color, color}
                };
                break;
            case J:
                color = Color.ORANGE;
                grid = new Color[][]{
                        {null, null, color},
                        {color, color, color}
                };
                break;
            case L:
                color = Color.BLUE;
                grid = new Color[][]{
                        {color, null, null},
                        {color, color, color}
                };
                break;
            case O:
                color = Color.YELLOW;
                grid = new Color[][]{
                        {color, color},
                        {color, color}
                };
                break;
            case S:
                color = Color.GREEN;
                grid = new Color[][]{
                        {null, color, color},
                        {color, color, null}
                };
                break;
            case T:
                color = Color.PURPLE;
                grid = new Color[][]{
                        {null, color, null},
                        {color, color, color}
                };
                break;
            case Z:
                color = Color.RED;
                grid = new Color[][]{
                        {color, color, null},
                        {null, color, color}
                };
                break;
            default:
                throw new IllegalArgumentException("Invalid MinoType: " + type);
        }
    }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Color[][] getGrid() {
        return grid;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getSize(){
        return size;
    }

    public double getFallSpeed() {
        return 0.5;
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null) {
                    gc.setFill(grid[row][col]);
                    gc.fillRect(x + col * size, y + row * size, size, size);
                }
            }
        }
    }

    @Override
    public void simulate(double deltaT) {
        if(gameState){
            delay += deltaT;
            if (delay >= fallSpeed) {
                y += size;
                delay = 0;
            }
        }else{ // gameover
            return;
        }
    }

    public void moveLeft() {
        if (x - size >= 0) {
            x -= size;
        }
    }

    public void moveRight() {
        if (x + getWidth() + size <= Tetris.XMAX) {
            x += size;
        }
    }

    public void moveDown() {
        if (y + getHeight() + size <= Tetris.YMAX) {
            y += size;
        }
    }

    private int getWidth() {
        return grid[0].length * size; // blocks * 50 , block in row which means length
    }

    private int getHeight() {
        return grid.length * size;
    }

    public void rotate() {
        if (type == MinoType.O) {
            return;
        }
        Color[][] newGrid = new Color[grid[0].length][grid.length];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                newGrid[col][grid.length - 1 - row] = grid[row][col];
            }
        }
        grid = newGrid;
        adjustPositionAfterRotation();
    }

    private void adjustPositionAfterRotation() {
        while (getX() + getWidth() > Tetris.XMAX) {
            x -= size;
        }
    }


}
