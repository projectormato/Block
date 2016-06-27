
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
