package de.proglabor.aufgabe6;

public class UnknownParameterException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor.
	 *
	 * @param message the message
	 */
	public UnknownParameterException(String message) {
		super("Unknown Parameter has input: " + message);
	}
}
