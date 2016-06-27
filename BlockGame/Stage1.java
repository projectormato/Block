
import greenfoot.GreenfootImage;
import java.util.ArrayList;

public class Stage1 extends PlayWorld {

    public Stage1() {
        prepare();
    }

    private void prepare() {
        setBackground("background1.jpg");

        Goal goal = new Goal();
        addObject(goal, getWidth() / 2, getHeight() / 2);

        ArrayList<Block> blocks = new ArrayList<>();
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

        Cursor cursor = new Cursor(goal);
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
}
