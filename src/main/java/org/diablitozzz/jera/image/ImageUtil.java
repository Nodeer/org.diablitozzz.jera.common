package org.diablitozzz.jera.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageUtil {

	final public static BufferedImage copy(BufferedImage src) {

		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		result.getGraphics().drawImage(src, 0, 0, null);
		return result;
	}

	final public static BufferedImage copy(BufferedImage src, int type) {

		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), type);
		result.getGraphics().drawImage(src, 0, 0, null);
		return result;
	}

	final public static void mergeImage(BufferedImage src, BufferedImage dest) {

		int width = src.getWidth();
		int height = src.getHeight();
		int[] pixels = new int[width * height];
		dest.getRGB(0, 0, width, height, pixels, 0, width);
		src.setRGB(0, 0, width, height, pixels, 0, width);
	}

	final public static BufferedImage resize(BufferedImage originalImage, int width, int height) {

		BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
		Graphics2D g = resizedImage.createGraphics();

		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		g.finalize();

		return resizedImage;
	}

	final public static BufferedImage resizeByHeight(BufferedImage originalImage, int height) {

		double width = (double) height / (double) originalImage.getHeight() * originalImage.getWidth();
		return ImageUtil.resize(originalImage, (int) width, height);
	}

	final public static BufferedImage resizeByLargeSide(BufferedImage originalImage, int size) {

		if (originalImage.getWidth() > originalImage.getHeight()) {
			return ImageUtil.resizeByWidth(originalImage, size);
		}
		return ImageUtil.resizeByHeight(originalImage, size);
	}

	final public static BufferedImage resizeByWidth(BufferedImage originalImage, int width) {

		double height = (double) width / (double) originalImage.getWidth() * originalImage.getHeight();
		return ImageUtil.resize(originalImage, width, (int) height);
	}

}
