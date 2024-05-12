package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ScoreInputController {
    @FXML
    private TextField tf1;

    @FXML
    private Label scoreController;
    
    @FXML
    private Label ipSave;
    @FXML
    private Label ipYScore;
    @FXML
    private Label ipName;
    @FXML
    private Button SAVE;

    private int score;

    public void setScore(int score) {
    	updateTexts(SettingLanguages.getCurrentLocale());
        this.score = score;
        scoreController.setText(String.valueOf(score));
    }

    @FXML
    private void handleSubmitButtonAction() {
        String name = tf1.getText().trim();
        if (tf1.getText().isEmpty()) {
            showAlert("Information", "Player names cannot be empty!");

        }else{  
        	
        	int id = getLastId() + 1;
            Score newScore = new Score(id, score, name);
            saveScoreToFile(newScore);
            saveNewId(id);
            closeStage();
        }
    }
    
    private int getLastId() {
        int lastId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("idChecker.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                lastId = Integer.parseInt(line.trim());
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastId;
    }
    
    private void saveNewId(int newId) {
        try (PrintWriter out = new PrintWriter(new FileWriter("idChecker.txt"))) {
            out.print(newId);
        } catch (IOException e) {
            e.printStackTrace();
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
            out.println(score.getNum() + ";" + score.getName() + ";" + score.getScore());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStage() {
        Stage stage = (Stage) tf1.getScene().getWindow();
        stage.close();
    }

    
    private void updateTexts(Locale locale) {
        ResourceBundle texts = ResourceBundle.getBundle("texts", locale);
        ipSave.setText(texts.getString("ipSave"));
        ipYScore.setText(texts.getString("ipYScore"));
        ipName.setText(texts.getString("ipName"));
        SAVE.setText(texts.getString("SAVE"));
    }
}
