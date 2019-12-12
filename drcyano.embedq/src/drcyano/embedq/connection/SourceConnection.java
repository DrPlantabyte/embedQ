package drcyano.embedq.connection;

import drcyano.embedq.data.Message;

/**
 * This abstract class defines the API that a Broker will use to publish messages to a specific Client.
 * The Client's end of the communication is handled by the <code>BrokerConnedtion</code> class.
 *
 * <b><u>It is vitally important that the <code>equals(Object other)</code> method return <code>true</code>
 * if two different instances of <code>SourceConnection</code> refer to the same Client!</u></b>
 */
public abstract class SourceConnection {
	
	private final String id;
	
	/**
	 * Implentors must call this constructor. Ideally, <code>idString</code> is unique to each
	 * subscriber and consistent to that subscriber (ie multiple instances of wrapping the same
	 * <code>Subscriber</code> instance will use the same idString). If not, then override the
	 * <code>equals(Object other)</code> and <code>hashCode()</code> methods to ensure that two
	 * instanceso of this class that communicate with the same Client are defined as equal to each
	 * other.
	 * @param idString A client-specific unique ID string.
	 */
	protected SourceConnection(String idString){
		this.id = idString;
	}
	
	/**
	 * This method takes a message from the Broker and transmits it to the client
	 * @param msg The message to transmit
	 */
	public abstract void sendMessage(Message msg);
	
	/**
	 * Hash code override based. Two instances of <code>SourceConnection</code> that talk to the same
	 * client are considered equal.
	 * @return a hash code
	 */
	@Override public int hashCode(){
		return id.hashCode();
	}
	
	/**
	 * Two instances of <code>SourceConnection</code> that talk to the same client are considered equal.
	 * @param other Another object instance
	 * @return <code>true</code> if and only if the other object is a SourceConnection connecting to
	 * the same client as this instance.
	 */
	@Override public boolean equals(Object other){
		if(this == other) return true;
		if(other instanceof SourceConnection){
			return this.id.equals(((SourceConnection)other).id);
		}
		return false;
	}
	
	/**
	 * toString() override.
	 * @return A string potentially useful in debugging.
	 */
	@Override public String toString(){
		return this.getClass().getSimpleName()+"#"+id;
	}
}
