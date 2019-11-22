package drcyano.embedq.client;

import drcyano.embedq.data.Message;

public interface Subscriber {
	public abstract void receiveMessage(Message m);
}
