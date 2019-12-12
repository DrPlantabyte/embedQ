package drcyano.embedq.protocol;

import drcyano.embedq.data.Message;
import drcyano.embedq.data.Topic;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class Protocol {
	
	private final static int headerSize = 4+8+4;
	
	public static long checksum(ByteBuffer buffer){
		CRC32 checksummer = new CRC32();
		checksummer.update(buffer);
		return checksummer.getValue();
	}
	
	public static ByteBuffer serializeMessageToBuffer(Message msg){
		final ByteBuffer topic = msg.getTopic().toUTF8Bytes();
		final ByteBuffer content = msg.getBytes();
		final long topicChecksum = checksum(topic); // checksum the topic string to quickly reject erroneous network traffic
		final int totalSize = 4+8+topic.limit()+4+content.limit();
		final ByteBuffer serialized = ByteBuffer.allocate(totalSize);
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public static Message deserializeMessageFromBuffer(ByteBuffer buffer){
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public static PayloadType decodePayloadType(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public static Message decodePayloadMessage(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public static Topic decodeSubscriberTopic(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public static QualityOfService decodeQoS(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public static int decodeUDPPort(ByteBuffer buffer) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public static boolean isCompletePayload(ByteBuffer bb) {
		// TODO: sanity check and see if the size of the content is equal to what is specified in the header
		// this method is called after every byte is read, so keep it simple
		int totalSize = bb.position();
		if(totalSize < headerSize) return false;
		throw new UnsupportedOperationException("not implemented yet");
	}
}
