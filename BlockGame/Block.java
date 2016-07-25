
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Block extends BaseAnimationActor implements NoWaitActor {

    private GreenfootSound onDiedSound;

    private final int ANIME_MAX_SIZE = 3; // 元画像の何倍まで拡大するか
    private GreenfootImage originalImg;

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
    public void animate(int step) {
        super.animate(step);
        if (getActorStatus() == ActorStatus.DIED) {
            // 壊れた時のアニメーション
            // 透明度を減らしながら、画像を拡大するアニメーションをする
            if (step < animeStep) {
                int maxAlpha = (int) (0xff * (double) (animeStep - step) / animeStep);
                int w = (int) (originalImg.getWidth() * (1.0 + (ANIME_MAX_SIZE - 1) * (0.0 + step) / animeStep));
                int h = (int) (originalImg.getHeight() * (1.0 + (ANIME_MAX_SIZE - 1) * (0.0 + step) / animeStep));
                GreenfootImage img = new GreenfootImage(originalImg);

                // サイズと透過度を変更
                img.scale(w, h);
                Utils.updateMaxAlpha(img, maxAlpha);
                // 画像を更新
                setImage(img);
            } else {
                // アニメーションが終わったら削除
                setActorStatus(ActorStatus.REMOVED);
            }
        }
    }

    @Override
    public void onDied() {
        super.onDied();
        if (Config.getBoolean("enableSoundEffect") && onDiedSound != null) {
            onDiedSound.play();
        }
        startAnimation();
    }
}
