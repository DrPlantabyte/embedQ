package drcyano.embedq.imp;

import drcyano.embedq.client.Subscriber;
import drcyano.embedq.connection.source.SourceConnection;
import drcyano.embedq.data.Message;

import java.util.concurrent.atomic.AtomicInteger;

public class IntraprocessSourceConnection extends SourceConnection {
	private final Subscriber src;
	private static final AtomicInteger sequencer = new AtomicInteger(0);
	public IntraprocessSourceConnection( Subscriber sub) {
		super(String.format("%s:%s", IntraprocessSourceConnection.class.getSimpleName(), sequencer.incrementAndGet()));
		this.src = sub;
	}
	
	@Override
	public void sendMessage(Message msg) {
		src.receiveMessage(msg);
	}
	
	@Override public int hashCode(){
		return src.hashCode();
	}
	
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
