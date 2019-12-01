package drcyano.embedq.broker;

import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;
import drcyano.embedq.imp.IntraprocessBrokerConnection;
import drcyano.embedq.imp.NetworkSourceConnection;
import protocol.PayloadType;
import protocol.Protocol;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBroker extends Broker {
	
	private final Map<Topic, Set<SourceConnection>> subscribers = new ConcurrentHashMap<Topic, Set<SourceConnection>>();
	
	private final AsynchronousServerSocketChannel netServer;
	private final Map<SourceConnection, AsynchronousSocketChannel> clientChannels;
	
	
	public SimpleBroker() {
		netServer = null;clientChannels = null;
	}
	
	public SimpleBroker(int networkPort) throws IOException {
		InetSocketAddress hostAddress = new InetSocketAddress(networkPort);
		netServer = AsynchronousServerSocketChannel.open();
		netServer.bind(hostAddress);
		netServer.accept("param", new ConnectCompletionHandler());
		clientChannels = Collections.synchronizedMap(new HashMap<>());
	}
	
	private void handleNetworkIO() {
		while(runControl.get()){
			try {
				handleNewConnections();
				readIncomingMessages();
				sendOutgoingMessages();
				Thread.sleep(0, 100000);
			} catch(InterruptedException e) {
				// interruption means signal to terminate
				break;
			}
		}
	}
	
	private void handleNewConnections() {
		for(AsynchronousServerSocketChannel serverChannel : hostChannels){
		
		}
	}
	
	
	@Override public BrokerConnection getConnection(){
		return new IntraprocessBrokerConnection(this);
	}
	
	public synchronized void openUnencryptedNetworkPort(int portNum) throws
			IOException {
		final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
		server.bind(new InetSocketAddress("localhost", portNum));
		server.accept("param", new CompletionHandler<AsynchronousSocketChannel, String>() {
			@Override
			public void completed(
					final AsynchronousSocketChannel channel,
					final String param
			) {
				NetworkSourceConnection netSrc = new NetworkSourceConnection(channel);
				//
				server.accept(param, this); // chain looping for continuous connection listening
			}
			
			@Override
			public void failed(final Throwable exc, final String param) {
				System.err.println("Failed to listen for incoming sockets");
				// TODO
			}
		});
		hostChannels.add(server);
	}
	
	public synchronized void closeNetworkPorts(){
		runControl.set(false);
		networkIOtThread.interrupt();
	}
	
	@Override public void publishMessage(final Message messageBuffer) {
		final Topic pubTopic = messageBuffer.getTopic();
		subscribers
				.keySet()
				.stream()
				.filter(pubTopic::matches)
				.map(subscribers::get)
				.forEach((Set<SourceConnection> set)->
						set.stream()
								.forEach((SourceConnection sub)->sub.sendMessage(messageBuffer)));
	}
	
	@Override public synchronized void addSubscription(SourceConnection sourceConnection, Topic topic) {
		Set<SourceConnection> set;
		if(subscribers.containsKey(topic) == false){
			set =  new HashSet<SourceConnection>();
			subscribers.put(topic,set);
		} else {
			set = subscribers.get(topic);
		}
		set.add(sourceConnection);
	}
	@Override public synchronized void removeSubscription(SourceConnection sourceConnection, Topic topic) {
		if(subscribers.containsKey(topic)){
			boolean removed = subscribers.get(topic).remove(sourceConnection);
		}
	}
	@Override public synchronized void removeSubscriber(SourceConnection sourceConnection) {
		for(Set<SourceConnection> channel : subscribers.values()){
			channel.remove(sourceConnection);
		}
	}
	
	
	private class ConnectCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, String> {
		
		@Override
		public void completed(AsynchronousSocketChannel socketChannel, String param) {
			ByteBuffer buffer = ByteBuffer.allocate(0x10000); // TODO: figure out whether to put limit on payload size
			socketChannel.read(buffer, buffer, new ReadCompletionHandler(socketChannel));
			
			// TODO
			netServer.accept("param", this); // asynchronous looping
		}
		
		@Override
		public void failed(Throwable exc, String attachment) {
		// TODO
		}
	}
	
	private class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer>{
		
		private final AsynchronousSocketChannel socketChannel;
		public ReadCompletionHandler(final AsynchronousSocketChannel channel){
			this.socketChannel = channel;
		}
		
		
		@Override
		public void completed(Integer result, ByteBuffer buffer) {
			PayloadType type = Protocol.decodePayloadType(buffer);
			switch (type){
				case PUBLISH:{
					Message m = Protocol.decodePayloadMessage(buffer);
					publishMessage(m);
					break;
				}
				
				case SUBSCRIBE:{
					Topic t = Protocol.decodeSubscriberTopic(buffer);
					try {
						SourceConnection src = NetworkSourceConnection.fromChannel(socketChannel);
						addSubscription(src, t);
					} catch (IOException e) {
						failed(e, buffer);
					}
					break;
				}
				
				case UNSUBSCRIBE:{
					Topic t = Protocol.decodeSubscriberTopic(buffer);
					try {
						SourceConnection src = NetworkSourceConnection.fromChannel(socketChannel);
						removeSubscriber(src, t);
					} catch (IOException e) {
						failed(e, buffer);
					}
					break;
				}
				case UNSUBSCRIBE_ALL:{
					Topic t = Protocol.decodeSubscriberTopic(buffer);
					// TODO
					break;
				}
				default:
					throw new IllegalStateException("Unexpected enum state: "+type.name());
			}
			// TODO
			socketChannel.read(buffer, buffer, this); // async looping
		}
		
		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
		
		}
	}
	
	private class WriteCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Message>{
		
		@Override
		public void completed(AsynchronousSocketChannel socketChannel, Message param) {
			// TODO
		}
		
		@Override
		public void failed(Throwable exc, Message attachment) {
			// TODO
		}
	}
}
