package org.deepserver.td15.network.client;

public class TestClient {
	public static void main(String[] args) {
		Client client = new Client();
		client.bindAndStart("127.0.0.1", 9090);
		client.sendServer("foobar".getBytes());
	}
}
