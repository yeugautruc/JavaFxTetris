package de.proglabor.aufgabe6;

import java.util.Scanner;

public class Console {
	public static void main(String[] arg) {
		Scanner scan = new Scanner(System.in);
		Tetris t = new Tetris();
		System.out.println("type s to start game:");
		try {
			t.loadGame();
		} catch (SpaceAlreadyOccupiedException e) {
			e.printStackTrace();
		}

		while (!t.gameFinished()) {
			String key = scan.next();
			switch (key) {
			case "w":
				t.rotating();
				break;
			case "a":
				t.movingLeft();
				break;
			case "d":
				t.movingRight();
				break;
			case "s":
				t.falling();
				break;
			case "x":
				t.fallingAll();
				break;
			case "save":
				t.saveGame();
				System.out.println("via Console!");
			}
			System.out.println("your score: " + t.getScore());
			System.out.println(t.toStringConsole());
		}
		System.out.println("Total Score: " + t.getScore());
		System.out.println("Game Over!!!");
		scan.close();
	}
	
}