package de.proglabor.aufgabe6;

import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyReader implements EventHandler<KeyEvent> {

	private Tetris t;
	private boolean isPaused;
	private int countPause = 0;
	private Timeline fall;
	private Group root;
	private Text pause = new Text("Paused");
	private double speed;

	public KeyReader(Tetris t, boolean isPaused, Timeline fall, Group root) {
		this.t = t;
		this.isPaused = isPaused;
		this.fall = fall;
		this.root = root;

		pause.setFill(Color.DARKRED);
		pause.setStyle("-fx-stroke: black;" + "-fx-stroke-width: 5;");
		pause.setFont(Font.font("Arial", FontWeight.BOLD, 158));
		pause.setX(170);
		pause.setY(760);
		pause.setOpacity(0.6);
	}

	@Override
	public void handle(KeyEvent e) {
		KeyCode kc = e.getCode();
		if (kc.equals(KeyCode.D) && isPaused == false && !t.gameFinished()) {
			t.movingRight();
		} else if (kc.equals(KeyCode.S) && isPaused == false && !t.gameFinished()) {
			t.falling();
		} else if (kc.equals(KeyCode.A) && isPaused == false && !t.gameFinished()) {
			t.movingLeft();
		} else if (kc.equals(KeyCode.W) && isPaused == false && !t.gameFinished()) {
			t.rotating();
		} else if (kc.equals(KeyCode.X) && isPaused == false && !t.gameFinished()) {
			t.fallingAll();
		} else if (kc.equals(KeyCode.MINUS) && !t.gameFinished()) {
			speed = fall.getRate();
			speed = speed / 2;
			fall.setRate(speed);
		} else if (kc.equals(KeyCode.PLUS) && !t.gameFinished()) {
			speed = fall.getRate();
			speed = speed * 2;
			fall.setRate(speed);
		} else if (kc.equals(KeyCode.P) && !t.gameFinished()) {
			countPause++;
			if (countPause == 1) {
				isPaused = true;
				fall.pause();
				root.getChildren().add(pause);
//				System.out.println(t.toString());
//				System.out.println(t.getScore());
			}
			if (countPause == 2) {
				// after cancel the task, have to INIT again task to run schedule again
				root.getChildren().remove(pause);
				isPaused = false;
				fall.play();
				countPause = 0;
			}
		}
	}
}