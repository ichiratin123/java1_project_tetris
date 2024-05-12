package application;

import java.sql.SQLException;

import org.h2.tools.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class App extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/TetrisStartView.fxml"));
			Pane root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.resizableProperty().set(false);
			primaryStage.setTitle("Tetris");
			primaryStage.show();
			primaryStage.setOnCloseRequest(this::exitProgram);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Server server = Server.createWebServer();
			log.info("To inspect DB go to URL: " + server.getURL());
			server.start();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		launch(args);
	}

	private void exitProgram(WindowEvent evt) {
		System.exit(0);
	}

}