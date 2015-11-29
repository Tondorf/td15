package org.deepserver.td15.screen;

import org.apache.log4j.Logger;
import org.deepserver.td15.Client;
import org.deepserver.td15.Main;
import org.deepserver.td15.network.client.NetClient;

public class ScreenJoinGame extends ScreenStartGame {
	private static Logger logger = Logger.getLogger(ScreenJoinGame.class);
	public ScreenJoinGame(Client client) {
		super(client);
		networked=true;
		
		netClient = new NetClient(this);
		netClient.bindAndStart(Main.serverIP, 9090);
		clientID = netClient.handshake("Pimmelbirne_" + System.currentTimeMillis());
		logger.info("Server thinks my name is stupid and will call me " + clientID + " instead:/");
	}
	
	@Override
	protected void init() {

	}
}
