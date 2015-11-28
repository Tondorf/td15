package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix4;
import org.joml.Vec2;
import org.joml.Vec3;

public class MonsterDefaultCam extends Monster {

	protected Vec3 lookAt = new Vec3();
	protected Vec3 eye = new Vec3(0.0f, 2.0f, 0.0f);

	protected Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);

	protected Vec3 ahead = new Vec3(0.0f, 0.0f, 1.0f);

	protected final float speed = 5.0f;
	protected final float angle = 10;

	public MonsterDefaultCam(World world, Vec2 v) {
		super(world);
		position = v;

	}

	@Override
	public void draw() {
		super.draw();
	}

	protected void setCamera() {
		lookAt = new Vec3(eye);
		lookAt.add(ahead);
		world.setCamera(eye, lookAt, up);
	}

	@Override
	public boolean canCrash() {
		return false;
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		Matrix4 rot = new Matrix4();
		Matrix4.rotation(angle * (float) delta, up, rot);
		rot.transform(ahead);

		Vec3 add = new Vec3(ahead);
		add.mul((float) (delta * speed));
		eye.add(add);

		setCamera();
	}
}
