
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

        super.tick();
        move(speed);

        if (isAtEdge()) {
            ((PlayWorld) getWorld()).lose();
            return;
        }
    }

    @Override
    public int getAttackAbility(BaseActor defender) {
        if (defender instanceof CursorBarrier) {
            return 0;
        }
        return super.getAttackAbility(defender);
    }

    @Override
    public void fight(Damage damage) {
        super.fight(damage);

        BaseActor defender = (BaseActor) damage.getDefender();
        if (defender instanceof Goal) {
            System.out.println("--- GOAL ---");
            ((PlayWorld) getWorld()).win();
        } else {
            turn(180);
        }
    }
}
