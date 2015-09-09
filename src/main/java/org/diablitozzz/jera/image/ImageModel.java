package org.diablitozzz.jera.image;

import java.awt.image.BufferedImage;

public class ImageModel {

	private int width;
	private int height;
	private int[][] pixels;

	public ImageModel(BufferedImage image) {

		this.applyImage(image);
	}

	public void applyImage(BufferedImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();

		//load data
		this.pixels = new int[this.width * this.height][4];
		int tmp[] = new int[this.width * this.height];

		image.getRGB(0, 0, this.width, this.height, tmp, 0, this.width);
		for (int i = 0; i < tmp.length; i++) {

			ImageColorUtil.colorToRgb(tmp[i], this.pixels[i]);
		}
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof ImageModel)) {
			return false;
		}
		ImageModel in = (ImageModel) object;
		if (in.pixels.length != this.pixels.length) {
			return false;
		}
		for (int i = 0; i < this.pixels.length; i++) {

			if (in.pixels[i][0] != this.pixels[i][0]) {
				return false;
			}
			if (in.pixels[i][1] != this.pixels[i][1]) {
				return false;
			}
			if (in.pixels[i][2] != this.pixels[i][2]) {
				return false;
			}
			if (in.pixels[i][3] != this.pixels[i][3]) {
				return false;
			}
		}
		return true;
	}

	public int getHeight() {
		return this.height;
	}

	public int[][] getPixels() {
		return this.pixels;
	}

	public int getWidth() {
		return this.width;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public BufferedImage toImage() {
		return this.toImage(BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * @param imageType see BufferdImage.type
	 */
	public BufferedImage toImage(int imageType) {

		BufferedImage image = new BufferedImage(this.width, this.height, imageType);
		/*/
				//если прозрачный рисуем подложку
				if (imageType == BufferedImage.TYPE_INT_ARGB) {
					Graphics2D g = (Graphics2D) image.getGraphics();
					g.setComposite(AlphaComposite.Clear);
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, this.width, this.height);
					g.dispose();
				}
		/*/
		//relocate
		int[] rgb = new int[this.width * this.height];
		for (int i = 0; i < rgb.length; i++) {
			rgb[i] = ImageColorUtil.rgbToColor(this.pixels[i]);

		}
		image.setRGB(0, 0, this.width, this.height, rgb, 0, this.width);
		return image;
	}

}
