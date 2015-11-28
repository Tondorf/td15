package org.deepserver.td15.network.server;

public class ServerProtocolWorker {
	private Server server;
	
	public ServerProtocolWorker(Server server) {
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