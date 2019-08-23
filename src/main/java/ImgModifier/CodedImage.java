package ImgModifier;

        import javax.imageio.ImageIO;
        import java.awt.image.BufferedImage;
        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.IOException;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.Base64;
        import java.util.InputMismatchException;

public class CodedImage {
    /*
    This is the main method of class CodedImage which calls out private method encodeDecode
     */
    public static void main(String[] args) {
        CodedImage.encodeDecode();
    }
    /*
    This method creates a buffered image from a web address, encodes it to Bse64 code, decodes it back to
    byte array, and returns an image file in jpg format into the temporary files folder.
    @param url - web location of the image
    @return - image file
    @throws - throws exceptions for malformed url, IO operation failure, and input mismatch
     */
    private static void encodeDecode() {
        try {
            URL url = new URL("https://www.elnet.ee/pildinaited/cover_images/bibimage_test/isbn_9789949526765_orig.jpg");
            BufferedImage bufferimage = ImageIO.read(url);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(bufferimage, "jpg", output);

            String encodedBytes = Base64.getEncoder().encodeToString(output.toByteArray());
            byte[] data = Base64.getDecoder().decode(encodedBytes);
            BufferedImage decodedImage = ImageIO.read(new ByteArrayInputStream(data));
            ImageIO.write(decodedImage, "jpg", new File(System.getProperty("java.io.tmpdir"), "DecodedImage.jpg"));
            output.flush();
            output.close();
        }
        catch (MalformedURLException ex) {
            System.out.println("MalformedURLException: " + ex.getMessage());
        }
        catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
        catch (InputMismatchException ex) {
            System.out.println("InputMismatchException: " + ex.getMessage());
        }
    }
}
