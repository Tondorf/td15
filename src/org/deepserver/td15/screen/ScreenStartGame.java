package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.monster.Monster;
import org.deepserver.td15.monster.MonsterEnemy;
import org.deepserver.td15.monster.MonsterPlayer;
import org.deepserver.td15.monster.MonsterSprite;
import org.joml.Vec2;

public class ScreenStartGame extends Screen {
	public final int monsterCount=200;
	
	public ScreenStartGame(Client client) {
		super(client);

		MonsterPlayer player=new MonsterPlayer(world);
		player.world = world;
		player.position = new Vec2(0f, 0f);
		world.add(player);

		MonsterEnemy enemy = new MonsterEnemy(world, new Vec2(1f,1f));
		world.add(enemy);
		
		
		for (int i=0;i<monsterCount;i++) {
				Monster q = new MonsterSprite(world, new Vec2(
						((float) Math.random() - 0.5f) * 2 * world.rockRadius,
						((float) Math.random() - 0.5f) * 2 * world.rockRadius));
						
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
