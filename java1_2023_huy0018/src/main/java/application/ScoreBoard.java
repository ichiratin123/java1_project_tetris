package application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.LevelResourceApi;
import org.openapitools.client.api.PlayerResourceApi;
import org.openapitools.client.api.ScoreResourceApi;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

@Log4j2
public class ScoreBoard {
	private List<Score> list = new LinkedList<>();
	@FXML
	private ListView<Score> scoreView;

	private Stage stage;

	@FXML
	private Label highScore;

	@FXML
	private Button backBT;

	public void setBoard() throws FileNotFoundException, IOException {
		updateTexts(SettingLanguages.getCurrentLocale());
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

			EntityManagerFactory emf = Persistence.createEntityManagerFactory("java2");
			EntityManager em = emf.createEntityManager();

			PlayerResourceApi playerApi = new PlayerResourceApi();
			playerApi.setCustomBaseUrl("http://localhost:8080");

			ScoreResourceApi scoreApi = new ScoreResourceApi();
			scoreApi.setCustomBaseUrl("http://localhost:8080");
			
			LevelResourceApi levelApi = new LevelResourceApi();
			levelApi.setCustomBaseUrl("http://localhost:8080");
			
			org.openapitools.client.model.Player playerRest = new org.openapitools.client.model.Player();
			org.openapitools.client.model.Score scoreRest = new org.openapitools.client.model.Score();
			org.openapitools.client.model.Level levelRest = new org.openapitools.client.model.Level();
			em.getTransaction().begin();

			while (null != (line = br.readLine())) {
				String[] tokens = line.split(";");
				int num = Integer.parseInt(tokens[0]);
				int score = Integer.parseInt(tokens[2]);
				String name = tokens[1];
				list.add(new Score(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[2]), tokens[1]));

				Player player = checkExistPlayer(em, name);
				
				Score newScore = findScoreByNum(em, num);
				if (newScore == null) {
					newScore = new Score(num, score, name);
					scoreRest.setName(name);
					scoreRest.setScore(score);
					
					Level level = new Level(10, 0.5, 0.1);
					if (player == null) {
						playerRest.setUsername(name);
						try {
							playerRest = playerApi.createPlayer(playerRest);
						} catch (ApiException e) {
							e.printStackTrace();
						}
						
						player = new Player(name);
						scoreRest.setPlayerId(playerRest.getId());
						em.persist(player);
					}else {
						playerRest.setId(player.getId());
					}
					Level newLevel = new Level(10, 0.5, level.getCurrentDropSpeed());
					newScore.setPlayer(player);
					scoreRest.setPlayerId(playerRest.getId());
					try {
						scoreRest = scoreApi.createScore(scoreRest);
					} catch (ApiException e) {
						e.printStackTrace();
					}
					newScore.setPlayer(player);
					newLevel.setPlayer(player);
					
					levelRest.setInitialDropSpeed(0.5);
					levelRest.setLinesPerLevel(10);
					levelRest.setSpeedIncreaseFactor(0.5);
					levelRest.setPlayerId(playerRest.getId());
					try {
						levelRest = levelApi.createLevel(levelRest);
					} catch (ApiException e) {
						e.printStackTrace();
					}
					
					em.persist(newScore);
					em.persist(newLevel);
				} else {
					continue;
				}
			}

			em.getTransaction().commit();
			em.clear();

			List<Score> list = em.createQuery("SELECT s FROM Score s ORDER BY s.score DESC", Score.class)
					.getResultList();
			for (Score sc : list) {
				log.info(sc);
			}
			em.clear();

			List<Score> list2 = em.createQuery("SELECT s FROM Score s ORDER BY s.score DESC", Score.class)
					.setMaxResults(3).getResultList();
			for (Score sc : list2) {
				log.info(sc);
			}

			em.clear();

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

	private Score findScoreByNum(EntityManager em, int num) {
		try {
			return em.createQuery("SELECT s FROM Score s WHERE s.num = :num", Score.class).setParameter("num", num)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	private Player checkExistPlayer(EntityManager em, String name) {
		try {
			return em.createQuery("SELECT p FROM Player p WHERE p.username = :username", Player.class)
					.setParameter("username", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public void switchBackStartView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/TetrisStartView.fxml"));
		Pane root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.resizableProperty().set(false);
		stage.setTitle("Tetris");
		stage.show();
	}

	private void updateTexts(Locale locale) {
		ResourceBundle texts = ResourceBundle.getBundle("texts", locale);
		highScore.setText(texts.getString("highScore"));
		backBT.setText(texts.getString("backBT"));
	}
}
