
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

    // addedToWorldの描画で、背景がnormalColorで描画されるようにするため、trueにしている。
    private boolean isMouseHovering = true;
    private Color normalColor = Color.DARK_GRAY;
    private Color hoveringColor = Color.GRAY;

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
        draw();
    }

    @Override
    public void animate(int step) {
        super.animate(step);
        if (step > ANIME_STEP) {
            stopAnimation();
            return;
        }
        System.out.println("ImgBtn: update");

        // animate()メソッドは迂闊に呼び出せないので、画像を描画するロジックをdraw()に分離した
        draw(step);
    }

    /**
     * 画像を描画する。
     *
     * @param step アニメーションの進行状況
     */
    private void draw(int step) {
        // 丸みを帯びた四角形を描くために、大きめの画像サイズを確保する
        int width, height;
        final int diameter = normalImg.getHeight();
        final int radius = diameter / 2;
        width = normalImg.getWidth() + diameter;
        height = normalImg.getHeight();
        GreenfootImage img = new GreenfootImage(width, height);
        Graphics2D g = img.getAwtImage().createGraphics();

        // 左右が丸みを帯びた四角形を描く
        g.setColor(getColor(step));
        g.fillOval(0, 0, diameter, diameter);
        g.fillOval(width - diameter, 0, diameter, diameter);
        g.fillRect(radius, 0, width - diameter, height);
        g.dispose();

        // 通常時の画像を重ねて描画する
        img.drawImage(normalImg, radius, 0);
        Utils.updateMaxAlpha(img, maxAlpha);
        setImage(img);
    }

    private void draw() {
        draw(0);
    }

    /**
     * 現在のアニメーションの状態に合わせた、ボタンの背景にある丸みを帯びた四角形の色を返す
     *
     * @return
     */
    private Color getColor(int step) {
        int r, g, b;
        if (isMouseHovering) {
            // normal -> hovering
            r = normalColor.getRed() * (ANIME_STEP - step) + hoveringColor.getRed() * step;
            g = normalColor.getGreen() * (ANIME_STEP - step) + hoveringColor.getGreen() * step;
            b = normalColor.getBlue() * (ANIME_STEP - step) + hoveringColor.getBlue() * step;
        } else {
            // hovering -> normal
            r = normalColor.getRed() * step + hoveringColor.getRed() * (ANIME_STEP - step);
            g = normalColor.getGreen() * step + hoveringColor.getGreen() * (ANIME_STEP - step);
            b = normalColor.getBlue() * step + hoveringColor.getBlue() * (ANIME_STEP - step);
        }
        r /= ANIME_STEP;
        g /= ANIME_STEP;
        b /= ANIME_STEP;
        return new Color(r, g, b);
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
