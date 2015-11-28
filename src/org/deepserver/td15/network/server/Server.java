package org.deepserver.td15.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Server {
	private static Logger logger = Logger.getLogger(Server.class);
	
	private static final String CHARSET_NAME = "UTF-8";
	private static final int MAX_CLIENTS = 1000;
	
	private Charset charset;
	private ServerSocket serverSocket;
	private Doorman doorman;
	private Map<Integer, ClientConnection> clients;
	private ServerProtocolWorker protocolWorker;
	private int clientID;
	
	public Server() {
		if (!Charset.isSupported(CHARSET_NAME)) {
			logger.error("Charset is not supported - fall back to default");
			this.charset = Charset.defaultCharset();
		} else {
			this.charset = Charset.forName(CHARSET_NAME);
		}
		
		this.clients = new HashMap<Integer, ClientConnection>();
		this.protocolWorker = new ServerProtocolWorker(this);
	}

	public void bindAndStart(int port) {
		logger.info("Bind server to port " + port);
		this.charset = getCharset();
		
		try {
			serverSocket = new ServerSocket(port);

			doorman = new Doorman(this);
			doorman.start();
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public void stop() {
		logger.info("Server is going down...");
		final Thread mainThread = Thread.currentThread();
		
		try {
			// do not except new requests, thus killing the doorman
			if (doorman != null && !doorman.isInterrupted()) {
				doorman.interrupt();
			}
			
			// kill all Clients
			for (ClientConnection client : clients.values()) {
				client.kill();
			}
			
			// close server socket
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}

			// wait for other threads to terminate
			mainThread.join();
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public ServerSocket getServerSocket() throws IllegalStateException {
		if (serverSocket.isBound()) {
			return serverSocket;
		} else {
			throw new IllegalStateException("Server socket is currently not bound");
		}
	}
	
	public synchronized void registerNewClient(Socket clientSocket) throws AboveCapacityException, IllegalStateException, IOException {
		if (!serverSocket.isBound()) {
			throw new IllegalAccessError("Server socket is currently not bound");
		} else if (clients.size() > MAX_CLIENTS) {
			throw new AboveCapacityException("Moren than " + MAX_CLIENTS + " clients registered");
		}
		
		int clientID = generateClientID();
		ClientConnection client = new ClientConnection(clientID, clientSocket, protocolWorker, charset);
		client.getReader().start();
		clients.put(clientID, client);
		
		logger.info("Succesfully added new client #" + clientID);
	}
	
	public synchronized int generateClientID() {
		clientID += 1;
		return clientID;
	}
	
	public synchronized void killClient(int clientID) {
		logger.info("Killing client #" + clientID);
		
		ClientConnection client = clients.get(clientID);
		client.kill();
		
		clients.remove(clientID);
	}
	
	public Charset getCharset() {
		return charset;
	}
	
	public void sendClient(int clientID, String msg) {
		ClientConnection client = clients.get(clientID);
		try {
			client.send(msg);
		} catch (IOException e) {
			logger.error("Could not send message to client #" + clientID + " (error: " + e.getMessage() + ")");
		}
	}
	
	public void sendClients(String msg) {
		for (ClientConnection client : clients.values()) {
			try {
				client.send(msg);
			} catch (IOException e) {
				logger.error("Could not send message to client #" + client.getId() + " (error: " + e.getMessage() + ")");
			}
		}
	}
}