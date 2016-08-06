
import greenfoot.GreenfootImage;
import greenfoot.World;

public class CursorBarrier extends BaseActor {

    // 反射角を決める際の係数
    private int k = 5;
    // アニメーションの進行状況
    // count < 0ならアニメーションは停止
    private int count = -1;
    private final int ANIME_SPEED = 5;
    private final int ANIME_STEP = 5;
    // アニメーションに必要な画像
    private GreenfootImage normalImg;
    private GreenfootImage effectImg;

    public CursorBarrier(Ball ball) {
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        normalImg = Config.getImage(this, "bg");
        effectImg = Config.getImage(this, "effect");
    }

    @Override
    public int getAttackAbility(BaseActor defender) {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (getActorStatus() != ActorStatus.ALIVE || count < 0) {
            return;
        }

        count++;

        if (count % ANIME_SPEED != 0) {
            return;
        }

        // ボールと衝突したときのアニメーション
        int step = count / ANIME_SPEED;
        int waveAlpha;
        if (step <= ANIME_STEP) {
            // 透過度を下げる
            waveAlpha = (0xff / ANIME_STEP) * step;
        } else if (step <= 2 * ANIME_STEP) {
            // 透過度を上げる
            waveAlpha = (0xff / ANIME_STEP) * (2 * ANIME_STEP - step);
        } else {
            // アニメーションを停止
            count = -1;
            waveAlpha = 0;
        }

        if (waveAlpha == 0) {
            setImage(normalImg, false);
        } else {
            GreenfootImage img = new GreenfootImage(effectImg);
            Utils.updateMaxAlpha(img, waveAlpha);
            img.drawImage(normalImg, 0, 0);
            setImage(img, false);
        }
    }

    @Override
    public void fight(Damage damage) {
        super.fight(damage);

        // アニメーションを開始する
        if (count < 0) {
            count = 0;
        }
    }
}
