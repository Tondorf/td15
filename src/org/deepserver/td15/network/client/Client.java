package org.deepserver.td15.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.deepserver.td15.network.server.Server;

public class Client {
	private static Logger logger = Logger.getLogger(Client.class);
	
	private Charset charset;
	private Socket serverSocket;
	private ServerConnection serverConnection;
	
	public Client() {
		if (!Charset.isSupported(Server.CHARSET_NAME)) {
			logger.error("Charset is not supported - fall back to default");
			this.charset = Charset.defaultCharset();
		} else {
			this.charset = Charset.forName(Server.CHARSET_NAME);
		}
	}
	
	public void bindAndStart(String hostname, int port) {
		try {
			serverSocket = new Socket(hostname, port);
			ClientProtocolWorker protocolWorker = new ClientProtocolWorker(this);
			serverConnection = new ServerConnection(serverSocket, protocolWorker, charset);
			serverConnection.getReader().start();
		} catch (UnknownHostException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public void sendServer(String msg) {
		try {
			serverConnection.send(msg);
		} catch (IOException e) {
			logger.error("Could not send message to server (error: " + e.getMessage() + ")");
		}
	}
}
