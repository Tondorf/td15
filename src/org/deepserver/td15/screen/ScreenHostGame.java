package org.deepserver.td15.screen;

import org.deepserver.td15.Client;

public class ScreenHostGame extends ScreenStartGame {

	public ScreenHostGame(Client client) {
		super(client);
		networked=true;
		// start server
	}

}
