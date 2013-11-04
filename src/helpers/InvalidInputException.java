package helpers;

public class InvalidInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException() {
	}

	public InvalidInputException(String message) {
		super(message);
	}

	public InvalidInputException(Throwable ta) {
		super(ta);
	}

	public InvalidInputException(String message, Throwable ta) {
		super(message, ta);
	}
}
