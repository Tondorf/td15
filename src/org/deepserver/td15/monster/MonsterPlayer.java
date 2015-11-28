package org.deepserver.td15.monster;

import org.deepserver.td15.Floathing;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix3;
import org.joml.Vec2;
import org.joml.Vec3;

public class MonsterPlayer extends MonsterSprite {

	protected final float speed = 50.0f;
	protected final float rotationSpeed = 180.0f;
	protected final float cameraHeight = 10.0f;
	protected final long shotDelay = 1000;

	private final float maxPitch = 10.0f;
	private Floathing pitchthing = new Floathing(1.0f);

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

		camPos.z -= cameraHeight;

		world.setCamera(camPos, position, new Vec3(-1.0f, -1.0f, 0.0f));
		world.setCamera(camPos, position, orientation.getUp());
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		if (is.firing) {
			MonsterShot shot = new MonsterShot(world, new Vec2(position));
			shot.orientation = new Matrix3(orientation);
			long now = System.currentTimeMillis();
			if((lastShotTimestamp+shotDelay) < now){
				world.add(shot);
				lastShotTimestamp = now;
			}
			System.out.println(now+"\n"+lastShotTimestamp+"\n"+(lastShotTimestamp+shotDelay)+"\n\n");
		}

		if (is.left) {
			orientation.mul(new Matrix3().rotate(rotationSpeed * (float) delta,
					0, 0, 1)); // in a flat, flat world ... always rotate around
								// z!
			pitchthing.update(delta, maxPitch);
		} else if (!is.right) {
			pitchthing.update(delta, 0.0f);
		}

		if (is.right) {
			orientation.mul(new Matrix3().rotate(
					-rotationSpeed * (float) delta, 0, 0, 1));
			pitchthing.update(delta, -maxPitch);
		} else if (!is.left) {
			pitchthing.update(delta, 0.0f);
		}
		//orientation.mul(new Matrix3().rotate)
		

		if (is.forward) {
			v.add(new Vec2(orientation.getUp()).mul(speed
					* (float) delta));
		}

		if (is.backward) {
			v.add(new Vec2(orientation.getUp()).mul(-speed
					* (float) delta));
		}
		
		Vec2 temp=new Vec2(v);
		temp.mul((float)delta);
		
		position.add(new Vec3(temp));

		setCamera();
	}
}
