package org.game.network.server;

public class TestServer {
	public static void main(String[] args) {
		Server server = new Server();
		server.bindAndStart(9090);
	}
}
