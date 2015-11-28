package org.deepserver.td15.monster;

import org.deepserver.td15.World;
import org.joml.Vec2;

public class MonsterShot extends MonsterSprite {
	protected final float speed = 10.0f;

	public MonsterShot(World world, Vec2 position) {
		super(world,position);
	}
	
	@Override
	public String getTextureName() {
		return "ship1.png";
	}
}
