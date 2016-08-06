
import greenfoot.GreenfootImage;
import java.awt.Color;

/**
 * <p>
 * Greenfootでこのゲームを開始した時に、真っ先に表示されるWorld。
 * Greenfootのスタートボタンを押す前から音楽が再生されてしまう問題を回避するために使用。</p>
 * <p>
 * ゲームのスタート画面を押すか、直ちに他のWorldに切り替える。</p>
 *
 * @author yuuki0xff
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
