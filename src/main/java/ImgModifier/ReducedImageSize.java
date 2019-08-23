package ImgModifier;

import javax.imageio.*;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class ReducedImageSize {
    /*
    This is the main method of class ReducedImageSize which calls out private methods reduceImage and packImage
     */
    public static void main(String[] args) {
        ReducedImageSize.reduceImage();
        ReducedImageSize.packImage();
    }

    /*
    This method creates an image with reduced measures, compared to the original image.
    @param width - width of the original image
    @param height - height of the original image
    @param proportion - width and height proportion of the original and output images
    @param newWidth - width of the output image
    @param newHeight - height of the output image
    @return - image file
    @throws - throws exceptions for IO operation failure
     */
    private static void reduceImage() {
        try {
            File inputFile = new File(System.getProperty("java.io.tmpdir") + "DecodedImage.jpg");
            int pos = inputFile.getName().lastIndexOf(".");
            String suffix = inputFile.getName().substring(pos + 1);
            Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
            ImageReader reader = iter.next();
            ImageInputStream stream = new FileImageInputStream(inputFile);
            reader.setInput(stream);
            float width = reader.getWidth(reader.getMinIndex());
            float height = reader.getHeight(reader.getMinIndex());
            reader.dispose();

            float proportion = (height / width);
            float newWidth = 100;
            float newHeight = (float) (newWidth * proportion);
            BufferedImage image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(inputFile);

            BufferedImage outImage = null;
            ImageIcon iIcon = new ImageIcon(image);
            outImage = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D gTwoD = (Graphics2D) outImage.createGraphics();
            gTwoD.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            gTwoD.drawImage(iIcon.getImage(), 0, 0, (int) newWidth, (int) newHeight, null);
            gTwoD.dispose();

            File outputFile = new File(System.getProperty("java.io.tmpdir") + "DecodedReduced.jpg");
            ImageIO.write(outImage, "jpg", outputFile);
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
    }

    /*
    This method creates an image with reduced file size in bytes, compared to the original image.
    @param quality - measure of data compression
    @return - image file
    @throws - throws exceptions for IO operation failure
     */
    private static void packImage() {
        try {
            File inputFile = new File(System.getProperty("java.io.tmpdir") + "DecodedImage.jpg");
            File outputFile = new File(System.getProperty("java.io.tmpdir") + "DecodedCompressed.jpg");

            BufferedImage image = ImageIO.read(inputFile);
            OutputStream out = new FileOutputStream(outputFile);
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                float quality = 0.1f;
                param.setCompressionQuality(quality);
            }
            writer.write(null, new IIOImage(image, null, null), param);

            out.close();
            ios.close();
            writer.dispose();
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
    }
}
