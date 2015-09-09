package org.diablitozzz.jera.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageFilterResize {

	final public static void filter(ImageModel image, int width, int height) {

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();

		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(image.toImage(), 0, 0, width, height, null);
		g.dispose();
		g.finalize();

		image.applyImage(result);
	}

	final public static void filterByHeight(ImageModel image, int height) {

		double width = (double) height / (double) image.getHeight() * image.getWidth();
		ImageFilterResize.filter(image, (int) width, height);
	}

	final public static void filterByLargeSide(ImageModel image, int size) {

		if (image.getWidth() > image.getHeight()) {
			ImageFilterResize.resizeByWidth(image, size);
		}
		else {
			ImageFilterResize.filterByHeight(image, size);
		}
	}

	final public static void resizeByWidth(ImageModel image, int width) {

		double height = (double) width / (double) image.getWidth() * image.getHeight();
		ImageFilterResize.filter(image, width, (int) height);
	}

}
