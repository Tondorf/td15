package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix2;
import org.joml.Matrix3;
import org.joml.Vec2;
import org.joml.Vec3;


public class Monster {
	
	public World world;
	
	public Vec2 position;
	public Matrix2 orientation;
	
	public boolean killMe=false;

	public float zLayer=0;
	
	public Monster(World world) {
		this.world=world;
		position=new Vec2();
		orientation=new Matrix2();
	}

	public void action(double delta,InputStatus is) {
	}
	
	public void draw() {
	}
}
