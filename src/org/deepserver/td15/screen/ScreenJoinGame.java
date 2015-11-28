package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.network.client.NetClient;

public class ScreenJoinGame extends ScreenStartGame {

	public ScreenJoinGame(Client client) {
		super(client);
		networked=true;
		
		netClient = new NetClient();
		netClient.bindAndStart("127.0.0.1", 9090);
	}
}
