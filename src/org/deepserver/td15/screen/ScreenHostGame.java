package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.network.server.NetServer;

public class ScreenHostGame extends ScreenStartGame {
	public ScreenHostGame(Client client) {
		super(client);
		networked=true;
	
		netServer = new NetServer();
		netServer.bindAndStart(9090);
	}
}
