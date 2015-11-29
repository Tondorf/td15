package org.deepserver.td15.screen;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.deepserver.td15.Client;
import org.deepserver.td15.network.server.NetServer;

public class ScreenHostGame extends ScreenStartGame {
	private static Logger logger = Logger.getLogger(ScreenHostGame.class);
	
	public ScreenHostGame(Client client) {
		super(client);
		networked=true;
	
		netServer = new NetServer(this);
		netServer.bindAndStart(9090);
		
	}
	
	public void clientActionReceived(int clientID, byte[] action) {
		logger.info("received message from client #" + clientID);
		
		// TODO: update world
	}
	
	public void clientAuthenticate(int clientID, String username) {
		logger.info("Client #" + clientID + " sent his username: " + username);
		
		byte[] bytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(42L).array();
		netServer.sendClient(clientID, bytes);
	}
}

