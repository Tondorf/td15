package org.deepserver.td15.network.server;

public class ServerProtocolWorker {
	private NetServer server;

	public ServerProtocolWorker(NetServer server) {
		this.server = server;
	}

	public synchronized void process(int clientID, byte[] msg) {

		// TODO: currently this is an echo-server
		if (server.hostScreen != null)
			server.hostScreen.clientActionReceived(clientID, msg);

		// server.sendClient(clientID, msg);
	}

	public void notifyDeadClient(int clientID) {
		server.killClient(clientID);
	}
}