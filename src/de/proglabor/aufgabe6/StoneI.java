package de.proglabor.aufgabe6;

public class StoneI extends IStone {

	public StoneI() {
		super(new Point2D(0, 0), new Point2D(0, 1), new Point2D(0, 2), new Point2D(0, 3), 1, 0);
		this.setCenter(this.getElement(2));
	}

}
