package org.deepserver.td15.network.server;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ClientReader extends Thread {
	private static Logger logger = Logger.getLogger(ClientReader.class);
	private int clientID;
	private BufferedReader in;
	private ProtocolWorker protocolWorker;

	public ClientReader(int clientID, BufferedReader in, ProtocolWorker protocolWorker) {
		this.clientID = clientID;
		this.in = in;
		this.protocolWorker = protocolWorker;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				String msg = in.readLine();
				if (msg != null) {
					protocolWorker.process(clientID, msg);
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				protocolWorker.notifyDeadClient(clientID);
			}
		}
	}
}
