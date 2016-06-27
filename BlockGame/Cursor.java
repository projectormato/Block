
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class Cursor here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Cursor extends BaseActor {

    Goal goal;
    Ball ball;
    Block[] blocks;

    public Cursor() {
    }

    public Cursor(Goal goal, Ball ball, Block[] blocks) {
        this.goal = goal;
        this.ball = ball;
        this.blocks = blocks;

        List<String> keys = new ArrayList<>();
        keys.add("up");
        keys.add("down");
        keys.add("left");
        keys.add("right");

        setListenKeys(keys);
    }

    @Override
    public void onKeyHolding(String key) {
        // Add your action code here.
        if (Greenfoot.isKeyDown("up")) {
            setLocation(getX(), getY() - 1);
        } else if (Greenfoot.isKeyDown("down")) {
            setLocation(getX(), getY() + 1);
        } else if (Greenfoot.isKeyDown("left")) {
            setLocation(getX() - 1, getY());
        } else if (Greenfoot.isKeyDown("right")) {
            setLocation(getX() + 1, getY());
        }
        if (isTouching(Goal.class)) {
            System.out.println("touch!");
            setLocation(getX() + 100, getY() - 100);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (isTouching(ball.getClass())) {
            ball.setRotation(getRotation());
        }
    }

    @Override
    public void onMouseMoved(MouseInfo mouse) {
        setLocation(mouse.getX(), mouse.getY());
        turnTowards(goal.getX(), goal.getY());
    }
}
