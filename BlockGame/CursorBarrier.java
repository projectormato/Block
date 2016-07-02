
public class CursorBarrier extends BaseActor {

    private Ball ball;

    public CursorBarrier(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void tick() {
        super.tick();
        if (isTouching(ball.getClass())) {
            ball.setRotation(getRotation());
        }
    }
}
