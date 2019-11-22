package drcyano.embedq.connection.source;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class NetworkSourceConnection extends SourceConnection {
	protected NetworkSourceConnection(InetAddress sourceAddress) {
		super(sourceAddress.toString());
	}
	
	@Override
	public void sendMessage(ByteBuffer msg) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
