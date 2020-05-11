package de.proglabor.aufgabe6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.proglabor.aufgabe6.IStone;
import de.proglabor.aufgabe6.Point2D;
import de.proglabor.aufgabe6.Tetris;

/**
 * Represent the condition and the stone action
 * 
 * @author Doan,Manh Hung
 * @version 1.0
 * @since 1.0
 **/
public class Tetris extends Observable {
	/* variable from class Tetris */
	public static final int XSIZE = 10;
	public static final int YSIZE = 20;
	public static final int mesh = 45;
	private int score = 0;
	private boolean gameOver = false;
	private IStone currentStone;
	private List<IStone> stones;
	private GameDataHandle gameHandle = new GameDataHandle("gameData.csv");

	/*
	 * *
	 * 
	 * @Constructor from TETRIS Class
	 * 
	 **/
	public Tetris() {
		stones = new ArrayList<IStone>();
		currentStone = newStone(randomNumber());
	}

	/*
	 * Call the newStone() method from class StoneFactory to create new Stone
	 * 
	 * @param recieve input number(0-6) to choose, which stone is created
	 */
	public IStone newStone(int number) {
		currentStone = StoneFactory.newStone(number);
		if (gameEndTest()) {
			gameOver = true;
		}
		return currentStone;
	}

	private boolean gameEndTest() {
		Point2D comparePoint = new Point2D(0, 0);
		Boolean end = false;
		int count = 0;
		for (int i = 0; i < Tetris.YSIZE; i++) {
			for (int j = 0; j < Tetris.XSIZE; j++) {
				comparePoint.setX(j);
				comparePoint.setY(i);
				for (IStone stone : stones) {
					if (stone.hasElement(comparePoint))
						end = true;
				}
			}
			if (end) {
				count++;
			}
		}
		if (count == 20) {
			return true;
		}

		return false;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/*
	 * @return random number (0~6)
	 */
	public int randomNumber() {

		return (int) (Math.random() * 7);
	}

	/*
	 * All move method are here
	 */
	public void movingLeft() {

		if (canMove("left")) {
			currentStone.movingLeft();
			setChanged();
			notifyObservers();
		}

	}

	public void movingRight() {
		if (canMove("right")) {
			currentStone.movingRight();
			setChanged();
			notifyObservers();
		}
	}

	public void falling() {
		if (canMove("down")) {
			currentStone.falling();
			setChanged();
			notifyObservers();
		} else {
			stones.add(currentStone);
			score += 1;
			deleteRow();
			newStone(randomNumber());
			setChanged();
			notifyObservers();
		}
	}

	public void fallingAll() {
		while (canMove("down")) {
			currentStone.falling();
			setChanged();
			notifyObservers();
		}
		if (!canMove("down")) {
			stones.add(currentStone);
			score += 1;
			deleteRow();
			newStone(randomNumber());
			setChanged();
			notifyObservers();
		}
	}

	public void rotating() {
		for (int i = 0; i < 2; i++) {
			if (!canMove("rotate")) {
				movingLeft();
				setChanged();
				notifyObservers();
			}
		}
		for (int i = 0; i < 2; i++) {
			if (!canMove("rotate")) {
				movingRight();
				setChanged();
				notifyObservers();
			}
		}
		if (canMove("rotate")) {
			currentStone.rotate();
			setChanged();
			notifyObservers();
		}
	}

	public boolean canMove(String in) {
		IStone tmpStone = StoneFactory.newStone(currentStone.getKey());
		tmpStone.copyStone(currentStone);
		switch (in) {
		case "left":
			tmpStone.movingLeft();
			break;
		case "right":
			tmpStone.movingRight();
			break;
		case "down":
			tmpStone.falling();
			break;
		case "rotate":
			tmpStone.rotate();
			break;
		}
		if (tmpStone.overRange()) {
			return false;
		}
		for (IStone stone : stones) {
			for (Point2D point : tmpStone.getElements()) {
				if (stone.hasElement(point) && stone.hashCode() != currentStone.hashCode()) {
					return false;
				}
			}
		}
		return true;
	}
	// draw method draw all stone in list into context

	public IStone getCurrentStone() {
		return currentStone;
	}

	public void setCurrentStone(IStone currentStone) {
		this.currentStone = currentStone;
	}

	/*
	 * Delete a full row
	 */
	// Delete row when fullRow() is true
	public void deleteRow() {
		for (int i = 0; i < Tetris.YSIZE; i++) {
			if (fullRow(i)) {
				score += 10;
//				System.out.println("Row " + i + " is full, get 10 points");
				for (IStone stone : stones) {
					stone.deleteElements(i);
				}
			}
		}
//		System.out.println("Size stones before remove "+ stones.size());
		clearList();
		for (IStone stone : stones) {
			currentStone = stone;
			while (canMove("down")) {
				currentStone.falling();
			}
		}
		for (int i = 0; i < Tetris.YSIZE; i++) {
			if (fullRow(i)) {
				deleteRow();
			}
		}

	}

	// Check if Row is full
	public boolean fullRow(int y) {
		Point2D comparePoint = new Point2D(0, 0);
		int checkFullLine = 0;
		comparePoint.setY(y);
		for (int j = 0; j < XSIZE; j++) {
			comparePoint.setX(j);
			for (IStone stone : stones) {
				if (stone.hasElement(comparePoint))
					checkFullLine++;
			}
		}
		if (checkFullLine == 10) {
			return true;
		}
		return false;
	}

	// Clear null Stone
	public void clearList() {
		List<IStone> remove = new ArrayList<IStone>();
		for (IStone stone : stones) {
			if (stone.checkIfNull()) {
				remove.add(stone);
			}
		}
		stones.removeAll(remove);
	}

	/*
	 * Save game and load game
	 */
	public void saveGame() {
		gameHandle.saveStringToCsv(toString());
	}

	public void loadGame() throws SpaceAlreadyOccupiedException {
		List<String> loadDataString = new ArrayList<String>();
		try {
			loadDataString = gameHandle.loadStringFromCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}
		stones.removeAll(stones);
		setScore(Integer.parseInt(loadDataString.get(0)));
		int index = 1;
		while (index < loadDataString.size()) {
			if (loadDataString.get(index) != null) {
				String temp = loadDataString.get(index);
				String[] result = temp.split(",");
				IStone newStone = StoneFactory.newStone(Integer.parseInt(result[0]));
				newStone.getElements().removeAll(newStone.getElements());
				for (int i = 1; i < result.length; i += 2) {
					if (Integer.parseInt(result[i].trim()) != (-1))
						newStone.getElements().add(new Point2D(Integer.parseInt(result[i].trim()),
								Integer.parseInt(result[i + 1].trim())));
				}
				stones.add(newStone);
			}
			index++;
		}
		//
		if (SpaceOccupiedTest()) {
			stones.removeAll(stones);
			throw new SpaceAlreadyOccupiedException();
		}
	}

	private boolean SpaceOccupiedTest() {
		for (int i = 0; i < stones.size() - 1; i++) {
			for (int j = i + 1; j < stones.size(); j++) {
				for (Point2D point : stones.get(i).getElements()) {
					if (stones.get(j).hasElement(point)) {
						return true;
					}
				}
			}
		}
		return false;   
	}

	// to string method
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder("");
		output.append("" + score + "\n");
		for (IStone stone : stones) {
			output.append("" + stone.getKey());
			if (stone.getElements().size() != 0) {
				for (int i = 0; i < stone.getElements().size(); i++) {
					output.append("," + stone.getElement(i + 1).getX() + "," + stone.getElement(i + 1).getY());
				}
				if (stone.getElements().size() < 4) {
					for (int i = stone.getElements().size(); i < 4; i++) {
						output.append(",-1,-1");
					}
				}
			}
			output.append("\n");
		}
		return output.toString();
	}

	public String toStringConsole() {
		String matchField = "";
		boolean hit = false;
		Point2D box = new Point2D(0, 0);
		for (int i = 0; i < YSIZE; i++) {
			box.setY(i);
			for (int j = 0; j < XSIZE; j++) {
				box.setX(j);
				for (IStone stone : stones) {
					if (stone.hasElement(box)) {
						hit = true;
					}
				}
				if (currentStone.hasElement(box)) {
					hit = true;
				}
				matchField += (hit) ? "x" : "-";
				hit = false;
			}
			matchField += "\n";
		}
		return matchField;
	}

	//
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	// get stones list
	public List<IStone> getStones() {
		return stones;
	}

	public boolean gameFinished() {
		return gameOver;
	}

	public GameDataHandle getSaveGameHandler() {
		return gameHandle;
	}
}
