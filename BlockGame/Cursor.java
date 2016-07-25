
import greenfoot.*;

public class Cursor extends BaseActor implements AnimationActor, NoWaitActor {

    private Goal goal;
    private Ball ball;
    private Block[] blocks;
    private CursorBarrier barrier;

    private boolean isDied = false;

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
        super.onMouseMoved(mouse);

        // 死んでいたら何もしない
        // 死んだ後はactorStatusはDISABLEDになるため、フラグでチェックをしている
        if (isDied) {
            return;
        }

        setLocation(mouse.getX(), mouse.getY());
        turnTowards(goal.getX(), goal.getY());

        double radian = Math.toRadians(getRotation());
        int width, height, barrierW, barrierH;
        width = getWidth();
        height = getHeight();
        barrierW = barrier.getWidth();
        barrierH = barrier.getHeight();

        // バリアをカーソルに追従させる
        barrier.setLocation(
                getX() + (int) (Math.cos(radian) * (width + barrierW)) / 2,
                getY() + (int) (Math.sin(radian) * (height + barrierH)) / 2);
        barrier.setRotation(getRotation());

        // ボールをバリアに追従させる
        switch (((PlayWorld) getWorld()).getWorldStatus()) {
            case STAGE_START_MSG:
            case WAITING:
                // 角度0のときの、barrierの中心からのballの位置のオフセット
                int dx = barrierW / 2 + ball.getWidth() / 2;
                int dy = 0;

                // barrierの中心の位置を原点として、回転移動した地点をballの位置とする
                ball.setLocation(barrier.getX() + (int) (dx * Math.cos(radian)),
                        barrier.getY() + (int) (dx * Math.sin(radian)));
                ball.setRotation(getRotation());
                break;
        }
    }

    @Override
    public void onDied() {
        super.onDied();
        isDied = true;
        // カーソルが爆発するアニメーションを開始
        CursorAnimate cursorAnimate = new CursorAnimate();
        getWorld().addObject(cursorAnimate, getX(), getY());
        cursorAnimate.setRotation(getRotation());

        // カーソルを透明にする
        GreenfootImage img = new GreenfootImage(getImage());
        Utils.updateMaxAlpha(img, 0);
        setImage(img);
    }
}
