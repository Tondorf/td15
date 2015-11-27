package org.deepserver.td15.screen;

import org.deepserver.td15.Client;

public class ScreenStartGame extends Screen {
	
	public ScreenStartGame(Client client) {
		super(client);
	}
	
	@Override
	public void escape() {
		client.changeScreen2d(new ScreenStartup(client));
		client.changeScreen3d(new ScreenStartupBackground(client));

		client.focus2d();		
	}
}

