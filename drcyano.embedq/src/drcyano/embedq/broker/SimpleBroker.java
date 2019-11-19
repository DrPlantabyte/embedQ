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

public class SimpleBroker extends Broker {
	
	@Override public IntraprocessBrokerConnection getConnection(){
		return new IntraprocessBrokerConnection(this);
	}
	
	@Override
	public void receivePayload(Payload payload) {
		PayloadType type = PayloadManager.decodeType(payload);
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
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
	
	protected void sendToSubscribers(Topic pubTopic, ByteBuffer messageBuffer) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	protected void addSubscription(SourceConnection sourceConnection, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	protected void removeSubscription(SourceConnection sourceConnection, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
}
