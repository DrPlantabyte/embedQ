package drcyano.embedq.broker;

import drcyano.embedq.communication.Payload;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Topic;

import java.nio.ByteBuffer;

public abstract class Broker {
	public abstract BrokerConnection getConnection();
	
	public abstract void receivePayload(Payload payload) ;
	
	protected abstract void sendToSubscribers(Topic pubTopic, ByteBuffer messageBuffer);
	
	protected abstract  void addSubscription(SourceConnection sourceConnection, Topic topic);
	
	protected abstract  void removeSubscription(SourceConnection sourceConnection, Topic topic);
	
}
