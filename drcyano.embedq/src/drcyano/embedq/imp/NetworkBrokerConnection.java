package drcyano.embedq.imp;
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
import drcyano.embedq.client.Subscriber;
import drcyano.embedq.connection.BrokerConnection;
import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.net.InetAddress;

public class NetworkBrokerConnection extends BrokerConnection {
	
	public NetworkBrokerConnection(InetAddress hostAddress, int portNumber){
		throw new UnsupportedOperationException("Not implemented yet!");
	}
	
	@Override
	public void subscribe(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
	
	@Override
	public void publish(Message m) {
		// use TCP stream
		throw new UnsupportedOperationException("Not implemented yet!");
		
	}
	
	@Override
	public void unsubscribe(Subscriber sub, Topic topic) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
	
	@Override
	public void unsubscribeAll(Subscriber sub) {
		throw new UnsupportedOperationException("Not implemented yet!");
	
	}
}
