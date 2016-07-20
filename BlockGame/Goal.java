
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Goal here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Goal extends BaseActor implements AnimationActor {

    private GreenfootImage originalImg;
    private GreenfootImage brokenImg;

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        originalImg = Config.getImage(this, "bg");
        brokenImg = Config.getImage(this, "broken");
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
        getWorld().addObject(new GoalAnimate(originalImg, brokenImg),
                getX(), getY());
        // 元のゴールは、透明にして見えなくする。ただし、当たり判定用のシルエットは更新する必要がない
        GreenfootImage img=new GreenfootImage(originalImg);
        Utils.updateMaxAlpha(img, 0);
        setImage(img);

        // 削除してしまうと、カーソルの向きが決められなくなる。
        // そのため、死んだ状態で放置
    }
}
