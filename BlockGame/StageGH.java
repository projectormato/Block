
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class StageGH extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;
    private Block block1,block2;

    private ArrayList<Double> radianOffsets;

    public StageGH() {
        super("Stage1");
        prepare();
    }

    private void prepare() {
        // setBackground(Config.getImage(this.getClass, "bg"));
        setBackground("bg/space.jpg");
        
        GreenfootImage blockImage = Config.getImage(Block.class, "bg");

        goal = new Goal();
        addDisabledObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        radianOffsets = new ArrayList<>();

        ball = new Ball();
        GreenfootImage ballImage = Config.getImage(Ball.class, "bg");
        ball.setImage(ballImage);
        addDisabledObject(ball, 10, 10);

        barrier = new CursorBarrier(ball);
        GreenfootImage barrierImage = Config.getImage(CursorBarrier.class, "bg");
        barrier.setImage(barrierImage);
        addDisabledObject(barrier, 0, 0);

        cursor = new Cursor(goal, ball, blocks.toArray(new Block[0]), barrier);
        GreenfootImage cursorImage = Config.getImage(Cursor.class, "bg");
        cursor.setImage(cursorImage);
        cursor.addListner(new EventListener(){
            @Override
            public void onDied(){
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
                block1.setImage(blockImage);
                blocks.add(block1);
                addDisabledObject(block1, getWidth() / 2 - 50 - j * 15, 100 + i * 15);
            }
        }
        
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block1 = new Block();
                block1.setImage(blockImage);
                blocks.add(block1);
                addDisabledObject(block1, getWidth() / 2 - 50 - j * 15, getHeight()-100 - i * 15);
            }
        }
        
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block2 = new Block();
                block2.setImage(blockImage);
                blocks.add(block2);
                addDisabledObject(block2, getWidth()/2 +50+ j * 15, 100 + i * 15);
            }
        }
        for (int i = 0; i * 15 < height; i++) {
            for (int j = 0; j < i + 1; j++) {
                block2 = new Block();
                block2.setImage(blockImage);
                blocks.add(block2);
                addDisabledObject(block2, getWidth() / 2 + 50 + j * 15, getHeight()-100 - i * 15);
            }
        }
        
    }

    private void update() {

    }

    @Override
    public void tick() {
        super.tick();
        update();
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
