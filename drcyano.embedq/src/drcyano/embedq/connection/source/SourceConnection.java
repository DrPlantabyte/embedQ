package drcyano.embedq.connection.source;

import java.nio.ByteBuffer;

public abstract class SourceConnection {
	public abstract void sendMessage(ByteBuffer msg);
}
