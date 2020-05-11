package de.proglabor.aufgabe6;

public class SpaceAlreadyOccupiedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor.
	 *
	 * @param message the message
	 */
	public SpaceAlreadyOccupiedException() {
		super("Space Already Occupied Exception occured!\nCannot Save Data.");
	}
}
