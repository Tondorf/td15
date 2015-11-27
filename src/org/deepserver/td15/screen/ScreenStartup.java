package org.deepserver.td15.screen;

import org.deepserver.td15.Client;


public class ScreenStartup extends ScreenMenu {
	
	public ScreenStartup(Client client) {
		super(client);
//		add("Start Game",() -> client.changeScreen2d(new ScreeenPlayerSelect(client)));
		
//		add("Resolution",() -> client.pushScreen2d(new ScreenResolution(client)));
		add("Quit",      () -> client.closeGame());
		add("Really Quit",      () -> client.closeGame());
		init(0);
	}	
}

