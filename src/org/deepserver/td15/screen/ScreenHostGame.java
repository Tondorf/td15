package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.network.server.NetServer;

public class ScreenHostGame extends ScreenStartGame {

	public ScreenHostGame(Client client) {
		super(client);
		networked=true;
	
		NetServer gameServer = new NetServer();
		gameServer.bindAndStart(9090);
	}
}
