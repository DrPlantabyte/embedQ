package drcyano.embedq.connection;

import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

public abstract class BrokerConnection {
	public abstract void subscribe(Subscriber sub, Topic topic);
	
	public abstract void publishReliable(Message m, Topic topic);
	public void publishFast(Message m, Topic topic){
		publishReliable(m, topic);
	}
	
	public abstract void unsubscribe(Subscriber sub, Topic topic);
	
	public abstract void unsubscribeAll(Subscriber sub);
}
