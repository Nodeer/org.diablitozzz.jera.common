package org.diablitozzz.jera.image;

import java.awt.Transparency;

public class ImageFilterAlphaComposite {

	/**
	 * Метод если встречает в маске цвет - заменяет в image на alpha
	 */
	final public static void filter(ImageModel image, ImageModel mask, int maskColorRgb) {

		int[] maskColor = ImageColorUtil.allocateRgb(maskColorRgb);
		int[] alpha = ImageColorUtil.allocateRgb(Transparency.OPAQUE);

		int[][] imagePixels = image.getPixels();
		int[][] maskPixels = mask.getPixels();

		for (int i = 0; i < imagePixels.length; i++) {

			if (ImageColorUtil.rgbEquals(maskColor, maskPixels[i])) {
				imagePixels[i] = alpha;
			}
		}
	}

}
