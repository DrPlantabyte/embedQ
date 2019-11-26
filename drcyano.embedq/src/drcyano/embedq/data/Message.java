package drcyano.embedq.data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Message {
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private final ByteBuffer messageBuffer;
	
	protected Message(ByteBuffer data){
		// WARNING: ByteBuffer is not thread safe!
		// Message class is intended for use in multithreaded environment
		// Java lacks any mechanism for transferring ownership of a byte array/buffer
		// Therefore, public API should use only defensive copies of buffer
		this.messageBuffer = data.asReadOnlyBuffer();
	}
	
	public static Message fromString(String s) {
		return new Message(UTF8.encode(s));
	}
	
	public static Message fromByteArray(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes); // moves the insert position of the byte buffer
		bb.flip(); // resets the insert position back to 0, and sets the limit to the old position
		return new Message(bb);
	}
	
	public static Message fromInputStream(InputStream in) throws IOException {
		// Reads until EOF, and then returns
		int i;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while((i = in.read()) >= 0){
			bout.write(i);
		}
		bout.close();
		return new Message(ByteBuffer.wrap(bout.toByteArray()));
	}
	
	public static Message compressedDataMessage(byte[] data) throws IOException {
		try(InputStream in = new ByteArrayInputStream(data)){
			return compressedDataMessage(in);
		}
	}
	public static Message compressedDataMessage(InputStream in) throws IOException {
		// Reads until EOF, and then returns
		int i;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		GZIPOutputStream compressor = new GZIPOutputStream(bout);
		while((i = in.read()) >= 0){
			compressor.write(i);
		}
		compressor.finish();
		compressor.close();
		bout.close();
		return new Message(ByteBuffer.wrap(bout.toByteArray()));
	}
	
	public static InputStream decompressDataMessage(Message m) throws IOException {
		InputStream in = new ByteArrayInputStream(m.getBytes().array());
		GZIPInputStream decompressor = new GZIPInputStream(in);
		return decompressor;
	}
	
	public static Message fromInteger(int i) {
		byte[] data = new byte[4];
		int r = data.length;
		for(int n = 0; n < data.length; n++){
			r--;
			// big-endian (aka network byte order)
			data[n] = (byte)((i >> (8 * r)) & 0xFF);
		}
		return fromByteArray(data);
	}
	
	public static String toString(Message m) {
		return UTF8.decode(m.getBytes()).toString();
	}
	
	public ByteBuffer getBytes() {
		// Note: ByteBuffer is read-only
		return messageBuffer.duplicate();
	}
}
