package drcyano.embedq.communication;

import drcyano.embedq.client.Subscriber;
import drcyano.embedq.communication.events.PublishEvent;
import drcyano.embedq.communication.events.SubscribeEvent;
import drcyano.embedq.communication.events.UnsubscribeEvent;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

public class PayloadManager {
	public static Payload encodeSubscribeEvent(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	public static Payload encodeUnsubscribeEvent(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	public static Payload encodePublishEvent(Message m, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	public static SubscribeEvent decodeSubscribeEvent(Payload payload) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	public static UnsubscribeEvent decodeUnsubscribeEvent(Payload payload) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	public static PublishEvent decodePublishEvent(Payload payload) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
}
