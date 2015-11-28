package org.deepserver.td15;

import static org.lwjgl.opengl.GL11.glLoadMatrixf;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.deepserver.td15.monster.Monster;
import org.deepserver.td15.screen.Screen;
import org.joml.Matrix4;
import org.joml.Vec2;
import org.joml.Vec3;
import org.lwjgl.BufferUtils;

public class World {
	public Screen screen;
	
	public ArrayList<Monster> list=new ArrayList<Monster>();
	public ArrayList<Monster> listOfnewMonsters=new ArrayList<Monster>();
	public ArrayList<Monster> listOfdeadMonsters=new ArrayList<Monster>();
	
	public final float rockRadius=50f;
	
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
}
