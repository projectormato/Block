import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Cursor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cursor extends BaseActor
{
    @Override
    public void onKeyHolding()
    {
        // Add your action code here.
        if(Greenfoot.isKeyDown("up")){
            setLocation(getX(), getY() -1);
        }    else if(Greenfoot.isKeyDown("down")){
            setLocation(getX(), getY() +1);
        }else if(Greenfoot.isKeyDown("left")){
            setLocation(getX() -1, getY());
        }else if(Greenfoot.isKeyDown("right")){
            setLocation(getX() +1, getY());
        }
        if(isTouching(Goal.class)){
            System.out.println("touch!");
            setLocation(getX() +100, getY() -100);
        }
    }    
}
