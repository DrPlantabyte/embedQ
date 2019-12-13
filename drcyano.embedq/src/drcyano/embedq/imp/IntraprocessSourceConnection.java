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
import drcyano.embedq.connection.SourceConnection;
import drcyano.embedq.data.Message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of <code>SourceConnection</code> for intra-process communication
 */
public class IntraprocessSourceConnection extends SourceConnection {
	private final Subscriber src;
	private static final AtomicInteger sequencer = new AtomicInteger(0);
	
	/**
	 * Wraps a <code>Subscriber</code> instance
	 * @param sub A message handler
	 */
	public IntraprocessSourceConnection( Subscriber sub) {
		super(String.format("%s:%s", IntraprocessSourceConnection.class.getSimpleName(), sequencer.incrementAndGet()));
		this.src = sub;
	}
	/**
	 * This method takes a message from the Broker and transmits it to the client
	 * @param msg The message to transmit
	 */
	@Override
	public void sendMessage(Message msg) {
		src.receiveMessage(msg);
	}
	/**
	 * Hash code override based. Two instances of <code>SourceConnection</code> that talk to the same
	 * client are considered equal.
	 * @return a hash code
	 */
	@Override public int hashCode(){
		return src.hashCode();
	}
	/**
	 * Two instances of <code>SourceConnection</code> that talk to the same client are considered equal.
	 * @param that Another object instance
	 * @return <code>true</code> if and only if the other object is a SourceConnection connecting to
	 * the same client as this instance.
	 */
	@Override public boolean equals(Object that){
		if (this == that) return true;
		if(that instanceof IntraprocessSourceConnection){
			IntraprocessSourceConnection other = (IntraprocessSourceConnection)that;
			return this.src == other.src;
		} else {
			return false;
		}
	}
	
	private static boolean isEqual(Object a, Object b){
		if(a == b) return true;
		if( a == null){
			return b == null;
		} else {
			return a.equals(b);
		}
	}
}
