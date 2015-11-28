package org.deepserver.td15.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ServerConnection {
	private static Logger logger = Logger.getLogger(ServerConnection.class);
	
	private Socket socket;
	private ServerReader reader;
	private DataInputStream in;
	private DataOutputStream out;
	
	public ServerConnection(Socket socket, ClientProtocolWorker protocolWorker) throws IOException {
		this.socket = socket;
		this.in = new DataInputStream(socket.getInputStream());
		this.reader = new ServerReader(in, protocolWorker);
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
			logger.error("Could not close server socket (error: " + e.getMessage() + ")");
		}
	}

	public Socket getSocket() {
		return socket;
	}
	
	public ServerReader getReader() {
		return reader;
	}
}
