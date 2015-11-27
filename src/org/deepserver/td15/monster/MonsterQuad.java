package org.deepserver.td15.monster;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.nio.FloatBuffer;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Matrix3;
import org.joml.Matrix4;
import org.joml.Vec2;
import org.joml.Vec3;
import org.lwjgl.BufferUtils;

public class MonsterQuad extends Monster {

	static public float size = 0.5f;

    protected FloatBuffer fb = BufferUtils.createFloatBuffer(16); 

	public MonsterQuad(World world, Vec2 v) {
		super(world);
		position.x = v.x;
		position.y = 0f;
		position.z = v.y;
		
	}

	public MonsterQuad(World world, Vec3 v) {
		super(world);
		position=v;
	}

	@Override
	public void draw() {
		super.draw();

		// first modelOrientation, then orientation
		Matrix3 m=new Matrix3(modelOrientation);
		m.mul(orientation);
		m.invert();

		Matrix4 q=new Matrix4(m);
		
		q.get(fb);
		fb.rewind();
		
		glPushMatrix();
		// first rotate, then translate (reverse order on stack)
		glTranslatef(position.x, position.y, position.z);
		glMultMatrixf(fb);
		
		glBegin(GL_QUADS);
		glColor3f(1.0f, 1.0f, 0.0f);
		glVertex3f(size, -size, -size);
		glVertex3f(size, size, -size);
		glVertex3f(-size, size, -size);
		glVertex3f(-size, -size, -size);
		glColor3f(0.0f, 1.0f, 1.0f);
		glVertex3f(size, -size, size);
		glVertex3f(size, size, size);
		glVertex3f(-size, size, size);
		glVertex3f(-size, -size, size);
		glColor3f(1.0f, 0.0f, 1.0f);
		glVertex3f(size, -size, -size);
		glVertex3f(size, size, -size);
		glVertex3f(size, size, size);
		glVertex3f(size, -size, size);
		glColor3f(0.0f, 1.0f, 0.0f);
		glVertex3f(-size, -size, size);
		glVertex3f(-size, size, size);
		glVertex3f(-size, size, -size);
		glVertex3f(-size, -size, -size);
		glColor3f(0.0f, 0.0f, 1.0f);
		glVertex3f(size, size, size);
		glVertex3f(size, size, -size);
		glVertex3f(-size, size, -size);
		glVertex3f(-size, size, size);
		glColor3f(1.0f, 0.0f, 0.0f);
		glVertex3f(size, -size, -size);
		glVertex3f(size, -size, size);
		glVertex3f(-size, -size, size);
		glVertex3f(-size, -size, -size);
		glEnd();

		glPopMatrix();
	}

	@Override
	public void action(double delta,InputStatus is) {
		super.action(delta,is);
	}
}
