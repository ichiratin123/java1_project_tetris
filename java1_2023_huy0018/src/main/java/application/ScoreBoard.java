package application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ScoreBoard {
    private List<Score> list = new LinkedList<>();
    @FXML
    private ListView<Score> scoreView;

    private Stage stage;

    public void setBoard() throws FileNotFoundException, IOException {
        File file = new File("scores.csv");
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (!created) {
                    throw new IOException("Could not create new file 'scores.csv'");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while (null != (line = br.readLine())) {
                String[] tokens = line.split(";");
                list.add(new Score(Integer.parseInt(tokens[1]), tokens[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scoreView.setItems(FXCollections.observableList(list));
        sort();
    }

    public void sort() {
        Collections.sort(list, new ScoreComparator());
        scoreView.setItems(FXCollections.observableList(list));
    }

    public void switchBackStartView(ActionEvent event) throws IOException {
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
