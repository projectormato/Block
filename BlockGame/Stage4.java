
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class Stage4 extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks, blocks1;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;
    private Block block1, block2, block3;

    public Stage4() {
        super("Stage4");
        prepare();
    }

    private void prepare() {
        goal = new Goal();
        addDisabledObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        blocks1 = new ArrayList<>();

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

//        block = new Block();
//        block.setImage(blockImage);
//        blocks.add(block);
//        addDisabledObject(block, 100, 100);
        int width = getWidth() / 2 - 25 - 300;
        int height = getHeight() / 2 - 25 - 100;
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block1 = new Block();
                blocks.add(block1);
                addDisabledObject(block1, getWidth() / 2 - 50 - j * 15, 100 + i * 15);
            }
        }
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block1 = new Block();
                blocks.add(block1);
                addDisabledObject(block1, getWidth() / 2 - 50 - j * 15, getHeight() - 100 - i * 15);
            }
        }
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block2 = new Block();
                blocks.add(block2);
                addDisabledObject(block2, getWidth() / 2 + 50 + j * 15, 100 + i * 15);
            }
        }
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block2 = new Block();
                blocks.add(block2);
                addDisabledObject(block2, getWidth() / 2 + 50 + j * 15, getHeight() - 100 - i * 15);
            }
        }

        for (int i = 0; i < 10; i++) {
            block3 = new Block();
            blocks1.add(block3);
            addDisabledObject(block3, goal.getX() + 100 + 10 * i, goal.getY());
        }
        for (int i = 0; i < 10; i++) {
            block3 = new Block();
            blocks1.add(block3);
            addDisabledObject(block3, goal.getX(), goal.getY() - 100 - 10 * i);
        }
        for (int i = 0; i < 10; i++) {
            block3 = new Block();
            blocks1.add(block3);
            addDisabledObject(block3, goal.getX() - 100 - 10 * i, goal.getY());
        }
        for (int i = 0; i < 10; i++) {
            block3 = new Block();
            blocks1.add(block3);
            addDisabledObject(block3, goal.getX(), goal.getY() + 100 + 10 * i);
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
