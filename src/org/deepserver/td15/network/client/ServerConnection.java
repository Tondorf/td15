package org.deepserver.td15.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

public class ServerConnection {
	private static Logger logger = Logger.getLogger(ServerConnection.class);
	
	private Socket socket;
	private ServerReader reader;
	private BufferedReader in;
	private OutputStreamWriter out;
	
	public ServerConnection(Socket socket, ClientProtocolWorker protocolWorker, Charset charset) throws IOException {
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.reader = new ServerReader(in, protocolWorker);
		this.out = new OutputStreamWriter(socket.getOutputStream(), charset);
	}
	
	public void send(String msg) throws IOException {
		out.write(msg + "\r\n");
		out.flush();
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
