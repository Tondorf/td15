package org.deepserver.td15.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.*;

import javax.swing.ImageIcon;

import org.deepserver.td15.monster.MonsterSprite;
import org.lwjgl.BufferUtils;

public class Texture {
	private static Hashtable<String, Texture> gcTextures = new Hashtable<String, Texture>();

	private int texId;
	private int width;
	private int height;
	private float wRatio = 0;
	private float hRatio = 0;

	private static ColorModel glAlphaColorModel = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 8 }, true, false,
			ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

	private static ColorModel glColorModel = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);

	private Texture() {
	}

	private Texture(String ref) throws IOException, FileNotFoundException {
		URL url = MonsterSprite.class.getClassLoader().getResource(ref);

		// if (url == null)
		// throw new IOException("Texture not found: " + ref);

		// Load image:

		Image img = null;
		if (url != null)
			img = new ImageIcon(url).getImage();
		if (img == null) {
			File f = new File(ref);
			if (!f.exists())
				throw new FileNotFoundException("Texture image not Found: " + ref);
			img = new ImageIcon(ref).getImage();
		}

		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		this.width = bufferedImage.getWidth();
		this.height = bufferedImage.getHeight();

		// Query an id for this texture:
		IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);
		glGenTextures(textureIDBuffer);
		texId = textureIDBuffer.get(0);

		int srcPixelFormat = (bufferedImage.getColorModel().hasAlpha()) ? GL_RGBA
				: GL_RGB;

		glBindTexture(GL_TEXTURE_2D, texId);
		ByteBuffer textureBuffer = convertImageData(bufferedImage); // ,texture);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
				get2Fold(bufferedImage.getWidth()),
				get2Fold(bufferedImage.getHeight()), 0, srcPixelFormat,
				GL_UNSIGNED_BYTE, textureBuffer);
	}

	public static Texture load(String ref) throws IOException {
		Texture oTex = gcTextures.get(ref);
		if (oTex != null) {
			return oTex;
		}

		// load texture
		oTex = new Texture(ref);
		gcTextures.put(ref, oTex);

		return null;
	}

	private ByteBuffer convertImageData(BufferedImage bufferedImage) { // ,Texture
																		// texture)
																		// {
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;

		int texWidth = 2;
		int texHeight = 2;

		// find the closest power of 2 for the width and height
		// of the produced texture
		while (texWidth < bufferedImage.getWidth()) {
			texWidth *= 2;
		}
		while (texHeight < bufferedImage.getHeight()) {
			texHeight *= 2;
		}

		// texture.setTextureHeight(texHeight);
		// texture.setTextureWidth(texWidth);

		// create a raster that can be used by OpenGL as a source
		// for a texture
		if (bufferedImage.getColorModel().hasAlpha()) {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
					texWidth, texHeight, 4, null);
			texImage = new BufferedImage(glAlphaColorModel, raster, false,
					new Hashtable<>());
		} else {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
					texWidth, texHeight, 3, null);
			texImage = new BufferedImage(glColorModel, raster, false,
					new Hashtable<>());
		}

		// copy the source image into the produced image
		Graphics2D g = (Graphics2D)texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, texWidth, texHeight);
		AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -bufferedImage.getHeight()));
        g.transform(at);
		g.drawImage(bufferedImage, 0, 0, null);
	
		
		// build a byte buffer from the temporary image
		// that be used by OpenGL to produce a texture.
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
				.getData();

		imageBuffer = ByteBuffer.allocateDirect(data.length);
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);
		imageBuffer.flip();

		return imageBuffer;
	}

	private static int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret *= 2;
		}
		return ret;
	}

	public int getTextureID() {
		return texId;
	}

	public int getImageWidth() {
		return width;
	}

	public int getImageHeight() {
		return height;
	}

	public float getWidth() {
		if (wRatio == 0) {
			int texWidth = 2;
			while (texWidth < width)
				texWidth *= 2;
			wRatio = ((float) width) / texWidth;
		}
		return wRatio;
	}
	
	public float getHeight() {
		if (hRatio == 0) {
			int texHeight = 2;
			while (texHeight < width)
				texHeight *= 2;
			hRatio = ((float) width) / texHeight;
		}
		return hRatio;
	}
}
