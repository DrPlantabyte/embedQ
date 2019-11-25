package drcyano.embedq.exceptions;

public class InvalidTopicStringException extends RuntimeException {
	public InvalidTopicStringException(String message){
		super(message);
	}
	public InvalidTopicStringException(String message, Throwable cause){
		super(message, cause);
	}
	public InvalidTopicStringException(Throwable cause){
		super(cause);
	}
}
