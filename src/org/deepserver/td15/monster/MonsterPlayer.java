package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix3;
import org.joml.Vec3;

public class MonsterPlayer extends Monster {
	
	protected final float speed = 20.0f;
	protected final float rotationSpeed=3.0f;
	protected final float cameraHeight=1.0f;

	protected InputStatus is=new InputStatus();

	public MonsterPlayer(World world) {
		super(world);
		groundDistance=1f;
	}
	
	protected void setCamera() {
		Vec3 camPos=new Vec3(position);

		camPos.z=cameraHeight;
		
		world.setCamera(camPos, position, orientation.getAhead());
	}

	@Override
	public void action(double delta,InputStatus is) {
		super.action(delta,is);
		
		if (is.firing) {
//			MonsterShot shot = new MonsterShot(world,true);
//			shot.position = new Vec3(position);
//			
//			Vec3 o=new Vec3(orientation.getLeft());
//			o.mul(gunOffsetX);
//			
//			if (fireLeft) 
//				shot.position.add(o);
//			else
//				shot.position.sub(o);
//			
//			fireLeft=!fireLeft;
//				
//			shot.orientation = new Matrix3(orientation);
//			shot.groundDistance=groundDistance;
//			world.add(shot);
		}
		
		Vec3 up=orientation.getUp();

		if (is.left) {
			orientation.mul(new Matrix3().rotate(-rotationSpeed*(float)delta,up.x,up.y,up.z));
		}

		if (is.right) {
			orientation.mul(new Matrix3().rotate( rotationSpeed*(float)delta,up.x,up.y,up.z));
		}

		if (is.forward) {
			position.add(new Vec3(orientation.getAhead()).mul(speed*(float)delta));
		}

		if (is.backward) {
			position.add(new Vec3(orientation.getAhead()).mul(-speed*(float)delta));
		}

		setCamera();
	}
}
