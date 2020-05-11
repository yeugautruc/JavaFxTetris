package de.proglabor.aufgabe6;

public class StoneS extends IStone {

	public StoneS() {
		super(new Point2D(2, 0), new Point2D(1, 0), new Point2D(1, 1), new Point2D(0, 1), 3, 4);
		this.setCenter(this.getElement(3));
	}

}
