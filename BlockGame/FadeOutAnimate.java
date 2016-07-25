
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * Actorが壊れたときのアニメーションをする。通常の画像から爆発した画像に切り替わり、その後フェードアウトしていくアニメーションが表示される。
 * このアニメーションが終了するまで画面切り替えが行われない。また、Worldに追加するとすぐにアニメーションが開始される。
 *
 * 使用方法:
 * BaseActor.onDied()からこのクラスのサブクラスをWorldに追加し、自身はWorldから削除、または透明にする。
 *
 * @author yuuki0xff
 */
public class FadeOutAnimate extends BaseAnimationActor {

    // 通常時の画像
    private GreenfootImage normalImg;
    // 壊れた時の画像
    private GreenfootImage brokenImg;

    @Override
    public void addedToWorld(World world) {
        normalImg = Config.getImage(this, "bg");
        brokenImg = Config.getImage(this, "broken");
        setImage(normalImg);

        // Disabledになることは無いので、追加した瞬間からアニメーションをスタートする
        startAnimation();
    }

    @Override
    public void animate(int step) {
        super.animate(step);

        switch (getActorStatus()) {
            case ALIVE: {
                if (step >= animeStep) {
                    setActorStatus(ActorStatus.DIED);
                    return;
                }

                int w = Math.max(normalImg.getWidth(), brokenImg.getHeight());
                int h = Math.max(normalImg.getHeight(), brokenImg.getHeight());
                // それぞれの画像の透過度
                int normalImageAlpha = (0xff / animeStep) * (animeStep - step);
                int brokenImageAlpha = (0xff / animeStep) * step;

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
                if (step >= animeStep) {
                    setActorStatus(ActorStatus.REMOVED);
                    return;
                }

                // 壊れた時の画像がフェードアウトしていくアニメーション
                // 壊れた時の画像を破壊しながら更新
                int brokenImageAlpha = (0xff / animeStep) * (animeStep - step);
                Utils.updateMaxAlpha(brokenImg, brokenImageAlpha);
                setImage(brokenImg, false);
                break;
            }
        }

    }

    @Override
    public void onDied() {
        super.onDied();
        // アニメーションをリセットして、死んだ時のアニメーションをスタート
        resetAnimation();
    }
}
