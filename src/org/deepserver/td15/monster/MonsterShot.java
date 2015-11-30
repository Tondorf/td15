package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.deepserver.td15.sound.SoundEffect;
import org.joml.Vec2;

public class MonsterShot extends MonsterSprite {
	protected final float ownSpeed = 10f;
	public float speed;

	public MonsterShot(World world, Vec2 position, float speed) {
		super(world, position);
		this.speed = speed + ownSpeed;
	}

	@Override
	public String getTextureName() {
		return "grosserschus.png";
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		Vec2 temp = new Vec2(orientation.getAhead());
		temp.mul(speed * (float) delta);

		position.add(temp);
	}

	public void destroy() {
		super.destroy();
		world.screen.audio.play(SoundEffect.KILL);
	}

}
