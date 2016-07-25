
import greenfoot.GreenfootImage;
import java.awt.Color;

/**
 *
 * @author yuuki
 */
public class BootWorld extends BaseWorld {

    public BootWorld() {
        Config.load();

        prepare();
    }

    private void prepare() {
        // 背景を真っ黒にする
        GreenfootImage blackBg = new GreenfootImage(getWidth(), getHeight());
        blackBg.setColor(Color.BLACK);
        blackBg.fill();
        setBackground(blackBg);
    }

    @Override
    public void tick() {
        changeWorld("next");
    }
}
