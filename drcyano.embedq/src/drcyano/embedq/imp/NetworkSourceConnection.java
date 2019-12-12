package drcyano.embedq.imp;

import drcyano.embedq.connection.SourceConnection;
import drcyano.embedq.data.Message;

import java.net.InetAddress;

public class NetworkSourceConnection extends SourceConnection {
	public NetworkSourceConnection(InetAddress sourceAddress, int portNumber) {
		super(String.format("%s#%s@%s", NetworkBrokerConnection.class.getSimpleName(),
				sourceAddress.getHostAddress(),
				portNumber));
	}
	
	@Override
	public void sendMessage(Message msg) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
