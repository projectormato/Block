
import java.util.ArrayList;
import java.util.Random;

public class Stage3 extends PlayWorld {

    private final static int BLOCK_SPACE = (int) (70 * 1.44);
    private final static int NUM_OF_BLOCKS = 200;

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;

    public Stage3() {
        super("Stage3");
        prepare();
    }

    private void prepare() {
        setBackground("bg/space.jpg");

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

        // ブロックをランダムな位置に配置
        Random r = new java.util.Random();
        for (int i = 0; i < NUM_OF_BLOCKS; i++) {
            int rx = r.nextInt(getWidth() - BLOCK_SPACE * 2) + BLOCK_SPACE;
            int ry = r.nextInt(getHeight() - BLOCK_SPACE * 2) + BLOCK_SPACE;
            Block block = new Block();
            blocks.add(block);
            addDisabledObject(block, rx, ry);
        }
    }

    @Override
    public void tick() {
        super.tick();
        switch (getWorldStatus()) {
            case STAGE_END_MSG:
                break;
            default:
                //relayout();
                break;
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
