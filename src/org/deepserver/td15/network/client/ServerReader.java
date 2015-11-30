package org.deepserver.td15.network.client;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ServerReader extends Thread {
	private static Logger logger = Logger.getLogger(ServerReader.class);

	private DataInputStream in;
	private ClientProtocolWorker protocolWorker;

	public ServerReader(DataInputStream in, ClientProtocolWorker protocolWorker) {
		this.in = in;
		this.protocolWorker = protocolWorker;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				int length = in.readInt();
				byte[] msg = new byte[length];
				in.readFully(msg);
				if (length > 0) {
					protocolWorker.process(msg);
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
}
