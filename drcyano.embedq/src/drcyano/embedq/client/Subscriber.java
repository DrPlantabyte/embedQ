package drcyano.embedq.client;

import drcyano.embedq.data.Message;

public abstract class Subscriber {
	public abstract void receiveMessage(Message m);
}
