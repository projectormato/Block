
import java.awt.Color;
import greenfoot.GreenfootImage;

/**
 * メッセージボックスの半透明の背景
 *
 * @author yuuki0xff
 */
public class Overlay extends BaseActor {

    GreenfootImage img;

    /**
     * 指定したサイズ・色の背景を作成する
     *
     * @param width
     * @param height
     * @param color 背景色
     */
    public Overlay(int width, int height, Color color) {
        img = new GreenfootImage(width, height);
        img.setColor(color);
        img.fill();
        setImage(img);
    }
}
