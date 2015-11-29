package org.deepserver.td15;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deepserver.td15.screen.Screen;
import org.deepserver.td15.screen.ScreenStartup;
import org.deepserver.td15.screen.ScreenStartupBackground;
import org.joml.Matrix4;
import org.joml.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Client {

	public final int mouseZerosToSkip=10;
	
	public float mouseSensitivity=8;
	
	protected int windowWidth;
	protected int windowHeight;
	
	protected GLFWErrorCallback errorCallback;
	protected GLFWKeyCallback keyCallback;
	protected GLFWCursorPosCallback mouseCallback;
	protected GLFWMouseButtonCallback buttonCallback;
	
	protected InputStatus is=new InputStatus();
	protected InputStatus empty=new InputStatus();

	protected double lastTime = 0;
	protected double deltaTime = 0;

	protected long window;

	protected Matrix4 projMatrix = new Matrix4();

	protected FloatBuffer fb = BufferUtils.createFloatBuffer(16);

	protected DoubleBuffer bx = BufferUtils.createDoubleBuffer(1);
	protected DoubleBuffer by = BufferUtils.createDoubleBuffer(1);

	protected double lastMouseX=0;
	protected double lastMouseY=0;
	protected boolean lastMouseIsValid=false;
	
	protected int lastRelativeX=0;
	protected int lastRelativeY=0;
	protected int zeroCounter=0;
	
	protected Screen screen3d;
	protected Screen screen2d;

	protected boolean keyFocus2d = true;

	//protected float shadingFactor = 0.0f;
	protected final float maxShadingFactor = 0.7f;
	//protected final float shadingSpeed = 2f;
	protected Floathing shadething = new Floathing(2.0f);

	protected ArrayList<Screen> screenStack = new ArrayList<Screen>();

	public Client() {
		if (Main.isWindowed) {
			windowWidth = 900;
			windowHeight = 500;
		} else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			windowWidth = screenSize.width;
			windowHeight = screenSize.height;
		}
	}

	public void run() {
		System.out.println("Client running");

		try {
			init();
			screen2d = new ScreenStartup(this);
			screen3d = new ScreenStartupBackground(this);

			clientLoop();

			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			glfwTerminate();
			errorCallback.release();
			keyCallback.release();
		}
	}
	
	protected void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

		// xxx select the default or highest resolution on start. Make this work
		// on Mo's notebook!

		window = glfwCreateWindow(windowWidth, windowHeight, "Hello World!",
				Main.isWindowed ? NULL : glfwGetPrimaryMonitor(), NULL);
		
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		if (Main.isClient  && Main.isWindowed) {
			if (Main.isLeft) 
				glfwSetWindowPos(window, 50, 100);
			else
				glfwSetWindowPos(window, (1920/2)+50, 100);
		}
		
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action,int mods) {
				List<Integer> ups = Arrays.asList(328,25,111);
				List<Integer> downs = Arrays.asList(336,39,116);
				List<Integer> lefts = Arrays.asList(331,38,113);
				List<Integer> rights = Arrays.asList(333,40,114);
				List<Integer> esc = Arrays.asList(1,9);
				List<Integer> enter = Arrays.asList(57,28,65,36);
				List<Integer> bkspc = Arrays.asList(22,14);
				
				if (ups.contains(scancode)) // up
					is.forward = (action != 0);
				if (downs.contains(scancode)) // down
					is.backward = (action != 0);
				if (lefts.contains(scancode)) // left
					is.left = (action != 0);
				if (rights.contains(scancode)) // right
					is.right = (action != 0);
				if (enter.contains(scancode)) // select=space or enter
					is.firing = (action != 0);
				if (bkspc.contains(scancode))
					is.fullBreak = (action != 0);

				// key events:
				if (esc.contains(scancode) && action==1 ) // esc
					is.escapeEvent=true;
				if (ups.contains(scancode) && action==1) // up
					is.upEvent=true;
				if (downs.contains(scancode) && action==1) // down
					is.downEvent=true;
				if (enter.contains(scancode) && action==1) // select=space or enter
					is.selectEvent=true;
			}
		});

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		
		// nope: becomes *really* ugly when entering menu screen again ...
		//glEnable(GL_TEXTURE_2D);
		
		glViewport(0, 0, windowWidth, windowHeight);

		glMatrixMode(GL_PROJECTION);
		projMatrix.setPerspective(45.0f, (float) windowWidth / windowHeight,
				0.01f, 1000.0f).get(fb);
		glLoadMatrixf(fb);

		glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

	}
	
	protected void pollEvents(InputStatus is) {
		is.leftButton =glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1)!=0;
		is.rightButton=glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_2)!=0;
		
		DoubleBuffer bx = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer by = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(window, bx, by);
		
		double mx=bx.get();
		double my=by.get();
		
		if (lastMouseIsValid) {
			int rx=(int)(mx-lastMouseX);
			int ry=(int)(my-lastMouseY);
			
			if (rx==0 && ry==0) {
				zeroCounter++;
				if (zeroCounter>mouseZerosToSkip) {
					is.mouseX=is.mouseY=0;
				}
			} else {
				zeroCounter=0;
				is.mouseX=rx;
				is.mouseY=ry;
			}
		}

		lastMouseIsValid=true;
		lastMouseX=mx;
		lastMouseY=my;
	}
	
	public void clearEvents() {
		is.escapeEvent=false;
		is.selectEvent=false;
	}

	protected void clientLoop() {
		lastTime = glfwGetTime();
		deltaTime = 1.0 / 60.0;

		while (glfwWindowShouldClose(window) == GL_FALSE) {
			glfwPollEvents();

			pollEvents(is);
			
			if (keyFocus2d) {
				screen2d.action(deltaTime,is);	  screen3d.action(deltaTime,empty);
			} else {
				screen2d.action(deltaTime,empty); screen3d.action(deltaTime,is);				
			}

			// ---------------------------- draw 3d world ---------------------
			glClearColor(0, 0.0f, 0.2f, 1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glMatrixMode(GL_MODELVIEW);
			
			

			screen3d.drawCamera();
			
			glEnable(GL_TEXTURE_2D);
//			glDisable(GL_DEPTH_TEST);
					
			screen3d.draw();

			glDisable(GL_TEXTURE_2D);
//			glEnable(GL_DEPTH_TEST);
			
			
			
//			// ---------------------------- draw shading rectangle when fading ---
//			glClear(GL_DEPTH_BUFFER_BIT);
//
//			shadething.update(deltaTime, screen2d.shade3d?maxShadingFactor:0.0f);
//			
//			// draw translucent plane between 2d and 3d screen
//			float shadingFactor = shadething.get();
//			if (shadingFactor != 0.0f) {
//				Matrix4 viewMatrix = new Matrix4();
//				viewMatrix.setLookAt(new Vec3(0f, 0f, 5f),
//						new Vec3(0f, 0f, 0f), new Vec3(0f, 1f, 0f)).get(fb); 
//				glLoadMatrixf(fb);
//				
//				float size = 3;
//
//				glBegin(GL_QUADS);
//				glColor4f(0.0f, 0.0f, 0.0f, shadingFactor);
//				glVertex3f(size, -size, size);
//				glVertex3f(size, size, size);
//				glVertex3f(-size, size, size);
//				glVertex3f(-size, -size, size);
//				glEnd();
//
//			}

			// -------------------------- draw 2d menu ----------------------------
			glClear(GL_DEPTH_BUFFER_BIT);

			screen2d.drawCamera();
			screen2d.draw();

			glfwSwapBuffers(window);
			
			//System.err.println(deltaTime);

			deltaTime = glfwGetTime() - lastTime;
			lastTime += deltaTime;
		}
	}

	public void changeScreen2d(Screen next) {
		screen2d = next;
		clearEvents();
	}

	public void changeScreen3d(Screen next) {
		screen3d = next;
		clearEvents();
	}

	public void focus2d() {
		keyFocus2d = true;
	}

	public void focus3d() {
		keyFocus2d = false;
	}

	public void pushScreen2d(Screen next) {
		screenStack.add(screen2d);
		changeScreen2d(next);
	}

	public void popScreen2d() {
		changeScreen2d(screenStack.remove(screenStack.size() - 1));
	}

	public void closeGame() {
		glfwSetWindowShouldClose(window, GL_TRUE);
	}

}
