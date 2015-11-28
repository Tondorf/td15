package org.deepserver.td15.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class NetClient {
	private static Logger logger = Logger.getLogger(NetClient.class);
	
	private Socket serverSocket;
	private ServerConnection serverConnection;
	
	public void bindAndStart(String hostname, int port) {
		try {
			serverSocket = new Socket(hostname, port);
			ClientProtocolWorker protocolWorker = new ClientProtocolWorker(this);
			serverConnection = new ServerConnection(serverSocket, protocolWorker);
			serverConnection.getReader().start();
		} catch (UnknownHostException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public void sendServer(byte[] msg) {
		try {
			serverConnection.send(msg);
		} catch (IOException e) {
			logger.error("Could not send message to server (error: " + e.getMessage() + ")");
		}
	}
}
