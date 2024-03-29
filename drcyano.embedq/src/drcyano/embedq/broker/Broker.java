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

/**
 * This is the abstract superclass of all EmbedQ Brokers. It defines the API which programmers will
 * use to interact with the Broker.
 *
 * Note that <code>getConnection()</code> can only be invoked within this process, so a
 * network-enabled broker may choose not to support it.
 */
public abstract class Broker {
	/**
	 * Gets a <code>BrokerConnection</code> object for intra-process communication with the broker
	 * (bypassing any remote access protocols)
	 * @return a <code>BrokerConnection</code> instance ready to use
	 */
	public abstract BrokerConnection getConnection();
	
	/**
	 * Publishes a message, relaying the message to all subscribers to the message's topic.
	 * @param message A message instance
	 */
	public abstract void publishMessage(Message message);
	
	/**
	 * Adds a subscriber to this Broker. This method <b>will not be called directly by subscribers</b>,
	 * but by the <code>BrokerConnection</code> intermediary.
	 * @param sourceConnection A <code>SourceConnection</code> instance to transport the messages from
	 *                         the Broker to the Client
	 * @param topic The topic to listen for messages
	 */
	public abstract  void addSubscription(SourceConnection sourceConnection, Topic topic);
	
	/**
	 * Tells the Broker to stop sending messages on the given topic to this <code>SourceConnection</code>
	 * instance (in otherwords, unsubscribe this client from this topic)
	 * @param sourceConnection A <code>SourceConnection</code> instance representing a Client
	 * @param topic The topic to unsubscribe from
	 */
	public abstract  void removeSubscription(SourceConnection sourceConnection, Topic topic);
	
	/**
	 * Tells the Broker to stop sending messages to a given client completely (unsubscribe from all
	 * topics).
	 * @param sourceConnection A <code>SourceConnection</code> instance representing a Client
	 */
	public abstract  void removeSubscriber(SourceConnection sourceConnection);
	
}
