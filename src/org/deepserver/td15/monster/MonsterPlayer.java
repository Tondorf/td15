package org.deepserver.td15.monster;

import org.deepserver.td15.Floathing;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix2;
import org.joml.Matrix3;
import org.joml.Vec2;
import org.joml.Vec3;

public class MonsterPlayer extends MonsterSprite {

	protected final float speed = 50.0f;
	protected final float rotationSpeed = 180.0f;
	protected final float cameraHeight = 10.0f;
	protected final long shotDelay = 1000;

	protected InputStatus is = new InputStatus();

	protected Vec2 v=new Vec2();
	
	protected long lastShotTimestamp;
	
	public MonsterPlayer(World world) {
		super(world,new Vec2(0f,0f));
		zLayer = 1f;
	}
	
	@Override
	public String getTextureName() {
		return "schif1.png";
	}

	protected void setCamera() {

		Vec3 camPos = new Vec3(position);

		camPos.z = cameraHeight;

		world.setCamera(camPos, new Vec3(position), new Vec3(orientation.getAhead()));
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		if (is.firing) {
			MonsterShot shot = new MonsterShot(world, new Vec2(position));
			shot.orientation = new Matrix2(orientation);
			long now = System.currentTimeMillis();
			if((lastShotTimestamp+shotDelay) < now){
				world.add(shot);
				lastShotTimestamp = now;
			}
		}

		if (is.left) {
			orientation.mul(new Matrix2().rotation(rotationSpeed * (float) delta)); // in a flat, flat world ... always rotate around
								// z!
		}

		if (is.right) {
			orientation.mul(new Matrix2().rotation(-rotationSpeed * (float) delta));
		} 

		if (is.forward) {
			v.add(new Vec2(orientation.getAhead()).mul(speed
					* (float) delta));
		}

		if (is.backward) {
			v.add(new Vec2(orientation.getAhead()).mul(-speed
					* (float) delta));
		}
		
		Vec2 temp=new Vec2(v);
		temp.mul((float)delta);
		
		position.add(new Vec2(temp));

		setCamera();
	}
}
