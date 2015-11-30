package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.monster.MonsterDefaultCam;
import org.deepserver.td15.monster.MonsterSpace;
import org.joml.Vec2;

public class ScreenStartupBackground extends Screen {

	public ScreenStartupBackground(Client client) {
		super(client);

		MonsterSpace space = new MonsterSpace(world);
		world.add(space);

		world.add(new MonsterDefaultCam(world, new Vec2(0f, 0f)));

	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);
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
