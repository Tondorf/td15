package org.deepserver.td15.network.client;

import org.apache.log4j.Logger;
import org.deepserver.td15.screen.Screen;

public class ClientProtocolWorker {
	private static Logger logger = Logger.getLogger(ClientProtocolWorker.class);
	
	private NetClient client;
	private Screen screen;
	
	public ClientProtocolWorker(NetClient client, Screen screen) {
		this.client = client;
		this.screen = screen;
	}
	
	public synchronized void process(byte[] msg) {
		screen.newWorldFromServer(msg);
	}
}
