package org.diablitozzz.jera.image;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageCompositeFilter {

	final public static void filter(BufferedImage src, BufferedImage mask, Composite composite) {

		Graphics2D g = (Graphics2D) src.getGraphics();
		g.setComposite(composite);
		g.drawImage(mask, 0, 0, null);
		g.dispose();
		g.finalize();

	}
}
