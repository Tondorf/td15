package org.deepserver.td15.network.client;

import org.apache.log4j.Logger;

public class ClientProtocolWorker {
	private static Logger logger = Logger.getLogger(ClientProtocolWorker.class);
	
	private Client client;
	
	public ClientProtocolWorker(Client client) {
		this.client = client;
	}
	
	public void process(String msg) {
		logger.info("received message from server: " + msg);
	}
}
