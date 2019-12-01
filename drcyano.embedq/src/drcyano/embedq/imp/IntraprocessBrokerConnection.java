package drcyano.embedq.imp;

import drcyano.embedq.broker.Broker;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.connection.BrokerConnection;
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
	
	@Override public void publishReliable(Message m){
		//Payload p = PayloadManager.encodePublishEvent(m, topic);
		broker.publishMessageReliable(m);
	}
	
	@Override public void publishFast(Message m){
		publishReliable(m);
	}
	
	@Override
	public void unsubscribe(Subscriber sub, Topic topic) {
		broker.removeSubscription(new IntraprocessSourceConnection(sub), topic);
	
	}
	
	@Override
	public void unsubscribeAll(Subscriber sub) {
		broker.removeSubscriber(new IntraprocessSourceConnection(sub));
	}
}
