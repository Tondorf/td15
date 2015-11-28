package org.deepserver.td15.monster;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glColor3f;

import org.deepserver.td15.World;
import org.joml.Matrix4;
import org.joml.Vec2;
import org.joml.Vec3;
import org.lwjgl.BufferUtils;

public class MonsterSpace extends Monster {

	protected FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	
	private ArrayList<Vec3> stars = new ArrayList<Vec3>();
	private final int numStars = 2000;
	private final float width = 150.0f;
	private final float height = 150.0f;
	private final float depth = 150.0f;
	private final float offset = 100.0f;
	
	public MonsterSpace(World world) {
		super(world);
		
		position = new Vec2();//10000.0f, 10000.0f);
		
		
		for (int i=0; i<numStars; i++) {
			stars.add(new Vec3(
					(float)Math.random() * width - width/2.0f,
					(float)Math.random() * height - height/2.0f,
					(float)Math.random() * depth - offset
					));
		}

	}

	@Override
	public void draw() {
		super.draw();

		Matrix4 q = new Matrix4(orientation);
		
		q.get(fb);
		fb.rewind();
		
		glPushMatrix();
		// first rotate, then translate (reverse order on stack)
		glTranslatef(0.0f, 0.0f, zLayer);
		glMultMatrixf(fb);
		
		//glDisable(GL_DEPTH_TEST);
		glBegin(GL_POINTS);
		
			glColor3f(1.0f, 1.0f, 1.0f);
			for (Vec3 v : stars) {
				glVertex3f(v.x, v.y, v.z);
			}
		
		glEnd();
		
		//glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}

	@Override
	public boolean canCrash() {
		return false;
	}

	public float getRadius() {
		return 0.0f;
	}

}
