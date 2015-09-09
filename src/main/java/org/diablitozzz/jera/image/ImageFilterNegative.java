package org.diablitozzz.jera.image;

public class ImageFilterNegative {

	final public static void filter(ImageModel image) {

		int[][] pixels = image.getPixels();

		int rgb, a, r;

		for (int i = 0; i < pixels.length; i++) {

			rgb = ImageColorUtil.rgbToColor(pixels[i]);
			a = rgb & 0xff000000;
			r = a | (~rgb & 0x00ffffff);
			ImageColorUtil.colorToRgb(r, pixels[i]);
		}

	}
}
