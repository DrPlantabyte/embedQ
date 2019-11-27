package drcyano.embedq.data;

import drcyano.embedq.io.MessageOutputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
