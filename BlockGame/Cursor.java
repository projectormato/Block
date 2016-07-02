
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
    CursorBarrier barrier;

    public Cursor() {
    }

    public Cursor(Goal goal, Ball ball, Block[] blocks, CursorBarrier barrier) {
        this.goal = goal;
        this.ball = ball;
        this.blocks = blocks;
        this.barrier = barrier;
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

        double degree = Math.toRadians(getRotation());
        int width, height, barrierW, barrierH;
        width = getImage().getWidth();
        height = getImage().getHeight();
        barrierW = barrier.getImage().getWidth();
        barrierH = barrier.getImage().getHeight();

        // バリアをカーソルに追従させる
        barrier.setLocation(
                getX() + (int) (Math.cos(degree) * (width + barrierW)) / 2,
                getY() + (int) (Math.sin(degree) * (height + barrierH)) / 2);
        barrier.setRotation(getRotation());
    }
}
