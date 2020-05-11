package de.proglabor.aufgabe6;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {
	/*
	 * Variables
	 */
	private double speed = 250.0;
	private boolean isPaused = false;
	private HighScoreList scoresList;
	private String uriString = new File("D:\\eclipse-workspace\\Prog-Aufgabe6\\tetris.mp3").toURI().toString();
	private MediaPlayer player = new MediaPlayer(new Media(uriString));
	private Tetris t;
	private Stage primaryStage;
	private DrawGame gameDraw;
	private Scene scene;
	private Group root;
	private Timeline fall = new Timeline();
	private Task askName;
	private Scanner input;
	private Boolean isInMainScene = false;
	private Boolean isHighScoreListDisplay = false;
	private Boolean isResetButtonClicked = false;
	private Boolean isSaveButtonClicked = false;
	private Boolean isAskScore = false;
	private Image iconOff = new Image(getClass().getResourceAsStream("/muteUn.jpg"), 100, 100, false, false);
	private Image iconOn = new Image(getClass().getResourceAsStream("/mute.jpg"), 100, 100, false, false);
	private ImageView viewOn = new ImageView(iconOn);
	private ImageView viewOff = new ImageView(iconOff);

	/*
	 * Constructor
	 */
	public Controller(Stage primaryStage, Tetris t, Scene scene, Group root) {
		this.t = t;
		this.primaryStage = primaryStage;
		this.scene = scene;
		this.root = root;

	}

	/*
	 * main frame FXML
	 */
	@FXML
	private AnchorPane anchorMain;
	@FXML
	private AnchorPane anchorWelcome;
	@FXML
	private GridPane grid;
	@FXML
	private ImageView gif;
	@FXML
	private StackPane stack;
	/*
	 * Button Play
	 */
	@FXML
	private Button play;

	@FXML
	protected void playGame() {
		// set elements visible state
		anchorWelcome.setVisible(false);
		anchorMain.setVisible(true);
		isInMainScene = true;
		img.setVisible(true);
		stack.setVisible(false);
		gif.setVisible(false);
		grid.setVisible(true);
		// set 2 mute button same state
		mute1.setVisible(false);
		if (mute1.isSelected()) {
			mute.setSelected(true);
			mute.setGraphic(viewOff);
			player.setMute(true);
		} else {
			mute.setSelected(false);
			mute.setGraphic(viewOn);
			player.setMute(false);
		}
		isHighScoreListDisplay = false;
		primaryStage.sizeToScene();
		img.toBack();
		gc = img.getGraphicsContext2D();
		try {
			scoresList = new HighScoreList("textHighScore.csv");
			askName = new Task(scoresList, fall);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameDraw = new DrawGame(t, scoresList, gc);
		t.addObserver(gameDraw);

		// Time line
		fall = new Timeline(new KeyFrame(Duration.millis(speed), e -> {
			if (t.gameFinished()) {
				input.close();
				System.out.println("Game Over!");
				Text over = new Text("Game Over");
				over.setFill(Color.DARKRED);
				over.setStyle("-fx-stroke: black;" + "-fx-stroke-width: 2;");
				over.setFont(Font.font("Arial", FontWeight.BOLD, 140));
				over.setX(65);
				over.setY(760);
				over.setOpacity(0.4);
				root.getChildren().add(over);
				fall.stop();
				if (!isResetButtonClicked && !isHighScoreListDisplay && !isSaveButtonClicked && !isAskScore) {
					if (scoresList.getHighScoreListe().size() == 10) {
						if ((t.getScore() > scoresList.getHighScoreListe().get(0).getPoints())) {
							askName.askNameWindow(t);
							isAskScore = true;
						} else {
							askName.displayHighScoreWindow();
							isHighScoreListDisplay = true;
							isAskScore = true;
						}
					} else {
						askName.askNameWindow(t);
					}
				}
			} else {
				t.falling();
			}

		}));
		fall.setCycleCount(Timeline.INDEFINITE);
		fall.play();
		// key reader
		scene.setOnKeyPressed(new KeyReader(t, isPaused, fall, root));
		// check if out side clicked then pause game
		primaryStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (!isNowFocused && isInMainScene && !t.gameFinished()) {
				fall.stop();
				player.pause();
				System.out.println("Outside clicked\nPaused");
			} else if (!isHighScoreListDisplay && !isSaveButtonClicked && !isAskScore) {
				if (!isPaused)
					fall.play();
				if (!isResetButtonClicked)
					player.play();
				System.out.println("Inside clicked");
			}
		});
	}

	/*
	 * Exit Button
	 */
	@FXML
	private Button exit;

	@FXML
	protected void handleExitButtonAction(ActionEvent event) {
		System.exit(0);
	}

	/*
	 * Reset Button
	 */
	@FXML
	private Button reset;

	@FXML
	protected void handleResetButtonAction(ActionEvent event) {
		isResetButtonClicked = true;
		t.setGameOver(true);
		fall.stop();
		player.stop();
		primaryStage.close();
		Platform.runLater(() -> new GUI().start(new Stage()));
	}

	/*
	 * Save Button
	 */
	@FXML
	private Button save;

	@FXML
	protected void handleSaveButtonAction(ActionEvent event) {
		isSaveButtonClicked = true;
		t.saveGame();
		player.stop();
		fall.stop();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/iconStage.PNG"));
		alert.setTitle("Successful Save!");
		alert.setHeaderText("Successful saved your game!");
		String s = t.getScore() + " is your score!\nContinue to play?";
		alert.setContentText(s);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			stage.close();
			fall.play();
		}
		if (result.get() == ButtonType.CANCEL) {
			primaryStage.close();
			Platform.runLater(() -> new GUI().start(new Stage()));
		}
	}

	/*
	 * Resume Button
	 */
	@FXML
	private Button resume;

	@FXML
	protected void handleResumeButtonAction(ActionEvent event) {
		try {
			t.loadGame();
		} catch (SpaceAlreadyOccupiedException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Load Error!");
			alert.setHeaderText(null);
			alert.setContentText("Cannot Load GameData!!! GameData is corrupted!!!");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				primaryStage.close();
				Platform.runLater(() -> new GUI().start(new Stage()));
			}
		}
		playGame();
	}

	/*
	 * HighScore Button
	 */
	@FXML
	private Button high;

	@FXML
	protected void handleHighButtonAction(ActionEvent event) {
		fall.pause();
		askName.displayHighScoreWindow();
		isHighScoreListDisplay = true;
	}

	/*
	 * Mute Button
	 */
	@FXML
	private ToggleButton mute;
	@FXML
	private ToggleButton mute1;

	@FXML
	protected void loadTB(ActionEvent event) {
		ToggleButton btnMute = (ToggleButton) event.getSource();
		if (btnMute.isSelected()) {
			btnMute.setGraphic(viewOff);
			player.setMute(true);
		} else {
			btnMute.setGraphic(viewOn);
			player.setMute(false);
		}
	}

	/*
	 * Game play draw in this canvas
	 */
	@FXML
	public Canvas img = new Canvas(0, 0);
	public GraphicsContext gc = img.getGraphicsContext2D();

	/*
	 * INIT when game start
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		anchorMain.setVisible(false);
		input = new Scanner(System.in);
		isInMainScene = false;
		img.setVisible(false);
		grid.setVisible(false);
		gif.toBack();
		primaryStage.getIcons().add(new Image(getClass().getResource("/iconStage.PNG").toExternalForm()));
		player.setVolume(0.1);
		player.setOnEndOfMedia(new Runnable() {
			public void run() {
				player.seek(Duration.ZERO);
			}
		});
		player.play();
	}
}
