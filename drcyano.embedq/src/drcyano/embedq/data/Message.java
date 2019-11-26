package drcyano.embedq.data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Message {
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	
	private final ByteBuffer messageBuffer;
	private final Topic topic;
	
	protected Message(ByteBuffer data, Topic topic){
		// WARNING: ByteBuffer is not thread safe!
		// Message class is intended for use in multithreaded environment
		// Java lacks any mechanism for transferring ownership of a byte array/buffer
		// Therefore, public API should use only defensive copies of buffer
		this.topic = topic;
		this.messageBuffer = data.asReadOnlyBuffer();
	}
	
	public static Message fromString(String s, Topic topic) {
		return new Message(UTF8.encode(s), topic);
	}
	
	public static Message fromByteArray(byte[] bytes, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes); // moves the insert position of the byte buffer
		bb.flip(); // resets the insert position back to 0, and sets the limit to the old position
		return new Message(bb, topic);
	}
	
	public static Message fromInputStream(InputStream in, Topic topic) throws IOException {
		// Reads until EOF, and then returns
		int i;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while((i = in.read()) >= 0){
			bout.write(i);
		}
		bout.close();
		return new Message(ByteBuffer.wrap(bout.toByteArray()), topic);
	}
	
	
	public static Message fromInteger(int i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	public static String toString(Message m) {
		return UTF8.decode(m.getBytes()).toString();
	}
	
	public ByteBuffer getBytes() {
		// Note: ByteBuffer is read-only
		return messageBuffer.duplicate();
	}
	
	public Topic getTopic(){
		return topic;
	}
}
