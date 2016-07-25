
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import greenfoot.World;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 画像ボタン
 *
 * @author yuuki0xff
 */
public class ImageButton extends Button {

    private boolean isMouseHovering = false;

    /**
     * keyに対応する画像のボタンを作成する。
     *
     * @param key 画像を探す際のキー
     */
    public ImageButton(String key) {
        this.key = key;
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);

        normalImg = Config.getImage(this, key);
        setImage(normalImg);
    }

    @Override
    public void animate(int step) {
        super.animate(step);
        System.out.println("ImgBtn: update");

        // 丸みを帯びた四角形を描くために、大きめの画像サイズを確保する
        int width, height;
        final int diameter = normalImg.getHeight();
        final int radius = diameter / 2;
        width = normalImg.getWidth() + diameter;
        height = normalImg.getHeight();
        GreenfootImage img = new GreenfootImage(width, height);
        Graphics2D g = img.getAwtImage().createGraphics();

        // 左右が丸みを帯びた四角形を描く
        g.setColor(Color.DARK_GRAY);
        g.fillOval(0, 0, diameter, diameter);
        g.fillOval(width - diameter, 0, diameter, diameter);
        g.fillRect(radius, 0, width - diameter, height);
        g.dispose();

        // 通常時の画像を重ねて描画する
        img.drawImage(normalImg, radius, 0);
        Utils.updateMaxAlpha(img, maxAlpha);
        setImage(img);
    }

    @Override
    public void onMouseIn(MouseInfo mouse) {
        super.onMouseIn(mouse);
        isMouseHovering = true;
        startAnimation();
    }

    @Override
    public void onMouseOut(MouseInfo mouse) {
        super.onMouseOut(mouse);
        isMouseHovering = false;
        startAnimation();
    }
}
