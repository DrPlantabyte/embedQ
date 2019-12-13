package drcyano.embedq.client;
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
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

/**
 * Functional interface for classes that relay Messages to the Broker from the Client.
 */
public interface Publisher {
	/**
	 * This method should pass the given message to the Broker
	 * @param m a message to publish
	 */
	public abstract void publish(Message m);
}
