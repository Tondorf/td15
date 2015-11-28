package org.deepserver.td15.network.client;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ServerReader extends Thread {
	private static Logger logger = Logger.getLogger(ServerReader.class);
	
	private BufferedReader in;
	private ClientProtocolWorker protocolWorker;

	public ServerReader(BufferedReader in, ClientProtocolWorker protocolWorker) {
		this.in = in;
		this.protocolWorker = protocolWorker;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				String msg = in.readLine();
				if (msg != null) {
					protocolWorker.process(msg);
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
