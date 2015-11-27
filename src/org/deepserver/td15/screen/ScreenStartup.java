package org.deepserver.td15.screen;

import org.deepserver.td15.Client;


public class ScreenStartup extends ScreenMenu {
	
	public ScreenStartup(Client client) {
		super(client);
		add("Start Game",() -> {
			client.changeScreen2d(new ScreenEmpty(client));
			client.changeScreen3d(new ScreenStartGame(client));
			client.focus3d();
			});		
		add("Quit",      () -> client.closeGame());
		init(0);
	}	
}

