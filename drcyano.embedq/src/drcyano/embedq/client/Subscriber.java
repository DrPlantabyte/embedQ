package drcyano.embedq.client;

import drcyano.embedq.data.Message;
/**
 * Functional interface for classes that listen for messages from the Broker. The Broker will handle
 * topic filtering, so the implementer of this interface need not check the Topic.
 */
public interface Subscriber {
	/**
	 * This method is called when a message is broadcast to the topic that this Subscriber has been
	 * subscribed to
	 * @param m The message received
	 */
	public abstract void receiveMessage(Message m);
}
