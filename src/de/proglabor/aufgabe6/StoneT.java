package de.proglabor.aufgabe6;

public class StoneT extends IStone {

	public StoneT() {
		super(new Point2D(0, 0), new Point2D(1, 0), new Point2D(2, 0), new Point2D(1, 1), 3, 5);
		this.setCenter(this.getElement(4));
	}

}
