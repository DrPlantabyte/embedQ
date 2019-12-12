package drcyano.embedq.client;

import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

/**
 * Functional interface for classes that relay Messages to the Broker from the Client.
 */
public interface Publisher {
	/**
	 * This method should pass the given message to the Broker
	 * @param m a message to publish
	 */
	public abstract void publish(Message m);
}
