package org.deepserver.td15.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

public class ClientConnection {
	private static Logger logger = Logger.getLogger(ClientConnection.class);
	
	private int id;
	private Socket socket;
	private ClientReader reader;
	private BufferedReader in;
	private OutputStreamWriter out;
	
	public ClientConnection(int id, Socket socket, ServerProtocolWorker protocolWorker, Charset charset) throws IOException {
		this.id = id;
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.reader = new ClientReader(id, in, protocolWorker);
		this.out = new OutputStreamWriter(socket.getOutputStream(), charset);
	}
	
	public void send(String msg) throws IOException {
		out.write(msg);
		out.flush();
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
