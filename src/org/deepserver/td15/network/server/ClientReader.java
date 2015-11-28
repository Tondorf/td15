package org.deepserver.td15.network.server;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ClientReader extends Thread {
	private static Logger logger = Logger.getLogger(ClientReader.class);
	private int clientID;
	private DataInputStream in;
	private ServerProtocolWorker protocolWorker;

	public ClientReader(int clientID, DataInputStream in, ServerProtocolWorker protocolWorker) {
		this.clientID = clientID;
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
					protocolWorker.process(clientID, msg);
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				protocolWorker.notifyDeadClient(clientID);
			}
		}
	}
}
