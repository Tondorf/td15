package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.monster.MonsterDefaultCam;
import org.deepserver.td15.monster.MonsterQuad;
import org.joml.Vec3;


public class ScreenStartupBackground extends Screen {

	public ScreenStartupBackground(Client client) {
		super(client);
		
		int cnt=30;
		
		for (int x=-cnt;x<cnt;x++)
			for (int y=-cnt;y<cnt;y++) {
				float h=0;
				do {
					h=(float)(Math.random()*17)-5;
				} while (h>=1 && h<2);
				
				world.add(new MonsterQuad(world,new Vec3((float)x*5,(float)h,(float)y*5)));
			}
		
		world.add(new MonsterDefaultCam(world, new Vec3(0f,0f,0f)));
		
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
