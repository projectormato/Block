
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Goal here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Goal extends BaseActor {

    public Goal() {
        GreenfootImage goalImage = new GreenfootImage("goal/jupiter.png");
        goalImage.scale(100, 100);
        setImage(goalImage);
    }

	@Override
	public void onDied(){
		(new GreenfootSound("se/core-destroyed.mp3")).play();
	}
}
