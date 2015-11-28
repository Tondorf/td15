package org.deepserver.td15.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ClientConnection {
	private static Logger logger = Logger.getLogger(ClientConnection.class);
	
	private int id;
	private Socket socket;
	private ClientReader reader;
	private DataInputStream in;
	private DataOutputStream out;
	
	public ClientConnection(int id, Socket socket, ServerProtocolWorker protocolWorker) throws IOException {
		this.id = id;
		this.socket = socket;
		this.in = new DataInputStream(socket.getInputStream());
		this.reader = new ClientReader(id, in, protocolWorker);
		this.out = new DataOutputStream(socket.getOutputStream());
	}
	
	public void send(byte[] msg) throws IOException {
		out.writeInt(msg.length);
		out.write(msg);
	}

	public void kill() {
		if (!reader.isInterrupted()) {
			reader.interrupt();
		}
		try {
			socket.close();
		} catch (IOException e) {
			logger.error("Could not close client socket #" + id + " (error: " + e.getMessage() + ")");
		}
	}
	
	public int getId() {
		return id;
	}

	public Socket getSocket() {
		return socket;
	}
	
	public ClientReader getReader() {
		return reader;
	}
}
