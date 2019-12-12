package drcyano.embedq.data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The <code>Message</code> class transports published messages
 */
public class Message {
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	
	private final ByteBuffer messageBuffer;
	private final Topic topic;
	
	Message(ByteBuffer data, Topic topic){ // package private
		// WARNING: ByteBuffer is not thread safe!
		// Message class is intended for use in multithreaded environment
		// Java lacks any mechanism for transferring ownership of a byte array/buffer
		// Therefore, public API should use only defensive copies of buffer
		this.topic = topic;
		this.messageBuffer = data.asReadOnlyBuffer();
	}
	
	
	public static MessageOutputStream createMessageOutputStream(Topic topic){
		return new MessageOutputStream(topic);
	}
	
	public static Writer createMessageWriter(Topic topic){
		return new OutputStreamWriter(createMessageOutputStream(topic), UTF8);
	}
	
	
	public static Message fromByte(byte i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(1);
		bb.put(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	public static Message fromShort(short i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putShort(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	public static Message fromInt(int i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	public static Message fromLong(long i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	public static Message fromFloat(float i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putFloat(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	public static Message fromDouble(double i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putDouble(i);
		bb.flip();
		return new Message(bb, topic);
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
	
	public byte toByte(){return this.getBytes().get();}
	public short toShort(){return this.getBytes().getShort();}
	public int toInt(){return this.getBytes().getInt();}
	public long toLong(){return this.getBytes().getLong();}
	public float toFloat(){return this.getBytes().getFloat();}
	public double toDouble(){return this.getBytes().getDouble();}
	@Override public String toString() {
		return UTF8.decode(this.getBytes()).toString();
	}
	public byte[] toByteArray(){return this.getBytes().array();}
	
	public String heuristicToString() {
		ByteBuffer bytes = getBytes();
		int size = bytes.limit();
		if(size > 1024){
			// big data
			return String.format("<%s KB>",size >> 10);
		}
		if(size == 1) return String.valueOf(bytes.get(0)); // probably a boolean, could be a tiny int
		if(bytes.get(0) < 0x1F){
			// invalid unicode
			// try integers
			if(bytes.get(0) == 0) {
				if (size == 2) return String.valueOf(bytes.getShort(0));
				if (size == 4) return String.valueOf(bytes.getInt(0));
				if (size == 8) return String.valueOf(bytes.getLong(0));
			}
			// try floating points, which are usually not exceptionally big nor small
			if(size == 4){
				if(bytes.get(0) == 0) return String.valueOf(bytes.getInt(0));
				float test = bytes.getFloat(0);
				if(
						(test >= -1000 && test < -0.01)
								|| (test > 0.01 && test <= 1000)
				) return String.valueOf(test);
			}
			if(size == 8) {
				if(bytes.get(0) == 0) return String.valueOf(bytes.getLong(0));
				double test = bytes.getDouble(0);
				if(
						(test >= -10000 && test < -0.001)
								|| (test > 0.001 && test <= 10000)
				) return String.valueOf(test);
			}
			// just write the hexadecimal byte array value
			StringBuilder sb = new StringBuilder("0x");
			for(int i = 0; i < size; i++){
				sb.append(' ');
				int nibble = (bytes.get(i) >> 4) & 0x0F;
				sb.append(Integer.toHexString(nibble));
				nibble = (bytes.get(i)) & 0x0F;
				sb.append(Integer.toHexString(nibble));
			}
			return sb.toString();
		}
		
		return UTF8.decode(this.getBytes()).toString();
	}
	
	public ByteBuffer getBytes() {
		// Note: This ByteBuffer is read-only
		ByteBuffer ref = messageBuffer.duplicate();
		ref.rewind(); // set position to zero
		return ref;
	}
	
	public Topic getTopic(){
		return topic;
	}
}
