package org.deepserver.td15.screen;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.apache.log4j.Logger;
import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.deepserver.td15.monster.Monster;
import org.deepserver.td15.monster.MonsterPlayer;
import org.deepserver.td15.network.client.NetClient;
import org.deepserver.td15.network.server.NetServer;
import org.deepserver.td15.sound.AudioManager;
import org.lwjgl.BufferUtils;

public abstract class Screen {
	private static Logger logger = Logger.getLogger(Screen.class);
	
	public boolean shade3d=true;
	
	public boolean networked=false;
	protected NetServer netServer = null;
	protected NetClient netClient = null;

	public Client client;
	protected World world;
	public AudioManager audio;
	
    protected FloatBuffer fb = BufferUtils.createFloatBuffer(16); 
    
    protected long clientID = -1L;

	public Screen(Client client) {
		this.client=client;
		world = new World(this);
		this.audio = AudioManager.getInstance();
	}
	
	public void newWorldFromServer(byte[] compressedWorld) {
		logger.info("client received new world");
		// TODO: update world
	}
	
	public void action(double delta, InputStatus is) {
		if (is.escapeEvent) { 
			is.escapeEvent=false;

			escape();
		}
		
		if (networked && netClient != null) {
			MonsterPlayer m = (MonsterPlayer) world.monsters.get(clientID);
			try {
				netClient.sendServer(m.toBytes());
			} catch (IOException e) {
				logger.error("Client could not send his monster (error: " + e.getMessage() + ")");
			}
		}
				
		world.action(delta,is);
		
		if (networked && netServer != null) {
			try {
				netClient.sendServer(world.toBytes());
			} catch (IOException e) {
				logger.error("Server could not send world (error: " + e.getMessage() + ")");
			}
		}
	}
	
	public void draw() {
		world.draw();
	}
	
	public void drawCamera() {
		world.drawCamera();
	}
	
	public void escape() {
		
	}
}
