package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;


public class ScreenEmpty extends Screen {

	public ScreenEmpty(Client client) {
		super(client);
		shade3d=false;
	}

	@Override
	public void action(double delta,InputStatus is) {
		super.action(delta,is);
	}

	@Override
	public void draw() {
		super.draw();
	}
	
	@Override
	public void escape() {
		super.escape();
	}
}
