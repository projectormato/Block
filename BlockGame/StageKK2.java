
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class StageKK2 extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;

    private ArrayList<Double> radianOffsets;

    public StageKK2() {
        super("Stage1");
        prepare();
    }

    private void prepare() {
        setBackground("bg/space.jpg");

        goal = new Goal();
        addDisabledObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        radianOffsets = new ArrayList<>();

        ball = new Ball();
        GreenfootImage ballImage = Config.getImage(ball.getClass(), "bg");
        ball.setImage(ballImage);
        addDisabledObject(ball, 10, 10);

        barrier = new CursorBarrier(ball);
        GreenfootImage barrierImage = Config.getImage(barrier.getClass(), "bg");
        barrier.setImage(barrierImage);
        addDisabledObject(barrier, 0, 0);

        cursor = new Cursor(goal, ball, blocks.toArray(new Block[0]), barrier);
        GreenfootImage cursorImage = Config.getImage(cursor.getClass(), "bg");
        cursor.setImage(cursorImage);
        cursor.addListner(new EventListener() {
            @Override
            public void onDied() {
                super.onDied();
                lose();
            }
        });
        addDisabledObject(cursor, 0, 0);
        relayout();
    }

    /**
     * Blockが時計回りにGoalを公転するアニメーション行う。BlockはGoalに近いほど早く公転する。
     */
    private void relayout() {
        final int BLOCK_SPACE = (int) (70 * 1.44);
        GreenfootImage blockImage = new GreenfootImage("block/b.png");
        blockImage.scale(20, 20);
        int[][] field
                = {
                    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,},
                    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,},
                    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,},
                    {0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0,},
                    {0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0,},
                    {0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0,},
                    {0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0,},
                    {0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0,},};

        Block[] blocks = new Block[200];
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (field[y][x] == 1) {
                    Block block = new Block();
                    blocks[x + y * field.length] = block;
                    block.setImage(blockImage);
                    addDisabledObject(block, x * 25 + BLOCK_SPACE, getHeight() - BLOCK_SPACE - field.length * 60 + y * 40);
                }
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
                //relayout();
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
