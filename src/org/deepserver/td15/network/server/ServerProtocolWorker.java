package org.deepserver.td15.network.server;

import java.util.SortedSet;
import java.util.TreeSet;

public class ServerProtocolWorker {
	private NetServer server;
	private SortedSet<Integer> authenticatedIDs;

	public ServerProtocolWorker(NetServer server) {
		this.server = server;
		this.authenticatedIDs = new TreeSet<Integer>();
	}

	public synchronized void process(int clientID, byte[] msg) {
		if (server.hostScreen != null) {
			if (authenticatedIDs.contains(clientID)) {
				server.hostScreen.clientActionReceived(clientID, msg);
			} else {
				server.hostScreen.clientAuthenticate(clientID, new String(msg));
				authenticatedIDs.add(clientID);
			}
		}
	}

	public void notifyDeadClient(int clientID) {
		server.killClient(clientID);
	}
}