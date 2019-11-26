package drcyano.embedq.client;

import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

public interface Publisher {
	
	public abstract void publishReliable(Message m, Topic topic);
	public abstract void publishFast(Message m, Topic topic);
}
