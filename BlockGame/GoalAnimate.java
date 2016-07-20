
import greenfoot.GreenfootImage;

/**
 *
 * @author yuuki
 */
public class GoalAnimate extends BaseActor implements AnimationActor {

    // 通常時の画像
    private GreenfootImage normalImg;
    // 壊れた時の画像
    private GreenfootImage brokenImg;
    private int count = 0;
    private final int ANIME_SPEED = 5;
    private final int ANIME_STEP = 20;

    public GoalAnimate(GreenfootImage normalImg, GreenfootImage brokenImg) {
        this.normalImg = new GreenfootImage(normalImg);
        this.brokenImg = new GreenfootImage(brokenImg);
        setImage(normalImg);
    }

    @Override
    public void tick() {
        super.tick();

        int step = count / ANIME_SPEED;
        switch (getActorStatus()) {
            case ALIVE: {
                count++;

                if (count >= ANIME_SPEED * ANIME_STEP) {
                    setActorStatus(ActorStatus.DIED);
                    return;
                }

                if (count % ANIME_SPEED != 0) {
                    return;
                }

                int w = Math.max(normalImg.getWidth(), brokenImg.getHeight());
                int h = Math.max(normalImg.getHeight(), brokenImg.getHeight());
                // それぞれの画像の透過度
                int normalImageAlpha = (0xff / ANIME_STEP) * (ANIME_STEP - step);
                int brokenImageAlpha = (0xff / ANIME_STEP) * step;
                System.out.println("Goal: " + step + "\t" + normalImageAlpha + "\t" + brokenImageAlpha);

                // 通常時の画像は、元画像を破壊しながら更新
                Utils.updateMaxAlpha(normalImg, normalImageAlpha);
                // 壊れた時の画像は、不透明度が上がっていくアニメーションなので、元画像のコピーを更新
                GreenfootImage brokenImg = new GreenfootImage(this.brokenImg);
                Utils.updateMaxAlpha(brokenImg, brokenImageAlpha);

                GreenfootImage img = new GreenfootImage(w, h);
                Utils.drawImageToCenter(img, normalImg);
                Utils.drawImageToCenter(img, brokenImg);
                setImage(img, false);
                break;
            }
            case DIED: {
                count++;
                if (count >= ANIME_SPEED * ANIME_STEP) {
                    setActorStatus(ActorStatus.REMOVED);
                    return;
                }

                if (count % ANIME_SPEED != 0) {
                    return;
                }

                // 壊れた時の画像がフェードアウトしていくアニメーション
                // 壊れた時の画像を破壊しながら更新
                int brokenImageAlpha = (0xff / ANIME_STEP) * (ANIME_STEP - step);
                Utils.updateMaxAlpha(brokenImg, brokenImageAlpha);
                setImage(brokenImg);
                break;
            }
        }

    }

    @Override
    public void onDied() {
        super.onDied();
        // アニメーションの回数をリセットして、次のアニメーションをスタート
        count = 0;
    }
}
