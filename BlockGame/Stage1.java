
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class Stage1 extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;

    private ArrayList<Double> radianOffsets;

    public Stage1() {
        super("Stage1");
        prepare();
    }

    private void prepare() {
        setBackground("bg/space.jpg");

        goal = new Goal();
        addDisabledObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        radianOffsets = new ArrayList<>();
        relayout();

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
    }

    /**
     * Blockが時計回りにGoalを公転するアニメーション行う。BlockはGoalに近いほど早く公転する。
     */
    private void relayout() {
        GreenfootImage blockImage = new GreenfootImage("block/b.png");
        blockImage.scale(20, 20);

        int minRadius = goal.getImage().getWidth();
        int maxRadius = minRadius + 200;
        int dRadius = (int) (20 * 1.44);
        int i = 0;
        int j = 0;
        for (int radius = minRadius; radius < maxRadius; radius += dRadius) {
            double dRadian = (2 * Math.PI) / (2 * radius * Math.PI / blockImage.getWidth());
            double radianOffset = 0;
            try {
                radianOffset = radianOffsets.get(j);
            } catch (IndexOutOfBoundsException e) {
                // 初回呼び出し時は、offsetの初期値をセット。
                radianOffsets.add(0.0);
            }
            for (double radian = 0; radian < 2 * Math.PI; radian += dRadian) {
                int x = goal.getX() + (int) (Math.cos(radian + radianOffset) * radius);
                int y = goal.getY() + (int) (Math.sin(radian + radianOffset) * radius);

                Block block;
                try {
                    block = blocks.get(i);
                    block.setLocation(x, y);
                } catch (IndexOutOfBoundsException e) {
                    // 初回呼び出し時は、Blockを新規作成する。
                    // この時は、メッセージが表示されるはずなので、disabledにしておく。
                    block = new Block();
                    block.setImage(blockImage);
                    blocks.add(block);
                    addDisabledObject(block, x, y);
                }
                i++;
            }

            // offsetを更新
            radianOffsets.set(j, radianOffset + Math.toRadians(10000.0 / (radius * radius)));
            j++;
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
    public boolean preChangeStatus(PlayWorldStatus newStatus) {
        if (getWorldStatus() == PlayWorldStatus.WAITING && newStatus == PlayWorldStatus.PLAYING) {
            // カーソルとブロックが衝突している状態なら、プレイを開始しない
            if (cursor.getIntersectingObjects(Block.class).size() > 0) {
                return false;
            }
        }
        return super.preChangeStatus(newStatus);
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
