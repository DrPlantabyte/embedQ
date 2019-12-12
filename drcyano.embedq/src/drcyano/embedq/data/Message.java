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
	
	/**
	 * Package-private constructor.
	 * <b><u><i>DO NOT USE UNLESS YOU CAN GUARENTEE THAT THE BYTEBUFFER CANNOT BE MODIFIED FROM NOW ON!</i></u></b>
	 * @param data A ByteBuffer whose data is assumed to be fixed (effective ownership transferred
	 *               to this <code>Message</code> instance).
	 * @param topic The topic to publish to
	 */
	Message(ByteBuffer data, Topic topic){ // package private
		// WARNING: ByteBuffer is not thread safe!
		// Message class is intended for use in multithreaded environment
		// Java lacks any mechanism for transferring ownership of a byte array/buffer
		// Therefore, public API should use only defensive copies of buffer
		this.topic = topic;
		this.messageBuffer = data.asReadOnlyBuffer();
	}
	
	/**
	 * Creates a byte array output stream that can be converted into a <code>Message</code> instance
	 * when you are done writing to it.
	 * <p>
	 * This method is good for conserving memory if the message is very large and you want to avoid
	 * data duplication in RAM
	 * @param topic Publish topic
	 * @param initialBufferSize Initial size of memory buffer (expandable)
	 * @return An <code>OutputStream</code> that can be converted into a <code>Message</code>
	 */
	public static MessageOutputStream createMessageOutputStream(Topic topic, int initialBufferSize){
		return new MessageOutputStream(initialBufferSize, topic);
	}
	/**
	 * Creates a byte array output stream that can be converted into a <code>Message</code> instance
	 * when you are done writing to it.
	 * <p>
	 * This method is good for conserving memory if the message is very large and you want to avoid
	 * data duplication in RAM
	 * @param topic Publish topic
	 * @return An <code>OutputStream</code> that can be converted into a <code>Message</code>
	 */
	public static MessageOutputStream createMessageOutputStream(Topic topic){
		return new MessageOutputStream( topic);
	}
	
	/**
	 * Creates a one-byte message
	 * @param i message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromByte(byte i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(1);
		bb.put(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	/**
	 * Creates a short-int message
	 * @param i message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromShort(short i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putShort(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	/**
	 * Creates a single integer message
	 * @param i message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromInt(int i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	/**
	 * Creates a long-int message
	 * @param i message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromLong(long i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	/**
	 * Creates a single float message
	 * @param i message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromFloat(float i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putFloat(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	/**
	 * Creates a single double-precision floating point message
	 * @param i message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromDouble(double i, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putDouble(i);
		bb.flip();
		return new Message(bb, topic);
	}
	
	
	/**
	 * Creates a message from a String, encoding it as a UTF-8 byte array.
	 * @param s message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromString(String s, Topic topic) {
		return new Message(UTF8.encode(s), topic);
	}
	
	/**
	 * Creates a message with the provided binary content. A deep-copy will be made, so the provided
	 * byte array can be re-used or discarded without affecting the message.
	 * @param bytes message content
	 * @param topic topic to publish to
	 * @return a new <code>Message</code> instance
	 */
	public static Message fromByteArray(byte[] bytes, Topic topic) {
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes); // moves the insert position of the byte buffer
		bb.flip(); // resets the insert position back to 0, and sets the limit to the old position
		return new Message(bb, topic);
	}
	
	/**
	 * Utility method to more easily get the content of the message back in the original data type.
	 * See {@link #fromByte(byte, Topic)}
	 * @return Returns the message as a byte
	 */
	public byte toByte(){return this.getBytes().get(0);}
	
	/**
	 * Utility method to more easily get the content of the message back in the original data type.
	 * See {@link #fromShort(short, Topic)}
	 * @return Returns the message as a short
	 */
	public short toShort(){return this.getBytes().getShort(0);}
	
	/**
	 * Utility method to more easily get the content of the message back in the original data type.
	 * See {@link #fromInt(int, Topic)}
	 * @return Returns the message as an int
	 */
	public int toInt(){return this.getBytes().getInt(0);}
	
	/**
	 * Utility method to more easily get the content of the message back in the original data type.
	 * See {@link #fromLong(long, Topic)}
	 * @return Returns the message as a long
	 */
	public long toLong(){return this.getBytes().getLong(0);}
	
	/**
	 * Utility method to more easily get the content of the message back in the original data type.
	 * See {@link #fromFloat(float, Topic)}
	 * @return Returns the message as a float
	 */
	public float toFloat(){return this.getBytes().getFloat(0);}
	
	/**
	 * Utility method to more easily get the content of the message back in the original data type.
	 * See {@link #fromDouble(double, Topic)}
	 * @return Returns the message as a double
	 */
	public double toDouble(){return this.getBytes().getDouble(0);}
	
	/**
	 * Decodes the message to a String assuming that the message data is a UTF-8 byte array.
	 * See {@link #fromString(String, Topic)}
	 * @return Returns the message as a String, which may contain placeholder characters in case of
	 * invalid unicode byte combinations int he message data
	 */
	@Override public String toString() {
		return UTF8.decode(this.getBytes()).toString();
	}
	
	/**
	 * Gets the message content as a byte array. The byte array may be a copy of the message buffer
	 * @return A byte-array containing the data
	 */
	public byte[] toByteArray(){return this.getBytes().array();}
	
	/**
	 * Use this method instead of {@link #toString()} when debugging, as it will automatically convert
	 * numbers to text without any prior knowledge of the message construction. This method may
	 * mistake one type of data for another.
	 * @return A human-readable String for debugging purposes
	 */
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
	
	/**
	 * Gets a read-only view of the message content
	 * @return A read-only <code>ByteBuffer</code>
	 */
	public ByteBuffer getBytes() {
		// Note: This ByteBuffer is read-only
		ByteBuffer ref = messageBuffer.duplicate();
		ref.rewind(); // set position to zero
		return ref;
	}
	
	/**
	 * Gets the topic that this message is published to. The Broker handles topic filtering, so this
	 * method is included primarily for logging and debugging purposes.
	 * @return The publication topic that this message was sent to.
	 */
	public Topic getTopic(){
		return topic;
	}
}
