package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.monster.Monster;
import org.deepserver.td15.monster.MonsterPlayer;
import org.deepserver.td15.monster.MonsterQuad;
import org.joml.Vec3;

public class ScreenStartGame extends Screen {

	public ScreenStartGame(Client client) {
		super(client);

		int cnt = 30;
		
		MonsterPlayer player=new MonsterPlayer(world);

		player.world = world;
		player.position = new Vec3(0f, 10f, 0f);
		world.add(player);

		for (int x = -cnt; x < cnt; x++)
			for (int y = -cnt; y < cnt; y++) {
				Monster q = new MonsterQuad(world, new Vec3(
						((float) Math.random() - 0.5f) * 300f,
						((float) Math.random() - 0.5f) * 300f,
						0.0f));
				world.add(q);
			}

	}

	@Override
	public void escape() {
		client.changeScreen2d(new ScreenStartup(client));
		client.changeScreen3d(new ScreenStartupBackground(client));

		client.focus2d();
	}
}
