package org.deepserver.td15.monster;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.deepserver.td15.texture.Texture;
import org.joml.Matrix2;
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
	}
	
	public String getTextureName() {
		return "stein.png";
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

		// Maybe load prior to draw() in order to avoid latencies:
		Texture t = null;
		String fn="res/"+getTextureName();
		try {
			t = Texture.load(fn);
		} catch (IOException ex) {
			System.err.println("Could not load "+fn);
			System.err.println(ex.getMessage());
		}
		if (t != null) {
//			glEnable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);
//			
			glBindTexture(GL_TEXTURE_2D, t.getTextureID());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP );
			glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP );
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}
	
		glBegin(GL_QUADS);
		{

			glColor3f(1.0f, 1.0f, 1.0f);

			if (t != null)
				glTexCoord2f(t.getWidth(), 0);
			glVertex3f(-size, -size, 0);
			
			if (t != null)
				glTexCoord2f(0, 0);
			glVertex3f(size, -size, 0);
			
			if (t != null)
				glTexCoord2f(0, t.getHeight());
			glVertex3f(size, size, 0);
			
			if (t != null)
				glTexCoord2f(t.getWidth(), t.getHeight());
			glVertex3f(-size, size, 0);
		}
		glEnd();

		if (t!=null) {
//			glDisable(GL_TEXTURE_2D);
			glEnable(GL_DEPTH_TEST);
			
		}
		glPopMatrix();
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);
	}

}
