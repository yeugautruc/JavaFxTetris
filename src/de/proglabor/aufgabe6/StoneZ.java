package de.proglabor.aufgabe6;

public class StoneZ extends IStone {

	public StoneZ() {
		super(new Point2D(0, 0), new Point2D(1, 0), new Point2D(1, 1), new Point2D(2, 1), 3, 6);
		this.setCenter(this.getElement(3));
	}

}
