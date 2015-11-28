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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

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

	protected float shadingFactor = 0.0f;
	protected final float maxShadingFactor = 0.7f;
	protected final float shadingSpeed = 2f;
	
	// slap lennart: checkin Helper class :p
	//protected boolean windoof = Helper.isWindoof();
	boolean windoof=false;

	protected ArrayList<Screen> screenStack = new ArrayList<Screen>();

	public Client() {
		if (Main.isWindowed) {
			windowWidth = 900;
			windowHeight = 500;
		} else {
			windowWidth = 1920;
			windowHeight = 1080;
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

				if ( (windoof && scancode == 336) || scancode == 39 ||  scancode == 116) // down
					is.backward = (action != 0);
				if ( (windoof && scancode == 331) || scancode == 38 || scancode == 113) // left
					is.left = (action != 0);
				if ( (windoof && scancode == 328) || scancode == 25 || scancode == 111) // up
					is.forward = (action != 0);
				if ( (windoof && scancode == 333) || scancode == 40 || scancode == 114) // right
					is.right = (action != 0);
				if ( (windoof && (scancode == 57 || scancode == 28)) || scancode == 65 || scancode == 36) // select=space or enter
					is.firing = (action != 0);
				
				// key events:
				if ( (windoof && scancode == 1 || scancode==9) && action==1 )
					is.escapeEvent=true;
				
				if ( (windoof && scancode == 336 || scancode == 39 ||  scancode == 116) && action==1) // down
					is.downEvent=true;
				
				if ( (windoof && scancode == 328 || scancode == 25 || scancode == 111) && action==1) // up
					is.upEvent=true;
				
				if ( ((windoof && (scancode == 57 || scancode == 28)) || scancode == 65 || scancode == 36) && action==1) // select=space or enter
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
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

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
			glClearColor(0, 0.3f, 0.5f, 1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glMatrixMode(GL_MODELVIEW);

			screen3d.drawCamera();
			screen3d.draw();

			// ---------------------------- draw shading rectangle when fading ---
			glClear(GL_DEPTH_BUFFER_BIT);

			if (screen2d.shade3d)
				shadingFactor += deltaTime * shadingSpeed;
			else
				shadingFactor -= deltaTime * shadingSpeed;

			if (shadingFactor < 0)
				shadingFactor = 0;
			if (shadingFactor > maxShadingFactor)
				shadingFactor = maxShadingFactor;

			if (shadingFactor != 0.0f) {
				Matrix4 viewMatrix = new Matrix4();
				viewMatrix.setLookAt(new Vec3(0f, 0f, 5f),
						new Vec3(0f, 0f, 0f), new Vec3(0f, 1f, 0f)).get(fb); // y
																				// is
																				// up
				glLoadMatrixf(fb);

				float size = 3;

				glBegin(GL_QUADS);
				glColor4f(0.0f, 0.0f, 0.0f, shadingFactor);
				glVertex3f(size, -size, size);
				glVertex3f(size, size, size);
				glVertex3f(-size, size, size);
				glVertex3f(-size, -size, size);
				glEnd();

			}

			// -------------------------- draw 2d menu ----------------------------
			glClear(GL_DEPTH_BUFFER_BIT);

			screen2d.drawCamera();
			screen2d.draw();

			glfwSwapBuffers(window);

			deltaTime = glfwGetTime() - lastTime;
			lastTime += deltaTime;
		}
	}

	public void changeScreen2d(Screen next) {
		screen2d = next;
	}

	public void changeScreen3d(Screen next) {
		screen3d = next;
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
