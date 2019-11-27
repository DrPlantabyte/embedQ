package drcyano.embedq.io;

import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class MessageOutputStream extends ByteArrayOutputStream {
	// this class intended for use when memory is limited (ie 25% < data < 100% available mem)
	
	private final Topic topic;
	private Message msg = null; // is set to a value on close()
	
	public MessageOutputStream(int initialBufferSize, Topic topic){
		super(initialBufferSize);
		this.topic = topic;
	}
	public MessageOutputStream(Topic topic){
		super(64);
		this.topic = topic;
	}
	
	@Override public synchronized void close() throws IOException {
		super.close();
		// ByteArrayOutputStream.close() does nothing and never throws,
		// according to docs and sourcecode
		
		ByteBuffer data = ByteBuffer.wrap(super.buf, 0, super.count);
	}
	
	@Override public synchronized void write(int b){
		if(msg != null){
			// stream closed!
//			try {
//				throw new IOException("Cannot write to closed "+this.getClass().getSimpleName());
//			} catch (IOException e) {
//				throw new RuntimeException("Unhandled runtime exception",e);
//			}
			// just do nothing
			return;
		}
		super.write(b);
	}
	
	@Override public synchronized void write(byte b[], int off, int len) {
		if(msg == null) super.write(b, off, len);
	}
	@Override public void writeBytes(byte b[]){
		if(msg == null) super.writeBytes(b);
	}
	
	@Override public synchronized void reset(){
		msg = null;
		super.reset();
	}
	
	public Message toMessage(){
		if (msg == null) {
			try{
				this.close(); // doesn't actually throw IOException
			} catch (IOException e) {
				throw new RuntimeException("Unexpected error", e);
			}
		}
		return msg;
	}
}
