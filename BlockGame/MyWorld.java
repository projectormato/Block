import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1);
        prepare();
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        GreenfootImage goalimage = new GreenfootImage("goal.jpg");
        goalimage.scale(100,100);
        GreenfootImage cursorimage = new GreenfootImage("cursor.jpg");
        cursorimage.scale(50,50);
        cursor cursor = new cursor();
        addObject(cursor,139,388);
        cursor.setImage(cursorimage);
        goal goal = new goal();
        addObject(goal,390,391);
        goal.setImage(goalimage);
    }
}
