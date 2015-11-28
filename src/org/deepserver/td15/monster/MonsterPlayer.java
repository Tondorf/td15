package org.deepserver.td15.monster;

import org.deepserver.td15.Floathing;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix2;
import org.joml.Vec2;
import org.joml.Vec3;

public class MonsterPlayer extends MonsterSprite {


	protected final float accel = 0.01f;
	protected final float rotationSpeed = 1.0f;
	protected final float cameraHeight = 10.0f;
	protected final long shotDelay = 1000;

	private final float maxPitch = 5.0f;
	private Floathing pitchthing = new Floathing(8.0f);


	protected InputStatus is = new InputStatus();

	protected float v=0;
	
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
	public void draw() {
		org.lwjgl.opengl.GL11.glPushMatrix();
		super.draw();
		org.lwjgl.opengl.GL11.glRotatef(13.0f, 0, 0, 1);
		org.lwjgl.opengl.GL11.glPopMatrix();
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
			v+=accel*(float)delta;
		}

		if (is.backward) {
			v-=accel*(float)delta;
		}

		if (is.fullBreak) {
			v *= 0.7f;
			if (v < 0.05f)
				v = 0f;
		}		
		//if (v > maxSpeed) {
		//	// 8 / 10
		//	float f =  maxSpeed / v.length();
		//	v.mul(f);
		//}
			
		
//		Vec2 temp=new Vec2(v);
//		temp.mul((float)delta);
		Vec2 temp=new Vec2(orientation.getAhead());
		temp.mul(v);
		
		position.add(temp);

		setCamera();
	}
}
