package org.deepserver.td15;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vec2;

public class PixelFont {

	protected final String fontName = "ocraextended.ttf";
	public final int characterSpacing = 1;

	protected Font font;
	protected float fontSize;
	protected BufferedImage cache;
	protected int cacheWidth;
	protected int cacheHeight;

	class Letter {
		public Letter() {
			list = new ArrayList<Vec2>();
			width = 3;
		}

		public ArrayList<Vec2> list;
		public int width;
	}

	protected HashMap<Character, Letter> map = new HashMap<Character, Letter>();

	public PixelFont(float fontSize) {
		this.fontSize = fontSize;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontName)).deriveFont(
					fontSize);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		BufferedImage tmp = new BufferedImage(8, 8, BufferedImage.TYPE_BYTE_GRAY);
		tmp.getGraphics().setFont(font);
		FontMetrics fm = tmp.getGraphics().getFontMetrics();

		cacheWidth = fm.getMaxAdvance();
		cacheHeight = fm.getMaxAscent() + fm.getMaxDescent();
		cache = new BufferedImage(cacheWidth, cacheHeight, BufferedImage.TYPE_BYTE_GRAY);
		cache.getGraphics().setFont(font);
	}

	protected void init(char c) {
		if (map.containsKey(c))
			return;

		Letter l = new Letter();

		Graphics g = cache.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, cacheWidth, cacheHeight);

		g.setColor(Color.WHITE);
		g.drawString(new Character(c).toString(), 0, cacheHeight - 1);

		byte[] pixels = ((DataBufferByte) cache.getRaster().getDataBuffer()).getData();

		int i = 0;
		int w = 0;
		for (int y = 0; y < cacheHeight; y++) {
			for (int x = 0; x < cacheWidth; x++) {
				if (pixels[i++] != 0) {
					l.list.add(new Vec2(x, -y));
					if (x > w)
						w = x;
				}
			}
		}

		l.width = w + 1;
		map.put(new Character(c), l);
	}

	protected Letter getLetter(char c) {
		init(c);
		return map.get(c);
	}

	public int getWidth(char c) {
		init(c);
		return map.get(c).width;
	}

	public int getWidth(String s) {
		int sum = 0;
		for (char c : s.toCharArray()) {
			sum += getWidth(c) + characterSpacing;
		}
		return sum;
	}

	public int getHeight() {
		return (int) cacheHeight;
	}

	public ArrayList<Vec2> getPixels(char c) {
		init(c);

		return map.get(c).list;
	}
}
