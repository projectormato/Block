
import java.util.ArrayList;
import java.util.Random;

public class StageKK3 extends PlayWorld {

    final private int MAX_NEXT_TURN_TIME = 400;

    private Goal goal;
    private ArrayList<Block> blocks = new ArrayList<>();
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;

    private Block[] random_blocks = new Block[200];
    private Block[][] goal_guard = new Block[20][20];
    private int count = 0;
    // 次に方向を変える時間
    private int nextTurnTime = 300;

    int width = getWidth() / 2;
    int height = getHeight() / 2;
    int BLOCK_SPACE = (int) (70 * 1.44);

    public StageKK3() {
        super("Stage1");
        prepare();
    }

    private void prepare() {
        Random r = new java.util.Random();
        goal = new Goal();
        addDisabledObject(goal, width, height);

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

        // ランダムにブロックを配置する
        for (int i = 0; i < random_blocks.length; i++) {
            int rx = r.nextInt(getWidth());
            int ry = r.nextInt(getHeight());
            // random_blocks[i] = new StrongBlock();
            random_blocks[i] = new Block();
            random_blocks[i].turn(r.nextInt(360));
            blocks.add(random_blocks[i]);
            addDisabledObject(random_blocks[i], rx, ry);
        }
        // ゴールの周囲にブロックを配置する
        for (int i = 0; i < goal_guard.length; i++) {
            for (int j = 0; j < goal_guard[i].length; j++) {
                goal_guard[i][j] = new Block();
                blocks.add(goal_guard[i][j]);
                addDisabledObject(goal_guard[i][j], width - 200 + i * 20, height + 200 - j * 20);
            }

        }
    }

    private void relayout() {
        if (getWorldStatus() == PlayWorldStatus.PLAYING) {
            count++;

            // 定期的に方向を変える
            if (count % nextTurnTime == 0) {
                Random r = new Random();
                count = r.nextInt(MAX_NEXT_TURN_TIME);
                for (Block block : random_blocks) {
                    block.turn(r.nextInt(360));
                }
            }
            //  動かす
            for (Block block : random_blocks) {
                block.move(1);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        switch (getWorldStatus()) {
            case STAGE_END_MSG:
                break;
            default:
                relayout();
                break;
        }
    }

    @Override
    public void win() {
        super.win();
        System.out.println("--- STAGE COMPLETE ---");
        disableAllActors();
    }

    @Override
    public void lose() {
        super.lose();
        System.out.println("--- GAME OVER ---");
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
