package org.deepserver.td15.monster;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexCoord2f;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.deepserver.td15.texture.Texture;
import org.joml.Matrix3;
import org.joml.Matrix4;
import org.joml.Vec2;
import org.lwjgl.BufferUtils;

public class MonsterSprite extends Monster {

	protected FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	static public float size = 0.5f;

	public MonsterSprite(World world, Vec2 v) {
		super(world);
		position.x = v.x;
		position.y = v.y;
		position.z = 0.0f;
	}

	// private Texture loadTexture(String ref) throws IOException {
	// Texture t = Texture.load(ref);
	// return t;
	// }

	@Override
	public void draw() {
		super.draw();

		// first modelOrientation, then orientation
		Matrix3 m = new Matrix3(modelOrientation);
		m.mul(orientation);
		m.invert();

		Matrix4 q = new Matrix4(m);

		q.get(fb);
		fb.rewind();

		glPushMatrix();
		// first rotate, then translate (reverse order on stack)
		glTranslatef(position.x, position.y, position.z);
		glMultMatrixf(fb);

		// Maybe load prior to draw() in order to avoid latencies:
		Texture t = null;
		try {
			t = Texture.load("res/buntmann.png");
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		if (t != null) {
			glEnable(GL_TEXTURE_2D);

			glPushMatrix();
			glBindTexture(GL_TEXTURE_2D, t.getTextureID());
		}

		// glTexCoord2f(0, 0);
		// glVertex2f(0, 0);
		//
		// glTexCoord2f(0, texture.getHeight());
		// glVertex2f(0, height);
		//
		// glTexCoord2f(texture.getWidth(), texture.getHeight());
		// glVertex2f(width, height);
		//
		// glTexCoord2f(texture.getWidth(), 0);
		// glVertex2f(width, 0);

		glBegin(GL_QUADS);
		{
			//if (t == null)
				glColor3f(1.0f, 1.0f, 1.0f);

			if (t != null)
				glTexCoord2f(t.getWidth(), 0);
			glVertex3f(-size, -size, size);
			
			if (t != null)
				glTexCoord2f(0, 0);
			glVertex3f(size, -size, size);
			
			if (t != null)
				glTexCoord2f(0, -t.getHeight());
			glVertex3f(size, size, size);
			
			if (t != null)
				glTexCoord2f(t.getWidth(), -t.getHeight());
			glVertex3f(-size, size, size);
		}
		glEnd();

		if (t!=null) {
			glPopMatrix();
			glDisable(GL_TEXTURE_2D);

		}
		glPopMatrix();
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);
	}

}
