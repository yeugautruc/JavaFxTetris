package de.proglabor.aufgabe4.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.proglabor.aufgabe6.IStone;
import de.proglabor.aufgabe6.Point2D;
import de.proglabor.aufgabe6.Tetris;

/**
 * 
 * @author Tanja Heuer
 *
 */
public class TetrisAufgabe4Test {

	private static int XSIZE = 10;
	private static int YSIZE = 20;

	private Tetris tetris;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tetris = new Tetris();
	}

	// +++ Test - correct toString outputs
	// ++++++++++++++++++++++++++++++++++++++++++++++++

	@Test(timeout = 1000)
	public void testToStringAfterOneDeletedRow() {

		/*
		 * Aussehen der toStringAusgabe eig: 13\n 3,8,19,-1,-1,9,19,-1,-1
		 */

		deleteOneRow(); // 10 point from lineDelete + 2 point from 2 Stone I
		// 2 Stone I can not make a line full
		IStone stoneO = tetris.newStone(3); // O
		moveRight(XSIZE);
		moveDown(YSIZE);

		String[] listed = tetris.toString().split("\\r?\\n");
		String[] stoneO_toString = listed[1].split(",");
		Integer[] newInt = parseToInt(stoneO_toString);

		// check score
		checkScore(13, listed[0]);

		// check correctness of stone
		testingElementsOfStonesToString(3, Integer.parseInt(stoneO_toString[0]), newInt, 2, stoneO);

	}

	/**
	 * Delete two rows and there are two stones left
	 */
	@Test(timeout = 1000)
	public void testToStringAfterTwoDeletedRows() {

		/*
		 * Aussehen der toStringAusgabe eig: 27 1,9,19,-1,-1,-1,-1,-1,-1
		 * 1,-1,-1,8,19,8,18,9,18
		 */

		deleteTwoRows();

		IStone stoneOne = tetris.newStone(1); // J
		moveRight(XSIZE);
		moveDown(YSIZE);

		IStone stoneTwo = tetris.newStone(1); // J
		rotate(2);
		moveRight(XSIZE);
		moveDown(YSIZE);
		System.out.println(tetris.toString());
		String[] listed = tetris.toString().split("\\r?\\n");
		String[] firstStone = new String[8];
		firstStone = listed[1].split(",");
		String[] secondStone = new String[8];
		secondStone = listed[2].split(",");
		Integer[] firstInt = parseToInt(firstStone);
		Integer[] secondInt = parseToInt(secondStone);

		checkScore(26, listed[0]);

		// check first stone
		testingElementsOfStonesToString(1, Integer.parseInt(firstStone[0]), firstInt, 3, stoneOne);

		// check second stone
		testingElementsOfStonesToString(1, Integer.parseInt(secondStone[0]), secondInt, 1, stoneTwo);
	}

	@Test(timeout = 1000)
	public void deleteOneRowAndTwoStonesLeft() {
		/*
		 * Aussehen der toStringAusgabe eig: 5 0,0,19,1,19,2,19,3,19
		 * 0,4,19,5,19,6,19,7,19 5,7,18,8,18,9,18,8,19 1,9,15,9,16,9,17,8,17
		 * 2,8,12,8,13,8,14,9,14
		 */

		deleteOneRow();

		IStone stoneOne = tetris.newStone(5); // T
		moveRight(XSIZE);
		moveDown(YSIZE);

		IStone stoneTwo = tetris.newStone(1); // J
		moveRight(XSIZE);
		moveDown(YSIZE);

		IStone stoneThree = tetris.newStone(2); // L
		moveRight(XSIZE);
		moveDown(YSIZE);

		String[] listed = tetris.toString().split("\\r?\\n");
		System.out.println(tetris.toString());
		assertEquals("Anzahl der ausgegebenen Zeilen: ", 6, listed.length);
		String[] firstStone = listed[3].split(",");
		String[] secondStone = listed[4].split(",");
		String[] thirdStone = listed[5].split(",");

		Integer[] firstInt = parseToInt(firstStone);
		Integer[] secondInt = parseToInt(secondStone);
		Integer[] thirdInt = parseToInt(thirdStone);

		checkScore(5, listed[0]);

		// check first stone
		testingElementsOfStonesToString(5, Integer.parseInt(firstStone[0]), firstInt, 0, stoneOne);

		// check second stone
		testingElementsOfStonesToString(1, Integer.parseInt(secondStone[0]), secondInt, 0, stoneTwo);

		// check second stone
		testingElementsOfStonesToString(2, Integer.parseInt(thirdStone[0]), thirdInt, 0, stoneThree);

	}

	@Test(timeout = 1000)
	public void loadGame() throws IOException {
		deleteOneRow();
		System.out.println(tetris.toString());
		tetris.saveGame();
//
		List<String> list = tetris.getSaveGameHandler().loadStringFromCsv();
//		System.out.println("vs\n" + tetris.toString());
//
		assertEquals("Anzahl der Zeilen: ", 3, list.size());
		assertEquals("abgespeicherte Score an der richtigen Stelle: ", 2, Integer.parseInt(list.get(0)));
	}

	@Test(timeout = 1000)
	public void loadGameTwo() throws IOException {
		deleteTwoRows();

		tetris.saveGame();

		List<String> list = tetris.getSaveGameHandler().loadStringFromCsv();

		assertEquals("Anzahl der Zeilen: ", 5, list.size());
		assertEquals("abgespeicherte Score an der richtigen Stelle: ", 4, Integer.parseInt(list.get(0)));
	}

	// ------Methoden zum Reihen löschen
	// -----------------------------------------------------------------

	/**
	 * Test deleting a row and set a new stone (L) on the left side
	 * 
	 * @param msg
	 * @param stone
	 * @param elements
	 */
	public void deleteOneRow() {

		tetris.newStone(0); // I
		rotate(1);
		moveLeft(XSIZE);
		moveDown(YSIZE);

		tetris.newStone(0); // I
		rotate(1);
		moveRight(1);
		moveDown(YSIZE);
	}

	/**
	 * Test deleting two rows and set a new stone (L) on the left side
	 * 
	 * @param msg
	 * @param stone
	 * @param elements
	 */
	public void deleteTwoRows() {

		tetris.newStone(0); // I
		rotate(1);
		moveLeft(XSIZE);
		moveDown(YSIZE);

		tetris.newStone(0); // I
		rotate(1);
		moveRight(1);
		moveDown(YSIZE);

		tetris.newStone(0); // I
		rotate(1);
		moveLeft(XSIZE);
		moveDown(YSIZE);

		tetris.newStone(0); // I
		rotate(1);
		moveRight(1);
		moveDown(YSIZE);
	}

	// ------ TEST METHODS
	// -----------------------------------------------------------------

	private void evaluateElements(String msg, IStone stone, int deleted, Point2D... elements) {
		String exp = "";
		String got = "";
		List<Point2D> elem = Arrays.asList(elements);

		int countDeletedElements = 0;

		for (Point2D float1 : elem) {
			if (float1.getX() == -1) {
				countDeletedElements++;
			}
		}
		assertEquals(deleted, countDeletedElements);

		for (int y = 0; y < YSIZE; y++) {
			for (int x = 0; x < XSIZE; x++) {
				Point2D p = new Point2D(x, y);
				if (elem.contains(p)) {
					exp += "(" + p.getX() + ", " + p.getY() + ")";
				}
				if (stone.hasElement(p)) {
					got += "(" + p.getX() + ", " + p.getY() + ")";
				}
			}
		}

		assertEquals(msg + stone.getClass().getSimpleName() + ": ", exp, got);
	}

	/*
	 * Ueberpruefung, ob der Score in der toString Methode richtig ist
	 */
	private void checkScore(int expected, String actual) {
		assertEquals("Punktzahl des Spieles", expected, Integer.parseInt(actual));
	}

	/*
	 * parse String array to int Array
	 */
	private Integer[] parseToInt(String[] list) {

		Integer[] newInt = new Integer[list.length];

		for (int i = 0; i < list.length; i++) {
			newInt[i] = Integer.parseInt(list[i]);
		}
		return newInt;
	}

	/*
	 * Diese Methode testet auf Korrektheit der Informationen aus der
	 * toString()-Methode, ob die Informationen korrekt abgespeichert werden können
	 */
	private void testingElementsOfStonesToString(int expectedType, int type, Integer[] newInt, int deletedStones,
			IStone stone) {

		// check number of second stone
		assertEquals("Typ des Steines :", expectedType, type);
		// check elements of second Stone
		evaluateElements("Elemente des Steines " + stone.getType() + ": ", stone, deletedStones,
				new Point2D(newInt[1], newInt[2]), new Point2D(newInt[3], newInt[4]), new Point2D(newInt[5], newInt[6]),
				new Point2D(newInt[7], newInt[8]));

	}

	// ------ METHODS FOR MOVING STONES
	// ---------------------------------------------------------------
	private void moveLeft(int n) {
		for (int i = 0; i < n; i++) {
			tetris.movingLeft();
		}
	}

	private void moveRight(int n) {
		for (int i = 0; i < n; i++) {
			tetris.movingRight();
		}
	}

	private void moveDown(int n) {
		for (int i = 0; i < n; i++) {
			tetris.falling();
		}
	}

	private void rotate(int n) {
		for (int i = 0; i < n; i++) {
			tetris.rotating();
		}
	}

}
