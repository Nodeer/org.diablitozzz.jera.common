package org.diablitozzz.jera.image;

import java.awt.image.BufferedImage;

/**
 * Фильтр производит модуляцию цветов картинки 
 * @see http://www.imagemagick.org/Usage/color_mods/#modulate
 */
public class ImageFilterColorModulation {

	final public static BufferedImage filter(BufferedImage image, double brightness, double saturation, double hue) {

		ImageModel model = new ImageModel(image);
		ImageFilterColorModulation.filter(model, brightness, saturation, hue);
		return model.toImage();
	}

	/**
	 * @param brightness - знаение в процентах насколько изменяется яркость - если 100 то нет изменений
	 * @param saturation - знаение в процентах насколько изменяется насыщенность - если 100 то нет изменений
	 * @param hue - знаение в процентах насколько изменяется цветовой тон - если 100 то нет изменений
	 */
	final public static void filter(ImageModel image, double brightness, double saturation, double hue) {

		brightness = brightness / 100d;
		saturation = saturation / 100d;
		hue = hue / 100d;

		int[][] pixels = image.getPixels();
		double[] hsb = new double[3];

		for (int i = 0; i < pixels.length; i++) {

			ImageColorUtil.rgbToHsb(pixels[i], hsb);

			hsb[0] = hsb[0] * hue;
			hsb[1] = hsb[1] * saturation;
			hsb[2] = hsb[2] * brightness;

			ImageColorUtil.hsbToRgb(hsb, pixels[i]);
		}
	}

}
