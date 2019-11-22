package drcyano.embedq.broker;

import drcyano.embedq.communication.Payload;
import drcyano.embedq.communication.PayloadManager;
import drcyano.embedq.communication.PayloadType;
import drcyano.embedq.communication.events.PublishEvent;
import drcyano.embedq.communication.events.SubscribeEvent;
import drcyano.embedq.communication.events.UnsubscribeEvent;
import drcyano.embedq.connection.IntraprocessBrokerConnection;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Topic;

import java.nio.ByteBuffer;
import java.util.*;

public class SimpleBroker extends Broker {
	
	private final Map<Topic, Set<SourceConnection>> subscribers = Collections.synchronizedMap(new HashMap<Topic, Set<SourceConnection>>());
	
	@Override public IntraprocessBrokerConnection getConnection(){
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
	
	@Override public synchronized void publishMessage(final Topic pubTopic, final ByteBuffer messageBuffer) {
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
			set = subscribers.put(topic, new HashSet<SourceConnection>());
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
