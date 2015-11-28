package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.deepserver.td15.sound.SoundEffect;
import org.joml.Matrix2;
import org.joml.Vec2;
import org.joml.Vec3;

public class MonsterPlayer extends MonsterSprite {


	protected final float accel = 0.01f;
	protected final float rotationSpeed = 1.0f;
	protected final long shotDelay = 200;

	protected final float cameraHeight = 30.0f;
	protected final float vToHeight = 10f;
	protected final float maxSpeed = 0.05f;

	protected InputStatus is = new InputStatus();

	protected float v=0;
	
	protected long lastShotTimestamp;

	protected final boolean ENGINE_NOISE = true;
	protected long lastEngineNoiseTimestamp;
	protected final long engineNoiseDelay = 2703;
	
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

		camPos.z = cameraHeight*(1+v*vToHeight);

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
		
		if (this.ENGINE_NOISE) {
			long now = System.currentTimeMillis();
			if((lastEngineNoiseTimestamp+engineNoiseDelay) < now){
				world.screen.audio.play(SoundEffect.ENGINE);
				lastEngineNoiseTimestamp = now;
			}
		}


		if (is.firing) {
			MonsterShot shot = new MonsterShot(world, new Vec2(position),v);
			shot.orientation = new Matrix2(orientation);
			shot.position.add(orientation.getAhead().mul(this.getRadius()+shot.getRadius()));
			long now = System.currentTimeMillis();
			if((lastShotTimestamp+shotDelay) < now) {
				world.add(shot);
				world.screen.audio.play(SoundEffect.SHOT);
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
		
		if (v>maxSpeed) {
			v=maxSpeed;
		}
		
		if (v<0) {
			v=0;
		}
			
		Vec2 temp=new Vec2(orientation.getAhead());
		temp.mul(v);
		
		position.add(temp);

		setCamera();
	}
}
