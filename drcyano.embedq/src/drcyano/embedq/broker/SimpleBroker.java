package drcyano.embedq.broker;

import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;
import drcyano.embedq.imp.IntraprocessBrokerConnection;
import drcyano.embedq.imp.NetworkSourceConnection;

import java.io.IOException;
import java.net.*;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleBroker extends Broker {
	
	private final Map<Topic, Set<SourceConnection>> subscribers = new ConcurrentHashMap<Topic, Set<SourceConnection>>();
	
	private final Thread networkIOtThread;
	private final List<AsynchronousServerSocketChannel> hostChannels = Collections.synchronizedList(new ArrayList<>());
	private final List<AsynchronousSocketChannel> clientChannels = Collections.synchronizedList(new ArrayList<>());
	private final AtomicBoolean runControl = new AtomicBoolean(true);
	
	
	public SimpleBroker() {
		networkIOtThread = new Thread(this::handleNetworkIO);
		networkIOtThread.setDaemon(true);
		networkIOtThread.setName(SimpleBroker.class.getCanonicalName()+"$outbox-worker");
		networkIOtThread.start();
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
	
}
