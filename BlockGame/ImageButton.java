
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * 画像ボタン
 *
 * @author yuuki0xff
 */
public class ImageButton extends Button {

    private int count = 0;

    /**
     * keyに対応する画像のボタンを作成する。
     *
     * @param key 画像を探す際のキー
     */
    public ImageButton(String key) {
        this.key = key;
    }

    @Override
    public void tick() {
        // アニメーション
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);

        normalImg = Config.getImage(this, key);
        setImage(normalImg);
        pressingImg = new GreenfootImage(normalImg);
        // TODO: ボタンが押された時の画像を生成する処理を入れる
    }
}
