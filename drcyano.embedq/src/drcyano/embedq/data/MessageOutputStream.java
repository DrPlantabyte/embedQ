package drcyano.embedq.data;

import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This class lets you convert a data stream into a message to publish on a topic.
 */
public class MessageOutputStream extends ByteArrayOutputStream {
	// this class intended for use when memory is limited (ie 25% < data < 100% available mem)
	
	private final Topic topic;
	private Message msg = null; // is set to a value on close()
	
	/**
	 * Create a new <code>MessageOutputStream</code> with a pre-allocated (but still expandable)
	 * buffer for receiving data.
	 * @param initialBufferSize Initial buffer size
	 * @param topic The topic to publish to
	 */
	public MessageOutputStream(int initialBufferSize, Topic topic){
		super(initialBufferSize);
		this.topic = topic;
	}
	/**
	 * Create a new <code>MessageOutputStream</code> with an expandable
	 * buffer for receiving data.
	 * @param topic The topic to publish to
	 */
	public MessageOutputStream(Topic topic){
		super(64);
		this.topic = topic;
	}
	/**
	 * Closes the stream. Once closed, no new data can be received
	 */
	@Override public synchronized void close(){
		try {
			super.close();
			
			// ByteArrayOutputStream.close() does nothing and never throws,
			// according to docs and sourcecode
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(msg == null) {
			ByteBuffer data = ByteBuffer.wrap(super.buf, 0, super.count);
			msg = new Message(data, topic);
		} // else already closed
		
	}
	/**
	 * Writes the specified byte to this {@code ByteArrayOutputStream}.
	 *
	 * @param   b   the byte to be written.
	 * @throws RuntimeException Thrown if you try to write and this stream is already closed
	 */
	@Override public synchronized void write(int b){
		if(msg != null){
			// stream closed!
			try {
				throw new IOException("Cannot write to closed "+this.getClass().getSimpleName());
			} catch (IOException e) {
				throw new RuntimeException("Unhandled runtime exception",e);
			}
		}
		super.write(b);
	}
	/**
	 * Writes {@code len} bytes from the specified byte array
	 * starting at offset {@code off} to this {@code ByteArrayOutputStream}.
	 *
	 * @param   b     the data.
	 * @param   off   the start offset in the data.
	 * @param   len   the number of bytes to write.
	 * @throws  NullPointerException if {@code b} is {@code null}.
	 * @throws  IndexOutOfBoundsException if {@code off} is negative,
	 * {@code len} is negative, or {@code len} is greater than
	 * {@code b.length - off}
	 */
	@Override public synchronized void write(byte b[], int off, int len) {
		if(msg == null) super.write(b, off, len);
	}
	/**
	 * Writes the complete contents of the specified byte array
	 * to this {@code ByteArrayOutputStream}.
	 *
	 * This method is equivalent to {@link #write(byte[],int,int)
	 * write(b, 0, b.length)}.
	 *
	 * @param   b     the data.
	 * @throws  NullPointerException if {@code b} is {@code null}.
	 */
	@Override public void writeBytes(byte b[]){
		if(msg == null) super.writeBytes(b);
	}
	/**
	 * Resets the {@code count} field of this {@code ByteArrayOutputStream}
	 * to zero, so that all currently accumulated output in the
	 * output stream is discarded. The output stream can be used again,
	 * reusing the already allocated buffer space.
	 *
	 * See    <code>java.io.ByteArrayInputStream.count</code>
	 */
	@Override public synchronized void reset(){
		msg = null;
		super.reset();
	}
	
	/**
	 * Closes the stream if it has not already been closed, and wraps the data received by this
	 * stream in a <code>Message</code> instance.
	 * @return A <code>Message</code> holding the written data as its message content
	 */
	public Message toMessage(){
		if (msg == null) {
			this.close();
		}
		return msg;
	}
}
