package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.network.server.Server;

public class ScreenHostGame extends ScreenStartGame {

	public ScreenHostGame(Client client) {
		super(client);
		networked=true;
	
		Server gameServer = new Server();
		gameServer.bindAndStart(9090);
	}
}
