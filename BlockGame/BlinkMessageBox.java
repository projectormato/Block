
import java.awt.Color;

/**
 *
 * @author yuuki
 */
public class BlinkMessageBox extends MessageBox {

    private double blinkInterbal = 100;
    private double radian = 0;
    private Color originalFontColor;

    public BlinkMessageBox(String msgConfigName, int width, int height) {
        super(msgConfigName, width, height);
        prepare();
    }

    private void prepare() {
        originalFontColor = fontColor;
        // 背景色を透明にする
        backgroundColor = new Color(0, true);
    }

    @Override
    public void tick() {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        // Alpha値を更新
        int rgba = originalFontColor.getRGB() & 0xffffff;
        rgba |= (int) Math.abs(Math.cos(radian) * originalFontColor.getAlpha()) << 24;
        fontColor = new Color(rgba, true);
        radian += Math.PI * 2 / blinkInterbal;

        // 再描画
        draw();
    }
}
