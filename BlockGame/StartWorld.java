
import greenfoot.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * スタート画面を表示する。
 */
public class StartWorld extends BaseWorld {

    public StartWorld() {
        logger = Logger.getLogger("SampleLogging");
        if (ENABLE_LOGGING) {
            try {
                FileHandler fh = new FileHandler(LOG_FILE, true);
                fh.setFormatter(new java.util.logging.SimpleFormatter());
                logger.addHandler(fh);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                // とにかく実行を中断
                throw new AssertionError();
            }
        }

        prepare();
    }

    /**
     * Prepare the world for the start of the program. That is: create the
     * initial objects and add them to the world.
     */
    private void prepare() {
        GreenfootImage goalimage = new GreenfootImage("goal.png");
        goalimage.scale(300, 300);

        Goal goal = new Goal();
        addObject(goal, 390, 391);
        goal.setImage(goalimage);
    }
}
