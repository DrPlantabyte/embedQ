package drcyano.embedq.broker;

import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

public abstract class Broker {
	public abstract BrokerConnection getConnection();
	
	public abstract void publishMessage(Topic pubTopic, Message messageBuffer);
	
	public abstract  void addSubscription(SourceConnection sourceConnection, Topic topic);
	
	public abstract  void removeSubscription(SourceConnection sourceConnection, Topic topic);
	
}
