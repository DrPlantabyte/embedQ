package drcyano.embedq.connection;
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
import drcyano.embedq.client.Publisher;
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

/**
 * This abstract class defines the API that a Client will use to publish messages to the Broker. The
 * Broker's end of the communication is handled by the <code>SourceConnedtion</code> class.
 */
public abstract class BrokerConnection implements Publisher {
	/**
	 * Subscribe a subscriber to a given topic.
	 * @param sub Message handler that wants to receive messages on the subscribed topic
	 * @param topic The topic to subscribe to
	 */
	public abstract void subscribe(Subscriber sub, Topic topic);
	
	/**
	 * Unsubscribe a subscriber from a specific topic
	 * @param sub Message handler that no longer wants to receive messages on the subscribed topic
	 * @param topic The topic to unsubscribe from
	 */
	public abstract void unsubscribe(Subscriber sub, Topic topic);
	
	/**
	 * Unsubscribe from all topics
	 * @param sub Message handler that no longer wants to receive messages
	 */
	public abstract void unsubscribeAll(Subscriber sub);
}
