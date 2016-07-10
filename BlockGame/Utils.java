
import greenfoot.GreenfootImage;
import java.awt.Graphics2D;

/**
 *
 * @author yuuki
 */
public class Utils {

    /**
     * Rotates an image. Actually rotates a new copy of the image.
     *
     * @param img The image to be rotated
     * @param angle The angle in degrees
     * @return The rotated image
     */
    public static GreenfootImage rotateImage(GreenfootImage img, double angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = img.getWidth();
        int h = img.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin);
        int newh = (int) Math.floor(h * cos + w * sin);
        GreenfootImage gimg = new GreenfootImage(neww, newh);
        Graphics2D g = gimg.getAwtImage().createGraphics();

        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(img.getAwtImage(), null);
        g.dispose();

        return gimg;
    }

}
