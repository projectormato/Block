
/**
 * アニメーションをするActorの親クラス。<br>
 * アニメーションの実装に便利なメソッドを提供する。
 *
 * @author yuuki0xff
 */
public abstract class BaseAnimationActor extends BaseActor implements AnimationActor {

    private int count = -1;
    protected int animeSpeed = 5;
    protected int animeStep = 20;

    @Override
    public void tick() {
        super.tick();

        // アニメーションが停止しているとき
        if (count < 0) {
            return;
        }

        int step = count / animeSpeed;
        count++;
        // フレームを間引きするべきとき
        if (count % animeSpeed != 0) {
            return;
        }

        animate(step);
    }

    /**
     * アニメーションを停止する
     */
    public void stopAnimation() {
        count = -1;
    }

    /**
     * アニメーションを開始する。
     */
    public void startAnimation() {
        if (count < 0) {
            count = 0;
        }
    }

    public void resetAnimation() {
        count = 0;
    }

    /**
     * アニメーションが行われているときは、1フレームごとに呼び出される。 画像や位置の更新をここで行う。
     *
     * @param step アニメーションの進行状況
     */
    public void animate(int step) {
    }
}
