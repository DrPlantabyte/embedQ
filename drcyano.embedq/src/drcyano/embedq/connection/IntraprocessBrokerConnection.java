package drcyano.embedq.connection;

import drcyano.embedq.broker.Broker;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.communication.Payload;
import drcyano.embedq.communication.PayloadManager;
import drcyano.embedq.connection.source.IntraprocessSourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

public class IntraprocessBrokerConnection extends BrokerConnection {
	private final Broker broker;
	
	public IntraprocessBrokerConnection(Broker host){
		this.broker = host;
	}
	
	@Override public void subscribe(Subscriber sub, Topic topic) {
		//Payload p = PayloadManager.encodeSubscribeEvent(sub, topic);
		broker.addSubscription(new IntraprocessSourceConnection(sub), topic);
	}
	
	@Override public void publish(Message m, Topic topic){
		//Payload p = PayloadManager.encodePublishEvent(m, topic);
		broker.publishMessage(topic, m.getBytes());
	}
	
	@Override
	public void unsubscribe(Subscriber sub, Topic topic) {
		//Payload p = PayloadManager.encodeUnsubscribeEvent(sub, topic);
		broker.receivePayload(p);
	
	}
}
