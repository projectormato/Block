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
        GreenfootImage goalimage = new GreenfootImage("goal.png");
        goalimage.scale(100,100);
        GreenfootImage cursorimage = new GreenfootImage("cursor.png");
        cursorimage.scale(50,50);
        Cursor cursor = new Cursor();
        addObject(cursor,139,388);
        cursor.setImage(cursorimage);
        Goal goal = new Goal();
        addObject(goal,390,391);
        goal.setImage(goalimage);
        Boll boll = new Boll();
        addObject(boll,536,571);
        Block block = new Block();
        addObject(block,407,289);
        Block block2 = new Block();
        addObject(block2,520,382);
        Block block3 = new Block();
        addObject(block3,259,397);
        Block block4 = new Block();
        addObject(block4,398,474);
    }
}
