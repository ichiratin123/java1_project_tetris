package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class Tetris implements DrawableSimulable{
    public static final int SIZE = 50; // block size
    private double width;
    private double height;
    public static final int XMAX = SIZE * 10; //width
    public static final int YMAX = SIZE * 20; //height
    private Color[][] board = new Color[YMAX / SIZE][XMAX / SIZE];
    private Mino mino;
    private MinoRandom randomMino;
    private int score;
    private int lastYPosition = -1;
    private GameListener gameListener = new EmptyGameListener();
    private Level level;
    private boolean changeImage = false;


    public Tetris(double width, double height) {
        this.width = width;
        this.height = height;
        this.level = new Level(10,0.5, 0.1);
        this.randomMino = new MinoRandom();
        mino = randomMino.createRandomMino(level.getCurrentDropSpeed());
        mino.setPosition(XMAX / 2 - mino.getSize(), 0);
        this.score = 0;
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillRect(0, 0, width, height);
        mino.draw(gc);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    gc.setFill(board[row][col]);
                    gc.fillRect(col * SIZE, row * SIZE, SIZE, SIZE);
                }
                gc.setStroke(Color.BLACK);
                gc.strokeRect(col * SIZE, row * SIZE, SIZE, SIZE);
            }
        }

        if(endGame()){
            Font font = new Font("Comic Sans MS", 50);
            gc.setFont(font);
            gc.setFill(Color.BLACK);
            gc.fillText("GAME OVER", (XMAX/2) - 150, YMAX/2);
        }
    }

    @Override
    public void simulate(double deltaT) {
        mino.simulate(deltaT);
        if(!endGame()){
            countScoreInRow();
            if (!checkYBlock()) {
                lockMino();
                setNextMino();
                mino.setPosition(XMAX / 2 - mino.getSize(), 0);
                removeFullLines();

            }
            gameListener.stateChanged(level.getCurrentLevel(), this.score);
        }else{
            mino.setSimlater(false);

        }
    }


    private void countScoreInRow(){
        int currentYPosition = mino.getY();
        if (currentYPosition / SIZE > lastYPosition / SIZE) {
            score += (currentYPosition / SIZE) - (lastYPosition / SIZE);
        }
        lastYPosition = currentYPosition;
    }

    private boolean checkYBlock() {
        Color[][] grid = mino.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null) {
                    int newX = mino.getX() / mino.getSize() + col;
                    int newY = mino.getY() / mino.getSize() + row + 1;
                    if (newY >= YMAX / mino.getSize()) {
                        return false;
                    }
                    if (newY < board.length && newX < board[newY].length && board[newY][newX] != null) {// 18, 10
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkXBlock(int move) {
        Color[][] grid = mino.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null) {
                    int newX = mino.getX() / mino.getSize() + col + move;
                    int newY = mino.getY() / mino.getSize() + row;
                    if (newX < 0 || newX >= XMAX / mino.getSize() || newY >= YMAX / mino.getSize() || (newY >= 0 && board[newY][newX] != null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void lockMino() {
        Color[][] grid = mino.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null) {
                    int boardX = mino.getX() / mino.getSize() + col;
                    int boardY = mino.getY() / mino.getSize() + row;
                    if (boardY < board.length && boardX < board[boardY].length) {
                        board[boardY][boardX] = grid[row][col];
                    }
                }
            }
        }
    }

    public void moveMinoLeft() {
        if (!checkXBlock(-1)) {
            mino.moveLeft();
        }
    }

    public void moveMinoRight() {
        if (!checkXBlock(1)) {
            mino.moveRight();
        }
    }

    public void moveMinoDown() {
        mino.moveDown();
    }

    public void rotateMino() {
        mino.rotate();
    }

    public void removeFullLines() {
        List<Integer> linesToRemove = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            boolean lineIsFull = true;
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == null) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                linesToRemove.add(row);
            }
        }

        for (int row : linesToRemove) {
            for (int r = row; r > 0; r--) {
                for (int col = 0; col < board[r].length; col++) {
                    board[r][col] = board[r - 1][col];
                }
            }
            for (int col = 0; col < board[0].length; col++) {
                board[0][col] = null;
            }
        }
        score += linesToRemove.size() * 10;
        for (int i = 0; i < linesToRemove.size(); i++) {
            level.lineCleared();
        }
    }

    public boolean endGame(){
        for (int col = 0; col < board[0].length; col++) {
            if (board[1][col] != null) {
                return true;
            }
        }
        return false;
    }

    public void setGameListener(GameListener gameListenerImpl) {
        this.gameListener = gameListenerImpl;

    }

    public Mino.MinoType getNextMinoType() {
        return randomMino.getNextMinoType();
    }

    private void setNextMino() {
        changeImage = true;
        if (level.checkLevelIncrease()) {
            mino = randomMino.createRandomMino(level.getCurrentDropSpeed());
        } else {
            mino = randomMino.createRandomMino(mino.getFallSpeed());
        }
    }

    public boolean updateImage() {
        if (changeImage) {
            changeImage = false;
            return true;
        }
        return false;
    }

}

