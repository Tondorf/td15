package org.deepserver.td15;

import static org.lwjgl.opengl.GL11.glLoadMatrixf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.BitSet;

import org.deepserver.td15.monster.Monster;
import org.deepserver.td15.screen.Screen;
import org.joml.Matrix2;
import org.joml.Matrix4;
import org.joml.Vec2;
import org.joml.Vec3;
import org.lwjgl.BufferUtils;

public class World {
	public Screen screen;
	
	public ArrayList<Monster> list=new ArrayList<Monster>();
	public ArrayList<Monster> listOfnewMonsters=new ArrayList<Monster>();
	public ArrayList<Monster> listOfdeadMonsters=new ArrayList<Monster>();
	
	protected final float rockRadius=50f;
	
	protected Vec3 cameraEye    = new Vec3(0.0f,0.0f,1.0f);
	protected Vec3 cameraLookAt = new Vec3(0.0f,0.0f,0.0f);
	protected Vec3 cameraUp     = new Vec3(0.0f,1.0f,0.0f);

    protected Matrix4 viewMatrix = new Matrix4();
    protected FloatBuffer fb = BufferUtils.createFloatBuffer(16); 

	
	public World(Screen screen) {
		this.screen=screen;
	}
	
	public void action(double delta,InputStatus is) {
		for (Monster m:list) {
			m.action(delta,is);
		}
		
		for (Monster m:list)
			for (Monster n:list) {
				if (m.id!=n.id) {
					Vec2 a=m.position;
					Vec2 b=n.position;
					
					
					if (a.distance(b)<m.getRadius()+n.getRadius()) {
						m.destroy();
						n.destroy();
					}
				}
			}
		
		for (Monster m:listOfdeadMonsters)
			list.remove(m);
		
		listOfdeadMonsters.clear();
	}
	
	public void draw() {
		ArrayList<Monster> next=new ArrayList<Monster>();
		
		for (Monster m:list) {
			if (!m.killMe) next.add(m);
		}
		list=next;

		list.addAll(listOfnewMonsters);
		listOfnewMonsters.clear();
		
		for (Monster m:list) {
			m.draw();
		}		
	}

	public void add(Monster monster) {
		listOfnewMonsters.add(monster);
	}
	
	public void remove(Monster monster) {
		listOfdeadMonsters.add(monster);
	}
	
	public void setCamera(Vec3 eye,Vec3 lookat,Vec3 up) {
		cameraLookAt=lookat;
		cameraEye=eye;
		cameraUp=up;
		
	}
	
	public void drawCamera() {
        viewMatrix.setLookAt(cameraEye,cameraLookAt,cameraUp).get(fb);  // y is up
        glLoadMatrixf(fb);
	}
	
	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		bout.write(ByteBuffer.allocate(4).putInt(list.size()).array());
		for (Monster m : list) {
			bout.write(ByteBuffer.allocate(4).putLong(m.id).array());
			bout.write(ByteBuffer.allocate(4).putFloat(m.position.x).array());
			bout.write(ByteBuffer.allocate(4).putFloat(m.position.y).array());
			bout.write(ByteBuffer.allocate(4).putFloat(m.orientation.getAhead().x).array());
			bout.write(ByteBuffer.allocate(4).putFloat(m.orientation.getAhead().y).array());
			bout.write(ByteBuffer.allocate(4).putFloat(m.zLayer).array());
		}
		return bout.toByteArray();
	}
	
	public void fromBytes(byte[] binbuf) {
		//ByteArrayInputStream bin = new ByteArrayInputStream(binbuf);
		ArrayList<Monster> ret= new ArrayList<Monster>();
		int len  = ByteBuffer.wrap(binbuf, 0, 4).getInt();
		int off = 4;
		for (int i=0;i<len; i++) {
			long mid = ByteBuffer.wrap(binbuf, off, 4).getLong();
			float x = ByteBuffer.wrap(binbuf, off+4, 4).getFloat();
			float y = ByteBuffer.wrap(binbuf, off+8, 4).getFloat();
			float ox = ByteBuffer.wrap(binbuf, off+12, 4).getFloat();
			float oy = ByteBuffer.wrap(binbuf, off+16, 4).getFloat();
			float z = ByteBuffer.wrap(binbuf, off+20, 4).getFloat();
			off += 24;
			Monster m = new Monster(this);
			m.id = mid;
			m.position = new Vec2(x,y);
			m.orientation.m00 = 1 - ox;
			m.orientation.m01 = ox;
			m.orientation.m10 = 1 - oy;
			m.orientation.m11 = oy;
			ret.add(m);
		}
		list = ret;
		//return ret;
	}
	
}
