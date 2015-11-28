package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix2;
import org.joml.Vec2;

public class MonsterEnemy extends MonsterSprite {
	protected final float maxSpeed = 2.0f;
	protected final float rotationSpeed = 0.5f;
	protected final int refreshTargetDelay = 1000;
	protected final float accel = 0.1f;

	protected boolean turnLeft = false;
	protected boolean turnRight = false;

	protected long lastTargetRefresh;
	protected float speed;
	protected Vec2 position;
	protected Vec2 target;
	protected boolean reachedMaxSpeed = false;
	protected float v = 0.0f;

	public MonsterEnemy(World world, Vec2 position) {
		super(world, position);
		this.speed = 10f;
		this.position = position;
	}

	@Override
	public String getTextureName() {
		return "ship1.png";
	}

	protected void lockTarget(Vec2 target) {
		this.target = target;

		turnRight = true;
		turnLeft = false;
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		long now = System.currentTimeMillis();
		if ((lastTargetRefresh + refreshTargetDelay) < now) {
			lastTargetRefresh = now;
			lockTarget(new Vec2(10f,10f));
		}

		if (turnLeft) {
			orientation.mul(new Matrix2().rotation(rotationSpeed * (float) delta));
		} else if (turnRight) {
			orientation.mul(new Matrix2().rotation(-rotationSpeed * (float) delta));
		}

		if (!reachedMaxSpeed) {
			v += accel * (float) delta;
			if (v > maxSpeed) {
				v = maxSpeed;
				reachedMaxSpeed = true;
			}
		}

		Vec2 temp = new Vec2(orientation.getAhead());
		temp.mul(v);
		
		System.out.println(v);

		position.add(temp);
	}
}
