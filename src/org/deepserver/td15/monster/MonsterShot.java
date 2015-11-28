package org.deepserver.td15.monster;

import org.deepserver.td15.World;
import org.joml.Vec3;

public class MonsterShot extends Monster {
	protected final float speed = 10.0f;

	public MonsterShot(World world, Vec3 position) {
		super(world);
	}
}
