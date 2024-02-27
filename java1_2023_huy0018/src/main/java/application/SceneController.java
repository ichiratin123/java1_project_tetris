package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private Stage stage;


    public void switchToGameView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
        Pane root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.resizableProperty().set(false);
        stage.setTitle("Tetris");
        stage.show();
        GameController controller = loader.getController();
        controller.startGame();
    }

    public void switchToScoreBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ScoreView.fxml"));
        Pane root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        ScoreBoard sb = loader.getController();
        sb.setBoard();
        stage.setScene(scene);
        stage.resizableProperty().set(false);
        stage.setTitle("Tetris");
        stage.show();

    }

}
