package org.deepserver.td15.network.client;

import org.apache.log4j.Logger;

public class ClientProtocolWorker {
	private static Logger logger = Logger.getLogger(ClientProtocolWorker.class);
	
	private NetClient client;
	
	public ClientProtocolWorker(NetClient client) {
		this.client = client;
	}
	
	public void process(byte[] msg) {
		logger.info("received message from server: " + new String(msg));
	}
}
