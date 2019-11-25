package drcyano.embedq.broker;

import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;
import drcyano.embedq.imp.IntraprocessBrokerConnection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBroker extends Broker {
	
	private final Map<Topic, Set<SourceConnection>> subscribers = new ConcurrentHashMap<Topic, Set<SourceConnection>>();
	
	@Override public BrokerConnection getConnection(){
		return new IntraprocessBrokerConnection(this);
	}
	/*
	@Override
	public void receivePayload(Payload payload) {
		PayloadType type = payload.getType();
		switch (type){
			case SUBSCRIBE:{
				SubscribeEvent subEvent = PayloadManager.decodeSubscribeEvent(payload);
				this.addSubscription(subEvent.getSourceConnection(), subEvent.getTopic());
				break;
			}
			case UNSUBSCRIBE:{
				UnsubscribeEvent subEvent = PayloadManager.decodeUnsubscribeEvent(payload);
				this.removeSubscription(subEvent.getSourceConnection(), subEvent.getTopic());
				break;
			}
			case PUBLISH:{
				PublishEvent pubEvent = PayloadManager.decodePublishEvent(payload);
				Topic pubTopic = pubEvent.getTopic();
				ByteBuffer messageBuffer = pubEvent.getMessageData();
				this.sendToSubscribers(pubTopic, messageBuffer);
				break;
			}
			default:
				throw new IllegalStateException("Unexpected payload type: "+type.name());
		}
	
	}*/
	
	@Override public void publishMessage(final Topic pubTopic, final Message messageBuffer) {
		subscribers
				.keySet()
				.stream()
				.filter(pubTopic::matches)
				.map(subscribers::get)
				.forEach((Set<SourceConnection> set)->
						set.stream()
								.forEach((SourceConnection sub)->sub.sendMessage(messageBuffer)));
	}
	
	@Override public synchronized void addSubscription(SourceConnection sourceConnection, Topic topic) {
		Set<SourceConnection> set;
		if(subscribers.containsKey(topic) == false){
			set =  new HashSet<SourceConnection>();
			subscribers.put(topic,set);
		} else {
			set = subscribers.get(topic);
		}
		set.add(sourceConnection);
	}
	@Override public synchronized void removeSubscription(SourceConnection sourceConnection, Topic topic) {
		if(subscribers.containsKey(topic)){
			subscribers.get(topic).remove(sourceConnection);
		}
	}
	
}
