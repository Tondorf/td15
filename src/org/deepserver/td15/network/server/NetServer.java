package org.deepserver.td15.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class NetServer {
	private static Logger logger = Logger.getLogger(NetServer.class);
	
	private static final int MAX_CLIENTS = 1000;
	
	private ServerSocket serverSocket;
	private Doorman doorman;
	private Map<Integer, ClientConnection> clientConnections;
	private ServerProtocolWorker protocolWorker;
	private int clientID;
	
	public NetServer() {
		this.clientConnections = new HashMap<Integer, ClientConnection>();
		this.protocolWorker = new ServerProtocolWorker(this);
	}

	public void bindAndStart(int port) {
		logger.info("Bind server to port " + port);
		
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
			for (ClientConnection client : clientConnections.values()) {
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
		} else if (clientConnections.size() > MAX_CLIENTS) {
			throw new AboveCapacityException("Moren than " + MAX_CLIENTS + " clients registered");
		}
		
		int clientID = generateClientID();
		ClientConnection clientConnection = new ClientConnection(clientID, clientSocket, protocolWorker);
		clientConnection.getReader().start();
		clientConnections.put(clientID, clientConnection);
		
		logger.info("Succesfully added new client #" + clientID);
	}
	
	public synchronized int generateClientID() {
		clientID += 1;
		return clientID;
	}
	
	public synchronized void killClient(int clientID) {
		logger.info("Killing client #" + clientID);
		
		ClientConnection client = clientConnections.get(clientID);
		client.kill();
		
		clientConnections.remove(clientID);
	}
	
	public void sendClient(int clientID, byte[] msg) {
		ClientConnection clientConnection = clientConnections.get(clientID);
		sendClient(clientConnection, msg);
	}
	
	public void sendClients(byte[] msg) {
		for (ClientConnection clientConnection : clientConnections.values()) {
			sendClient(clientConnection, msg);
		}
	}
	
	private void sendClient(ClientConnection clientConnection, byte[] msg) {
		try {
			clientConnection.send(msg);
		} catch (IOException e) {
			logger.error("Could not send message to client #" + clientConnection.getId() + " (error: " + e.getMessage() + ")");
		}
	}
}