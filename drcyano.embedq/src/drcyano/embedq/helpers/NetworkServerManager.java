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
	
	private final AtomicBoolean runControl = new AtomicBoolean(false);
	
	public NetworkServerManager(){
		throw new UnsupportedOperationException("not implemented yet");
		
	}
	
	@Override
	public void close() throws IOException {
		runControl.set(false);
		
		throw new UnsupportedOperationException("not implemented yet");
	
	}
}
