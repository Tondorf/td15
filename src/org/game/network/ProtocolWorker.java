package org.game.network;

public class ProtocolWorker {
	private Server server;
	
	public ProtocolWorker(Server server) {
		this.server = server;
	}
	
	public void process(int clientID, String msg) {
		
		// TODO: currently this is an echo-server
		server.sendClient(clientID, msg);
		server.sendClients("CC: " + msg);
	}
	
	public void notifyDeadClient(int clientID) {
		server.killClient(clientID);
	}
}