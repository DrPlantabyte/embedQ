package drcyano.embedq.broker;
/*
This file is part of EmbedQ.

EmbedQ is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

EmbedQ is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with EmbedQ.  If not, see <https://www.gnu.org/licenses/>.
 */
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.connection.SourceConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;
import drcyano.embedq.imp.IntraprocessBrokerConnection;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a simple implementation of <code>Broker</code>. It acts as a message queue within this
 * JVM instance and does not support any inter-process or remote communication
 */
public class IntraprocessBroker extends Broker {
	
	private final Map<Topic, Set<SourceConnection>> subscribers = new ConcurrentHashMap<Topic, Set<SourceConnection>>();
	
	/**
	 * Gets a new connection to this Broker, which enables you to add or remove subscribers to this
	 * Broker
	 * @return a <code>BrokerConnection</code> instance for intraprocess communication
	 */
	@Override public BrokerConnection getConnection(){
		return new IntraprocessBrokerConnection(this);
	}
	
	/**
	 * Publishes a message, relaying the message to all subscribers to the message's topic.
	 * @param messageBuffer A message instance
	 */
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

/**
 * Adds a subscriber to this Broker. This method <b>will not be called directly by subscribers</b>,
 * but by the <code>BrokerConnection</code> intermediary.
 * @param sourceConnection A <code>SourceConnection</code> instance to transport the messages from
 *                         the Broker to the Client
 * @param topic The topic to listen for messages
 * */
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
	
	/**
	 * Tells the Broker to stop sending messages on the given topic to this <code>SourceConnection</code>
	 * instance (in otherwords, unsubscribe this client from this topic)
	 * @param sourceConnection A <code>SourceConnection</code> instance representing a Client
	 * @param topic The topic to unsubscribe from
	 */
	@Override public synchronized void removeSubscription(SourceConnection sourceConnection, Topic topic) {
		if(subscribers.containsKey(topic)){
			boolean removed = subscribers.get(topic).remove(sourceConnection);
		}
	}
	
	/**
	 * Tells the Broker to stop sending messages to a given client completely (unsubscribe from all
	 * topics).
	 * @param sourceConnection A <code>SourceConnection</code> instance representing a Client
	 */
	@Override public synchronized void removeSubscriber(SourceConnection sourceConnection) {
		for(Set<SourceConnection> channel : subscribers.values()){
			channel.remove(sourceConnection);
		}
	}
	
}
