
import greenfoot.*;
import java.awt.Color;

/**
 *
 * @author yuuki
 */
public class BlinkMessageBox extends MessageBox {

    private int blinkInterbal = 100;
    private Color originalFontColor;
    private GreenfootImage[] imageCache;
    private int count = 0;

    public BlinkMessageBox(String msgConfigName, int width, int height) {
        super(msgConfigName, width, height);
        prepare();
    }

    private void prepare() {
        originalFontColor = fontColor;
        // 背景色を透明にする
        backgroundColor = new Color(0, true);
    }

    /**
     * 画像のキャッシュを作成する
     */
    public void createImageCache() {
        imageCache = new GreenfootImage[blinkInterbal];
        double radian = 0;
        for (int i = 0; i < blinkInterbal; i++) {
            // Alpha値を更新
            int rgba = originalFontColor.getRGB() & 0xffffff;
            rgba |= (int) Math.abs(Math.cos(radian) * originalFontColor.getAlpha()) << 24;
            fontColor = new Color(rgba, true);
            radian += Math.PI * 2 / blinkInterbal;

            // 再描画してキャッシュに追加
            draw();
            imageCache[i] = new GreenfootImage(getImage());
        }
    }

    @Override
    public void tick() {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }
        if (imageCache == null) {
            createImageCache();
        }

        setImage(imageCache[count % blinkInterbal]);
        count++;
    }
}
