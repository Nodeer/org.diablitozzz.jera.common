/*
 * Copyright (c) 2012, diablitozzz.org All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: * Redistributions
 * of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of diablitozzz.org nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.diablitozzz.jera.hash;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.BitSet;

public class PHashCodec {

    public static long getHashAsLong(int size, int smallerSize, BufferedImage image) {

        PHashCodec codec = new PHashCodec(size, smallerSize);
        return codec.getHashAsLong(image);
    }

    private final int size;

    private final int smallerSize;

    private final ColorConvertOp colorConvert;

    private final double[] c;

    public PHashCodec() {
        this(32, 8);
    }

    public PHashCodec(int size, int smallerSize)
    {
        this.size = size;
        this.smallerSize = smallerSize;
        this.c = new double[this.size];
        for (int i = 1; i < this.size; i++) {
            this.c[i] = 1;
        }
        this.c[0] = 1 / Math.sqrt(2.0);
        this.colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    }

    /**
     * Compute the average value.
     * Like the Average Hash, compute the mean DCT value (using only
     * the 8x8 DCT low-frequency values and excluding the first term
     * since the DC coefficient can be significantly different from
     * the other values and will throw off the average).
     * 
     * 5. Вычислить среднее значение. 
     * Как и в хэше по среднему, здесь вычисляется среднее значение DCT, 
     * оно вычисляется на блоке 8x8 и нужно исключить из расчёта самый первый 
     * коэффициент, чтобы убрать из описания хэша пустую информацию, например, одинаковые цвета.
     */
    private double computeAverage(double[][] dctVals)
    {
        double total = 0;
        for (int x = 0; x < this.smallerSize; x++) {
            for (int y = 0; y < this.smallerSize; y++) {
                total += dctVals[x][y];
            }
        }
        total -= dctVals[0][0];

        double avg = total / ((this.smallerSize * this.smallerSize) - 1);
        return avg;
    }

    /** 
     * Compute the DCT.
     * The DCT separates the image into a collection of frequencies
     * and scalars. While JPEG uses an 8x8 DCT, this algorithm uses a 32x32 DCT.
     */
    private double[][] computeTheDCT(BufferedImage img)
    {
        double[][] vals = new double[this.size][this.size];
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                vals[x][y] = this.getBlue(img, x, y);
            }
        }
        return vals;
    }

    private int getBlue(BufferedImage img, int x, int y)
    {
        return (img.getRGB(x, y)) & 0xff;
    }

    /**
     * Returns a 'binary string' (like. 001010111011100010) which is easy to do a hamming distance on.
     */
    public BitSet getHash(BufferedImage img)
    {
        //BufferedImage img = ImageIO.read(is);

        // 1. Reduce size.
        BufferedImage imgCur = this.reduceSize(img);

        // 2. Reduce color.
        imgCur = this.reduceColor(imgCur);

        // 3. Compute the DCT.
        double[][] vals = this.computeTheDCT(imgCur);

        // 4. Reduce the DCT.
        double[][] dctVals = this.reduceDCT(vals);

        // 5. Compute the average value.
        double avg = this.computeAverage(dctVals);

        // 6. Further reduce the DCT.
        return this.makeHashFromDCT(dctVals, avg);
    }

    public long getHashAsLong(BufferedImage image)
    {
        BitSet hash = this.getHash(image);
        long[] hashLA = hash.toLongArray();

        if (hashLA.length == 0) {
            return 0L;
        }
        return hashLA[0];
    }

    private BufferedImage grayscale(BufferedImage img)
    {
        this.colorConvert.filter(img, img);
        return img;
    }

    /**
     * Further reduce the DCT.
     * This is the magic step. Set the 64 hash bits to 0 or 1
     * depending on whether each of the 64 DCT values is above or
     * below the average value. The result doesn't tell us the
     * actual low frequencies; it just tells us the very-rough
     * relative scale of the frequencies to the mean. The result
     * will not vary as long as the overall structure of the image
     * remains the same; this can survive gamma and color histogram
     * adjustments without a problem.
     * 
     * 6. Ещё сократить DCT. 
     * Тоже магический шаг. 
     * Присвойте каждому из 64 DCT-значений 0 или 1 в зависимости от того, оно больше или меньше среднего. 
     * Такой вариант уже выдержит без проблем гамма-коррекцию или изменение гистограммы.
     */
    private BitSet makeHashFromDCT(double[][] dctVals, double avg)
    {
        //int size = (this.smallerSize - 1) * (this.smallerSize - 1);
        int size = (this.smallerSize) * (this.smallerSize);
        BitSet hash = new BitSet(size);

        //byte[] hash = new byte[size];

        int i = 0;
        for (int x = 0; x < this.smallerSize; x++) {
            for (int y = 0; y < this.smallerSize; y++) {
                //      if (x != 0 && y != 0) {
                boolean val = (dctVals[x][y] > avg) ? true : false;
                hash.set(i, val);
                i++;
                //    }
            }
        }
        return hash;
    }

    /**
     * Reduce color.
     * The image is reduced to a grayscale just to further simplify the number of computations.
     * 
     * Убрать цвет. 
     * Аналогично, цветовые каналы убирают, чтобы упростить дальнейшие вычисления.
     */
    private BufferedImage reduceColor(BufferedImage img) {
        return this.grayscale(img);
    }

    /**
     * Дискретное косинусное преобразование
     * DCT function stolen from http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java
     * Reduce the DCT.
     * This is the magic step. While the DCT is 32x32, just keep the
     * top-left 8x8. Those represent the lowest frequencies in the picture.
     * 
     * Запустить дискретное косинусное преобразование. 
     * DCT разбивает картинку на набор частот и векторов. 
     * В то время как алгоритм JPEG прогоняет DCT на блоках 8x8, 
     * в данном алгоритме DCT работает на 32x32.
     */
    private double[][] reduceDCT(double[][] f)
    {
        int size = this.size;
        double[][] factory = new double[size][size];
        for (int u = 0; u < size; u++)
        {
            for (int v = 0; v < size; v++)
            {
                double sum = 0.0;
                for (int i = 0; i < size; i++)
                {
                    for (int j = 0; j < size; j++)
                    {
                        sum += Math.cos(((2 * i + 1) / (2.0 * size)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * size)) * v * Math.PI) * (f[i][j]);
                    }
                }
                sum *= ((this.c[u] * this.c[v]) / 4.0);
                factory[u][v] = sum;
            }
        }
        return factory;
    }

    /**
     * Reduce size.
     * Like Average Hash, pHash starts with a small image.
     * However, the image is larger than 8x8; 32x32 is a good size.
     * This is really done to simplify the DCT computation and not because it is needed to reduce the high frequencies.
     * 
     * Уменьшить размер. 
     * Как и в случае хэша по среднему, pHash работает на маленьком размере картинки. 
     * Однако, здесь изображение больше, чем 8х8, вот 32x32 хороший размер. 
     * На самом деле это делается ради упрощения DCT, а не для устранения высоких частот.
     */
    private BufferedImage reduceSize(BufferedImage image)
    {
        int width = this.size;
        int height = this.size;
        return this.resize(image, width, height);
    }

    private BufferedImage resize(BufferedImage image, int width, int height)
    {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
