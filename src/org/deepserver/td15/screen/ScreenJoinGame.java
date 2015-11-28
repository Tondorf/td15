package org.deepserver.td15.screen;

import org.deepserver.td15.Client;

public class ScreenJoinGame extends ScreenStartGame {

	public ScreenJoinGame(Client client) {
		super(client);
		networked=true;
		// connect to server
	}

}
