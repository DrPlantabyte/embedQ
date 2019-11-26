package drcyano.embedq.connection;

import drcyano.embedq.client.Publisher;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

public abstract class BrokerConnection implements Publisher {
	public abstract void subscribe(Subscriber sub, Topic topic);
	
	
	public abstract void unsubscribe(Subscriber sub, Topic topic);
	
	public abstract void unsubscribeAll(Subscriber sub);
}
