package org.deepserver.td15.monster;

import java.util.ArrayList;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.PixelFont;
import org.deepserver.td15.World;
import org.joml.Vec2;

public class MonsterTextLine extends Monster {
	static public float factor = 4;

	String text;
	int slot;
	PixelFont pixelFont;
	boolean didMyJob = false;
	boolean delayedHighlight = false;

	ArrayList<MonsterPixel> myMonsters = new ArrayList<MonsterPixel>();

	public MonsterTextLine(World world, String text, int slot, PixelFont pixelFont) {
		super(world);
		this.text = text;
		this.slot = slot;
		this.pixelFont = pixelFont;
	}

	@Override
	public boolean canCrash() {
		return false;
	}

	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		if (!didMyJob) {
			int x = 0;
			for (char c : text.toCharArray()) {
				ArrayList<Vec2> list = pixelFont.getPixels(c);
				for (Vec2 v : list) {
					Vec2 w = new Vec2(v.x + x, v.y + slot * pixelFont.getHeight());
					MonsterPixel m = new MonsterPixel(world, w.mul(MonsterPixel.initialSize
							* factor));
					myMonsters.add(m);
				}
				x += pixelFont.getWidth(c) + pixelFont.characterSpacing;
			}

			for (Monster m : myMonsters) {
				m.position.x -= x * MonsterPixel.initialSize * factor / 2.0f;
				world.add(m);
			}
			didMyJob = true;

			if (delayedHighlight) {
				delayedHighlight = false;
				highlight();
			}
		}
	}

	public void highlight() {
		if (didMyJob)
			for (MonsterPixel m : myMonsters)
				m.highLight();
		else
			delayedHighlight = true;
	}

	public void lowlight() {
		for (MonsterPixel m : myMonsters)
			m.lowLight();
	}

	@Override
	public void destroy() {
	}
}
