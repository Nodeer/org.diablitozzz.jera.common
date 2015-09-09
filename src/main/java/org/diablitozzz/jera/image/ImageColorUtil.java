package org.diablitozzz.jera.image;

import java.awt.Color;

public class ImageColorUtil {

	/**
	 * sRGB to XYZ conversion matrix
	 */
	final private static double[][] XYZ_M = {
			{ 0.4124d, 0.3576d, 0.1805d },
			{ 0.2126d, 0.7152d, 0.0722d },
			{ 0.0193d, 0.1192d, 0.9505d }
	};

	/**
	 * XYZ to sRGB conversion matrix
	 */
	final private static double[][] XYZ_Mi = {
			{ 3.2406d, -1.5372d, -0.4986d },
			{ -0.9689d, 1.8758d, 0.0415d },
			{ 0.0557d, -0.2040d, 1.0570d }
	};

	final private static double[] XYZ_WHITE_POINT = { 95.05d, 100.0d, 108.8999999999999d };

	final private static double LAB_T_A = Math.pow(6.0d / 29.0d, 3.0d);
	final private static double LAB_T_B = (1.0d / 3.0d) * ((29.0d / 6.0d) * (29.0d / 6.0d));
	final private static double LAB_T_C = 4.0d / 29.0d;

	final private static double[] tmp3 = new double[3];

	final public static double[] allocateLab(Color color) {

		double[] lab = new double[3];
		int[] argb = ImageColorUtil.allocateRgb(color);
		ImageColorUtil.rgbToLab(argb, lab);
		return lab;
	}

	final public static int[] allocateRgb(Color color) {

		int[] argb = new int[4];
		ImageColorUtil.colorToRgb(color.getRGB(), argb);
		return argb;
	}

	final public static int[] allocateRgb(int color) {

		int[] argb = new int[4];
		ImageColorUtil.colorToRgb(color, argb);
		return argb;
	}

	final public static String colorToHex(Color color) {
		int[] argb = new int[] { color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue() };
		return ImageColorUtil.rgbToHex(argb);
	}

	final public static void colorToRgb(int color, int argb[]) {

		argb[0] = (color >> 24) & 0xFF;
		argb[1] = (color >> 16) & 0xFF;
		argb[2] = (color >> 8) & 0xFF;
		argb[3] = (color >> 0) & 0xFF;
	}

	/**
	 * Евклидова дистанция между точками в пространсве 
	 */
	final public static double getHsbDifference(double[] hsbA, double[] hsbB) {

		return Math.pow(
				Math.pow((hsbA[0] - hsbB[0]), 2d)
						+ Math.pow((hsbA[1] - hsbB[1]), 2d)
						+ Math.pow((hsbA[2] - hsbB[2]), 2d)
				, 0.5d);

	}

	/**
	* http://www.brucelindbloom.com/index.html?Eqn_DeltaE_CIE2000.html
	* http://en.wikipedia.org/wiki/Color_difference
	* Возвращает разницу в цветах по CIE2000
	*/
	final public static double getLabDifference(double[] labA, double[] labB) {

		double l1 = labA[0];
		double a1 = labA[1];
		double b1 = labA[2];

		double l2 = labB[0];
		double a2 = labB[1];
		double b2 = labB[2];

		//Wheight factor
		double WHTL = 1.0d;
		double WHTC = 1.0d;
		double WHTH = 1.0d;

		//xCX
		double xCX1 = Math.sqrt(a1 * a1 + b1 * b1);
		double xCX2 = Math.sqrt(a2 * a2 + b2 * b2);
		double xCX = (xCX1 + xCX2) / 2.0d;

		//xGX
		double xGX = 0.5d * (1.0d - Math.sqrt(Math.pow(xCX, 7.0d) / (Math.pow(xCX, 7.0d) + Math.pow(25.0d, 7.0d))));

		//xC1 xH1
		double xNN1 = (1.0d + xGX) * a1;
		double xC1 = Math.sqrt(xNN1 * xNN1 + b1 * b1);
		double xH1 = (xNN1 == 0.0d && b1 == 0.0d) ? 0.0d : Math.toDegrees(Math.atan2(b1, xNN1));
		if (xH1 < 0.0d) {
			xH1 = 360.0d + xH1;
		}

		// xC2 xH2
		double xNN2 = (1.0d + xGX) * a2;
		double xC2 = Math.sqrt(xNN2 * xNN2 + b2 * b2);
		double xH2 = (xNN2 == 0.0d && b2 == 0.0d) ? 0.0d : Math.toDegrees(Math.atan2(b2, xNN2));
		if (xH2 < 0.0d) {
			xH2 = 360.0d + xH2;
		}

		double xDL = l2 - l1;
		double xDC = xC2 - xC1;

		// xDH
		double xDH;
		if ((xC1 * xC2) == 0.0d) {
			xDH = 0.0d;
		}
		else {
			double xNN = xH2 - xH1;
			if (Math.abs(xNN) <= 180.0d) {
				xDH = xH2 - xH1;
			}
			else {
				if (xNN > 180.0d) {
					xDH = xH2 - xH1 - 360.0d;
				}
				else {
					xDH = xH2 - xH1 + 360.0d;
				}
			}
		}
		xDH = 2.0d * Math.sqrt(xC1 * xC2) * Math.sin(Math.toRadians(xDH / 2.0d));

		// xHX
		double xLX = (l1 + l2) / 2.0d;
		double xCY = (xC1 + xC2) / 2.0d;
		double xHX;
		if ((xC1 * xC2) == 0.0d) {
			xHX = xH1 + xH2;
		}
		else {
			double xNN = Math.abs(xH1 - xH2);
			if (xNN > 180.0d) {
				if ((xH2 + xH1) < 360.0d) {
					xHX = xH1 + xH2 + 360.0d;
				}
				else {
					xHX = xH1 + xH2 - 360.0d;
				}
			}
			else {
				xHX = xH1 + xH2;
			}
			xHX = xHX / 2.0d;
		}

		//xTX
		double xTX = 1.0d
				- 0.17d * Math.cos(Math.toRadians(xHX - 30.0d))
				+ 0.24d * Math.cos(Math.toRadians(2.0d * xHX))
				+ 0.32d * Math.cos(Math.toRadians(3.0d * xHX + 6.0d))
				- 0.20d * Math.cos(Math.toRadians(4.0d * xHX - 63.0d));

		double xPH = 30.0d * Math.exp(-((xHX - 275.0d) / 25.0d) * ((xHX - 275.0d) / 25.0d));

		double xRC = 2.0d * Math.sqrt(Math.pow(xCY, 7.0d) / (Math.pow(xCY, 7.0d) + Math.pow(25.0d, 7.0d)));

		double xSL = 1.0d
				+ ((0.015d * ((xLX - 50.0d) * (xLX - 50.0d)))
				/ Math.sqrt(20.0d + ((xLX - 50.0d) * (xLX - 50.0d))));

		double xSC = 1.0d + 0.045d * xCY;
		double xSH = 1.0d + 0.015d * xCY * xTX;
		double xRT = -Math.sin(Math.toRadians(2.0d * xPH)) * xRC;

		xDL = xDL / (WHTL * xSL);
		xDC = xDC / (WHTC * xSC);
		xDH = xDH / (WHTH * xSH);

		double delta = Math.sqrt(xDL * xDL + xDC * xDC + xDH * xDH + xRT * xDC * xDH);

		return delta;
	}

	/**
	 * Определяет контраст цвета, от серой гаммы
	 */
	final public static int getRgbContrast(int[] argb) {

		return ((argb[1] * 299) + (argb[2] * 587) + (argb[3] * 114)) / 100;
	}

	/**
	 * Евклидова дистанция между точками в пространсве 
	 */
	final public static double getRgbDifference(int[] argbA, int[] argbB) {

		return Math.pow(
				Math.pow((argbA[0] - argbB[0]), 2d)
						+ Math.pow((argbA[1] - argbB[1]), 2d)
						+ Math.pow((argbA[2] - argbB[2]), 2d)
						+ Math.pow((argbA[3] - argbB[3]), 2d)
				, 0.5d);

	}

	final public static int getRgbDifferenceFromGray(int[] argbA, int[] argbB) {

		return (100 * Math.abs(argbA[0] - argbB[1])
				+ 30 * Math.abs(argbA[1] - argbB[1])
				+ 59 * Math.abs(argbA[2] - argbB[2])
				+ 11 * Math.abs(argbA[3] - argbB[3]));
	}

	/**
	 * Евклидова дистанция между точками в пространсве 
	 */
	final public static double getXyzDifference(double[] xyzA, double[] xyzB) {

		return Math.pow(
				Math.pow((xyzA[0] - xyzB[0]), 2d)
						+ Math.pow((xyzA[1] - xyzB[1]), 2d)
						+ Math.pow((xyzA[2] - xyzB[2]), 2d)
				, 0.5d);

	}

	final public static Color hexToColor(String hex) {

		int[] argb = new int[4];
		ImageColorUtil.hexToRgb(hex, argb);
		return new Color(argb[1], argb[2], argb[3], argb[0]);
	}

	final public static void hexToRgb(String hex, int argb[]) {

		hex = hex.replaceAll("#", "").trim();
		int length = hex.length();

		if (length == 3) {
			int color = Integer.parseInt(hex, 16);
			int red = (color & 0xF00) >> 8;
			int green = (color & 0xF0) >> 4;
			int blue = (color & 0xF);
			red = (red << 4) + red;
			green = (green << 4) + green;
			blue = (blue << 4) + blue;

			argb[0] = 255;
			argb[1] = red;
			argb[2] = green;
			argb[3] = blue;
		}
		else if (length == 6) {
			int color = Integer.parseInt(hex, 16);
			int red = (color & 0xFF0000) >> 16;
			int green = (color & 0xFF00) >> 8;
			int blue = (color & 0xFF);

			argb[0] = 255;
			argb[1] = red;
			argb[2] = green;
			argb[3] = blue;
		}
		else {
			throw new IllegalArgumentException("Color pattern not matched #RGB, #RRGGBB");
		}
	}

	final public static boolean hsbEquals(double hsbA[], double[] hsbB) {
		return ImageColorUtil.xyzEquals(hsbA, hsbB);
	}

	final public static void hsbToRgb(double[] hsb, int argb[]) {

		int r = 0;
		int g = 0;
		int b = 0;
		double saturation = hsb[1];
		double brightness = hsb[2];
		double hue = hsb[0];

		if (saturation > 1d) {
			saturation = 1d;
		}
		else if (saturation < 0d) {
			saturation = 0d;
		}
		if (brightness > 1d) {
			brightness = 1d;
		}
		else if (brightness < 0d) {
			brightness = 0d;
		}

		if (saturation == 0d) {
			r = g = b = (int) (brightness * 255.0d + 0.5d);
		} else {
			double h = (hue - Math.floor(hue)) * 6.0d;
			double f = h - Math.floor(h);
			double p = brightness * (1.0d - saturation);
			double q = brightness * (1.0d - saturation * f);
			double t = brightness * (1.0d - (saturation * (1.0d - f)));

			switch ((int) h) {
				case 0:
					r = (int) (brightness * 255.0d + 0.5d);
					g = (int) (t * 255.0d + 0.5d);
					b = (int) (p * 255.0d + 0.5d);
					break;
				case 1:
					r = (int) (q * 255.0d + 0.5d);
					g = (int) (brightness * 255.0d + 0.5d);
					b = (int) (p * 255.0d + 0.5d);
					break;
				case 2:
					r = (int) (p * 255.0d + 0.5d);
					g = (int) (brightness * 255.0d + 0.5d);
					b = (int) (t * 255.0d + 0.5d);
					break;
				case 3:
					r = (int) (p * 255.0d + 0.5d);
					g = (int) (q * 255.0d + 0.5d);
					b = (int) (brightness * 255.0d + 0.5d);
					break;
				case 4:
					r = (int) (t * 255.0d + 0.5d);
					g = (int) (p * 255.0d + 0.5d);
					b = (int) (brightness * 255.0d + 0.5d);
					break;
				case 5:
					r = (int) (brightness * 255.0d + 0.5d);
					g = (int) (p * 255.0d + 0.5d);
					b = (int) (q * 255.0d + 0.5d);
					break;
				default:
					break;
			}
		}

		argb[0] = 255;
		argb[1] = r;
		argb[2] = g;
		argb[3] = b;
	}

	final public static boolean labEquals(double labA[], double[] labB) {
		return ImageColorUtil.xyzEquals(labA, labB);
	}

	final public static void labToRgb(double[] lab, int[] argb) {

		double[] xyz = ImageColorUtil.tmp3;
		ImageColorUtil.labToXyz(lab, xyz);
		ImageColorUtil.xyzToRgb(xyz, argb);
	}

	final public static void labToXyz(double[] lab, double[] xyz) {

		double y = (lab[0] + 16.0d) / 116.0d;
		double y3 = Math.pow(y, 3.0d);
		double x = (lab[1] / 500.0d) + y;
		double x3 = Math.pow(x, 3.0d);
		double z = y - (lab[2] / 200.0d);
		double z3 = Math.pow(z, 3.0d);

		if (y3 > 0.008856d) {
			y = y3;
		}
		else {
			y = (y - (16.0d / 116.0d)) / 7.787d;
		}
		if (x3 > 0.008856d) {
			x = x3;
		}
		else {
			x = (x - (16.0d / 116.0d)) / 7.787d;
		}
		if (z3 > 0.008856d) {
			z = z3;
		}
		else {
			z = (z - (16.0d / 116.0d)) / 7.787d;
		}

		xyz[0] = x * ImageColorUtil.XYZ_WHITE_POINT[0];
		xyz[1] = y * ImageColorUtil.XYZ_WHITE_POINT[1];
		xyz[2] = z * ImageColorUtil.XYZ_WHITE_POINT[2];
	}

	final public static boolean rgbEquals(int argbA[], int[] argbB) {

		if (argbA[0] != argbB[0]) {
			return false;
		}
		if (argbA[1] != argbB[1]) {
			return false;
		}
		if (argbA[2] != argbB[2]) {
			return false;
		}
		if (argbA[3] != argbB[3]) {
			return false;
		}
		return true;
	}

	final public static int rgbToColor(int argb[]) {

		return ((argb[0] & 0xFF) << 24) |
				((argb[1] & 0xFF) << 16) |
				((argb[2] & 0xFF) << 8) |
				((argb[3] & 0xFF) << 0);
	}

	final public static String rgbToHex(int[] argb) {
		return String.format("#%02x%02x%02x", argb[1], argb[2], argb[3]);
	}

	final public static void rgbToHsb(int argb[], double[] hsb) {

		double hue;
		double saturation;
		double brightness;

		double r = argb[1];
		double g = argb[2];
		double b = argb[3];

		double cmax = (r > g) ? r : g;
		if (b > cmax) {
			cmax = b;
		}
		double cmin = (r < g) ? r : g;
		if (b < cmin) {
			cmin = b;
		}

		brightness = (cmax) / 255.0d;
		if (cmax != 0d) {
			saturation = (cmax - cmin) / (cmax);
		} else {
			saturation = 0d;
		}
		if (saturation == 0d) {
			hue = 0d;
		} else {
			double redc = (cmax - r) / (cmax - cmin);
			double greenc = (cmax - g) / (cmax - cmin);
			double bluec = (cmax - b) / (cmax - cmin);
			if (r == cmax) {
				hue = bluec - greenc;
			} else if (g == cmax) {
				hue = 2.0d + redc - bluec;
			} else {
				hue = 4.0d + greenc - redc;
			}
			hue = hue / 6.0d;
			if (hue < 0d) {
				hue = hue + 1.0d;
			}
		}

		hsb[0] = hue;
		hsb[1] = saturation;
		hsb[2] = brightness;
	}

	final public static void rgbToLab(int[] argb, double[] lab) {

		double[] xyz = ImageColorUtil.tmp3;
		ImageColorUtil.rgbToXyz(argb, xyz);
		ImageColorUtil.xyzToLab(xyz, lab);
	}

	final public static void rgbToXyz(int[] argb, double[] xyz) {

		// convert 0..255 into 0..1
		double r = argb[1] / 255.0d;
		double g = argb[2] / 255.0d;
		double b = argb[3] / 255.0d;

		// assume sRGB
		if (r <= 0.04045d) {
			r = r / 12.92d;
		}
		else {
			r = Math.pow(((r + 0.055d) / 1.055d), 2.4d);
		}
		if (g <= 0.04045d) {
			g = g / 12.92d;
		}
		else {
			g = Math.pow(((g + 0.055d) / 1.055d), 2.4d);
		}
		if (b <= 0.04045d) {
			b = b / 12.92d;
		}
		else {
			b = Math.pow(((b + 0.055d) / 1.055d), 2.4d);
		}

		r = r * 100.0d;
		g = g * 100.0d;
		b = b * 100.0d;

		// [X Y Z] = [r g b][M]
		xyz[0] = (r * ImageColorUtil.XYZ_M[0][0]) + (g * ImageColorUtil.XYZ_M[0][1]) + (b * ImageColorUtil.XYZ_M[0][2]);
		xyz[1] = (r * ImageColorUtil.XYZ_M[1][0]) + (g * ImageColorUtil.XYZ_M[1][1]) + (b * ImageColorUtil.XYZ_M[1][2]);
		xyz[2] = (r * ImageColorUtil.XYZ_M[2][0]) + (g * ImageColorUtil.XYZ_M[2][1]) + (b * ImageColorUtil.XYZ_M[2][2]);
	}

	final private static double toLabF(double t) {

		if (t > ImageColorUtil.LAB_T_A) { // (6/29)^3
			return Math.pow(t, 1.0d / 3.0d);
		}
		return ImageColorUtil.LAB_T_B * t + ImageColorUtil.LAB_T_C; //1/3*(29/6)*(29/6)*$t + 4/29
	}

	final public static boolean xyzEquals(double xyzA[], double[] xyzB) {

		//округляем до 6 знаков

		double o = 1000000d;

		if ((int) (xyzA[0] * o) != (int) (xyzB[0] * o)) {
			return false;
		}
		if ((int) (xyzA[1] * o) != (int) (xyzB[1] * o)) {
			return false;
		}
		if ((int) (xyzA[2] * o) != (int) (xyzB[2] * o)) {
			return false;
		}
		return true;
	}

	final public static void xyzToLab(double[] xyz, double[] lab) {

		double x = xyz[0];
		double y = xyz[1];
		double z = xyz[2];

		double xW = ImageColorUtil.XYZ_WHITE_POINT[0];
		double yW = ImageColorUtil.XYZ_WHITE_POINT[1];
		double zW = ImageColorUtil.XYZ_WHITE_POINT[2];

		lab[0] = 116.0d * ImageColorUtil.toLabF(y / yW) - 16.0d;
		lab[1] = 500.0d * (ImageColorUtil.toLabF(x / xW) - ImageColorUtil.toLabF(y / yW));
		lab[2] = 200.0d * (ImageColorUtil.toLabF(y / yW) - ImageColorUtil.toLabF(z / zW));
	}

	final public static void xyzToRgb(double[] xyz, int[] argb) {

		double x = xyz[0] / 100.0d;
		double y = xyz[1] / 100.0d;
		double z = xyz[2] / 100.0d;

		// [r g b] = [X Y Z][Mi]
		double r = (x * ImageColorUtil.XYZ_Mi[0][0]) + (y * ImageColorUtil.XYZ_Mi[0][1]) + (z * ImageColorUtil.XYZ_Mi[0][2]);
		double g = (x * ImageColorUtil.XYZ_Mi[1][0]) + (y * ImageColorUtil.XYZ_Mi[1][1]) + (z * ImageColorUtil.XYZ_Mi[1][2]);
		double b = (x * ImageColorUtil.XYZ_Mi[2][0]) + (y * ImageColorUtil.XYZ_Mi[2][1]) + (z * ImageColorUtil.XYZ_Mi[2][2]);

		// assume sRGB
		if (r > 0.0031308d) {
			r = ((1.055d * Math.pow(r, 1.0d / 2.4d)) - 0.055d);
		}
		else {
			r = (r * 12.92d);
		}
		if (g > 0.0031308d) {
			g = ((1.055d * Math.pow(g, 1.0d / 2.4d)) - 0.055d);
		}
		else {
			g = (g * 12.92d);
		}
		if (b > 0.0031308d) {
			b = ((1.055d * Math.pow(b, 1.0d / 2.4d)) - 0.055d);
		}
		else {
			b = (b * 12.92d);
		}

		r = (r < 0) ? 0 : r;
		g = (g < 0) ? 0 : g;
		b = (b < 0) ? 0 : b;

		//result
		argb[0] = 255;
		argb[1] = (int) Math.round(r * 255);
		argb[2] = (int) Math.round(g * 255);
		argb[3] = (int) Math.round(b * 255);
	}

}
