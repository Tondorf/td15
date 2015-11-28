package org.deepserver.td15.network.client;

public class TestClient {
	public static void main(String[] args) {
		NetClient client = new NetClient();
		client.bindAndStart("127.0.0.1", 9090);
		client.sendServer("foobar".getBytes());
	}
}
