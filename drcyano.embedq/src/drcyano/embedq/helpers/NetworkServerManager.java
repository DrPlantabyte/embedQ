package drcyano.embedq.helpers;

import drcyano.embedq.protocol.Protocol;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkServerManager implements Closeable {
	private static final int networkPayloadSizeLimit = 0x10000;
	private final AtomicBoolean runControl = new AtomicBoolean(false);
	private final Map<Socket, InputStream> tcpClientInputs = Collections.synchronizedMap(new HashMap<>());
	private final Map<Socket, OutputStream> tcpClientOutputs = Collections.synchronizedMap(new HashMap<>());
	private final Map<Socket, ByteBuffer> tcpClientbuffers = Collections.synchronizedMap(new HashMap<>());
	
	private void tcpListenLoop(){
		while(runControl.get()){
			for(Map.Entry<Socket, InputStream> client : tcpClientInputs.entrySet()){
				Socket sock  = client.getKey();
				if(sock.isClosed()){
					dropSocket(sock);
					continue;
				}
				readSocketBlock:
				{
					InputStream in = client.getValue();
					ByteBuffer bb = tcpClientbuffers
					while (in.available() > 0) {
						if(bb.position() >= networkPayloadSizeLimit){
							// payload too big (invalid)
							oversizedPayloadError(sock);
							clearBuffer(sock);
							break readSocketBlock;
						}
						int bin = in.read();
						if (bin < 0) {
							// end of stream
							closeClient(sock);
							break readSocketBlock;
						}
						bb.put((byte) bin);
						boolean done = Protocol.isCompletePayload(bb);
					}
				}
			}
		}
	}
	
	private void clearBuffer(Socket sock) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	private void oversizedPayloadError(Socket sock) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	private void closeClient(Socket sock) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	private void dropSocket(Socket sock) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	@Override
	public void close() throws IOException {
		runControl.set(false);
		
		throw new UnsupportedOperationException("not implemented yet");
	
	}
}
