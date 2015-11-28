package org.deepserver.td15.network.server;

public class TestServer {
	public static void main(String[] args) {
		NetServer server = new NetServer(null);
		server.bindAndStart(9090);
	}
}
