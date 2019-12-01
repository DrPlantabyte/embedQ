package drcyano.embedq.connection.source;

import drcyano.embedq.data.Message;

import java.io.IOException;

public abstract class SourceConnection {
	
	private final String id;
	
	protected SourceConnection(String idString){
		this.id = idString;
	}
	
	public abstract void sendMessageReliable(Message msg) throws IOException;
	public abstract void sendMessageFast(Message msg) throws IOException;
	
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
