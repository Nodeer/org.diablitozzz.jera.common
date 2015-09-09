package org.diablitozzz.jera.image;

import java.awt.Color;
import java.awt.Transparency;

public class ImageFilterColorToAlpha {

	/**
	 * Метод заменяет заданный цвет на альфа 
	 */
	final public static void filter(ImageModel image, Color color) {

		int[] argb = ImageColorUtil.allocateRgb(color);
		int[] alpha = ImageColorUtil.allocateRgb(Transparency.OPAQUE);

		int[][] pixels = image.getPixels();

		for (int i = 0; i < pixels.length; i++) {
			if (ImageColorUtil.rgbEquals(argb, pixels[i])) {
				pixels[i] = alpha;
			}
		}
	}

}
