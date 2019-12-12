package drcyano.embedq.exceptions;

/**
 * This exception is thrown to indicate that a <code>Topic</code> string is not valid.
 */
public class InvalidTopicStringException extends RuntimeException {
	/**
	 * Standard exception constructor
	 * @param message error message
	 */
	public InvalidTopicStringException(String message){
		super(message);
	}
	
	/**
	 *
	 * Standard exception constructor
	 * @param message error message
	 * @param cause Other exception that lead to this one being thrown
	 */
	public InvalidTopicStringException(String message, Throwable cause){
		super(message, cause);
	}
	
	/**
	 *
	 * Standard exception constructor
	 * @param cause Other exception that lead to this one being thrown
	 */
	public InvalidTopicStringException(Throwable cause){
		super(cause);
	}
}
