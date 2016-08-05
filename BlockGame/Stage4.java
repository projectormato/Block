
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class Stage4 extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;

    public Stage4() {
        super("Stage4");
        prepare();
    }

    private void prepare() {
        goal = new Goal();
        addDisabledObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();

        ball = new Ball();
        addDisabledObject(ball, 10, 10);

        barrier = new CursorBarrier(ball);
        addDisabledObject(barrier, 0, 0);

        cursor = new Cursor(goal, ball, blocks.toArray(new Block[0]), barrier);
        cursor.addListner(new EventListener() {
            @Override
            public void onDied() {
                super.onDied();
                lose();
            }
        });
        addDisabledObject(cursor, 0, 0);

        Block block;
        int width = getWidth() / 2 - 25 - 300;
        int height = getHeight() / 2 - 25 - 100;

        // ゴールの近くに三角形上にブロックを配置する
        // 左上
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block = new Block();
                blocks.add(block);
                addDisabledObject(block, getWidth() / 2 - 50 - j * 15, 100 + i * 15);
            }
        }
        // 左下
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block = new Block();
                blocks.add(block);
                addDisabledObject(block, getWidth() / 2 - 50 - j * 15, getHeight() - 100 - i * 15);
            }
        }
        // 右上
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block = new Block();
                blocks.add(block);
                addDisabledObject(block, getWidth() / 2 + 50 + j * 15, 100 + i * 15);
            }
        }
        // 右下
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block = new Block();
                blocks.add(block);
                addDisabledObject(block, getWidth() / 2 + 50 + j * 15, getHeight() - 100 - i * 15);
            }
        }

        // ゴールを取り囲む4つの三角形の領域の隙間に、ブロックを置く
        // 右
        for (int i = 0; i < 10; i++) {
            block = new Block();
            blocks.add(block);
            addDisabledObject(block, goal.getX() + 100 + 10 * i, goal.getY());
        }
        // 上
        for (int i = 0; i < 10; i++) {
            block = new Block();
            blocks.add(block);
            addDisabledObject(block, goal.getX(), goal.getY() - 100 - 10 * i);
        }
        // 左
        for (int i = 0; i < 10; i++) {
            block = new Block();
            blocks.add(block);
            addDisabledObject(block, goal.getX() - 100 - 10 * i, goal.getY());
        }
        // 下
        for (int i = 0; i < 10; i++) {
            block = new Block();
            blocks.add(block);
            addDisabledObject(block, goal.getX(), goal.getY() + 100 + 10 * i);
        }
    }

    @Override
    public void win() {
        super.win();
        disableAllActors();
    }

    @Override
    public void lose() {
        super.lose();
        disableAllActors();
    }

    private void disableAllActors() {
        goal.setActorStatus(ActorStatus.DISABLED);
        for (BaseActor block : blocks) {
            block.setActorStatus(ActorStatus.DISABLED);
        }
        ball.setActorStatus(ActorStatus.DISABLED);
        barrier.setActorStatus(ActorStatus.DISABLED);
        cursor.setActorStatus(ActorStatus.DISABLED);
    }

    @Override
    public void onChangeStatus() {
        super.onChangeStatus();
        switch (getWorldStatus()) {
            case PLAYING:
                for (Object actor : getObjects(BaseActor.class)) {
                    ((BaseActor) actor).setActorStatus(ActorStatus.ALIVE);
                }
                break;
        }
    }

}
