package org.deepserver.td15.monster;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glColor4f;

import org.deepserver.td15.World;
import org.joml.Matrix4;
import org.joml.Vec2;
import org.joml.Vec3;
import org.lwjgl.BufferUtils;

public class MonsterSpace extends Monster {

	protected FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	
	private ArrayList<Vec3> stars = new ArrayList<Vec3>();
	private final int numStars = 1000;
	private final float width = 50.0f;
	private final float height = 50.0f;
	private final float depth = 50.0f;
	private final float offset = 75.0f;
	
	public MonsterSpace(World world) {
		super(world);
		
		position = new Vec2(10000.0f, 10000.0f);
		
		
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
		glTranslatef(position.x, position.y, zLayer);
		glMultMatrixf(fb);
		
		glBegin(GL_POINTS);
		{
			glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
			for (Vec3 v : stars) {
				glVertex3f(v.x, v.y, v.z);
			}
		}
		glEnd();
		
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
