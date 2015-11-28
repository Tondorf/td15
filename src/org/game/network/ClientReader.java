package org.game.network;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReader extends Thread {
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
		try {
			String msg = in.readLine();
			if (msg != null) {
				protocolWorker.process(clientID, msg);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
