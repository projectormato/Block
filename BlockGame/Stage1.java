
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class Stage1 extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private Cursor cursor;

    private ArrayList<Double> radianOffsets;

    public Stage1() {
        prepare();
    }

    private void prepare() {
        setBackground("background1.jpg");

        goal = new Goal();
        addObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        radianOffsets = new ArrayList<>();
        relayout();

        ball = new Ball();
        GreenfootImage ballImage = new GreenfootImage("ball.png");
        ballImage.scale(40, 40);
        ball.setImage(ballImage);
        addObject(ball, 10, 10);

        cursor = new Cursor(goal, ball, blocks.toArray(new Block[0]));
        GreenfootImage cursorImage = new GreenfootImage("cursor.png");
        cursorImage.scale(50, 50);
        cursorImage.rotate(90);
        cursor.setImage(cursorImage);
        addObject(cursor, 0, 0);
    }

    /**
     * Blockが時計回りにGoalを公転するアニメーション行う。BlockはGoalに近いほど早く公転する。
     */
    private void relayout() {
        GreenfootImage blockImage = new GreenfootImage("block.png");
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
                    block = new Block();
                    block.setImage(blockImage);
                    blocks.add(block);
                    addObject(block, x, y);
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
        relayout();
    }

    @Override
    public void win() {
        super.win();
        System.out.println("--- STAGE COMPLETE ---");
        Greenfoot.stop();
    }

    @Override
    public void lose() {
        super.lose();
        System.out.println("--- GAME OVER ---");
        Greenfoot.stop();
    }
}
