package de.proglabor.aufgabe6;

public class StoneO extends IStone {

	public StoneO() {
		super(new Point2D(0, 0), new Point2D(0, 1), new Point2D(1, 0), new Point2D(1, 1), 2, 3);
		this.setCenter(this.getElement(2));
	}

	@Override
	public void rotate() {
	}
}
