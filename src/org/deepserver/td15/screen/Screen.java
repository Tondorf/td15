package org.deepserver.td15.screen;

import java.nio.FloatBuffer;

import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.lwjgl.BufferUtils;

public abstract class Screen {
	
	public boolean shade3d=true;

	public Client client;
	protected World world;
	
    protected FloatBuffer fb = BufferUtils.createFloatBuffer(16); 

	public Screen(Client client) {
		this.client=client;
		world = new World(this);
	}
		
	public void action(double delta, InputStatus is) {
		if (is.escapeEvent) { 
			is.escapeEvent=false;
			escape();
		}
		
		world.action(delta,is);
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
