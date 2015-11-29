package org.deepserver.td15.screen;

import java.nio.FloatBuffer;

import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.deepserver.td15.network.client.NetClient;
import org.deepserver.td15.network.server.NetServer;
import org.deepserver.td15.sound.AudioManager;
import org.lwjgl.BufferUtils;

public abstract class Screen {
	
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
	
	public void actionFromClient(int clientID, InputStatus is) {
		if (netServer != null) {
			// TODO: update world
		}
	}
	
	public void updateFromServer(/* unzipped world */) {
		if (netClient != null) {
			// TODO: update world
		}
	}
	
	public void action(double delta, InputStatus is) {
		if (is.escapeEvent) { 
			is.escapeEvent=false;

			escape();
		}
		
		if (networked && netClient != null) {
			
			// TODO: serialize InputStatus and send it
			// netClient.sendServer(is.zip);
		}
				
		world.action(delta,is);
		
		if (networked && netServer != null) {
			// TODO: serialize me the world
			// netServer.sendClients(world.zip);
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
