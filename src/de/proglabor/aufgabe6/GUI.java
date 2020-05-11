package de.proglabor.aufgabe6;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class GUI extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Tetris t = new Tetris();
			Group root = new Group();
			Scene scene = new Scene(root);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("tetris.fxml"));
			Controller gameController = new Controller(primaryStage, t, scene, root);
			URL stylesheet = getClass().getResource("tetrisStyle.css");
			scene.getStylesheets().add(stylesheet.toExternalForm());
			loader.setController(gameController);
			Parent ui = loader.load();
			root.getChildren().add(ui);
			primaryStage.sizeToScene();
			primaryStage.setTitle("TETRIS");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
