
import greenfoot.*;

public class Cursor extends BaseActor {

    private Goal goal;
    private Ball ball;
    private Block[] blocks;
    private CursorBarrier barrier;

    public Cursor() {
    }

    public Cursor(Goal goal, Ball ball, Block[] blocks, CursorBarrier barrier) {
        this.goal = goal;
        this.ball = ball;
        this.blocks = blocks;
        this.barrier = barrier;
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

        // ボールをバリアに追従させる
        switch (((PlayWorld) getWorld()).getWorldStatus()) {
            case STAGE_START_MSG:
            case WAITING:
                ball.setLocation(
                        barrier.getX() + (int) (Math.cos(degree) * (barrierW + 10)) / 2,
                        barrier.getY() + (int) (Math.sin(degree) * (barrierH + 10)) / 2);
                ball.setRotation(getRotation());
                break;
        }
    }
}
