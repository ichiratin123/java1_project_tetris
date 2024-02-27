package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ScoreInputController {
    @FXML
    private TextField tf1;

    @FXML
    private Label scoreController;

    private int score;

    public void setScore(int score) {
        this.score = score;
        scoreController.setText(String.valueOf(score));
    }

    @FXML
    private void handleSubmitButtonAction() {
        String name = tf1.getText().trim();
        if (tf1.getText().isEmpty()) {
            showAlert("Information", "Player names cannot be empty!");

        }else{
            Score newScore = new Score(score, name);
            saveScoreToFile(newScore);
            closeStage();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveScoreToFile(Score score) {
        try (PrintWriter out = new PrintWriter(new FileWriter("scores.csv", true))) {
            out.println(score.getName() + ";" + score.getScore());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStage() {
        Stage stage = (Stage) tf1.getScene().getWindow();
        stage.close();
    }

}
