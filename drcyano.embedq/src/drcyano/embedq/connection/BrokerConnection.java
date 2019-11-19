package drcyano.embedq.connection;

import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

public abstract class BrokerConnection {
	public abstract void subscribe(Subscriber sub, Topic topic);
	
	public abstract void publish(Message m, Topic topic);
	
	public abstract void unsubscribe(Subscriber sub, Topic topic);
	
	public void unsubscribe(Subscriber sub) {
		// unsubscribe from all topics
		unsubscribe(sub, new Topic("#"));
	}
}
