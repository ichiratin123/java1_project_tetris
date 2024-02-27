package application;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class GameController {
    @FXML
    private Canvas canvas;

    private Tetris tetris;

    @FXML
    private Label levelLB;
    @FXML
    private Label scoreLB;
    private int tetrisScore;
    private Stage stage;
    @FXML
    private ImageView nextMino;

    public void startGame(){
        this.tetris = new Tetris(canvas.getWidth(), canvas.getHeight());
        this.tetris.setGameListener(new GameListenerImpl());
        setupKeyListeners();
        DrawingThread animationTimer = new DrawingThread(canvas, tetris);
        animationTimer.start();
    }

    private void setupKeyListeners() {
        canvas.getScene().setOnKeyPressed(event -> {
            handleKeyPressed(event);
        });
    }

    private void handleKeyPressed(KeyEvent event) {
        if (tetris != null) {
            switch (event.getCode()) {
                case LEFT:
                    tetris.moveMinoLeft();
                    break;
                case RIGHT:
                    tetris.moveMinoRight();
                    break;
                case DOWN:
                    tetris.moveMinoDown();
                    break;
                case R:
                    tetris.rotateMino();
                    break;
            }
        }
    }



    private class GameListenerImpl implements GameListener {
        private Stage stage;
        @Override
        public void stateChanged(int level, int score) {
            GameController.this.levelLB.setText(""+level);
            GameController.this.scoreLB.setText(""+score);
            GameController.this.tetrisScore = score;
            if (tetris.updateImage()) {
                updateNextMinoImage();
            }else{
                updateNextMinoImage(); // the first prediction
            }
            if(tetris.endGame()){
                gameOver();
            }
        }

        @Override
        public void gameOver() {
            PauseTransition pause = new PauseTransition(Duration.millis(1500));
            pause.setOnFinished(event -> swichSceneInputScore());
            pause.play();
        }

        private void swichSceneInputScore() {
            try {
                FXMLLoader loader =  new FXMLLoader(getClass().getResource("/inputScore.fxml"));
                Pane root = loader.load();
                ScoreInputController sc =  loader.getController();
                sc.setScore(tetrisScore);
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void updateNextMinoImage() {
            Mino.MinoType nextType = tetris.getNextMinoType();
            if (nextType != null) {
                Image imagePath = getImagePathForMinoType(nextType);
                nextMino.setImage(imagePath);
            }
        }


        private Image getImagePathForMinoType(Mino.MinoType minoType) {
            switch (minoType) {
                case I: return new Image("/I.png");
                case J: return new Image("/J.png");
                case L: return new Image("/L.png");
                case O: return new Image("/O.png");
                case S: return new Image("/S.png");
                case T: return new Image("/T.png");
                case Z: return new Image("/Z.png");
                default: return new Image("/title.png");
            }
        }

    }

    public void switchBackStartView(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TetrisStartView.fxml"));
        Pane root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.resizableProperty().set(false);
        stage.setTitle("Tetris");
        stage.show();
    }
}