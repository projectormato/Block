
import greenfoot.World;

/**
 * 画像ボタン
 *
 * @author yuuki0xff
 */
public class ImageButton extends Button {

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
        // animate()メソッドは迂闊に呼び出せないので、画像を描画するロジックをdraw()に分離した
        draw(step);
    }
}
