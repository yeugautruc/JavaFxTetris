package de.proglabor.aufgabe6;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Task {
	private HighScoreList scoresList;
	String uriString = new File("D:\\eclipse-workspace\\Prog-Aufgabe3\\tetris.mp3").toURI().toString();
	MediaPlayer player = new MediaPlayer(new Media(uriString));
	private Timeline fall;

	// Constructor
	public Task(HighScoreList scoresList, Timeline fall) {
		this.scoresList = scoresList;
		this.fall = fall;
	}

	public void askNameWindow(Tetris in) {
		Stage stage = new Stage();
		stage.getIcons().add(new Image(getClass().getResource("/iconStage.PNG").toExternalForm()));
		VBox box = new VBox();
		box.setPadding(new Insets(10));
		box.setAlignment(Pos.CENTER);
		Label label = new Label("Please enter your name!");
		TextField textUser = new TextField();
		textUser.setPromptText("enter your name");
		Button btnOk = new Button();
		btnOk.setText("OK");
		btnOk.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Assume success always!
				setLoggedInUser(textUser.getText(), in);
				stage.close(); // return to main window
			}
		});
		box.getChildren().add(label);
		box.getChildren().add(textUser);
		box.getChildren().add(btnOk);
		Scene scene = new Scene(box, 350, 250);
		stage.setTitle("Ask Name");
		stage.setScene(scene);
		stage.show();
	}

	public void setLoggedInUser(String user, Tetris t) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Successful!");
		alert.setHeaderText("Successful added to Top 10 HighScore");
		String s = user + " is your name";
		alert.setContentText(s);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm,dd.MM.yyyy");
			LocalDateTime now = LocalDateTime.now();
			System.out.println(dtf.format(now));
			HighScore newSc = new HighScore(user, dtf.format(now), t.getScore());
			scoresList.add(newSc);
			scoresList.saveScoreIntoFile("textHighScore.csv");
			displayHighScoreWindow();
		}

	}

	// display the high score
	@SuppressWarnings("unchecked")
	public void displayHighScoreWindow() {
		Stage stage = new Stage();
		stage.getIcons().add(new Image(getClass().getResource("/iconStage.PNG").toExternalForm()));
		VBox box = new VBox();
		box.setSpacing(20);
		box.setPadding(new Insets(10, 0, 0, 10));

		// center align content
		box.setAlignment(Pos.CENTER);

		Label label = new Label("High Score List");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 40));
		TableView<HighScore> table = new TableView<HighScore>();
		table.setStyle("-fx-font-size:20;-fx-font-weight: bold");
		table.setEditable(false);
		table.setMinHeight(445);
		ObservableList<HighScore> data = FXCollections.observableArrayList();
		for (HighScore highScore : scoresList.getHighScoreListe()) {
			data.add(highScore);
		}
		TableColumn<HighScore, Long> noCol = new TableColumn<HighScore, Long>("No");
		noCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		noCol.setMinWidth(15);
		noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
		TableColumn<HighScore, Long> scoreCol = new TableColumn<HighScore, Long>("Score");
		scoreCol.setStyle("-fx-alignment: CENTER;");
		scoreCol.setMinWidth(150);
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("points"));
		TableColumn<HighScore, String> nameCol = new TableColumn<HighScore, String>("Name");
		nameCol.setMinWidth(200);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<HighScore, String> timeCol = new TableColumn<HighScore, String>("Time");
		timeCol.setMinWidth(180);
		timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
		table.getColumns().addAll(noCol, scoreCol, nameCol, timeCol);
		table.setItems(data);

		Button btnExit = new Button();
		btnExit.setText("BACK");
		btnExit.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		btnExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
				fall.play();
			}
		});
		box.getChildren().add(label);
		box.getChildren().add(table);
		box.getChildren().add(btnExit);
		Scene scene = new Scene(box, 650, 650);
		stage.setTitle("HighScore");
		stage.setScene(scene);
		stage.show();
	}

}
