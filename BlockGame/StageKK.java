
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;
import java.util.Random;

public class StageKK extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;

    private ArrayList<Double> radianOffsets;

    public StageKK() {
        super("Stage1");
        prepare();
    }

    private void prepare() {
        setBackground("bg/space.jpg");

        goal = new Goal();
        addDisabledObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        radianOffsets = new ArrayList<>();
        //relayout();

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
        relayout();
    }

    private void relayout() {
        GreenfootImage blockImage = new GreenfootImage("block/b.png");
        blockImage.scale(20, 20);

        final int BLOCK_SPACE = (int) (70 * 1.44);
        Block[] blocks = new Block[200];
        Random r = new java.util.Random();
        for (int i = 0; i < blocks.length; i++) {
            int rx = r.nextInt(getWidth() - BLOCK_SPACE * 2) + BLOCK_SPACE;
            int ry = r.nextInt(getHeight() - BLOCK_SPACE * 2) + BLOCK_SPACE;
            blocks[i] = new Block();
            blocks[i].setImage(blockImage);
            addDisabledObject(blocks[i], rx, ry);
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
