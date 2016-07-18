
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Goal here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Goal extends BaseActor {

    @Override
    public void onDied() {
        if (Config.getBoolean("enableSoundEffect")) {
            GreenfootSound sound = Config.getSound(this.getClass(), "broken");
            if (sound != null) {
                sound.play();
            }
        }
    }
}
