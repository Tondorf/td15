package org.game.network;

public class ProtocolWorker {
	private Server server;
	
	public ProtocolWorker(Server server) {
		this.server = server;
	}
	
	public void process(int id, String msg) {
		
		// TODO: currently this is an echo-server
		server.sendClient(id, msg);
		server.sendClients("CC: " + msg);
	}
}