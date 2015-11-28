package org.deepserver.td15.screen;

import java.util.ArrayList;

import org.deepserver.td15.Client;
import org.deepserver.td15.Floathing;
import org.deepserver.td15.InputStatus;
import org.deepserver.td15.PixelFont;
import org.deepserver.td15.monster.MonsterPixel;
import org.deepserver.td15.monster.MonsterTextLine;
import org.deepserver.td15.sound.SoundEffect;
import org.joml.Vec3;

public class ScreenMenu extends Screen {

	protected PixelFont pixelFont = new PixelFont(20);
	protected Floathing y=new Floathing(pixelFont.getHeight()*1f);
	
	protected int slot = 0;
	protected int startSlot = 0;

	protected ArrayList<MenuEntry> list = new ArrayList<MenuEntry>();
	protected ArrayList<MonsterTextLine> lines = new ArrayList<MonsterTextLine>();

	protected InputStatus is=new InputStatus();
	
	@FunctionalInterface
	protected interface Caller {
		void call();
	}

	protected class MenuEntry {
		String text;
		Caller f;

		public MenuEntry(String text, Caller f) {
			this.text = text;
			this.f = f;
		}
	}

	public ScreenMenu(Client client) {
		super(client);
	}

	public void add(String text, Caller f) {
		list.add(new MenuEntry(text, f));
	}

	public void init(int startSlot) {
		for (int i = list.size() - 1; i >= 0; i--) {
			MenuEntry m = list.get(i);
			MonsterTextLine tl = new MonsterTextLine(world, m.text, slot++,
					pixelFont);
			world.add(tl);
			lines.add(tl);
		}

		slot = startSlot;
		lines.get((lines.size() - 1) - startSlot).highlight();
	}

	@Override
	public void action(double delta,InputStatus is) {
		super.action(delta,is);
		
		if (is.downEvent) {// down
			is.downEvent=false;
			if (slot<lines.size()-1) {
				lines.get((lines.size() - 1) - slot).lowlight();
				slot++;
				lines.get((lines.size() - 1) - slot).highlight();
				world.screen.audio.play(SoundEffect.FRR);
			}
		}
		if (is.upEvent) {// up
			is.upEvent=false;
			if (slot > 0) {
				lines.get((lines.size() - 1) - slot).lowlight();

				slot--;
				lines.get((lines.size() - 1) - slot).highlight();
				world.screen.audio.play(SoundEffect.FRR);
			}
		}
		if (is.selectEvent) { // select=space or enter
			is.selectEvent=false;
			if (list.get(slot).f!=null) list.get(slot).f.call();
		}

		float aimFor=(lines.size()-2-slot)*pixelFont.getHeight()*MonsterPixel.initialSize*MonsterTextLine.factor;
		y.update(delta, aimFor);
		
		world.setCamera(new Vec3(0.0f,y.get(),7.0f),
		        new Vec3(0.0f,y.get(),0.0f),
		        new Vec3(0.0f,1.0f,0.0f));

	}

	@Override
	public void draw() {
		super.draw();
	}
}
