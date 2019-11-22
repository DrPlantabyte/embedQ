package drcyano.embedq.connection.source;

import java.nio.ByteBuffer;

public abstract class SourceConnection {
	
	private final String id;
	
	protected SourceConnection(String idString){
		this.id = idString;
	}
	
	public abstract void sendMessage(ByteBuffer msg);
	
	@Override public int hashCode(){
		return id.hashCode();
	}
	@Override public boolean equals(Object other){
		if(this == other) return true;
		if(other instanceof SourceConnection){
			return this.id.equals(((SourceConnection)other).id);
		}
		return false;
	}
	@Override public String toString(){
		return this.getClass().getSimpleName()+"#"+id;
	}
}
