import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class cursor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class cursor extends Actor
{
    /**
     * Act - do whatever the cursor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
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
    if(isTouching(goal.class)){
          System.out.println("touch!");
          setLocation(getX() +100, getY() -100);
        }
    }    
}
