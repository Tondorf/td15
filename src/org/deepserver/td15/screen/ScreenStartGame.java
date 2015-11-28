package org.deepserver.td15.screen;

import org.deepserver.td15.Client;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.deepserver.td15.monster.Monster;
import org.deepserver.td15.monster.MonsterEnemy;
import org.deepserver.td15.monster.MonsterPlayer;
import org.deepserver.td15.monster.MonsterRock;
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

		MonsterEnemy enemy = new MonsterEnemy(world, new Vec2(1f,1f), player);
		world.add(enemy);

		for (int i = 0; i < monsterCount; i++) {
			Monster q = new MonsterSprite(world, new Vec2(((float) Math.random() - 0.5f) * 2 * world.rockRadius,
					((float) Math.random() - 0.5f) * 2 * world.rockRadius));

			world.add(q);
		}
		addRockCircle(world, world.rockRadius);
	}

	private void addRockCircle(World world, float rockRadius) {
		double theta = 0; // angle that will be increased each loop
		int X = 0; // x coordinate of circle center
		int Y = 0; // y coordinate of circle center
		double step = deg2rad(1); // amount to add to theta each time (degrees)

		while (theta < 1.999 * Math.PI) {
			double x = X + rockRadius * Math.cos(theta);
			double y = Y + rockRadius * Math.sin(theta);

			world.add(new MonsterRock(world, new Vec2((float) x, (float) y)));

			theta += step;
		}
	}

	private double deg2rad(double n) {
		return n * Math.PI / 180d;
	}
	
	@Override
	public void escape() {
		client.changeScreen2d(new ScreenStartup(client));
		client.changeScreen3d(new ScreenStartupBackground(client));

		client.focus2d();
	}
}
