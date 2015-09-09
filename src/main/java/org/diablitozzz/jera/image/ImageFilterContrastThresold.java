package org.diablitozzz.jera.image;

import java.awt.Color;

/**
 * Фильтр заменяет двумя цветами, менее или более контрастные цвета 
 *
 */
public class ImageFilterContrastThresold {

	public static void filter(ImageModel image, int thresold) {
		ImageFilterContrastThresold.filter(image, thresold, Color.WHITE, Color.BLACK);
	}

	public static void filter(ImageModel image, int thresold, Color more, Color less) {
		ImageFilterContrastThresold.filter(image, thresold, more.getRGB(), less.getRGB());
	}

	public static void filter(ImageModel image, int thresold, int moreRgb, int lessRgb) {

		int[][] pixels = image.getPixels();
		int[] argbM = ImageColorUtil.allocateRgb(moreRgb);
		int[] argbL = ImageColorUtil.allocateRgb(lessRgb);

		for (int i = 0; i < pixels.length; i++) {

			//контраст - дистанция до серего цвета
			int contrast = ImageColorUtil.getRgbContrast(pixels[i]);
			if (contrast >= thresold) {
				pixels[i] = argbM;
			}
			else {
				pixels[i] = argbL;
			}
		}

	}

}
