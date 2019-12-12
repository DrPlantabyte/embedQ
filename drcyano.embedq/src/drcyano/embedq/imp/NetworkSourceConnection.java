package drcyano.embedq.imp;

import drcyano.embedq.connection.SourceConnection;
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
	private final DatagramSocket udpSocket;
	private final int clientDatagramPort;
	private final BiConsumer<Exception, AsynchronousSocketChannel> connectionErrorHandler;
	
	protected NetworkSourceConnection(InetSocketAddress address, AsynchronousSocketChannel tcpChannel, BiConsumer<Exception, AsynchronousSocketChannel> tcpConnectionErrorHandler, DatagramSocket udpSendSocket, final int clientDatagramPort) {
		super(String.format("%s#%s", address.getAddress().toString(), address.getPort()));
		this.clientAddress = address;
		this.channel = tcpChannel;
		this.connectionErrorHandler = tcpConnectionErrorHandler;
		this.udpSocket = udpSendSocket;
		this.clientDatagramPort = clientDatagramPort;
	}
	
	public static NetworkSourceConnection fromChannel(final AsynchronousSocketChannel channel, BiConsumer<Exception, AsynchronousSocketChannel> tcpConnectionErrorHandler, DatagramSocket udpSendSocket, final int clientDatagramPort)
			throws IOException {
		final SocketAddress address = channel.getRemoteAddress();
		if(address instanceof InetSocketAddress == false){
			throw new UnsupportedOperationException("Non-network sockets (sockets lacking an IP address and port number) are not supported by this class");
		}
		NetworkSourceConnection newCon = new NetworkSourceConnection((InetSocketAddress)address, channel, tcpConnectionErrorHandler, udpSendSocket, clientDatagramPort);
		//
		return newCon;
	}
	public static NetworkSourceConnection fromChannel(final AsynchronousSocketChannel channel, final BiConsumer<Exception, AsynchronousSocketChannel> tcpConnectionErrorHandler)
			throws IOException {
		return fromChannel(channel, tcpConnectionErrorHandler, null, 0); // no UDP support
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
	public void sendMessageFast(Message msg) throws IOException {
		if(this.udpSocket == null){
			// use TCP
			sendMessageReliable(msg);
		} else {
				byte[] datagram = Protocol.serializeMessageToBuffer(msg).array();
				DatagramPacket pk = new DatagramPacket(datagram, datagram.length, clientAddress.getAddress(), clientDatagramPort);
				udpSocket.send(pk);
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
