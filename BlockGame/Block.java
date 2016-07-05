
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Block extends BaseActor {

	@Override
	public void onDied(){
		super.onDied();
		(new GreenfootSound("se/block-broken.mp3")).play();
	}

	@Override
	public void fight(Damage damage){
		super.fight(damage);
	}
}
