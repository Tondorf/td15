package org.deepserver.td15.network.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.deepserver.td15.screen.Screen;

public class NetClient {
	private static Logger logger = Logger.getLogger(NetClient.class);

	private Socket serverSocket;
	private ServerConnection serverConnection;
	
	private Screen screen;
	
	public NetClient(Screen screen) {
		this.screen = screen;
	}

	public void bindAndStart(String hostname, int port) {
		try {
			serverSocket = new Socket(hostname, port);
			ClientProtocolWorker protocolWorker = new ClientProtocolWorker(this, screen);
			serverConnection = new ServerConnection(serverSocket, protocolWorker);
		} catch (UnknownHostException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/*
	 * this is a dirty hack: the server asserts the first bytearray from a
	 * client to be the clients username, i.e. handshakes have to be called only
	 * once and before sending anything else... (that was not my idea--I am the
	 * realizing coding monkey--find someone else to blame)
	 */
	public long handshake(String username) {
		try {
			serverConnection.send(username.getBytes());

			// wait for response and activate the ServerReader AFTERwards
			DataInputStream in = new DataInputStream(serverSocket.getInputStream());
			int length = in.readInt();
			byte[] msg = new byte[length];
			do {
				in.readFully(msg);
			} while (length == 0);

			// now start ServerReader
			serverConnection.getReader().start();
			
			// first byte is the most significant byte
			long id = 0L;
			for (int i = 0; i < msg.length; i++) {
				id = (id << 8) + (msg[i] & 0xff);
			}
			return id;
		} catch (IOException e) {
			logger.error("Critical error: client could NOT send his username the sever is still waiting for");
		}

		return -1L;
	}

	public void sendServer(byte[] msg) {
		try {
			serverConnection.send(msg);
		} catch (IOException e) {
			logger.error("Could not send message to server (error: " + e.getMessage() + ")");
		}
	}
}
