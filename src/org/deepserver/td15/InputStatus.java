package org.deepserver.td15;

public class InputStatus {

	// Action Screen:
	public boolean left = false;
	public boolean right = false;
	public boolean forward = false;
	public boolean backward = false;
	public boolean firing = false;
	public boolean fullBreak = false;

	public int mouseX = 0;
	public int mouseY = 0;

	public boolean leftButton = false;
	public boolean rightButton = false;

	// Everywhere:
	public boolean escapeEvent = false;

	// Menu Screen:
	public boolean upEvent = false;
	public boolean downEvent = false;
	public boolean selectEvent = false;

	public InputStatus() {
	}

	public InputStatus(InputStatus is) {
		left = is.left;
		right = is.right;
		forward = is.forward;
		backward = is.backward;
		firing = is.firing;

		mouseX = is.mouseX;
		mouseY = is.mouseY;

		leftButton = is.leftButton;
		rightButton = is.rightButton;

		escapeEvent = is.escapeEvent;
		upEvent = is.upEvent;
		downEvent = is.downEvent;
		selectEvent = is.selectEvent;
	}

	public void clearEvents() {
		escapeEvent = false;
		upEvent = false;
		downEvent = false;
		selectEvent = false;
	}
}
