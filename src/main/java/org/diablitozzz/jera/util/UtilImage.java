package org.diablitozzz.jera.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class UtilImage {
    
    /**
     * Проверяет эквивалентность двух картинок - по писксильно 
     */
    public static boolean equals(final BufferedImage imageA, final BufferedImage imageB) {
        
        if (imageA.getWidth() != imageB.getWidth()) {
            return false;
        }
        
        if (imageA.getHeight() != imageB.getHeight()) {
            return false;
        }
        
        for (int x = 0; x < imageA.getWidth(); x++) {
            for (int y = 0; y < imageA.getHeight(); y++) {
                
                if (imageA.getRGB(x, y) != imageB.getRGB(x, y)) {
                    return false;
                }
            }
            
        }
        
        return true;
    }
    
    public static BufferedImage fromBlob(final byte[] blob) throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
        final BufferedImage image = ImageIO.read(inputStream);
        return image;
    }
    
    public static String getImageType(final byte[] blob) throws IOException {
        
        ByteArrayInputStream inputStream = null;
        ImageInputStream stream = null;
        
        try {
            inputStream = new ByteArrayInputStream(blob);
            stream = ImageIO.createImageInputStream(inputStream);
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            
            if (!readers.hasNext()) {
                return null;
            }
            final ImageReader reader = readers.next();
            final String format = reader.getFormatName();
            reader.dispose();
            return format;
        } finally {
            UtilIO.closeForce(inputStream);
            UtilIO.closeForce(stream);
        }
    }
    
    public static void save(final BufferedImage image, final File file, final String format) throws IOException {
        ImageIO.write(image, format, file);
    }
    
    public static byte[] toBlob(final BufferedImage image, final String format) throws IOException {
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }
    
}
