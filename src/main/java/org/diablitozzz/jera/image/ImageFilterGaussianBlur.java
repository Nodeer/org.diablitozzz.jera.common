package org.diablitozzz.jera.image;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ImageFilterGaussianBlur {

	final private static ConvolveOp createFilter(int radius, float sigma, boolean horizontal) {
		if (radius < 1) {
			radius = 1;
		}

		int size = radius * 2 + 1;
		float[] data = new float[size];
		//float sigma = radius / 3.0f;
		float twoSigmaSquare = 2.0f * sigma * sigma;
		float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
		float total = 0.0f;

		for (int i = -radius; i <= radius; i++) {
			float distance = i * i;
			int index = i + radius;
			data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
			total += data[index];
		}

		for (int i = 0; i < data.length; i++) {
			data[i] /= total;
		}

		Kernel kernel = null;
		if (horizontal) {
			kernel = new Kernel(size, 1, data);
		} else {
			kernel = new Kernel(1, size, data);
		}
		return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	}

	final public static BufferedImage filter(BufferedImage image, int radius, float sigma) {

		BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		ConvolveOp filter = ImageFilterGaussianBlur.createFilter(radius, sigma, false);
		ConvolveOp hFilter = ImageFilterGaussianBlur.createFilter(radius, sigma, true);

		filter.filter(image, out);
		hFilter.filter(image, out);

		return out;
	}

	final public static void filter(ImageModel image, int radius, float sigma) {

		BufferedImage src = image.toImage();
		BufferedImage out = ImageFilterGaussianBlur.filter(src, radius, sigma);
		image.applyImage(out);
	}

}
