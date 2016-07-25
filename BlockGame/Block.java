
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Block extends BaseActor implements AnimationActor, NoWaitActor {

    private GreenfootSound onDiedSound;

    private final int ANIME_SPEED = 5;
    private final int ANIME_STEP = 10;
    private final int ANIME_MAX_SIZE = 3; // 元画像の何倍まで拡大するか
    private GreenfootImage originalImg;
    private int count = 0;

    public Block() {
        onDiedSound = Config.getSound(this.getClass(), "broken");
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        // アニメーションのために、加工の画像が必要だから、事前に取得しておく
        if (originalImg == null) {
            originalImg = getImage();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (getActorStatus() == ActorStatus.DIED) {
            // 壊れた時のアニメーション
            // 透明度を減らしながら、画像を拡大するアニメーションをする
            int step = count / ANIME_SPEED;
            if (count < ANIME_STEP * ANIME_SPEED) {
                int maxAlpha = (int) (0xff * (double) (ANIME_STEP - step) / ANIME_STEP);
                int w = (int) (originalImg.getWidth() * (1.0 + (ANIME_MAX_SIZE - 1) * (0.0 + step) / ANIME_STEP));
                int h = (int) (originalImg.getHeight() * (1.0 + (ANIME_MAX_SIZE - 1) * (0.0 + step) / ANIME_STEP));
                GreenfootImage img = new GreenfootImage(originalImg);

                // サイズと透過度を変更
                img.scale(w, h);
                Utils.updateMaxAlpha(img, maxAlpha);
                // 画像を更新
                setImage(img);
            } else {
                // アニメーションが終わったら削除
                setActorStatus(ActorStatus.REMOVED);
                return;
            }
            count++;
        }
    }

    @Override
    public void onDied() {
        super.onDied();
        if (Config.getBoolean("enableSoundEffect") && onDiedSound != null) {
            onDiedSound.play();
        }
    }
}
