package drcyano.embedq.connection;

import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.net.InetAddress;
import java.net.SocketAddress;

public class NetworkBrokerConnection extends BrokerConnection {
	
	public NetworkBrokerConnection(InetAddress hostAddress, int portNumber){
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	@Override
	public void subscribe(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
	
	@Override
	public void publish(Message m, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
	
	@Override
	public void unsubscribe(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
}
