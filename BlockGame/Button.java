
import greenfoot.*;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * アニメーションをするボタン。
 *
 * @author yuuki0xff
 */
public abstract class Button extends BaseAnimationActor {

    GreenfootImage normalImg, pressingImg;
    int bgcolor = 0;
    int color = 0;
    private GreenfootSound onMouseInSound;
    private GreenfootSound onMouseClickedSound;
    protected String key;
    protected int maxAlpha = 0xff;

    // addedToWorldの描画で、背景がnormalColorで描画されるようにするため、trueにしている。
    protected boolean isMouseHovering = true;
    protected Color normalColor = Color.DARK_GRAY;
    protected Color hoveringColor = Color.GRAY;

    public Button() {
        onMouseInSound = Config.getSound(this.getClass(), "mouseIn");
        onMouseClickedSound = Config.getSound(this.getClass(), "mouseClicked");
    }

    /**
     * このActorの透過度を変更します。この値が考慮されるのは、アニメーションを行っている最中だけです。
     *
     * @param maxAlpha
     */
    public void setMaxAlpha(int maxAlpha) {
        this.maxAlpha = maxAlpha;
    }

    /**
     * 画像を描画する。
     *
     * @param step アニメーションの進行状況
     */
    protected void draw(int step) {
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

    protected void draw() {
        draw(0);
    }

    /**
     * 現在のアニメーションの状態に合わせた、ボタンの背景にある丸みを帯びた四角形の色を返す
     *
     * @param step
     * @return
     */
    protected Color getColor(int step) {
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
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }
        if (Config.getBoolean("enableSoundEffect") && onMouseInSound != null) {
            onMouseInSound.play();
        }
        isMouseHovering = true;
        startAnimation();
    }

    @Override
    public void onMouseOut(MouseInfo mouse) {
        super.onMouseOut(mouse);
        isMouseHovering = false;
        startAnimation();
    }

    @Override
    public void onMouseDown(MouseInfo mouse) {
        super.onMouseDown(mouse);
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        // setImage(pressingImg);
    }

    @Override
    public void onMouseUp(MouseInfo mouse) {
        super.onMouseUp(mouse);
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        // setImage(normalImg);
    }

    @Override
    public void onMouseClicked(MouseInfo mouse) {
        super.onMouseClicked(mouse);
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        if (Config.getBoolean("enableSoundEffect") && onMouseClickedSound != null) {
            onMouseClickedSound.play();
        }
    }
}
