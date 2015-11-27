package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix3;
import org.joml.Vec3;


public class Monster {
	
	public World world;
	
	public Vec3 position;
	public Matrix3 orientation;
	public Matrix3 modelOrientation;
	
	public boolean killMe=false;

	public float groundDistance=0;
	
	public Monster(World world) {
		this.world=world;
		position=new Vec3();
		orientation=new Matrix3();
		modelOrientation=new Matrix3();
	}

	public void action(double delta,InputStatus is) {
	}
	
	public void draw() {
	}
}
