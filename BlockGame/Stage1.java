
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class Stage1 extends PlayWorld {

    private Goal goal;
    private ArrayList<Block> blocks;
    private Ball ball;
    private Cursor cursor;

    public Stage1() {
        prepare();
    }

    private void prepare() {
        setBackground("background1.jpg");

        goal = new Goal();
        addObject(goal, getWidth() / 2, getHeight() / 2);

        blocks = new ArrayList<>();
        GreenfootImage blockImage = new GreenfootImage("block.png");
        blockImage.scale(20, 20);
        int radius = goal.getImage().getWidth();
        int depth = 100;
        for (int x = 0; x < getWidth(); x += blockImage.getWidth()) {
            for (int y = 0; y < getHeight(); y += blockImage.getHeight()) {
                double d = getDistance(goal, x, y);
                if (radius < d && d < radius + depth) {
                    Block block = new Block();
                    block.setImage(blockImage);
                    blocks.add(block);
                    addObject(block, x, y);
                }
            }
        }

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

    private double getDistance(BaseActor actor1, int x, int y) {
        int dx = actor1.getX() - x;
        int dy = actor1.getY() - y;
        return Math.sqrt(dx * dx + dy * dy);
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
