package de.proglabor.aufgabe6;

public class StoneFactory {
	public static IStone newStone(int number) {
		IStone out = null;
		switch (number) {
		case 0:
			out = new StoneI();
			break;
		case 1:
			out = new StoneJ();
			break;
		case 2:
			out = new StoneL();
			break;
		case 3:
			out = new StoneO();
			break;
		case 4:
			out = new StoneS();
			break;
		case 5:
			out = new StoneT();
			break;
		case 6:
			out = new StoneZ();
			break;
		}
		return out;
	}
}
