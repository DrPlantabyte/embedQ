package drcyano.embedq.imp;

import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkSourceConnection extends SourceConnection {
	
	private static final AtomicInteger sequencer = new AtomicInteger(0);
	
	private final AsynchronousSocketChannel channel;
	private final InetSocketAddress clientAddress;
	
	protected NetworkSourceConnection(InetSocketAddress address, AsynchronousSocketChannel channel){
		super(String.format("%s#%s", address.getAddress().toString(), address.getPort()));
		this.clientAddress = address;
		this.channel = channel;
	}
	
	public static NetworkSourceConnection fromChannel(final AsynchronousSocketChannel channel)
			throws IOException {
		final SocketAddress address = channel.getRemoteAddress();
		if(address instanceof InetSocketAddress == false){
			throw new UnsupportedOperationException("Non-network sockets (sockets lacking an IP address and port number) are not supported by this class");
		}
		NetworkSourceConnection newCon = new NetworkSourceConnection((InetSocketAddress)address, channel);
		//
		return newCon;
	}
	
	@Override
	public void sendMessage(Message msg) {
		channel.write()
	}
}
