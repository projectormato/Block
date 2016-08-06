
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 惑星。これを破壊するとゲームクリア。 破壊されるときは、アニメーションを再生する。
 *
 * @author yuuki0xff
 */
public class Goal extends BaseActor implements AnimationActor, NoWaitActor {

    private GreenfootImage originalImg;

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        originalImg = Config.getImage(this, "bg");
    }

    @Override
    public void onDied() {
        super.onDied();
        if (Config.getBoolean("enableSoundEffect")) {
            GreenfootSound sound = Config.getSound(this.getClass(), "broken");
            if (sound != null) {
                sound.play();
            }
        }
        // ゴールのアニメーションを開始する
        getWorld().addObject(new GoalAnimate(), getX(), getY());
        // 元のゴールは、透明にして見えなくする。ただし、当たり判定用のシルエットは更新する必要がない
        GreenfootImage img = new GreenfootImage(originalImg);
        Utils.updateMaxAlpha(img, 0);
        setImage(img);

        // 削除してしまうと、カーソルの向きが決められなくなる。
        // そのため、死んだ状態で放置
    }
}
