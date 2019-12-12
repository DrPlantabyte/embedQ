package drcyano.embedq.connection;

import drcyano.embedq.client.Publisher;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

/**
 * This abstract class defines the API that a Client will use to publish messages to the Broker. The
 * Broker's end of the communication is handled by the <code>SourceConnedtion</code> class.
 */
public abstract class BrokerConnection implements Publisher {
	/**
	 * Subscribe a subscriber to a given topic.
	 * @param sub Message handler that wants to receive messages on the subscribed topic
	 * @param topic The topic to subscribe to
	 */
	public abstract void subscribe(Subscriber sub, Topic topic);
	
	/**
	 * Unsubscribe a subscriber from a specific topic
	 * @param sub Message handler that no longer wants to receive messages on the subscribed topic
	 * @param topic The topic to unsubscribe from
	 */
	public abstract void unsubscribe(Subscriber sub, Topic topic);
	
	/**
	 * Unsubscribe from all topics
	 * @param sub Message handler that no longer wants to receive messages
	 */
	public abstract void unsubscribeAll(Subscriber sub);
}
