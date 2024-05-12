package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneController {
    private Stage stage;
    
    @FXML
    private Button switchBT;
    @FXML
    private Button ScoreBoard;
    @FXML
    private Label gameScore;
    
    public void initialize() {
        updateTexts(SettingLanguages.getCurrentLocale());
    }


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
    private void updateTexts(Locale locale) {
        ResourceBundle texts = ResourceBundle.getBundle("texts", locale);
        switchBT.setText(texts.getString("switchBT"));
        ScoreBoard.setText(texts.getString("ScoreBoard"));
    }

    @FXML
	public void switchToCz(ActionEvent event) {
		SettingLanguages.setCurrentLocale(new Locale("cz", "CZ"));
		updateTexts(SettingLanguages.getCurrentLocale());
	}
    
    @FXML
	public void switchToEn(ActionEvent event) {
    	SettingLanguages.setCurrentLocale(new Locale("en", "US"));
		updateTexts(SettingLanguages.getCurrentLocale());
	}
    
    @FXML
	public void switchToVi(ActionEvent event) {
    	SettingLanguages.setCurrentLocale(new Locale("vi", "VN"));
		updateTexts(SettingLanguages.getCurrentLocale());
	}
    
   

}
