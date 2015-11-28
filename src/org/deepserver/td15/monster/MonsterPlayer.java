package org.deepserver.td15.monster;

import org.deepserver.td15.Floathing;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix2;
import org.joml.Matrix3;
import org.joml.Vec2;
import org.joml.Vec3;

public class MonsterPlayer extends MonsterSprite {

	protected final float maxSpeed = 50.0f;
	protected final float speed = 20.0f;
	protected final float rotationSpeed = 90.0f;
	protected final float cameraHeight = 30.0f;

	protected final long shotDelay = 1000;

	private final float maxPitch = 5.0f;
	private Floathing pitchthing = new Floathing(8.0f);


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

// tk testing:
		Vec3 tgt = new Vec3(position);
		float p = pitchthing.get();
		p *= v.length()/maxSpeed;
		
		Vec2 dir = orientation.getAhead();
		float d = dir.length();
		Vec2 rot = new Vec2();
		rot.x = dir.x / d;
		rot.y = dir.y / d; // norm
		double a = Math.atan2(rot.y,  rot.x) / Math.PI * 180.0;
		a += 90.0;
		while (a < -180.0) a += 360.0;
		while (a > 180.0) a -= 360.0;
		a = a / 180.0 * Math.PI;
		rot.x = (float)Math.cos(a) * d;
		rot.y = (float)Math.sin(a) * d;
		tgt.add(new Vec3(rot).mul(p));
		camPos.add(new Vec3(rot).mul(-p/2));
		world.setCamera(camPos, tgt, new Vec3(orientation.getAhead()));
//oj:		world.setCamera(camPos, new Vec3(position), new Vec3(orientation.getAhead()));
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
			v.add(new Vec2(orientation.getAhead()).mul(speed
					* (float) delta));
		}

		if (is.backward) {
			v.add(new Vec2(orientation.getAhead()).mul(-speed
					* (float) delta));
		}
		
		if (is.fullBreak) {
			v.mul(0.7f);
			if (v.length() < 0.5f)
				v.mul(0f);
		}		
		if (v.length() > maxSpeed) {
			// 8 / 10
			float f =  maxSpeed / v.length();
			v.mul(f);
		}
			
		
		Vec2 temp=new Vec2(v);
		temp.mul((float)delta);
		
		position.add(new Vec2(temp));

		setCamera();
	}
}
