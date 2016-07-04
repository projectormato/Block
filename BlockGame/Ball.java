
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class Ball here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ball extends BaseActor {

    int speed = 3;

    @Override
    public void tick() {
        if (getActorStatus() != ActorStatus.ALIVE) {
            return;
        }

        List objs;
        super.tick();
        move(speed);

        if (getIntersectingObjects(Goal.class).size() > 0) {
            System.out.println("--- GOAL ---");
            ((PlayWorld) getWorld()).win();
            return;
        }

        objs = getIntersectingObjects(Block.class);
        if (objs.size() > 0) {
            // TODO: blockにダメージを与える
            Damage.faights(this, objs.toArray());
            turn(180);
            return;
        }

        if (isAtEdge()) {
            ((PlayWorld) getWorld()).lose();
            return;
        }
    }
}
