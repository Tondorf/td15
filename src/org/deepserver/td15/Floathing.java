package org.deepserver.td15;

public class Floathing {
	protected float goal = 0;
	protected float speed = 0;

	protected float current = 0;

	public Floathing(float current, float goal, float speed) {
		this.current = current;
		this.goal = goal;
		this.speed = speed;
	}

	public Floathing(float goal, float speed) {
		this.goal = goal;
		this.speed = speed;
	}

	public Floathing(float speed) {
		this.goal = 0;
		this.speed = speed;
	}

	public float update(double delta, boolean up, boolean down) {
		float aimFor = 0;

		if (up)
			aimFor = goal;
		if (down)
			aimFor = -goal;

		return update(delta, aimFor);
	}

	public float update(double delta, float aimFor) {
		float max = speed * (float) delta;

		if (current < aimFor) {
			float offset = aimFor - current;
			if (offset > max)
				offset = max;
			current += offset;
		}

		if (current > aimFor) {
			float offset = current - aimFor;
			if (offset > max)
				offset = max;
			current -= offset;
		}

		return current;
	}

	public float get() {
		return current;
	}

}
