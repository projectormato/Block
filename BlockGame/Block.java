
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Block extends BaseActor {

    private GreenfootSound onDiedSound;

    public Block() {
        onDiedSound = Config.getSound(this.getClass(), "broken");
    }

    @Override
    public void onDied() {
        super.onDied();
        if (Config.getBoolean("enableSoundEffect") && onDiedSound != null) {
            onDiedSound.play();
        }
    }

    @Override
    public void fight(Damage damage) {
        super.fight(damage);
    }
}
