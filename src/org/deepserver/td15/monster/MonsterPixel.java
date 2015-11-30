package org.deepserver.td15.monster;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Vec2;

public class MonsterPixel extends Monster {

	static public final float initialSize = 0.02f;
	static public final float highligtedSize = 0.04f;

	static public final float duration = 0.5f; // seconds

	protected float size = initialSize;
	protected double startTime;
	protected double time;
	protected int direction = 0;

	public MonsterPixel(World world, Vec2 v) {
		super(world);
		position.x = v.x;
		position.y = v.y;
	}

	@Override
	public void draw() {
		super.draw();

		glPushMatrix();
		glTranslatef(position.x, position.y, 0.f);

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
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		if (direction != 0) {
			if (time > duration) {
				if (direction == 1)
					size = highligtedSize;
				else
					size = initialSize;

				direction = 0;
			} else {
				if (direction == 1)
					size = (float) (initialSize + time / duration * (highligtedSize - initialSize));
				else
					size = (float) (highligtedSize - time / duration
							* (highligtedSize - initialSize));
			}
		}

		time += delta;
	}

	public void highLight() {
		startTime = glfwGetTime();
		time = 0;
		direction = 1;
	}

	public void lowLight() {
		startTime = glfwGetTime();
		time = 0;
		direction = -1;
	}

	@Override
	public void destroy() {
	}

}
