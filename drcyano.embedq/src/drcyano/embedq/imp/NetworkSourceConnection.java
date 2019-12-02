package drcyano.embedq.imp;

import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.protocol.Protocol;

import java.io.IOException;
import java.net.*;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class NetworkSourceConnection extends SourceConnection {
	
	private static final AtomicInteger sequencer = new AtomicInteger(0);
	
	private final AsynchronousSocketChannel channel;
	private final InetSocketAddress clientAddress;
	private final int brokerDatagramPort;
	private final int clientDatagramPort;
	private final DatagramSocket udpSocket;
	private final BiConsumer<Exception, AsynchronousSocketChannel> connectionErrorHandler;
	
	protected NetworkSourceConnection(InetSocketAddress address, AsynchronousSocketChannel channel, final int clientUDP, final int hostUDP, BiConsumer<Exception, AsynchronousSocketChannel> connectionErrorHandler) throws SocketException {
		super(String.format("%s#%s", address.getAddress().toString(), address.getPort()));
		this.clientAddress = address;
		this.channel = channel;
		this.brokerDatagramPort = hostUDP;
		this.clientDatagramPort = clientUDP;
		this.connectionErrorHandler = connectionErrorHandler;
		if(brokerDatagramPort > 0){
			udpSocket = new DatagramSocket(brokerDatagramPort);
		}else {
			udpSocket = null;
		}
	}
	
	public static NetworkSourceConnection fromChannel(final AsynchronousSocketChannel channel, final int clientUDP, final int hostUDP, BiConsumer<Exception, AsynchronousSocketChannel> connectionErrorHandler)
			throws IOException {
		final SocketAddress address = channel.getRemoteAddress();
		if(address instanceof InetSocketAddress == false){
			throw new UnsupportedOperationException("Non-network sockets (sockets lacking an IP address and port number) are not supported by this class");
		}
		NetworkSourceConnection newCon = new NetworkSourceConnection((InetSocketAddress)address, channel, clientUDP, hostUDP, connectionErrorHandler);
		//
		return newCon;
	}
	public static NetworkSourceConnection fromChannel(final AsynchronousSocketChannel channel)
			throws IOException {
		return fromChannel(channel, 0, 0, null); // no UDP support
	}
	
	@Override public int hashCode(){
		return clientAddress.hashCode();
	}
	@Override public boolean equals(Object o){
		if(o == this) return true;
		if(o instanceof NetworkSourceConnection){
			NetworkSourceConnection that = (NetworkSourceConnection)o;
			if(this.channel == that.channel) return true;
			return this.clientAddress.equals(that.clientAddress);
		}
		return false;
	}
	
	@Override
	public void sendMessageReliable(Message msg) {
		channel.write(Protocol.serializeMessageToBuffer(msg), msg, new WriteCompletionHandler(connectionErrorHandler, channel));
	}
	@Override
	public void sendMessageFast(Message msg) {
		if(this.brokerDatagramPort <= 0){
			// use TCP
			sendMessageReliable(msg);
		} else {
			try {
				byte[] datagram = Protocol.serializeMessageToBuffer(msg).array();
				DatagramPacket pk = new DatagramPacket(datagram, datagram.length, clientAddress.getAddress(), clientDatagramPort);
				udpSocket.send(pk);
			}catch (IOException ex){
				if(connectionErrorHandler != null){
					connectionErrorHandler.accept(ex, udpChannel);
				}
			}
		}
	}
	
	private static class WriteCompletionHandler implements CompletionHandler<Integer, Message>{
		private final BiConsumer<Exception, AsynchronousSocketChannel> connectionErrorHandler;
		private final AsynchronousSocketChannel channel;
		
		private WriteCompletionHandler(BiConsumer<Exception, AsynchronousSocketChannel> connectionErrorHandler, AsynchronousSocketChannel channel) {
			this.connectionErrorHandler = connectionErrorHandler;
			this.channel = channel;
		}
		
		@Override
		public void completed(Integer result, Message attachment) {
			// do nothing
		}
		
		@Override
		public void failed(Throwable exc, Message attachment) {
			if(connectionErrorHandler != null && exc instanceof Exception) {
				connectionErrorHandler.accept((Exception) exc, channel);
			} else {
				exc.printStackTrace(System.err); // uh-oh, bad shit just happened!
			}
		}
	}
}
