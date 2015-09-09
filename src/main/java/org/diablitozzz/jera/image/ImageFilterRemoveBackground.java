package org.diablitozzz.jera.image;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * 
 * Фильтр удаления фона
 */
public class ImageFilterRemoveBackground {

	final public static BufferedImage filter(BufferedImage image) {

		//создаём маску
		ImageModel mask = new ImageModel(image);

		//негатив
		ImageFilterNegative.filter(mask);
		//модуляция
		ImageFilterColorModulation.filter(mask, 210, 50, 200);
		//порог
		ImageFilterContrastThresold.filter(mask, 500, Color.WHITE.getRGB(), Transparency.OPAQUE);

		//заменяем альфа на альфа по маске
		ImageModel result = new ImageModel(image);
		ImageFilterAlphaComposite.filter(result, mask, Transparency.OPAQUE);

		//возвращаем
		return result.toImage();
	}

	final public static void filterFast(ImageModel image) {

		int[][] in = image.getPixels();
		int[] mask = new int[4];
		double[] hsb = new double[3];
		int rgb;

		double brightness = 410d / 100d;
		double saturation = 50d / 100d;
		double hue = 200d / 100d;

		int[] opaque = ImageColorUtil.allocateRgb(Transparency.OPAQUE);
		int thresold = 1000;

		for (int i = 0; i < in.length; i++) {

			//маска
			mask = in[i].clone();

			//негатив
			rgb = ImageColorUtil.rgbToColor(mask);
			rgb = (rgb & 0xff000000) | (~rgb & 0x00ffffff);
			ImageColorUtil.colorToRgb(rgb, mask);

			//модуляция
			ImageColorUtil.rgbToHsb(mask, hsb);
			hsb[0] = hsb[0] * hue;
			hsb[1] = hsb[1] * saturation;
			hsb[2] = hsb[2] * brightness;
			ImageColorUtil.hsbToRgb(hsb, mask);

			//порог  и маска
			//контраст - дистанция до серего цвета
			int contrast = ImageColorUtil.getRgbContrast(mask);
			if (contrast < thresold) {
				in[i] = opaque;
			}
		}
	}
}
