package de.proglabor.aufgabe6;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DrawGame implements Observer {
	private int mesh;
	private int XSIZE;
	private int YSIZE;
	private Tetris t;
	private HighScoreList scoresList;
	private GraphicsContext gc;

	// Constructor
	public DrawGame(Tetris t, HighScoreList scoresList, GraphicsContext gc) {
		this.mesh = Tetris.mesh;
		this.XSIZE = Tetris.XSIZE;
		this.YSIZE = Tetris.YSIZE;
		this.t = t;
		this.scoresList = scoresList;
		this.gc = gc;
		drawGame();
	}

	//
	public void drawGame() {
		drawFrame(gc, t, scoresList);
		drawCurrentStone(gc, t);
		drawCurrentStoneInside(gc, t);
		drawFalledStones(gc, t);
		drawFalledStonesInside(gc, t);
		System.out.println(t.toStringConsole());
	}

	public void drawFalledStones(GraphicsContext c, Tetris t) {
		for (IStone stone : t.getStones()) {
			c.setFill(Color.DARKRED);
			for (int i = 1; i < stone.getElements().size() + 1; i++) {
				if (stone.getElement(i).getY() >= 0 && stone.getElement(i).getY() < 20) {
					c.fillRect(20 + stone.getElement(i).getX() * mesh, 20 + stone.getElement(i).getY() * mesh, mesh - 1,
							mesh - 1);
				}
			}
		}
	}

	public void drawFalledStonesInside(GraphicsContext c, Tetris t) {
		for (IStone stone : t.getStones()) {
			switchColor(c, stone);
			for (int i = 1; i < stone.getElements().size() + 1; i++) {
				if (stone.getElement(i).getY() >= 0 && stone.getElement(i).getY() < 20) {
					c.fillRect((20 + stone.getElement(i).getX() * mesh) + 2,
							(20 + stone.getElement(i).getY() * mesh) + 2, mesh - 2, mesh - 2);
				}
			}
		}
	}

	public void drawCurrentStone(GraphicsContext c, Tetris t) {
		c.setFill(Color.DARKRED);
		for (int i = 1; i < t.getCurrentStone().getElements().size() + 1; i++) {
			if (t.getCurrentStone().getElement(i).getY() >= 0 && t.getCurrentStone().getElement(i).getY() < 20) {
				c.fillRect(20 + t.getCurrentStone().getElement(i).getX() * mesh,
						20 + t.getCurrentStone().getElement(i).getY() * mesh, mesh - 1, mesh - 1);
			}
		}
	}

	public void drawCurrentStoneInside(GraphicsContext c, Tetris t) {
		switchColor(c, t.getCurrentStone());
		for (int i = 1; i < t.getCurrentStone().getElements().size() + 1; i++) {
			if (t.getCurrentStone().getElement(i).getY() >= 0 && t.getCurrentStone().getElement(i).getY() < 20) {
				c.fillRect(20 + t.getCurrentStone().getElement(i).getX() * mesh + 2,
						20 + t.getCurrentStone().getElement(i).getY() * mesh + 2, mesh - 2, mesh - 2);
			}
		}
	}

	public void drawFrame(GraphicsContext c, Tetris t, HighScoreList scoresList) {

		String textScore = "Score: ";
		Font newFont1 = Font.font("Arial", FontWeight.BOLD, 40);
		c.setFont(newFont1);
		c.clearRect(0, 0, 900, 950);
		c.setFill(Color.DARKSLATEGRAY);
		c.fillText(textScore + t.getScore(), 85 + 20 + mesh * XSIZE, 160);
		String textTitel = "TETRIS";
		String textHighScore = "Highest Score: ";
		Font newFont2 = Font.font("Arial", FontWeight.BOLD, 30);
		c.setFont(newFont2);
		c.fillText(textTitel, 85 + 20 + mesh * XSIZE, 100);
		c.fillText(
				textHighScore + "\nName: " + scoresList.getHighScoreName() + "\nTime: " + scoresList.getHighScoreTime(),
				85 + 20 + mesh * XSIZE, 240);
		c.fillText("" + scoresList.getHighScore(), 85 + 20 + mesh * XSIZE + 220, 240);
		c.fillRect(20, 20, mesh * XSIZE, 2);
		c.fillRect(20, 20, 2, mesh * YSIZE);
		c.fillRect(20, 20 + mesh * YSIZE, mesh * XSIZE, 2);
		c.fillRect(20 + mesh * XSIZE, 20, 2, mesh * YSIZE);
	}

	public void switchColor(GraphicsContext c, IStone stone) {
		switch (stone.getKey()) {
		case 0:
			c.setFill(Color.DARKGOLDENROD);
			break;
		case 1:
			c.setFill(Color.INDIANRED);
			break;
		case 2:
			c.setFill(Color.YELLOWGREEN);
			break;
		case 3:
			c.setFill(Color.CADETBLUE);
			break;
		case 4:
			c.setFill(Color.FORESTGREEN);
			break;
		case 5:
			c.setFill(Color.LIGHTSLATEGRAY);
			break;
		case 6:
			c.setFill(Color.ROSYBROWN);
			break;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		drawGame();
//		System.out.println("Observer\nupdate()\ncalled");
	}
}
