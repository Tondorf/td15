package org.game.network;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

/*
 * Doorman waits for clients to connect.
 * He accepts them and passes them to the server for registration.
 */
public class Doorman extends Thread {
	private static Logger logger = Logger.getLogger(Doorman.class);
	
	private Server server;
	
	public Doorman(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		logger.info("Waiting for new clients...");
		while (!isInterrupted()) {
			try {
				Socket clientSocket = server.getServerSocket().accept();
				server.registerNewClient(clientSocket);
				
				logger.info("Accepted new client");
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (AboveCapacityException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
