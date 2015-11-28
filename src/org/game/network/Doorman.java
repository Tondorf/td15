package org.game.network;

import java.io.IOException;
import java.net.Socket;

/*
 * Doorman waits for clients to connect.
 * He accepts them and passes them to the server for registration.
 */
public class Doorman extends Thread {
	private Server server;
	
	public Doorman(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket clientSocket = server.getServerSocket().accept();
				server.registerNewClient(clientSocket);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			} catch (AboveCapacityException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
