package drcyano.embedq.broker;

import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;
import drcyano.embedq.imp.IntraprocessBrokerConnection;
import drcyano.embedq.imp.NetworkSourceConnection;
import drcyano.embedq.protocol.PayloadType;
import drcyano.embedq.protocol.Protocol;
import drcyano.embedq.protocol.QualityOfService;

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
	
	
	public SimpleBroker() {
		netServer = null;
	}
	
	public SimpleBroker(int networkPort) throws IOException {
		InetSocketAddress hostAddress = new InetSocketAddress(networkPort);
		netServer = AsynchronousServerSocketChannel.open();
		netServer.bind(hostAddress);
		netServer.accept("param", new ConnectCompletionHandler());
	}
	
	
	
	@Override public BrokerConnection getConnection(){
		return new IntraprocessBrokerConnection(this);
	}
	
	
	@Override public void publishMessageReliable(final Message messageBuffer) {
		final Topic pubTopic = messageBuffer.getTopic();
		subscribers
				.keySet()
				.stream()
				.filter(pubTopic::matches)
				.map(subscribers::get)
				.forEach((Set<SourceConnection> set)->
						set.stream()
								.forEach((SourceConnection sub)->sub.sendMessageReliable(messageBuffer)));
	}
	
	@Override public void publishMessageFast(final Message messageBuffer) {
		final Topic pubTopic = messageBuffer.getTopic();
		subscribers
				.keySet()
				.stream()
				.filter(pubTopic::matches)
				.map(subscribers::get)
				.forEach((Set<SourceConnection> set)->
						set.stream()
								.forEach((SourceConnection sub)->sub.sendMessageFast(messageBuffer)));
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
			netServer.accept("param", this); // asynchronous looping
		}
		
		@Override
		public void failed(Throwable exc, String attachment) {
		// TODO
			exc.printStackTrace(System.err);
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
					QualityOfService qos = Protocol.decodeQoS(buffer);
					Message m = Protocol.decodePayloadMessage(buffer);
					switch (qos){
						case FAST:{
							publishMessageFast(m);
							break;
						}
						case RELIABLE:{
							publishMessageReliable(m);
							break;
						}
						default:{
							failed(new IllegalStateException("Unexpected enum state: "+qos.name()), buffer);
						}
					}
					break;
				}
				
				case SUBSCRIBE:{
					Topic t = Protocol.decodeSubscriberTopic(buffer);
					int udpPort = Protocol.decodeUDPPort(buffer);
					try {
						SourceConnection src = NetworkSourceConnection.fromChannel(socketChannel, udpPort);
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
						removeSubscription(src, t);
					} catch (IOException e) {
						failed(e, buffer);
					}
					break;
				}
				case UNSUBSCRIBE_ALL:{
					try {
						SourceConnection src = NetworkSourceConnection.fromChannel(socketChannel);
						removeSubscriber(src);
					} catch (IOException e) {
						failed(e, buffer);
					}
					break;
				}
				default:
					throw new IllegalStateException("Unexpected enum state: "+type.name());
			}
			//
			socketChannel.read(buffer, buffer, this); // async looping
		}
		
		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
			// TODO
			exc.printStackTrace(System.err);
		}
	}
	
}
