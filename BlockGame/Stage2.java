
import greenfoot.GreenfootImage;
import java.util.ArrayList;

/**
 * ゴールの周りを大量のブロックを円形に配置したステージ。 ゴールの周りをブロックが公転するアニメーション有り。
 *
 * @author Hirano
 */
public class Stage2 extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private CursorBarrier barrier;
    private Cursor cursor;

    private ArrayList<Double> radianOffsets;

    public Stage2() {
        super("Stage2");
        prepare();
    }

    private void prepare() {
        goal = new Goal();
        addDisabledObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        radianOffsets = new ArrayList<>();
        relayout();

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
    }

    /**
     * Blockが時計回りにGoalを公転するアニメーション行う。BlockはGoalに近いほど早く公転する。
     */
    private void relayout() {
        GreenfootImage blockImage = new GreenfootImage("block/b.png");
        blockImage.scale(20, 20);

        int minRadius = goal.getWidth();
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
