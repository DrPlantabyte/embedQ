package drcyano.embedq.exceptions;
/*
This file is part of EmbedQ.

EmbedQ is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

EmbedQ is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with EmbedQ.  If not, see <https://www.gnu.org/licenses/>.
 */
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
