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
import drcyano.embedq.connection.SourceConnection;
import drcyano.embedq.data.Message;

import java.net.InetAddress;

public class NetworkSourceConnection extends SourceConnection {
	public NetworkSourceConnection(InetAddress sourceAddress, int portNumber) {
		super(String.format("%s#%s@%s", NetworkBrokerConnection.class.getSimpleName(),
				sourceAddress.getHostAddress(),
				portNumber));
	}
	
	@Override
	public void sendMessage(Message msg) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
