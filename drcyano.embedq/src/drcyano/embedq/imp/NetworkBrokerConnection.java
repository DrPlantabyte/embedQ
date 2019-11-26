package drcyano.embedq.imp;

import drcyano.embedq.client.Subscriber;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.net.InetAddress;

public class NetworkBrokerConnection extends BrokerConnection {
	
	public NetworkBrokerConnection(InetAddress hostAddress, int portNumber){
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	@Override
	public void subscribe(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
	
	@Override
	public void publishReliable(Message m, Topic topic) {
		// use TCP stream
		throw new UnsupportedOperationException("Not implemented yet!");
		
	}
	
	@Override
	public void publishFast(Message m, Topic topic) {
		// use series of UDP blocks (of size 508 bytes each, including internal header)
		throw new UnsupportedOperationException("Not implemented yet!");
		
	}
	
	@Override
	public void unsubscribe(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
}
